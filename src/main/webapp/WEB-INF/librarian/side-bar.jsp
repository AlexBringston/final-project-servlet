<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="res"/>


<div class="side-bar">
  <div class="book-sort">
    <p><fmt:message key="title.options"/></p>
    <div class="admin-menu">
      <a href="${pageContext.request.contextPath}/app/librarian/orders"><fmt:message key="librarian.orders"/></a>
      <a href="${pageContext.request.contextPath}/app/librarian/readers"><fmt:message key="librarian.readers"/></a>
      <a href="${pageContext.request.contextPath}/app/librarian/readingRoom"><fmt:message key="librarian.reading.room"/></a>
    </div>
  </div>
</div>