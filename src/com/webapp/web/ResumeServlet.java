package com.webapp.web;

import com.webapp.Config;
import com.webapp.exception.NotExistStorageException;
import com.webapp.model.*;
import com.webapp.storage.Storage;
import com.webapp.util.DateUtil;
import com.webapp.util.HtmlUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            if (HtmlUtil.isEmpty(value) && (values == null || values.length == 0)) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.setSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        if (values != null && values.length > 0) {
                            r.setSection(type, new ListSection(values));
                        } else {
                            r.setSection(type, new ListSection(new String[0]));
                        }
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Company> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");

                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                String name = values[i];
                                if (!HtmlUtil.isEmpty(name)) {
                                    List<Period> positions = new ArrayList<>();
                                    String pfx = type.name() + i;
                                    String[] startDates = request.getParameterValues(pfx + "startDate");
                                    String[] endDates = request.getParameterValues(pfx + "endDate");
                                    String[] titles = request.getParameterValues(pfx + "title");
                                    String[] descriptions = request.getParameterValues(pfx + "description");

                                    if (titles != null && startDates != null && endDates != null && descriptions != null) {
                                        for (int j = 0; j < titles.length; j++) {
                                            if (!HtmlUtil.isEmpty(titles[j])) {
                                                positions.add(new Period(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                            }
                                        }
                                    }

                                    if (!positions.isEmpty()) {
                                        String url = (urls != null && i < urls.length) ? urls[i] : null;
                                        orgs.add(new Company(name, url, positions));
                                    }
                                }
                            }
                        }

                        r.setSection(type, new CompanySection(orgs));
                        break;
                }
            }
        }

        storage.update(r);

        List<Company> companyList = ((CompanySection) r.getSections().get(TypeSection.EXPERIENCE)).getCompanies();
        request.setAttribute("org.companyList", companyList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/view.jsp");
        dispatcher.forward(request, response);
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
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            case "edit":
                r = storage.get(uuid);
                for (TypeSection type : TypeSection.values()) {
                    Section section = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            if (section == null) {
                                section = new CompanySection(new ArrayList<>());
                            }
                            break;
                    }
                    r.setSection(type, section);

                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}