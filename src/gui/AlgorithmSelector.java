package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import tagger.autotagger.AutoTagger;
import tagger.autotagger.AutoTaggerLoader;
import tagger.autotagger.Configurable;

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
public class AlgorithmSelector extends Dialog {

	private Shell dialogShell;
	private Table tblPlugins;
	private Button btnConfig;
	private CLabel lblFileData;
	private CLabel lblFile;
	private CLabel lblReleaseDate;
	private CLabel lblVersion;
	private CLabel lblAuthor;
	private CLabel lblReleaseDateData;
	private CLabel lblVersionData;
	private Button btnCancel;
	private Button btnOK;
	private Text txtDescription;
	private CLabel lblAuthorData;
	private Group grpDescription;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	AutoTagger selectedAutoTagger = null;
	private Map<File, AutoTagger> chosenAutoTaggers = null;

	public AlgorithmSelector(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Shows the GUI for automatic tagging algorithms selection. This GUI displays algorithm data as well.
	 * @param availablePlugins The plugins available for loading.
	 * @param curPlugins The plugins which should be marked as loaded already.
	 * @return Files of chosen automatic taggers and their classes. <TT>null</TT> is returned
	 * if the user chose to abandon changes. 
	 */
	public Map<File, AutoTagger> open(final File[] availablePlugins, File[] curPlugins) {

			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Automatic Tagging Plug-ins");
			dialogShell.setLayout(new FormLayout());
			dialogShell.setMaximized(false);
			{
				lblReleaseDate = new CLabel(dialogShell, SWT.NONE);
				FormData lblReleaseDateLData = new FormData();
				lblReleaseDateLData.left =  new FormAttachment(0, 1000, 12);
				lblReleaseDateLData.top =  new FormAttachment(0, 1000, 181);
				lblReleaseDateLData.width = 81;
				lblReleaseDateLData.height = 21;
				lblReleaseDate.setLayoutData(lblReleaseDateLData);
				lblReleaseDate.setText("Release Date:");
			}
			{
				lblVersion = new CLabel(dialogShell, SWT.NONE);
				FormData lblVersionLData = new FormData();
				lblVersionLData.left =  new FormAttachment(0, 1000, 12);
				lblVersionLData.top =  new FormAttachment(0, 1000, 162);
				lblVersionLData.width = 52;
				lblVersionLData.height = 21;
				lblVersion.setLayoutData(lblVersionLData);
				lblVersion.setText("Version:");
			}
			{
				lblAuthor = new CLabel(dialogShell, SWT.NONE);
				FormData lblAuthorLData = new FormData();
				lblAuthorLData.left =  new FormAttachment(0, 1000, 12);
				lblAuthorLData.top =  new FormAttachment(0, 1000, 143);
				lblAuthorLData.width = 46;
				lblAuthorLData.height = 21;
				lblAuthor.setLayoutData(lblAuthorLData);
				lblAuthor.setText("Author:");
			}
			{
				FormData lblReleaseDateDataLData = new FormData();
				lblReleaseDateDataLData.left =  new FormAttachment(0, 1000, 91);
				lblReleaseDateDataLData.top =  new FormAttachment(0, 1000, 181);
				lblReleaseDateDataLData.width = 315;
				lblReleaseDateDataLData.height = 21;
				lblReleaseDateData = new CLabel(dialogShell, SWT.NONE);
				lblReleaseDateData.setLayoutData(lblReleaseDateDataLData);
			}
			{
				FormData lblVersionDataLData = new FormData();
				lblVersionDataLData.left =  new FormAttachment(0, 1000, 91);
				lblVersionDataLData.top =  new FormAttachment(0, 1000, 162);
				lblVersionDataLData.width = 315;
				lblVersionDataLData.height = 21;
				lblVersionData = new CLabel(dialogShell, SWT.NONE);
				lblVersionData.setLayoutData(lblVersionDataLData);
			}
			{
				FormData lblAuthorDataLData = new FormData();
				lblAuthorDataLData.left =  new FormAttachment(0, 1000, 91);
				lblAuthorDataLData.top =  new FormAttachment(0, 1000, 143);
				lblAuthorDataLData.width = 315;
				lblAuthorDataLData.height = 21;
				lblAuthorData = new CLabel(dialogShell, SWT.NONE);
				lblAuthorData.setLayoutData(lblAuthorDataLData);
			}
			{
				btnConfig = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData btnConfigLData = new FormData();
				btnConfigLData.left =  new FormAttachment(0, 1000, 342);
				btnConfigLData.top =  new FormAttachment(0, 1000, 112);
				btnConfigLData.width = 74;
				btnConfigLData.height = 25;
				btnConfig.setLayoutData(btnConfigLData);
				btnConfig.setText("Configure...");
				btnConfig.setEnabled(false);
			}
			{
				grpDescription = new Group(dialogShell, SWT.NONE);
				GridLayout group1Layout = new GridLayout();
				group1Layout.makeColumnsEqualWidth = true;
				grpDescription.setLayout(group1Layout);
				FormData group1LData = new FormData();
				group1LData.left =  new FormAttachment(0, 1000, 12);
				group1LData.width = 388;
				group1LData.height = 72;
				group1LData.bottom =  new FormAttachment(1000, 1000, -12);
				group1LData.right =  new FormAttachment(1000, 1000, -12);
				group1LData.top =  new FormAttachment(674, 1000, 0);
				grpDescription.setLayoutData(group1LData);
				grpDescription.setText("Description");
				{
					txtDescription = new Text(grpDescription, SWT.MULTI | SWT.WRAP);
					GridData txtDesciptionLData = new GridData();
					txtDesciptionLData.horizontalAlignment = GridData.CENTER;
					txtDesciptionLData.widthHint = 392;
					txtDesciptionLData.heightHint = 59;
					txtDesciptionLData.grabExcessVerticalSpace = true;
					txtDesciptionLData.grabExcessHorizontalSpace = true;
					txtDescription.setBackground(dialogShell.getBackground());
					txtDescription.setEditable(false);
					txtDescription.setLayoutData(txtDesciptionLData);
				}
			}
			{
				FormData table1LData = new FormData();
				table1LData.left =  new FormAttachment(0, 1000, 14);
				table1LData.top =  new FormAttachment(0, 1000, 15);
				table1LData.width = 299;
				table1LData.height = 101;
				tblPlugins = new Table(dialogShell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL);
				tblPlugins.setLayoutData(table1LData);

				tblPlugins.addSelectionListener(new SelectionListener() {
				
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
				
						// Ignore double click
					}
				
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
						// If a checkbox click
						if (arg0.detail == SWT.CHECK) {
							
							// Stop processing - no need to display any data
							return;
						}
						
						// Get AutoTagger object of selected algorithm
						selectedAutoTagger =
							((FileAutoTaggerPair)arg0.item.getData()).getAutoTagger();
						
						// Display data for selected plug-in
						txtDescription.setText(selectedAutoTagger.getDescription());
						lblAuthorData.setText(selectedAutoTagger.getAuthor());
						lblVersionData.setText(selectedAutoTagger.getVersion().toString());
						lblReleaseDateData.setText(dateFormat.format(selectedAutoTagger.getVersion().getDate()));
						lblFileData.setText(((FileAutoTaggerPair)arg0.item.getData()).getFile().getAbsolutePath());

						// If algorithm can be configured by GUI
						if (selectedAutoTagger instanceof Configurable) {

							// Enable configuration by GUI
							btnConfig.setEnabled(true);
							if (btnConfig.getListeners(SWT.Selection).length > 0) {							
								btnConfig.removeListener(SWT.Selection, btnConfig.getListeners(SWT.Selection)[0]);
							}
							btnConfig.addListener(SWT.Selection, new Listener() {

								@Override
								public void handleEvent(Event arg0) {
									
									// Use selected plug-in's GUI configuration when this button is clicked
									try {
										((Configurable)selectedAutoTagger).showConfigurationGUI();
									} catch (Exception e) {
										
										MessageBox msgBox = new MessageBox(dialogShell, SWT.ERROR | SWT.OK);
										msgBox.setText("Algorithm Configuration GUI Error");
										msgBox.setMessage("A problem occurred while trying to display the " +
														  "configuration GUI for the selected algorithm.\n\n" +
														  "An exception was thrown with the details:\n" +
														  e.getLocalizedMessage());
										
										// Show a message box with the error and disable the
										// configuration GUI for now
										msgBox.open();
										btnConfig.setEnabled(false);
									}
								}
							});
						}
						else {
							// Disable configuration by GUI - not available
							btnConfig.setEnabled(false);
						}
					}
				});
				{	
					// If received a valid list
					if (availablePlugins != null) {
						
						// Populate plug-in list
						TableItem curTableItem = null;
						for (File curPluginFile : availablePlugins) {
							
							AutoTagger curAutoTagger = null;
							try {
							curAutoTagger =
								AutoTaggerLoader.getAutoTagger(curPluginFile);
							} catch (Exception e) {
								break;
							}
							curTableItem = new TableItem(tblPlugins, SWT.NONE);
							curTableItem.setText(curAutoTagger.getName());
							curTableItem.setData(new FileAutoTaggerPair(curPluginFile,curAutoTagger));
							
							// Tick checkbox if algorithm should be loaded already
							for (File curLoadedFile : curPlugins) {

								if (curPluginFile.equals(curLoadedFile)) {

									curTableItem.setChecked(true);
									break;
								}
							}
						}
					}
				}
			}
			{
				btnOK = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.left =  new FormAttachment(0, 1000, 342);
				button1LData.top =  new FormAttachment(0, 1000, 17);
				button1LData.width = 74;
				button1LData.height = 25;
				btnOK.setLayoutData(button1LData);
				btnOK.setText("OK");
				btnOK.setToolTipText("Set marked algorithms for automatic tagging.");
				btnOK.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {

						chosenAutoTaggers = new HashMap<File, AutoTagger>(availablePlugins.length);
						
						// For each table item
						for (TableItem curTblItem : tblPlugins.getItems()) {
							
							// If algorithm is marked
							if (curTblItem.getChecked()) {
								
								// Add it to map of chosen algorithms
								FileAutoTaggerPair curData =
									(FileAutoTaggerPair) curTblItem.getData();
								chosenAutoTaggers.put(
										curData.getFile(),
										curData.getAutoTagger());
							}
						}
						
						// Dispose of the window - we're done here
						dialogShell.dispose();
					}
				});
			}
			{
				btnCancel = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData btnCancelLData = new FormData();
				btnCancelLData.left =  new FormAttachment(0, 1000, 342);
				btnCancelLData.top =  new FormAttachment(0, 1000, 48);
				btnCancelLData.width = 74;
				btnCancelLData.height = 25;
				btnCancel.setLayoutData(btnCancelLData);
				btnCancel.setText("Cancel");
				btnCancel.setToolTipText("Discard changes to algorithm selection.");
				btnCancel.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
					
						// Just dispose of the window
						dialogShell.dispose();
					}
				});
			}
			{
				lblFile = new CLabel(dialogShell, SWT.NONE);
				FormData lblFileLData = new FormData();
				lblFileLData.left =  new FormAttachment(0, 1000, 12);
				lblFileLData.top =  new FormAttachment(0, 1000, 199);
				lblFileLData.width = 81;
				lblFileLData.height = 21;
				lblFile.setLayoutData(lblFileLData);
				lblFile.setText("File:");
			}
			{
				FormData lblFileDataLData = new FormData();
				lblFileDataLData.left =  new FormAttachment(0, 1000, 91);
				lblFileDataLData.top =  new FormAttachment(0, 1000, 199);
				lblFileDataLData.width = 315;
				lblFileDataLData.height = 21;
				lblFileData = new CLabel(dialogShell, SWT.NONE);
				lblFileData.setLayoutData(lblFileDataLData);
			}
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(432, 368);
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			dialogShell.addListener(SWT.Close, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					dialogShell.dispose();
				}
			});
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}

		// Return chosen automatic taggers
		return chosenAutoTaggers;
	}
	
	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			AlgorithmSelector algSelector = new AlgorithmSelector(shell, SWT.NULL);
			
			File[] pluginList =
				new File[] {
					new File("c:/temp/TaggerBySize.class"),
					new File("c:/temp/TaggerByASCIIContents.class"),
					new File("c:/temp/TaggerByPathKeywords.class"),
					new File("c:/temp/TaggerByMetadata.class")
				};
			File[] curPlugins =
				new File[] {
					new File("c:/temp/TaggerByMetadata.class"),
					new File("c:/temp/TaggerByPathKeywords.class")
				};
			
			// Show the dialog
			algSelector.open(pluginList,curPlugins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This inner class is used to simplify the saving of two object as data for each table item,
	 * since we want to save a file name and also an autotagger object if one is created. This is so that
	 * we don't have to create an <CODE>AutoTagger</CODE> object again if it already was created.
	 * @author Or Shwartz
	 */
	private class FileAutoTaggerPair {

		File file;
		AutoTagger autoTagger;
		
		/**
		 * Initializes the class with the file and the autotagger object. 
		 * @param file The file of the automatic tagger.
		 * @param autoTagger The automatic tagger object.
		 */
		public FileAutoTaggerPair(File file, AutoTagger autoTagger) {
			
			this.file = file;
			this.autoTagger = autoTagger;
		}

		/**
		 * Getter for the file.
		 * @return the file
		 */
		public File getFile() {
			return file;
		}
		
		/**
		 * Getter for the automatic tagger object.
		 * @return the autoTagger
		 */
		public AutoTagger getAutoTagger() {
			return autoTagger;
		}

		/**
		 * Setter for <CODE>AutoTagger</CODE> object.
		 * @param autoTagger the autoTagger to set
		 */
		public void setAutoTagger(AutoTagger autoTagger) {
			this.autoTagger = autoTagger;
		}
	}
}