<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>

<fmt:setBundle basename="jsp.questions" var="questionsBundle"/>
<jsp:include page="base.jsp"/>

<!-- Vars to place in attributes -->
<fmt:message key="questions.button.search" var="searchLocalized" bundle="${ questionsBundle }" />
<fmt:message key="questions.placeholder.tags" var="tagsLocalized" bundle="${ questionsBundle }" />
<fmt:message key="questions.placeholder.title" var="titleLocalized" bundle="${ questionsBundle }" />

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/questions.css">
    </head>
    <body>
        <!-- Search panel, appears on small screens -->
        <div class="container-fluid d-lg-none bg-dark">
            <div class="row">
                <div class="col">
                    <form>
                        <input type="hidden" name="command" value="search">
                        <input type="text" name="title" placeholder="${titleLocalized}">
                        <input type="text" name="tags" placeholder="${tagsLocalized}">
                        <input class="btn btn-outline-primary" type="submit" name="btn" value="${ searchLocalized }">
                    </form>
                </div>
            </div>
        </div>
        <!-- Main container for questions -->
        <div class="container-fluid" id="baseContainer">
            <div class="row">
                <div class="col col-sm-10 offset-sm-1 col-xl-8 offset-xl-2">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col col-lg-9">
                                <c:forEach items="${questions}" var="question">
                                    <!-- One question -->
                                    <div class="container-fluid question-container">
                                        <div class="row">
                                            <!-- User info -->
                                            <div class="d-none d-md-block col-md-2">
                                                <ctg:userpageLink user="${ question.user }">
                                                    <ctg:userAvatar user="${ question.user }"/>
                                                    <c:out value="${question.user.username}"/>
                                                </ctg:userpageLink>
                                                <ul>
                                                    <li>(</li><!--
                                                    --><li class="question-pos-rating"><c:out value="+${question.user.positiveRating}"/></li><!--
                                                    --><li>/</li><!--
                                                    --><li class="question-neg-rating"><c:out value="-${question.user.negativeRating}"/></li><!-- 
                                                    --><li>)</li>
                                                </ul>
                                            </div>
                                            <!-- Question info -->
                                            <div class="col col-md-10">
                                                <div class="row">
                                                    <div class="col-8">
                                                        <h5>
                                                            <a class="question-title"
                                                            href="${pageContext.request.contextPath}/controller?command=show_question&question_id=${question.id}">
                                                                <c:out value="${ question.title }"/>
                                                            </a>
                                                            <c:if test="${ question.status == 'CLOSED' }">
                                                                (<fmt:message key="questions.label.closed" bundle="${ questionsBundle }"/>)
                                                            </c:if>
                                                            <c:if test="${ question.status == 'DELETED' }">
                                                                (<fmt:message key="questions.label.deleted" bundle="${ questionsBundle }"/>)
                                                            </c:if>
                                                        </h5>                                                           
                                                    </div>
                                                    <div class="col-4">
                                                        <span class="question-date float-right d-none d-md-inline-block">
                                                            <fmt:formatDate value="${question.lastEditDate}" type="both" dateStyle="medium" 
                                                                timeStyle="medium"/>
                                                        </span>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-8">
                                                        <div class="col">
                                                            <!-- tags -->
                                                            <c:forEach items="${question.tags}" var="tag">
                                                                <a class="question-tag" href="${pageContext.request.contextPath}/controller?command=show_questions&tags=${tag.text}">
                                                                    <c:out value="${tag.text}"/>
                                                                </a>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="question-text">
                                                    <c:out value="${question.text}"/>
                                                </div>
                                                <a href="${pageContext.request.contextPath}/controller?command=show_question&question_id=${question.id}"
                                                    class="question-answers">
                                                    <c:out value="${question.answersCount}"/> 
                                                    <fmt:message key="questions.label.answers" bundle="${ questionsBundle }" />
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <!-- one question ends -->

                                <c:if test="${ pages > 1 }">
                                    <div class="row" style="justify-content: center;  margin-bottom: 10px;">
                                        <%--For displaying Previous link except for the 1st page --%>
                                        <c:if test="${page != 1}">
                                            <c:set var="linkPrev" 
                                                value="${pageContext.request.contextPath}/controller?command=show_questions&page=${page - 1}"/>
                                            <c:if test="${ (not empty title) || (not empty tags)}">
                                                <c:set var="linkPrev" value = "${pageContext.request.contextPath}/controller?command=show_questions&page=${page - 1}&title=${title}&tags=${tags}"/>
                                            </c:if>
                                            <a class="btn btn-outline-primary" 
                                                href="${ linkPrev }">
                                                <fmt:message key="questions.button.previous" bundle="${ questionsBundle }" />
                                            </a>
                                        </c:if>
                                     
                                        <%--For displaying Page numbers. 
                                        The when condition does not display a link for the current page--%>
                                        
                                        <c:if test="${ page > 5 }">
                                            <c:set var="startPage" value = "${ page - 5 }"/>
                                        </c:if>
                                        <c:if test="${ page <= 5 }">
                                            <c:set var="startPage" value = "${ 1 }"/>
                                        </c:if>

                                        <c:if test="${ pages - page > 5 }">
                                            <c:set var="endPage" value = "${ page + 5 }"/>
                                        </c:if>
                                        <c:if test="${ pages - page <= 5 }">
                                            <c:set var="endPage" value = "${ pages }"/>
                                        </c:if>

                                        <table border="0" cellpadding="5" cellspacing="5" style="font-size: 1.3rem;">
                                            <tr>
                                                <c:forEach begin="${ startPage }" end="${ endPage }" var="i">
                                                    <c:choose>
                                                        <c:when test="${page eq i}">
                                                            <td>${i}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>
                                                                <c:set var="linkNum" 
                                                                value="${pageContext.request.contextPath}/controller?command=show_questions&page=${i}"/>
                                                                <c:if test="${ (not empty title) || (not empty tags)}">
                                                                    <c:set var="linkNum" value = "${pageContext.request.contextPath}/controller?command=show_questions&page=${i}&title=${title}&tags=${tags}"/>
                                                                </c:if>
                                                                <a href="${linkNum}">${i}</a>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </tr>
                                        </table>
                                         
                                        <%--For displaying Next link --%>
                                        <c:if test="${page lt pages}">
                                            <c:set var="linkNext" 
                                                value="${pageContext.request.contextPath}/controller?command=show_questions&page=${page + 1}"/>
                                            <c:if test="${ (not empty title) || (not empty tags)}">
                                                <c:set var="linkNext" value = "${pageContext.request.contextPath}/controller?command=show_questions&page=${page + 1}&title=${title}&tags=${tags}"/>
                                            </c:if>
                                            <a class="btn btn-outline-primary"
                                                href="${ linkNext }">
                                                <fmt:message key="questions.button.next" bundle="${ questionsBundle }" />
                                            </a>
                                        </c:if>
                                    </div>           
                                </c:if>
                            </div>

                            <!-- Side search menu, appears on big screens -->
                            <div class="d-none d-lg-inline-block col col-lg-3">
                                <div class="container-fluid sticky-top ">
                                    <div class="container-fluid side-menu">
                                        <form action="${pageContext.request.contextPath}/controller" method="get" autocomplete="off">
                                            <input type="hidden" name="command" value="show_questions">
                                            <label>
                                                <fmt:message key="questions.label.tags" bundle="${ questionsBundle }" />
                                                <input class="side-menu-input" type="text" name="tags" placeholder="${tagsLocalized}"
                                                    value="${ tags }">
                                            </label>
                                            <c:if test="${ not empty tagsErrorMessage }">
                                                <label class="error-label">
                                                    ${ tagsErrorMessage }
                                                </label>
                                                <c:remove var="tagsErrorMessage"/>
                                            </c:if>
                                            <label class="side-menu-label">
                                                <fmt:message key="questions.label.title" bundle="${ questionsBundle }" />
                                                <input class="side-menu-input" name="title" placeholder="${titleLocalized}"
                                                    value="${ title }">
                                            </label>
                                            <c:if test="${ not empty titleErrorMessage }">
                                                <label class="error-label">
                                                    ${ titleErrorMessage }
                                                </label>
                                                <c:remove var="titleErrorMessage"/>
                                            </c:if>
                                            <div class="btn-group-vertical">
                                                <input class="btn btn-outline-primary side-menu-btn" type="submit" value="${ searchLocalized }" name="search">
                                            </div>
                                        </form>
                                    </div>
                                    <ctg:userAvailable>
                                        <div class="container-fluid side-menu">
                                            <div class="btn-group-vertical">
                                                <a class="btn btn-primary side-menu-btn" href="${pageContext.request.contextPath}/controller?command=show_question_creation">
                                                    <span class="d-xl-none">
                                                        <fmt:message key="questions.button.askQuestion" bundle="${ questionsBundle }" />
                                                    </span>
                                                    <span class="d-none d-xl-inline-block">
                                                        <fmt:message key="questions.button.xl.askQuestion" bundle="${ questionsBundle }" />
                                                    </span>
                                                </a>
                                            </div>
                                        </div>
                                    </ctg:userAvailable>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
    </body>
</html>