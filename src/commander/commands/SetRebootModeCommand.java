package commander.commands;

public class SetRebootModeCommand extends TSCommand{

	@Override
	public Object execute(Object[] params) {
		
		getTagRepository().setRebootMode();
		return null;
	}

}
