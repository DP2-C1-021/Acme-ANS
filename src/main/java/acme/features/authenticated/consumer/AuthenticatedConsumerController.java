/*
 * AuthenticatedConsumerController.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Consumer;

@GuiController
public class AuthenticatedConsumerController extends AbstractGuiController<Authenticated, Consumer> {

	// Internal state ---------------------------------------------------------

	private final AuthenticatedConsumerCreateService	createService;
	private final AuthenticatedConsumerUpdateService	updateService;


	@Autowired
	public AuthenticatedConsumerController(final AuthenticatedConsumerCreateService createService, final AuthenticatedConsumerUpdateService updateService) {
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
