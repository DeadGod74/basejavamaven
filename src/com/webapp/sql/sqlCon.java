package com.webapp.sql;

import com.webapp.model.Achievement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sqlCon {
    public List<Achievement> getAchievementsFromDatabase(String uuid) {
        List<Achievement> achievements = new ArrayList<>();

        // Подключение к базе данных
        String url = "jdbc:postgresql://localhost:5432/resumes";
        String user = "postgres";
        String password = "123";

        String query = "SELECT type, content FROM section s WHERE s.resume_uuid = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uuid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String type = resultSet.getString("type");
                    String content = resultSet.getString("content");
                    achievements.add(new Achievement(type, content));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achievements;
    }
}
