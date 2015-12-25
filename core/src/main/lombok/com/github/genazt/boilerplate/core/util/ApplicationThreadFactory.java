/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.util;

import com.github.genazt.boilerplate.core.ApplicationEntrypoint;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a ThreadFactory which generates Threads and names them after the poolnumber and the name of the application
 * which gets out of a {@link ApplicationEntrypoint} instance
 *
 * @author geNAZt
 * @version 1.0
 */
public class ApplicationThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger( 1 );
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger( 1 );
	private final String namePrefix;

	/**
	 * Construct a new ThreadFactory for the given ApplicationEntryPoint
	 *
	 * @param applicationEntrypoint from which the name should be used
	 */
	public ApplicationThreadFactory( ApplicationEntrypoint applicationEntrypoint ) {
		SecurityManager s = System.getSecurityManager();
		group = ( s != null ) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = applicationEntrypoint.getApplicatioName() + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	@Override
	public Thread newThread( Runnable r ) {
		Thread t = new Thread( group, r, namePrefix + threadNumber.getAndIncrement(), 0 );

		if ( t.isDaemon() ) {
			t.setDaemon( false );
		}

		if ( t.getPriority() != Thread.NORM_PRIORITY ) {
			t.setPriority( Thread.NORM_PRIORITY );
		}

		return t;
	}
}
