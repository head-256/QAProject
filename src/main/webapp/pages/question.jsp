<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.question" var="questionBundle"/>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/question.css">
        <script src="${pageContext.request.contextPath}/js/question.js"></script>
    </head>
    <body>
        <!-- Search panel, appears on small screens -->
        <div class="container-fluid d-lg-none  bg-dark">
            <div class="row">
                <div class="col">
                    <form>
                        <input type="text" name="title" placeholder="Title">
                        <input type="text" name="tags" placeholder="Tags">
                        <input class="btn btn-outline-primary" type="button" name="btn" value="Search">
                    </form>
                </div>
            </div>
        </div>

        <c:if test="${ empty answerErrorMessage }">
            <c:set var = "alertDisplay" value = "none"/>
        </c:if>
        <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert" style="display: ${ alertDisplay };">
            <label id="error-label">
                <c:out value="${ answerErrorMessage }"/>                
            </label>
            <c:remove var="answerErrorMessage" scope="session" />
            <button type="button" class="close" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <!-- Main container for question -->
        <div class="container-fluid" id="baseContainer">
            <div class="row">
                <div class="col col-sm-10 offset-sm-1 col-xl-8 offset-xl-2">
                    <div class="container-fluid">
                        <!-- Question -->
                        <div class="row question-container">
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
                                    <div class="col-9">
                                        <div>
                                            <h5>
                                                <c:out value="${question.title}"/>
                                                <c:if test="${ question.status == 'CLOSED' }">
                                                    (<fmt:message key="question.label.closed" bundle="${ questionBundle }"/>)
                                                </c:if>
                                                <c:if test="${ question.status == 'DELETED' }">
                                                    (<fmt:message key="question.label.deleted" bundle="${ questionBundle }"/>)
                                                </c:if>
                                            </h5>
                                        </div>
                                        <div>
                                            <div class="col">
                                                <!-- tags -->
                                                <c:forEach items="${question.tags}" var="tag">
                                                    <a class="question-tag" href="${pageContext.request.contextPath}/controller?command=show_questions&tags=${tag.text}">
                                                    <c:out value="${tag.text}"/>
                                                    </a>
                                                </c:forEach>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="question-text">
                                                <c:out value="${question.text}"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="container-fluid">
                                            <div class="question-date float-right d-none d-md-inline-block">
                                                <fmt:formatDate value="${question.lastEditDate}" type="both" dateStyle="medium" timeStyle="short"/>
                                            </div>
                                            <div class="btn-group-vertical">
                                                <ctg:userAvailable>
                                                    <c:if test="${ question.status eq 'OPEN' && (question.user.id eq user.id || user.privilegeLevel != 0) }">
                                                        <a class="btn btn-outline-primary owner-button"
                                                            href="${pageContext.request.contextPath}/controller?command=show_edit_question&question_id=${question.id}">
                                                            <fmt:message key="question.button.edit" bundle="${ questionBundle }"/>
                                                        </a>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/controller?command=close_question&question_id=${question.id}" class="btn btn-outline-danger owner-button">
                                                            <fmt:message key="question.button.close" bundle="${ questionBundle }"/>
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${ question.status != 'DELETED' }">
                                                        <ctg:adminRights>
                                                            <a class="btn btn-outline-danger" class="nav-link" href="${pageContext.request.contextPath}/controller?command=delete_question&question_id=${question.id}">
                                                                <fmt:message key="question.button.delete" bundle="${ questionBundle }" />
                                                            </a>   
                                                        </ctg:adminRights>
                                                    </c:if>
                                                </ctg:userAvailable>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Answers -->
                        <div class="row">
                            <div class="col-10 offset-2">
                                <c:forEach items="${question.answers}" var="answer">
                                    <div class="container-fluid answer-container">
                                        <div class="row">
                                            <!-- User info -->
                                            <div class="d-none d-md-block col-md-2">
                                                <ctg:userpageLink user="${ answer.user }">
                                                    <ctg:userAvatar user="${ answer.user }"/>
                                                    <c:out value="${answer.user.username}"/>
                                                </ctg:userpageLink>
                                                <ul>
                                                    <li>(</li><!--
                                                    --><li class="question-pos-rating"><c:out value="+${answer.user.positiveRating}"/></li><!--
                                                    --><li>/</li><!--
                                                    --><li class="question-neg-rating"><c:out value="-${answer.user.negativeRating}"/></li><!-- 
                                                    --><li>)</li>
                                                </ul>
                                            </div>
                                            <div class="col col-md-10">
                                                <div class="row">
                                                    <div class="col-8 answer">
                                                        <div class="answer-text">
                                                            <c:out value="${ answer.text }"/>
                                                        </div>
                                                        <div class="answer-edit" style="display: none;">
                                                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                                                <input type="hidden" name="question_id" value="${ question.id }">
                                                                <input type="hidden" name="answer_id" value="${ answer.id }">
                                                                <input type="hidden" name="command" value="edit_answer">
                                                                <textarea style="width: 100%;" rows="4" name="text"><c:out value="${answer.text}"/></textarea>
                                                                <input type="submit" class="btn btn-outline-primary" name="submit" value="Save">
                                                                <button class="btn btn-outline-danger cancel-answer-edit">
                                                                    <fmt:message key="question.button.back" bundle="${ questionBundle }"/>
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                    <div class="col-4">
                                                        <div class="row">
                                                            <div class="question-date d-none d-md-inline-block">
                                                                <fmt:formatDate value="${answer.lastEditDate}" type="both" dateStyle="medium" 
                                                                    timeStyle="medium"/>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <ctg:adminRights>
                                                                <c:if test="${ answer.deleted }">
                                                                    <fmt:message key="question.label.answerDeleted" bundle="${ questionBundle }"/>
                                                                </c:if>
                                                            </ctg:adminRights>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col">
                                                                <div class="btn-group float-right mt-2" role="group"
                                                                    style="margin-right: 2rem"
                                                                    id="${answer.id}">
                                                                    <c:if test="${ answer.currentUserMark == 1 }">
                                                                        <div class="answer-pos-rating btn btn-success">
                                                                            <c:out value="+${answer.positiveRating}"/>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${ answer.currentUserMark != 1}">
                                                                        <div class="answer-pos-rating btn btn-outline-success">
                                                                            <c:out value="+${answer.positiveRating}"/>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${ answer.currentUserMark == -1 }">
                                                                        <div class="answer-neg-rating btn btn-danger">
                                                                            <c:out value="-${answer.negativeRating}"/>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${ answer.currentUserMark != -1}">
                                                                        <div class="answer-neg-rating btn btn-outline-danger">
                                                                            <c:out value="-${answer.negativeRating}"/>
                                                                        </div>
                                                                    </c:if>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col">
                                                                <form action="${ pageContext.request.contextPath}/controller " method="post">
                                                                <div class="btn-group float-right mt-2" role="group"
                                                                    style="margin-right: 2rem;">
                                                                    <c:if test="${ user.privilegeLevel >= 1 && answer.deleted eq 'false' && 
                                                                        question.status eq 'OPEN'}">
                                                                            <input type="hidden" name="command" value="delete_answer">
                                                                            <input type="hidden" name="question_id" value="${ question.id }">
                                                                            <input type="hidden" name="answer_id" value="${ answer.id }">
                                                                            <button type="submit" class='btn btn-lg' style='background-color:transparent;'>
                                                                                <i class="far fa-trash-alt"></i>
                                                                            </button>
                                                                    </c:if>
                                                                    <c:if test="${ answer.user.id eq user.id && answer.positiveRating eq 0 && 
                                                                        answer.negativeRating eq 0 && question.status eq 'OPEN' && answer.deleted eq false}">
                                                                        <button class='btn btn-lg answer-edit-btn' 
                                                                            style='background-color:transparent;'>
                                                                            <i class="fa fa-edit"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <!-- Answer form -->
                                <ctg:notBanned>
                                    <c:if test="${ question.status eq 'OPEN' }">
                                        <div class="container-fluid answer-container">
                                            <div class="row">
                                                <div class="d-none d-md-block col-md-2">
                                                    <ctg:userpageLink user="${ currentUser }">
                                                        <ctg:userAvatar user="${ currentUser }"/>
                                                        <c:out value="${currentUser.username}"/>
                                                    </ctg:userpageLink>
                                                    <ul>
                                                        <li>(</li><!--
                                                        --><li class="question-pos-rating"><c:out value="+${currentUser.positiveRating}"/></li><!--
                                                        --><li>/</li><!--
                                                        --><li class="question-neg-rating"><c:out value="-${currentUser.negativeRating}"/></li><!-- 
                                                        --><li>)</li>
                                                    </ul>
                                                </div>
                                                <div class="col col-md-10">
                                                    <form action="${ pageContext.request.contextPath}/controller" style="width: 100%;" method="post">
                                                        <input type="hidden" name="command" value="create_answer">
                                                        <input type="hidden" name="question_id" value="${ question.id }">
                                                        <textarea name="text" style="width: 100%;" rows="4"></textarea>
                                                        <fmt:message key="question.button.submit" var="signupLocalized" bundle="${ questionBundle }" />
                                                        <input type="submit" name="submit" value="${ signupLocalized }" class="btn btn-outline-primary" ">
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </ctg:notBanned>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <script src="${pageContext.request.contextPath}/js/question.js"></script>
        </div> 
    </body>
</html>