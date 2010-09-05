package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import listener.ListenedDirectory;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.cloudgarden.resource.SWTResourceManager;


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
public class ListenedPathsDialog extends Dialog {

	private Shell dialogShell;
	private Label lblListenedPaths;
	private Composite cmpstRemoveAddRegex;
	private Button btnCancel;
	private Button btnOK;
	private Button btnAddRegex;
	private Button btnRemoveRegex;
	private List lstRegularExpressions;
	private CheckboxTreeViewer checkboxTreeViewer;
	private Label lblRegularExpressions;
	
	private String[] checkedPaths = null;
	
	HashMap<File, ListenedDirectory> listenedDirs = null;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			ListenedPathsDialog inst = new ListenedPathsDialog(shell, SWT.NONE);
			
			HashMap<File, ListenedDirectory> listenedDirs = new HashMap<File, ListenedDirectory>();
			Collection<String> regexes = new ArrayList<String>();
			regexes.add(".*\\.txt");
			regexes.add(".*\\.mp3");
			regexes.add(".*\\.pdf");
			regexes.add(".*\\.doc");
			File dir = new File("C:\\TEMP");
			listenedDirs.put(dir, new ListenedDirectory(dir, regexes));
			dir = new File("C:\\Vered&Or"); 
			listenedDirs.put(dir, new ListenedDirectory(dir, regexes));
			dir = new File("D:/DELL");
			listenedDirs.put(dir, new ListenedDirectory(dir, regexes));

			String[] checkedPaths = inst.open(listenedDirs);
			if (checkedPaths != null) {
				for (String string : checkedPaths) {
					System.out.println(string);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListenedPathsDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Converts an array of objects to array of strings, using
	 * their toString method.
	 * @param objArray Array of objects.
	 * @return The objects array converted to a string array.
	 */
	private String[] convertObjArrayToStrArray(Object[] objArray) {
		
		final int size =
			((objArray != null)?
					objArray.length :
					0);
		
		String[] strArray = new String[size];
		
		// For each object
		for (int curItemIdx = 0; curItemIdx < size; ++curItemIdx) {
			
			// Convert the object to a string
			strArray[curItemIdx] = objArray[curItemIdx].toString();
		}
		
		return strArray;
	}
	
	/**
	 * Saves the checked paths.
	 * @param checkedPaths Checked paths to save.
	 */
	private void setCheckedPaths(String[] checkedPaths) {
		
		this.checkedPaths = checkedPaths;
	}
	
	public String[] open(final HashMap<File, ListenedDirectory> realListenedDirs) {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM |
											SWT.APPLICATION_MODAL |
											SWT.RESIZE |
											SWT.MAX);

			{
				//Register as a resource user - SWTResourceManager will
				//handle the obtaining and disposing of resources
				SWTResourceManager.registerResourceUser(dialogShell);
			}

			// Duplicate the hashmap
			listenedDirs =
				new HashMap<File, ListenedDirectory>(realListenedDirs.keySet().size());		
			for (ListenedDirectory curListenedDir : realListenedDirs.values()) {
				
				listenedDirs.put(curListenedDir.getDirectory(), curListenedDir);
			}
			
			GridLayout dialogShellLayout = new GridLayout();
			dialogShellLayout.numColumns = 3;
			dialogShell.setLayout(dialogShellLayout);
			dialogShell.setText("Select Listened Paths");
			{
				lblListenedPaths = new Label(dialogShell, SWT.NONE);
				GridData lblListenedPathsLData = new GridData();
				lblListenedPaths.setLayoutData(lblListenedPathsLData);
				lblListenedPaths.setText("Listened paths");
			}
			{
				lblRegularExpressions = new Label(dialogShell, SWT.NONE);
				GridData lblRegularExpressionsLData = new GridData();
				lblRegularExpressionsLData.horizontalSpan = 2;
				lblRegularExpressions.setLayoutData(lblRegularExpressionsLData);
				lblRegularExpressions.setText("File filters");
			}
//			{
//				GridData treePathsLData = new GridData();
//				treePathsLData.verticalAlignment = GridData.FILL;
//				treePathsLData.horizontalAlignment = GridData.FILL;
//				treePathsLData.grabExcessVerticalSpace = true;
//				treePathsLData.grabExcessHorizontalSpace = true;
//				treePaths = new Tree(dialogShell, SWT.BORDER);
//				treePaths.setLayoutData(treePathsLData);
//			}
			{
				checkboxTreeViewer = new CheckboxTreeViewer(dialogShell, SWT.BORDER);
				GridData checkboxTreeViewerLData = new GridData();
				checkboxTreeViewerLData.verticalAlignment = GridData.FILL;
				checkboxTreeViewerLData.horizontalAlignment = GridData.FILL;
				checkboxTreeViewerLData.grabExcessVerticalSpace = true;
				checkboxTreeViewerLData.grabExcessHorizontalSpace = true;
				checkboxTreeViewer.getTree().setLayoutData(checkboxTreeViewerLData);
				checkboxTreeViewer.setContentProvider(new FileTreeContentProvider());
				checkboxTreeViewer.setLabelProvider(new FileTreeLabelProvider());
				checkboxTreeViewer.setInput("root"); // pass a non-null that will be ignored
				checkboxTreeViewer.addCheckStateListener(new ICheckStateListener() {
					@Override
					public void checkStateChanged(CheckStateChangedEvent event) {

// TODO: Reconsider using this implementation
//						// If the item is checked...
//						if (event.getChecked()) {
//							// ...check all its children
//							checkboxTreeViewer.setSubtreeChecked(event.getElement(), true);
//						}
//						// Else, item is unchecked so...
//						else {
//							// ...uncheck all its children
//							checkboxTreeViewer.setSubtreeChecked(event.getElement(), false);					
//						}

						// If the item is checked...
						if (event.getChecked()) {

						}
						// Else, item is unchecked so...
						else {
					
						}
						
						setCheckedPaths(convertObjArrayToStrArray(checkboxTreeViewer.getCheckedElements()));
					}
				});
				checkboxTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent arg0) {
						
						String selection = arg0.getSelection().toString();
						String selectedDir = selection.substring(1,selection.length()-1);  
						ListenedDirectory listenedDir = listenedDirs.get(new File(selectedDir));
						
						lstRegularExpressions.removeAll();
						
						// If selected directory is listened to
						if (listenedDir != null) {
							
							// For each regular expression of listened directory
							for (String curRegex : listenedDir.getRegularExpressions()) {
								
								// Add the regular expression the the displayed list
								lstRegularExpressions.add(curRegex);
							}
						}
					}
				});
				
				// For each listened directory
				Collection<ListenedDirectory> listenedDirsValues = listenedDirs.values();
				for (ListenedDirectory listenedDirectory : listenedDirsValues) {
					
					// Mark directory as listened in the tree
					checkboxTreeViewer.setChecked(
							listenedDirectory.getDirectory(),
							true);
				}
			}
			{
				GridData lstRegularExpressionsLData = new GridData();
				lstRegularExpressionsLData.verticalAlignment = GridData.FILL;
				lstRegularExpressionsLData.horizontalAlignment = GridData.FILL;
				lstRegularExpressionsLData.grabExcessVerticalSpace = true;
				lstRegularExpressionsLData.grabExcessHorizontalSpace = true;
				lstRegularExpressions = new List(dialogShell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
				lstRegularExpressions.setLayoutData(lstRegularExpressionsLData);
				lstRegularExpressions.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						btnRemoveRegex.setEnabled(
							lstRegularExpressions.getSelectionCount() == 0?
								false :
								true);
					}
				});
			}
			{
				cmpstRemoveAddRegex = new Composite(dialogShell, SWT.NONE);
				GridLayout cmpstRemoveAddRegexLayout = new GridLayout();
				cmpstRemoveAddRegexLayout.makeColumnsEqualWidth = true;
				GridData cmpstRemoveAddRegexLData = new GridData();
				cmpstRemoveAddRegexLData.horizontalAlignment = GridData.FILL;
				cmpstRemoveAddRegex.setLayoutData(cmpstRemoveAddRegexLData);
				cmpstRemoveAddRegex.setLayout(cmpstRemoveAddRegexLayout);
				{
					btnRemoveRegex = new Button(cmpstRemoveAddRegex, SWT.PUSH | SWT.CENTER);
					GridData btnRemoveRegexLData = new GridData();
					btnRemoveRegexLData.horizontalAlignment = GridData.FILL;
					btnRemoveRegex.setLayoutData(btnRemoveRegexLData);
					btnRemoveRegex.setText("-");
					btnRemoveRegex.setToolTipText("Remove selected regular expressions.");
					btnRemoveRegex.setEnabled(false);
					btnRemoveRegex.addListener(SWT.Selection, new Listener() {

						@Override
						public void handleEvent(Event arg0) {

							// Remove selected regular expressions and disable the
							// button (because nothing will be selected)
							lstRegularExpressions.remove(lstRegularExpressions.getSelectionIndices());
							btnRemoveRegex.setEnabled(false);
						}
					});
				}
				{
					btnAddRegex = new Button(cmpstRemoveAddRegex, SWT.PUSH | SWT.CENTER);
					GridData btnAddRegexLData = new GridData();
					btnAddRegex.setLayoutData(btnAddRegexLData);
					btnAddRegex.setText("+");
					btnAddRegex.setToolTipText("Add a new regular expression.");
					btnAddRegex.addListener(SWT.Selection, new Listener() {

						@Override
						public void handleEvent(Event arg0) {
							
							RegexInputDialog regexInputDialog =
								new RegexInputDialog(dialogShell, SWT.NONE);
							String newRegex = regexInputDialog.open();
							System.out.println(newRegex);
						}
					});
				}
			}
			{
				btnOK = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				GridData btnOKLData = new GridData();
				btnOKLData.horizontalAlignment = GridData.END;
				btnOK.setLayoutData(btnOKLData);
				btnOK.setText("OK");
				btnOK.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						// TODO: Apply changes...
					}
				});
			}
			{
				btnCancel = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				GridData btnCancelLData = new GridData();
				btnCancelLData.horizontalSpan = 2;
				btnCancel.setLayoutData(btnCancelLData);
				btnCancel.setText("Cancel");
				btnCancel.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						// TODO: Discard changes...
					}
				});
			}
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(500, 500);
			dialogShell.setMinimumSize(250, 250);
			dialogShell.open();
			
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return checkedPaths;
	}
	
}
