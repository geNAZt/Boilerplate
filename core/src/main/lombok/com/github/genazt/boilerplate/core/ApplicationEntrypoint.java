/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core;

import com.github.genazt.boilerplate.core.command.CommandRegistry;
import com.github.genazt.boilerplate.core.logger.Logger;
import com.github.genazt.boilerplate.core.logger.LoggingOutputStream;
import com.github.genazt.boilerplate.core.logger.ModuleLogger;
import com.github.genazt.boilerplate.core.util.ApplicationThreadFactory;
import com.github.genazt.boilerplate.core.util.ConsoleColor;
import jline.console.ConsoleReader;
import lombok.Getter;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

/**
 * A abstract Class to extend from when you want to bootstrap a Application. This inits the jLine console Reader
 * and can be used to init all Libs needed.
 *
 * @author geNAZt
 * @version 1.0
 */
public class ApplicationEntrypoint {
	private final AtomicBoolean running = new AtomicBoolean( true );

	private ConsoleReader consoleReader;
	protected CommandRegistry commandRegistry;

	/**
	 * Core logger of this Application. This is needed for any submodule Loggers which you can generate via
	 * the {@link ModuleLogger}
	 *
	 * @return core logger of the application
	 */
	@Getter
	protected Logger logger;

	/**
	 * Name of the application. Used for the Logger and ExecutorService Names for example.
	 *
	 * @return name of the application
	 */
	@Getter
	protected final String applicatioName;

	/**
	 * ExecutorService of this application
	 *
	 * @return scheduled executor service which should be used everywhere possible
	 */
	@Getter
	protected final ScheduledExecutorService executorService;

	public ApplicationEntrypoint( String applicationName, String[] args ) {
		this.applicatioName = applicationName;
		this.executorService = new ScheduledThreadPoolExecutor( 8, new ApplicationThreadFactory( this ) );

		/**
		 * Init output logging and jLine
		 */

		// Some hacks to get it working..... Better not look here
		System.setProperty( "library.jansi.version", "CustomApplication" );
		System.setProperty( "jline.WindowsTerminal.directConsole", "false" );
		AnsiConsole.systemInstall();

		// Console init and Stream redirections
		try {
			this.consoleReader = new ConsoleReader();
			this.consoleReader.setExpandEvents( false );

			this.logger = new Logger( this, this.consoleReader );
			System.setErr( new PrintStream( new LoggingOutputStream( this.logger, Level.SEVERE ), true ) );
			System.setOut( new PrintStream( new LoggingOutputStream( this.logger, Level.INFO ), true ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		this.logger.info( "Inited jLine and remapped stdout and stderr" );

		/**
		 * Restore the terminal after shutdown
		 */
		Runtime.getRuntime().addShutdownHook( new Thread( "JLine Cleanup Thread" ) {
			@Override
			public void run() {
				try {
					consoleReader.getTerminal().restore();
				} catch ( Exception ex ) {
					// Ignored
				}
			}
		} );

		/**
		 * Init all Commands and the Registry for it
		 */
		this.commandRegistry = new CommandRegistry( new ModuleLogger( "CommandRegistry", this.logger ) );
	}

	/**
	 * Check if the Application is still running or not. This should be used to determinate if we need a
	 * safe shutdown or not.
	 *
	 * @return true when Application should run, false when it should shutdown
	 */
	public boolean isRunning() {
		return this.running.get();
	}

	/**
	 * Run the main thread loop which only handles Command inputs. You must execute this Method when you fully inited
	 * your Application.
	 *
	 * THIS WILL BLOCK ANY FURTHER CALCULATIONS ON THE MAIN THREAD!!!
	 */
	protected void mainThreadLoop() {
		try {
			while ( isRunning() ) {
				String line = this.consoleReader.readLine( ConsoleColor.AQUA + "> " );
				if ( line != null ) {
					if ( !this.commandRegistry.processCommand( line ) ) {
						this.logger.info( "Invalid command: " + line );
					}
				}
			}
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}
}
