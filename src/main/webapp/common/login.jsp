<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" %>
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
<jsp:include page="header.jsp"/>
<jsp:include page="bg-images.jsp"/>
<section class="content">
  <div class="container">
    <div class="books-wrap log">
      <div class="go-to log">
        <p><fmt:message key="title.enter"/></p>
        <div class="authorize-form">
            <form id="loginForm" method="post" action="${pageContext.request.contextPath}/app/login">
            <input type="text" name="username" placeholder="<fmt:message key="placeholder.enter.login"/>" required>
            <input type="password" name="password" placeholder="<fmt:message key="placeholder.enter.password"/>" required>
          </form>
          <input type="submit" form="loginForm" value="<fmt:message key="title.enter"/>">
          <p><fmt:message key="title.no.account"/> <a
                  href="${pageContext.request.contextPath}/app/registration"><fmt:message key="button.login"/></a></p>
        </div>
      </div>
      <c:if test="${requestScope.errorMessage != null}">
        <div class="error-p">
          <p><span><fmt:message key="title.error"/>: </span><span><fmt:message key="${requestScope.errorMessage}"/></span></p>
        </div>
      </c:if>


    </div>
  </div>
</section>
<jsp:include page="footer.jsp" />
</body>
</html>