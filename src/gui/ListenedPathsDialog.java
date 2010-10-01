package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import listener.ListenedDirectory;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
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
	
	private ListenedPathsDelta pathsDelta = null;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			ListenedPathsDialog inst = new ListenedPathsDialog(shell, SWT.NONE);
			
			HashMap<File, ListenedDirectory> listenedDirs = new HashMap<File, ListenedDirectory>();
			ArrayList<String> regexes = new ArrayList<String>();
			regexes.add(".*\\.txt");
			regexes.add(".*\\.mp3");
			regexes.add(".*\\.pdf");
			regexes.add(".*\\.doc");
			File dir = new File("C:\\TEMP");
			listenedDirs.put(dir, new ListenedDirectory(dir, regexes));
			dir = new File("C:\\Vered&Or"); 
			listenedDirs.put(dir, new ListenedDirectory(dir, (Collection<String>) regexes.clone()));
			dir = new File("D:/DELL");
			listenedDirs.put(dir, new ListenedDirectory(dir, (Collection<String>) regexes.clone()));

			ListenedPathsDelta pathsDelta = inst.open(listenedDirs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListenedPathsDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Converts a single selection of a file from the checkbox tree viewer
	 * to a file object.
	 * @param selection the <CODE>ISelection</CODE> object to convert.
	 * @return The file object for the matching selection.
	 */
	private File convertISelectionToFile(ISelection selection) {
		
		String strSelection = selection.toString();
		
		return new File(strSelection.substring(1,
											   strSelection.length()-1));
	}
	
	/**
	 * @param listenedDirsOriginal The current status of listened directories, which will
	 * be displayed and also the returned delta will be calculated from.
	 * @return an object containing the user entered data in the form of the
	 * differences between what data was before and the new data.
	 */
	@SuppressWarnings("unchecked")
	public ListenedPathsDelta open(final HashMap<File, ListenedDirectory> listenedDirsOriginal) {

		final HashMap<File, ListenedDirectory> listenedDirsNew =
			(HashMap<File, ListenedDirectory>) listenedDirsOriginal.clone();
		
		// Clone the collection of regular expressions since we're going to
		// edit it and we don't want changes in the collection to be reflected
		// in the original collection
		Collection<ListenedDirectory> newListenedDirs =
			listenedDirsNew.values();
		for (ListenedDirectory curListenedDir : newListenedDirs) {

			Collection<String> newRegexes =
				new ArrayList<String>(curListenedDir.getRegularExpressions());
			try {
				ListenedDirectory clonedListenedDirectory =
					new ListenedDirectory(curListenedDir.getDirectory(),
										  newRegexes);
				listenedDirsNew.put(curListenedDir.getDirectory(),
									clonedListenedDirectory);
			} catch (Exception e) {

				// Ignore, not supposed to happen in this case but do display
				// stack trace in case it does, somehow
				e.printStackTrace();
			}
		}
		
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
						
						// If the item is checked...
						if (event.getChecked()) {
							// If item is already listened to
							File checkedDir = (File) event.getElement();
							if (listenedDirsOriginal.containsKey(checkedDir)) {
								
								// Use the original regular expressions
								listenedDirsNew.put(checkedDir,
													listenedDirsOriginal.get(checkedDir));
							}
							
							// Else, item was not already listened to
							else {
								try {
									// Set no filter, using an "everything" filter
									Collection<String> regexes = new ArrayList<String>(5);
									regexes.add(".*");
									listenedDirsNew.put(checkedDir,
														new ListenedDirectory(checkedDir,
																			  regexes));
								} catch (Exception e) {
									
									// Ignore it
									return;
									
									// XXX: Maybe something better needs to be done?
								}
							}

							// If checked element is also selected, update regex display
							File selectedElement =
								convertISelectionToFile(checkboxTreeViewer.getSelection());
							if (event.getElement().equals(selectedElement)) {

								// Clear the displayed regular expressions list
								lstRegularExpressions.removeAll();
								
								// For each regular expression
								Collection<String> curRegexes =
									((ListenedDirectory)listenedDirsNew.get(checkedDir)).
										getRegularExpressions();
								for (String curRegex : curRegexes) {
									
									// Add regular expression to displayed list of
									// regular expressions
									lstRegularExpressions.add(curRegex);
								}
								
								// Enable regex addition button
								btnAddRegex.setEnabled(true);
							}
						}
						// Else, item is unchecked so...
						else {
							// If unchecked element is also selected
							File selectedElement =
								convertISelectionToFile(checkboxTreeViewer.getSelection());
							
							// FIXME : I'm not sure it's correct
							//		   but why we do also need mouse selection
							//         for unchecking the directory ?
							
							//if (event.getElement().equals(selectedElement)) {
								// Clear the list of regular expressions
								lstRegularExpressions.removeAll();
								
								// Remove directory from wanted listened directories
								// FIXME : previous : listenedDirsNew.remove(selectedElement);
								listenedDirsNew.remove(event.getElement());
							//}							
							// Disable regex addition button
							btnAddRegex.setEnabled(false);
						}
					}
				});
				checkboxTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent arg0) {
						File selectedDir = convertISelectionToFile(arg0.getSelection());  

						// Check if directory is checked (if it is - it must be in the new dirs)
						boolean dirIsChecked = listenedDirsNew.containsKey(selectedDir);
						
						// If directory is checked, enable addition of filters -
						// otherwise, disable it
						btnAddRegex.setEnabled(dirIsChecked);
						
						// Clear current list of regular expressions
						lstRegularExpressions.removeAll();
						
						// If directory is checked
						if (dirIsChecked) {
							// Display regexes of listened directory
							Collection<String> regexes =
								((ListenedDirectory)listenedDirsNew.get(selectedDir)).
								getRegularExpressions();
							for (String curRegex : regexes) {
								lstRegularExpressions.add(curRegex);
							}
						}
					}
				});
				
				// For each listened directory
				Collection<ListenedDirectory> listenedDirsValues = listenedDirsOriginal.values();
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
						
						// Enable removal button if anything is selected and it's not
						// the "everything" filter
						btnRemoveRegex.setEnabled(
							(lstRegularExpressions.getSelectionCount() != 0) &&
							!lstRegularExpressions.getSelection()[0].equals(".*"));
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

							// Remove selected regular expressions from hash table
							ListenedDirectory selectedListenedDir =
								listenedDirsNew.get(convertISelectionToFile(checkboxTreeViewer.getSelection()));
							Collection<String> regexesOfSelection = selectedListenedDir.getRegularExpressions();
							for (int curSelIdx : lstRegularExpressions.getSelectionIndices()) {
								regexesOfSelection.remove(lstRegularExpressions.getItem(curSelIdx));
							}
							
							// Remove selected regular expressions from display
							lstRegularExpressions.remove(lstRegularExpressions.getSelectionIndices());
							
							// If no regular expressions are left
							if (lstRegularExpressions.getItemCount() == 0) {
								
								// Add the "everything" filter (display and hash map)
								regexesOfSelection.add(".*");
								lstRegularExpressions.add(".*");
							}
							
							// Disable button (because nothing will be selected)
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
							
							// Get a new regular expression file filter from the user
							RegexInputDialog regexInputDialog =
								new RegexInputDialog(dialogShell, SWT.NONE);
							String newRegex = regexInputDialog.open();

							// If regular expression was not entered
							if (newRegex == null) {
								
								// Stop process
								return;
							}
							
							// Stop process if entered regular expression already exists on the list
							for (String curRegex : lstRegularExpressions.getItems()) {
								
								if (curRegex.equals(newRegex)) {
									
									return;
								}
							}
							
							// Get selected directory
							File selectedDir =
								convertISelectionToFile(checkboxTreeViewer.getSelection());
							
							// If the only filter is the "everything" filter
							Collection<String> newRegexes =
								listenedDirsNew.get(selectedDir).getRegularExpressions();
							if (lstRegularExpressions.getItemCount() == 1 &&
								lstRegularExpressions.getItems()[0].equals(".*")) {
								
								// Remove filter from display
								lstRegularExpressions.remove(0);
								
								// Remove filter from wanted listened directories hash map
								newRegexes.remove(".*");
							}

							// Add new regular expression to displayed list
							lstRegularExpressions.add(newRegex);
							
							// Add new regular expression to wanted listened directory filters
							newRegexes.add(newRegex);
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
						
						// Calculate list of removed listened directories
						Set<File> removedListenedDirs =
							((HashMap<File, ListenedDirectory>)listenedDirsOriginal.clone()).keySet();
						removedListenedDirs.removeAll(listenedDirsNew.keySet());
						
						// Calculate list of added listened directories
						HashMap<File, ListenedDirectory> addedListenedDirs =
							(HashMap<File, ListenedDirectory>) listenedDirsNew.clone();
						addedListenedDirs.keySet().removeAll(listenedDirsOriginal.keySet());

						// Calculate possibly modified listened directories (from intersection of original and new)
						HashMap<File, ListenedDirectory> modifiedListenedDirs =
							(HashMap<File, ListenedDirectory>) listenedDirsNew.clone();
						modifiedListenedDirs.keySet().retainAll(listenedDirsOriginal.keySet());
						
						// Remove listened directories which have no change
						Set<File> unchangedListenedDirs = new HashSet<File>();
						for (ListenedDirectory curListenedDir : modifiedListenedDirs.values()) {
							
							// Get original regular expression filters for directory
							File curDir = curListenedDir.getDirectory();
							Collection<String> origRegexes =
								listenedDirsOriginal.get(curDir).getRegularExpressions();
							Collection<String> newRegexes =
								curListenedDir.getRegularExpressions();

							// If original and new regular expressions are equal sets
							if (origRegexes.containsAll(newRegexes) &&
								newRegexes.containsAll(origRegexes)) {
								
								// Add directory to set of unchanged directories
								unchangedListenedDirs.add(curListenedDir.getDirectory());
							}
						}
						modifiedListenedDirs.keySet().removeAll(unchangedListenedDirs);
						
						// Return changes to listened paths
						pathsDelta = new ListenedPathsDelta(addedListenedDirs.values(),
															removedListenedDirs,
															modifiedListenedDirs.values());

						// Close dialog
						dialogShell.dispose();
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
						
						// Return no changes
						pathsDelta =
							new ListenedPathsDelta(new ArrayList<ListenedDirectory>(0),
												   new ArrayList<File>(0),
												   new ArrayList<ListenedDirectory>(0));
						
						// Close dialog
						dialogShell.dispose();
					}
				});
			}
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(500, 500);
			dialogShell.setMinimumSize(250, 250);
			dialogShell.open();
			dialogShell.addListener(SWT.Close, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					// Fake a click on the cancel button since closing
					// the window is like canceling the action
					btnCancel.getListeners(SWT.Selection)[0].handleEvent(null);
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

		return pathsDelta;
	}
}
