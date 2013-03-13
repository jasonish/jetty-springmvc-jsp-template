/*
 * Logback configuration in Groovy.
 * 
 * While this appears to be the Logback preference going forward you can still
 * use an XML configuration file by deleting this one and creating a 
 * logback.xml in its place.
 */


import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

def DEFAULT_PATTERN = "%d [%thread] %-5level %logger{36} - %msg%n"

appender("STDOUT", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { 
		pattern = DEFAULT_PATTERN 
	}
}

/* Make Spring less verbose. */
logger("org.springframework", ERROR)

/* Jetty can be really noisy on a shaded jar. */
logger("org.eclipse.jetty.webapp.WebAppClassLoader", INFO)
logger("org.eclipse.jetty.util.resource.JarResource", INFO)

/* Quieten Jetty in general. */
logger("org.eclipse", ERROR);

def appenders = []
appenders.add("STDOUT")

root(DEBUG, appenders)