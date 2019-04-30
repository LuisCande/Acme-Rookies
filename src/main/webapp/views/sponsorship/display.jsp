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



<spring:message code="sponsorship.banner" var="banner" />
<spring:message code="sponsorship.targetPage" var="targetPage" />
<spring:message code="sponsorship.provider" var="provider" />
<spring:message code="sponsorship.position" var="position" />
<spring:message code="sponsorship.charge" var="charge" />
<spring:message code="sponsorship.creditCard" var="creditCard" />
<spring:message code="sponsorship.creditCard.holder" var="holder" />
<spring:message code="sponsorship.creditCard.make" var="make" />
<spring:message code="sponsorship.creditCard.number" var="number" />
<spring:message code="sponsorship.creditCard.expMonth" var="expMonth" />
<spring:message code="sponsorship.creditCard.expYear" var="expYear" />
<spring:message code="sponsorship.creditCard.cvv" var="cvv" />


<%-- For the selected floatAcme, display the following information: --%>

	<jstl:out value="${banner}" />:
	<a href="${sponsorship.banner}"><jstl:out value="${sponsorship.banner}" /></a>
	<br />
	
	<jstl:out value="${targetPage}" />:
	<a href="${sponsorship.targetPage}"><jstl:out value="${sponsorship.targetPage}"/></a>
	<br />
	
	<jstl:out value="${charge}" />:
	<jstl:out value="${sponsorship.charge}"/>
	<br />
	
	<jstl:out value="${provider}" />:
	<jstl:out value="${sponsorship.provider.name} ${sponsorship.provider.surnames}"/>
	<br />
	
	<fieldset>
		<legend><jstl:out value="${creditCard}"/></legend>
	<jstl:out value="${holder}" />:
	<jstl:out value="${sponsorship.creditCard.holder}"/>
	<br />
	<jstl:out value="${make}" />:
	<jstl:out value="${sponsorship.creditCard.make}"/>
	<br />
	<jstl:out value="${expMonth}" />:
	<jstl:out value="${sponsorship.creditCard.expMonth}"/>
	<br />
	<jstl:out value="${expYear}" />:
	<jstl:out value="${sponsorship.creditCard.expYear}"/>
	<br />
	<jstl:out value="${cvv}" />:
	<jstl:out value="${sponsorship.creditCard.cvv}"/>
	<br />
	</fieldset>
