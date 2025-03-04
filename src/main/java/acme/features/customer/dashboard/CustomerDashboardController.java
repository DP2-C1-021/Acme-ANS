
package acme.features.customer.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acme.realms.Customer;

@RestController
@RequestMapping("/api/customer-dashboard")
public class CustomerDashboardController {

	@Autowired
	private CustomerDashboardService service;


	@GetMapping("/last-five-destinations")
	public List<?> getLastFiveDestinations(@RequestParam final Customer customer) {
		return this.service.getLastFiveDestinations(customer);
	}

	@GetMapping("/money-spent-last-year")
	public Double getMoneySpentLastYear(@RequestParam final Customer customer) {
		return this.service.getMoneySpentLastYear(customer);
	}

	@GetMapping("/bookings-by-travel-class")
	public List<Object[]> getBookingsByTravelClass(@RequestParam final Customer customer) {
		return this.service.getBookingsByTravelClass(customer);
	}

	@GetMapping("/booking-price-statistics")
	public Object[] getBookingPriceStatistics(@RequestParam final Customer customer) {
		return this.service.getBookingPriceStatistics(customer);
	}

	@GetMapping("/passenger-statistics")
	public Object[] getPassengerStatistics(@RequestParam final Customer customer) {
		return this.service.getPassengerStatistics(customer);
	}
}
