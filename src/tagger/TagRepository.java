package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * This interface represents required methods from a Tag Repository,
 * enabling the tagging of files, removing tags, creating tags, getting
 * files by tags, etc...
 * @author Or Shwartz
 */
public interface TagRepository {
	
	public boolean tagExists(String tag);
	public void removeTag(String tag) throws TagNotFoundException;
	public void addTag(Collection<String> tags) throws TagAlreadyExistsException;
	public void tagFile(String file, Collection<String> tags) throws FileNotFoundException;
	public void untagFile(File file, String tag) throws FileNotTaggedException;
	public void renameTag(String oldName, String newName) throws TagNotFoundException, TagAlreadyExistsException;
	public Collection<File> searchByTag(Collection<String> includedTags,
										Collection<String> excludedTags);
	public Collection<String> getTagListFreqOrdered();
}
