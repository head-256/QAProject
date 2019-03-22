<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.createQuestion" var="createQuestionBundle"/>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/createQuestion.css">
</head>
<body>

<div class="container-fluid">
	<div class="col">
		<form action="${pageContext.request.contextPath}/controller" method="post" autocomplete="off">
			<div class="row">
				<div class="col-8 offset-1 col-md-6 offset-md-2">
					<div class="main-container">
						<input type="hidden" name="command" value="create_question">
						<table class="table">
							<tbody>
								<tr>
									<td>
										<label for="questionTitleInput">
											<fmt:message key="create.question.label.title" bundle="${ createQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="create.question.title.title" bundle="${ createQuestionBundle }" var="titleTitle"/>
										<input type="text" name="title" id="questionTitleInput" maxlength="64" minlength="4" 
											required title="${ titleTitle }" value="${ title }">
						                <c:if test="${ not empty titleErrorMessage }">
						                	<br/>
						                    <label class="error-label">
						                        <c:out value="${ titleErrorMessage }"/>
						                    </label>
						                </c:if>
									</td>
								</tr>
								<tr>
									<td>
										<label for="questionTagsInput">
											<fmt:message key="create.question.label.tags" bundle="${ createQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="create.question.title.tags" bundle="${ createQuestionBundle }" var="tagsTitle"/>
										<input type="text" name="tags" id="questionTagsInput"
											pattern="\s*([\p{L}0-9_][\p{L}0-9\-\s_]{1,14}[\p{L}0-9_])*(\s*,\s*[\p{L}0-9_][\p{L}0-9\-\s_]{1,14}[\p{L}0-9_]){0,15}"
											required title="${ tagsTitle }" value="${ tags }">
										<label class="error-label">
						                    <c:out value="${ tagsErrorMessage }"/>
						                </label>
									</td>
								</tr>
								<tr>
									<td>
										<label for="questionTextInput">
											<fmt:message key="create.question.label.text" bundle="${ createQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="create.question.title.text" bundle="${ createQuestionBundle }" var="textTitle"/>
										<textarea name="text" rows="6" id="questionTextInput" maxlength="10000" title="${ textTitle }">${text}</textarea>
										<label class="error-label">
						                    <c:out value="${ textErrorMessage }"/>
						                </label>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="col-2">
                    <div class="container-fluid sticky-top ">
                    	<div class="container-fluid side-menu">
                        	<div class="btn-group-vertical">
								<fmt:message key="create.question.button.publish" var="publishLocalized" bundle="${ createQuestionBundle }" />
                                <input type="submit" name="publish" value="${ publishLocalized }" class="btn btn-primary side-menu-btn">
								<a class="btn btn-outline-primary side-menu-btn" href="${pageContext.request.contextPath}/controller?command=show_questions">
									<fmt:message key="create.question.button.return" bundle="${ createQuestionBundle }"/>
								</a>
                            </div>
                        </div>
                    </div>                
                </div>
			</div>
		</form>
	</div>
</div>
<div class="d-none" id="jsVariables"
 	data-text_placeholder="DefaultPlaceholder">
</div>	

   
<script src="${pageContext.request.contextPath}/js/createQuestion.js"></script>

</body>
</html>
