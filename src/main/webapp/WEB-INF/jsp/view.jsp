<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.webapp.model.TextSection" %>
<%@ page import="com.webapp.model.ListSection" %>
<%@ page import="com.webapp.model.CompanySection" %>
<%@ page import="com.webapp.util.HtmlUtil" %>
<%@ page import="com.webapp.util.SectionUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
<hr>
    <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.webapp.model.TypeSection, com.webapp.model.Section>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.webapp.model.Section"/>
            <tr>
                <td colspan="2"><h2><a name="type.name">${type.title}</a></h2></td>
            </tr>
<c:choose>
    <c:when test="${type == 'OBJECTIVE'}">
        <tr>
           <td colspan="2">
               <h3><%=((TextSection) section).getTextRepresentation()%></h3>
           </td>
        </tr>
    </c:when>
    <c:when test="${type == 'PERSONAL'}">
        <tr>
           <td colspan="2">
               <h3><%=((TextSection) section).getTextRepresentation()%></h3>
           </td>
        </tr>
    </c:when>
    <c:when test="${type == 'QUALIFICATIONS' || type == 'ACHIEVEMENT'}">
          <tr>
                     <td colspan="2">
                         <h3><%=((ListSection) section).getTextRepresentation()%></h3>
                     </td>
                  </tr>
    </c:when>
    <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
        <tr>
                            <td colspan="2">
                                <h3><%=((TextSection) section).getTextRepresentation()%></h3>
                            </td>
                         </tr>
    </c:when>
</c:choose>
        </c:forEach>
    </table>
    <br/>
    <button onclick="window.history.back()">ОК</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>