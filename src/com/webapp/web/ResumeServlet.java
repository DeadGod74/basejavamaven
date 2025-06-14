package com.webapp.web;

import com.webapp.Config;
import com.webapp.exception.NotExistStorageException;
import com.webapp.model.*;
import com.webapp.storage.Storage;
import com.webapp.util.DateUtil;
import com.webapp.util.HtmlUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (HtmlUtil.isEmpty(value)) {
                r.getContacts().remove(type);
            } else {
                r.setContact(type, value);
            }
        }
        for (TypeSection type : TypeSection.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections(type).remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.setSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.setSection(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Company> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Period> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Period(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                if (!positions.isEmpty()) {
                                    orgs.add(new Company(name, urls[i], positions));
                                }
                            }
                        }
                        r.setSection(type, new CompanySection(orgs));
                        break;
                }
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume r;
        try {
            switch (action) {
                case "delete":
                    storage.delete(uuid);
                    response.sendRedirect("resume");
                    return;

                case "view":
                case "edit":
                    r = storage.get(uuid);
                    if ("edit".equals(action)) {
                        prepareEditResume(r);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Action " + action + " is illegal");
            }

            request.setAttribute("resume", r);
            String jspPage = "view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp";
            request.getRequestDispatcher(jspPage).forward(request, response);

        } catch (NotExistStorageException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void prepareEditResume(Resume r) {
        for (TypeSection type : new TypeSection[]{TypeSection.EXPERIENCE, TypeSection.EDUCATION}) {
            CompanySection section = (CompanySection) r.getSection(type);
            List<Company> emptyFirstOrganizations = new ArrayList<>();
            emptyFirstOrganizations.add(new Company("Empty Company", "http://empty.com", new ArrayList<>())); // Создаем пустую компанию

            if (section != null && section.getCompanies() != null) {
                for (Company org : section.getCompanies()) {
                    List<Period> emptyFirstPeriods = new ArrayList<>();
                    emptyFirstPeriods.add(new Period(null, null, null, null)); // Добавляем пустой период или создайте свой способ для пустого периода
                    emptyFirstPeriods.addAll(org.getPeriods()); // Используем существующий метод getPeriods
                    emptyFirstOrganizations.add(new Company(org.getNameCompany(), org.getWebsite(), emptyFirstPeriods));
                }
            }

            r.setSection(type, new CompanySection(emptyFirstOrganizations));
        }
    }

}