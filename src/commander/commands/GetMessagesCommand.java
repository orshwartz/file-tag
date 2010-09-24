package commander.commands;


/**
 * @author Or Shwartz
 */
public class GetMessagesCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {
		
		// Return the messages
		return getLog().getMessages();
	}

}
