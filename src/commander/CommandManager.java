package commander;

import gui.MainAppGUI;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import listener.Listener;
import log.Log;
import tagger.TagRepository;

import commander.commands.ActivateListenerCommand;
import commander.commands.DeactivateListenerCommand;
import commander.commands.GetFileByTagsCommand;
import commander.commands.GetFreqOfOneTagCommand;
import commander.commands.GetMessagesCommand;
import commander.commands.GetTagsByFreqCommand;
import commander.commands.ListenToCommand;
import commander.commands.StopListenToCommand;
import commander.commands.TSCommand;
import commander.commands.TagFileCommand;
import commander.commands.WriteMessageCommand;

import static commander.CommandManager.CmdCodes.*;

/**
 * This class is responsible for holding system commands,
 * whether self-invoked or indirectly-invoked by the user (through UI).
 * @author Or Shwartz, Itay Evron
 */
public class CommandManager implements Observer {
	
	public enum CmdCodes {
		LOG_WRITE_MESSAGE,
		LOG_GET_MESSAGES,
		LSTNR_ACTIVATE,
		LSTNR_DEACTIVATE,
		LSTNR_LISTEN_TO,
		LSTNR_STOP_LISTENING_TO,
		TAGGER_GET_FILES_BY_TAGS,
		TAGGER_GET_TAGS_BY_FREQ,
		TAGGER_TAG_FILE,
		TAGGER_GET_FREQ_OF_ONE_TAG,
		TOTAL_COMMAND_CODES
	}
	
	private TagRepository tagRep = null;
	private Log log = null;
	private MainAppGUI gui = null;
	private Listener listener = null;
	private HashMap<CmdCodes, TSCommand> commandMappings = null;
	
	/**
	 * @param tagRep
	 * @param listener
	 * @param log
	 * @param gui
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
	
		// TODO: Remove this stub message
		System.out.println(this.getClass().getName() + " up.");
		
		// Set sub-system components
		setListener(listener);
		setLog(log);
		setTagRepository(tagRep);
	
		// Setup commands for sub-systems
		TSCommand.setCommandedSubsystems(log, listener, tagRep);
		initCmdMappings();
	}

	/**
	 * This method sets the mapping of command objects to enumerated codes.
	 */
	private void initCmdMappings() {
		
		commandMappings =
			new HashMap<CmdCodes, TSCommand>(TOTAL_COMMAND_CODES.ordinal());
		
		// Add all the commands to the hashmap
		commandMappings.put(LOG_WRITE_MESSAGE,
							new WriteMessageCommand());
		commandMappings.put(LOG_GET_MESSAGES,
							new GetMessagesCommand());
		commandMappings.put(LSTNR_ACTIVATE,
							new ActivateListenerCommand());
		commandMappings.put(LSTNR_DEACTIVATE,
							new DeactivateListenerCommand());
		commandMappings.put(LSTNR_LISTEN_TO,
							new ListenToCommand());
		commandMappings.put(LSTNR_STOP_LISTENING_TO,
							new StopListenToCommand());
		commandMappings.put(TAGGER_GET_FILES_BY_TAGS,
							new GetFileByTagsCommand());
		commandMappings.put(TAGGER_GET_TAGS_BY_FREQ,
							new GetTagsByFreqCommand());
		commandMappings.put(TAGGER_GET_FREQ_OF_ONE_TAG,
				new GetFreqOfOneTagCommand());
		
		commandMappings.put(TAGGER_TAG_FILE,
							new TagFileCommand());
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
		
		// TODO: Remove this debug message
		System.out.printf("DEBUG DEBUG DEBUG *** %d/%d (Mapped/Codes) *** DEBUG DEBUG DEBUG\n",
						  commandMappings.size(),
						  TOTAL_COMMAND_CODES.ordinal());
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
	 * This should be called when there is a file addition which
	 * needs to be tagged, but because the file is new the tagger needs
	 * more information from the user.
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable object, Object arg) {

		// TODO : Invoke GUI command for new file to tag (TBD)		
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
