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

<security:authorize access="hasRole('ADMIN')">

		<spring:message code="admin.datatable"/>
	<table style="width:'100%' border='0' align='center' ">
			<tr>
				<th><spring:message code="admin.type"/></th>
				<th><spring:message code="admin.positionsPerCompany"/></th>
				<th><spring:message code="admin.applicationsPerHacker"/></th>
				<th><spring:message code="admin.salaryPerPosition"/></th>
				<th><spring:message code="admin.curriculasPerHacker"/></th>
				<th><spring:message code="admin.resultsPerFinder"/></th>
			</tr>
			<tr>
				<td><spring:message code="admin.average"/></td>
				<td><jstl:out value="${avgPositionsPerCompany}"/></td>
				<td><jstl:out value="${avgApplicationsPerHacker}"/></td>
				<td><jstl:out value="${avgSalaryPerPosition}"/></td>	
				<td><jstl:out value="${avgCurriculasPerHacker}"/></td>	
				<td><jstl:out value="${avgResultsPerFinder}"/></td>		
			</tr>
			<tr>
				<td><spring:message code="admin.minimum"/></td>
				<td><jstl:out value="${minPositionsPerCompany}"/></td>
				<td><jstl:out value="${minApplicationsPerHacker}"/></td>
				<td><jstl:out value="${minSalaryPerPosition}"/></td>
				<td><jstl:out value="${minCurriculasPerHacker}"/></td>	
				<td><jstl:out value="${minResultsPerFinder}"/></td>
			</tr>	
			<tr>
				<td><spring:message code="admin.maximum"/></td>
				<td><jstl:out value="${maxPositionsPerCompany}"/></td>
				<td><jstl:out value="${maxApplicationsPerHacker}"/></td>
				<td><jstl:out value="${maxSalaryPerPosition}"/></td>	
				<td><jstl:out value="${maxCurriculasPerHacker}"/></td>
				<td><jstl:out value="${maxResultsPerFinder}"/></td>
			</tr>
			<tr>
				<td><spring:message code="admin.stdv"/></td>
				<td><jstl:out value="${stdevPositionsPerCompany}"/></td>
				<td><jstl:out value="${stdevApplicationsPerHacker}"/></td>
				<td><jstl:out value="${stdevSalaryPerPosition}"/></td>
				<td><jstl:out value="${stdevCurriculasPerHacker}"/></td>
				<td><jstl:out value="${stdevResultsPerFinder}"/></td>
			</tr>
	</table>
	
		<table style="width:'100%' border='0' align='center' ">
		<tr>
			<th><spring:message code="admin.type"/></th>
			<th><spring:message code="admin.ratio"/></th>
		</tr>
		<tr>
			<th><spring:message code="admin.rEmptyFinders"/></th>
			<th><jstl:out value="${ratioEmptyFinders}"/></th>
		</tr>
	</table>

	<b><spring:message code="admin.maxPositionsCompanies"/></b>
	<jstl:if test="${empty maxPositionsCompanies}"><spring:message code="admin.empty"/></jstl:if>
	<table style="width:'100%' border='0' align='center' ">
		<jstl:forEach var="i" items="${maxPositionsCompanies}">
		<tr>
			<td><jstl:out value="${i.commercialName}"/> (<a href="actor/show.do?actorId=${i.id}"><jstl:out value="${i.userAccount.username}"/></a>)  </td>
		</tr>			
		</jstl:forEach>
	</table>
	
	<b><spring:message code="admin.maxApplicationsHackers"/></b>
	<jstl:if test="${empty maxApplicationsHackers}"><spring:message code="admin.empty"/></jstl:if>
	<table style="width:'100%' border='0' align='center' ">
		<jstl:forEach var="i" items="${maxApplicationsHackers}">
		<tr>
			<td><jstl:out value="${i.name}"/> <jstl:out value="${i.surnames}"/> (<a href="actor/show.do?actorId=${i.id}"><jstl:out value="${i.userAccount.username}"/></a>)</td>
		</tr>			
		</jstl:forEach>
	</table>
	
	<b><spring:message code="admin.bestSalaryPosition"/></b>
		<jstl:if test="${bestSalaryPosition==null}"><spring:message code="admin.empty"/></jstl:if>
	
	<table style="width:'100%' border='0' align='center' ">
		<tr>
			<td><jstl:out value="${bestSalaryPosition.title}"/></td>
		</tr>			
	</table>
	
	<b><spring:message code="admin.worstSalaryPosition"/></b>
	<jstl:if test="${worstSalaryPosition==null}"><spring:message code="admin.empty"/></jstl:if>
	<table style="width:'100%' border='0' align='center' ">
		<tr>
			<td><jstl:out value="${worstSalaryPosition.title}"/></td>
		</tr>			
	</table>
</security:authorize>

