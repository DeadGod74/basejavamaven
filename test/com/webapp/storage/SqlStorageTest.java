package com.webapp.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webapp.Config;
import com.webapp.model.*;
import com.webapp.util.JsonParser;
import org.junit.Test;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.get().getStorage());
    }

    @Test
    public void testListSectionSerialization() {
        // Получаем достижения из базы данных
        List<String> uuids = getFromDatabase();
        for (String uuid : uuids) {
            List<Achievement> personUuid = getAchievementsFromDatabase(uuid);
            //System.out.println(personUuid);
            for (Achievement achievement : personUuid) {
                if ("PERSONAL".equals(achievement.getType()) || "OBJECTIVE".equals(achievement.getType())) {
                    //System.out.println(achievement.getContent()); // Выводим только content
                }
                if ("ACHIEVEMENT".equals(achievement.getType()) || "QUALIFICATIONS".equals(achievement.getType())) {
                    //System.out.println(achievement.getContent());
                    ListSection listSection = new ListSection(achievement.getContent());
                    String json = listSection.toString();
                    if (listSection instanceof ListSection) {
                        ListSection deserialized = JsonParser.read(json, ListSection.class);
                        //System.out.println("Type of deserialized object: " + deserialized.getClass().getName());

                        //System.out.println("Deserialized ListSection: " + deserialized);
                        for (String des : deserialized.getItems()) {
                            //System.out.println(des);
                        }
                    } else {
                        //System.out.println("не верный класс: " + listSection.getClass());
                    }
                }
                if ("EDUCATION".equals(achievement.getType()) || "EXPERIENCE".equals(achievement.getType())) {
                    System.out.println("Тестируем компани секшион "+ achievement.getContent());
                    // Десериализация JSON в объект CompanySection
                    CompanySection companySection = JsonParser.read(achievement.getContent(), CompanySection.class);

                    // Вывод информации о компаниях
                    for (Company company : companySection.getCompanies()) {
                        System.out.println(company.getClass());
                        System.out.println("Название: " + company.getNameCompany());
                        System.out.println("Вебсайт: " + company.getWebsite());
                        System.out.println("Periods: " + company.getPeriods());
                        List<Period> newL = company.getPeriods();
                        for (Period period : newL) {
                            System.out.println("  - Период: " + period.getName());
                            System.out.println("    Даты: " + period.getStartDate() + " - " + period.getEndDate());
                            System.out.println("    Описание: " + period.getDescription());
                        }
                    }
                }
            }
        }

    }

    private List<String> getFromDatabase() {
        List<String> uuid = new ArrayList<>();
        // Здесь подключение к базе данных и выполнение SQL-запроса
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/resumes", "postgres", "123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT uuid FROM resume")) {

            while (resultSet.next()) {
                uuid.add(resultSet.getString("uuid"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений
        }
        return uuid;
    }

    private List<Achievement> getAchievementsFromDatabase(String uuid) {
        List<Achievement> achievements = new ArrayList<>();

        // Подключение к базе данных
        String url = "jdbc:postgresql://localhost:5432/resumes";
        String user = "postgres";
        String password = "123";

        String query = "SELECT type, content FROM section s WHERE s.resume_uuid = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Установка параметра uuid
            preparedStatement.setString(1, uuid);

            // Выполнение запроса
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String type = resultSet.getString("type");
                    String content = resultSet.getString("content");
                    achievements.add(new Achievement(type, content));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений, например, логирование
        }

        return achievements;
    }




}