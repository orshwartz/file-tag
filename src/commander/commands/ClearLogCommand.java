package commander.commands;

public class ClearLogCommand extends TSCommand {

	@Override
	public Object execute(Object[] params) {
		getLog().clearLog();
		return null;
	}

}
