package gui;

import java.io.File;
import java.util.Collection;

import listener.ListenedDirectory;

/**
 * This class may be used to represent changes to listened paths:
 * <LI>Paths added for listening</LI>
 * <LI>Paths removed from listening</LI>
 * <LI>Paths already listened, with file filter changes</LI>
 * 
 * @author Or Shwartz
 */
public class ListenedPathsDelta {
	
	private Collection<ListenedDirectory> addedListenedDirectories;
	private Collection<File> removedListenedDirectories;
	private Collection<ListenedDirectory> modifiedListenedDirectories;
	
	/**
	 * Constructor for the delta object.
	 * @param addedListenedDirectories
	 * @param removedListenedDirectories
	 * @param modifiedListenedDirectories
	 */
	public ListenedPathsDelta(
			Collection<ListenedDirectory> addedListenedDirectories,
			Collection<File> removedListenedDirectories,
			Collection<ListenedDirectory> modifiedListenedDirectories) {
		
		this.addedListenedDirectories = addedListenedDirectories;
		this.removedListenedDirectories = removedListenedDirectories;
		this.modifiedListenedDirectories = modifiedListenedDirectories;
	}

	/**
	 * Gets the new directories for listening, not being listened to currently. 
	 * @return The listened directories and their regular expression file filters.
	 */
	public Collection<ListenedDirectory> getAddedListenedDirectories() {
	
		return addedListenedDirectories;
	}
	
	/**
	 * Gets the directories being listened to currently, which should not be listened
	 * to anymore. 
	 * @return The listened directories to be "muted".
	 */
	public Collection<File> getRemovedListenedDirectories() {
		
		return removedListenedDirectories;
	}
	
	/**
	 * Gets the directories being listened to currently, which have different file
	 * filters now. 
	 * @return The listened directories and their new regular expression file filters (all filters are
	 * returned and not just the delta).
	 */
	public Collection<ListenedDirectory> getModifiedListenedDirectories() {
	
		return modifiedListenedDirectories;
	} 
}
