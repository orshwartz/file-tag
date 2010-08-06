package gui;

import static commander.CommandManager.CmdCodes.TAGGER_GET_FILES_BY_TAGS;
import static commander.CommandManager.CmdCodes.TAGGER_GET_TAGS_BY_FREQ;
import static commander.CommandManager.CmdCodes.TAGGER_GET_FREQ_OF_ONE_TAG;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
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
	private List lstResults;
	private Button btnExcludeTag;
	private List lstIncludedTags;
	
	private Button btnReturnLeft;
	private Button btnReturnRight;

	private static Shell window;
	public SearchWindow(CommandManager commander) {

		this.commander = commander;
	}

	@SuppressWarnings("unchecked")
	public void makeWindow() {

		window = new Shell(MainAppGUI.display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		window.setText("Search");
		{
			lstAvailableTags = new List(window, SWT.MULTI);
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
			btnIncludeTag.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					
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
				}
			});
		}
		{
			btnExcludeTag = new Button(window, SWT.PUSH | SWT.CENTER);
			btnExcludeTag.setText("Exclude >>");
			btnExcludeTag.setBounds(490, 105, 69, 30);
			btnExcludeTag.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					
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
				}});
		}
		{
			btnSearch = new Button(window, SWT.PUSH | SWT.CENTER);
			btnSearch.setText("Search");
			btnSearch.setBounds(354, 243, 60, 30);
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
			btnReturnLeft = new Button(window, SWT.PUSH | SWT.CENTER);
			btnReturnLeft.setText("Remove >>");
			btnReturnLeft.setBounds(74, 243, 70, 30);
			btnReturnLeft.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					
					String[] tag =
						lstIncludedTags.getSelection();
					
					
					if (tag.length > 0 ){	
						// Add tag to included tags list
						lstAvailableTags.add(commander.getCommand(TAGGER_GET_FREQ_OF_ONE_TAG).execute(tag)
								.toString());
						
						// Remove tag from available tags list
						lstIncludedTags.remove(tag[0]);
					}
					
				}
			
			});
		}
		{
			btnReturnRight = new Button(window, SWT.PUSH | SWT.CENTER);
			btnReturnRight.setText("<< Remove");
			btnReturnRight.setBounds(634, 243, 70, 30);
			btnReturnRight.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					
					String[] tag =
						lstExcludedTags.getSelection();
					
					
					if(	tag.length > 0) {	
						// Add tag to included tags list
					lstAvailableTags.add(commander.getCommand(TAGGER_GET_FREQ_OF_ONE_TAG).execute(tag)
							.toString());
					
						// Remove tag from available tags list
						lstExcludedTags.remove(tag[0]);
					}
					
				}
			
			});
			
			
		}
		
		
		{
			lstResults = new List(window, SWT.NONE);
			lstResults.setBounds(12, 282, 749, 182);
		}
	}

	public void open() {

		makeWindow();
		window.open();
		while (!window.isDisposed()) {
			if (!MainAppGUI.display.readAndDispatch())
				MainAppGUI.display.sleep();
		}
		//pack();
	}
}
