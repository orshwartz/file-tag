package commander.commands;

import listener.FileEvent;

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

		// Process required tagging changes for file change event
		getTagRepository().processFileChangeTagging((FileEvent) params[0]);
		
		return null;
	}
}
