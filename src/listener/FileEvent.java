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
	
	public String toString(){
		
		switch(event){
			case CREATED :
				return new String("The file " + file.toAbsolutePath() + 
						" was created in the directory " + 
						file.getParent());
				
			case NEW_DIRECTORY :
				return new String("Register: " + file.toAbsolutePath());
				
			case DELETED:
			case MODIFIED:
				return getFile() + " " + event.name().toLowerCase() + ".";

			default:
				return event.name();
		}	
	}
}
