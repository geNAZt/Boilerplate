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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author geNAZt
 * @version 1.0
 */
@RequiredArgsConstructor
public class CommandRegistry {
	// Logger for this Module
	private final ModuleLogger moduleLogger;
	// Map which holds all commands
	private final Map<String, Command> commandMap = new ConcurrentHashMap<>();

	/**
	 * Register a new Command with a name and a instance of the Command itself
	 *
	 * @param name of the command
	 * @param command instance of the command which should be invoked when the user triggers the command
	 */
	public void registerCommand( String name, Command command ) {
		this.moduleLogger.info( "Wanting to register new command: " + name + " with instance " + command.toString() );

		// Check if we already have a command with this name
		if ( this.commandMap.containsKey( name ) ) {
			this.moduleLogger.warning( "Already have a command with the name " + name + " registered" );
			return;
		}

		// Register the command
		this.commandMap.put( name, command );
	}

	/**
	 * Process a incoming Command line. It gets splitted by all spaces and the first split is used as command name
	 * the rest is used as arguments to get passed to the Command itself
	 *
	 * @param line which has been entered from the user
	 * @return true if the command has been executed, false if not
	 */
	public boolean processCommand( String line ) {
		// Split the arguments
		String[] split = line.split( " " );
		String commandName = split[0];

		// Find the command
		Command command = this.commandMap.get( commandName );
		if ( command != null ) {
			// Reapply the arguments
			String[] arguments = new String[split.length - 1];
			System.arraycopy( split, 1, arguments, 0, split.length - 1 );

			// Run the command
			return command.run( arguments );
		}

		// Command not found
		return false;
	}
}
