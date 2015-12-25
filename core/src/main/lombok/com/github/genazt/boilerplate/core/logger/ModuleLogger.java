/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.logger;

import com.github.genazt.boilerplate.core.ApplicationEntrypoint;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A ModuleLogger is used to suffix a Name of the Module into the Logger. You can chain ModuleLoggers but at least the
 * top one needs to have the {@link ApplicationEntrypoint#getLogger()} as parent.
 *
 * @author geNAZt
 * @version 1.0
 */
public class ModuleLogger extends Logger {
    private final Logger parent;

	/**
	 * Construct a new ModuleLogger
	 *
	 * @param name		of the Module which should be suffix
	 * @param parent	to log into
	 */
    public ModuleLogger( String name, Logger parent ) {
        super( parent.getName() + "/" + name, null );
        this.parent = parent;
        setLevel( parent.getLevel() );
    }

    @Override
    public void log( LogRecord logRecord ) {
        parent.log( logRecord );
    }
}
