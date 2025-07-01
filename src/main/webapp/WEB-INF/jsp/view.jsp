<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.webapp.model.TextSection" %>
<%@ page import="com.webapp.model.ListSection" %>
<%@ page import="com.webapp.model.CompanySection" %>
<%@ page import="com.webapp.model.ContactType" %>
<%@ page import="com.webapp.util.HtmlUtil" %>
<%@ page import="com.webapp.model.TypeSection" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.webapp.model.Resume" scope="request"/>
    <title>Резюме ${not empty resume.fullName ? resume.fullName : 'Без имени'}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<h3>Резюме: ${not empty resume.fullName ? resume.fullName : 'Без имени'}</h3>

<div class="container">
<div class="section">
    <h1>Контакты</h1>
    <ul class="without-bullets">
        <%
            Map<ContactType, String> contacts = resume.getContacts();
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                ContactType type = entry.getKey();
                String contact = entry.getValue();
                if (contact != null && !contact.isEmpty()) {
        %>
                    <li>
                    <label for="<%= type.name() %>"><%= type.name() %>:</label>
                    <%= contact %>
                    </li>
        <%
                }
            }
        %>
    </ul>
</div>

    <div class="section">
        <h1>Личные качества</h1>
        <ul class="without-bullets">
            <li>${personalInfo.getTextRepresentation().replace('"', '')}</li>
        </ul>
    </div>

    <div class="section">
        <h1>Позиция</h1>
         <ul class="without-bullets">
                <li>${objectives.getTextRepresentation().replace('"', '')}</li>
         </ul>
    </div>

    <div class="section">
        <h1>Достижения</h1>
        <ul class="with-bullets">
            <c:forEach var="achievement" items="${achievements}">
                <li>${achievement}</li>
            </c:forEach>
        </ul>
    </div>

    <div class="section">
        <h1>Квалификация</h1>
        <ul class="with-bullets">
            <c:forEach var="qualification" items="${qualifications}">
                <li>${qualification}</li>
            </c:forEach>
        </ul>
    </div>
<div class="section">
    <h1>Опыт</h1>
    <ul class="with-bullets">
        <c:forEach var="company" items="${companies}">
            <li>
                <strong>${company.nameCompany}</strong><br>
                <a href="${company.website}" target="_blank">${company.website}</a>
                <ul>
                    <c:forEach var="period" items="${company.periods}">
                        <li>
                            <strong>${period.name}</strong><br>
                            Даты: ${period.startDate} - ${period.endDate}<br>
                            Описание: ${period.description}
                        </li>
                    </c:forEach>
                </ul>
            </li>
        </c:forEach>
    </ul>
</div>


    <!-- Кнопки "Назад" и "Редактировать" -->
    <div class="button-container">
        <button class="button" onclick="window.history.back()">Назад</button>
        <a href="<c:url value='/resume'/>?uuid=${resume.uuid}&action=edit" class="button">Редактировать</a>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
