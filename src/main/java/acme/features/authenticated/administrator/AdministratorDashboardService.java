
package acme.features.authenticated.administrator;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;

@GuiService
public class AdministratorDashboardService extends AbstractGuiService<Administrator, Object> {

	@Autowired
	private AdministratorDashboardRepository repository;


	@Override
	public void load() {
		Dataset dataset = new Dataset();

		dataset.add("totalAirportsByScope", this.repository.findTotalAirportsByScope());
		dataset.add("totalAirlinesByType", this.repository.findTotalAirlinesByType());
		dataset.add("ratioAirlinesWithEmailAndPhone", this.repository.findRatioAirlinesWithEmailAndPhone());
		dataset.add("ratioActiveAircrafts", this.repository.findRatioActiveAircrafts());
		dataset.add("ratioNonActiveAircrafts", this.repository.findRatioNonActiveAircrafts());
		dataset.add("ratioReviewsAboveFive", this.repository.findRatioReviewsAboveFive());
		dataset.add("reviewStatistics", this.repository.findReviewStatistics());

		super.getBuffer().addData(dataset);
	}
}
