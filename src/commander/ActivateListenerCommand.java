/**
 * 
 */
package commander;

/**
 * @author Or Shwartz, Itay Evron
 *
 */
public class ActivateListenerCommand implements Command {

	/**
	 * @see commander.Command#execute()
	 */
	@Override
	public void execute() {
		System.out.println("listener is on");

	}

}
