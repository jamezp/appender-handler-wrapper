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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Priority;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class LevelMapping {


    private static final Map<Level, org.apache.log4j.Level> priorityMap;

    private LevelMapping() {
    }

    static {
        final Map<Level, org.apache.log4j.Level> map = new IdentityHashMap<Level, org.apache.log4j.Level>();
        map.put(Level.SEVERE, Log4jJDKLevel.SEVERE);
        map.put(Level.WARNING, Log4jJDKLevel.WARNING);
        map.put(Level.CONFIG, Log4jJDKLevel.CONFIG);
        map.put(Level.INFO, Log4jJDKLevel.INFO);
        map.put(Level.FINE, Log4jJDKLevel.FINE);
        map.put(Level.FINER, Log4jJDKLevel.FINER);
        map.put(Level.FINEST, Log4jJDKLevel.FINEST);

        map.put(org.jboss.logmanager.Level.FATAL, org.apache.log4j.Level.FATAL);
        map.put(org.jboss.logmanager.Level.ERROR, org.apache.log4j.Level.ERROR);
        map.put(org.jboss.logmanager.Level.WARN, org.apache.log4j.Level.WARN);
        map.put(org.jboss.logmanager.Level.INFO, org.apache.log4j.Level.INFO);
        map.put(org.jboss.logmanager.Level.DEBUG, org.apache.log4j.Level.DEBUG);
        map.put(org.jboss.logmanager.Level.TRACE, org.apache.log4j.Level.TRACE);
        priorityMap = map;
    }

    static org.apache.log4j.Level getPriorityFor(Level level) {
        final org.apache.log4j.Level p;
        return (p = priorityMap.get(level)) == null ? org.apache.log4j.Level.DEBUG : p;
    }

    static Level getLevelFor(Priority level) {
        switch (level.toInt()) {
            case org.apache.log4j.Level.TRACE_INT:
                return org.jboss.logmanager.Level.TRACE;
            case org.apache.log4j.Level.DEBUG_INT:
                return org.jboss.logmanager.Level.DEBUG;
            case org.apache.log4j.Level.INFO_INT:
                return org.jboss.logmanager.Level.INFO;
            case org.apache.log4j.Level.WARN_INT:
                return org.jboss.logmanager.Level.WARN;
            case org.apache.log4j.Level.ERROR_INT:
                return org.jboss.logmanager.Level.ERROR;
            case org.apache.log4j.Level.FATAL_INT:
                return org.jboss.logmanager.Level.FATAL;
            default:
                return org.jboss.logmanager.Level.DEBUG;
        }
    }

    static final class Log4jJDKLevel extends org.apache.log4j.Level {

        private static final long serialVersionUID = -2456662804627419121L;

        /**
         * Instantiate a Level object.
         */
        protected Log4jJDKLevel(int level, String levelStr, int syslogEquivalent) {
            super(level, levelStr, syslogEquivalent);
        }

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#SEVERE SEVERE} level; numerically
         * equivalent to log4j's {@link org.apache.log4j.Level#ERROR ERROR} level.
         */
        public static final org.apache.log4j.Level SEVERE = new Log4jJDKLevel(org.apache.log4j.Level.ERROR_INT, "SEVERE", 3);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#WARNING WARNING} level; numerically
         * equivalent to log4j's {@link org.apache.log4j.Level#WARN WARN} level.
         */
        public static final org.apache.log4j.Level WARNING = new Log4jJDKLevel(org.apache.log4j.Level.WARN_INT, "WARNING", 4);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#INFO INFO} level; numerically
         * equivalent to log4j's {@link org.apache.log4j.Level#INFO INFO} level.
         */
        public static final org.apache.log4j.Level INFO = new Log4jJDKLevel(org.apache.log4j.Level.INFO_INT, "INFO", 5);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#CONFIG CONFIG} level; numerically
         * falls between log4j's {@link org.apache.log4j.Level#INFO INFO} and {@link org.apache.log4j.Level#DEBUG DEBUG}
         * levels.
         */
        public static final org.apache.log4j.Level CONFIG = new Log4jJDKLevel(org.apache.log4j.Level.INFO_INT - 5000, "CONFIG", 6);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#FINE FINE} level; numerically
         * equivalent to log4j's {@link org.apache.log4j.Level#DEBUG DEBUG} level.
         */
        public static final org.apache.log4j.Level FINE = new Log4jJDKLevel(org.apache.log4j.Level.DEBUG_INT, "FINE", 7);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#FINER FINER} level; numerically
         * falls between log4j's {@link org.apache.log4j.Level#DEBUG DEBUG} and {@link org.apache.log4j.Level#TRACE
         * TRACE} levels.
         */
        public static final org.apache.log4j.Level FINER = new Log4jJDKLevel(org.apache.log4j.Level.DEBUG_INT - 2500, "FINER", 7);

        /**
         * A mapping of the JDK logging {@link java.util.logging.Level#FINEST FINEST} level; numerically
         * equivalent to log4j's {@link org.apache.log4j.Level#TRACE TRACE} level.
         */
        public static final org.apache.log4j.Level FINEST = new Log4jJDKLevel(org.apache.log4j.Level.TRACE_INT, "FINEST", 7);

        private static final Map<String, org.apache.log4j.Level> levelMapping = new HashMap<String, org.apache.log4j.Level>();

        private static void add(org.apache.log4j.Level lvl) {
            levelMapping.put(lvl.toString(), lvl);
        }

        static {
            add(SEVERE);
            add(WARNING);
            add(INFO);
            add(CONFIG);
            add(FINE);
            add(FINER);
            add(FINEST);
        }

        /**
         * Get the level for the given name.  If the level is not one of the levels defined in this class,
         * this method delegates to {@link org.apache.log4j.Level#toLevel(String) toLevel(String)} on the superclass.
         *
         * @param name the level name
         *
         * @return the equivalent level
         */
        public static org.apache.log4j.Level toLevel(String name) {
            final org.apache.log4j.Level level = levelMapping.get(name.trim().toUpperCase());
            return level != null ? level : org.apache.log4j.Level.toLevel(name);
        }
    }
}
