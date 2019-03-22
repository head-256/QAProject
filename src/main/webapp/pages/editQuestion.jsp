<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.editQuestion" var="editQuestionBundle"/>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editQuestion.css">
</head>
<body>

<div class="container-fluid">
	<div class="col">
		<form action="${pageContext.request.contextPath}/controller" method="post" autocomplete="off">
			<input type="hidden" name="command" value="save_question_changes">
			<input type="hidden" name="question_id" value="${ question.id }">
			<div class="row">
				<div class="col-8 offset-1 col-md-6 offset-md-2">
					<div class="main-container">
						<table class="table">
							<tbody>
								<tr>
									<td>
										<label for="questionTitleInput">
											<fmt:message key="edit.question.label.title" bundle="${ editQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="edit.question.title.title" bundle="${ editQuestionBundle }" var="titleTitle"/>
										<input type="text" name="title" id="questionTitleInput" maxlength="64"
											required value="${ question.title }" title="${ titleTitle }">
										<label class="error-label">
											<c:out value="${ titleErrorMessage }"/>
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<label for="questionTagsInput">
											<fmt:message key="edit.question.label.tags" bundle="${ editQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="edit.question.title.tags" bundle="${ editQuestionBundle }" var="tagsTitle"/>
										<input type="text" name="tags" id="questionTagsInput"
											pattern="\s*([\p{L}0-9_][\p{L}0-9\-\s_]{1,14}[\p{L}0-9_])*(\s*,\s*[\p{L}0-9_][\p{L}0-9\-\s_]{1,14}[\p{L}0-9_]){0,15}" value="${ question.tagsAggregated }" title="${ tagsTitle }">
										<label class="error-label">
											<c:out value="${ tagsErrorMessage }"/>
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<label for="questionTextInput">
											<fmt:message key="edit.question.label.text" bundle="${ editQuestionBundle }"/>
										</label>
									</td>
									<td>
			                    		<fmt:message key="edit.question.title.text" bundle="${ editQuestionBundle }" var="textTitle"/>
										<textarea name="text" rows="6" id="questionTextInput" maxlength="10000" title="${ textTitle }"
											><c:out value="${ question.text }"/></textarea>
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
								<fmt:message key="edit.question.button.publish" var="publishLocalized" bundle="${ editQuestionBundle }" />
                                <input type="submit" name="publish" value="Save changes" class="btn btn-primary side-menu-btn">
								<a class="btn btn-outline-primary side-menu-btn" 
									href="${pageContext.request.contextPath}/controller?command=show_question&question_id=${question.id}">
									<fmt:message key="edit.question.button.return" bundle="${ editQuestionBundle }"/>
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
</body>
</html>
