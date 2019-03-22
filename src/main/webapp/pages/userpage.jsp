<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.userpage" var="userpageBundle"/>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ownerUserpage.css">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-8 offset-2 col-lg-6 offset-md-3">
            <!-- Main info -->
            <div class="row">
            	<!-- Avatar -->
            	<div class="col-6">
            		<div class="container-fluid userpage-container">
            			<c:if test="${ not empty userpageUser.avatarPath}">
							<img class="author-img rounded-circle" style="margin-top: 10px; margin-bottom: 10px; width: 100%;" src="${pageContext.request.contextPath}/image?image=avatars/${userpageUser.username}${userpageUser.avatarPath}">
                        </c:if>
                        <c:if test="${ empty userpageUser.avatarPath}">
                        	<img class="author-img rounded-circle" style="margin-top: 10px; margin-bottom: 10px; width: 100%;" 
                        	src="${pageContext.request.contextPath}/image">
                        </c:if>
            		</div>			
				</div>
            	
            	<div class="col-6">
            		<div class="container-fluid userpage-container text-right">
		            	<table class="table userpage-table">
							<tr>
								<td class="userpage-table-td">
		            				<label for="usernameInput">    
			            				<fmt:message key="userpage.table.username" bundle="${ userpageBundle }"/>
		            				</label>
		            			</td>
							   	<td class="userpage-table-input-td">
							   		<label>
							   			<c:out value="${ userpageUser.username }"/>
							   		</label>
							   	</td>
						  	</tr>
						  	<tr>
						    	<td class="userpage-table-td">
						    		<label>
			            				<fmt:message key="userpage.table.email" bundle="${ userpageBundle }"/>
						    		</label>
	            				</td>
							    <td class="userpage-table-input-td">
							   		<label>
							   			<c:out value="${ userpageUser.email }"/>
							   		</label>
							   	</td>
							</tr><tr>
						    	<td class="userpage-table-td">
						    		<label>
			            				<fmt:message key="userpage.table.privilegesLevel" bundle="${ userpageBundle }"/>
						    		</label>
	            				</td>
							    <td class="userpage-table-input-td">
							   		<label>
							   			<c:out value="${ userpageUser.privilegeLevel }"/>
							   		</label>
							   	</td>
							</tr>
						  	<tr>
						    	<td class="userpage-table-td">
						    		<label>
			            				<fmt:message key="userpage.table.rating" bundle="${ userpageBundle }"/>
						    		</label>
	            				</td>
							    <td class="userpage-table-input-td">
							   		<ul>
										<li class="pos-rating"><c:out value="+${ userpageUser.positiveRating }"/></li><!--
			                            --><li>/</li><!--
			                            --><li class="neg-rating"><c:out value="-${ userpageUser.negativeRating }"/></li>
			                        </ul>
							   	</td>
							</tr>
						</table>
            		</div>
            	</div>
            </div>
            <div class="row">
            	<div class="col">
	                <div class="card">
	                    <div class="card-header" id="headingOne">
	                        <h5 class="mb-0">
	                            <button class="btn btn-link" data-toggle="collapse" data-target="#collapseOne"
	                            	aria-expanded="true" aria-controls="collapseOne">
			            			<fmt:message key="userpage.button.questions" bundle="${ userpageBundle }"/>
	                            </button>
	                        </h5>
	                    </div>
	                	<div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
	                    	<div class="card-body">
	                    		<c:if test="${ not empty questions}">
								    <table class="table table-striped">
								        <thead>
									        <tr>
									            <th scope="col">
							            			<fmt:message key="userpage.table.title" bundle="${ userpageBundle }"/>
									            </th>
									            <th scope="col">
									            	<fmt:message key="userpage.table.lastEditDate" bundle="${ userpageBundle }"/>
									        	</th>
									            <th scope="col">
									            	<fmt:message key="userpage.table.answers" bundle="${ userpageBundle }"/>
									            </th>
									        </tr>
								        </thead>
								        <tbody>
									        <c:forEach items="${questions}" var="question">
									            <tr>
									                <td>
									                	<a href="${pageContext.request.contextPath}/controller?command=show_question&question_id=${question.id}">
									                		<c:out value="${ question.title }"/>
									                	</a>
									                </td>
									                <td>
									                	<c:out value="${ question.lastEditDate }"/>
									                </td>
									                <td>
									                	<c:out value="${ question.answersCount }"/>
									                </td>
									            </tr>
									        </c:forEach>
								        </tbody>
								    </table>
								</c:if>
								<c:if test="${ empty questions }">
								    <label>
								    	<fmt:message key="userpage.label.noQuestions" bundle="${ userpageBundle }"/>
								    </label>
								</c:if>
	                        </div>
	                    </div>
	                </div>
            	</div>
            </div>
        </div>
    </div>
    <div class="d-none" id="jsVariables"
    	data-username="${ userpageUser.username }"
    	data-email="${ userpageUser.email }">
    </div>
    <script src="${pageContext.request.contextPath}/js/ownerUserpage.js"></script>
</div>
</body>
</html>