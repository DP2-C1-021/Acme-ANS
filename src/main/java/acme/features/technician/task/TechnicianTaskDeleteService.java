
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S5.InvolvedIn;
import acme.entities.S5.Task;
import acme.entities.S5.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		Integer taskId;
		Task task;
		Technician technician;

		if (super.getRequest().hasData("id", Integer.class)) {
			taskId = super.getRequest().getData("id", Integer.class);
			if (taskId != null) {
				task = this.repository.findTaskById(taskId);
				if (task != null) {
					technician = task.getTechnician();
					status = task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
				}
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int taskId;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		;
	}

	@Override
	public void validate(final Task task) {
		boolean status;
		Collection<InvolvedIn> involves;

		involves = this.repository.findInvolvesByTaskId(task.getId());

		status = involves.isEmpty();
		super.state(status, "*", "acme.validation.task.maintenance-record-linked.message", task);

	}

	@Override
	public void perform(final Task task) {

		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getIdentity().getFullName());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
