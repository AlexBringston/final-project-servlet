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
            <jsp:include page="/WEB-INF/admin/side-bar.jsp"/>
            <div class="go-to">
                <p>Бібліотекарі</p>
                <div class="add-book">
                    <a href="${pageContext.request.contextPath}/app/admin/addLibrarian"><fmt:message
                            key="button.add.librarian"/></a>
                </div>
                <div class="books-list">
                    <c:forEach var="librarian" items="${requestScope.librariansPage.objects}">
                        <div class="book-info">
                            <div class="librarians-list">
                                <p>${librarian.name} ${librarian.surname}</p>
                                <form class="accordion-btns" method="post"
                                      action="${pageContext.request.contextPath}/app/admin/librarians">
                                    <input type="hidden" name="userId" value="${librarian.id}">
                                    <button type="submit" value="delete"><fmt:message key="button.remove"/></button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>


                </div>
                <c:if test="${requestScope.librariansPage.objects.size() != 0}" >
                    <form class="pagination"  id="form1" method="get"
                          action="${pageContext.request.contextPath}/app/admin/librarians">
                        <c:if test="${(requestScope.librariansPage.totalPages != 1) && (requestScope.librariansPage.currentPage !=0)}">
                            <button form="form1" type="submit" name="page" value="0" >
                                <fmt:message key="button.first"/>
                            </button>
                        </c:if>
                        <c:if test="${requestScope.librariansPage.previousPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.librariansPage.previousPage}">
                                <fmt:message key="button.left"/></button>
                        </c:if>
                        <c:if test="${requestScope.librariansPage.nextPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.librariansPage.nextPage}">
                                <fmt:message key="button.right"/>
                            </button>
                        </c:if>

                        <c:if test="${(requestScope.librariansPage.totalPages != 1) && (requestScope.librariansPage.currentPage !=
                    requestScope.librariansPage.lastPage)}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.librariansPage.lastPage}">
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
</body>
</html>
