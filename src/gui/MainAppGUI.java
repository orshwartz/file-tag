/**
 * 
 */
package gui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
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
	Display display = null;
	private Button btnOptionsAndSettings;
	private Button btnSearch;
	private Button btnControl;
	
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
		RowLayout sShellLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
		sShell.setLayout(sShellLayout);
		{
			btnControl = new Button(sShell, SWT.PUSH);
			RowData btnControlLData = new RowData();
			btnControl.setLayoutData(btnControlLData);
			btnControl.setText("Control and Monitor");
			btnControl.setImage(SWTResourceManager.getImage("gui/res/monitor.bmp"));
		}
		{
			btnOptionsAndSettings = new Button(sShell, SWT.PUSH);
			RowData btnOptionsAndSettingsLData = new RowData();
			btnOptionsAndSettings.setLayoutData(btnOptionsAndSettingsLData);
			btnOptionsAndSettings.setText("Options and Settings");
			btnOptionsAndSettings.setImage(SWTResourceManager.getImage("gui/res/settings.bmp"));
		}
		{
			btnSearch = new Button(sShell, SWT.PUSH);
			RowData btnSearchLData = new RowData();
			btnSearch.setLayoutData(btnSearchLData);
			btnSearch.setText("Search");
			btnSearch.setImage(SWTResourceManager.getImage("gui/res/search.bmp"));
		}
		sShell.pack();
		sShell.setSize(478, 100);
	}
}
