package listener;

import java.io.File;
import java.util.Collection;


/**
 * @author Or Shwartz
 *
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
}
