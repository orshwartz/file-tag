/**
 * 
 */
package gui;


import static commander.CommandManager.CmdCodes.LSTNR_GET_LISTENED_DIRS;
import static commander.CommandManager.CmdCodes.LSTNR_LISTEN_TO;
import static commander.CommandManager.CmdCodes.LSTNR_STOP_LISTENING_TO;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import listener.ListenedDirectory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
 * @author Or Shwartz, Itay Evron
 *
 */
public class MainAppGUI {

	public static CommandManager commander = null;
	private static Shell sShell = null;
	static Display display = null;
	private Button btnOptionsAndSettings;
	private Button btnSearch;
	private Button btnControl;
	private Label lblControlAndMonitor;
	private Label lblOptionsAndSettings;
	private Label lblSearch;

	
	private static ControlsWindow controlsWindow = null;
	private static SearchWindow searchWindow = null;
	
	/**
	 * @param commander TODO: Connect commands to actionListeners
	 */
	public MainAppGUI(CommandManager commander) {

		// TODO Auto-generated constructor stub
		
		MainAppGUI.commander = commander;
		
		createSShell();
		
		controlsWindow = new ControlsWindow(commander);
		searchWindow = new SearchWindow(commander);
		
		System.out.println(this.getClass().getName() + " up.");
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
		sShell.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				
				// Dispose of the window
				sShell.dispose();
			}
		});
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

			Menu mnuOptions = new Menu(sShell, SWT.POP_UP);
			
			MenuItem mnuListenedPaths = new MenuItem(mnuOptions, SWT.PUSH);
			mnuListenedPaths.setText("Select listened paths");
			mnuListenedPaths.addListener(SWT.Selection, new Listener() {

				@SuppressWarnings("unchecked")
				public void handleEvent(Event e) {
					ListenedPathsDialog listenedPathsDialog = new ListenedPathsDialog(sShell, SWT.None);
					ListenedPathsDelta pathsDelta =
						listenedPathsDialog.open(
							(HashMap<File, ListenedDirectory>) commander.getCommand(
									LSTNR_GET_LISTENED_DIRS).execute(null));
					
					// Listen to newly added directories
					for (ListenedDirectory curListenedDir : pathsDelta.getAddedListenedDirectories()) {
						Object[] params = new Object[] {curListenedDir};
						commander.getCommand(LSTNR_LISTEN_TO).execute(params);
					}
					
					// Stop listening to removed directories
					for (File curFile : pathsDelta.getRemovedListenedDirectories()) {
						Object[] params = new Object[] {curFile};
						commander.getCommand(LSTNR_STOP_LISTENING_TO).execute(params);
					}
					
					// Update regular expressions for directories
					for (ListenedDirectory curListenedDir : pathsDelta.getModifiedListenedDirectories()) {
						Object[] params = new Object[] {curListenedDir};
						commander.getCommand(LSTNR_LISTEN_TO).execute(params);
					}					
				}
			});
			MenuItem mnuAlgorithmSelector = new MenuItem(mnuOptions, SWT.PUSH);
			mnuAlgorithmSelector.setText("Select tagging algorithms");
			mnuAlgorithmSelector.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event e) {

					File pluginsDir =
						new File("./plugins");System.out.println(pluginsDir.getAbsolutePath());
					File[] possiblePlugins =
						pluginsDir.listFiles(new FilenameFilter() {

						/**
						 * Accepts .class and .jar files.
						 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
						 */
						@Override
						public boolean accept(File dir, String name) {

							return name.endsWith(".class") ||
								   name.endsWith(".jar");
						}
					});
					
					AlgorithmSelector algSelector =
						new AlgorithmSelector(sShell, SWT.DIALOG_TRIM |
													  SWT.APPLICATION_MODAL);
					
					// Open the algorithm selection GUI with given possible plugins
					algSelector.open(possiblePlugins);
				}
			});
			btnOptionsAndSettings.setMenu(mnuOptions);
			btnOptionsAndSettings.addListener(SWT.Selection, new Listener() {

				@SuppressWarnings("unchecked")
				@Override
				public void handleEvent(Event arg0) {
					
					btnOptionsAndSettings.getMenu().setVisible(true);
				}
			});
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


		/* -------- Listen to buttons ---------------*/
		
		
//		btnControl.addListener(SWT.Selection, new Listener() {
//			public void handleEvent(Event e) {
//				switch (e.type) {
//		          case SWT.Selection:
//		        	  
//		        	  controlsWindow.makeWindow();
//		        	  controlsWindow.open();
//				}	
//			   }
//		});
		
		btnSearch.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				searchWindow.open();
			}
		});
				
		sShell.pack();
	}
}
