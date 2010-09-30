package tagger;

/**
 * @author Itay Evron
 *
 */
public class TagNotFoundException extends Exception {

	private static final long serialVersionUID = 831795234144189789L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Tag not found.";
	}

}
