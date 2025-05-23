
package acme.features.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Group.Aircraft;
import acme.entities.Group.Airline;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Aircraft aircraft = (Aircraft) this.repository.findById(id).get();
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationnumber", "capacity", "cargoweight", "status", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {

		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices selectedAirlines = SelectChoices.from(airlines, "name", aircraft.getAirline());

		Dataset dataset = super.unbindObject(aircraft, "model", "registrationnumber", "capacity", "cargoweight", "status", "details");
		dataset.put("statuses", aircraft.getStatus());
		dataset.put("airline", selectedAirlines.getSelected().getKey());
		dataset.put("airlines", selectedAirlines);

		super.getResponse().addData(dataset);
	}
}
