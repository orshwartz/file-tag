package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
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
public class AlgorithmSelector extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Table tblPlugins;
	private Button btnConfig;
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

	public AlgorithmSelector(Shell parent, int style) {
		super(parent, style);
	}

	public void open(Collection<File> pluginFiles) {

		try {
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
				tblPlugins.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event event) {
						
						TableItem[] selection = tblPlugins.getSelection();

						try {
							selectedAutoTagger =
								AutoTaggerLoader.getAutoTagger((File)(selection[0].getData()));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// Display data for selected plug-in
						txtDescription.setText(selectedAutoTagger.getDescription());
						lblAuthorData.setText(selectedAutoTagger.getAuthor());
						lblVersionData.setText(selectedAutoTagger.getVersion().toString());
						lblReleaseDateData.setText(dateFormat.format(selectedAutoTagger.getVersion().getDate()));

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
									((Configurable)selectedAutoTagger).showConfigurationGUI();
								}
							});
						}
						else
						{
							// Disable configuration by GUI - not available
//							btnConfig.removeListener(SWT.Selection, btnConfig.getListeners(SWT.Selection)[0]);
							btnConfig.setEnabled(false);
						}
					}

				});
				{					
					// Populate plug-in list
					TableItem curTableItem = null;
					for (File curPluginFile : pluginFiles) {
						
						curTableItem = new TableItem(tblPlugins, SWT.NONE);
						curTableItem.setText(AutoTaggerLoader.getAutoTagger(curPluginFile).getName()); // XXX: Consider saving the classes to a data member, for later use (showing description, returning, etc...)
						curTableItem.setData(curPluginFile);
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
				btnOK.setToolTipText("Add an auto-tagging algorithm.");
				btnOK.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						// TODO: Set selected algorithms and exit, maybe save them too - to show them selected when window is opened
					}
				});
			}
			{
				btnCancel = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button2LData = new FormData();
				button2LData.left =  new FormAttachment(0, 1000, 342);
				button2LData.top =  new FormAttachment(0, 1000, 48);
				button2LData.width = 74;
				button2LData.height = 25;
				btnCancel.setLayoutData(button2LData);
				btnCancel.setText("Cancel");
				btnCancel.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
					
						// TODO: Just close the window... dispose or something
						dialogShell.dispose();
					}
				});
			}
			dialogShell.layout();
			dialogShell.pack();			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			
			Collection<File> pluginList = new ArrayList<File>();
			pluginList.add(new File("c:/temp/TaggerBySize.class"));
			pluginList.add(new File("c:/temp/TaggerByASCIIContents.class"));
			pluginList.add(new File("c:/temp/TaggerByPathKeywords.class"));
			pluginList.add(new File("c:/temp/TaggerByMetadata.class"));
			
			algSelector.open(pluginList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}