package listener;

/**
 * @author Or Shwartz
 *
 */
public interface Listener {

	public boolean activate();
	public boolean deactivate();
	/* TBD remember to use Observer interface and Observable class */
	public boolean listenTo(ListenedDirectory dir);
	public boolean stopListeningTo(ListenedDirectory dir);
}
