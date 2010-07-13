package gui;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

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
	private TableItem tableItem1;
	private TableItem tableItem2;
	private Group grpDescription;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			AlgorithmSelector inst = new AlgorithmSelector(shell, SWT.NULL);
			inst.open(new File(".").toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AlgorithmSelector(Shell parent, int style) {
		super(parent, style);
	}

	public void open(Path pluginDirectory) {
		
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
				lblReleaseDateLData.width = 75;
				lblReleaseDateLData.height = 21;
				lblReleaseDate.setLayoutData(lblReleaseDateLData);
				lblReleaseDate.setText("Release Date:");
			}
			{
				lblVersion = new CLabel(dialogShell, SWT.NONE);
				FormData lblVersionLData = new FormData();
				lblVersionLData.left =  new FormAttachment(0, 1000, 12);
				lblVersionLData.top =  new FormAttachment(0, 1000, 162);
				lblVersionLData.width = 48;
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
					txtDescription = new Text(grpDescription, SWT.MULTI);
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
				tblPlugins.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) {
						
						System.out.println("Focus gained.");
					}
					
					@Override
					public void focusLost(FocusEvent arg0) {
						
						System.out.println("Focus lost.");
					}
				});
				{
					tableItem1 = new TableItem(tblPlugins, SWT.NONE);
					tableItem1.setText("tableItem1");
					tableItem2 = new TableItem(tblPlugins, SWT.CHECK);
					tableItem2.setText("tableItem2");
				}
				tblPlugins.addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
						System.out.println("widgetDefaultSelected");
					}
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
						System.out.println(tblPlugins.getSelectionIndex());
						
						System.out.println("widgetSelected");
					}
				});
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
			}
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}