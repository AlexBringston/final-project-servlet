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
                <p>Змінити інформацію про книгу</p>
                <form action="${pageContext.request.contextPath}/app/admin/manageBook" method="post"
                      class="new-book">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" name="bookId" value="${requestScope.book.id}" />

                    <label for="name">Book name</label>
                    <input type="text" id="name" placeholder="12 best Italian recipes" name="name"
                           value="${requestScope.book.name}" pattern="^(?!\s*$).+" required>

                    <div class="checkboxDiv">
                        <label for="checkbox">Is only for reading hall</label>
                        <input type="checkbox" id="checkbox" name="onlyForReadingHall"
                        <c:if test="${requestScope.book.onlyForReadingHall}">checked</c:if>  >
                    </div>

                    <label for="publisher">Publisher</label>
                    <input type="text" id="publisher" placeholder="Orion" name="publisher.name"
                           value="${requestScope.book.publisher.name}" pattern="^(?!\s*$).+" required>

                    <label for="publishedAt">Published at</label>
                    <input type="date" placeholder="Рік видання" id="publishedAt" name="publishedAt"
                           value="${requestScope.book.publishedAt}"
                           required>

                    <label for="quantity">Quantity</label>
                    <input type="number" min="1" id="quantity" placeholder="430" name="quantity"
                           value="${requestScope.book.quantity}" required>

                    <label for="imgURL">Image URL</label>
                    <input type="text" id="imgURL" placeholder="http://dummyimage.com/117x100.png/dddddd/000000" name="imgUrl"
                           value="${requestScope.book.imgUrl}" required>

                    <label for="mainAuthorName">Main author name</label>
                    <input type="text" id="mainAuthorName" placeholder="Antonio" name="mainAuthor.name"
                           value="${requestScope.book.mainAuthor.name}" pattern="^(?!\s*$).+" required>

                    <label for="mainAuthorSurname">Main author surname</label>
                    <input type="text" id="mainAuthorSurname" placeholder="Giovanni"
                           name="mainAuthor.surname"
                           value="${requestScope.book.mainAuthor.surname}" pattern="^(?!\s*$).+" required>

                    <button type="submit" name="action" value="save">Save</button>
                </form>
            </div>
        </div>
        <div class="error-p">
            <c:if test="${requestScope.errorName != null}">
                <p><span>Error: </span><span>${requestScope.errorName}</span></p>
            </c:if>
            <c:if test="${requestScope.errorPublisher != null}">
                <p><span>Error: </span><span>${requestScope.errorPublisher}</span></p>
            </c:if>
            <c:if test="${requestScope.errorDate != null}">
                <p><span>Error: </span><span>${requestScope.errorDate}</span></p>
            </c:if>
            <c:if test="${requestScope.errorQuantity != null}">
                <p><span>Error: </span><span>${requestScope.errorQuantity}</span></p>
            </c:if>
            <c:if test="${requestScope.errorImage != null}">
                <p><span>Error: </span><span>${requestScope.errorImage}</span></p>
            </c:if>
            <c:if test="${requestScope.errorAuthor != null}">
                <p><span>Error: </span><span>${requestScope.errorAuthor}</span></p>
            </c:if>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
</body>
</html>
