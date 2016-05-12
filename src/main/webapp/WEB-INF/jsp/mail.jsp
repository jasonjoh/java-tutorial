<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
	<caption>Inbox</caption>
	<thead>
		<tr>
			<th><span class="glyphicon glyphicon-envelope"></span></th>
			<th>From</th>
			<th>Subject</th>
			<th>Received</th>
			<th>Preview</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${messages}" var="message">
			<tr class="${message.isRead == true ? '' : 'info'}">
				<td>
					<c:if test="${message.isRead == false}">
						<span class="glyphicon glyphicon-envelope"></span>
					</c:if>
				</td>
				<td><c:out value="${message.from.emailAddress.name}" /></td>
				<td><c:out value="${message.subject}" /></td>
				<td><c:out value="${message.receivedDateTime}" /></td>
				<td><c:out value="${message.bodyPreview}" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>