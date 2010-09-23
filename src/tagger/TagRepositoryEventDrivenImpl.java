/**
 * 
 */
package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Observable;
import java.util.TreeSet;

import listener.FileEvent;
import tagger.autotagger.AutoTagger;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz
 */
public class TagRepositoryEventDrivenImpl extends TagRepositoryEventDriven {

	private DataAccessLevel DAL = null;
    private final static Locale dfltLocal = Locale.getDefault();
    private Collection<AutoTagger> autoTaggers = new ArrayList<AutoTagger>();
	
	/**
	 * <CODE>TagRepositoryEventDrivenImpl</CODE> constructor.
	 */
	public TagRepositoryEventDrivenImpl() {
		// TODO Auto-generated constructor stub
		
		DAL = new DataAccessLevel();
		DAL.getConnection();
		
		System.out.println(this.getClass().getName() + " up.");
	}

	/**
	 * This method fixes a string to be used as a tag in the repository.
	 * @param string to fix.
	 * @return The string lowercased and without leading and trailing
	 * whitespace.
	 */
	private String fixString(String string) {
		
		// Change to lowercase and remove leading and trailing whitespace
		return string.trim().toLowerCase(dfltLocal);	
	}
	
	/**
	 * This method receives a collection of strings and adds them
	 * to the repository.
	 * @see tagger.TagRepository#addTags(java.util.Collection)
	 */
	@Override
	public void addTags(Collection<String> tags) throws TagAlreadyExistsException {
		// TODO Auto-generated method stub
		
		Collection<String> fixedTags = new ArrayList<String>(tags.size());
		
		// For each tag string
		for (String curTag : tags)
		{
			// Fix current string and add to list
			fixedTags.add(fixString(curTag));
		}
		
		// Add fixed strings to database
		DAL.addTags(fixedTags);
	}

	/**
	 * This method returns all the tags, ordered by frequency - most frequent first.
	 * @see tagger.TagRepository#getTagListFreqOrdered()
	 */
	@Override
	public Collection<TagFreq> getTagListFreqOrdered() {

		return DAL.getTagListFreqOrdered();
	}

	/**
	 * This method removes a tag from the repository.
	 * TODO: Say what happens if tag is associated to files or doesn't exist.
	 * @see tagger.TagRepository#removeTag(java.lang.String)
	 */
	@Override
	public void removeTag(String tag) throws TagNotFoundException {

		DAL.removeTag(tag);
	}

	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException, TagAlreadyExistsException {
		DAL.renameTag(oldName, newName);
	}

	@Override
	public Collection<String> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		
		// TODO Auto-generated method stub
		return DAL.searchByTag(includedTags, excludedTags);
	}

	@Override
	public boolean tagExists(String tag) {
		// TODO Auto-generated method stub
		return DAL.tagExists(tag);
	}

	@Override
	public void tagFile(String file, Collection<String> tags) throws FileNotFoundException {
		DAL.tagFile(file, tags);
		
	}

	@Override
	public void untagFile(String file, String tag) throws FileNotTaggedException {
		DAL.untagFile(file, tag);
		
	}
	
	@Override
	public void unTagFileAll(String file){
		DAL.unTagFileAll(file);
	}

	/**
	 * Update this Observer with file events. The argument received should be a <CODE>{@link FileEvent}</CODE> object.
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
		FileEvent event = (FileEvent)arg;
		Path file = ((FileEvent)arg).getFile();
		
		switch (event.getEvent()) {
			case MODIFIED:

				// Remove tags for file (and re-tag it using fall-thru to file creation event)
				DAL.unTagFileAll(file.toString());

			case CREATED:

				Collection<String> fileTags = new TreeSet<String>(); // XXX: Consider a different data structure if tagging is slow

				// For each tagging algorithm
				for (AutoTagger curAutoTagger : autoTaggers) {
					
					try {
						// Get tags for file
						Collection<String> tagsForFile =
							curAutoTagger.autoTag(new File(file.toString()));

						// Perform safety check on returned result
						if (tagsForFile != null) {
						
							// Add automatically generated tags of file to collection
							fileTags.addAll(tagsForFile); // XXX: Consider using just a path or just a file, if tagging is slow
						}
					} catch (Exception e) {

						// Problem with tagger, continue to next one
						continue; // TODO: Consider attempting to report the problem somehow
					}
				}
				
				// Tag the file with generated tags
				DAL.tagFile(file.toString(), fileTags);
				
				break;
			case DELETED:

				// The file was deleted, so delete it
				DAL.removeFile(file.toString());
				
				break;
		}
		
	}
	
	/**
	 * Sets the automatic tagging to use the given automatic taggers.
	 * @param autoTaggers Automatic taggers to use.
	 */
	@Override
	public void setAutoTaggers(Collection<AutoTagger> autoTaggers) {
		
		// Set automatic taggers to use
		this.autoTaggers = autoTaggers;
	}
	
	public void dropTables(){
		DAL.dropTables();
	}	
	
	/* delete after we're done */	
	public void removeFile(String file){
		//TODO : delete after we're done
		DAL.removeFile(file);
	}
	
}
