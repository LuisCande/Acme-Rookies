<%--
 * list.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%-- Stored message variables --%>

<spring:message code="sponsorship.create" var="create" />
<spring:message code="sponsorship.edit" var="edit" />
<spring:message code="sponsorship.save" var="save" />
<spring:message code="sponsorship.cancel" var="cancel" />
<spring:message code="sponsorship.display" var="display" />
<spring:message code="sponsorship.banner" var="banner" />
<spring:message code="sponsorship.targetPage" var="targetPage" />
<spring:message code="sponsorship.position" var="position" />
<spring:message code="sponsorship.provider" var="provider" />

<spring:message code="sponsorship.delete" var="msgDelete" />
<spring:message code="sponsorship.confirm.delete" var="msgConfirm" />
<spring:message code="sponsorship.pay" var="msgPay" />
<spring:message code="sponsorship.pay.error" var="msgPayError" />
<spring:message code="sponsorship.pay.confirm" var="msgConfirmPay" />
<spring:message code="sponsorship.charge" var="msgCharge" />




<security:authorize access="hasRole('PROVIDER')">

<%-- Listing grid --%>

<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="sponsorships" requestURI="${requestURI}" id="row">

	<%-- Attributes --%>
	
	<display:column property="banner" title="${banner}" sortable="true" />

	<display:column property="targetPage" title="${targetPage}" sortable="true" />
	
	<display:column property="position.title" title="${position}" sortable="true" />
	
	<display:column property="charge" title="${msgCharge}" sortable="true" />
	
	<%-- Edit --%>	
		<spring:url var="editUrl" value="sponsorship/provider/edit.do">
			<spring:param name="varId" value="${row.id}" />
		</spring:url>
		<display:column title="${edit}">
			<a href="${editUrl}"><jstl:out value="${edit}" /></a>
		</display:column>		

	<%-- Display --%>
		<spring:url var="displayUrl" value="sponsorship/provider/display.do">
			<spring:param name="varId" value="${row.id}" />
		</spring:url>
		<display:column title="${display}">
			<a href="${displayUrl}"><jstl:out value="${display}" /></a>
		</display:column>
		
	<!--  Pay sponsorship -->

	<spring:url var="payURL" value="sponsorship/provider/pay.do">
		<spring:param name="varId" value="${row.id}" />
	</spring:url>

	<display:column title="${msgPay}">
		<a href="${payURL}" onclick="return confirm('${msgConfirmPay}')" ><jstl:out value="${msgPay}" /></a>
	</display:column>
	
	<!-- Delete -->
	
	<spring:url var="deleteURL" value="sponsorship/provider/delete.do">
		<spring:param name="varId" value="${row.id}" />
	</spring:url>

	<display:column title="${msgDelete}">
		<a href="${deleteURL}" onclick="return confirm('${msgConfirm}')" ><jstl:out value="${msgDelete}" /></a>
	</display:column>
	
</display:table>

	<spring:url var="createUrl" value="sponsorship/provider/create.do"/>
	<a href="${createUrl}"><jstl:out value="${create}"/></a>

</security:authorize>