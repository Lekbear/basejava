<%--
  Created by IntelliJ IDEA.
  User: Lek36
  Date: 23.08.2024
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.basejava.webapp.model.*" %>
<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <title>Редактирование резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<main>
    <div class="container-fluid my-2">
        <div class="row">
            <div class="col-8 offset-2">
                <form method="post" action="list">
                    <h3>Резюме:</h3>
                    <jsp:useBean id="resume" scope="request" type="com.basejava.webapp.model.Resume"/>
                    <div class="form-group has-validation">
                        <label for="fullName">ФИО:</label>
                        <input type="text" id="fullName" value="${resume.fullName}" class="form-control" name="fullName"
                               required>
                    </div>

                    <input type="hidden" id="uuid" value="${resume.uuid}" class="form-control" name="uuid">

                    <h3>Контакты:</h3>
                    <c:forEach var="type" items="<%=ContactType.values()%>">
                        <div class="form-group">
                            <label for="${type.name()}">${type.title}:</label>
                            <input type="text" id="${type.name()}" name="${type.name()}"
                                   value="${resume.getContact(type)}"
                                   class="form-control">
                        </div>
                    </c:forEach>

                    <h3>Секции:</h3>
                    <c:forEach var="sectionType" items="<%= SectionType.values()%>">
                        <jsp:useBean id="sectionType" type="com.basejava.webapp.model.SectionType"/>
                        <c:choose>
                            <c:when test="${sectionType == SectionType.PERSONAL || sectionType == SectionType.OBJECTIVE }">
                                <div class="form-group">
                                    <label for="${sectionType.name()}">${sectionType.title}:</label>
                                    <input type="text" id="${sectionType.name()}" name="${sectionType.name()}"
                                           value="<%=resume.getSection(sectionType) == null ? "" :
                                               ((TextSection) resume.getSection(sectionType)).getText() %>"
                                           class="form-control">
                                </div>
                            </c:when>

                            <c:when test="${sectionType == SectionType.ACHIEVEMENTS || sectionType == SectionType.QUALIFICATIONS }">
                                <c:set var="listTexts"
                                       value="<%= resume.getSection(sectionType) == null ? new ArrayList<>() :
                                           ((ListTextSection) resume.getSection(sectionType)).getTexts()%>"/>
                                <jsp:useBean id="listTexts" type="java.util.List"/>
                                <div class="form-group">
                                    <label for="${sectionType.name()}">${sectionType.title}:</label>
                                    <textarea id="${sectionType.name()}"
                                              rows="<%=listTexts == null || listTexts.isEmpty() ? 1 : listTexts.size()%>"
                                              class="form-control"
                                              name="${sectionType.name()}"><%=String.join("\n", listTexts) %></textarea>
                                </div>
                            </c:when>
                            <c:when test="${sectionType == SectionType.EXPERIENCE || sectionType == SectionType.EDUCATION}">
                                <c:if test="${sectionType == SectionType.EXPERIENCE}">
                                    <h3>Опыт работы:</h3>
                                </c:if>
                                <c:if test="${sectionType == SectionType.EDUCATION}">
                                    <h3>Образование:</h3>
                                </c:if>

                                <c:set var="companyName" value="${sectionType.name()}_company_name_0_0"/>
                                <c:set var="companyUrl" value="${sectionType.name()}_company_url_0_0"/>
                                <c:set var="periodStartDate" value="${sectionType.name()}_period_start_date_0_0"/>
                                <c:set var="periodEndDate" value="${sectionType.name()}_period_end_date_0_0"/>
                                <c:set var="periodTitle" value="${sectionType.name()}_period_title_0_0"/>
                                <c:set var="periodDescription" value="${sectionType.name()}_period_description_0_0"/>

                                <div class="form-group">
                                    <label for="${companyName}">Название:</label>
                                    <input type="text" id="${companyName}" name="${companyName}"
                                           class="form-control">

                                    <label for="${companyUrl}">URL:</label>
                                    <input type="text" id="${companyUrl}" name="${companyUrl}"
                                           class="form-control">

                                    <div class="row">
                                        <div class="col-6">
                                            <label for="${periodStartDate}">Дата начала(ГГГГ-ММ-ДД):</label>
                                            <input type="text" id="${periodStartDate}" name="${periodStartDate}"
                                                   class="form-control">
                                        </div>

                                        <div class="col-6">
                                            <label for="${periodEndDate}">Дата окончания(ГГГГ-ММ-ДД):</label>
                                            <input type="text" id="${periodEndDate}" name="${periodEndDate}"
                                                   class="form-control">
                                        </div>
                                    </div>

                                    <label for="${periodTitle}">Заголовок:</label>
                                    <input type="text" id="${periodTitle}" name="${periodTitle}"
                                           class="form-control">

                                    <label for="${periodDescription}">Описание:</label>
                                    <textarea id="${periodDescription}" rows="1" class="form-control"
                                              name="${periodDescription}"></textarea>
                                </div>

                                <c:forEach var="company" varStatus="i"
                                           items="<%= resume.getSection(sectionType) == null ? new ArrayList<>() :
                                    ((CompanySection) resume.getSection(sectionType)).getCompanies()%>">
                                    <jsp:useBean id="company" type="com.basejava.webapp.model.Company"/>

                                    <c:set var="companyName" value="${sectionType.name()}_company_name_${i.count}_0"/>
                                    <c:set var="companyUrl" value="${sectionType.name()}_company_url_${i.count}_0"/>

                                    <hr class="my-3">
                                    <div class="form-group">
                                        <label for="${companyName}">Название:</label>
                                        <input type="text" id="${companyName}" name="${companyName}"
                                               class="form-control"
                                               value="${company.name}">

                                        <label for="${companyUrl}">URL:</label>
                                        <input type="text" id="${companyUrl}" name="${companyUrl}" class="form-control"
                                               value="${company.website}">

                                        <c:set var="sizePeriods" value="<%= company.getPeriods().size() %>"/>
                                        <c:set var="periodStartDate"
                                               value="${sectionType.name()}_period_start_date_${i.count}_0"/>
                                        <c:set var="periodEndDate"
                                               value="${sectionType.name()}_period_end_date_${i.count}_0"/>
                                        <c:set var="periodTitle"
                                               value="${sectionType.name()}_period_title_${i.count}_0"/>
                                        <c:set var="periodDescription"
                                               value="${sectionType.name()}_period_description_${i.count}_0"/>

                                        <div class="row">
                                            <div class="col-6">
                                                <label for="${periodStartDate}">Дата начала(ГГГГ-ММ-ДД):</label>
                                                <input type="text" id="${periodStartDate}" name="${periodStartDate}"
                                                       class="form-control">
                                            </div>

                                            <div class="col-6">
                                                <label for="${periodEndDate}">Дата окончания(ГГГГ-ММ-ДД):</label>
                                                <input type="text" id="${periodEndDate}" name="${periodEndDate}"
                                                       class="form-control">
                                            </div>
                                        </div>

                                        <label for="${periodTitle}">Заголовок:</label>
                                        <input type="text" id="${periodTitle}" name="${periodTitle}"
                                               class="form-control">

                                        <label for="${periodDescription}">Описание:</label>
                                        <textarea id="${periodDescription}"
                                                  rows="1"
                                                  class="form-control"
                                                  name="${periodDescription}"></textarea>

                                        <c:forEach var="period" varStatus="j" items="<%= company.getPeriods() %>">
                                            <jsp:useBean id="period" type="com.basejava.webapp.model.Period"/>

                                            <c:set var="periodStartDate"
                                                   value="${sectionType.name()}_period_start_date_${i.count}_${j.count}"/>
                                            <c:set var="periodEndDate"
                                                   value="${sectionType.name()}_period_end_date_${i.count}_${j.count}"/>
                                            <c:set var="periodTitle"
                                                   value="${sectionType.name()}_period_title_${i.count}_${j.count}"/>
                                            <c:set var="periodDescription"
                                                   value="${sectionType.name()}_period_description_${i.count}_${j.count}"/>

                                            <div class="row">
                                                <div class="col-6">
                                                    <label for="${periodStartDate}">Дата начала(ГГГГ-ММ-ДД):</label>
                                                    <input type="text" id="${periodStartDate}" name="${periodStartDate}"
                                                           class="form-control" value="${period.startDate}">
                                                </div>

                                                <div class="col-6">
                                                    <label for="${periodEndDate}">Дата окончания(ГГГГ-ММ-ДД):</label>
                                                    <input type="text" id="${periodEndDate}" name="${periodEndDate}"
                                                           class="form-control" value="${period.endDate}">
                                                </div>
                                            </div>

                                            <label for="${periodTitle}">Заголовок:</label>
                                            <input type="text" id="${periodTitle}" name="${periodTitle}"
                                                   class="form-control" value="${period.title}">

                                            <label for="${periodDescription}">Описание:</label>
                                            <textarea id="${periodDescription}"
                                                      rows="<%= period.getDescription() == null ||
                                                          period.getDescription().isEmpty() ? 1 :
                                                          period.getDescription().length() / 100 + 1 %>"
                                                      class="form-control"
                                                      name="${periodDescription}">${period.description}</textarea>
                                        </c:forEach>
                                    </div>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </c:forEach>

                    <div class="form-group my-3">
                        <button type="submit" class="btn btn-success">Сохранить</button>
                        <button type="reset" onclick="window.history.back()" class="btn btn-danger">Отменить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
