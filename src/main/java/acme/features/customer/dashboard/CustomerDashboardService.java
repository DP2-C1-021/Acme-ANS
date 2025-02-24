
package acme.features.customer.dashboard;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@Service
public class CustomerDashboardService extends AbstractService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		CustomerDashboard customerDashboard = new CustomerDashboard();

		// 1️⃣ Últimos 5 destinos
		customerDashboard.setLastFiveDestinations(this.repository.findLastFiveDestinations(customerId));

		// 2️⃣ Dinero gastado en el último año
		customerDashboard.setMoneySpentLastYear(this.repository.findMoneySpentLastYear(customerId));

		// 3️⃣ Número de reservas por clase de viaje
		customerDashboard.setBookingsByTravelClass(this.repository.countBookingsByTravelClass(customerId));

		// 4️⃣ Estadísticas de costos de reservas en los últimos 5 años
		Object[] bookingStats = this.repository.getBookingCostStatsLastFiveYears(customerId);
		customerDashboard.setBookingCount((Long) bookingStats[0]);
		customerDashboard.setBookingAvgCost((BigDecimal) bookingStats[1]);
		customerDashboard.setBookingMinCost((BigDecimal) bookingStats[2]);
		customerDashboard.setBookingMaxCost((BigDecimal) bookingStats[3]);
		customerDashboard.setBookingStdDevCost((BigDecimal) bookingStats[4]);

		// 5️⃣ Obtener número de pasajeros por reserva y calcular estadísticas en Java
		List<Long> passengerCounts = this.repository.getPassengerCountsPerBooking(customerId);

		if (!passengerCounts.isEmpty()) {
			customerDashboard.setPassengerCount(passengerCounts.stream().mapToLong(Long::longValue).sum());
			customerDashboard.setPassengerAvg(BigDecimal.valueOf(passengerCounts.stream().mapToLong(Long::longValue).average().orElse(0)));
			customerDashboard.setPassengerMin(passengerCounts.stream().mapToLong(Long::longValue).min().orElse(0));
			customerDashboard.setPassengerMax(passengerCounts.stream().mapToLong(Long::longValue).max().orElse(0));

			// Cálculo de la desviación estándar
			double avg = customerDashboard.getPassengerAvg().doubleValue();
			double variance = passengerCounts.stream().mapToDouble(count -> Math.pow(count - avg, 2)).average().orElse(0);

			customerDashboard.setPassengerStdDev(BigDecimal.valueOf(Math.sqrt(variance))); // Convertir a BigDecimal
		} else {
			customerDashboard.setPassengerCount(0L);
			customerDashboard.setPassengerAvg(BigDecimal.ZERO);
			customerDashboard.setPassengerMin(0L);
			customerDashboard.setPassengerMax(0L);
			customerDashboard.setPassengerStdDev(BigDecimal.ZERO);
		}

		super.getBuffer().addData(customerDashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		super.unbind(object);
		super.getResponse().addData(object);
	}

}
