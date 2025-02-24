
package acme.features.customer.dashboard;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import acme.client.controllers.AbstractController;
import acme.client.services.AbstractService;
import acme.forms.CustomerDashboard;
import acme.internals.controllers.ControllerMetadata;
import acme.realms.Customer;

@Controller
public class CustomerDashboardController extends AbstractController<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

	@Override
	protected ControllerMetadata<Customer, CustomerDashboard> buildMetadata(final Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RequestMappingInfo buildMappingInfo(final String command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Method buildHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void redirect(final AbstractService<Customer, CustomerDashboard> service) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object buildResult(final AbstractService<Customer, CustomerDashboard> service) {
		// TODO Auto-generated method stub
		return null;
	}
}
