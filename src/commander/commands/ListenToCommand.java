package commander.commands;

import java.io.IOException;

import listener.ListenedDirectory;

public class ListenToCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 * @see listener.Listener#listenTo(listener.ListenedDirectory)
	 */
	@Override
	public Object execute(Object[] params) {

		try {
			getListener().listenTo((ListenedDirectory) params[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
