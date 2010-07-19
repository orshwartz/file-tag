package commander.commands;

import java.util.ArrayList;
import java.util.Collection;

import commander.commands.TSCommand;

/**
 * @author Or Shwartz
 *
 */
public class GetTagsByFreqCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 * @see tagger.TagRepository#getTagListFreqOrdered()
	 */
	@Override
	public Object execute(Object[] params) {

		//return getTagRepository().getTagListFreqOrdered();
		
		
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("argo");
		tags.add("beagle");
		tags.add("plasma");
		tags.add("argono");
		tags.add("plastic");
		return tags;
	}

}
