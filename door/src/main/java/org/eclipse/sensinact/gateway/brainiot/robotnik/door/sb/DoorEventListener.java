/*
 * Copyright (c) 2020 Kentyou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
*    Kentyou - initial API and implementation
*/
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.sb;


import org.eclipse.sensinact.gateway.brainiot.door.api.CloseDoorRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.CommandDoorRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.DoorStatusRequest;
import org.eclipse.sensinact.gateway.brainiot.door.api.OpenDoorRequest;
import org.eclipse.sensinact.gateway.brainiot.service.api.SnaAct;
import org.eclipse.sensinact.gateway.brainiot.service.api.SnaGet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.brain.iot.eventing.annotation.SmartBehaviourDefinition;
import eu.brain.iot.eventing.api.EventBus;
import eu.brain.iot.eventing.api.SmartBehaviour;

@Component(immediate=true,service = SmartBehaviour.class)
@SmartBehaviourDefinition(consumed = {DoorStatusRequest.class,CloseDoorRequest.class,OpenDoorRequest.class}, filter="(timestamp=*)", name="Door Requests Listener")
public class DoorEventListener implements SmartBehaviour<CommandDoorRequest>  {

    private static final Logger LOG = LoggerFactory.getLogger(DoorEventListener.class);
    
    @Reference
    private EventBus bus;
    
	@Override
	public void notify(CommandDoorRequest event) {
		if(event == null) {
			LOG.error("Null event");
			return;
		}
		if(event instanceof DoorStatusRequest) {
			SnaGet get = new SnaGet();
			get.provider = "door";
			get.service = "command";
			get.resource = "status";
			bus.deliver(get);

		} else if(event instanceof OpenDoorRequest) {			
			SnaAct act = new SnaAct();
			act.provider = "door";
			act.service = "command";
			act.resource = "open";
			bus.deliver(act);

		} else if(event instanceof CloseDoorRequest) {			
			SnaAct act = new SnaAct();
			act.provider = "door";
			act.service = "command";
			act.resource = "close";
			bus.deliver(act);
		}
	}
}
