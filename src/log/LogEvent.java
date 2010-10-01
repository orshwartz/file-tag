package log;

import java.util.Date;

/**
 * This class represents a file tagging system logged event.
 * @author Or Shwartz, Itay Evron
 */
public class LogEvent implements Comparable<LogEvent> {

	EventType type;
	String filename;
	Date time;
	
	/**
	 * Constructor for LogEvent class.
	 * @param type of event.
	 * @param filename of relevant file, or pathname if path event.
	 * @param time of logging.
	 */
	public LogEvent(EventType type, String filename, Date time) {

		this.type = type;
		this.filename = filename;
		this.time = time;
	}

	/**
	 * Event type getter.
	 * @return the type of the event.
	 * @see EventType
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Filename getter.
	 * @return the filename of the relevant event. Can be pathname
	 * if path event.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Log time getter.
	 * @return the time logged for relevant event.
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Compare the events by using the Date comparison method.
	 * @see java.util.Date#compareTo(Date)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(LogEvent otherEvent) {

		return getTime().compareTo(otherEvent.getTime());
	}
}
