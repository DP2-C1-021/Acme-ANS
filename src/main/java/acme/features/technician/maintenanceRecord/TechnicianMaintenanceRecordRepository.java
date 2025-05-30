
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Group.Aircraft;
import acme.entities.S5.InvolvedIn;
import acme.entities.S5.MaintenanceRecord;
import acme.entities.S5.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.technician.id = :id")
	Collection<MaintenanceRecord> findMainteanceRecordsByTechnicianId(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select i from InvolvedIn i where i.maintenanceRecord.id = :id")
	Collection<InvolvedIn> findInvolvesByMaintenanceRecordId(int id);

	@Query("select t from Task t join InvolvedIn i on t.id = i.task.id WHERE i.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksAssociatedWithMaintenanceRecordById(int masterId);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();
}
