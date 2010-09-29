package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;

import listener.FileEvent;

import tagger.autotagger.AutoTagger;

/**
 * This interface represents required methods from a Tag Repository,
 * enabling the tagging of files, removing tags, creating tags, getting
 * files by tags, etc...
 * @author Or Shwartz
 */
public interface TagRepository {

	public void processFileChangeTagging(FileEvent fileEvent);
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
	
	/**
	 * Sets the requested algorithms by which files should be tagged.
	 * @param autoTaggersToSet The algorithms to use for tagging.
	 */
	public void setAutoTaggers(Map<File, AutoTagger> autoTaggersToSet);
	
	/**
	 * Gets the automatic taggers in use.
	 * @return Automatic taggers used.
	 */
	public Map<File, AutoTagger> getAutoTaggers();
	
	public void dropTables();
	
	public void removeFile(String file); // TODO : delete after we're done
	public void unTagFileAll(String file);
	
	public Observable getSignal();
}