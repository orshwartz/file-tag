/**
 * 
 */
package commander;

/**
 * @author Or Shwartz
 *
 */
public class DeactivateListenerCommand implements Command {

	/**
	 * @see commander.Command#execute()
	 */
	@Override
	public void execute() {
		System.out.println("Listener is off");

	}

}
