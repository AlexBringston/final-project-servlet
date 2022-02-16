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
            <div class="go-to">
                <p><fmt:message key="title.orders"/></p>
                <div class="books-list">
                    <c:forEach var="request" items="${requestScope.requestsPage.objects}">
                        <div class="book-info">
                            <div class="accordion">
                                <p>"${request.book.name}"</p>
                                <div class="accordion-btns">
                                    <c:if test="${request.book.onlyForReadingHall}">
                                        <p><fmt:message key="title.only.for.reading.hall"/></p>
                                    </c:if>
                                    <form action="${pageContext.request.contextPath}/app/librarian/orderManagement"
                                          method="post">
                                        <input type="hidden" name="requestId" value="${request.id}">
                                        <button type="submit" name="action" value="abonnement">Видати (А)</button>
                                        <button type="submit" name="action" value="readingHall">Видати (ЧЗ)</button>
                                        <button type="submit" name="action" value="reject">Відмовити</button>
                                    </form>
                                </div>
                            </div>
                            <div class="panel">
                                <p>
                                    Ordered by: ${request.user.name} ${request.user.surname}
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                    

                </div>
                <c:if test="${requestScope.requestsPage.objects.size() != 0}" >
                <form class="pagination" id="form1" method="get"
                      action="${pageContext.request.contextPath}/app/librarian/orders">
                    <c:if test="${(requestScope.requestsPage.totalPages != 1) && (requestScope.requestsPage.currentPage !=0)}">
                        <button form="form1" type="submit" name="page" value="0">
                            <fmt:message key="button.first"/>
                        </button>
                    </c:if>
                    <c:if test="${requestScope.requestsPage.previousPage != null}">
                        <button type="submit" name="page" form="form1"
                                value="${requestScope.requestsPage.previousPage}">
                            <fmt:message key="button.left"/></button>
                    </c:if>
                    <c:if test="${requestScope.requestsPage.nextPage != null}">
                        <button type="submit" name="page" form="form1"
                                value="${requestScope.requestsPage.nextPage}">
                            <fmt:message key="button.right"/>
                        </button>
                    </c:if>

                    <c:if test="${(requestScope.requestsPage.totalPages != 1) && (requestScope.requestsPage.currentPage !=
                    requestScope.requestsPage.lastPage)}">
                        <button type="submit" name="page" form="form1" value="${requestScope.requestsPage.lastPage}">
                            <fmt:message key="button.last"/>
                        </button>
                    </c:if>
                </form>
                </c:if>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
