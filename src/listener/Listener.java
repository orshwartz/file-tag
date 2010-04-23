package listener;

import java.util.Observable;

/**
 * Abstract class for a filesystem listener, watching for changes.
 * @see java.util.Observable
 * @author Or Shwartz, Itay Evron
 */
public abstract class Listener extends Observable {

	public abstract void activate(); /* TBD throws exception? */
	public abstract void deactivate(); /* TBD throws exception? */
	public abstract boolean isActive();
	/* TBD remember to use Observer interface and Observable class */
	public abstract void listenTo(ListenedDirectory dir); /* TBD throws exception? */
	public abstract void stopListeningTo(ListenedDirectory dir); /* TBD throws exception? */
}
