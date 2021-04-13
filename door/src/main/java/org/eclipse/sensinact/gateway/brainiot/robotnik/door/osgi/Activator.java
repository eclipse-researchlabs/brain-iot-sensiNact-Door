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
package org.eclipse.sensinact.gateway.brainiot.robotnik.door.osgi;

import java.util.Base64;

import org.eclipse.sensinact.gateway.brainiot.robotnik.door.DoorCommandRequestConfigurator;
import org.eclipse.sensinact.gateway.brainiot.robotnik.door.DoorStatusResponsePacket;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.execution.Executable;
import org.eclipse.sensinact.gateway.generic.Task.CommandType;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.HttpTaskConfiguration;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.HttpTasks;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.KeyValuePair;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.SimpleHttpTask;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.DefaultHttpTaskProcessingContext;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.DefaultHttpTaskProcessingContextFactory;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpActivator;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpTaskConfigurator;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpTaskProcessingContext;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpTaskProcessingContextFactory;
import org.eclipse.sensinact.gateway.sthbnd.http.task.HttpTask;

/**
 * In charge of activating / deactivating the module
 */
@HttpTasks(tasks = {
	@SimpleHttpTask(
		commands = {CommandType.GET},
		configuration = @HttpTaskConfiguration(
			httpMethod = "GET",
			host = "$(brainiot.door.host)",
			port = "$(brainiot.door.port)",
			path = "$(brainiot.door.path.get)",
			packet = DoorStatusResponsePacket.class,
			headers = {
				@KeyValuePair(key = "Authorization", value = "@context[brainiot.door.auth]")
			})
		
	),
	@SimpleHttpTask(
		commands = {CommandType.ACT},
		configuration = @HttpTaskConfiguration(
			httpMethod = "POST",
			host = "$(brainiot.door.host)",
			port = "$(brainiot.door.port)",
			path = "$(brainiot.door.path.post)",
			direct = true,
			content = DoorCommandRequestConfigurator.class,
			headers = {
				@KeyValuePair(key = "Authorization", value = "@context[brainiot.door.auth]"),
				@KeyValuePair(key = "Content-Type", value = "application/json")
			})
		)
})
public class Activator extends HttpActivator {
	
    private String authorizationHeader;

	@Override
    public void doStart() throws Exception {
		this.authorizationHeader = null;
        Object prop = mediator.getProperty("brainiot.door.auth");
        if (prop != null) {
	        byte[] encoded = Base64.getEncoder().encode(String.valueOf(prop).getBytes());
	        String result = String.format("Basic %s",new String(encoded));
	        this.authorizationHeader = result;
        }
        super.doStart();
    }
    
	@Override
    public HttpTaskProcessingContextFactory getTaskProcessingContextFactory() {
        return new DefaultHttpTaskProcessingContextFactory(mediator) {
            @Override
            public HttpTaskProcessingContext newInstance(HttpTaskConfigurator httpTaskConfigurator, String endpointId, HttpTask<?,?> task) {
                return new BrainIoTDoorTaskProcessingContext(Activator.this.mediator, httpTaskConfigurator, endpointId, task);
            }

        };
    }
	
	/**
     * Extended {@link DefaultHttpTaskProcessingContext} dedicated BrainIoT Door tasks processing context
     */
    private class BrainIoTDoorTaskProcessingContext extends DefaultHttpTaskProcessingContext {
       
        public <TASK extends HttpTask<?,?>> BrainIoTDoorTaskProcessingContext(Mediator mediator, HttpTaskConfigurator httpTaskConfigurator, final String endpointId, final TASK task) {
            super(mediator, httpTaskConfigurator, endpointId, task);
            super.properties.put("brainiot.door.auth", new Executable<Void, String>() {
                @Override
                public String execute(Void parameter) throws Exception {
                    return Activator.this.authorizationHeader;
                }
            });
        }

    }

}
