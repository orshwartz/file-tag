package gui;

import static commander.CommandManager.CmdCodes.TAGGER_GET_FILES_BY_TAGS;
import static commander.CommandManager.CmdCodes.TAGGER_GET_TAGS_BY_FREQ;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import tagger.TagFreq;

import commander.CommandManager;
import commander.commands.TSCommand;

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
 */
public class SearchWindow {

	public CommandManager commander;
	private List lstAvailableTags;
	private List lstExcludedTags;
	private Button btnIncludeTag;
	private Button btnSearch;
	private Label lblSearchResults;
	private Label lblExcludedTags;
	private Label lblAvailableTags;
	private Label lblIncludedTags;
	private List lstResults;
	private Button btnExcludeTag;
	private List lstIncludedTags;

	private static Shell window;
	public SearchWindow(CommandManager commander) {

		this.commander = commander;
	}

	@SuppressWarnings("unchecked")
	public void makeWindow() {

		window = new Shell(MainAppGUI.display, SWT.CLOSE |
											   SWT.TITLE |
											   SWT.MAX |
											   SWT.MIN |
											   SWT.RESIZE);
		GridLayout windowLayout = new GridLayout();
		windowLayout.makeColumnsEqualWidth = false;
		windowLayout.numColumns = 5;
		window.setLayout(windowLayout);
		window.setText("Search");
		{
			lblIncludedTags = new Label(window, SWT.NONE);
			GridData lblIncludedTagsLData = new GridData();
			lblIncludedTagsLData.horizontalSpan = 2;
			lblIncludedTags.setLayoutData(lblIncludedTagsLData);
			lblIncludedTags.setText("Included tags");
		}
		{
			lblAvailableTags = new Label(window, SWT.NONE);
			GridData lblAvailableTagsLData = new GridData();
			lblAvailableTagsLData.horizontalSpan = 2;
			lblAvailableTags.setLayoutData(lblAvailableTagsLData);
			lblAvailableTags.setText("Available tags");
		}
		{
			lblExcludedTags = new Label(window, SWT.NONE);
			GridData lblExcludedTagsLData = new GridData();
			lblExcludedTags.setLayoutData(lblExcludedTagsLData);
			lblExcludedTags.setText("Excluded tags");
		}
		{
			lstIncludedTags = new List(window, SWT.BORDER);
			GridData lstIncludedTagsLData = new GridData();
			lstIncludedTagsLData.verticalAlignment = GridData.FILL;
			lstIncludedTagsLData.grabExcessVerticalSpace = true;
			lstIncludedTagsLData.grabExcessHorizontalSpace = true;
			lstIncludedTagsLData.horizontalAlignment = GridData.FILL;
			lstIncludedTags.setLayoutData(lstIncludedTagsLData);
			lstIncludedTags.setBounds(12, 9, 193, 222);
		}
		{
			btnIncludeTag = new Button(window, SWT.PUSH | SWT.CENTER);
			btnIncludeTag.setText("<< Include");
			GridData btnIncludeTagLData = new GridData();
			btnIncludeTagLData.horizontalAlignment = GridData.CENTER;
			btnIncludeTagLData.widthHint = 69;
			btnIncludeTagLData.heightHint = 30;
			btnIncludeTag.setLayoutData(btnIncludeTagLData);
			btnIncludeTag.setBounds(211, 105, 69, 30);
			btnIncludeTag.setEnabled(false);
			btnIncludeTag.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					
					// Get indices of tag selections
					String[] selTags =
						lstAvailableTags.getSelection();
					
					// For each tag
					for (String curTag : selTags) {
						
						// Add tag to included tags list
						lstIncludedTags.add(new TagFreq(curTag).getTagName());
						
						// Remove tag from available tags list
						lstAvailableTags.remove(curTag);
					}
					
					// Enable search button
					btnSearch.setEnabled(true);
				}
			});
		}
		{
			lstAvailableTags = new List(window, SWT.MULTI | SWT.BORDER);
			GridData lstAvailableTagsLData = new GridData();
			lstAvailableTagsLData.verticalAlignment = GridData.FILL;
			lstAvailableTagsLData.grabExcessVerticalSpace = true;
			lstAvailableTagsLData.grabExcessHorizontalSpace = true;
			lstAvailableTagsLData.horizontalAlignment = GridData.FILL;
			lstAvailableTags.setLayoutData(lstAvailableTagsLData);
			lstAvailableTags.setBounds(288, 9, 193, 222);
			
			// Populate list of available tags, sorted by frequency
			TSCommand getTagsCmd =
				commander.getCommand(TAGGER_GET_TAGS_BY_FREQ);
			Collection<TagFreq> availableTags =
				(Collection<TagFreq>) getTagsCmd.execute(null);
			
			// For each tag frequency
			for (TagFreq tagFreq : availableTags) {
				
				// Add as string to list
				lstAvailableTags.add(tagFreq.toString());
			}
			
			lstAvailableTags.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					// If anything is selected
					if (lstAvailableTags.getSelectionCount() >= 1) {
						
						// Enable inclusion and exclusion buttons
						btnIncludeTag.setEnabled(true);
						btnExcludeTag.setEnabled(true);
					}
				}
			});
		}
		{
			btnExcludeTag = new Button(window, SWT.PUSH | SWT.CENTER);
			btnExcludeTag.setText("Exclude >>");
			GridData btnExcludeTagLData = new GridData();
			btnExcludeTagLData.horizontalAlignment = GridData.CENTER;
			btnExcludeTagLData.widthHint = 69;
			btnExcludeTagLData.heightHint = 30;
			btnExcludeTag.setLayoutData(btnExcludeTagLData);
			btnExcludeTag.setBounds(490, 105, 69, 30);
			btnExcludeTag.setEnabled(false);
			btnExcludeTag.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					
					// Get tag selections
					String[] selTags =
						lstAvailableTags.getSelection();
					
					// For each tag
					for (String curTag : selTags) {
						
						// Add tag to excluded tags list
						lstExcludedTags.add(new TagFreq(curTag).getTagName());
						
						// Remove tag from available tags list
						lstAvailableTags.remove(curTag);
					}
					
					// Enable search button
					btnSearch.setEnabled(true);
				}});
		}
		{
			lstExcludedTags = new List(window, SWT.BORDER);
			GridData lstExcludedTagsLData = new GridData();
			lstExcludedTagsLData.verticalAlignment = GridData.FILL;
			lstExcludedTagsLData.grabExcessVerticalSpace = true;
			lstExcludedTagsLData.grabExcessHorizontalSpace = true;
			lstExcludedTagsLData.horizontalAlignment = GridData.FILL;
			lstExcludedTags.setLayoutData(lstExcludedTagsLData);
			lstExcludedTags.setBounds(568, 7, 193, 227);
		}
		{
			btnSearch = new Button(window, SWT.PUSH | SWT.CENTER);
			btnSearch.setText("Search");
			GridData btnSearchLData = new GridData();
			btnSearchLData.horizontalSpan = 5;
			btnSearchLData.horizontalAlignment = GridData.CENTER;
			btnSearch.setLayoutData(btnSearchLData);
			btnSearch.setBounds(354, 243, 60, 30);
			btnSearch.setEnabled(false);
			btnSearch.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					// Clear previous search results
					lstResults.removeAll();
					
					Object[] params = {Arrays.asList(lstIncludedTags.getItems()),
									   Arrays.asList(lstExcludedTags.getItems())};

					// Get search results
					Collection<String> resultsCollection =
						(Collection<String>)commander.getCommand(TAGGER_GET_FILES_BY_TAGS).
						execute(params);

					// Set results
					for (String curFile : resultsCollection) {
						lstResults.add(curFile);
					}
				}
			});
		}
		{
			lblSearchResults = new Label(window, SWT.NONE);
			GridData lblSearchResultsLData = new GridData();
			lblSearchResultsLData.horizontalSpan = 5;
			lblSearchResults.setLayoutData(lblSearchResultsLData);
			lblSearchResults.setText("Search results");
		}
		{
			lstResults = new List(window, SWT.BORDER);
			GridData lstResultsLData = new GridData();
			lstResultsLData.horizontalSpan = 5;
			lstResultsLData.horizontalAlignment = GridData.FILL;
			lstResultsLData.verticalAlignment = GridData.FILL;
			lstResultsLData.grabExcessVerticalSpace = true;
			lstResultsLData.grabExcessHorizontalSpace = true;
			lstResults.setLayoutData(lstResultsLData);
			lstResults.setBounds(12, 282, 749, 182);
		}

		window.setSize(500, 400);
		window.setMinimumSize(400, 200);
	}

	public void open() {

		makeWindow();
		window.open();
		while (!window.isDisposed()) {
			if (!MainAppGUI.display.readAndDispatch())
				MainAppGUI.display.sleep();
		}
	}
}
