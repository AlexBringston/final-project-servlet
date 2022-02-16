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
                <p>Книги</p>
                <div class="add-book">
                    <a href="${pageContext.request.contextPath}/app/admin/manageBook?action=add">+ Додати книгу</a>
                </div>
                <div class="books-list">
                    <c:if test="${requestScope.booksPage.objects.size() == 0}">
                        <fmt:message key="title.empty.page"/>
                    </c:if>
                    <c:forEach var="book" items="${requestScope.booksPage.objects}">
                        <div class="book-info">

                            <div class="accordion">
                                <p>"${book.name}"</p>
                                <div class="accordion-btns">
                                    <form method="get"
                                          action="${pageContext.request.contextPath}/app/admin/manageBook">
                                        <input type="hidden" name="bookId" value="${book.id}"/>
                                        <button type="submit" name="action" value="update">Change</button>
                                    </form>
                                    <form method="get"
                                          action="${pageContext.request.contextPath}/app/admin/manageBook">
                                        <input type="hidden" name="bookId" value="${book.id}"/>
                                        <button type="submit" name="action" value="updateAuthors">Change
                                            authors</button>
                                    </form>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/app/admin/manageBook">
                                        <input type="hidden" name="bookId" value="${book.id}"/>
                                        <button type="submit" name="action" value="delete">Delete</button>
                                    </form>
                                </div>

                            </div>
                            <div class="panel">
                                <p>Автор: ${book.mainAuthor.name} ${book.mainAuthor.surname}</p>
                                <p>Рік видання: ${book.publishedAt}</p>
                                <p>Кількість екземплярів: 5</p>
                                <p>Видано: 3</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${requestScope.booksPage.objects.size() != 0}" >
                    <form class="pagination"  id="form1" method="get"
                          action="${pageContext.request.contextPath}/app/admin/books">
                        <c:if test="${(requestScope.booksPage.totalPages != 1) && (requestScope.booksPage.currentPage !=0)}">
                            <button form="form1" type="submit" name="page" value="0" >
                                <fmt:message key="button.first"/>
                            </button>
                        </c:if>
                        <c:if test="${requestScope.booksPage.previousPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.booksPage.previousPage}">
                                <fmt:message key="button.left"/></button>
                        </c:if>
                        <c:if test="${requestScope.booksPage.nextPage != null}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.booksPage.nextPage}">
                                <fmt:message key="button.right"/>
                            </button>
                        </c:if>

                        <c:if test="${(requestScope.booksPage.totalPages != 1) && (requestScope.booksPage.currentPage !=
                    requestScope.booksPage.lastPage)}">
                            <button type="submit" name="page" form="form1"
                                    value="${requestScope.booksPage.lastPage}">
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
