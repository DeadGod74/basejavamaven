package com.webapp.storage;

import com.webapp.exception.NotExistStorageException;
import com.webapp.model.*;
import com.webapp.sql.SqlHelper;
import com.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;


public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    public Resume get(String uuid) {
        Resume resume = sqlHelper.execute("SELECT * FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new NotExistStorageException(uuid);
                        }
                        return new Resume(uuid, rs.getString("full_name"));
                    }
                });

        sqlHelper.execute("SELECT * FROM contact WHERE resume_uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            addContact(rs, resume);
                        }
                    }
                    return null;
                });

        sqlHelper.execute("SELECT * FROM section WHERE resume_uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            addSection(rs, resume);
                        }
                    }
                    return null;
                });

        return resume;
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            updateResume(conn, resume);
            deleteContacts(conn, resume.getUuid());
            insertContact(conn, resume);
            deleteSections(conn, resume);
            insertSections(conn, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    insertContact(conn, resume);
                    insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> resumes = new LinkedHashMap<>();

        executeQuery("SELECT * FROM resume ORDER BY full_name, uuid", rs -> {
            try {
                String uuid = rs.getString("uuid");
                resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
            } catch (SQLException e) {
                throw new RuntimeException("Error reading resume from ResultSet", e);
            }
        });

        executeQuery("SELECT * FROM contact", rs -> {
            try {
                Resume r = resumes.get(rs.getString("resume_uuid"));
                if (r != null) {
                    addContact(rs, r);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error reading contact from ResultSet", e);
            }
        });

        executeQuery("SELECT * FROM section", rs -> {
            try {
                Resume r = resumes.get(rs.getString("resume_uuid"));
                if (r != null) {
                    addSection(rs, r);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error reading section from ResultSet", e);
            }
        });

        return Arrays.asList(resumes.values().toArray(new Resume[0]));
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        });
    }

    private void insertContact(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) " +
                "VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate(); // Используем executeUpdate для получения количества удаленных строк
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            try {
                ContactType type = ContactType.valueOf(rs.getString("type"));
                resume.addContact(type, value);
            } catch (IllegalArgumentException e) {
                System.err.println("Неверный тип контакта: " + rs.getString("type"));
            }
        }
    }

    private void updateResume(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE resume " +
                "SET full_name = ? " +
                "WHERE uuid = ?")) {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() != 1) {
                throw new NotExistStorageException(resume.getUuid());
            }
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            TypeSection type = TypeSection.valueOf(rs.getString("type"));
            r.addSection(type, new TextSection(content));
        }
    }

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<TypeSection, Section> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                Section section = e.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void executeQuery(String query, Consumer<ResultSet> resultSetConsumer) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultSetConsumer.accept(rs);
                }
            }
            return null;
        });
    }
}