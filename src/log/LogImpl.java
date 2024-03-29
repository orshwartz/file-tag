package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This is an implementation of a log. It's a singleton, so there
 * can be only one log in the system.
 * This log singleton implementation is thread-safe.
 * It enables writing file change events to the log, along with
 * timestamps. It also enables getting all messages sorted by timestamp
 * and clearing the log.<BR>
 * <BR>
 * The log works with a DB/Text File/XML/Log4J (TBD) TODO: Choose a method  
 * @author Or Shwartz
 */
public class LogImpl implements Log {

	private static final String LOG_NAME = "FileTag.log";
	Logger logger;
	
	/**
	 * Log constructor. Sets up required files for logging.
	 */
	public LogImpl() {

		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");

		configLog4J();
	}

	/**
	 * @throws FactoryConfigurationError
	 */
	private void configLog4J() throws FactoryConfigurationError {

		// Configure the Log4J
		DOMConfigurator.configure("Log4j.xml");
		logger = Logger.getLogger("FileTagger");
	}
	
	/**
	 * @see log.Log#clearLog()
	 */
	@Override
	public void clearLog() {
		
		// Disable the log file
		logger.getAppender("fileTag").close();
		logger.removeAppender("fileTag");
		
		// Delete the log file
		try {
			File logFile = new File(LOG_NAME);
			OutputStream os = new FileOutputStream(logFile);
			OutputStreamWriter writer = new OutputStreamWriter(os);
			writer.write("");
			writer.close();
			
			// Re-config log4j
			configLog4J();
		} catch (Exception e) {
			// TODO: Something
		}
//		
//		// Recreate the log file
//		try {
//			FileAppender appender = new FileAppender(new PatternLayout(), LOG_NAME, true);
//			appender.activateOptions();
//			logger.addAppender(appender);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * @see log.Log#getMessages()
	 */
	@Override
	public Collection<String> getMessages() {
		
		Collection<String> messages = new ArrayList<String>();
		
		// Read the log file line by line
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream(LOG_NAME);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputStreamReader logReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(logReader);
		
		String currLine;
		
		try {
			currLine = reader.readLine();
			
			while (currLine != null) {
						
				messages.add(currLine);	
				currLine = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		// Return the log messages
		return messages;
	}

	/**
	 * @see log.Log#writeMessage(log.LogEvent)
	 */
	@Override
	public void writeMessage(Object obj) {
		logger.info(obj.toString());
		
	}
}
