package commander.commands;

/**
 * This command deletes all the tags in the system and the
 * file-to-tags attachments
 * @author Itay Evron
 *
 */
public class RebootTaggerCommand extends TSCommand{

	
	@Override
	public Object execute(Object[] params) {
		
		getTagRepository().deleteAll();
		return null;
	}

}
