package log;

import java.util.SortedSet;

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

	/**
	 * 
	 */
	private LogImpl() {

		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");
	}
	
	/**
	 * LogHolder is loaded on the first execution of
	 * LogImpl.getInstance() or the first access to LogHolder.INSTANCE,
	 * not before.
	 */
	private static class LogHolder {
		
		private static final LogImpl INSTANCE = new LogImpl();
	}

	/**
	 * @return
	 */
	public static LogImpl getInstance() {

		return LogHolder.INSTANCE;
	}

	
	/**
	 * @see log.Log#clearLog()
	 */
	@Override
	public void clearLog() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see log.Log#getMessages()
	 */
	@Override
	public SortedSet<LogEvent> getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see log.Log#writeMessage(log.LogEvent)
	 */
	@Override
	public void writeMessage(LogEvent event) {
		// TODO Auto-generated method stub

	}

}
