/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.command;

/**
 * @author geNAZt
 * @version 1.0
 */
public interface Command {
	/**
	 * Execute the given command
	 *
	 * @param args arguments which have been given into the command
	 * @return true on success, false on failure
	 */
	boolean run( String[] args );
}
