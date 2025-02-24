
package acme.forms;

import java.math.BigDecimal;
import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private List<String>		lastFiveDestinations;
	private BigDecimal			moneySpentLastYear;
	private List<Object[]>		bookingsByTravelClass;

	private Long				bookingCount;
	private BigDecimal			bookingAvgCost;
	private BigDecimal			bookingMinCost;
	private BigDecimal			bookingMaxCost;
	private BigDecimal			bookingStdDevCost;

	private Long				passengerCount;
	private BigDecimal			passengerAvg;
	private Long				passengerMin;
	private Long				passengerMax;
	private BigDecimal			passengerStdDev;
}
