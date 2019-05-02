<%--
 * edit.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Stored message variables --%>

<spring:message code="item.edit" var="edit" />
<spring:message code="item.description" var="description" />
<spring:message code="item.link" var="link" />
<spring:message code="item.picture" var="picture" />
<spring:message code="item.save" var="save" />
<spring:message code="item.cancel" var="cancel" />
<spring:message code="item.link.warning" var="linkWarning" />


<security:authorize access="hasRole('PROVIDER')">

<form:form action="${requestURI}" modelAttribute="item">

	<%-- Form fields --%>
	
	<form:hidden path="id" />
	
	<acme:textbox code="item.name" path="name"/>
	<acme:textarea code="item.description" path="description" />
	<acme:textbox code="item.link" path="link" placeholder="link"/>
	<acme:textbox code="item.picture" path="picture" placeholder="links.warning"/>
	
	<br/>
		<jstl:out value="${linkWarning}"/>
		<br />
		<br />
		
	<%-- Buttons --%>
	<input type="submit" name="save" value="${save}"/>&nbsp; 
	
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript: relativeRedir('item/provider/list.do');" />

</form:form>

</security:authorize>