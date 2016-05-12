<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<table class="table">
	<caption>Contacts</caption>
	<thead>
		<tr>
			<th>Name</th>
			<th>Company</th>
			<th>Email</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${contacts}" var="contact">
			<tr>
				<td><c:out value="${contact.givenName} ${contact.surname}" /></td>
				<td><c:out value="${contact.companyName}" /></td>
				<td>
					<ul class="list-inline">
						<c:forEach items="${contact.emailAddresses}" var="address">
							<li><c:out value="${address.address}" /></li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>