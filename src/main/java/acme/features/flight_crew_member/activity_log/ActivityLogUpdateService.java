
package acme.features.flight_crew_member.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.Leg;
import acme.entities.student3.ActivityLog;
import acme.entities.student3.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class ActivityLogUpdateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int activityLogId;
			ActivityLog activityLog;

			activityLogId = super.getRequest().getData("id", int.class);
			activityLog = this.repository.findActivityLogById(activityLogId);
			if (activityLog != null) {
				int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
				boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
				boolean authorised = authorised1 && this.repository.thatActivityLogIsOf(activityLogId, flightCrewMemberId);

				status = authorised && activityLog != null && activityLog.isDraftMode();
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;
		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {

		if (activityLog == null)
			return;
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentByActivityLogId(activityLog.getId());
		if (activityLog.getRegistrationMoment() == null || flightAssignment == null)
			return;
		Leg leg = flightAssignment.getLeg();
		if (leg == null || leg.getScheduledArrival() == null)
			return;
		boolean activityLogMomentIsAfterscheduledArrival = this.repository.associatedWithCompletedLeg(activityLog.getId(), MomentHelper.getCurrentMoment());
		super.state(activityLogMomentIsAfterscheduledArrival, "WrongActivityLogDate", "acme.validation.activityLog.wrongMoment.message");

	}

	@Override
	public void perform(final ActivityLog activityLog) {

		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setDraftMode(true);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("draftMode", activityLog.isDraftMode());
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);
	}
}
