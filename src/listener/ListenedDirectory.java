package listener;

import java.io.File;
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
	private boolean recursive;
	private Collection<String> regularExpressions;
	
	/**
	 * @param directory
	 * @param recursive
	 * @param regularExpressions
	 */
	public ListenedDirectory(File directory,
							 boolean recursive,
							 Collection<String> regularExpressions) {
		this.directory = directory;
		this.recursive = recursive;
		this.regularExpressions = regularExpressions;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * @return the regularExpressions
	 */
	public Collection<String> getRegularExpressions() {
		return regularExpressions;
	}
}
