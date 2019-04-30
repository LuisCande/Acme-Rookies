<%--
 * dashboard.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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

<%-- Stored message variables --%>

<spring:message code="auditor.name" var="name" />
<spring:message code="auditor.surnames" var="surnames" />
<spring:message code="auditor.vatNumber" var="vatNumber" />
<spring:message code="auditor.creditCard" var="creditCard" />
<spring:message code="auditor.creditCard.holder" var="holder" />
<spring:message code="auditor.creditCard.make" var="make" />
<spring:message code="auditor.creditCard.expMonth" var="expMonth" />
<spring:message code="auditor.creditCard.expYear" var="expYear" />
<spring:message code="auditor.creditCard.cvv" var="cvv" />
<spring:message code="auditor.photo" var="photo" />
<spring:message code="auditor.email" var="email" />
<spring:message code="auditor.phone" var="phone" />
<spring:message code="auditor.address" var="address" />
<spring:message code="auditor.spammer" var="spammer" />
<spring:message code="auditor.Tspammer" var="Tspammer" />
<spring:message code="auditor.Fspammer" var="Fspammer" />

<spring:message code="auditor.return" var="returnMsg" />


	
	<%-- Displays the information of the selected auditor --%>
	
	<jstl:out value="${name}" />:
	<jstl:out value="${auditor.name}"/>
	<br />
	
	<jstl:out value="${surnames}" />:
	<jstl:out value="${auditor.surnames}"/>
	<br />
	
	<jstl:out value="${vatNumber}" />:
	<jstl:out value="${auditor.vatNumber}"/>
	<br />
	
	<jstl:out value="${photo}" />:
	<a href="${auditor.photo}"><jstl:out value="${auditor.photo}"/></a>
	<br />
	
	<jstl:out value="${email}" />:
	<jstl:out value="${auditor.email}"/>
	<br />
	
	<jstl:out value="${phone}" />:
	<jstl:out value="${auditor.phone}"/>
	<br />
	
	<jstl:out value="${address}" />:
	<jstl:out value="${auditor.address}"/>
	<br />
	
	<security:authorize access="hasRole('ADMIN')" >
	<jstl:if test="${actor.evaluated eq true}">
	<jstl:out value="${spammer}" />:
	<jstl:if test="${actor.spammer eq true}">
	<jstl:out value="${Tspammer}"/>
	</jstl:if>
	<jstl:if test="${actor.spammer eq false}">
	<jstl:out value="${Fspammer}"/>
	</jstl:if>
	<br />
	</jstl:if>
	<jstl:if test="${actor.evaluated eq false}">
	<jstl:out value="${spammer}" />:
	<jstl:out value="N/A"/>
	<br />
	</jstl:if>
  	</security:authorize>
	
	<br />
	
	<fieldset>
	<legend><jstl:out value="${creditCard}" /></legend>
	
	<jstl:out value="${holder}" />:
	<jstl:out value="${auditor.creditCard.holder}"/>
	<br />
	
	<jstl:out value="${make}" />:
	<jstl:out value="${auditor.creditCard.make}"/>
	<br />
	
	<jstl:out value="${expMonth}" />:
	<jstl:out value="${auditor.creditCard.expMonth}"/>
	<br />
	
	<jstl:out value="${expYear}" />:
	<jstl:out value="${auditor.creditCard.expYear}"/>
	<br />
	
	<jstl:out value="${cvv}" />:
	<jstl:out value="${auditor.creditCard.cvv}"/>
	<br />
	</fieldset>
  		
	<a href="welcome/index.do"><jstl:out value="${returnMsg}" /></a>

