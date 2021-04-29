/*
 * Copyright (c) 2020 - 2021 Kentyou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
*    Kentyou - initial API and implementation
*/
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.sb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.sensinact.gateway.brainiot.door.api.CloseDoorRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.CloseDoorResponse;
import org.eclipse.sensinact.gateway.brainiot.door.api.CommandDoorResponse;
import org.eclipse.sensinact.gateway.brainiot.door.api.DoorStatusRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.DoorStatusResponse;
import org.eclipse.sensinact.gateway.brainiot.door.api.OpenDoorRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.OpenDoorResponse;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.brain.iot.eventing.annotation.SmartBehaviourDefinition;
import eu.brain.iot.eventing.api.EventBus;
import eu.brain.iot.eventing.api.SmartBehaviour;

@Component(immediate=true,service = SmartBehaviour.class)
@JaxrsResource
@SmartBehaviourDefinition(consumed = {DoorStatusResponse.class,CloseDoorResponse.class,OpenDoorResponse.class}, filter="(timestamp=*)", name="Door Responses Listener")
public class DoorEventSender implements SmartBehaviour<CommandDoorResponse> {

	private static final Logger LOG = LoggerFactory.getLogger(DoorEventSender.class);

	@Reference
	private EventBus eventBus;
	
	@Activate
	@Modified
	protected void activate() {
		LOG.info("Sender Component ACTIVATED");
	}

	@GET
	@Path("/get")
	public String get() {
		DoorStatusRequest msg = new DoorStatusRequest();
		LOG.info("Sending message : STATUS");				
		eventBus.deliver(msg);
		return "Sending message : STATUS";
	}

	@GET
	@Path("/close")
	public String close() {
		CloseDoorRequest msg = new CloseDoorRequest();
		LOG.info("Sending message : CLOSE");				
		eventBus.deliver(msg);
		return "Sending message : CLOSE";
	}

	@GET
	@Path("/open")
	public String open() {
		OpenDoorRequest msg = new OpenDoorRequest();
		LOG.info("Sending message : OPEN");				
		eventBus.deliver(msg);
		return "Sending message : OPEN";
	}

	@Override
	public void notify(CommandDoorResponse event) {
		LOG.info("===================================>");
		LOG.info("RESPONSE : "+event);
		LOG.info("===================================>");
	}
}
