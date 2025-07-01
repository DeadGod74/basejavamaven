package com.webapp.web;

import com.webapp.model.Achievement;
import com.webapp.Config;
import com.webapp.model.*;
import com.webapp.sql.sqlCon;
import com.webapp.storage.Storage;
import com.webapp.util.JsonParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.webapp.util.LogUtil;

@WebServlet("/resumeList")

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();
    private sqlCon databaseConnection = new sqlCon();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.storage = Config.get().getStorage();
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;

        List<Company> companies = new ArrayList<>();

        final boolean isCreate = (uuid == null || uuid.length() == 0);
        if (isCreate) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }

        String personalInfo = request.getParameter("personalInfo");
        String objectives = request.getParameter("objectives");
        String[] achievementsArray = request.getParameterValues("achievements");
        String[] qualificationsArray = request.getParameterValues("qualifications");
        String newType = request.getParameter("newType");
        String newContact = request.getParameter("newContact");
        LogUtil.logInfo(String.format("newType parameters: item%s", newType));
        LogUtil.logInfo(String.format("newContact parameters: item%s", newContact));
        String action = request.getParameter("action");

        if (newType != null && !newType.isEmpty() && newContact != null && !newContact.isEmpty()) {
            resume.setContact(ContactType.valueOf(newType), newContact);
        }
        resume.setSection(TypeSection.PERSONAL, new TextSection(personalInfo));
        resume.setSection(TypeSection.OBJECTIVE, new TextSection(objectives));

        // Перебираем все компании
        int companyIndex = 0;
        while (true) {
            String companyName = request.getParameter("companyName_" + companyIndex);
            String website = request.getParameter("companyWebsite_" + companyIndex);

            if (companyName == null || website == null) {
                break; // Если нет больше компаний, выходим из цикла
            }

            List<Period> periods = new ArrayList<>();
            int periodIndex = 0;

            // Перебираем все периоды для текущей компании
            while (true) {
                String name = request.getParameter("name_" + companyIndex + "_" + periodIndex);
                String startDateStr = request.getParameter("startDate_" + companyIndex + "_" + periodIndex);
                String endDateStr = request.getParameter("endDate_" + companyIndex + "_" + periodIndex);
                String description = request.getParameter("description_" + companyIndex + "_" + periodIndex);

                if (name == null && startDateStr == null && endDateStr == null) {
                    break; // Если нет больше периодов, выходим из цикла
                }
                // Создание объекта Period и добавление в список
                Period period = new Period(LocalDate.parse(startDateStr), LocalDate.parse(endDateStr), name, description);
                periods.add(period);
                periodIndex++;
            }

            // Создание объекта Company и добавление в список
            Company company = new Company(companyName, website, periods);
            companies.add(company);
            companyIndex++;
        }

        // Обработка новой компании из формы
        String newCompanyName = request.getParameter("newCompanyName");
        String newCompanyWebsite = request.getParameter("newCompanyWebsite");

        // Проверка, что новая компания была введена
        if (newCompanyName != null && newCompanyWebsite != null && !newCompanyName.isEmpty() && !newCompanyWebsite.isEmpty()) {
            List<Period> newPeriods = new ArrayList<>();

            // Получаем данные для нового периода
            String newPeriodName = request.getParameter("newPeriodName");
            String newPeriodStartDateStr = request.getParameter("newPeriodStartDate");
            String newPeriodEndDateStr = request.getParameter("newPeriodEndDate");
            String newPeriodDescription = request.getParameter("newPeriodDescription");

            // Проверка, что данные периода введены
            if (newPeriodName != null && newPeriodStartDateStr != null && newPeriodEndDateStr != null &&
                    !newPeriodName.isEmpty() && !newPeriodStartDateStr.isEmpty() && !newPeriodEndDateStr.isEmpty()) {
                Period newPeriod = new Period(
                        LocalDate.parse(newPeriodStartDateStr),
                        LocalDate.parse(newPeriodEndDateStr),
                        newPeriodName,
                        newPeriodDescription
                );
                newPeriods.add(newPeriod);
            }
            Company newCompany = new Company(newCompanyName, newCompanyWebsite, newPeriods);
            companies.add(newCompany);
        }
        resume.setSection(TypeSection.EXPERIENCE, new CompanySection(companies));

        List<String> filteredAchievements = Arrays.stream(achievementsArray)
                .filter(achievement -> achievement != null && !achievement.trim().isEmpty())
                .collect(Collectors.toList());
        resume.setSection(TypeSection.ACHIEVEMENT, new ListSection(filteredAchievements));

        List<String> filteredQualifications = Arrays.stream(qualificationsArray)
                .filter(qualification -> qualification != null && !qualification.trim().isEmpty())
                .collect(Collectors.toList());
        resume.setSection(TypeSection.QUALIFICATIONS, new ListSection(filteredQualifications));


LogUtil.logInfo(String.format("resume full: item%s", resume));
        if (isCreate) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }

        if (uuid.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/resume");
        } else if ("saveForm1".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/resume?uuid=" + uuid + "&action=edit");
        } else {
            response.sendRedirect(request.getContextPath() + "/resume?uuid=" + uuid + "&action=view");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", this.storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);

        } else {
            StringBuilder personalInfoBuilder = new StringBuilder();
            StringBuilder objectivesInfoBuilder = new StringBuilder();
            List<String> experiencesWeb = new ArrayList<>();
            List<String> experiences = new ArrayList<>();
            List<String> achievements = new ArrayList<>();
            List<String> qualifications = new ArrayList<>();
            Resume resume;
            if (uuid == null || uuid.isEmpty()) {
                resume = Resume.EMPTY;
            } else {
                List<Achievement> personUuid = databaseConnection.getAchievementsFromDatabase(uuid);
                resume = this.storage.get(uuid);
                resume.setUuid(uuid);

                switch (action) {
                    case "delete":
                        this.storage.delete(uuid);
                        response.sendRedirect("resume");
                        return;
                    case "add":
                        break;
                    case "view":
                    case "edit":
                        for (Achievement achievement : personUuid) {
                            switch (achievement.getType()) {
                                case "PERSONAL":
                                    personalInfoBuilder.append(achievement.getContent());
                                    break;
                                case "OBJECTIVE":
                                    objectivesInfoBuilder.append(achievement.getContent());
                                    break;
                                case "ACHIEVEMENT":
                                    ListSection listSection = new ListSection(achievement.getContent());
                                    String json = listSection.toString();
                                    if (listSection instanceof ListSection) {
                                        ListSection deserialized = JsonParser.read(json, ListSection.class);
                                        for (String des : deserialized.getItems()) {
                                            achievements.add(des);
                                        }
                                    }
                                    break;
                                case "QUALIFICATIONS":
                                    ListSection listSectionQ = new ListSection(achievement.getContent());
                                    String jsonQ = listSectionQ.toString();
                                    if (listSectionQ instanceof ListSection) {
                                        ListSection deserialized = JsonParser.read(jsonQ, ListSection.class);
                                        for (String des : deserialized.getItems()) {
                                            qualifications.add(des);
                                        }
                                    }
                                    break;
                                case "EXPERIENCE":
                                    CompanySection companySection = JsonParser.read(achievement.getContent(), CompanySection.class);
                                    List<Company> companies = companySection.getCompanies();
                                    if (companies != null && !companies.isEmpty()) {
                                        // Проверяем, есть ли компании в списке
                                        Company firstCompany = companies.get(0); // Получаем первую компанию
                                        List<Period> periods = firstCompany.getPeriods();
                                        if (periods != null && !periods.isEmpty()) {
                                            // Проверяем, есть ли периоды у первой компании
                                            request.setAttribute("companies", companies);
                                            request.setAttribute("period", periods);
                                        } else {
                                            // Обработка случая, когда нет периодов
                                            request.setAttribute("companies", companies);
                                            request.setAttribute("period", Collections.emptyList());
                                        }
                                    } else {
                                        // Обработка случая, когда нет компаний
                                        request.setAttribute("companies", Collections.emptyList());
                                        request.setAttribute("period", Collections.emptyList());
                                    }
                                    break;
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Action " + action + " is illegal");
                }
            }
// Проверка на пустоту для personalInfo
            String personalInfoText = personalInfoBuilder.toString();
            if (personalInfoText.isEmpty()) {
                personalInfoText = "";
            }
            TextSection personalInfo = new TextSection(personalInfoText);

// Проверка на пустоту для objectives
            String objectivesText = objectivesInfoBuilder.toString();
            if (objectivesText.isEmpty()) {
                objectivesText = "";
            }
            TextSection objectives = new TextSection(objectivesText);

            request.setAttribute("personalInfo", personalInfo);
            request.setAttribute("objectives", objectives);
            request.setAttribute("achievements", achievements);
            request.setAttribute("qualifications", qualifications);
            request.setAttribute("experiences", experiences);
            request.setAttribute("experiencesWeb", experiencesWeb);
            request.setAttribute("resume", resume);

            String targetPage;

            if ("view".equals(action)) {
                targetPage = "/WEB-INF/jsp/view.jsp";
            } else if ("list".equals(action)) {
                targetPage = "/WEB-INF/jsp/list.jsp";
            } else {
                targetPage = "/WEB-INF/jsp/edit.jsp";
            }

            request.getRequestDispatcher(targetPage).forward(request, response);
        }
    }
}
