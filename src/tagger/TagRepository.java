package tagger;

import java.io.File;
import java.util.Collection;

/**
 * @author Or Shwartz
 *
 */
public interface TagRepository {
	
	public boolean tagExists(String tag);
	public void removeTag(String tag) throws TagNotFoundException;
	public void addTag(String tag) throws TagAlreadyExistsException;
	public boolean tagFile(File file, String tag);
	public boolean untagFile(File file, String tag);
	public void renameTag(String oldName, String newName) throws TagNotFoundException;
	public Collection<File> searchByTag(Collection<String> includedTags,
										Collection<String> excludedTags);
	public Collection<String> getTagListFreqOrdered();
}
