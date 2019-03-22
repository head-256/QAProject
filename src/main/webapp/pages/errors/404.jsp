<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<jsp:include page="../base.jsp"/>
<fmt:setBundle basename="jsp.errors" var="errorBundle"/>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/errorPage.css">
</head>
<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-6 offset-3">
            <div class="container-fluid error-message-container">
                <h1>
        			<fmt:message key="error.label.error" bundle="${ errorBundle }"/>
        			404:
                    <fmt:message key="error.label.pageNotFound" bundle="${ errorBundle }"/>
                </h1>
                <h4>
                    <fmt:message key="error.label.pageNotFoundMessage" bundle="${ errorBundle }"/>
                </h4>
            </div>
        </div>
    </div>
</div>

</body>
</html>
