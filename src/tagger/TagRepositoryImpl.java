/**
 * 
 */
package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementation of a tag repository, enabling various operations
 * for tagging files.
 * @author Or Shwartz
 */
public class TagRepositoryImpl implements TagRepository, Observer {

	/**
	 * @see tagger.TagRepository#addTag(java.lang.String)
	 */
	@Override
	public void addTag(String tag) throws TagAlreadyExistsException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see tagger.TagRepository#getTagListFreqOrdered()
	 */
	@Override
	public Collection<String> getTagListFreqOrdered() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see tagger.TagRepository#removeTag(java.lang.String)
	 */
	@Override
	public void removeTag(String tag) throws TagNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see tagger.TagRepository#renameTag(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see tagger.TagRepository#searchByTag(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<File> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see tagger.TagRepository#tagExists(java.lang.String)
	 */
	@Override
	public boolean tagExists(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see tagger.TagRepository#tagFile(java.io.File, java.lang.String)
	 */
	@Override
	public void tagFile(File file, String tag) throws FileNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see tagger.TagRepository#untagFile(java.io.File, java.lang.String)
	 */
	@Override
	public void untagFile(File file, String tag) throws FileNotTaggedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
