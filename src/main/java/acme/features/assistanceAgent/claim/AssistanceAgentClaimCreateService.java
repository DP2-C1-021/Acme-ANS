
package acme.features.assistanceAgent.claim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.ClaimType;
import acme.entities.S4.Indicator;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		AssistanceAgent assistanceAgent;
		boolean status;
		boolean bool;
		int legId;
		Leg leg;

		if (super.getRequest().getMethod().equals("GET"))
			bool = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);

			boolean isLegValid = leg != null;
			boolean isLegNotDraft = isLegValid && !leg.isDraftMode();
			boolean isFlightNotDraft = isLegNotDraft && !leg.getFlight().getDraftMode();
			boolean isLegIdZero = legId == 0;

			bool = isLegIdZero || isLegValid && isLegNotDraft && isFlightNotDraft;
		}

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		status = bool && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim = new Claim();
		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int agentId = principal.getId();
		AssistanceAgent agent = this.repository.findAssistanceAgentById(agentId);
		Date today = MomentHelper.getCurrentMoment();

		claim.setAssistanceAgent(agent);
		claim.setRegistrationMoment(today);
		claim.setIndicator(Indicator.PENDING);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "leg");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim object) {
	}

	@Override
	public void perform(final Claim object) {

		object.setRegistrationMoment(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		Dataset dataset;
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, object.getType());
		SelectChoices indicatorChoices = SelectChoices.from(Indicator.class, object.getIndicator());
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegs(), "flightNumber", object.getLeg());

		dataset = super.unbindObject(object, "registrationMoment", "passengerEmail", "description", "leg", "indicator", "type");
		dataset.put("types", claimTypeChoices);
		dataset.put("indicators", indicatorChoices);
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
