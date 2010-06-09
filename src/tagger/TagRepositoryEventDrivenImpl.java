/**
 * 
 */
package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Observable;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz
 */
public class TagRepositoryEventDrivenImpl extends TagRepositoryEventDriven {

	/**
	 * 
	 */
	public TagRepositoryEventDrivenImpl() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass().getName() + " up.");
	}

	@Override
	public void addTag(String tag) throws TagAlreadyExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<String> getTagListFreqOrdered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTag(String tag) throws TagNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<File> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tagExists(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tagFile(File file, String tag) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void untagFile(File file, String tag) throws FileNotTaggedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
