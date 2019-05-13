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


<spring:message code="administrator.queryc1" var="queryc1" />
<spring:message code="administrator.queryc2" var="queryc2" />
<spring:message code="administrator.queryc3" var="queryc3" />
<spring:message code="administrator.queryc4" var="queryc4" />
<spring:message code="administrator.queryc5" var="queryc5" />
<spring:message code="administrator.queryc6" var="queryc6" />
<spring:message code="administrator.queryb1" var="queryb1" />
<spring:message code="administrator.queryb2" var="queryb2" />
<spring:message code="administrator.queryb3" var="queryb3" />

<spring:message code="administrator.queryARc1" var="queryARc1" />
<spring:message code="administrator.queryARc2" var="queryARc2" />
<spring:message code="administrator.queryARc3" var="queryARc3" />
<spring:message code="administrator.queryARc4" var="queryARc4" />
<spring:message code="administrator.queryARb1" var="queryARb1" />
<spring:message code="administrator.queryARb2" var="queryARb2" />
<spring:message code="administrator.queryARa1" var="queryARa1" />
<spring:message code="administrator.queryARa2" var="queryARa2" />
<spring:message code="administrator.queryARa3" var="queryARa3" />

<spring:message code="administrator.notPosition" var="notPosition" />

<spring:message code="administrator.return" var="returnMsg" />

<security:authorize access="hasRole('ADMIN')" >
	
	<%-- Displays the result of all required database queries --%>
	
	<table style="width:100%">
 		
  		<tr>
    		<td><jstl:out value="${queryc1}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevPositionsPerCompany}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryc2}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevApplicationsPerRookie}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryc3}" /></td>
    		<td><jstl:out value="${companiesWithMoreOfferedPossitions}" /></td> 
  		</tr>

  		<tr>
    		<td><jstl:out value="${queryc4}" /></td>
    		<td><jstl:out value="${rookiesWithMoreApplications}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryc5}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevOfferedSalaries}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryc6}" /></td>
    		<td><jstl:out value="${bestAndWorstPositions}" /></td> 
  		</tr>
  		
  		
    	<tr>
    		<td><jstl:out value="${queryb1}" /></td>
    		<td><jstl:out value="${minMaxAvgStddevCurriculaPerRookie}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryb2}" /></td>
    		<td><jstl:out value="${minMaxAvgStddevResultsFinders}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryb3}" /></td>
    		<td><jstl:out value="${ratioEmptyVersusNonEmptyFinders}" /></td> 
  		</tr>
		
  		<tr>
    		<td><jstl:out value="${queryARc1}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevAuditScorePerPosition}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARc2}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevAuditScorePerCompany}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARc3}" /></td>
    		<td><jstl:out value="${companiesWithHighAuditScore}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARc4}" /></td>
    		<jstl:if test="${empty avgSalaryOfferedPerPositionWithHighestAvgAuditScore}">
    		<td><jstl:out value="${notPosition}" /></td> 
    		</jstl:if>
    		<jstl:if test="${not empty avgSalaryOfferedPerPositionWithHighestAvgAuditScore}">
    		<td><jstl:out value="${avgSalaryOfferedPerPositionWithHighestAvgAuditScore}" /></td> 
    		</jstl:if> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARb1}" /></td>
    		<td><jstl:out value="${minMaxAvgStddevItemPerProvider}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARb2}" /></td>
    		<td><jstl:out value="${top5ProviderInTermsOfItems}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARa1}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevSponsorshipsPerProvider}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARa2}" /></td>
    		<td><jstl:out value="${avgMinMaxStddevSponsorshipsPerPosition}" /></td> 
  		</tr>
  		
  		<tr>
    		<td><jstl:out value="${queryARa3}" /></td>
    		<td><jstl:out value="${providersWith10PerCentMoreSponsorshipsThanAvg}" /></td> 
  		</tr>
  		
	</table>
	
	<a href="welcome/index.do"><jstl:out value="${returnMsg}" /></a>

</security:authorize>