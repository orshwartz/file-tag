package commander.commands;

import java.util.Collection;

import tagger.TagRepository;

import commander.commands.TSCommand;

/**
 * @author Or Shwartz
 */
public class GetFileByTagsCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 * @see tagger.TagRepository#searchByTag(Collection, Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] params) {

		return
			getTagRepository().searchByTag((Collection<String>) params[0],
										   (Collection<String>) params[1]);
	}

}
