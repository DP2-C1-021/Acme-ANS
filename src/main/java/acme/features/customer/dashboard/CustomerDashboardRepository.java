
package acme.features.customer.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import acme.entities.S2.Booking;
import acme.realms.Customer;

@Repository
public interface CustomerDashboardRepository extends CrudRepository<Booking, Long> {

	// 1. Últimos cinco destinos reservados por el cliente
	@Query("SELECT b FROM Booking b WHERE b.customer = ?1 ORDER BY b.purchaseMoment DESC")
	List<Booking> findLastFiveDestinations(Customer customer);

	// 2. Dinero gastado en reservas durante el último año
	@Query("SELECT SUM(b.price.amount) FROM Booking b WHERE b.customer = ?1 AND b.purchaseMoment >= :lastYear")
	Double findMoneySpentLastYear(Customer customer, Date lastYear);

	// 3. Número de reservas agrupadas por TravelClass
	@Query("SELECT b.travelClass, COUNT(b) FROM Booking b WHERE b.customer = ?1 GROUP BY b.travelClass")
	List<Object[]> countBookingsByTravelClass(Customer customer);

	// 4. Estadísticas del costo de las reservas en los últimos cinco años
	@Query("SELECT COUNT(b), AVG(b.price.value), MIN(b.price.value), MAX(b.price.value), STDDEV(b.price.value) FROM Booking b WHERE b.customer = ?1 AND b.purchaseMoment >= ?2")
	Object[] getBookingPriceStatistics(Customer customer, Date lastFiveYears);

	// 5. Estadísticas del número de pasajeros por reserva
	@Query("SELECT COUNT(p), AVG(p.count), MIN(p.count), MAX(p.count), STDDEV(p.count) " + "FROM Passenger p JOIN p.booking b WHERE b.customer = ?1")
	Object[] getPassengerStatistics(Customer customer);
}
