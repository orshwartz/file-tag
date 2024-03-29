package tagger.autotagger;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

/**
 * This interface represents an automatic tagger, returning tags for
 * a given file. It should also be able to return a description of the
 * algorithm used to return those tags.
 * @author Or Shwartz
 */
public interface AutoTagger extends Serializable {

	/**
	 * Return tags for given <code>file</code>.
	 * Tags matching the file are determined by implementing algorithm.
	 * @param file for which tags should be returned.
	 * @return tags for file, determined by implementing algorithm.
	 * @throws Exception If any problem occurred during attempt to retrieve tags.
	 */
	Collection<String> autoTag(File file) throws Exception;
	
	/**
	 * Get a description of the algorithm.
	 * @return Algorithm description.
	 */
	String getDescription();
	
	/**
	 * Get version of this auto tagging algorithm.
	 * @return Version of algorithm.
	 */
	Version getVersion();
	
	/**
	 * Get author of this algorithm.
	 * @return Name of algorithm creator.
	 */
	String getAuthor();
	
	/**
	 * Returns the name of the algorithm.
	 * @return Algorithm name.
	 */
	String getName();
}
