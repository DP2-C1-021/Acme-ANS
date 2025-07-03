/*
 * AuthenticatedAssistanceAgentController.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.assistance_agent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.assistance_agent.AssistanceAgent;

@GuiController
public class AuthenticatedAssistanceAgentController extends AbstractGuiController<Authenticated, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	private final AuthenticatedAssistanceAgentCreateService	createService;
	private final AuthenticatedAssistanceAgentUpdateService	updateService;


	@Autowired
	public AuthenticatedAssistanceAgentController(final AuthenticatedAssistanceAgentCreateService createService, final AuthenticatedAssistanceAgentUpdateService updateService) {
		this.createService = createService;
		this.updateService = updateService;
	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
