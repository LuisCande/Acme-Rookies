<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<security:authorize access="hasRole('ADMIN')">

<a href="admin/computeSpammers.do">compute spammers</a>

<display:table name="${actors}" id="row" pagesize="5" requestURI="admin/listActors.do">

	<display:column titleKey="actor.name" property="name" />

	<display:column titleKey="actor.surnames" property="surnames" />
	
	<display:column titleKey="admin.userAccount.username" property="userAccount.username" />
	
	<display:column>
	<a href="actor/show.do?actorId=${row.id}"><jstl:out value="${row.userAccount.username}"/></a><br>
	</display:column>
	
	<display:column titleKey="actor.isSpammer" property="isSpammer" />

	<display:column titleKey="actor.isBanned" property="isBanned" />

	<display:column titleKey="actor.action">
		<jstl:if test="${row.isBanned == false and row.isSpammer == true}">
			<a href="admin/banActor.do?actorId=${row.id}"> <spring:message code="admin.banActor" /></a>
		</jstl:if>
			
		<jstl:if test="${row.isBanned == true }">
			<a href="admin/unbanActor.do?actorId=${row.id}"> <spring:message code="admin.unBanActor" /></a>
		</jstl:if>
			
	</display:column>
	
</display:table>
</security:authorize>
