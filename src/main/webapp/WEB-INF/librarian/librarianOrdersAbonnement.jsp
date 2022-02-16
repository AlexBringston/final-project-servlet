<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <jsp:include page="/WEB-INF/librarian/side-bar.jsp"/>
            <div class="go-to give log">
                <div class="give-form">
                    <p>Видача на абонемент</p>
                    <div class="give-info">
                        <p>Book: "${requestScope.request.book.name}"</p>
                        <p>Ordered by: ${requestScope.request.user.name} ${requestScope.request.user.surname}</p>
                    </div>
                    <form class="authorize-form reg give" method="post"
                          action="${pageContext.request.contextPath}/app/librarian/addOrder">
                        <input type="hidden" name="userId" value="${requestScope.request.user.id}">
                        <input type="hidden" name="bookId" value="${requestScope.request.book.id}">
                        <input type="hidden" name="requestId" value="${requestScope.request.id}">
                        <label for="date">Введіть дату повернення книги:</label>
                        <input id="date" type="date" name="returnDate">
                        <label for="penalty">Введіть суму штрафу:</label>
                        <input id="penalty" type="number" min="0" name="penalty">
                        <input type="submit" value="Видати">
                    </form>
                </div>
                <div class="error-p">
                    <p><span>Помилка: </span>введено невірний пароль</p>
                    <p><span>Помилка: </span>введено невірний пароль</p>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
