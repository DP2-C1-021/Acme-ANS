<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <!-- Bot�n para crear un nuevo registro -->
    <acme:button code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
</acme:form>

<acme:list>
    <acme:list-column code="technician.maintenance-record.list.label.moment" path="maintenanceMoment" width="30%"/>
    <acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="20%"/>
    <acme:list-column code="technician.maintenance-record.list.label.next-inspection" path="nextInspectionDate" width="30%"/>
    <acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft" width="20%"/>
    <acme:list-payload path="payload"/>
</acme:list>
