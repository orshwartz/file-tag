package tagging;

import java.io.File;
import java.util.Collection;

/**
 * @author Or Shwartz
 *
 */
public interface TagRepository {
	
	public boolean tagExists(String tag);
	public boolean removeTag(String tag);/* TBD Consider
											exception instead of
											boolean */
	public boolean addTag(String tag); /* TBD Consider
										exception instead of
										boolean */
	public boolean tagFile(File file, String tag);
	public boolean untagFile(File file, String tag);
	public boolean renameTag(String oldName, String newName); /* TBD Consider
																exception instead of
																boolean */
	public Collection<File> searchByTag(Collection<String> includedTags,
										Collection<String> excludedTags);
	public Collection<String> getTagListFreqOrdered();
}
