package tagger;

/**
 * @author Itay Evron
 *
 */
public class TagNotFoundException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Tag not found.";
	}

}
