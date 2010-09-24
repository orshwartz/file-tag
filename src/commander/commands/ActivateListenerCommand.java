/**
 * 
 */
package commander.commands;

/**
 * @author Or Shwartz, Itay Evron
 *
 */
public class ActivateListenerCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {

		// Activate the listener
		getListener().activate();
		
		// Write event to log
		getLog().writeMessage("Listener activated.");
		
		return null;
	}

}
