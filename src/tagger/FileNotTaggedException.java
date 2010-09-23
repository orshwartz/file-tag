package tagger;

/**
 * This class represents an exception generated upon attempt to perform
 * an operation on a file which is not stored in the tag repository.
 * @author Or Shwartz
 */
public class FileNotTaggedException extends Exception {

	private static final long serialVersionUID = -302357737462011260L;

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		return "File is not tagged.";
	}

}
