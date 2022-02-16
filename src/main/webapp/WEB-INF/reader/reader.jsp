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
      <div class="side-bar twin">
      </div>
      <div class="go-to">
        <p><fmt:message key="title.reader"/> ${sessionScope.loggedUser.name}</p>
        <div class="registration-info">
          <p><span><fmt:message key="title.name"/>:</span><span> ${sessionScope.loggedUser.name}</span></p>
          <p><span><fmt:message key="title.surname"/>:</span><span> ${sessionScope.loggedUser.surname}</span></p>
          <p><span><fmt:message key="title.role"/>:</span><span> ${sessionScope.loggedUser.role.name}</span></p>
        </div>
      </div>
      <div class="side-bar right">
      </div>
    </div>
  </div>
</section>
<jsp:include page="/WEB-INF/../common/footer.jsp"/>
</body>
</html>
