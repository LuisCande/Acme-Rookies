<%--
 * display.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Stored message variables --%>

<spring:message code="audit.moment" var="moment" />
<spring:message code="audit.text" var="text" />
<spring:message code="audit.auditor" var="auditor" />
<spring:message code="audit.position" var="position" />
<spring:message code="audit.score" var="score" />
<spring:message code="audit.finalMode" var="finalMode" />
<spring:message code="audit.finalMode.false" var="msgFalse" />
<spring:message code="audit.finalMode.true" var="msgTrue" />
<spring:message code="audit.return" var="returnMsg" />
<spring:message code="formatDate" var="formatDate" />

<%-- For the selected floatAcme, display the following information: --%>

	<jstl:out value="${moment}" />:
	<fmt:formatDate value="${audit.moment}" pattern="${formatDate}"/>
	<br />
	
	<jstl:out value="${text}" />
	<jstl:out value="${audit.text}"/>
	<br />
	
	<jstl:out value="${position}" />:
	<jstl:out value="${audit.position.title}"/>
	<br />
	
	<jstl:out value="${auditor}" />:
	<jstl:out value="${audit.auditor.name} ${audit.auditor.surnames}"/>
	<br />
	
	<jstl:out value="${finalMode}" />:
	<jstl:if test="${audit.finalMode eq false}">
		<jstl:out value="${msgFalse}" />
	</jstl:if>
	<jstl:if test="${audit.finalMode eq true}">
		<jstl:out value="${msgTrue}" />
	</jstl:if>
	<br />
	<br>
	
	<a href="welcome/index.do"><jstl:out value="${returnMsg}" /></a>
