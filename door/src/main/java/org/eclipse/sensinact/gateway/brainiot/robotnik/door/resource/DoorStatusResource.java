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

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.sensinact.gateway.core.Attribute;
import org.eclipse.sensinact.gateway.core.ModelInstance;
import org.eclipse.sensinact.gateway.core.ResourceConfig;
import org.eclipse.sensinact.gateway.core.method.AccessMethod;
import org.eclipse.sensinact.gateway.generic.ExtModelInstance;
import org.eclipse.sensinact.gateway.generic.ExtResourceConfig;
import org.eclipse.sensinact.gateway.generic.ExtResourceDescriptor;
import org.eclipse.sensinact.gateway.generic.ExtResourceImpl;
import org.eclipse.sensinact.gateway.generic.ExtServiceImpl;
import org.eclipse.sensinact.gateway.generic.Task;
import org.eclipse.sensinact.gateway.util.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extended {@link ExtResourceImpl} dedicated to BrainIot Door Resource
 */
public class DoorStatusResource extends ExtResourceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(DoorStatusResource.class);
	private Timer timer;
	private int updated;
	
    /**
     * Constructor
     * 
     * @param modelInstance the {@link ModelInstance} to which the DoorStatusResource to be instantiated belongs to
     * @param resourceConfig the {@link ExtResourceConfig} describing the DoorStatusResource to be instantiated
     * @param service the {@link ExtServiceImpl} parent of the the DoorStatusResource to be instantiated
     */
    public DoorStatusResource(ExtModelInstance<?> modelInstance, ExtResourceConfig resourceConfig, ExtServiceImpl service) {
        super(modelInstance, resourceConfig, service);
    }

    
    @Override
    protected void updated(final Attribute attribute, final Object value, boolean hasChanged) {
		if (!super.started.get() || attribute.isHidden()) {
			return;
		}
		LOG.info("UPDATED DOOR STATUS = "+value);
		super.updated(attribute, value, hasChanged);
		if(!hasChanged)
			return;
		if("CLOSING".equals(value) || "OPENING".equals(value)) {
			if(this.timer != null) {
				super.modelInstance.mediator().error("Something went wrong : a timer already exists !");
				this.timer.cancel();
				this.timer = null;
			}
			this.timer = new Timer();
			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						DoorStatusResource.this.updated+=1;
						String[] pathElements = UriUtils.getUriElements(DoorStatusResource.this.getPath());
				        String service = null;
				        String resource = null;

				        if (pathElements.length > 2) {
				            service = pathElements[1];
				            resource = pathElements[2];
				        }
				        Task.CommandType command = Task.CommandType.valueOf(AccessMethod.GET);
				        ResourceConfig resourceConfig = ((ExtModelInstance<?>)DoorStatusResource.this.modelInstance
				        	).configuration().getResourceConfig(new ExtResourceDescriptor().withServiceName(service
				        			).withResourceName(resource));

				        Task task = ((ExtModelInstance<?>) DoorStatusResource.this.modelInstance
				        		).propagate(command, DoorStatusResource.this.getPath(), resourceConfig, null);

				        if (task != null) {
				            long wait = task.getTimeout();
				            while (!task.isResultAvailable() && wait > 0) {
				                try {
				                    Thread.sleep(150);
				                    wait -= 150;
				                } catch (InterruptedException e) {
				                    Thread.interrupted();
				                    ((ExtModelInstance<?>) DoorStatusResource.this.modelInstance
							        		).mediator().error(e);
				                    break;
				                }
				            }
				            if (!task.isResultAvailable()) {
				                task.abort(AccessMethod.EMPTY);
				            }
				        }
				        Object result = task.getResult();
				        if(result == null || result == AccessMethod.EMPTY)
				        	return;
				        boolean toBeUpdated = false;
				        switch(String.valueOf(value)) {
					        case "OPENING":
					        	if("OPENED".equals(result) || DoorStatusResource.this.updated > 5)
					        		toBeUpdated = true;
					        	break;
					        case "CLOSING":
					        	if("CLOSED".equals(result) || DoorStatusResource.this.updated > 5)
					        		toBeUpdated = true;
					        	break;
					        default:
				        		toBeUpdated = true;
					        	break;
				        }
				        if(toBeUpdated)
				        	DoorStatusResource.this.update(null, null, result, System.currentTimeMillis());
					} catch (Exception e) {
						DoorStatusResource.super.modelInstance.mediator().error(e);
						e.printStackTrace();
					}
				}				
			}, 3000, 2000);
		} else if(timer != null) {
			this.timer.cancel();
			this.timer = null;
			this.updated = 0;
		}
	}
}
