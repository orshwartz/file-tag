package commander.commands;

public class GetTagsOfFileCommand extends TSCommand {

	@Override
	public Object execute(Object[] params) {

		return getTagRepository().getTagsOfFile((String)params[0]);
		
	}

}
