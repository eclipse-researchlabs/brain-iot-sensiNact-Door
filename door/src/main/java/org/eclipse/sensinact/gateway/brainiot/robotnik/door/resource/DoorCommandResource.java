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
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.resource;

import org.eclipse.sensinact.gateway.core.Attribute;
import org.eclipse.sensinact.gateway.core.DataResource;
import org.eclipse.sensinact.gateway.core.ModelInstance;
import org.eclipse.sensinact.gateway.core.ResourceImpl;
import org.eclipse.sensinact.gateway.core.method.AccessMethod;
import org.eclipse.sensinact.gateway.generic.ExtModelInstance;
import org.eclipse.sensinact.gateway.generic.ExtResourceConfig;
import org.eclipse.sensinact.gateway.generic.ExtResourceImpl;
import org.eclipse.sensinact.gateway.generic.ExtServiceImpl;
import org.eclipse.sensinact.gateway.generic.annotation.Act;
import org.json.JSONObject;


/**
 * Extended {@link ExtResourceImpl} dedicated to BrainIot Door Resource
 */
public class DoorCommandResource extends ExtResourceImpl {

    private Attribute statusAttribute;

	/**
     * Constructor
     * 
     * @param modelInstance the {@link ModelInstance} to which the DoorStatusResource to be instantiated belongs to
     * @param resourceConfig the {@link ExtResourceConfig} describing the DoorStatusResource to be instantiated
     * @param service the {@link ExtServiceImpl} parent of the the DoorStatusResource to be instantiated
     */
    public DoorCommandResource(ExtModelInstance<?> modelInstance, ExtResourceConfig resourceConfig, ExtServiceImpl service) {
        super(modelInstance, resourceConfig, service);
    }

    /*
     * (non-javadoc)
     *  Define the status to be applied according to the current one and the command to be executed
     */
    private String processCommand() { 
    	
    	if(this.statusAttribute == null) {
    		ResourceImpl statusResource = ((ExtServiceImpl)super.parent).getResource("status");
            this.statusAttribute = statusResource==null?null:statusResource.getAttribute(DataResource.VALUE);
    	}    	
    	if(statusAttribute == null)
    		return null;
    	
    	Object obj = this.statusAttribute.getValue();
    	String status = obj == null ? null :String.valueOf(obj);
		
		if(status != null) {
			switch(status) {
				case "OPENING":
				case "CLOSING":
				case "PREVENTED":
					//do nothing if already opening, closing or if in prevented state
					return getName();
				case "CLOSED":
					switch(getName()) {
					   case "open":
						   //execute open command if currently closed 
						   return "OPENING";
					   case "close":
					   default:
						   //otherwise do nothing
						   return getName();
					}
				case "OPENED":
					switch(getName()) {
					   case "close":
						   //execute close command if currently opened 
						   return "CLOSING";
					   case "open":
					   default:
						   //otherwise do nothing
						   return getName();
					}
				default:
					return getName();
			}
	    } else {
			switch(getName()) {
			   case "open":
				   //execute open command if currently closed 
				   return "OPENING";
			   case "close":
				   //execute open command if currently closed 
				   return "CLOSING";
			   default:
				   //otherwise do nothing
				   return getName();
			}
	    }
    }

    @Override
    protected JSONObject passOn(String type, String uri, Object[] parameters) throws Exception {
    	String status = null;
    	if(AccessMethod.ACT.equals(type)) {
    		status = processCommand();
    		if(status==null || getName().equals(status)) 
    			return null;
    	}
    	JSONObject obj = super.passOn(type, uri, parameters);
    	if(AccessMethod.ACT.equals(type)) 
    		this.statusAttribute.setValue(status);
    	return obj;
    }
    
    @Act
    public JSONObject actuating() {
    	return new JSONObject().put("message","actuation done!");
    }
}
