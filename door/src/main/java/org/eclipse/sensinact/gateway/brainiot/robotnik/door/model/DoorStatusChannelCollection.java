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
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Door API Status Channel Collection (Uplink)
 */
public class DoorStatusChannelCollection {

    @JsonProperty("DIVal")
    private DoorStatusChannel[] doorStatusChannels;
    
	/**
	 * Constructor
	 */
	public DoorStatusChannelCollection() {
	}

	/**
	 * Constructor
	 * 
	 * @param doorStatusChannels
	 */
	public DoorStatusChannelCollection(DoorStatusChannel[] doorStatusChannels) {
		this.doorStatusChannels = doorStatusChannels; 
    }

	public DoorStatusChannel[] getDoorStatusChannels() {
		return this.doorStatusChannels;
	}

	public void setDoorStatusChannels(DoorStatusChannel[] doorStatusChannels) {
		this.doorStatusChannels = doorStatusChannels;
	}
}
