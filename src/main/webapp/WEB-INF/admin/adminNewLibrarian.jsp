<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="res"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>E-library</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200;300;400;500;600;700;800;900&family=Vollkorn:wght@400;600;700;800;900&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/img/favicon.png" type="image/png"/>
</head>
<body class="body">
<jsp:include page="/WEB-INF/../common/header.jsp"/>
<jsp:include page="/WEB-INF/../common/bg-images.jsp"/>
<section class="content">
    <div class="container">
        <div class="books-wrap">
            <jsp:include page="/WEB-INF/admin/side-bar.jsp"/>
            <div class="go-to">
                <p>Додати бібліотекаря</p>
                <form action="${pageContext.request.contextPath}/app/admin/addLibrarian" class="new-book" method="post">
                    <label for="name"><fmt:message key="placeholder.enter.name" />:</label>
                    <input type="text" id="name" name="name" placeholder="">
                    <label for="surname"><fmt:message key="placeholder.enter.surname" />:</label>
                    <input type="text" id="surname" name="surname" placeholder="">
                    <label for="login"><fmt:message key="placeholder.enter.login" />:</label>
                    <input type="text" id="login" name="username" placeholder="">
                    <label for="password"><fmt:message key="placeholder.enter.password" />:</label>
                    <input type="password" id="password" name="password" placeholder="">
                    <label for="date"><fmt:message key="placeholder.enter.birth.date" />:</label>
                    <input id="date" type="date" name="birthDate">
                    <input type="submit" value="<fmt:message key="button.register" />">
                </form>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
