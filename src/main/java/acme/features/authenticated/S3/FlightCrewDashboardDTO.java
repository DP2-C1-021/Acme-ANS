
package acme.features.authenticated.S3;

import java.util.List;

import acme.client.components.basis.AbstractObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewDashboardDTO extends AbstractObject {

	private static final long	serialVersionUID	= 1L;

	private List<Object>		lastFiveDestinations;
	private Object[]			incidentSeverityStats;
	private List<Object>		assignedCrewMembers;
	private Object[]			flightAssignmentsByStatus;
	private Object[]			assignmentStatistics;
}
