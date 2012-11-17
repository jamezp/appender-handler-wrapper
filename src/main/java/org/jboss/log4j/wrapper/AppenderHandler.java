/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.log4j.wrapper;

import java.util.logging.Formatter;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.jboss.logmanager.ExtHandler;
import org.jboss.logmanager.ExtLogRecord;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public abstract class AppenderHandler<T extends Appender> extends ExtHandler {
    protected final T appender;
    private final boolean applyLayout;

    /**
     * Construct a new instance.
     *
     * @param appender the appender to delegate to
     */
    protected AppenderHandler(final T appender) {
        // Should be true as the formatter will always be set
        this(appender, true);
    }

    /**
     * Construct a new instance, possibly applying a {@code Layout} to the given appender instance.
     *
     * @param appender    the appender to delegate to
     * @param applyLayout {@code true} to apply an emulated layout, {@code false} otherwise
     */
    protected AppenderHandler(final T appender, final boolean applyLayout) {
        this.appender = appender;
        this.applyLayout = applyLayout;
        if (applyLayout) {
            appender.setLayout(null);
        }
    }

    /**
     * Get the log4j appender.
     *
     * @return the log4j appender
     */
    public Appender getAppender() {
        return appender;
    }

    /**
     * Activates the appender only if it's an {@link OptionHandler option handler}.
     */
    public void activate() {
        if (appender instanceof OptionHandler) {
            ((OptionHandler) appender).activateOptions();
        }
    }

    @Override
    public void setFormatter(final Formatter newFormatter) throws SecurityException {
        if (applyLayout) {
            final Appender appender = this.appender;
            if (appender != null) {
                appender.setLayout(new FormatterLayout(newFormatter));
            }
        }
        super.setFormatter(newFormatter);
    }

    @Override
    protected void doPublish(final ExtLogRecord record) {
        final Appender appender = this.appender;
        if (appender == null) {
            throw new IllegalStateException("Appender has been closed");
        }
        final LoggingEvent event = new WrappedLoggingEvent(record);
        appender.doAppend(event);
        super.doPublish(record);
    }

    @Override
    public void flush() {
        // Do nothing (there is no equivalent method on log4j appenders).
    }

    @Override
    public void close() throws SecurityException {
        checkAccess();
        appender.close();
    }

    /**
     * An emulator for log4j {@code Layout}s.
     *
     * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
     */
    public final class FormatterLayout extends Layout {
        private final Formatter formatter;

        /**
         * Construct a new instance.
         *
         * @param formatter the formatter to delegate to
         */
        public FormatterLayout(final Formatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public String format(final LoggingEvent event) {
            return formatter.format(WrappedLoggingEvent.getLogRecordFor(event));
        }

        @Override
        public boolean ignoresThrowable() {
            // just be safe
            return false;
        }

        @Override
        public void activateOptions() {
            // options are always activated already
        }
    }
}
