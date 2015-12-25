/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.logger;

import jline.console.ConsoleReader;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Redirect any output into the Logger instead of printing it directly into the pipe. It takes a level and a logger.
 * The level decides how the messages from the Pipe get into the Logger. For example stderr should go to
 * {@link Level#SEVERE}, stdout should go to {@link Level#INFO}
 *
 * @author geNAZt
 * @version 1.0
 */
@RequiredArgsConstructor
public class LoggingOutputStream extends ByteArrayOutputStream {
    private final Logger logger;
    private final Level level;

    @Override
    public void flush() throws IOException {
		// Get the content which is inside the underlying ByteArray and reset the ByteArray
        String contents = toString();
        super.reset();

		// Only take contents which are non empty
        if ( !contents.isEmpty() && !contents.equals( ConsoleReader.CR ) ) {
            logger.logp( level, "", "", contents );
        }
    }
}