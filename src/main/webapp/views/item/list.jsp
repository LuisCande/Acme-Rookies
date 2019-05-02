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
<spring:message code="item.name" var="name" />
<spring:message code="item.description" var="description" />
<spring:message code="item.link" var="link" />
<spring:message code="item.picture" var="picture" />
<spring:message code="item.provider" var="provider" />

<spring:message code="item.create" var="create" />
<spring:message code="item.edit" var="edit" />
<spring:message code="item.save" var="save" />
<spring:message code="item.cancel" var="cancel" />
<spring:message code="item.display" var="display" />
<spring:message code="item.delete" var="msgDelete" />
<spring:message code="item.confirm.delete" var="msgConfirm" />


<%-- Listing grid --%>

<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="items" requestURI="${requestURI}" id="row">

	<%-- Attributes --%>
	
	<spring:url var="displayProviderUrl" value="provider/display.do">
			<spring:param name="varId" value="${row.provider.id}" />
		</spring:url>
	
	<display:column property="name" title="${name}" sortable="true" />
	<display:column property="link" title="${link}" sortable="true" />
	<display:column title="${provider}" sortable="true" >
	<a href="${displayProviderUrl}"><jstl:out value="${row.provider.name} "/><jstl:out value="${row.provider.surnames}" /></a>
	</display:column>
	<%-- Display --%>
		<spring:url var="displayUrl" value="item/display.do">
			<spring:param name="varId" value="${row.id}" />
		</spring:url>
		<display:column title="${display}">
			<a href="${displayUrl}"><jstl:out value="${display}" /></a>
		</display:column>
		
	<%-- Edit --%>	
	<security:authorize access="hasRole('PROVIDER')">
	<spring:url var="editUrl" value="item/provider/edit.do">
		<spring:param name="varId" value="${row.id}" />
	</spring:url>
		
	<display:column title="${edit}">
			<a href="${editUrl}"><jstl:out value="${edit}" /></a>
	</display:column>		

	<!-- Delete -->
	
	<spring:url var="deleteURL" value="item/provider/delete.do">
		<spring:param name="varId" value="${row.id}" />
	</spring:url>
	
	
	<display:column title="${msgDelete}">
			<a href="${deleteURL}" onclick="return confirm('${msgConfirm}')" ><jstl:out value="${msgDelete}" /></a>
	</display:column>
	</security:authorize>
</display:table>
<security:authorize access="hasRole('PROVIDER')">
	<spring:url var="createUrl" value="item/provider/create.do"/>
	<a href="${createUrl}"><jstl:out value="${create}"/></a>
</security:authorize>
