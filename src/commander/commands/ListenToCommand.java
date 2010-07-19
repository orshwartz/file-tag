package commander.commands;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.plaf.OptionPaneUI;

import org.eclipse.swt.widgets.MessageBox;

import listener.ListenedDirectory;
import commander.commands.TSCommand;

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
