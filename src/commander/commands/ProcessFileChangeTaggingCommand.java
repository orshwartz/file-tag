package commander.commands;

import java.util.Collection;

import listener.FileEvent;
import listener.FileEvents;

/**
 * @author Or Shwartz
 *
 */
public class ProcessFileChangeTaggingCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {

		FileEvent fileEvent = (FileEvent) params[0];
		
		// Process required tagging changes for file change event
		Collection<String> tags =
			getTagRepository().processFileChangeTagging(fileEvent);
		
		if (tags != null) {
			for (String curTag : tags) {
				getLog().writeMessage(fileEvent.getFile().toAbsolutePath().toString() + " tagged " + curTag);
			}
		} else if (fileEvent.getEvent() == FileEvents.DELETED) {
			getLog().writeMessage(fileEvent.getFile().toAbsolutePath().toString() + " deleted.");
		}
		
		return null;
	}
}
