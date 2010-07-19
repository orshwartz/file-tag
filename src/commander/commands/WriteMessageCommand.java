/**
 * 
 */
package commander.commands;


/**
 * This command writes a certain message to the log.
 * @author Or Shwartz
 */
public class WriteMessageCommand extends TSCommand {

	/**
	 * @see commander.commands.Command#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] params) {
		
		// Write a message to the log
		//log.LogImpl.getInstance().writeMessage(params[0].toString());
		
		getLog().writeMessage((String)params[0]);
		
		return null;
	}
}
