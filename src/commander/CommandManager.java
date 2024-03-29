package commander;

import static commander.CommandManager.CmdCodes.*;

import gui.MainAppGUI;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import listener.Listener;
import log.Log;
import tagger.TagRepository;

import commander.commands.ActivateListenerCommand;
import commander.commands.AskActivityOfListenerCommand;
import commander.commands.ClearAllTagsCommand;
import commander.commands.ClearLogCommand;
import commander.commands.DeactivateListenerCommand;
import commander.commands.GetAutoTaggersCommand;
import commander.commands.GetFileByTagsCommand;
import commander.commands.GetListenedDirsCommand;
import commander.commands.GetMessagesCommand;
import commander.commands.GetRebootModeCommand;
import commander.commands.GetTagsByFreqCommand;
import commander.commands.GetTagsOfFileCommand;
import commander.commands.ListenToCommand;
import commander.commands.ProcessFileChangeTaggingCommand;
import commander.commands.RebootListenerSourcesCommand;
import commander.commands.SetAutoTaggersCommand;
import commander.commands.SetRebootModeCommand;
import commander.commands.StopListenToCommand;
import commander.commands.TSCommand;
import commander.commands.WriteFileMessageCommand;
import commander.commands.WriteMessageCommand;

/**
 * This class is responsible for holding system commands,
 * whether self-invoked or indirectly-invoked by the user (through UI).
 * @author Or Shwartz, Itay Evron
 */
public class CommandManager implements Observer {
	
	/**
	 * Codes for tagging system commands - used as keys to get concrete
	 * command objects through the <CODE>getCommand</CODE> method.
	 * @author Or Shwartz
	 */
	public enum CmdCodes {
		LOG_WRITE_MESSAGE,
		LOG_WRITE_FILE_MESSAGE,
		LOG_GET_MESSAGES,
		LOG_CLEAR,
		LSTNR_ACTIVATE,
		LSTNR_DEACTIVATE,
		LSTNR_LISTEN_TO,
		LSTNR_STOP_LISTENING_TO,
		LSTNR_GET_LISTENED_DIRS,
		LSTNR_ASK_ACTIVE,
		LSTNR_RETAG_ALL_FILES,
		TAGGER_GET_FILES_BY_TAGS,
		TAGGER_GET_TAGS_BY_FREQ,
		TAGGER_PROCESS_FILE_CHANGE_TAGGING,
		TAGGER_SET_AUTO_TAGGERS,
		TAGGER_GET_AUTO_TAGGERS,
		TAGGER_GET_TAGS_OF_FILE,
		TAGGER_CLEAR_ALL_TAGS,
		TAGGER_SET_REBOOT_MODE,
		TAGGER_GET_REBOOT_MODE,
		TOTAL_COMMAND_CODES
	}
	
	private TagRepository tagRep = null;
	private Log log = null;
	private MainAppGUI gui = null;
	private Listener listener = null;
	private HashMap<CmdCodes, TSCommand> commandMappings = null;
	
	/**
	 * Constructor for the command manager.
	 * @param tagRep Tag repository to control through command manager.
	 * @param listener Listener to control through command manager.
	 * @param log Log to control through command manager.
	 * @param gui GUI to control through command manager.
	 */
	public CommandManager(TagRepository tagRep,
						  Listener listener,
						  Log log,
						  MainAppGUI gui) {
		
		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");

		// Set sub-system components
		setListener(listener);
		setLog(log);
		setGui(gui);
		setTagRepository(tagRep);
		
		// Setup commands for sub-systems
		TSCommand.setCommandedSubsystems(log, listener, tagRep);
		initCmdMappings();
	}

	/**
	 * <B><U>Warning</U></B>: This object is not ready for work. GUI setter needed to
	 * be used prior to working with this object.
	 * @param tagRep The tag repository to command on.
	 * @param listener The listener to command on.
	 * @param log The log to write to.
	 */
	public CommandManager(TagRepository tagRep,
			  			  Listener listener,
			  			  Log log) {
			
		// Set sub-system components
		setListener(listener);
		setLog(log);
		setTagRepository(tagRep);
	
		// Setup commands for sub-systems
		TSCommand.setCommandedSubsystems(log, listener, tagRep);
		initCmdMappings();
		
		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");
	}

	/**
	 * This method sets the mapping of command objects to enumerated codes.
	 */
	private void initCmdMappings() {
		
		commandMappings =
			new HashMap<CmdCodes, TSCommand>(TOTAL_COMMAND_CODES.ordinal());
		
		// Add all the commands to the hashmap
		commandMappings.put(LOG_WRITE_MESSAGE, new WriteMessageCommand());
		commandMappings.put(LOG_WRITE_FILE_MESSAGE, new WriteFileMessageCommand());
		commandMappings.put(LOG_GET_MESSAGES, new GetMessagesCommand());
		commandMappings.put(LOG_CLEAR, new ClearLogCommand());
		commandMappings.put(LSTNR_ACTIVATE, new ActivateListenerCommand());
		commandMappings.put(LSTNR_DEACTIVATE, new DeactivateListenerCommand());
		commandMappings.put(LSTNR_LISTEN_TO, new ListenToCommand());
		commandMappings.put(LSTNR_STOP_LISTENING_TO, new StopListenToCommand());
		commandMappings.put(LSTNR_GET_LISTENED_DIRS, new GetListenedDirsCommand());
		commandMappings.put(LSTNR_ASK_ACTIVE, new AskActivityOfListenerCommand());
		commandMappings.put(LSTNR_RETAG_ALL_FILES, new RebootListenerSourcesCommand());
		commandMappings.put(TAGGER_GET_FILES_BY_TAGS, new GetFileByTagsCommand());
		commandMappings.put(TAGGER_GET_TAGS_BY_FREQ, new GetTagsByFreqCommand());
		commandMappings.put(TAGGER_PROCESS_FILE_CHANGE_TAGGING, new ProcessFileChangeTaggingCommand());
		commandMappings.put(TAGGER_SET_AUTO_TAGGERS, new SetAutoTaggersCommand());
		commandMappings.put(TAGGER_GET_AUTO_TAGGERS, new GetAutoTaggersCommand());
		commandMappings.put(TAGGER_GET_TAGS_OF_FILE, new GetTagsOfFileCommand());
		commandMappings.put(TAGGER_CLEAR_ALL_TAGS, new ClearAllTagsCommand());
		commandMappings.put(TAGGER_SET_REBOOT_MODE, new SetRebootModeCommand());
		commandMappings.put(TAGGER_GET_REBOOT_MODE, new GetRebootModeCommand());
		/* TODO: Add rest of the commands to the hashmap here...
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
	}

	/**
	 * Receives <CODE>command</CODE> to execute and its <CODE>parameters</CODE>
	 * and runs it. The result is returned.
	 * @param command code to execute.
	 * @param parameters for the command as an object array, for generality.
	 * One should read the actual subsystem commands to know the signature.
	 */
	public Object runCommand(CmdCodes command, Object[] parameters){
		
		return commandMappings.get(command).execute(parameters);
	}

	/**
	 * This should be called when there's an update to a file in some directory.
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable object, Object arg) {

		//pass this date to the log
		Object[] params = new Object[] {arg};
		getCommand(LOG_WRITE_FILE_MESSAGE).execute(params);
		
		
		// Pass this data to the tagger
		//Object[] params = new Object[] {arg};
		getCommand(TAGGER_PROCESS_FILE_CHANGE_TAGGING).execute(params);
	}

	/**
	 * @param code of the requested command
	 * @return
	 */
	public TSCommand getCommand(CmdCodes code) {
		
		// Return command object matching the code
		return commandMappings.get(code);
	}
	
	/**
	 * @param tagRep The tag repository to set.
	 */
	public void setTagRepository(TagRepository tagRep) {

		this.tagRep = tagRep;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(Log log) {
	
		this.log = log;
	}

	/**
	 * @param gui the gui to set
	 */
	public void setGui(MainAppGUI gui) {
	
		this.gui = gui;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(Listener listener) {
	
		this.listener = listener;
	}
}
