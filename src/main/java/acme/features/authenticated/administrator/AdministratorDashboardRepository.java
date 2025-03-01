
package acme.features.authenticated.administrator;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	// Número total de aeropuertos agrupados por su alcance operativo
	@Query("SELECT a.operationalScope, COUNT(a) FROM Airport a GROUP BY a.operationalScope")
	Object[] findTotalAirportsByScope();

	// Número de aerolíneas agrupadas por tipo
	@Query("SELECT a.type, COUNT(a) FROM Airline a GROUP BY a.type")
	Object[] findTotalAirlinesByType();

	// Ratio de aerolíneas que tienen tanto un email como un número de teléfono
	@Query("SELECT (COUNT(a) * 1.0 / (SELECT COUNT(a2) FROM Airline a2)) FROM Airline a WHERE a.email IS NOT NULL AND a.phoneNumber IS NOT NULL")
	Double findRatioAirlinesWithEmailAndPhone();

	// Ratio de aeronaves activas y en mantenimiento
	@Query("SELECT (COUNT(a) * 1.0 / (SELECT COUNT(a2) FROM Aircraft a2)) FROM Aircraft a WHERE a.status = 'ACTIVE'")
	Double findRatioActiveAircrafts();

	@Query("SELECT (COUNT(a) * 1.0 / (SELECT COUNT(a2) FROM Aircraft a2)) FROM Aircraft a WHERE a.status = 'MAINTENANCE'")
	Double findRatioNonActiveAircrafts();

	// Ratio de reseñas con una puntuación mayor a 5.00
	@Query("SELECT (COUNT(r) * 1.0 / (SELECT COUNT(r2) FROM Review r2)) FROM Review r WHERE r.score > 5.00")
	Double findRatioReviewsAboveFive();

	// Estadísticas de reseñas en las últimas 10 semanas (cantidad, promedio, mínimo, máximo y desviación estándar)
	@Query("SELECT COUNT(r), AVG(r.score), MIN(r.score), MAX(r.score), STDDEV(r.score) FROM Review r WHERE r.postedMoment >= CURRENT_DATE - INTERVAL 10 WEEK")
	Object[] findReviewStatistics();
}
