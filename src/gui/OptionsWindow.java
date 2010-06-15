package gui;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import commander.CommandManager;


/**
 * @author Itai Evron
 *
 */
public class OptionsWindow {
	
	public CommandManager commander;
	
	private static Shell window;
	private static Group group;
	
	
	public OptionsWindow(CommandManager commander){
		this.commander = commander;	
	}
	
	
	public void makeWindow(){
		window = new Shell(MainAppGUI.display);
		window.setText("Options and Settings");
		window.setSize(300,340);
	}
	
	public void open(){
		 window.open();
		  while (!window.isDisposed()) {
		      if (!MainAppGUI.display.readAndDispatch())
		    	  MainAppGUI.display.sleep();
		    }
	 }

}
