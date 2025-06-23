<%@ page import="com.webapp.model.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="${ContactType.values()}">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="type" items="${TypeSection.values()}">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <h2>${type.title}</h2>

            <c:choose>
                <c:when test="${type == TypeSection.OBJECTIVE}">
                    <input type='text' name='${type.name()}' size='75' value='${section != null ? section : ''}'/>
                </c:when>
                <c:when test="${type == TypeSection.PERSONAL}">
                    <textarea name='${type.name()}' cols='75' rows='5'>${section != null ? section : ''}</textarea>
                </c:when>
                <c:when test="${type == TypeSection.QUALIFICATIONS || type == TypeSection.ACHIEVEMENT}">
                    <textarea name='${type.name()}' cols='75' rows='5'>
                        <c:forEach var="item" items="${items}">
                            <li>${item}</li>
                        </c:forEach>
                    </textarea>
                </c:when>
                <c:when test="${type == TypeSection.EXPERIENCE || type == TypeSection.EDUCATION}">
                                    <c:forEach var="org" items="<%=((CompanySection) section).getCompanies()%>"
                                               varStatus="counter">
                                        <dl>
                                            <dt>Название учереждения:</dt>
                                            <dd><input type="text" name='${type}' size=100 value="${org.getNameCompany()}"></dd>
                                        </dl>
                                        <dl>
                                            <dt>Сайт учереждения:</dt>
                                            <dd><input type="text" name='${type}url' size=100 value="${org.getWebsite()}"></dd>
                                            </dd>
                                        </dl>
                                        <br>
                                    </c:forEach>
                                </c:when>
                <c:otherwise>
                    <p>Неизвестный тип секции: ${type.title}</p>
                </c:otherwise>
            </c:choose>

        </c:forEach>

        <input type="submit" value="Сохранить">
    </form>
</section>
</body>