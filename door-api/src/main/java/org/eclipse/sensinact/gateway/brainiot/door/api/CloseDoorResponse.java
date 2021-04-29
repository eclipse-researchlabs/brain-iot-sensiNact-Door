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
package org.eclipse.sensinact.gateway.brainiot.door.api;

/**
 * Response for door closing request
 */
public class CloseDoorResponse extends CommandDoorResponse{

	/**
	 * Status of the command execution
	 */
	public CommandDoorStatus response;
	
	/**
	 * Error message if relevant
	 */
	public String message;
}

