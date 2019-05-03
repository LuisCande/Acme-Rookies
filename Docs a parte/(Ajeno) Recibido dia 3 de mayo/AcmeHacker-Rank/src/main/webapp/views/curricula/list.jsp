<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="curriculas" id="row" requestURI="curricula/hacker/list.do" pagesize="5">
		<display:column>
		<a href="curricula/hacker/show.do?curriculaId=${row.id}"> 
				<spring:message code="curricula.showCurricula" />
		</a><br/>
		</display:column>
		
		<display:column property="name" titleKey="curricula.name"/>
		
	</display:table>
	
	<jstl:if test="${hackerIsOwner == true}">
	<spring:message code="curricula.create" var="curricula" />
		<input type="button" name="curricula" value="${curricula}"
			onclick="javascript:relativeRedir('curricula/hacker/create.do');" />
	</jstl:if>