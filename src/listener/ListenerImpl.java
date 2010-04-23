/**
 * 
 */
package listener;

/**
 * This class represents an object capable of listening to filesystem
 * events on pre-specified folders and notify about changes to files.
 * @author Or Shwartz, Itay Evron
 */
public class ListenerImpl extends Listener {

	public ListenerImpl() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass().getName() + " up.");
	}
	
	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void listenTo(ListenedDirectory dir) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopListeningTo(ListenedDirectory dir) {
		// TODO Auto-generated method stub
		
	}
}
