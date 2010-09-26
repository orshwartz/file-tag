/**
 * 
 */
package commander.commands;

import log.EventType;


/**
 * @author Or Shwartz
 *
 */
public class DeactivateListenerCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {
		
		// Deactivate the listener
		getListener().deactivate();
		
		// Write to log
		getLog().writeMessage(EventType.Lstnr_Deact);
		
		return null;
	}

}
