<%--
  Created by IntelliJ IDEA.
  User: Lek36
  Date: 23.08.2024
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.TextSection" %>
<%@ page import="com.basejava.webapp.model.ListTextSection" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="java.util.ArrayList" %>
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
                        <div class="form-group">
                            <c:choose>
                                <c:when test="${sectionType == SectionType.PERSONAL || sectionType == SectionType.OBJECTIVE }">
                                    <label for="${sectionType.name()}">${sectionType.title}:</label>
                                    <input type="text" id="${sectionType.name()}" name="${sectionType.name()}"
                                           value="<%=resume.getSection(sectionType) == null ? "" :
                                           ((TextSection) resume.getSection(sectionType)).getText() %>"
                                           class="form-control">
                                </c:when>

                                <c:when test="${sectionType == SectionType.ACHIEVEMENTS || sectionType == SectionType.QUALIFICATIONS }">
                                    <c:set var="listTexts"
                                           value="<%= resume.getSection(sectionType) == null ? new ArrayList<>() :
                                           ((ListTextSection) resume.getSection(sectionType)).getTexts()%>"/>
                                    <jsp:useBean id="listTexts" type="java.util.List"/>
                                    <label for="${sectionType.name()}">${sectionType.title}:</label>
                                    <textarea id="${sectionType.name()}"
                                              rows="<%=listTexts == null || listTexts.isEmpty() ? 1 : listTexts.size()%>"
                                              class="form-control"
                                              name="${sectionType.name()}"><%=String.join("\n", listTexts) %></textarea>
                                </c:when>
                                <c:when test="${sectionType == SectionType.EDUCATION || sectionType == SectionType.EXPERIENCE}">
                                </c:when>
                            </c:choose>
                        </div>
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
