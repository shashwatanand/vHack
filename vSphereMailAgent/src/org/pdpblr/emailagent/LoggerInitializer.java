package org.pdpblr.emailagent;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * @author Reshmi Chowdhury
 */
public class LoggerInitializer {
	private static LoggerInitializer lInitializer = new LoggerInitializer();

	/*private static Logger logger = Logger.getLogger(LoggerInitializer.class
			.getName());//This String parameter used for named susbstance,what is named substance
*/
	private static Logger logger = Logger.getLogger("org.pdpblr");
	private LoggerInitializer() {
	}

	public void intialize() {
		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("c:\\MyLogFile.log", true);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.log(Level.FINEST, "Logger is initialized successfuly");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static LoggerInitializer getLoggerInitializer() {
		return lInitializer;
	}
}
