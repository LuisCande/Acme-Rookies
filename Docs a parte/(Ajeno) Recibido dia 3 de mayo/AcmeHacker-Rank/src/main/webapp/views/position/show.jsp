<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

		<div>
        <b><spring:message code="position.ticker" /></b>: <jstl:out value="${position.ticker}" /> <br/>
		<b><spring:message code="position.title"  /></b>: <jstl:out value="${position.title}"  /> <br/> 
		<b><spring:message code="position.description" /></b>: <jstl:out value="${position.description}" /> <br/>
		<spring:message    code="position.moment.format" var="formatMoment"/>
		<b><spring:message code="position.deadline" /></b>: <jstl:out value="${position.deadline}" /> <br/>
	    <b><spring:message code="position.profile"  /></b>: <jstl:out value="${position.profile}"  /> <br/>
	    <b><spring:message code="position.skills"   /></b>: <jstl:out value="${position.skills}"   /> <br/> 
	    <b><spring:message code="position.salary"   /></b>: <jstl:out value="${position.salary}"   /> <br/> 
	    <b><spring:message code="position.technologies" /></b>: <jstl:out value="${position.technologies}" /> <br/>
		<b><spring:message code="position.company" /></b>: <jstl:out value="${position.company.commercialName}" /> <br/>
		</div>
		
		<acme:cancel url="/position/list.do" code="position.back"/>
		
