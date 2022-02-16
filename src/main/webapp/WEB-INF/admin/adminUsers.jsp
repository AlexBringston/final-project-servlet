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
                <p>Користувачі</p>
                <div class="books-list">
                    <c:forEach var="user" items="${requestScope.usersPage.objects}" varStatus="loop">
                        <div class="book-info <c:if test="${!user.accountNonLocked}">blocked-user</c:if>">
                            <div class="accordion">
                                <p>${user.name} ${user.surname}</p>
                                <form id="form2" class="accordion-btns" method="post"
                                      action="${pageContext.request.contextPath}/app/admin/users">
                                    <input type="hidden" name="userId" value="${user.id}">
                                    <c:choose>
                                        <c:when test="${user.accountNonLocked}">
                                            <button type="submit" name="action"
                                                    value="block"><fmt:message key="button.block"/></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" name="action"
                                                    value="unblock"><fmt:message key="button.unblock"/></button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>
                            <div class="panel">
                                <p><fmt:message key="title.age"/> : ${requestScope.ages[loop.index]}</p>
                                <p><fmt:message key="title.books.taken"/> : ${requestScope.booksTaken[loop.index]}</p>
                            </div>
                        </div>
                    </c:forEach>

                </div>
                <c:if test="${requestScope.usersPage.objects.size() != 0}" >
                    <form class="pagination"  id="form1" method="get"
                          action="${pageContext.request.contextPath}/app/admin/users">
                        <c:if test="${(requestScope.usersPage.totalPages != 1) && (requestScope.usersPage.currentPage !=0)}">
                            <button form="form1" type="submit" name="page" value="0" >
                                <fmt:message key="button.first"/>
                            </button>
                        </c:if>
                        <c:if test="${requestScope.usersPage.previousPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.usersPage.previousPage}">
                                <fmt:message key="button.left"/></button>
                        </c:if>
                        <c:if test="${requestScope.usersPage.nextPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.usersPage.nextPage}">
                                <fmt:message key="button.right"/>
                            </button>
                        </c:if>

                        <c:if test="${(requestScope.usersPage.totalPages != 1) && (requestScope.usersPage.currentPage !=
                    requestScope.usersPage.lastPage)}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.usersPage.lastPage}">
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
