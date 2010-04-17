package tagger;

/**
 * @author Itay Evron
 *
 */
public class TagAlreadyExistsException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Tag already exists.";
	}


}
