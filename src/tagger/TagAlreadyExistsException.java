package tagger;

/**
 * @author Itay Evron
 *
 */
public class TagAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -6538547593664565402L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Tag already exists.";
	}


}
