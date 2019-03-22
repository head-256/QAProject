<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.login" var="loginBundle"/>

<fmt:message key="login.placeholder.username" var="usernameLocalized" bundle="${ loginBundle }" />
<fmt:message key="login.placeholder.password" var="passwordLocalized" bundle="${ loginBundle }" />
<fmt:message key="login.button.login" var="loginLocalized" bundle="${ loginBundle }" />

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-6 offset-3 col-lg-4 offset-md-4 login-menu">
            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="login"/>
                <label class="login-label">
                    <fmt:message key="login.label.username" bundle="${ loginBundle }"/>
                    <br>
                    <fmt:message key="login.title.password" bundle="${ loginBundle }" var="usernameTitle"/>
                    <input class="login-input" 
                        name="username" 
                        pattern="\p{L}[\p{L}0-9_]{5,19}" 
                        placeholder="${usernameLocalized}"
                        value="${username}"
                        required
                        title="${usernameTitle}">
                </label>
                        
                <c:if test="${ not empty usernameErrorMessage }">
                    <br>
                    <label class="error-label">
                        <c:out value="${ usernameErrorMessage }"/>
                    </label>
                    <br>
                </c:if>

                <c:if test="${ empty usernameErrorMessage }">
                    <br>
                </c:if>
                <label class="login-label">
                    <fmt:message key="login.label.password" bundle="${ loginBundle }"/>
                    <br>
                    <fmt:message key="login.title.password" bundle="${ loginBundle }" var="passwordTitle"/>
                    <input class="login-input" type="password" 
                        name="password"
                        placeholder="${passwordLocalized}"
                        pattern="^[\p{L}0-9_]{8,16}$"
                        required
                        title="${passwordTitle}">
                </label>

                <c:if test="${ not empty passwordErrorMessage }">
                    <br>
                    <label class="error-label">
                        <c:out value="${ passwordErrorMessage }"/>
                    </label>
                </c:if> 
                <c:if test="${ not empty loginErrorMessage }">
                    <br>
                    <label class="error-label">
                        <c:out value="${ loginErrorMessage }"/>
                    </label>
                </c:if>

                <br>
                <input class="btn btn-primary login-btn" type="submit" value="${loginLocalized}" name="login"/>
                <a class="btn btn-outline-primary login-btn" href="${pageContext.request.contextPath}/controller?command=show_signup">
                    <fmt:message key="login.button.signup" bundle="${ loginBundle }"/>
                </a>
            </form>
        </div>
    </div>
</div>
</body>
</html>