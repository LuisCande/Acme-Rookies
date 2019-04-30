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
<spring:message code="audit.moment" var="moment" />
<spring:message code="audit.text" var="text" />
<spring:message code="audit.score" var="score" />
<spring:message code="audit.finalMode" var="finalMode" />
<spring:message code="audit.finalMode.true" var="finalModeTrue" />
<spring:message code="audit.finalMode.false" var="finalModeFalse" />
<spring:message code="audit.position" var="position" />
<spring:message code="audit.auditor" var="auditor" />
<spring:message code="formatDate" var="formatDate" />

<spring:message code="audit.create" var="create" />
<spring:message code="audit.edit" var="edit" />
<spring:message code="audit.save" var="save" />
<spring:message code="audit.cancel" var="cancel" />
<spring:message code="audit.display" var="display" />
<spring:message code="audit.delete" var="msgDelete" />
<spring:message code="audit.confirm.delete" var="msgConfirm" />


<%-- Listing grid --%>

<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="audits" requestURI="${requestURI}" id="row">

	<%-- Attributes --%>
	
	<display:column title="${moment}" sortable="true">
		<fmt:formatDate value="${row.moment}" pattern="${formatDate}"/>
	</display:column>
	<display:column property="score" title="${score}" sortable="true" />
	
	<display:column title="${finalMode}" sortable="true">
		<jstl:if test="${row.finalMode eq true}">
		<jstl:out value="${finalModeTrue}"/>
		</jstl:if>
		<jstl:if test="${row.finalMode eq false}">
		<jstl:out value="${finalModeFalse}"/>
		</jstl:if>
	</display:column>
	
	<display:column property="position.title" title="${position}" sortable="true" />
	
	<%-- Display --%>
		<spring:url var="displayUrl" value="audit/display.do">
			<spring:param name="varId" value="${row.id}" />
		</spring:url>
		<display:column title="${display}">
			<a href="${displayUrl}"><jstl:out value="${display}" /></a>
		</display:column>
		
	<%-- Edit --%>	
		<spring:url var="editUrl" value="audit/auditor/edit.do">
			<spring:param name="varId" value="${row.id}" />
		</spring:url>
		
		<display:column title="${edit}">
		<jstl:if test="${row.finalMode eq false}">
			<a href="${editUrl}"><jstl:out value="${edit}" /></a>
		</jstl:if>
		</display:column>		

	<!-- Delete -->
	
	<spring:url var="deleteURL" value="audit/auditor/delete.do">
		<spring:param name="varId" value="${row.id}" />
	</spring:url>
	
	
	<display:column title="${msgDelete}">
	<jstl:if test="${row.finalMode eq false}">
		<a href="${deleteURL}" onclick="return confirm('${msgConfirm}')" ><jstl:out value="${msgDelete}" /></a>
	</jstl:if>
	</display:column>
	
</display:table>

	<spring:url var="createUrl" value="audit/auditor/create.do"/>
	<a href="${createUrl}"><jstl:out value="${create}"/></a>

