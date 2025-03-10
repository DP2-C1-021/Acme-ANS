
package acme.features.authenticated.S3;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;

@GuiService
public class AuthenticatedFlightCrewDashboardService extends AbstractGuiService<Authenticated, FlightCrewDashboardDTO> {

	@Autowired
	private AuthenticatedFlightCrewDashboardRepository repository;


	@Override
	public void load() {
		int crewMemberId = super.getRequest().getPrincipal().getAccountId();

		FlightCrewDashboardDTO dto = new FlightCrewDashboardDTO();

		// Filtrar solo los últimos 5 destinos en el servicio
		dto.setLastFiveDestinations(this.repository.findLastFiveDestinations(crewMemberId).stream().limit(5)  // Limita los resultados a los últimos 5
			.collect(Collectors.toList()));
		dto.setIncidentSeverityStats(this.repository.findIncidentSeverityStats(crewMemberId));
		dto.setAssignedCrewMembers(this.repository.findAssignedCrewMembers(crewMemberId).stream().limit(1)  // Limita a 1 resultado en memoria
			.collect(Collectors.toList()));
		dto.setFlightAssignmentsByStatus(this.repository.findFlightAssignmentsByStatus(crewMemberId));
		dto.setAssignmentStatistics(this.repository.findAssignmentStatistics(crewMemberId));

		super.getBuffer().addData(dto);
	}

}
