
package acme.features.customer.dashboard;

import java.math.BigDecimal;

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

		// 1️ Últimos 5 destinos
		customerDashboard.setLastFiveDestinations(this.repository.findLastFiveDestinations(customerId));

		// 2️ Dinero gastado en el último año
		customerDashboard.setMoneySpentLastYear(this.repository.findMoneySpentLastYear(customerId));

		// 3️ Número de reservas por clase de viaje
		customerDashboard.setBookingsByTravelClass(this.repository.countBookingsByTravelClass(customerId));

		// 4️ Estadísticas de costos de reservas en los últimos 5 años
		Object[] bookingStats = this.repository.getBookingCostStatsLastFiveYears(customerId);
		customerDashboard.setBookingCount((Long) bookingStats[0]);
		customerDashboard.setBookingAvgCost((BigDecimal) bookingStats[1]);
		customerDashboard.setBookingMinCost((BigDecimal) bookingStats[2]);
		customerDashboard.setBookingMaxCost((BigDecimal) bookingStats[3]);
		customerDashboard.setBookingStdDevCost((BigDecimal) bookingStats[4]);

		// 5️ Estadísticas del número de pasajeros en sus reservas
		Object[] passengerStats = this.repository.getPassengersStats(customerId);
		customerDashboard.setPassengerCount((Long) passengerStats[0]);
		customerDashboard.setPassengerAvg((BigDecimal) passengerStats[1]);
		customerDashboard.setPassengerMin((Long) passengerStats[2]);
		customerDashboard.setPassengerMax((Long) passengerStats[3]);
		customerDashboard.setPassengerStdDev((BigDecimal) passengerStats[4]);

		super.getBuffer().addData(customerDashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		super.unbind(object);
		super.getResponse().addData(object);
	}

}
