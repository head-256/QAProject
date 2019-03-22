<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<fmt:setBundle basename="jsp.users" var="usersBundle"/>
<fmt:message key="users.status.available" var="availableLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.status.banned" var="bannedLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.status.notConfirmed" var="notConfirmedLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.button.removeAdmin" var="removeAdminLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.button.makeAdmin" var="makeAdminLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.button.ban" var="banLocalized" bundle="${ usersBundle }" />
<fmt:message key="users.button.unban" var="unbanLocalized" bundle="${ usersBundle }" />
<jsp:include page="base.jsp"/>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users.css">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-8 offset-2 col-lg-6 offset-md-3 users-container">
        	<div class="row">

			<table class="table table-stripped">
		        <tr>
		            <th>
						<fmt:message key="users.table.id" bundle="${ usersBundle }"/>
					</th>
		            <th>
		            	<fmt:message key="users.table.username" bundle="${ usersBundle }"/>
		        	</th>
		            <th>
		            	<fmt:message key="users.table.email" bundle="${ usersBundle }"/>
		            </th>
		            <th>
		            	<fmt:message key="users.table.privileges" bundle="${ usersBundle }"/>
		            </th>
		            <th>
		            	<fmt:message key="users.table.status" bundle="${ usersBundle }"/>
		            </th>
		            <th>
		            	<fmt:message key="users.status.adminStatus" bundle="${ usersBundle }"/>
		            </th>
		            <th></th>
		        </tr>
		 
		        <c:forEach var="cycleUser" items="${ users }">
		            <tr id="${ cycleUser.id }">
		                <td>
		                	<c:out value="${ cycleUser.id }"/>
		                </td>
		                <td>
		                	<c:out value="${ cycleUser.username }"/>
		                </td>
		                <td>
		                	<c:out value="${ cycleUser.email }"/>
		                </td>
		                <td>
		                	<div class="privilege-td">
		                		<c:out value="${ cycleUser.privilegeLevel }"/>
		                	</div>
		                </td>
		                <td>
		                	<div class="status-td">
		                		<c:if test="${ cycleUser.status == 'BANNED' }">
		                			<c:out value="${ bannedLocalized }"/>
			                	</c:if>
			                	<c:if test="${ cycleUser.status == 'AVAILABLE' }">
		                			<c:out value="${ availableLocalized }"/>
		                		</c:if>
		                	</div>
		                </td>
		                <c:if test="${ user.privilegeLevel > cycleUser.privilegeLevel }">
		                	<c:if test="${ cycleUser.privilegeLevel == 1 }">
		                		<td>
		                			<dev class="btn btn-outline-primary admin-btn unmake-admin-btn">
		            					<fmt:message key="users.button.removeAdmin" bundle="${ usersBundle }"/>
		                			</dev> 
		                		</td>
		                	</c:if>
							<c:if test="${ cycleUser.privilegeLevel == 0 }">
		                		<td>
		                			<dev class="btn btn-outline-primary admin-btn make-admin-btn">
		            					<fmt:message key="users.button.makeAdmin" bundle="${ usersBundle }"/>
		            				</dev> 
		                		</td>
		                	</c:if>
		            	</c:if>
		            	<c:if test="${ user.privilegeLevel > cycleUser.privilegeLevel }">
							<c:if test="${ cycleUser.status == 'BANNED' }">
		                		<td>
		                			<dev class="btn btn-outline-primary ban-btn unban-user-btn">
		            					<fmt:message key="users.button.unban" bundle="${ usersBundle }"/>
		            				</dev> 
		                		</td>
		                	</c:if>
		                	<c:if test="${ cycleUser.status == 'AVAILABLE' }">
		                		<td>
		                			<dev class="btn btn-outline-primary ban-btn ban-user-btn">
		            					<fmt:message key="users.button.ban" bundle="${ usersBundle }"/>
		            				</dev> 
		                		</td>
		                	</c:if>
		            	</c:if>
		            </tr>
		        </c:forEach>
		    </table>

        	</div>
        	<c:if test="${ pages > 1 }">
	        	<div class="row" style="justify-content: center; margin-bottom: 10px;">
	        		<%--For displaying Previous link except for the 1st page --%>
				    <c:if test="${page != 1}">
				        <a class="btn btn-outline-primary" 
				        	href="${pageContext.request.contextPath}/controller?command=show_users&page=${page - 1}">
		            		<fmt:message key="users.pagination.previous" bundle="${ usersBundle }"/>
				        </a>
				    </c:if>
				 
				    <%--For displaying Page numbers. 
				    The when condition does not display a link for the current page--%>
				    
				    <c:if test="${ page > 5 }">
						<c:set var="startPage" value = "${ page - 5 }"/>
				    </c:if>
				    <c:if test="${ page < 5 }">
						<c:set var="startPage" value = "${ 1 }"/>
				    </c:if>

				    <c:if test="${ pages - page > 5 }">
						<c:set var="endPage" value = "${ page + 5 }"/>
				    </c:if>
				    <c:if test="${ pages - page < 5 }">
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
				                        <td><a href="${pageContext.request.contextPath}/controller?command=show_users&page=${i}">${i}</a></td>
				                    </c:otherwise>
				                </c:choose>
				            </c:forEach>
				        </tr>
				    </table>
				     
				    <%--For displaying Next link --%>
				    <c:if test="${page lt pages}">
				        <a class="btn btn-outline-primary"
				        	href="${pageContext.request.contextPath}/controller?command=show_users&page=${page + 1}">
		            		<fmt:message key="users.pagination.next" bundle="${ usersBundle }"/>
				        </a>
				    </c:if>
	        	</div>           
        	</c:if>
        </div>
    </div>
</div>

<div class="d-none" id="jsVariables"
	data-make="${ makeAdminLocalized }"
    data-unmake="${ removeAdminLocalized }"
    data-ban="${ banLocalized }"
    data-unban="${ unbanLocalized }"
    data-available="${ availableLocalized }"
    data-banned="${ bannedLocalized }"
</body>
    <script src="${pageContext.request.contextPath}/js/users.js"></script>
</html>