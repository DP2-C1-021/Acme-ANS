
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S5.InvolvedIn;
import acme.entities.S5.MaintenanceRecord;
import acme.entities.S5.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInCreateService extends AbstractGuiService<Technician, InvolvedIn> {

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
		status = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		InvolvedIn object;
		int masterId;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);

		object = new InvolvedIn();
		object.setTask(null);
		object.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final InvolvedIn involves) {
		int masterId;
		Task task;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		task = super.getRequest().getData("task", Task.class);

		super.bindObject(involves);
		involves.setTask(task);
		involves.setMaintenanceRecord(maintenanceRecord);
	}

	@Override
	public void validate(final InvolvedIn involves) {
		;
	}

	@Override
	public void perform(final InvolvedIn involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final InvolvedIn involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> tasks;

		tasks = this.repository.findAllTasks();
		taskChoices = SelectChoices.from(tasks, "ticker", involves.getTask());

		dataset = super.unbindObject(involves, "task");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("maintenanceRecord", involves.getMaintenanceRecord().getId());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);

		super.getResponse().addData(dataset);
	}
}
