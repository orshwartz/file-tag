/**
 * 
 */
package tagger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Observable;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz
 */
public class TagRepositoryEventDrivenImpl extends TagRepositoryEventDriven {

	private DataAccessLevel DAL = null;
    private final static Locale dfltLocal = Locale.getDefault();
	
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
	public Collection<String> getTagListFreqOrdered() {

		// TODO Auto-generated method stub
		return null;
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
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void dropTables(){
		DAL.dropTables();
	}
}
