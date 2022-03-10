package edu.usfca.cs272;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generates several log messages to test Log4j2 setup.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2022
 */
public class LoggerSetup {
	// TODO See the README file for what to do for this homework

	/**
	 * Outputs all levels of messages to a logger.
	 *
	 * @param log the logger to use
	 */
	public static void outputMessages(Logger log) {
		log.trace("Toucan");
		log.debug("Dove");
		log.info("Ibis");
		log.warn("Wren");
		log.error("Eastern", new Exception("Eagle"));
		log.catching(Level.FATAL, new RuntimeException("Finch"));
	}

	/**
	 * Generates several log messages to two different loggers.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		System.out.println("Class Logger:");
		outputMessages(LogManager.getLogger());
		System.out.println();

		System.out.println("Root Logger:");
		outputMessages(LogManager.getRootLogger());
	}
}
