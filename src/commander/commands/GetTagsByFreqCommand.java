package commander.commands;


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

		return getTagRepository().getTagListFreqOrdered();
	}

}
