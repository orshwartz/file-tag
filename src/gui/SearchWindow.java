package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

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
 * @author Itay Evron
 * 
 */
public class SearchWindow {

	public CommandManager commander;
	private List lstAvailableTags;
	private List lstExcludedTags;
	private Button btnIncludeTag;
	private Button btnSearch;
	private List lstResults;
	private Button btnExclude;
	private List lstIncludedTags;

	private static Shell window;
	private static Group group;

	public SearchWindow(CommandManager commander) {

		this.commander = commander;
	}

	public void makeWindow() {

		window = new Shell(MainAppGUI.display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		window.setText("Search");
		{
			lstAvailableTags = new List(window, SWT.NONE);
			lstAvailableTags.setBounds(288, 9, 193, 222);
		}
		{
			lstIncludedTags = new List(window, SWT.NONE);
			lstIncludedTags.setBounds(12, 9, 193, 222);
		}
		{
			lstExcludedTags = new List(window, SWT.NONE);
			lstExcludedTags.setBounds(568, 7, 193, 227);
		}
		{
			btnIncludeTag = new Button(window, SWT.PUSH | SWT.CENTER);
			btnIncludeTag.setText("<< Include");
			btnIncludeTag.setBounds(211, 105, 69, 30);
		}
		{
			btnExclude = new Button(window, SWT.PUSH | SWT.CENTER);
			btnExclude.setText("Exclude >>");
			btnExclude.setBounds(490, 105, 69, 30);
		}
		{
			btnSearch = new Button(window, SWT.PUSH | SWT.CENTER);
			btnSearch.setText("Search");
			btnSearch.setBounds(354, 243, 60, 30);
		}
		{
			lstResults = new List(window, SWT.NONE);
			lstResults.setBounds(12, 282, 749, 182);
		}
	}

	public void open() {

		window.open();
		while (!window.isDisposed()) {
			if (!MainAppGUI.display.readAndDispatch())
				MainAppGUI.display.sleep();
		}
		//pack();
	}
}
