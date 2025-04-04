
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S5.InvolvedIn;
import acme.entities.S5.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select i from InvolvedIn i where i.task.id = :id")
	Collection<InvolvedIn> findInvolvesByTaskId(int id);
}
