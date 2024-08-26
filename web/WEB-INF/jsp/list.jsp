<%--
  Created by IntelliJ IDEA.
  User: Lek36
  Date: 22.08.2024
  Time: 21:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<main>
    <div class="container-fluid">
        <div class="row">
            <div class="col-4 offset-4">
                <table class="table caption-top">
                    <caption>Список резюме</caption>
                    <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Имя</th>
                        <th>Редактировать</th>
                        <th>Удалить</th>
                    </tr>
                    </thead>
                    <tbody>
                    <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
                    <c:forEach items="${resumes}" var="resume" varStatus="counter">
                        <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume"/>
                        <tr>
                            <th>${counter.count}</th>
                            <td><a href="list?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                            <td><a href="list?uuid=${resume.uuid}&action=edit">редактировать</a></td>
                            <td><a href="list?uuid=${resume.uuid}&action=delete">удалить</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-4 offset-4">
                <a href="list?action=add" class="btn btn-success" role="button">Добавить резюме</a>
            </div>
        </div>
    </div>
</main>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
