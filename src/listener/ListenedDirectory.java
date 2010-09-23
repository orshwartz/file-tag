package listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.Collection;


/**
 * This class represents a directory available for listening,
 * enabling the possibility to define recursive listening for
 * sub-folders and also define regular expressions for files
 * that should be watched.
 * @author Or Shwartz
 */
public class ListenedDirectory {

	private File directory;
	private Collection<String> regularExpressions;
	
	/**
	 * @param directory The directory for event listening.
	 * @param regularExpressions A collection of regular expressions for files
	 * that should be watched.
	 * @throws NotDirectoryException 
	 * @throws FileNotFoundException 
	 */
	public ListenedDirectory(File directory,
							 Collection<String> regularExpressions) throws 	NotDirectoryException,
							 												FileNotFoundException {
		
		// If given directory argument is not really a directory
		if (!directory.isDirectory())
		{
			// Throw exception - we must have a directory
			throw new NotDirectoryException(directory.getAbsolutePath());
		}
		
		// Else, if given directory doesn't exist
		else if (!directory.exists())
		{
			// Throw Exception - the directory must exist in order to be
			// listened
			throw new FileNotFoundException();
		}
		
		this.directory = directory;
		this.regularExpressions = regularExpressions;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @return the regularExpressions
	 */
	public Collection<String> getRegularExpressions() {
		return regularExpressions;
	}
}
