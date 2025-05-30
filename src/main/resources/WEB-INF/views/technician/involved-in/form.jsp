

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="technician.involved-in.form.label.task" 
			path="task" 
			readonly="${_command != 'create'}" 
			choices="${tasks}" />
	<jstl:if test="${_command == 'show'}">
		<acme:input-textbox code="technician.involved-in.form.label.technician" path="taskTechnician" readonly="true" />
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && draftRecord == true}">
			<acme:submit code="technician.involved-in.form.button.delete"
				action="/technician/involved-in/delete?masterId=${masterId}" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.involved-in.form.button.create"
				action="/technician/involved-in/create?masterId=${masterId}" />
		</jstl:when>
	</jstl:choose>
</acme:form>