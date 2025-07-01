<%@ page import="com.webapp.model.Resume" %>
<%@ page import="com.webapp.model.ContactType" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section class="container">
    <h1>Список всех резюме</h1>
    <div class="section">
        <table>
            <thead>
                <tr>
                    <th>Имя</th>
                    <th>Email</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${resumes}" var="resume">
                    <jsp:useBean id="resume" type="com.webapp.model.Resume"/>
                    <tr>
                        <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                        <td><%=ContactType.MAIL.toHtml(resume.getContact(ContactType.MAIL))%></td>
                        <td>
                            <a class="button" href="resume?uuid=${resume.uuid}&action=delete">Удалить</a>
                            <a class="button" href="resume?uuid=${resume.uuid}&action=edit">Редактировать</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="button-container">
        <a class="button" href="resume?action=add">Добавить нового сотрудника</a>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>