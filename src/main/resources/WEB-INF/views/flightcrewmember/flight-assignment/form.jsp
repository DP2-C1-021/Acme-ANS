<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.duty" path="duty"/>
	<acme:input-checkbox code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-money code="flight-crew-member.flight-assignment.form.label.status" path="status"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
	</jstl:if>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish')&& publish==true}">
		<acme:input-moment code="flight-crew-member.flight-assignment.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
		<acme:input-moment code="flight-crew-member.flight-assignment.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
		<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.origin" path="origin" readonly="true"/>
		<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.destination" path="destination" readonly="true"/>
		<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.layovers" path="layovers" readonly="true"/>
		<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
		<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
		<acme:submit code="flight-crew-member.flight-assignment.form.button.draftMode" action="/flight-crew-member/flight-assignment/draft-mode"/>
	</jstl:if>
</acme:form>