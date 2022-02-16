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
                <c:choose>
                    <c:when test="${requestScope.abonnementPage.objects.size() != 0}">
                        <p><fmt:message key="title.abonnement"/>
                                ${requestScope.abonnementPage.objects.get(0).user.name}
                                ${requestScope.abonnementPage.objects.get(0).user.surname}</p>
                    </c:when>
                    <c:otherwise>
                        <p>
                            This user has no pending books in his abonnement
                        </p>
                    </c:otherwise>
                </c:choose>


                <div class="books-list">
                    <c:forEach var="abonnement" items="${requestScope.abonnementPage.objects}">
                        <div class="book-info">
                            <div class="accordion">
                                <p>"${abonnement.book.name}"</p>
                                <div class="accordion-btns">
                                    <form action="${pageContext.request.contextPath}/app/librarian/changeAbonnementStatus" method="post">
                                        <input type="hidden" name="bookId" value="${abonnement.book.id}">
                                        <input type="hidden" name="userId" value="${abonnement.user.id}">
                                        <button type="submit" name="action"
                                                value="returned"><fmt:message key="status.returned"/></button>
                                        <button type="submit" name="action" value="expired"><fmt:message
                                                key="status.expired"/></button>
                                    </form>
                                </div>
                            </div>
                            <div class="panel">
                                <p><fmt:message key="title.author"/> : ${abonnement.book.mainAuthor.name}
                                        ${abonnement.book.mainAuthor.surname}  </p>
                                <p><fmt:message key="title.return.date"/> : ${abonnement.returnDate}</p>
                            </div>
                        </div>
                    </c:forEach>


                </div>
                <form class="pagination" id="form1" method="get"
                      action="${pageContext.request.contextPath}/app/librarian/abonnement">
                    <c:if test="${(requestScope.abonnementPage.totalPages != 1) && (requestScope.abonnementPage.currentPage !=0)}">
                        <button form="form1" type="submit" name="page" value="0" >
                            <fmt:message key="button.first"/>
                        </button>
                    </c:if>
                    <c:if test="${requestScope.abonnementPage.previousPage != null}">
                        <button type="submit" name="page" form="form1"
                                value="${requestScope.abonnementPage.previousPage}">
                            <fmt:message key="button.left"/></button>
                    </c:if>
                    <c:if test="${requestScope.abonnementPage.nextPage != null}">
                        <button type="submit" name="page" form="form1"
                                value="${requestScope.abonnementPage.nextPage}">
                            <fmt:message key="button.right"/>
                        </button>
                    </c:if>

                    <c:if test="${(requestScope.abonnementPage.totalPages != 1) && (requestScope.abonnementPage.currentPage !=
                    requestScope.abonnementPage.lastPage)}">
                        <button type="submit" name="page" form="form1"
                                value="${requestScope.abonnementPage.lastPage}">
                            <fmt:message key="button.last"/>
                        </button>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
