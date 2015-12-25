/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.logger;

import com.github.genazt.boilerplate.core.ApplicationEntrypoint;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

/**
 * A async dispatcher for log messages
 *
 * @author geNAZt
 * @version 1.0
 */
@RequiredArgsConstructor
public class LogDispatcher implements Runnable {
    private final ApplicationEntrypoint applicationEntrypoint;
    private final Logger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
		// Don't run if the application has been shutdown
        while ( this.applicationEntrypoint.isRunning() ) {
            LogRecord record;

            try {
                record = queue.poll( 5, TimeUnit.MILLISECONDS );
            } catch ( InterruptedException ex ) {
				// Continue if we got interrupted to check if the application is still up
                continue;
            }

            if ( record != null ) {
                logger.doLog( record );
            }
        }

		// When the application did shut down, log the remaining messages from the queue
        for ( LogRecord logRecord : this.queue ) {
            logger.doLog( logRecord );
        }
    }

	/**
	 * Queue up a new Logrecord to be async dispatched to the underlying Handlers
	 *
	 * @param record which should be queued for dispatch
	 */
    public void queue( LogRecord record ) {
        if ( this.applicationEntrypoint.isRunning() ) {
            queue.add( record );
        }
    }
}