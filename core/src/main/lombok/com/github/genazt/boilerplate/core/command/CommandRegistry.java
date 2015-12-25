/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.command;

import com.github.genazt.boilerplate.core.logger.ModuleLogger;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt
 * @version 1.0
 */
@RequiredArgsConstructor
public class CommandRegistry {
	// Logger for this Module
	private final ModuleLogger moduleLogger;
	// Map which holds all commands
	private final Map<String, Command> commandMap = new HashMap<>();

	/**
	 * Register a new Command with a name and a instance of the Command itself
	 *
	 * @param name of the command
	 * @param command instance of the command which should be invoked when the user triggers the command
	 */
	public void registerCommand( String name, Command command ) {
		this.moduleLogger.info( "Wanting to register new command: " + name + " with instance " + command.toString() );
	}
}
