<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="res"/>
<div class="side-bar">
  <div class="book-sort">
    <p><fmt:message key="title.options"/></p>
    <div class="admin-menu">
      <a href="${pageContext.request.contextPath}/app/admin/books"><fmt:message key="admin.books"/> </a>
      <a href="${pageContext.request.contextPath}/app/admin/librarians"><fmt:message key="admin.librarians"/></a>
      <a href="${pageContext.request.contextPath}/app/admin/users"><fmt:message key="admin.users"/></a>
    </div>
  </div>
</div>