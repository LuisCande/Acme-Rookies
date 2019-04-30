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

<%-- Stored message variables --%>

<spring:message code="sponsorship.create" var="create" />
<spring:message code="sponsorship.edit" var="edit" />
<spring:message code="sponsorship.delete" var="delete" />
<spring:message code="sponsorship.confirm.delete" var="confirm" />
<spring:message code="sponsorship.banner" var="banner" />
<spring:message code="sponsorship.targetPage" var="targetPage" />
<spring:message code="sponsorship.provider" var="provider" />
<spring:message code="sponsorship.position" var="position" />
<spring:message code="sponsorship.creditCard" var="creditCard" />
<spring:message code="sponsorship.creditCard.holder" var="holder" />
<spring:message code="sponsorship.creditCard.make" var="make" />
<spring:message code="sponsorship.creditCard.number" var="number" />
<spring:message code="sponsorship.creditCard.expMonth" var="expMonth" />
<spring:message code="sponsorship.creditCard.expYear" var="expYear" />
<spring:message code="sponsorship.creditCard.cvv" var="cvv" />
<spring:message code="cvv.ph" var="cvvPH" />
<spring:message code="month.ph" var="monthPH" />
<spring:message code="year.ph" var="yearPH" />
<spring:message code="sponsorship.save" var="save" />
<spring:message code="sponsorship.cancel" var="cancel" />


<security:authorize access="hasRole('PROVIDER')">

<form:form action="${requestURI}" modelAttribute="sponsorship">

	<%-- Form fields --%>
	
	<form:hidden path="id" />
	
	<acme:textbox code="sponsorship.banner" path="banner" placeholder="link"/>
	<acme:textbox code="sponsorship.targetPage" path="targetPage" placeholder="link"/>
	<fieldset>
		<legend><jstl:out value="${creditCard}"/></legend>
		<acme:textbox code="sponsorship.creditCard.holder" path="creditCard.holder" />
		<acme:textbox code="sponsorship.creditCard.make" path="creditCard.make" />
		<form:label path="creditCard.number">
			<jstl:out value="${number}"/>
		</form:label>	
		<form:input path="creditCard.number" pattern="\d*" placeholder="num."/>
		<form:errors path="creditCard.number" cssClass="error" />
		<br>
		<form:label path="creditCard.expMonth">
			<jstl:out value="${expMonth}"/>
		</form:label>	
		<form:input path="creditCard.expMonth" pattern="\d{1,2}" placeholder="${monthPH}"/>
		<form:errors path="creditCard.expMonth" cssClass="error" />
		<br>
		
		<form:label path="creditCard.expYear">
			<jstl:out value="${expYear}"/>
		</form:label>	
		<form:input path="creditCard.expYear" pattern="\d{4}" placeholder="${yearPH}"/>
		<form:errors path="creditCard.expYear" cssClass="error" />
		<br>
		<form:label path="creditCard.cvv">
			<jstl:out value="${cvv}"/>
		</form:label>	
		<form:input path="creditCard.cvv" pattern="\d{3}" placeholder="${cvvPH}"/>
		<form:errors path="creditCard.cvv" cssClass="error" />
		<br>
		</fieldset>
		<acme:select code="sponsorship.position" path="position"
			items="${positions}" itemLabel="title" id="positions" />
		<br />
	<%-- Buttons --%>
	<input type="submit" name="save" value="${save}"/>&nbsp; 
	
	
		<input type="button" name="cancel" value="${cancel}"
			onclick="javascript: relativeRedir('sponsorship/provider/list.do');" />

</form:form>

</security:authorize>