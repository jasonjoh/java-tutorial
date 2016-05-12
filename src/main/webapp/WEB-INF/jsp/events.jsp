<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
	<caption>Calendar</caption>
	<thead>
		<tr>
			<th>Organizer</th>
			<th>Subject</th>
			<th>Start</th>
			<th>End</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${events}" var="event">
			<tr>
				<td><c:out value="${event.organizer.emailAddress.name}" /></td>
				<td><c:out value="${event.subject}" /></td>
				<td><c:out value="${event.start.dateTime}" /></td>
				<td><c:out value="${event.end.dateTime}" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>