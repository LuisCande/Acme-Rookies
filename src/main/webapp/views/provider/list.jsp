<%--
 * suspiciousList.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%-- Stored message variables --%>

<spring:message code="provider.makeP" var="makeP" />
<spring:message code="provider.email" var="email" />
<spring:message code="provider.phone" var="phone" />
<spring:message code="provider.address" var="address" />
<spring:message code="provider.items" var="items" />
<spring:message code="provider.display" var="display" />
<spring:message code="provider.return" var="msgReturn" />

<jsp:useBean id="now" class="java.util.Date"/>

<%-- Listing grid --%>

<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="providers" requestURI="${requestURI}" id="row">
	
	<%-- Attributes --%>
	
	<display:column property="makeP" title="${makeP}" sortable="true" />
	<display:column property="email" title="${email}" sortable="true" />
	<display:column property="phone" title="${phone}" sortable="true" />
	<display:column property="address" title="${address}" sortable="true" />

	<%-- Links towards display, apply, edit and cancel views --%>
	
	<spring:url var="itemsUrl"
		value="item/listByProvider.do">
		<spring:param name="varId"
			value="${row.id}"/>
	</spring:url>
	
	<spring:url var="displayUrl"
		value="provider/display.do">
		<spring:param name="varId"
			value="${row.id}"/>
	</spring:url>
	

	<display:column title="${items}">
			<a href="${itemsUrl}"><jstl:out value="${items}" /></a>
	</display:column>
	
	<display:column title="${display}">
			<a href="${displayUrl}"><jstl:out value="${display}" /></a>
	</display:column>



</display:table>
<a href="welcome/index.do"><jstl:out value="${msgReturn}" /></a>

