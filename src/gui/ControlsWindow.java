package gui;

import static commander.CommandManager.CmdCodes.LSTNR_ACTIVATE;
import static commander.CommandManager.CmdCodes.LSTNR_DEACTIVATE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.cloudgarden.resource.SWTResourceManager;
import commander.CommandManager;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * 
 * @author Itai Evron
 *
 */
public class ControlsWindow {

	public CommandManager commander;
	private static Shell window;
	
	
	private static Group logGroup;
	private Table table1;
	private Label label1;
	private Button button1;

	public ControlsWindow(CommandManager commander){
		this.commander = commander;	
		
	}
	
	public void makeWindow(){
		// make the window
		window = new Shell(MainAppGUI.display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		window.setText("Control and Monitor");
		//GridLayout sShellLayout = new GridLayout();
		//sShellLayout.makeColumnsEqualWidth = true;
		//sShellLayout.numColumns = 2;
		window.setLayout(new FormLayout());
		window.setMaximized(false);
		window.setSize(500, 340);
		{
			label1 = new Label(window, SWT.NONE);
			FormData label1LData = new FormData();
			label1LData.left =  new FormAttachment(0, 1000, 12);
			label1LData.top =  new FormAttachment(0, 1000, 12);
			label1LData.width = 115;
			label1LData.height = 13;
			label1.setLayoutData(label1LData);
			label1.setText("System Log");
		}
		{
			FormData table1LData = new FormData();
			table1LData.width = 330;
			table1LData.height = 239;
			table1 = new Table(window, SWT.BORDER);
			table1.setLayoutData(table1LData);
		}
		{
			button1 = new Button(window, SWT.PUSH | SWT.UP);
			FormData button1LData = new FormData();
			button1LData.width = 113;
			button1LData.height = 49;
			button1.setLayoutData(button1LData);
			button1.setText("Activate Listener");
		}

	}
	
	
	
	public void open(){
		 window.open();
		  while (!window.isDisposed()) {
		      if (!MainAppGUI.display.readAndDispatch())
		    	  MainAppGUI.display.sleep();
		    }
	 }
}
