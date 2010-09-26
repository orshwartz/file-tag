package commander.commands;

import log.EventType;

/**
 * This command writes a certain message to the log.
 * @author Or Shwartz, Itay Evron
 */
public class WriteMessageCommand extends TSCommand {

	/**
	 * Receives a string and writes it to the log.
	 * @see commander.commands.Command#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {
		
		
		// Write a message to the log
		getLog().writeMessage((EventType)params[0]);
		
		return null;
	}
}
