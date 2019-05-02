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

<spring:message code="provider.name" var="name" />
<spring:message code="provider.surnames" var="surnames" />
<spring:message code="provider.vatNumber" var="vatNumber" />
<spring:message code="provider.creditCard" var="creditCard" />
<spring:message code="provider.creditCard.holder" var="holder" />
<spring:message code="provider.creditCard.make" var="make" />
<spring:message code="provider.creditCard.expMonth" var="expMonth" />
<spring:message code="provider.creditCard.expYear" var="expYear" />
<spring:message code="provider.creditCard.cvv" var="cvv" />
<spring:message code="provider.photo" var="photo" />
<spring:message code="provider.email" var="email" />
<spring:message code="provider.phone" var="phone" />
<spring:message code="provider.address" var="address" />
<spring:message code="provider.makeP" var="makeP" />
<spring:message code="provider.items" var="items" />
<spring:message code="provider.spammer" var="spammer" />
<spring:message code="provider.Tspammer" var="Tspammer" />
<spring:message code="provider.Fspammer" var="Fspammer" />

<spring:message code="provider.return" var="returnMsg" />


	
	<%-- Displays the information of the selected provider --%>
	
	<jstl:out value="${name}" />:
	<jstl:out value="${provider.name}"/>
	<br />
	
	<jstl:out value="${surnames}" />:
	<jstl:out value="${provider.surnames}"/>
	<br />
	
	<jstl:out value="${vatNumber}" />:
	<jstl:out value="${provider.vatNumber}"/>
	<br />
	
	<jstl:out value="${photo}" />:
	<a href="${provider.photo}"><jstl:out value="${provider.photo}"/></a>
	<br />
	
	<jstl:out value="${email}" />:
	<jstl:out value="${provider.email}"/>
	<br />
	
	<jstl:out value="${phone}" />:
	<jstl:out value="${provider.phone}"/>
	<br />
	
	<jstl:out value="${address}" />:
	<jstl:out value="${provider.address}"/>
	<br />
	
	<jstl:out value="${makeP}" />:
	<jstl:out value="${provider.makeP}"/>
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
	
	<spring:url var="itemsUrl"
		value="item/listByProvider.do">
		<spring:param name="varId"
			value="${provider.id}"/>
	</spring:url>
	
	<a href="${itemsUrl}"><jstl:out value="${items}" /></a>
	
	<br />
	<br/>
	
	<fieldset>
	<legend><jstl:out value="${creditCard}" /></legend>
	
	<jstl:out value="${holder}" />:
	<jstl:out value="${provider.creditCard.holder}"/>
	<br />
	
	<jstl:out value="${make}" />:
	<jstl:out value="${provider.creditCard.make}"/>
	<br />
	
	<jstl:out value="${expMonth}" />:
	<jstl:out value="${provider.creditCard.expMonth}"/>
	<br />
	
	<jstl:out value="${expYear}" />:
	<jstl:out value="${provider.creditCard.expYear}"/>
	<br />
	
	<jstl:out value="${cvv}" />:
	<jstl:out value="${provider.creditCard.cvv}"/>
	<br />
	</fieldset>
  		
  	
	<a href="welcome/index.do"><jstl:out value="${returnMsg}" /></a>

