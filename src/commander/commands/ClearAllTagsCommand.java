package commander.commands;

/**
 * This command deletes all the tags in the system and the
 * file-to-tags attachments
 * @author Itay Evron
 *
 */
public class ClearAllTagsCommand extends TSCommand {

	
	@Override
	public Object execute(Object[] params) {
		
		getTagRepository().deleteAll();
		
		getLog().writeMessage("Cleared all tags.");
		
		return null;
	}

}
