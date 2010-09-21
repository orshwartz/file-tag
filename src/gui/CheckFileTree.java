package gui;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * This class demonstrates the CheckboxTreeViewer
 */
public class CheckFileTree extends FileTree {
	
	CheckboxTreeViewer tv = null;
	
	/**
	 * Configures the shell
	 * 
	 * @param shell
	 *            the shell
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Listened directory selector");
	}

	/**
	 * Creates the main window's contents
	 * 
	 * @param parent
	 *            the main window
	 * @return Control
	 */
	protected Control createContents(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		// Create the tree viewer to display the directory tree
		tv = new CheckboxTreeViewer(composite);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.setContentProvider(new FileTreeContentProvider());
		tv.setLabelProvider(new FileTreeLabelProvider());
		tv.setInput("root"); // pass a non-null that will be ignored

		// When user checks a checkbox in the tree, check all its children
		// and vice versa
		tv.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				// If the item is checked...
				if (event.getChecked()) {
					// ...check all its children
					tv.setSubtreeChecked(event.getElement(), true);
				}
				// Else, item is unchecked so...
				else {
					// ...uncheck all its children
					tv.setSubtreeChecked(event.getElement(), false);					
				}
			}
		});
		return composite;
	}
	
	public String[] getChosenPaths () {
		
		return (String[]) tv.getCheckedElements();
	}

	/**
	 * The application entry point
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		
		CheckFileTree cft = new CheckFileTree();
		cft.run();
		System.out.println(cft.getChosenPaths());
		cft.dispose();
	}
}