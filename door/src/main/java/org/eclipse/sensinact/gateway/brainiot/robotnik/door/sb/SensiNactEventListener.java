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


import org.eclipse.sensinact.gateway.brainiot.door.api.CloseDoorResponse;
import org.eclipse.sensinact.gateway.brainiot.door.api.CommandDoorStatus;
import org.eclipse.sensinact.gateway.brainiot.door.api.DoorStatus;
import org.eclipse.sensinact.gateway.brainiot.door.api.DoorStatusResponse;
import org.eclipse.sensinact.gateway.brainiot.door.api.OpenDoorResponse;
import org.eclipse.sensinact.gateway.brainiot.robotnik.door.model.NameTypeValue;
import org.eclipse.sensinact.gateway.brainiot.service.api.EventBusResponseEvent;
import org.eclipse.sensinact.gateway.brainiot.service.api.SnaActResponse;
import org.eclipse.sensinact.gateway.brainiot.service.api.SnaEvent;
import org.eclipse.sensinact.gateway.brainiot.service.api.SnaGetResponse;
import org.eclipse.sensinact.gateway.util.UriUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.brain.iot.eventing.annotation.SmartBehaviourDefinition;
import eu.brain.iot.eventing.api.EventBus;
import eu.brain.iot.eventing.api.SmartBehaviour;

@Component(immediate=true,service = SmartBehaviour.class)
@SmartBehaviourDefinition(consumed = {SnaActResponse.class, SnaGetResponse.class}, filter="(timestamp=*)", name="Sna Responses Listener")
public class SensiNactEventListener implements SmartBehaviour<SnaEvent>  {

    private static final Logger LOG = LoggerFactory.getLogger(SensiNactEventListener.class);

    @Reference
    private EventBus bus;
    
	@Override
	public void notify(SnaEvent event) {
		if(event == null) {
			LOG.error("Null event");
			return;
		}
		if(!(event instanceof EventBusResponseEvent)) {
			LOG.debug("Unhandled event type : "+event.getClass());
			return;
		}
		switch(event.getClass().getSimpleName()){
			case "SnaActResponse" :
				final SnaActResponse r = (SnaActResponse) event;
				String provider = r.provider;
				String service = r.service;
				String resource = r.resource;
				if(!"door".equals(provider) || !"command".equals(service)){
					LOG.debug("Unhandled source : "+ UriUtils.getUri(new String[] {provider,service,resource}));
					return;
				}					
				switch(resource) {
					case "open":
							OpenDoorResponse open = new OpenDoorResponse();
							open.response = r.status==200?CommandDoorStatus.SUCCESS:CommandDoorStatus.FAILURE;
							open.message = r.value;
							bus.deliver(open);
						break;
					case "close" :
							CloseDoorResponse close = new CloseDoorResponse();
							close.response = r.status==200?CommandDoorStatus.SUCCESS:CommandDoorStatus.FAILURE;
							close.message = r.value;
							bus.deliver(close);
						break;
					default : 
						LOG.debug("Unhandled actuator : "+resource);
						break;
				}
				break;
			case "SnaGetResponse" : 
				final SnaGetResponse g = (SnaGetResponse) event;
				provider = g.provider;
				service = g.service;
				resource = g.resource;
				if(!"door".equals(provider) || !"command".equals(service)|| !"status".equals(resource)){
					LOG.debug("Unhandled source : "+ UriUtils.getUri(new String[] {provider,service,resource}));
					return;
				}	
				DoorStatusResponse resp = new DoorStatusResponse();
				try {
					ObjectMapper mapper = new ObjectMapper();
					NameTypeValue ntv = mapper.readValue(g.value, NameTypeValue.class);
					resp.status = ntv.getValue()==null?null:DoorStatus.valueOf(String.valueOf(ntv.getValue()));
					bus.deliver(resp);
				} catch(Exception e) {
					LOG.error(e.getMessage(),e);
				}
				break;
			default:
				LOG.debug("Unhandled event type :"+event.getClass());
				return;
		}
	}
}
