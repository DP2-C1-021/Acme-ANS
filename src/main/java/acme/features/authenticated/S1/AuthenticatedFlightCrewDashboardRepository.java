
package acme.features.authenticated.S1;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S3.FlightCrewMember;

@Repository
public interface AuthenticatedFlightCrewDashboardRepository extends AbstractRepository {

	// Últimos 5 destinos a los que ha sido asignado un tripulante
	@Query("SELECT f.flightLeg.destination FROM FlightAssignment f " + "WHERE f.flightCrewMember.id = :crewMemberId " + "ORDER BY f.lastUpdate DESC")
	List<String> findLastFiveDestinations(int crewMemberId);

	// Número de tramos con incidentes agrupados por severidad
	@Query("SELECT " + "(SELECT COUNT(a) FROM ActivityLog a WHERE a.severityLevel BETWEEN 0 AND 3 AND a.flightAssignment.flightCrewMember.id = :crewMemberId), "
		+ "(SELECT COUNT(a) FROM ActivityLog a WHERE a.severityLevel BETWEEN 4 AND 7 AND a.flightAssignment.flightCrewMember.id = :crewMemberId), "
		+ "(SELECT COUNT(a) FROM ActivityLog a WHERE a.severityLevel BETWEEN 8 AND 10 AND a.flightAssignment.flightCrewMember.id = :crewMemberId) " + "FROM ActivityLog a WHERE a.flightAssignment.flightCrewMember.id = :crewMemberId")
	Object[] findIncidentSeverityStats(int crewMemberId);

	// Miembros de tripulación asignados en el último tramo
	@Query("SELECT f.flightCrewMember FROM FlightAssignment f " + "WHERE f.flightLeg = ( " + "SELECT fa.flightLeg FROM FlightAssignment fa " + "WHERE fa.flightCrewMember.id = :crewMemberId " + "ORDER BY fa.lastUpdate DESC) " +  // Se elimina LIMIT
		"AND f.flightCrewMember.id <> :crewMemberId")
	List<FlightCrewMember> findAssignedCrewMembers(int crewMemberId);

	// Asignaciones de vuelo agrupadas por estado
	@Query("SELECT f.assignmentStatus, COUNT(f) " + "FROM FlightAssignment f " + "WHERE f.flightCrewMember.id = :crewMemberId " + "GROUP BY f.assignmentStatus")
	Object[] findFlightAssignmentsByStatus(int crewMemberId);

	// Estadísticas de asignaciones de vuelo en el último mes
	@Query("SELECT COUNT(f), AVG(COUNT(f)), MIN(COUNT(f)), MAX(COUNT(f)), STDDEV(COUNT(f)) " + "FROM FlightAssignment f " + "WHERE f.flightCrewMember.id = :crewMemberId " + "AND f.lastUpdate >= CURRENT_DATE - 30 " + "GROUP BY f.flightCrewMember.id")
	Object[] findAssignmentStatistics(int crewMemberId);
}
