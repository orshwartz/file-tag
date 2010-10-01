/**
 * 
 */
package gui;


import static commander.CommandManager.CmdCodes.*;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

import listener.ListenedDirectory;
import log.EventType;

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

import tagger.autotagger.AutoTagger;

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

	private static SearchWindow searchWindow = null;
	private static MonitorWindow monitorWindow = null;
	
	
	/**
	 * This class is the main GUI controller for the application.
	 * @param commander The is what contains all the commands the system enables,
	 * so that the GUI can control different subsystems.
	 */
	public MainAppGUI(CommandManager commander) {
		
		MainAppGUI.commander = commander;
		
		createSShell();
		
		searchWindow = new SearchWindow(commander);
		monitorWindow = new MonitorWindow(sShell, commander);
		
		Object[] params = {EventType.System_Up};
		commander.getCommand(LOG_WRITE_MESSAGE).execute(params);
		
		
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

				@SuppressWarnings("unchecked")
				public void handleEvent(Event e) {

					File pluginsDir =
						new File("plugins");
					try {
						// Create directory for plugins
						pluginsDir.toPath().createDirectory();
					} catch (FileAlreadyExistsException e0) {
					
						// Ignore this - if directory exists there's no problem
					} catch (Exception e1) {

						// Report problem to log
						Object[] params =
							new Object[] {EventType.Error_Plugin_Creation};
						commander.getCommand(LOG_WRITE_MESSAGE).execute(params);
					}
					File[] possiblePlugins =
						pluginsDir.listFiles(new FilenameFilter() {

							/**
							 * Accepts .class and .jar files.
							 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
							 */
							@Override
							public boolean accept(File dir, String name) {
	
								/// FIXME: Ignore jars : I couldn't get jars
								/// to work before project submission :-(
//								return name.endsWith(".class") ||
//									   name.endsWith(".jar");
								return name.endsWith(".class");
							}
						});
					
					AlgorithmSelector algSelector =
						new AlgorithmSelector(sShell, SWT.DIALOG_TRIM |
													  SWT.APPLICATION_MODAL);

					// Get loaded automatic taggers
					Map<File, AutoTagger> usedAutotaggers =
						(Map<File, AutoTagger>) commander.getCommand(TAGGER_GET_AUTO_TAGGERS).execute(null);

					// Open the algorithm selection GUI with given possible plugins
					Map<File,AutoTagger> chosenAutoTaggers =
						algSelector.open(
								possiblePlugins,
								castObjArrayToFileArray(usedAutotaggers.keySet().toArray()));

					// If user didn't abandon changes
					if (chosenAutoTaggers != null) {
						
						// FIXME : Should it be SET or GET ??????
						// Set chosen automatic taggers
						commander.getCommand(TAGGER_SET_AUTO_TAGGERS).execute(
								new Object[] {chosenAutoTaggers});
					}
				}

				private File[] castObjArrayToFileArray(Object[] objArray) {
					
					File[] fileArray = new File[objArray.length];
					
					// Cast each object in the object array to file
					for (int i = 0; i < objArray.length; i++) {
						
						fileArray[i] = (File)objArray[i];
					}
					
					return fileArray;
				}
			});
			btnOptionsAndSettings.setMenu(mnuOptions);
			btnOptionsAndSettings.addListener(SWT.Selection, new Listener() {

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


		/* -------- Listen to buttons --------------- */
		
		
		btnControl.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
	
		        	  monitorWindow.open();
			   }
		});
		
		btnSearch.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				searchWindow.open();
			}
		});
				
		sShell.pack();
	}
}
