package com.webapp.web;

import com.webapp.Config;
import com.webapp.model.ContactType;
import com.webapp.model.Resume;
import com.webapp.model.TypeSection;
import com.webapp.storage.Storage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final Storage storage = Config.get().getStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Writer writer = response.getWriter();

        writer.write(
                "<html>\n" +
                        "<head>\n" +
                        "<meta http-equiv=\"Content-Type\"content=\"text/html; charset=UTF-8\">\n" +
                        "<link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                        "<title>Список резюме</title>\n," +
                        "</head>\n" +
                        "<body>\n" +
                        "<section>\n" +
                        "<table border=\"1\" cellpadding=\"8\" cellspacing=\"0\">\n" +
                        "<tr>\n" +
                        "<th>full_name</th>\n" +
                        "<th>MAIL</th>\n" +
                        "<th>PHONE</th>\n" +
                        "<th>HOME_PAGE</th>\n" +
                        "<th>PERSONAL</th>\n" +
                        "<th>ACHIEVEMENT</th>\n" +
                        "</tr>\n"
        );
        for (Resume resume : storage.getAllSorted()) {
            writer.write(
                    "<tr>\n" +
                            "<td><a href=\"resume?uuid=" + resume.getUuid() + "\">" +  resume.getFullName() + "</a></td>\n" +
                            "<td>" + resume.getContact(ContactType.MAIL) + "</td>\n" +
                            "<td>" + resume.getContact(ContactType.PHONE) + "</td>\n" +
                            "<td>" + resume.getContact(ContactType.HOME_PAGE) + "</td>\n" +
                            "<td>" + resume.getSections(TypeSection.PERSONAL) + "</td>\n" +
                            "<td>" + resume.getSections(TypeSection.ACHIEVEMENT) + "</td>\n" +
                            "</tr>\n");
        }

        writer.write(
                "</table>\n" +
                        "</section>\n" +
                        "</body>\n" +
                        "</html>");
    }
}