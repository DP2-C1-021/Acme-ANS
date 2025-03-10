
package acme.features.authenticated.S3;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;

@GuiController
public class AuthenticatedFlightCrewDashboardController extends AbstractGuiController<Authenticated, FlightCrewDashboardDTO> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuthenticatedFlightCrewDashboardService dashboardService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.dashboardService);
	}
}
