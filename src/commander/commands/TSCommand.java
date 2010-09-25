package commander.commands;

import listener.Listener;
import log.Log;
import tagger.TagRepository;

/**
 * Abstract class for a simple tagging system command object.
 * 
 * This is an abstract class and not an interface in order to share the
 * references to the subsystems amongst all commands.
 * 
 * @author Itay Evron
 */
public abstract class TSCommand {

	private static Log log = null;
	private static Listener listener = null;
	private static TagRepository tagRepository = null;
	
	/**
	 * Setter for subsystems used for commands.
	 * @param log
	 * @param listener
	 * @param tagRepository
	 */
	public static void setCommandedSubsystems(Log log,
											  Listener listener,
											  TagRepository tagRepository) {
		
		TSCommand.log = log;
		TSCommand.listener = listener;
		TSCommand.tagRepository = tagRepository;
	}
	
	/**
	 * This executes the actual command encapsulated by this <CODE>TSCommand</CODE>
	 * object. One should check the documentation for the actual signature of the
	 * command. Also, all extenders of this class are strongly advised to document
	 * the signature using the JavaDoc for the execute command.
	 * @param params Parameters for the actual command.
	 * @return the return value of the actual command. If actual command has no
	 * return value, this should be null and either way cannot be counted on.
	 */
	public abstract Object execute(Object[] params);

	/**
	 * @return the log
	 */
	protected static Log getLog() {

		return TSCommand.log;
	}

	/**
	 * @return the listener
	 */
	protected static Listener getListener() {
		
		return TSCommand.listener;
	}

	/**
	 * @return the tagger
	 */
	protected static TagRepository getTagRepository() {
		
		return TSCommand.tagRepository;
	}
}
