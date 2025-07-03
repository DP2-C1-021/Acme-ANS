
package acme.features.flight_crew_member.flight_assignments;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S1.LegStatus;
import acme.entities.student3.AvailabilityStatus;
import acme.entities.student3.CurrentStatus;
import acme.entities.student3.Duty;
import acme.entities.student3.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		String method = super.getRequest().getMethod();
		boolean authorised;
		FlightAssignment flightAssignment = null;
		boolean isHis = false;
		if (method.equals("GET"))
			authorised = false;
		else {
			int flightAssignmentId = super.getRequest().getData("id", int.class);
			flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
			int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int legId = super.getRequest().getData("leg", int.class);
			boolean authorised3 = true;
			if (legId != 0)
				authorised3 = this.repository.existsLeg(legId);
			boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
			authorised = authorised3 && authorised1 && this.repository.thatFlightAssignmentIsOf(flightAssignmentId, flightCrewMemberId);
			isHis = flightAssignment.getFlightCrewMember().getId() == flightCrewMemberId;
		}
		super.getResponse().setAuthorised(authorised && flightAssignment != null && flightAssignment.isDraftMode() && isHis);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		Integer legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findMemberById(flightCrewMemberId);

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks");
		flightAssignment.setLeg(leg);
		flightAssignment.setFlightCrewMember(flightCrewMember);
	}

	private boolean isLegCompatible(final FlightAssignment flightAssignment) {
		Collection<Leg> legsByMember = this.repository.findLegsByFlightCrewMember(flightAssignment.getFlightCrewMember().getId());
		Leg newLeg = flightAssignment.getLeg();

		return legsByMember.stream().allMatch(existingLeg -> this.areLegsCompatible(newLeg, existingLeg));
	}

	private boolean areLegsCompatible(final Leg newLeg, final Leg oldLeg) {
		return !(MomentHelper.isInRange(newLeg.getScheduledDeparture(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()) || MomentHelper.isInRange(newLeg.getScheduledArrival(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()));
	}

	private void checkPilotAndCopilotAssignment(final FlightAssignment flightAssignment) {
		boolean havePilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignment.getLeg().getId(), Duty.PILOT);
		boolean haveCopilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignment.getLeg().getId(), Duty.COPILOT);

		if (Duty.PILOT.equals(flightAssignment.getDuty()))
			super.state(!havePilot, "duty", "acme.validation.FlightAssignment.havePilot.message");
		if (Duty.COPILOT.equals(flightAssignment.getDuty()))
			super.state(!haveCopilot, "duty", "acme.validation.FlightAssignment.haveCopilot.message");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		FlightAssignment original = this.repository.findFlightAssignmentById(flightAssignment.getId());
		Leg leg = flightAssignment.getLeg();
		AvailabilityStatus status = flightAssignment.getFlightCrewMember().getAvailabilityStatus();
		boolean cambioDuty = !original.getDuty().equals(flightAssignment.getDuty());
		boolean cambioLeg = !original.getLeg().equals(flightAssignment.getLeg());
		boolean completedLeg = flightAssignment.getLeg() == null ? false : LegStatus.LANDED.equals(flightAssignment.getLeg().getStatus());
		boolean draftedLeg = flightAssignment.getLeg() == null ? false : flightAssignment.getLeg().isDraftMode();
		boolean pendingCurrentStatus = flightAssignment.getCurrentStatus() != CurrentStatus.PENDING;

		if (leg != null && cambioLeg && !this.isLegCompatible(flightAssignment))
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignment.FlightCrewMemberIncompatibleLegs.message");

		if (leg != null && (cambioDuty || cambioLeg))
			this.checkPilotAndCopilotAssignment(flightAssignment);

		if (!pendingCurrentStatus)
			super.state(false, "currentStatus", "acme.validation.FlightAssignment.FlightCrewMemberIncompatibleLegs.message");

		if (draftedLeg)
			super.state(false, "leg", "acme.validation.FlightAssignment.DraftModeLeg.message");

		if (!AvailabilityStatus.AVAILABLE.equals(status))
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignment.OnlyAvailableCanBeAssigned.message");

		if (completedLeg)
			super.state(false, "leg", "acme.validation.FlightAssignment.LegAlreadyCompleted.message");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Collection<Leg> legs = this.repository.findAllLegs();
		boolean isCompleted;
		int flightAssignmentId;

		flightAssignmentId = super.getRequest().getData("id", int.class);

		LegStatus landed = LegStatus.LANDED;
		isCompleted = this.repository.areLegsCompletedByFlightAssignment(flightAssignmentId, landed);
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		SelectChoices currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		SelectChoices duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findMemberById(flightCrewMemberId);

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getCurrentMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMember.getEmployeeCode());
		dataset.put("isCompleted", isCompleted);
		dataset.put("draftMode", flightAssignment.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
