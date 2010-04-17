package listener;

/**
 * @author Or Shwartz
 *
 */
public interface Listener {

	public boolean activate();
	public boolean deactivate();
	/* TBD remember to use Observer interface and Observable class */
	public boolean listenTo(/*TBD Decide of format for parameter - 
	 						container, single parameter, etc... - and
	 						probably enable a recursive folder listening
	 						parameter*/);
	public boolean stopListeningTo(/*TBD same as above*/);
}
