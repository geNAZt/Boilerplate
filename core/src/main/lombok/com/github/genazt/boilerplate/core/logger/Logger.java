/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.logger;

import com.github.genazt.boilerplate.core.ApplicationEntrypoint;
import jline.console.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A Logger which is based on top of the JDK Logger. This Logger needs jLine inside of the Classpath (libs.dep when loaded
 * with the Bootstrap).
 *
 * The Logger exports a property to set the logginglevel inside of the Application. By default this is set to "ALL" and
 * can be configured via -Dboilerplate.logginglevel=LEVEL where level can be INFO, WARNING, etc.
 *
 * @author geNAZt
 * @version 1.0
 */
public class Logger extends java.util.logging.Logger {
    private final LogDispatcher dispatcher;
    private final ApplicationEntrypoint applicationEntrypoint;

	/**
     * New Logger which directly starts to dispatch Logmessages. This Logger needs a preconfigured jLine
     * Consolereader to function. Also it needs the ApplicationEntrypoint used to determinate when the Application
     * has been shut down so it can clean up properly.
     *
     * @param applicationEntrypoint     which has been used to startup the application
     * @param consoleReader             which has been preconfigured
     */
    public Logger( ApplicationEntrypoint applicationEntrypoint, ConsoleReader consoleReader ) {
        // Let the JDK Logger build up
        super( applicationEntrypoint.getApplicatioName() + "Logger", null );

        // Build up the LogDispatcher
        this.applicationEntrypoint = applicationEntrypoint;
        this.dispatcher = new LogDispatcher( this.applicationEntrypoint, this );

        // Check on which level we want to log
        setLevel( Level.parse( System.getProperty( "boilerplate.logginglevel", "ALL" ) ) );

        try {
            // Create the folder for the logfiles if needed
            File logDir = new File( "logs" );
            if ( !logDir.exists() ) {
                if ( !logDir.mkdir() ) {
                    throw new RuntimeException( "Could not create log dir. Please be sure to check the Filesystem permissions" );
                }
            }

            // Add a new Handler for Files which logs a maximum of 8 Files a 16 MB logs
            FileHandler fileHandler = new FileHandler( "logs" + File.separator + "log.log", 1 << 24, 8, true );
            fileHandler.setFormatter( new ConciseFormatter( false ) );
            addHandler( fileHandler );

            // Add a new Handler which colors the logrecord and then prints it on the Console via jLine
            ConsoleWriterHandler consoleHandler = new ConsoleWriterHandler( consoleReader );
            consoleHandler.setFormatter( new ConciseFormatter( true ) );
            addHandler( consoleHandler );
        } catch ( IOException ex ) {
            System.err.println( "Could not register logger!" );
            ex.printStackTrace();
        }

        // Execute the dispatcher
		applicationEntrypoint.getExecutorService().execute( this.dispatcher );
    }

    @Override
    public void log( LogRecord record ) {
        dispatcher.queue( record );
    }

    void doLog( LogRecord record ) {
        super.log( record );
    }
}