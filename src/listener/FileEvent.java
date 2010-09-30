package listener;

import java.nio.file.Path;

/**
 * This class represents an event for a file -
 * modified, created or deleted.
 * @author Or Shwartz
 */
public class FileEvent {
	
	Path file;
	FileEvents event;
	
	/**
	 * Constructor for <CODE>FileEvent</CODE>.
	 * @param file relevant to the event.
	 * @param event that happened.
	 */
	public FileEvent(Path file, FileEvents event) {
		this.file = file;
		this.event = event;
	}

	/**
	 * @return the file
	 */
	public Path getFile() {
		return file;
	}

	/**
	 * @return the event
	 */
	public FileEvents getEvent() {
		return event;
	}
}
