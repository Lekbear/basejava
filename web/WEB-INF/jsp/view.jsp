<%--
  Created by IntelliJ IDEA.
  User: Lek36
  Date: 23.08.2024
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.TextSection" %>
<%@ page import="com.basejava.webapp.model.ListTextSection" %>

<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <title>Просмотр резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<main>
    <div class="container-fluid my-2">
        <div class="row">
            <div class="col-8 offset-2">
                <jsp:useBean id="resume" scope="request" type="com.basejava.webapp.model.Resume"/>

                <div class="row">
                    <div class="col-2">
                        <h3>Резюме:</h3>
                    </div>
                    <div class="col-10 pt-2">
                        <a href="list?uuid=${resume.uuid}&action=edit">Редактировать</a>
                    </div>
                </div>

                <dl class="row">
                    <dt class="col-2"> ФИО:</dt>
                    <dd class="col-10">${resume.fullName}</dd>
                </dl>

                <c:forEach var="contactEntry" items="${resume.contacts}" varStatus="counter">
                    <jsp:useBean id="contactEntry"
                                 type="java.util.Map.Entry<com.basejava.webapp.model.ContactType, java.lang.String>"/>
                    <c:if test="${counter.count eq 1}">
                        <h3>Контакты:</h3>
                    </c:if>
                    <dl class="row">
                        <dt class="col-2"><%= contactEntry.getKey().getTitle()%>:</dt>
                        <dd class="col-10">${contactEntry.getValue()}</dd>
                    </dl>
                </c:forEach>

                <c:forEach var="sectionEntry" items="${resume.sections}" varStatus="counter">
                    <jsp:useBean id="sectionEntry"
                                 type="java.util.Map.Entry<com.basejava.webapp.model.SectionType, java.lang.String>"/>
                    <c:set var="sectionType" value="<%=sectionEntry.getKey()%>"/>
                    <jsp:useBean id="sectionType" type="com.basejava.webapp.model.SectionType"/>
                    <c:if test="${counter.count eq 1}">
                        <h3>Секции:</h3>
                    </c:if>
                    <dl class="row">
                        <c:choose>
                            <c:when test="${sectionType == SectionType.PERSONAL || sectionType == SectionType.OBJECTIVE }">
                                <dt class="col-2"><%= sectionEntry.getKey().getTitle()%>:</dt>
                                <dd class="col-10"><%= ((TextSection) resume.getSection(sectionType)).getText() %>
                            </c:when>
                            <c:when test="${sectionType == SectionType.ACHIEVEMENTS || sectionType == SectionType.QUALIFICATIONS }">
                                <c:set var="listTexts"
                                       value="<%= ((ListTextSection) resume.getSection(sectionType)).getTexts()%>"/>
                                <jsp:useBean id="listTexts" type="java.util.List"/>
                                <dt class="col-2"><%= sectionEntry.getKey().getTitle()%>:</dt>
                                <dd class="col-10"><%=String.join("\n", listTexts) %>
                                </dd>
                            </c:when>
                            <c:when test="${sectionType == SectionType.EDUCATION || sectionType == SectionType.EXPERIENCE}">
                            </c:when>
                        </c:choose>
                    </dl>
                </c:forEach>
            </div>
        </div>
    </div>
</main>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
