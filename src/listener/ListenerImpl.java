package listener;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKind.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKind.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKind.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKind.OVERFLOW;
import static listener.FileEvents.CREATED;
import static listener.FileEvents.DELETED;
import static listener.FileEvents.MODIFIED;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileRef;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.Attributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import log.EventType;

/**
 * This class represents an object capable of listening to filesystem events on
 * pre-specified folders and notify about changes to files.
 * 
 * @author Or Shwartz, Itay Evron
 */
public class ListenerImpl extends Listener {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private Map<File, Collection<String>> listenedPaths;	// TODO: Consider a possible optimization by saving a PathMatcher object rather than a String - will save the need to use getPathMatcher each time
	private final Map<Kind<?>, FileEvents> fileEventsMap;
	Thread watchThread = null;
	private boolean trace = false;
	private AtomicBoolean listenerActive = new AtomicBoolean(false);
	private final String FILENAME_PERSISTENCE = "listener_persistence.bin";
	private boolean listenerClosed = false;

	/**
	 * Constructor - Sets up the listener.
	 * @throws IOException if error occurred during creation of file system watcher.
	 */
	public ListenerImpl() throws IOException {

		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");

		// Create a new watch service for listening to directory changes
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		
		try {
			// Load previous listened directories data
			loadListenerData();
			
			// For each listened path
			for (Entry<File, Collection<String>> curPath :
				 listenedPaths.entrySet()) {
				
				// Listen to the restored paths
				listenTo(new ListenedDirectory(curPath.getKey(),
											   curPath.getValue()));
			}
		} catch (FileNotFoundException e) {
			
			// Create a new hash map for listened directories and
			// their regular expressions 
			this.listenedPaths = new HashMap<File, Collection<String>>();
		}
		
		// Set up mapping between events
		fileEventsMap = new HashMap<Kind<?>, FileEvents>(FileEvents.values().length);
		fileEventsMap.put(ENTRY_MODIFY, MODIFIED);
		fileEventsMap.put(ENTRY_CREATE, CREATED);
		fileEventsMap.put(ENTRY_DELETE, DELETED);
	}

	/**
	 * This method causes the listener to start
	 * monitor changes to listened directories. 
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
		else {
			// Signal thread to resume work
			listenerActive.set(true);
			synchronized (watchThread) {
				
				watchThread.notify();
			} 
		}
	}

	/**
	 * This method pauses the system monitoring for file changes in requested
	 * directories.
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
	 * Listens to file changes in given directory, using files matching any
	 * of the supplied regular expressions. If the given directory is already
	 * being listen to, the regular expression file filters are simply updated
	 * to the new given filters.
	 * @throws IOException If an I/O error occurs when attempting to register
	 * a directory for listening.
	 * @see listener.Listener#listenTo(listener.ListenedDirectory)
	 */
	@Override
	public void listenTo(ListenedDirectory dir) throws IOException {

		if (!listenedPaths.containsKey(dir.getDirectory())) {
			// TODO: I don't remember why this condition is here with no code.
			// Try to remember.
		}
		
		WatchKey key =
			dir.getDirectory().toPath().register(watcher,
												 ENTRY_CREATE,
												 ENTRY_DELETE,
												 ENTRY_MODIFY);
		
		// Save path to monitored directories map and their regular expressions
		listenedPaths.put(dir.getDirectory(), dir.getRegularExpressions());
		
		// If anything had already been registered
		if (trace) {

			FileRef previousRef = keys.get(key);

			// If path not registered before
			if (previousRef == null) {

				// TODO: Instead of this... maybe write to log
				System.out.println("obs: "+ countObservers());
				setChanged();
				notifyObservers(new FileEvent( dir.getDirectory().toPath(),
					FileEvents.CREATED));
				System.out.format("register: %s\n", dir.getDirectory().toString());
			}
			else if (!dir.getDirectory().toPath().equals(previousRef)) {

				// TODO: Instead of this... maybe write to log
				System.out.format("update: %s -> %s\n", previousRef, dir.getDirectory().toString());
			}
		}

		// Save monitoring key
		keys.put(key, dir.getDirectory().toPath());

		// Enable trace after initial registration
		this.trace = true;
		
		// Save new listened paths for next time
		saveListenerData();
	}

	/**
	 * @see listener.Listener#stopListeningTo(File)
	 */
	@Override
	public void stopListeningTo(File pathToMute) {
			
		// Remove path from path->RegEx map
		listenedPaths.remove(pathToMute);
		
		// Scan key-map to search for path that should be removed 
		for (Map.Entry<WatchKey, Path> curEntry : keys.entrySet()) {
			
			// If the path is found
			if (curEntry.getValue().equals(pathToMute.toPath())) {

				WatchKey curKey = curEntry.getKey();
				
				// "Mute" directory, remove from watch keys map 
				// and stop searching (TODO: Mute inner directories too? Probably will happen by itself upon deletion.)
				curKey.cancel();
				keys.remove(curKey);
				break;
			}
		}
		
		// Save new listened paths for next time
		saveListenerData();
	}

	// TODO: Add init method or something like... maybe in the ctor directly
	
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        
    	// Register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir) {
                try {
                	 // TODO: listen to inner directories recursively,
            		 // and use parent's regex for them (I think...).
                    listenTo(new ListenedDirectory(new File(dir.toString()),
                    							   listenedPaths.get(new File(dir.getParent().toString()))));
                } catch (IOException x) {
                	// TODO: Handle this exception?
                    throw new IOError(x);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
	
	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * This class is a thread for processing file events for
	 * watched directories. This needs to be a thread because waiting
	 * for file events is a blocking operation. 
	 * @author Or Shwartz
	 */
	public class ListenerThread extends Thread {

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			// Run until listener closed
			while (!listenerClosed) {

				// Check if should wait
				synchronized (this) {
					
					// While requested to pause
					while (!listenerActive.get()) {
						try {
						
							// Pause this thread and wait for someone to wake it up
							wait();
						} catch (Exception e) {
							
							// TODO: Handle this exception?
						}
					}
				} 				
				
				// Wait for key to be signaled
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					return;
				} catch (ClosedWatchServiceException e) {
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
					
					WatchEvent.Kind<?> kind = event.kind();

					// TODO: Check how OVERFLOW event is handled
					if (kind == OVERFLOW) {
						
						System.out.println("OVERFLOW occurred."); // TODO: Delete this line
						continue;
					}

					// Context for directory entry event is the file name of
					// entry
					WatchEvent<Path> ev = cast(event);
					Path name = ev.context();
					Path child = dir.resolve(name);
					
//					SimpleDateFormat timeFormat =
//						new SimpleDateFormat("HH:mm:ss:SSS");
//					Date now = new Date();
//					String.format(SimpleDateFormat.getDateInstance().format(now) + " " +
//							  SimpleDateFormat.getTimeInstance().format(now) +
//							  " %s: %s",
//							  kind.name(),
//							  child);
					System.out.println(kind + "\t" + child); // TODO: Remove this line
					
					try {
						// If directory event occurred
						if (Attributes.readBasicFileAttributes(child, NOFOLLOW_LINKS).isDirectory()) {					
						
							// If directory is created then register it and its sub-directories
							if (kind == ENTRY_CREATE) {
								// Listen to all sub-folders of new folder
								registerAll(child);
							}
							else if (kind == ENTRY_MODIFY) {
								// TODO: Do something?
							}
						}
						
						// Else, a file event occurred
						else if (Attributes.readBasicFileAttributes(child, NOFOLLOW_LINKS).isRegularFile()) {
							
							// If file matches a regular expression mask for its containing path
							if (checkRegex(child,
										   listenedPaths.get(new File(child.getParent().toString())))) {
								
								// Notify observers of file changes
								setChanged();
								notifyObservers(new FileEvent(child,
													  		  fileEventsMap.get(kind)));
							}
						}
					} catch (NoSuchFileException e) {
						
						// The exception was probably thrown when an element was deleted 
						// and then we attempted to read the element's attributes - so make sure
						if (kind == ENTRY_DELETE) {

							// If element is a directory we're listening to
							if (listenedPaths.containsKey(new File(child.toString()))) {
								
								// Stop listening to this folder
								stopListeningTo(new File(child.toAbsolutePath().toString()));
							}
							
							// Else, element might be a deleted file
							else {
								
								// Notify observers of file changes
								setChanged();
								notifyObservers(new FileEvent(child,
													  		  fileEventsMap.get(kind)));								
							}
						}
					} catch (IOException e) {

						// Ignore to keep sample readable TODO: Really?! Ignore?
						e.printStackTrace();
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
	
	/**
	 * This method checks whether a filename matches any of the given regular expressions.
	 * @param fileName which should be matched.
	 * @param regularExpressions to check against.
	 * @return True is returned if the filename matches at least one of the regular expressions. False,
	 * if it matches none of the regular expressions. 
	 */
	private boolean checkRegex(Path fileName, Collection<String> regularExpressions) {
		
		PathMatcher matcher = null;
		
		// If any parameter is null TODO: Check might be removable
		if (fileName == null || regularExpressions == null) {
			
			// Return false - nothing to check TODO: Remove debug print
			System.out.println("fileName = " + fileName + "\tNOT MATCHING\t regularExpressions = " + regularExpressions);
			return false;
		}
		
		// For each regular expression
		for (String curRegEx : regularExpressions) {

			// Get matcher for current regular expression
			matcher = FileSystems.getDefault().getPathMatcher("regex:" + curRegEx);
			
			// If there's a match to the current regular expression
			if (matcher.matches(fileName)) {
				
				// Return true - a match is found
				return true;
			}
		}
		
		// No match was found - return false
		return false;
	}
	
	/**
	 * Load listened directories and matching regular expressions from file. 
	 * @throws FileNotFoundException if persistence file doesn't exist.
	 */
	@SuppressWarnings("unchecked")
	private void loadListenerData() throws FileNotFoundException  {

		File filePersistence =
			new File(FILENAME_PERSISTENCE);
		
		FileInputStream fileInStream =
			new FileInputStream(filePersistence);

		try {
			ObjectInputStream objInStream =
				new ObjectInputStream(fileInStream);

			// Read listened paths data from file
			listenedPaths =
				(Map<File, Collection<String>>)objInStream.readObject();
			objInStream.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save listened directories and matching regular expressions to file.
	 */
	private void saveListenerData() {

		try {
			FileOutputStream fileOutStream =
				new FileOutputStream(FILENAME_PERSISTENCE);
		
			ObjectOutputStream objOutStream =
				new ObjectOutputStream(fileOutStream);
			
			// Save listened paths data to file
			objOutStream.writeObject(listenedPaths);
			objOutStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method counts the number of files currently
	 * in listened directories.
	 * @return the number of files in listened directories.
	 */
	private int countListenedFiles() {
		
		int fileCount = 0;
		Set<Entry<File, Collection<String>>> listenedPaths =
			this.listenedPaths.entrySet();
		
		// For each listened path XXX: Maybe there's a faster method (using newDirectoryStream of Path?)
		for (final Entry<File, Collection<String>> curPath : listenedPaths) {
			
			// Add file count of current directory (only files matching regular expressions) 
			fileCount += curPath.getKey().list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {

					return checkRegex(new File(dir.getAbsoluteFile() +
											   FileSystems.getDefault().getSeparator() +
											   name).toPath(),
							   		  curPath.getValue());
				}				
			}).length;
		}
		
		return fileCount;
	}
	
	/**
	 * Use this method when done using the listener.
	 * 
	 * Do not restart the listener afterwards.
	 */
	@Override
	public void close() {

		// Stop the listener thread
		deactivate();
		
		// Save listener data
		saveListenerData();
		
		// Set thread as closed to stop processing
		listenerClosed = true;
		
		try {
			// Close the watch service TODO: Remove message
			System.out.println("Closing the watch service...");
			watcher.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets a copy of the listened directories and the regular
	 * expression filters for the files in these directories.
	 * 
	 * This is a "heavy" operation and its use should be minimized.
	 * 
	 * The returned construct is optimized for read-only use.
	 * 
	 * @return the listenedPaths
	 */
	public Map<File, ListenedDirectory> getListenedPaths() {
		
		Map<File, ListenedDirectory> returnedPaths =
			new HashMap<File, ListenedDirectory>(listenedPaths.size(), 1.0f);
		File curPath = null;
		Collection<String> curReturnedRegexes = null;
		Collection<String> curRegexes = null;
		
		// For each entry
		for (Entry<File, Collection<String>> curEntry :
			 listenedPaths.entrySet()) {
			
			// Clone the key
			curPath = new File(curEntry.getKey().getAbsolutePath());
			 
			// Clone the regular expressions 
			curReturnedRegexes = new ArrayList<String>(curEntry.getValue().size());
			curRegexes = curEntry.getValue();
			for (String curRegex : curRegexes) {
				
				curReturnedRegexes.add(curRegex);
			}
			
			// Enter current entry to returned map
			try {
				returnedPaths.put(curPath,
								  new ListenedDirectory(curPath,
										  				curRegexes));
			} catch (Exception e) {
				// Ignore
			}
		}
		
		// Returned the cloned structure
		return returnedPaths;
	}
}
