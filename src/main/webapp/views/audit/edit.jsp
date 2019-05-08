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

<spring:message code="audit.create" var="create" />
<spring:message code="audit.edit" var="edit" />
<spring:message code="audit.delete" var="delete" />
<spring:message code="audit.confirm.delete" var="confirm" />
<spring:message code="audit.moment" var="moment" />
<spring:message code="audit.text" var="text" />
<spring:message code="audit.score" var="score" />
<spring:message code="audit.position" var="position" />
<spring:message code="audit.finalMode" var="finalMode" />
<spring:message code="audit.auditor" var="auditor" />
<spring:message code="audit.save" var="save" />
<spring:message code="audit.cancel" var="cancel" />


<security:authorize access="hasRole('AUDITOR')">

<form:form action="${requestURI}" modelAttribute="audit">

	<%-- Form fields --%>
	
	<form:hidden path="id" />
	
	<acme:textarea code="audit.text" path="text" />
	<form:label path="score">
			<jstl:out value="${score}"/>
		</form:label>	
		<form:input path="score" pattern="\d{1,2}|\d{1,2}\.\d{1}" placeholder="num."/>
		<form:errors path="score" cssClass="error" />
		<br>
	<form:label path="finalMode">
			<jstl:out value="${finalMode}" />:
		</form:label>
			<form:select path="finalMode" >
				<form:option
					label="NO"
					value="false" />
				<form:option
					label="YES"
					value="true" />
			</form:select>
		<br>
		<acme:select code="audit.position" path="position"
			items="${positions}" itemLabel="title" id="positions" />
		<br />
	<%-- Buttons --%>
	<input type="submit" name="save" value="${save}"/>&nbsp; 
	
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript: relativeRedir('audit/auditor/list.do');" />

</form:form>

</security:authorize>