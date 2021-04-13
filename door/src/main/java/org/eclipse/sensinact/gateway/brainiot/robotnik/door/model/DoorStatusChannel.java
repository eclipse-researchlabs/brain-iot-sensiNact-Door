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
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Door API Status Channel
 */
@JsonIgnoreProperties(value={"Md","Stat","Cnting","ClrCnt","OvLch"})
public class DoorStatusChannel {

    @JsonProperty("Ch")
    private int identifier;
    
    @JsonProperty("Val")
    private int value;
    
	/**
	 * Constructor
	 */
	public DoorStatusChannel() {
	}

	/**
	 * Constructor
	 * 
	 * @param identifier
	 * @param value
	 */
	public DoorStatusChannel(int identifier, int value) {
		this.identifier = identifier; 
		this.value = value;
    }

	public int getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
