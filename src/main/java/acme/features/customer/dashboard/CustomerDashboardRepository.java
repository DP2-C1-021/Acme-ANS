
package acme.features.customer.dashboard;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	// 1️⃣ Últimos 5 destinos visitados por el cliente (se asume locatorCode como destino)
	@Query("""
		    SELECT DISTINCT b.locatorCode
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    ORDER BY b.purchaseMoment DESC
		""")
	List<String> findLastFiveDestinations(@Param("customerId") int customerId);

	// 2️⃣ Dinero gastado en reservas en el último año
	@Query("""
		    SELECT COALESCE(SUM(b.price), 0)
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    AND b.purchaseMoment >= CURRENT_DATE - 365
		""")
	BigDecimal findMoneySpentLastYear(@Param("customerId") int customerId);

	// 3️⃣ Número de reservas agrupadas por clase de viaje
	@Query("""
		    SELECT b.travelClass, COUNT(b)
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    GROUP BY b.travelClass
		""")
	List<Object[]> countBookingsByTravelClass(@Param("customerId") int customerId);

	// 4️⃣ Estadísticas de costos de reservas en los últimos 5 años
	@Query("""
		    SELECT COUNT(b), AVG(b.price), MIN(b.price), MAX(b.price), STDDEV(b.price)
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    AND b.purchaseMoment >= CURRENT_DATE - 1825
		""")
	Object[] getBookingCostStatsLastFiveYears(@Param("customerId") int customerId);

	// 5️⃣ Estadísticas del número de pasajeros en sus reservas
	@Query("""
		    SELECT COUNT(p)
		    FROM Passenger p
		    WHERE p.booking.customer.id = :customerId
		    GROUP BY p.booking.id
		""")
	List<Long> getPassengerCountsPerBooking(@Param("customerId") int customerId);

}
