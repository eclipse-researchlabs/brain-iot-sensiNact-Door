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
package org.eclipse.sensinact.gateway.brainiot.robotnik.door;

import org.eclipse.sensinact.gateway.brainiot.robotnik.door.model.DoorStatusChannel;
import org.eclipse.sensinact.gateway.brainiot.robotnik.door.model.DoorStatusChannelCollection;
import org.eclipse.sensinact.gateway.generic.Task.CommandType;
import org.eclipse.sensinact.gateway.generic.packet.annotation.CommandID;
import org.eclipse.sensinact.gateway.generic.packet.annotation.Data;
import org.eclipse.sensinact.gateway.generic.packet.annotation.ResourceID;
import org.eclipse.sensinact.gateway.generic.packet.annotation.ServiceID;
import org.eclipse.sensinact.gateway.generic.packet.annotation.ServiceProviderID;
import org.eclipse.sensinact.gateway.sthbnd.http.HttpResponse;
import org.eclipse.sensinact.gateway.sthbnd.http.HttpResponsePacket;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * the {@link HttpResponsePacket} wrapping the response of a door status request 
 */
public class DoorStatusResponsePacket extends HttpResponsePacket {

	private static final String DOOR_SERVICE_PROVIDER = "door";
	private static final String COMMAND_SERVICE = "command";
	private static final String STATUS_RESOURCE = "status";
	
	DoorStatusChannel[] doorStatusChannels;
	
	/**
	 * Constructor
	 * 
	 * @param response the {@link HttpResponse} on which the DoorStatusResponsePacket to
	 * be instantiated is based
	 */
	public DoorStatusResponsePacket(HttpResponse response) {
		super(response);
		try {
		    ObjectMapper mapper = new ObjectMapper();
		    DoorStatusChannelCollection doorStatusChannelCollection = mapper.readValue(content, DoorStatusChannelCollection.class);
		    doorStatusChannels = doorStatusChannelCollection.getDoorStatusChannels();
		} catch (Exception ex) {
		    ex.printStackTrace(); 
		    doorStatusChannels = new DoorStatusChannel[0];
		}		
	}

	@CommandID
	public CommandType getCommandId() {
		return CommandType.GET;
	}
	
	@ServiceProviderID
	public String getServiceProviderId() {
		return DOOR_SERVICE_PROVIDER;
	}
	
	@ServiceID
	public String getServiceId() {
		return COMMAND_SERVICE;
	}

	@ResourceID
	public String getResourceId() {
		return STATUS_RESOURCE;
	}

	@Data
	public Object getData() {
		String status = null;
		if(doorStatusChannels == null || doorStatusChannels.length < 4 )
			return status;
		int val = 0;
		for(int i=0;i<3;i++)
			val+=doorStatusChannels[i].getValue()*Math.pow(10d, (2-i));
		switch(val) {
		case 101:
			status="CLOSED";
			break;
		case 1:
			status="OPENED";
			break;
		case 0:
			status="PREVENTED";
			break;
		default:
			break;
		}
		return status;
	}

}
