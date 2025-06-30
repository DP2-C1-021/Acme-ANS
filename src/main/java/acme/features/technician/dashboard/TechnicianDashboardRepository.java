
package acme.features.technician.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Group.Aircraft;
import acme.entities.student5.MaintenanceRecord;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("select count(mr) from MaintenanceRecord mr where mr.status = acme.entities.student5.MaintenanceRecordStatus.PENDING and mr.technician.id = :technicianId")
	Integer numberOfMaintenanceRecordsPending(int technicianId);

	@Query("select count(mr) from MaintenanceRecord mr where mr.status = acme.entities.student5.MaintenanceRecordStatus.IN_PROGRESS and mr.technician.id = :technicianId")
	Integer numberOfMaintenanceRecordsInProgress(int technicianId);

	@Query("select count(mr) from MaintenanceRecord mr where mr.status = acme.entities.student5.MaintenanceRecordStatus.COMPLETED and mr.technician.id = :technicianId")
	Integer numberOfMaintenanceRecordsCompleted(int technicianId);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId order by abs(timestampdiff(second, :currentMoment, mr.nextInspectionDueTime))")
	MaintenanceRecord nearestMaintenanceRecordByInspectionDueDate(int technicianId, Date currentMoment, PageRequest pageRequest);

	@Query("select i.maintenanceRecord.aircraft from InvolvedIn i where i.maintenanceRecord.technician.id = :technicianId group by i.maintenanceRecord.aircraft order by count(i.task) desc")
	List<Aircraft> topFiveAircraftsWithMostTasks(int technicianId, PageRequest pageRequest);

	@Query("select mr.estimatedCost.currency, AVG(mr.estimatedCost.amount), MIN(mr.estimatedCost.amount), MAX(mr.estimatedCost.amount), STDDEV(mr.estimatedCost.amount) from MaintenanceRecord mr where mr.technician.id = :technicianId and mr.moment >= :lastYearDate group by mr.estimatedCost.currency")
	List<Object[]> maintenanceRecordEstimatedCostLastYearStats(int technicianId, Date lastYearDate);

	@Query("select mr.estimatedCost.currency, count(mr) from MaintenanceRecord mr where mr.technician.id = :technicianId and mr.moment >= :lastYearDate group by mr.estimatedCost.currency")
	List<Object[]> countMaintenanceRecordsByCurrency(int technicianId, Date lastYearDate);

	@Query("select avg(i.task.estimatedDuration) from InvolvedIn i where i.maintenanceRecord.technician.id = :technicianId")
	Double averageTaskDuration(int technicianId);

	@Query("select min(i.task.estimatedDuration) from InvolvedIn i where i.maintenanceRecord.technician.id = :technicianId")
	Double minimumTaskDuration(int technicianId);

	@Query("select max(i.task.estimatedDuration) from InvolvedIn i where i.maintenanceRecord.technician.id = :technicianId")
	Double maximumTaskDuration(int technicianId);

	@Query("select stddev(i.task.estimatedDuration) from InvolvedIn i where i.maintenanceRecord.technician.id = :technicianId")
	Double deviationTaskDuration(int technicianId);

}
