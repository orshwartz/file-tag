/**
 * 
 */
package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

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
 * @author Or Shwartz
 *
 */
public class MainAppGUI {

	private Shell sShell = null;
	static Display display = null;
	private Button btnOptionsAndSettings;
	private Button btnSearch;
	private Label lblControlAndMonitor;
	private Label lblOptionsAndSettings;
	private Label lblSearch;
	private Button btnControl;
	
	private static ControlsWindow controlsWindow = new ControlsWindow();
	
	/**
	 * @param commander TODO: Connect commands to actionListeners
	 */
	public MainAppGUI(CommandManager commander) {

		// TODO Auto-generated constructor stub
		System.out.println(this.getClass().getName() + " up.");
		createSShell();
	}

	public void displayGUI() {

		// Display the window
		sShell.open();
		
	    // Set up the event loop.
	    while (!sShell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        // If no more entries in the event queue
	        display.sleep();
	      }
	    }
	    display.dispose();
	}
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		// Setup main window
		display = new Display();
		sShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);

		{
			//Register as a resource user - SWTResourceManager will
			//handle the obtaining and disposing of resources
			SWTResourceManager.registerResourceUser(sShell);
		}
		
		sShell.setText("Tig-Tag-Toe");
		GridLayout sShellLayout = new GridLayout();
		sShellLayout.makeColumnsEqualWidth = true;
		sShellLayout.numColumns = 2;
		sShell.setLayout(sShellLayout);
		{
			btnOptionsAndSettings = new Button(sShell, SWT.PUSH);
			GridData btnOptionsAndSettingsLData = new GridData();
			btnOptionsAndSettingsLData.widthHint = 183;
			btnOptionsAndSettingsLData.heightHint = 58;
			btnOptionsAndSettings.setLayoutData(btnOptionsAndSettingsLData);
			btnOptionsAndSettings.setText("Options and Settings");
			btnOptionsAndSettings.setImage(SWTResourceManager.getImage("gui/res/preferences_system.png"));
		}
		{
			lblOptionsAndSettings = new Label(sShell, SWT.NONE);
			GridData lblOptionsAndSettingsLData = new GridData();
			lblOptionsAndSettings.setLayoutData(lblOptionsAndSettingsLData);
			lblOptionsAndSettings.setText("Select listened directories and files to tag.");
		}
		{
			btnControl = new Button(sShell, SWT.PUSH);
			GridData btnControlLData = new GridData();
			btnControlLData.widthHint = 183;
			btnControlLData.heightHint = 58;
			btnControl.setLayoutData(btnControlLData);
			btnControl.setText("Control and Monitor");
			btnControl.setImage(SWTResourceManager.getImage("gui/res/activity_monitor.png"));
		}
		{
			lblControlAndMonitor = new Label(sShell, SWT.NONE);
			GridData lblControlAndMonitorLData = new GridData();
			lblControlAndMonitor.setLayoutData(lblControlAndMonitorLData);
			lblControlAndMonitor.setText("Start/stop listener and view log.");
		}
		{
			btnSearch = new Button(sShell, SWT.PUSH);
			GridData btnSearchLData = new GridData();
			btnSearchLData.widthHint = 183;
			btnSearchLData.heightHint = 58;
			btnSearch.setLayoutData(btnSearchLData);
			btnSearch.setText("Search");
			//			btnSearch.setImage(SWTResourceManager.getImage("gui/res/search.bmp"));
			btnSearch.setImage(SWTResourceManager.getImage("gui/res/find.png"));
		}
		{
			lblSearch = new Label(sShell, SWT.NONE);
			GridData lblSearchLData = new GridData();
			lblSearch.setLayoutData(lblSearchLData);
			lblSearch.setText("Search tagged files by tags.");
		}
		
		btnControl.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
		          case SWT.Selection:
		        	  
		        	  controlsWindow.makeWindow();
		        	  controlsWindow.open();
				}	
			   }
		});
		
		
		
		sShell.pack();
	}
}
