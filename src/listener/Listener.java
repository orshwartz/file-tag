package listener;

/**
 * Interface for a filesystem listener, watching for changes.
 * @author Or Shwartz
 */
public interface Listener {

	public void activate(); /* TBD throws exception? */
	public void deactivate(); /* TBD throws exception? */
	public boolean isActive();
	/* TBD remember to use Observer interface and Observable class */
	public void listenTo(ListenedDirectory dir); /* TBD throws exception? */
	public void stopListeningTo(ListenedDirectory dir); /* TBD throws exception? */
}
