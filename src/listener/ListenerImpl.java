/**
 * 
 */
package listener;

import static java.nio.file.StandardWatchEventKind.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

/**
 * This class represents an object capable of listening to filesystem
 * events on pre-specified folders and notify about changes to files.
 * @author Or Shwartz, Itay Evron
 */
public class ListenerImpl extends Listener {

	private WatchService watcher;
	
	/**
	 * @throws IOException
	 */
	public ListenerImpl() throws IOException {
	
		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");
		
		// Create a new watch service for listening to directory changes 
		this.watcher = FileSystems.getDefault().newWatchService();
	}
	
	/**
	 * @see listener.Listener#activate()
	 */
	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see listener.Listener#deactivate()
	 */
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see listener.Listener#isActive()
	 */
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see listener.Listener#listenTo(listener.ListenedDirectory)
	 */
	@Override
	public void listenTo(ListenedDirectory dir) throws IOException {
		// TODO Auto-generated method stub
		
		dir.getDirectory().toPath().register(watcher,
											 ENTRY_CREATE,
											 ENTRY_DELETE,
											 ENTRY_MODIFY);
	}

	/**
	 * @see listener.Listener#stopListeningTo(listener.ListenedDirectory)
	 */
	@Override
	public void stopListeningTo(ListenedDirectory dir) {
		// TODO Auto-generated method stub
		
	}
}
