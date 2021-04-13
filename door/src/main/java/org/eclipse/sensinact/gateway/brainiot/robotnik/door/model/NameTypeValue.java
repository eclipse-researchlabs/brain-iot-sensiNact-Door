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

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameTypeValue {
	
	@JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private Object value;

    @JsonProperty("timestamp")
    private long timestamp;
    
    public NameTypeValue() {}

	public NameTypeValue(String name, String type, Object value, long timestamp) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
