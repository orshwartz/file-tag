package gui;

import java.io.File;
import java.io.FileFilter;

/**
 * This is a file filter that accepts only directories.
 * @author Or Shwartz
 */
class DirectoryFileFilter implements FileFilter {

	/**
	 * Accept only directories;
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File pathname) {
	
		return pathname.isDirectory();
	}
}