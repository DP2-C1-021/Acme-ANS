
package acme.features.customer.dashboard;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.S2.Booking;
import acme.realms.Customer;

@Service
public class CustomerDashboardService {

	@Autowired
	private CustomerDashboardRepository repository;


	public List<Booking> getLastFiveDestinations(final Customer customer) {
		return this.repository.findLastFiveDestinations(customer).subList(0, Math.min(5, this.repository.findLastFiveDestinations(customer).size()));
	}

	public Double getMoneySpentLastYear(final Customer customer) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		return this.repository.findMoneySpentLastYear(customer, calendar.getTime());
	}

	public List<Object[]> getBookingsByTravelClass(final Customer customer) {
		return this.repository.countBookingsByTravelClass(customer);
	}

	public Object[] getBookingPriceStatistics(final Customer customer) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -5);
		return this.repository.getBookingPriceStatistics(customer, calendar.getTime());
	}

	public Object[] getPassengerStatistics(final Customer customer) {
		return this.repository.getPassengerStatistics(customer);
	}
}
