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

<spring:message code="rookie.name" var="name" />
<spring:message code="rookie.surnames" var="surnames" />
<spring:message code="rookie.vatNumber" var="vatNumber" />
<spring:message code="rookie.creditCard" var="creditCard" />
<spring:message code="rookie.creditCard.holder" var="holder" />
<spring:message code="rookie.creditCard.make" var="make" />
<spring:message code="rookie.creditCard.expMonth" var="expMonth" />
<spring:message code="rookie.creditCard.expYear" var="expYear" />
<spring:message code="rookie.creditCard.cvv" var="cvv" />
<spring:message code="rookie.photo" var="photo" />
<spring:message code="rookie.email" var="email" />
<spring:message code="rookie.phone" var="phone" />
<spring:message code="rookie.address" var="address" />
<spring:message code="rookie.spammer" var="spammer" />
<spring:message code="rookie.Tspammer" var="Tspammer" />
<spring:message code="rookie.Fspammer" var="Fspammer" />

<spring:message code="rookie.return" var="returnMsg" />


	
	<%-- Displays the information of the selected rookie --%>
	
	<jstl:out value="${name}" />:
	<jstl:out value="${rookie.name}"/>
	<br />
	
	<jstl:out value="${surnames}" />:
	<jstl:out value="${rookie.surnames}"/>
	<br />
	
	<jstl:out value="${vatNumber}" />:
	<jstl:out value="${rookie.vatNumber}"/>
	<br />
	
	<jstl:out value="${photo}" />:
	<a href="${rookie.photo}"><jstl:out value="${rookie.photo}"/></a>
	<br />
	
	<jstl:out value="${email}" />:
	<jstl:out value="${rookie.email}"/>
	<br />
	
	<jstl:out value="${phone}" />:
	<jstl:out value="${rookie.phone}"/>
	<br />
	
	<jstl:out value="${address}" />:
	<jstl:out value="${rookie.address}"/>
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
	<jstl:out value="${rookie.creditCard.holder}"/>
	<br />
	
	<jstl:out value="${make}" />:
	<jstl:out value="${rookie.creditCard.make}"/>
	<br />
	
	<jstl:out value="${expMonth}" />:
	<jstl:out value="${rookie.creditCard.expMonth}"/>
	<br />
	
	<jstl:out value="${expYear}" />:
	<jstl:out value="${rookie.creditCard.expYear}"/>
	<br />
	
	<jstl:out value="${cvv}" />:
	<jstl:out value="${rookie.creditCard.cvv}"/>
	<br />
	</fieldset>
  		
	<a href="welcome/index.do"><jstl:out value="${returnMsg}" /></a>

