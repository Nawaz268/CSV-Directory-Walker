import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimpleLogging {

	public static void main(String[] args) {

		Handler consoleHandler = null;

		Handler fileHandler = null;
		Formatter simpleFormatter = null;

		Logger logger = Logger.getLogger("Main");

		final long startTime = System.currentTimeMillis();

		// Creating consoleHandler and fileHandler
		consoleHandler = new ConsoleHandler();
		try {
			fileHandler = new FileHandler("./sampleLogfile.log");
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Assigning handlers to LOGGER object
		logger.addHandler(consoleHandler);
		logger.addHandler(fileHandler);
		// Setting levels to handlers and LOGGER
		consoleHandler.setLevel(Level.ALL);
		fileHandler.setLevel(Level.FINE);
		logger.setLevel(Level.ALL);
		
		simpleFormatter = new SimpleFormatter();
		
		// Setting formatter to the handler
		fileHandler.setFormatter(simpleFormatter);

		logger.config("Configuration done.");

		for (int i = 0; i < 50; i++) {
			logger.log(Level.FINE, "This is a fine log message " + i);
		}
		for (int i = 0; i < 50; i++) {
			logger.log(Level.INFO, "This is a info log message " + i);
		}
		for (int i = 0; i < 50; i++) {
			logger.log(Level.WARNING, "This is a warning log message " + i);
		}
		for (int i = 0; i < 50; i++) {
			logger.log(Level.SEVERE, "This is a sever log message " + i);
		}		
		
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) + " ms");

	}

}
