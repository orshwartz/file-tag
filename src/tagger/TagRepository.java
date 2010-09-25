package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import tagger.autotagger.AutoTagger;

/**
 * This interface represents required methods from a Tag Repository,
 * enabling the tagging of files, removing tags, creating tags, getting
 * files by tags, etc...
 * @author Or Shwartz
 */
public interface TagRepository {
	
	public boolean tagExists(String tag);
	public void removeTag(String tag) throws TagNotFoundException;
	public void addTags(Collection<String> tags) throws TagAlreadyExistsException;
	public void tagFile(String file, Collection<String> tags) throws FileNotFoundException;
	public void untagFile(String file, String tag) throws FileNotTaggedException;
	public void renameTag(String oldName, String newName) throws TagNotFoundException, TagAlreadyExistsException;
	public void deleteAll();
	public Collection<String> getTagsOfFile(String file);
	public Collection<String> searchByTag(Collection<String> includedTags,
										Collection<String> excludedTags);
	public Collection<TagFreq> getTagListFreqOrdered();
	
	void setAutoTaggers(Collection<AutoTagger> autoTaggers);
	
	public void dropTables();
	
	public void removeFile(String file); // TODO : delete after we're done
	public void unTagFileAll(String file);
}