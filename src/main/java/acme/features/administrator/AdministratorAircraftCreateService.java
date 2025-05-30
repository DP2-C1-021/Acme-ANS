
package acme.features.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Group.Aircraft;
import acme.entities.Group.Airline;
import acme.entities.Group.Status;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft = new Aircraft();
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {

		int airlineId = super.getRequest().getData("airline", int.class);
		Airline airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationnumber", "capacity", "cargoweight", "status", "details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices = SelectChoices.from(Status.class, aircraft.getStatus());
		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices selectedAirlines = SelectChoices.from(airlines, "name", aircraft.getAirline());

		Dataset dataset = super.unbindObject(aircraft, "model", "registrationnumber", "capacity", "cargoweight", "status", "details");
		dataset.put("statuses", choices);
		dataset.put("airlines", selectedAirlines);
		dataset.put("airline", selectedAirlines.getSelected().getKey());
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
