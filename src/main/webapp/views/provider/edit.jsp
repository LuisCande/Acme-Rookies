<%--
 * edit.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Stored message variables --%>

<spring:message code="provider.edit" var="edit" />
<spring:message code="provider.userAccount.username" var="username" />
<spring:message code="provider.userAccount.password" var="password" />
<spring:message code="provider.name" var="name" />
<spring:message code="provider.surnames" var="surnames" />
<spring:message code="provider.vatNumber" var="vatNumber"/>
<spring:message code="provider.creditCard" var="creditCard" />
<spring:message code="provider.creditCard.holder" var="holder" />
<spring:message code="provider.creditCard.make" var="make" />
<spring:message code="provider.creditCard.number" var="number" />
<spring:message code="provider.creditCard.expMonth" var="expMonth" />
<spring:message code="provider.creditCard.expYear" var="expYear" />
<spring:message code="provider.creditCard.cvv" var="cvv" />
<spring:message code="provider.photo" var="photo" />
<spring:message code="provider.email" var="email" />
<spring:message code="provider.phone" var="phone" />
<spring:message code="provider.address" var="address" />
<spring:message code="provider.save" var="save" />
<spring:message code="provider.cancel" var="cancel" />
<spring:message code="provider.confirm" var="confirm" />
<spring:message code="provider.phone.pattern1" var="phonePattern1" />
<spring:message code="provider.phone.pattern2" var="phonePattern2" />
<spring:message code="provider.phone.warning" var="phoneWarning" />
<spring:message code="provider.phone.note" var="phoneNote" />
<spring:message code="cvv.ph" var="cvvPH" />
<spring:message code="month.ph" var="monthPH" />
<spring:message code="year.ph" var="yearPH" />


<security:authorize access="hasRole('PROVIDER')">

	<form:form id="form" action="${requestURI}"
		modelAttribute="provider">

		<%-- Forms --%>

		<form:hidden path="id" />
		
		<acme:textbox code="provider.name" path="name"/>
		<acme:textbox code="provider.surnames" path="surnames"/>
		<acme:textbox code="provider.makeP" path="makeP"/>
		<acme:textbox code="provider.vatNumber" path="vatNumber"/>
		<fieldset>
		<legend><jstl:out value="${creditCard}"/></legend>
		<acme:textbox code="provider.creditCard.holder" path="creditCard.holder" />
		<acme:textbox code="provider.creditCard.make" path="creditCard.make" />
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
		<acme:textbox code="provider.photo" path="photo" placeholder="link"/>
		<acme:textbox code="provider.email" path="email" placeholder="provider.mail.ph"/>
		<acme:textbox code="provider.phone" path="phone" placeholder="phone.ph"/>
		<acme:textbox code="provider.address" path="address"/>
		
		
		<br>
		<jstl:out value="${phoneWarning}" />
		<br />
		<jstl:out value="${phonePattern1}" />
		<br />
		<jstl:out value="${phonePattern2}" />
		<br />

		<%-- Buttons --%>

		<input type="submit" name="save" value="${save}"
			onclick="return confirm('${confirm}')" />&nbsp;
		
		<acme:cancel url="welcome/index.do" code="provider.cancel" />
	</form:form>
</security:authorize>