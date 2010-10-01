package commander.commands;

import listener.FileEvent;

/**
 * This command writes a certain file message to the log.
 * @author Itay Evron
 *
 */
public class WriteFileMessageCommand extends TSCommand{

	@Override
	public Object execute(Object[] params) {
		
		getLog().writeMessage((FileEvent)params[0]);
		return null;
	}

}
