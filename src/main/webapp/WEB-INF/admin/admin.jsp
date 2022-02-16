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
        <p><fmt:message key="title.admin"/> </p>
        <div class="registration-info">
          <p><span><fmt:message key="title.user"/>:</span>
            <span>${sessionScope.loggedUser.name} ${sessionScope.loggedUser.surname}</span></p>
          <p><span><fmt:message key="title.role"/>:</span><span>${sessionScope.loggedUser.role.name}</span></p>
          <p><span><fmt:message key="title.position"/>:</span> <fmt:message key="title.position.admin"/></p>
        </div>
      </div>
    </div>
  </div>
</section>

<jsp:include page="/WEB-INF/../common/footer.jsp"/>
</body>
</html>
