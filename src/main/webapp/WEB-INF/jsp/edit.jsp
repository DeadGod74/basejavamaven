<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.webapp.model.TextSection" %>
<%@ page import="com.webapp.model.ListSection" %>
<%@ page import="com.webapp.model.CompanySection" %>
<%@ page import="com.webapp.util.HtmlUtil" %>
<%@ page import="com.webapp.model.TypeSection" %>
<%@ page import="com.webapp.model.ContactType" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.webapp.model.Resume" scope="request"/>

<script>
        window.onload = function() {
            var personalInfoTextarea = document.getElementsByName("personalInfo")[0];
            if (personalInfoTextarea) {
                personalInfoTextarea.value = personalInfoTextarea.value.replace(/"/g, "");
            }

            var objectivesTextarea = document.getElementsByName("objectives")[0];
            if (objectivesTextarea) {
                objectivesTextarea.value = objectivesTextarea.value.replace(/"/g, "");
            }
        };
    </script>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>


<div class="container">
    <form action="<c:url value='/resume'/>" method="post">
        <input type="hidden" name="uuid" value="${resume.uuid}"/>
        <jsp:include page="fragments/header.jsp"/>
        <h3>Резюме:</h3>
            <input type="text" name="fullName" value="${not empty resume.fullName ? resume.fullName : ''}" />
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
                    <textarea id="<%= type.name() %>" name="<%= type.name() %>" rows="2" cols="50"><%= contact %></textarea>
                </li>
            <%
                    }
                }
            %>
        </ul>
<!-- Поля для нового типа контакта -->
<hr />
<h2>Добавить новый контакт</h2>
<ul class="without-bullets">
<li>
    <label for="newType">Выберите тип контакта:</label>
    <select id="newType" name="newType">
        <option value="" disabled selected>Выберите тип контакта</option>
        <%
            for (ContactType type : ContactType.values()) {
        %>
            <option value="<%= type.name() %>"><%= type.name() %></option>
        <%
            }
        %>
    </select>

</li>
    </ul>
    <ul class="without-bullets">
<li>
    <label for="newContact">Контакт:</label>
    <textarea id="newContact" name="newContact" rows="2" cols="50"></textarea>
</li>
    </ul>
    <button type="submit" class="button" name="action" value="saveForm1">Добавить контакт</button>
    <hr />
        <div class="section">
            <h1>Личные качества</h1>
            <textarea name="personalInfo" rows="4" cols="50">${personalInfo.getTextRepresentation()}</textarea>
        </div>

        <div class="section">
            <h1>Позиция</h1>
            <textarea name="objectives" rows="4" cols="50">${objectives.getTextRepresentation()}</textarea>
        </div>

        <div class="section">
            <h1>Достижения</h1>
            <ul class="without-bullets">
                <c:forEach var="achievement" items="${achievements}">
                    <li>
                        <input type="text" name="achievements" value="${achievement}" />
                    </li>
                </c:forEach>
                <li>
                    <input type="text" name="achievements" placeholder="Add new achievement" />
                </li>
            </ul>
        </div>
        <div class="section">
            <h1>Квалификация</h1>
            <ul class="without-bullets">
                <c:forEach var="qualification" items="${qualifications}">
                    <li>
                        <input type="text" name="qualifications" value="${qualification}" />
                    </li>
                </c:forEach>
                <li>
                    <input type="text" name="qualifications" placeholder="Add new qualification" />
                </li>
            </ul>
        </div>
<div class="section">
    <h1>Опыт работы</h1>
    <c:forEach var="company" items="${companies}" varStatus="companyStatus">
        <h2>Компания:
            <input type="text" name="companyName_${companyStatus.index}" required value="${company.nameCompany}" />
        </h2>
        <label>Сайт:
            <input type="url" name="companyWebsite_${companyStatus.index}" value="${company.website}" />
        </label>
        <ul>
            <c:forEach var="period" items="${company.periods}" varStatus="periodStatus">
                <li>
                    Имя компании: <input type="text" name="name_${companyStatus.index}_${periodStatus.index}" value="${period.name}" />
                    Дата начала: <input type="text" name="startDate_${companyStatus.index}_${periodStatus.index}" value="${period.startDate}" />
                    Дата окончания: <input type="text" name="endDate_${companyStatus.index}_${periodStatus.index}" value="${period.endDate}" />
                    Описание: <input type="text" name="description_${companyStatus.index}_${periodStatus.index}" value="${period.description}" />
                </li>
            </c:forEach>
        </ul>
    </c:forEach>
</div>

<h2>Добавить новую компанию</h2>
<h6>При добавлении компании все поля обязательны к заполнению</h6>
<div class="new-company-form">
    <label>Название компании:
        <input type="text" name="newCompanyName" />
    </label>
    <label>Сайт:
        <input type="url" name="newCompanyWebsite" />
    </label>
    <h3>Период работы</h3>
    <div class="new-period-form">
        <label>Имя компании:
            <input type="text" name="newPeriodName" />
        </label>
        <label>Дата начала:
            <input type="date" name="newPeriodStartDate" />
        </label>
        <label>Дата окончания:
            <input type="date" name="newPeriodEndDate" />
        </label>
        <label>Описание:
            <input type="text" name="newPeriodDescription" />
        </label>
    </div>
</div>
<div id="error-message" style="color: red; display: none;"></div>

<script>
    document.querySelector('.new-company-form').addEventListener('submit', function(event) {
        const newCompanyName = document.querySelector('input[name="newCompanyName"]').value;
        const newCompanyWebsite = document.querySelector('input[name="newCompanyWebsite"]').value;
        const newPeriodName = document.querySelector('input[name="newPeriodName"]').value;
        const newPeriodStartDate = document.querySelector('input[name="newPeriodStartDate"]').value;
        const newPeriodEndDate = document.querySelector('input[name="newPeriodEndDate"]').value;
        const newPeriodDescription = document.querySelector('input[name="newPeriodDescription"]').value;
    });
</script>

      <!-- Сообщение об ошибке -->
              <div id="errorMessage" style="color: red; display: none;"></div>
              <!-- Кнопки "Назад" и "Сохранить" -->
              <div class="button-container">
                  <a href="<c:url value='/resume'/>?uuid=${resume.uuid}&action=view" class="button">Назад</a>
                  <button type="submit" class="button">Сохранить</button>
              </div>
          </form>
      </div>

      <jsp:include page="fragments/footer.jsp"/>

      <script>
          function validateForm() {
              const errorMessageDiv = document.getElementById('errorMessage');
              errorMessageDiv.style.display = 'none'; // Скрыть предыдущее сообщение об ошибке
              const inputs = document.querySelectorAll('input[type="text"], input[type="url"]');
              let hasEmptyField = false;
              let emptyFields = [];

              inputs.forEach(input => {
                  if (input.value.trim() === '') {
                      hasEmptyField = true;
                      emptyFields.push(input.name); // Сохраняем имя пустого поля
                      input.style.borderColor = 'red'; // Подсветка пустого поля
                  } else {
                      input.style.borderColor = ''; // Убрать подсветку, если поле не пустое
                  }
              });

              // Проверка нового типа контакта только если поле для нового контакта заполнено
              const newContactField = document.getElementById('newContact');
              if (newContactField.value.trim() !== '' && document.getElementById('newType').value === '') {
                  hasEmptyField = true;
                  errorMessageDiv.innerText = 'Пожалуйста, выберите тип контакта для нового контакта.';
                  errorMessageDiv.style.display = 'block'; // Показать сообщение об ошибке
                  return false; // Предотвратить отправку формы
              }

              if (hasEmptyField) {
                  errorMessageDiv.innerText = 'Пожалуйста, заполните все обязательные поля: ' + emptyFields.join(', ');
                  errorMessageDiv.style.display = 'block'; // Показать сообщение об ошибке
                  return false; // Предотвратить отправку формы
              }

              return true; // Разрешить отправку формы
          }
      </script>
</body>
</html>