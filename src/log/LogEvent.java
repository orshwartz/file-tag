package log;

import java.util.Date;

/**
 * @author Or Shwartz, Itay Evron
 *
 */
public class LogEvent {

	EventType type;
	String filename;
	Date time;
	
	/**
	 * @param type
	 * @param filename
	 * @param time 
	 */
	public LogEvent(EventType type, String filename, Date time) {

		this.type = type;
		this.filename = filename;
		this.time = time;
	}

	/**
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
}
