
package acme.forms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
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
	private List<Money>			moneySpentLastYear;
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


	public List<Money> parseoMoney(final Collection<Object[]> stats) {
		List<Money> money = new ArrayList<Money>();

		for (Object[] row : stats) {

			String currency = (String) row[0];
			Double averageAmount = ((Number) row[1]).doubleValue();

			Money m = new Money();
			m.setCurrency(currency);
			m.setAmount(averageAmount);
			money.add(m);
		}
		return money;

	}
}
