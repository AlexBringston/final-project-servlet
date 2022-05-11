<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" %>
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
<jsp:include page="header.jsp"/>
<jsp:include page="bg-images.jsp"/>
<section class="content">
    <div class="container">
        <div class="books-wrap log">
            <div class="go-to log">
                <p>Зареєструватися</p>
                <form action="${pageContext.request.contextPath}/app/registration" method="post">
                    <div class="authorize-form reg">
                        <label for="reg-name"><fmt:message key="placeholder.enter.name"/> :</label>
                        <input id="reg-name" name="name" type="text" placeholder="Anthony"
                               pattern="[A-Z][a-z]+|\u0400-\u042F][\u0430-\u04FF']+" required>
                        <label for="reg-surname"><fmt:message key="placeholder.enter.surname"/>:</label>
                        <input id="reg-surname" name="surname" type="text" placeholder="Hopkins"
                               pattern="[A-Z][a-z]+|\u0400-\u042F][\u0430-\u04FF']+" required>
                        <label for="reg-login"><fmt:message key="placeholder.enter.login"/>:</label>
                        <input id="reg-login" name="username" type="text" placeholder="tony_13_hopkins"
                               pattern="^[A-Za-z][A-Za-z0-9_]{1,20}$" required>
                        <label for="reg-password"><fmt:message key="placeholder.enter.password"/>:</label>
                        <input id="reg-password" name="password" type="password" pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$" required>
                        <label for="reg-date"><fmt:message key="placeholder.enter.birth.date"/>:</label>
                        <input id="reg-date" name="birthDate" type="date">
                        <input type="submit" value="Register">
                        <p><fmt:message key="title.already.registered"/> <a
href="${pageContext.request.contextPath}/app/login"><fmt:message key="button.login"/></a></p>
                    </div>
                </form>
            </div>
            <div class="error-p">

            </div>
        </div>
    </div>
</section>
<jsp:include page="footer.jsp" />
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
