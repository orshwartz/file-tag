package commander.commands;

import java.io.File;

import commander.commands.TSCommand;

public class StopListenToCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 * @see listener.Listener#stopListeningTo(File)
	 */
	@Override
	public Object execute(Object[] params) {

		getListener().stopListeningTo((File) params[0]);
		
		return null;
	}

}
