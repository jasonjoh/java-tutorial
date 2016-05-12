<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><tiles:getAsString name="title"></tiles:getAsString></title>
	
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
	
	<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-2.2.3.min.js"></script>
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
	<tilesx:useAttribute name="current" />
	<div class="container">

		<!-- Static navbar -->
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#navbar" aria-expanded="false"
						aria-controls="navbar">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="<spring:url value="/" />">Java Outlook Tutorial</a>
				</div>
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li class="${current == 'index' ? 'active' : '' }">
							<a href="<spring:url value="/" />">Home</a>
						</li>
						<c:if test="${userConnected eq true}">
							<li class="${current == 'mail' ? 'active' : '' }">
								<a href="<spring:url value="/mail.html" />">Mail</a>
							</li>
							<li class="${current == 'events' ? 'active' : '' }">
								<a href="<spring:url value="/events.html" />">Events</a>
							</li>
							<li class="${current == 'contacts' ? 'active' : '' }">
								<a href="<spring:url value="/contacts.html" />">Contacts</a>
							</li>
						</c:if>
					</ul>
					<c:if test="${userConnected eq true}">
						<p class="navbar-text navbar-right">Signed in as ${userName}</p>
						<ul class="nav navbar-nav navbar-right">
							<li>
								<a href="<spring:url value="/logout.html" />">Logout</a>
							</li>
						</ul>
					</c:if>
				</div>
			</div>
		</nav>

		<tiles:insertAttribute name="body" />
	</div>
</body>
</html>