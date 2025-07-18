
package acme.features.authenticated.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student2.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerListService	listService;

	@Autowired
	private CustomerPassengerShowService	showService;

	@Autowired
	private CustomerPassengerCreateService	createService;

	@Autowired
	private CustomerPassengerUpdateService	updateService;

	@Autowired
	private CustomerPassengerPublishService	publishService;

	@Autowired
	private CustomerPassengerListAllService	listAllService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
