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
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200;300;400;500;600;700;800;900&family=Vollkorn:wght@400;600;700;800;900&display=swap"
        rel="stylesheet">
  <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/img/favicon.png" type="image/png"/>
</head>
<body class="body">
<c:if test="${(sessionScope.loggedUser != null) && (sessionScope.loggedUser.role.name.equals('ROLE_READER'))}">
  <div class="topper">
    <form class="search" method="get" id="form1" action="${pageContext.request.contextPath}/app/reader/search">
      <input type="text" name="query" value="${requestScope.booksPage.query}" placeholder="Пошук">
      <input type="submit" value="">
    </form>
  </div>
</c:if>
<jsp:include page="/WEB-INF/../common/header.jsp"/>
<jsp:include page="/WEB-INF/../common/bg-images.jsp"/>
<section class="content">
  <div class="container">
    <div class="books-wrap">
      <div class="side-bar">
        <div class="book-sort">
          <p><fmt:message key="sort.title"/></p>
          <select class="localization sort" name="sortField" form="form1">
            <option
                    <c:if test="${requestScope.booksPage.sortField == 'book_name'}">selected</c:if>
                    value="book_name"><fmt:message key="sort.field.name"/>
            </option>
            <option
                    <c:if test="${requestScope.booksPage.sortField == 'author_surname'}">selected</c:if>
                    value="author_surname"><fmt:message key="sort.field.author"/>
            </option>
            <option
                    <c:if test="${requestScope.booksPage.sortField == 'publisher_name'}">selected</c:if>
                    value="publisher_name">
              <fmt:message key="sort.field.publisher"/></option>
            <option
                    <c:if test="${requestScope.booksPage.sortField == 'published_at'}">selected</c:if>
                    value="published_at"><fmt:message key="sort.field.published.at"/></option>
          </select>
          <select class="localization sort" name="sortDirection" form="form1">
            <option
                    <c:if test="${requestScope.booksPage.sortDirection == 'ASC'}">selected</c:if>
                    value="ASC"><fmt:message key="sort.direction.asc"/></option>
            <option
                    <c:if test="${requestScope.booksPage.sortDirection == 'DESC'}">selected</c:if>
                    value="DESC"><fmt:message key="sort.direction.desc"/></option>
          </select>
          <input type="submit" value="<fmt:message key="button.apply"/>" form="form1">
        </div>
      </div>
      <div class="go-to">

        <p><fmt:message key="title.library"/></p>
        <div class="books-catalog">
          <c:if test="${requestScope.booksPage.objects.size() == 0}">
            <fmt:message key="title.empty.page"/>
          </c:if>
          <c:forEach var="book" items="${requestScope.booksPage.objects}">
            <div class="book">
              <div class="book-cover">
                <img src="${book.imgUrl}" alt=""/>
              </div>
              <div class="book-NAP">
                <p>"${book.name}"</p>
                <p>${book.mainAuthor.name} ${book.mainAuthor.surname}</p>
                <p>${book.publishedAt.year}</p>
              </div>
              <c:if test="${(sessionScope.loggedUser != null) && (sessionScope.loggedUser.role.name.equals('ROLE_READER'))}">
                <form method="post"
                      action="${pageContext.request.contextPath}/app/reader/createOrder">
                  <input type="hidden" name="bookId" value="${book.id}"/>
                  <button type="submit"><fmt:message key="button.order"/></button>
                </form>
              </c:if>
            </div>
          </c:forEach>
        </div>

        <c:if test="${requestScope.booksPage.objects.size() != 0}" >
        <div class="pagination">
          <c:if test="${(requestScope.booksPage.totalPages != 1) && (requestScope.booksPage.currentPage !=0)}">
            <button form="form1" type="submit" name="page" value="0">
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
        </div>
        </c:if>


      </div>
    </div>
  </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
</body>
</html>
