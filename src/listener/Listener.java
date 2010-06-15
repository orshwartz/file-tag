package listener;

import java.io.IOException;
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
	public abstract void stopListeningTo(ListenedDirectory dir);
}
