/**
 * 
 */
package listener;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKind.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKind.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKind.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKind.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileRef;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.Attributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import sun.util.logging.resources.logging;

/**
 * This class represents an object capable of listening to filesystem events on
 * pre-specified folders and notify about changes to files.
 * 
 * @author Or Shwartz, Itay Evron
 */
public class ListenerImpl extends Listener {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	Thread watchThread = null;
	private boolean trace = false;
	private AtomicBoolean listenerActive = new AtomicBoolean(false);

	/**
	 * @throws IOException
	 */
	public ListenerImpl() throws IOException {

		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");

		// Create a new watch service for listening to directory changes
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
	}

	/**
	 * @see listener.Listener#activate()
	 */
	@Override
	public void activate() {
		
		// If thread not created before
		if (watchThread == null) {
		
			// Create the processing thread
			watchThread = new ListenerThread();
			
			// Start the thread (process directory listening)
			listenerActive.set(true);
			watchThread.start();
		}
		
		// Else, thread exists - resume is requested
		else
		{
			// Signal thread to resume work
			listenerActive.set(true);
			synchronized (watchThread) {
				
				watchThread.notify();
			} 
		}
	}

	/**
	 * @see listener.Listener#deactivate()
	 */
	@Override
	public void deactivate() {

		// Signal the processing thread it should pause 
		listenerActive.set(false);
	}

	/**
	 * @see listener.Listener#isActive()
	 */
	@Override
	public boolean isActive() {

		return listenerActive.get();
	}

	/**
	 * @throws IOException 
	 * @see listener.Listener#listenTo(listener.ListenedDirectory)
	 */
	@Override
	public void listenTo(ListenedDirectory dir) throws IOException {
		// TODO Auto-generated method stub

		WatchKey key =
			dir.getDirectory().toPath().register(watcher,
												 ENTRY_CREATE,
												 ENTRY_DELETE,
												 ENTRY_MODIFY);
		
		// If anything had already been registered
		if (trace) {

			FileRef previousRef = keys.get(key);

			// If path not registered before
			if (previousRef == null) {

				// TODO: Instead of this... probably write to log
				System.out.format("register: %s\n", dir.getDirectory().toString());
			}
 
			else if (!dir.getDirectory().toPath().equals(previousRef)) {

				System.out.format("update: %s -> %s\n", previousRef, dir);
			}
		}

		keys.put(key, dir.getDirectory().toPath());

		// Enable trace after initial registration
		this.trace = true;
	}

	/**
	 * @see listener.Listener#stopListeningTo(listener.ListenedDirectory)
	 */
	@Override
	public void stopListeningTo(ListenedDirectory dir) {
		// TODO Auto-generated method stub
	}

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir) {
//                try {
//                	 // TODO: listen to inner directories recursively,
//            		 // and use parent's regex for them (I think...).
//                    listenTo(dir);
//                } catch (IOException x) {
//                    throw new IOError(x);
//                }
                return FileVisitResult.CONTINUE;
            }
        });
    }	
	
	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * @author Or Shwartz
	 */
	public class ListenerThread extends Thread {

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			// TODO: Use a flag to enable pausing the thread in an orderly manner
			// Run "forever"
			while (true) {

				// Check if should wait
				synchronized (this) {
					
					// While requested to pause
					while (!listenerActive.get()) {
						try {
						
							// Pause this thread and wait for someone to wake it up
							wait();
						} catch (Exception e) {
						}
					}
				} 				
				
				// Wait for key to be signaled
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					return;
				}

				Path dir = keys.get(key);
				if (dir == null) {
					// TODO: Printing is not discouraged... think this through.
					System.err.println("WatchKey not recognized!!");
					continue;
				}

				// For each event generated by watcher
				for (WatchEvent<?> event : key.pollEvents()) {
					
					WatchEvent.Kind kind = event.kind();

					// TODO: Check how OVERFLOW event is handled
					if (kind == OVERFLOW) {
						
						continue;
					}

					// Context for directory entry event is the file name of
					// entry
					WatchEvent<Path> ev = cast(event);
					Path name = ev.context();
					Path child = dir.resolve(name);
					
					// TODO: Send something useful to tagger
					setChanged();
					SimpleDateFormat timeFormat =
						new SimpleDateFormat("HH:mm:ss:SSS");
					Date now = new Date();
					notifyObservers(String.format(SimpleDateFormat.getDateInstance().format(now) + " " +
												  SimpleDateFormat.getTimeInstance().format(now) +
												  " %s: %s",
												  event.kind().name(),
												  child));

					System.out.println(kind + "\t" + child); // TODO: Remove this line
					
					// If directory is created, and watching recursively, then
					// register it and its sub-directories
					if (/* TODO: Was "recursive &&" but I removed it */kind == ENTRY_CREATE) {
						try {
							if (Attributes.readBasicFileAttributes(child,
																   NOFOLLOW_LINKS).isDirectory()) {
								registerAll(child);
							}
						} catch (IOException x) {
							
							// Ignore to keep sample readable
						}
					}
				}

				// Reset key and remove from set if directory no longer
				// accessible
				boolean valid = key.reset();
				
				if (!valid) {
					
					keys.remove(key);

					// All directories are inaccessible
					if (keys.isEmpty()) {
					
						break;
					}
				}
			}
		}
	}
}
