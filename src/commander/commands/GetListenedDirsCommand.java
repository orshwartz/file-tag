package commander.commands;

/**
 * This command returns a copy of the paths being listened to, and
 * the regular expression file filters for these directories.
 * @author Or Shwartz
 */
public class GetListenedDirsCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {

		// Returned the listened paths
		return getListener().getListenedPaths();
	}

}
