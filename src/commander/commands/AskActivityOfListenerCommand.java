package commander.commands;

/**
 * This command asks the Listener if he is active or not
 * 
 * @author Itay Evron
 *
 */
public class AskActivityOfListenerCommand extends TSCommand{

	@Override
	public Object execute(Object[] params) {

		return getListener().isActive();
	}

}
