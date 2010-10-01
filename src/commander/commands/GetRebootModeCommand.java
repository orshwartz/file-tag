package commander.commands;

public class GetRebootModeCommand extends TSCommand{

	@Override
	public Object execute(Object[] params) {
		return getTagRepository().getRebootMode();
	}

}
