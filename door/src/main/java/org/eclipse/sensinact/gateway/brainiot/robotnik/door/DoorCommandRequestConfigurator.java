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

import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpTaskConfigurator;
import org.eclipse.sensinact.gateway.sthbnd.http.task.HttpTask;
import org.eclipse.sensinact.gateway.util.UriUtils;

/**
 * {@link HttpTaskConfigurator} dedicated to build the content of door command requests 
 */
public class DoorCommandRequestConfigurator implements HttpTaskConfigurator {

	@Override
	public <T extends HttpTask<?, ?>> void configure(T task) throws Exception {
		String[] pathElements = UriUtils.getUriElements(task.getPath());
		String command = pathElements[2].toLowerCase();
		switch(command) {
		case "open":
			task.setContent("{\"DOVal\": [{\"Ch\":0,\"Md\":0,\"Stat\":1,\"Val\":1,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":1,\"Md\":0,\"Stat\":0,\"Val\":1,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":2,\"Md\":1,\"Stat\":0,\"Val\":0,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":3,\"Md\":3,\"Stat\":1,\"Val\":0,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0}]}");
			break;
		case "close":
			task.setContent("{\"DOVal\": [{\"Ch\":0,\"Md\":0,\"Stat\":1,\"Val\":0,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":1,\"Md\":0,\"Stat\":0,\"Val\":0,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":2,\"Md\":1,\"Stat\":0,\"Val\":1,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0},{\"Ch\":3,\"Md\":3,\"Stat\":1,\"Val\":0,\"PsCtn\":0,\"PsStop\":0,\"PsIV\": 0}]}");
		default:
			break;
		}
	}

}
