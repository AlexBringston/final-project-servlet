<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="res"/>
<header class="header">
    <div class="container">
        <div class="nav">
            <div class="logo-locale">
                <span class="logo"><a href="${pageContext.request.contextPath}/app/index"><img
                        src="${pageContext.request.contextPath}/static/img/logo.png" alt=""/></a> </span>
                <div class="select">
                    <a href="?locale=ua">UA</a>
                    <a href="?locale=en">EN</a>
                </div>
            </div>
            <ul>
                <c:if test="${(sessionScope.loggedUser != null) && (sessionScope.loggedUser.role.name.equals('ROLE_READER'))}">
                    <li><a
                            href="${pageContext.request.contextPath}/app/reader/abonnement"><fmt:message
                            key="title.abonnement"/></a></li>
                </c:if>
                <c:if test="${sessionScope.loggedUser != null}">
                    <li><a href="${pageContext.request.contextPath}/app/account">
                            ${sessionScope.loggedUser.name}</a></li>
                </c:if>


                <li><a href="${pageContext.request.contextPath}/app/${sessionScope.loggedUser == null ? 'login' : 'logout'}">
                    <img src="${pageContext.request.contextPath}/static/img/user.png" alt=""/></a></li>
            </ul>
        </div>
    </div>
</header>