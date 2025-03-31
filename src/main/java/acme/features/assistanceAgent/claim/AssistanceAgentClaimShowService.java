
package acme.features.assistanceAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.Indicator;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.entities.S4.ClaimType;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegByClaimId(id);
		claim = this.repository.findClaimById(id);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices indicatorChoices = SelectChoices.from(Indicator.class, claim.getIndicator());
		SelectChoices legChoices = SelectChoices.from(this.repository.findAvailableLegs(), "flightNumber", claim.getLeg());
		SelectChoices draftModeChoices = new SelectChoices();
		draftModeChoices.add("true", "True", claim.isDraftMode());
		draftModeChoices.add("false", "False", !claim.isDraftMode());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode", "leg");
		dataset.put("type", claimTypeChoices);
		dataset.put("indicator", indicatorChoices);
		dataset.put("draftMode", draftModeChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
