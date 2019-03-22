<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html lang="en">
<body>
    <c:set var = "lowerCaseCommand" value="${fn:toLowerCase(command)}"/>
	<c:choose>
		<c:when test="${ lowerCaseCommand eq 'show_question' }">
			<c:redirect url="/controller?command=${lowerCaseCommand}&question_id=${question_id}"/>
		</c:when>
		<c:when test="${ lowerCaseCommand eq 'show_owner_user_page' }">
			<c:redirect url="/controller?command=${lowerCaseCommand}&question_id=${question_id}"/>
		</c:when>
		<c:otherwise>
			<c:redirect url="/controller?command=${lowerCaseCommand}"/>
		</c:otherwise>
	</c:choose>
</body>
</html>