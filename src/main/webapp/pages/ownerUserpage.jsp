<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>


<jsp:include page="base.jsp"/>
<fmt:setBundle basename="jsp.ownerUserpage" var="ownerUserpageBundle"/>

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
            	<div class="col-5">
            		<div class="container-fluid userpage-container">
                        <ctg:userAvatar user="${ userpageUser }"/>
						<div class="row" style="width: 100%">    
							<ctg:userAvailable>
								<div class="col">        		
				            		<form method="post" action="${pageContext.request.contextPath}/controller" enctype="multipart/form-data"
				            			id="uploadForm">
				            			<input type="hidden" name="command" value="upload_avatar">
				            			<label class="btn btn-outline-primary btn-file" style="width: 100%;">
											<fmt:message key="owner.userpage.button.browse" bundle="${ ownerUserpageBundle }"/>
				            				<input type="file" name="multiPartServlet" id="fileChooser" style="display: none;" accept=".png,.img,.jpg"/>
										</label>
									</form>         
								</div>  	
							</ctg:userAvailable>
							<c:if test="${ not empty userpageUser.avatarPath }">
								<div class="col">
									<a href="${pageContext.request.contextPath}/controller?command=delete_avatar" class="btn btn-outline-danger"
									style="width: 100%;">
										<fmt:message key="owner.userpage.button.delete" bundle="${ ownerUserpageBundle }"/>
									</a>	
								</div>
							</c:if>
						</div>
            		</div>
            		<div class="container-fluid userpage-container">
						<ul>
							<li><fmt:message key="owner.userpage.label.rating" bundle="${ ownerUserpageBundle }"/></li><!--
                            --><li class="pos-rating"><c:out value="+${ userpageUser.positiveRating }"/></li><!--
                            --><li>/</li><!--
                            --><li class="neg-rating"><c:out value="-${ userpageUser.negativeRating }"/></li>
                        </ul>
            		</div>
				</div>
            	
            	<div class="col-7">
            		<div class="container-fluid userpage-container text-right">
		            	<table class="userpage-table">
						  	<tr>
						    	<td class="userpage-table-td">
		           					<label for="usernameInput">            						
			           					<fmt:message key="owner.userpage.label.username" bundle="${ ownerUserpageBundle }"/>
		           					</label>
		           				</td>
						    	<td class="userpage-table-input-td" style="width: 75%;">
						    		<label style="width: 100%">
						    			<c:out value="${ userpageUser.username }"/>
							    	</label>
							    </td>
							</tr>
						</table>
            		</div>
	            	<div class="container-fluid userpage-container text-right">
			           	<form action="${pageContext.request.contextPath}/controller" method="post" autocomplete="off">
			           		<input type="hidden" name="command" value="change_email">
			           		<table class="userpage-table">
							  	<tr>
							    	<td class="userpage-table-td">
							    		<label>
							    			<fmt:message key="owner.userpage.label.email" bundle="${ ownerUserpageBundle }"/>
								    	</label>
			           				</td>
							    	<td class="userpage-table-input-td">
							    		<input class="userpage-table-input" type="text" name="email" 
							    			value="${ userpageUser.email }" id="emailInput">
							    		<c:if test="${ not empty emailErrorMessage }">
							    			<label class="error-label">${emailErrorMessage}</label>
							    			<c:remove var="emailErrorMessage"/>
							    		</c:if>
							    	</td>
								</tr>
							</table>
							<fmt:message key="owner.userpage.button.saveChanges" var="saveChangesLocalized" bundle="${ ownerUserpageBundle }" />
			                <fmt:message key="owner.userpage.title.email" bundle="${ ownerUserpageBundle }" var="emailTitle"/>
				           	<input type="submit" 
				           		name="save" 
				           		class="btn btn-outline-danger userpage-save-button" 
				           		id="saveChangesButton" 
				           		value="${ saveChangesLocalized }" 
					           	disabled
					           	title="${ emailTitle }">
				        </form>
	            	</div>
					<ctg:notBanned>
	            		<div class="container-fluid userpage-container text-right">
			            	<form id="passwordForm" action="${pageContext.request.contextPath}/controller" method="post" autocomplete="off">
			            		<input type="hidden" name="command" value="change_password">
				                <fmt:message key="owner.userpage.title.password" bundle="${ ownerUserpageBundle }" var="passwordTitle"/>
			            		<table class="userpage-table">
								  	<tr>
								    	<td class="userpage-table-td">
			            					<label for="usernameInput">            						
				            					<fmt:message key="owner.userpage.label.password" bundle="${ ownerUserpageBundle }"/> 
			            					</label>
			            				</td>
								    	<td class="userpage-table-input-td">
								    		<input class="userpage-table-input" type="password" name="passwordOne" id="passwordOneInput"
								    			placeholder="********"
								    			required
								    			pattern="^\w{8,16}$"
								    			title="${ passwordTitle }" />
								    	</td>
								  	</tr>
								  	<tr>
								    	<td class="userpage-table-td">
								    		<label>
				            					<fmt:message key="owner.userpage.label.repeatPassword" bundle="${ ownerUserpageBundle }"/> 
								    		</label>
			            				</td>
								    	<td class="userpage-table-input-td">
								    		<input class="userpage-table-input" type="password" name="passwordTwo" id="passwordTwoInput"
								    			placeholder="********"
								    			required
								    			pattern="^\w{8,16}$"
								    			title="${ passwordTitle }" /
								    			>
								    		<c:if test="${ not empty passwordErrorMessage }">
								    			<label class="error-label">${passwordErrorMessage}</label>
								    			<c:remove var="passwordErrorMessage"/>
								    		</c:if>
								    	</td>
								  	</tr>
								  	<c:if test="${ not empty passwordErrorMessage }">
								  		<tr>
								  			<td></td>
								  			<td>
								                <br>
								                <label class="errorLabel">
								                    <c:out value="${ passwordErrorMessage }"/>
								                </label>
								                <br>
								  			</td>
								  		</tr>
								    </c:if>
								</table>
								<ctg:notBanned>
									<fmt:message key="owner.userpage.button.changePassword" var="changePasswordLocalized" bundle="${ ownerUserpageBundle }" />
					            	<input type="submit" name="save" class="btn btn-outline-danger userpage-save-button" 
					            		id="savePswdButton" value="${ changePasswordLocalized }" disabled="true">
								</ctg:notBanned>
				            </form>
	            		</div>
			        </ctg:notBanned>
            	</div>
            </div>
            <div class="row">
	        	<div class="col">
		            <div class="card">
		                <div class="card-header" id="headingOne">
		                    <h5 class="mb-0">
		                        <button class="btn btn-link" data-toggle="collapse" data-target="#collapseOne"
		                        	aria-expanded="true" aria-controls="collapseOne">
		                            <fmt:message key="owner.userpage.label.questions" bundle="${ ownerUserpageBundle }"/> 
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
										            <fmt:message key="owner.userpage.label.noQuestions" bundle="${ ownerUserpageBundle }"/> 
										        </th>
										        <th scope="col">
										          	<fmt:message key="owner.userpage.table.lastEditDate" bundle="${ ownerUserpageBundle }"/> 
										        </th>
									            <th scope="col">
									            	<fmt:message key="owner.userpage.table.answers" bundle="${ ownerUserpageBundle }"/> 
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
								    	<fmt:message key="owner.userpage.label.noQuestions" bundle="${ ownerUserpageBundle }"/> 
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
    	data-email="${ userpageUser.email }">
    </div>
    <script src="${pageContext.request.contextPath}/js/ownerUserpage.js"></script>
</div>
</body>
</html>