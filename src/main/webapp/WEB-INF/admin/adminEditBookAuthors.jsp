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
                <form action="${pageContext.request.contextPath}/app/admin/manageBook" method="post" class="new-book">
                    <input type="hidden" name="action" value="updateAuthors">
                    <input type="hidden" name="bookId" value="${requestScope.book.id}">
                    <label for="newAuthorName">Author to add (Name)</label>
                    <div class="new-book-authors">
                        <input type="text" id="newAuthorName" placeholder="Author name" name="additionalAuthorName">
                    </div>
                    <label for="newAuthorSurname">Author to add (Surname)</label>
                    <div class="new-book-authors">
                        <input type="text"  id="newAuthorSurname" placeholder="Author surname"
                               name="additionalAuthorSurname">
                    </div>
                    <button type="submit" id="submit-author" name="operation" value="+">Add author</button>
                    <label for="presentAuthors">Present authors</label>
                    <div class="new-book-authors">
                        <select id="presentAuthors" class="new-book-select" name="authorToDelete">
                            <c:forEach var="author" items="${requestScope.authors}">
                                <option>${author.name} ${author.surname}</option>
                            </c:forEach>

                        </select>
                        <input type="submit" name="operation" value="-">
                    </div>

                    <input type="submit" name="operation" value="finish"/>
                </form>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
</body>
</html>
