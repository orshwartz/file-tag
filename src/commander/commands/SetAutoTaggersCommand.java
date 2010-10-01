package commander.commands;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import tagger.autotagger.AutoTagger;

/**
 * @author Or Shwartz
 */
public class SetAutoTaggersCommand extends TSCommand {

	/**
	 * @see tagger.TagRepository#setAutoTaggers(Collection)
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] params) {

		Map<File,AutoTagger> autoTaggersToSet =
			(Map<File, AutoTagger>) params[0];

		// Set requested automatic taggers 
		getTagRepository().setAutoTaggers(autoTaggersToSet);
		
		return null;
	}

}
