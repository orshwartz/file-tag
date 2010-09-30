package commander.commands;

import java.util.Collection;

/**
 * @author Or Shwartz
 */
public class GetAutoTaggersCommand extends TSCommand {

	/**
	 * @see tagger.TagRepository#getAutoTaggers(Collection)
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {

		// Return the requested automatic taggers 
		return getTagRepository().getAutoTaggers();
	}

}
