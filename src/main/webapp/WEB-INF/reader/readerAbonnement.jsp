<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

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
            <div class="side-bar">
                <form class="book-sort" id="form1" method="get"
                      action="${pageContext.request.contextPath}/app/reader/abonnement">
                    <p><fmt:message key="sort.title"/></p>
                    <select class="localization sort" name="sortField">
                        <option
                                <c:if test="${requestScope.abonnementPage.sortField == 'book_name'}">selected</c:if>
                                value="book_name"><fmt:message key="sort.field.name"/>
                        </option>
                        <option
                                <c:if test="${requestScope.abonnementPage.sortField == 'author_surname'}">selected</c:if>
                                value="author_surname"><fmt:message key="sort.field.author"/>
                        <option
                                <c:if test="${requestScope.abonnementPage.sortField == 'return_date'}">selected</c:if>
                                value="return_date">
                            <fmt:message key="sort.field.return.date"/></option>
                    </select>
                    <select class="localization sort" name="sortDirection">
                        <option
                                <c:if test="${requestScope.abonnementPage.sortDirection == 'ASC'}">selected</c:if>
                                value="ASC"><fmt:message key="sort.direction.asc"/></option>
                        <option
                                <c:if test="${requestScope.abonnementPage.sortDirection == 'DESC'}">selected</c:if>
                                value="DESC"><fmt:message key="sort.direction.desc"/></option>
                    </select>
                    <input type="submit" value="<fmt:message key="button.apply"/>" form="form1">
                </form>
            </div>
            <div class="go-to">
                <p>Абонемент</p>
                <div class="books-list">
                    <c:forEach var="abonnement" items="${requestScope.abonnementPage.objects}">
                        <div class="book-info reader
                            <c:choose>
                                <c:when test="${abonnement.status.name.equals('status.expired')}">late</c:when>
                                <c:when test="${abonnement.status.name.equals('status.returned')}">done</c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>" >
                            <div class="accordion">
                                <p>"${abonnement.book.name}"</p>
                                <c:choose>
                                    <c:when test="${abonnement.status.name.equals('status.expired')}">
                                        <span>
                                            Expired : (Penalty${abonnement.penalty})
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>
                                            <fmt:message key="${abonnement.status.name}"/>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="panel">
                                <p>Автор: ${abonnement.book.mainAuthor.name} ${abonnement.book.mainAuthor.surname}</p>
                                <p>Дата видачі книги: 1.1.2022</p>
                                <p>Return date : ${abonnement.returnDate}</p>
                            </div>
                        </div>
                    </c:forEach>

                </div>
                <div class="pagination">
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
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
