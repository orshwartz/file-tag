package commander.commands;

import java.io.FileNotFoundException;
import java.util.Collection;

import tagger.TagRepository;
import commander.commands.TSCommand;

/**
 * @author Or Shwartz
 *
 */
public class TagFileCommand extends TSCommand {

	/**
	 * @see commander.commands.TSCommand#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] params) {

		try {
			getTagRepository().tagFile((String)params[0],
									   (Collection<String>)params[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
