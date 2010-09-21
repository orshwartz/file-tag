package listener;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;

/**
 * Abstract class for a filesystem listener, watching for changes.
 * @see java.util.Observable
 * @author Or Shwartz, Itay Evron
 */
public abstract class Listener extends Observable {

	/**
	 * Start listening to directory changes.
	 */
	public abstract void activate();

	/**
	 * Stop the listening to directory changes. 
	 */
	public abstract void deactivate();
	
	
	/**
	 * @return True if the listener is active. False is returned otherwise.
	 */
	public abstract boolean isActive();
	

	/**
	 * @param dir Directory for file-change-monitoring.
	 * @throws IOException If an I/O error occurs (<code>dir</code> is not a directory
	 * of doesn't exist.
	 */
	public abstract void listenTo(ListenedDirectory dir) throws IOException;
	
	
	/**
	 * @param dir Directory to remove from listener.
	 */
	public abstract void stopListeningTo(File dir);

	/**
	 * Gets a copy of the listened directories and the regular
	 * expression filters for the files in these directories.
	 * 
	 * @return the listenedPaths
	 */
	public abstract Map<File, ListenedDirectory> getListenedPaths();
	
	/**
	 * Performs listener finalization actions. Should be called when
	 * the listener is no longer needed.
	 */
	public abstract void close();
}
