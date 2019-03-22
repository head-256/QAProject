<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>

<fmt:setBundle basename="jsp.base" var="baseBundle"/>

<html lang="en">
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <title><fmt:message key="base.label.title" bundle="${ baseBundle }" /></title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    </head>
<body>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" 
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" 
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    
    <script src="https://code.jquery.com/jquery-3.3.1.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" 
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

    <!-- Navigation bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a href="#" class="navbar-brand">
            <img src="https://png.icons8.com/ios/2x/circled-q-filled.png" width="30" height="30" alt="logo">
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a href="${pageContext.request.contextPath}/controller?command=show_questions" class="nav-link">
                        <fmt:message key="base.label.questions" bundle="${ baseBundle }"/>
                    </a>
                </li>
                <li class="nav-item d-lg-none">
                    <div class="btn btn-primary">
                        <fmt:message key="base.label.askQuestion" bundle="${ baseBundle }" />
                    </div>
                </li>
                <ctg:adminRights>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/controller?command=show_users&page=1" class="nav-link">
                            <fmt:message key="base.label.users" bundle="${ baseBundle }" />
                        </a>   
                    </li>
                </ctg:adminRights>
            </ul>
            <div class="nav-item">
                <c:choose>
                    <c:when test="${ not empty user }"> 
                        <div class="dropdown">
                            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <c:out value="${ user.username }"/>
                            </button>
                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <a class="dropdown-item" 
                                    href="${pageContext.request.contextPath}/controller?command=show_owner_user_page">
                                    <fmt:message key="base.label.profile" bundle="${ baseBundle }" />
                                </a>
                                <a class="dropdown-item" 
                                    href="${pageContext.request.contextPath}/controller?command=logout">
                                    <fmt:message key="base.label.logout" bundle="${ baseBundle }" />
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/controller?command=show_login">
                            <fmt:message key="base.label.login" bundle="${ baseBundle }" />
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="nav-item">
                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            ${ sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'].displayLanguage }
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item" id="dropdownRussian" href="${pageContext.request.contextPath}/controller?command=change_locale&locale=ru_RU">
                            Русский
                        </a>
                        <a class="dropdown-item" id="dropdownEnglish" href="${pageContext.request.contextPath}/controller?command=change_locale&locale=en_EN">
                            English
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </nav>

    <c:if test="${ user.status eq 'BANNED' }">
        <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert"   >
            <label id="error-label">
                <fmt:message key="base.alert.banned" bundle="${ baseBundle }" />        
            </label>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>
    <c:if test="${ not empty alertErrorMessage }">
        <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert"   >
            <label id="error-label">
                <c:out value="${alertErrorMessage}"/>
            </label>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <c:remove var="alertErrorMessage"/>
        </div>
    </c:if>
    <c:if test="${ not empty alertSuccessMessage }">
        <div id="error-alert" class="alert alert-success alert-dismissible fade show" role="alert"   >
            <label id="error-label">
                <c:out value="${alertSuccessMessage}"/>
            </label>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <c:remove var="alertSuccessMessage"/>
        </div>
    </c:if>
</body>
</html>