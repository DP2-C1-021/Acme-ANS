
package acme.features.authenticated.administrator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;

@GuiController
public class AdministratorDashboardController extends AbstractGuiController<Administrator, Object> {

	@Autowired
	private AdministratorDashboardService dashboardService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.dashboardService);
	}
}
