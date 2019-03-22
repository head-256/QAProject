<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.signup" var="signupBundle"/>

<fmt:message key="signup.placeholder.username" var="usernameLocalized" bundle="${ signupBundle }" />
<fmt:message key="signup.placeholder.password" var="passwordLocalized" bundle="${ signupBundle }" />
<fmt:message key="signup.placeholder.email" var="emailLocalized" bundle="${ signupBundle }" />
<fmt:message key="signup.button.signup" var="signupLocalized" bundle="${ signupBundle }" />

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/signup.css">
</head>
<body>

<div class="container-fluid" id="baseContainer">
    <div class="row">
        <div class="col-10 offset-1 col-lg-8 offset-lg-2">
            <!-- From here -->
            <div class="container-fluid  sticky-top  flex-container flex-end">
                <div class="col-6 offset-3 col-lg-4 offset-md-4 signup-menu">
                    <form action="${pageContext.request.contextPath}/controller" method="post" autocomplete="off">
                        <input type="hidden" name="command" value="signup">
                        
                        <label class="signup-label">
                            <fmt:message key="signup.title.username" bundle="${ signupBundle }" var="usernameTitle"/>
                            <fmt:message key="signup.label.username" bundle="${ signupBundle }"/>
                            <br>
                            <input class="signup-input" name="username" pattern="[\p{L}0-9_-]{4,32}"
                                placeholder="${usernameLocalized}" required title="${ usernameTitle }" value="${username}">
                        </label>
                        <label class="error-label">
                            <c:out value="${ usernameErrorMessage }"/>
                        </label>
                        
                        <label class="signup-label">
                            <fmt:message key="signup.label.email" bundle="${ signupBundle }"/>
                            <fmt:message key="signup.title.email" bundle="${ signupBundle }" var="emailTitle"/>
                            <br>
                            <input class="signup-input" name="email" placeholder="${ emailLocalized }"
                                pattern="^[\p{L}0-9._%+-]+@[\p{L}0-9.-]+\.[\p{L}]{2,}$" required title="${emailTitle}" value="${email}">
                        </label>

                        <label class="error-label">
                            <c:out value="${ emailErrorMessage }"/>
                        </label>
                        
                        <fmt:message key="signup.title.password" bundle="${ signupBundle }" var="passwordTitle"/>
                        <label class="signup-label">
                            <fmt:message key="signup.label.password" bundle="${ signupBundle }"/>
                            <br>
                            <input class="signup-input" type="password" name="passwordOne" value=""
                                   placeholder="${ passwordLocalized }"
                                   pattern="^[\w&&[^\s]]{8,16}$" required title="${passwordTitle}">
                        </label>
            
                        <label class="signup-label">
                            <fmt:message key="signup.label.repeatPassword" bundle="${ signupBundle }"/>
                            <br>
                            <input class="signup-input" type="password" name="passwordTwo" value=""
                                    placeholder="${ passwordLocalized }"
                                    pattern="^[\w&&[^\s]]{8,16}$" required title="${passwordTitle}">
                        </label>

                        <label class="error-label">
                            <c:out value="${ passwordErrorMessage }"/>
                        </label>

                        <br>
                        <input class="btn btn-primary signup-btn" type="submit" value="${ signupLocalized }" name="signup">
                        <a class="btn btn-outline-primary signup-btn" href="${pageContext.request.contextPath}/controller?command=show_login">
                            <fmt:message key="signup.label.login" bundle="${ signupBundle }"/>
                        </a>
                    </form>
                </div>
            </div>
            <!-- To here -->
        </div>
    </div>
</div>

</body>
</html>