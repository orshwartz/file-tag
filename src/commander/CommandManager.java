package commander;

import gui.MainAppGUI;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import listener.Listener;
import log.Log;
import tagger.TagRepository;

/**
 * This class is responsible for holding system commands,
 * whether self-invoked or indirectly-invoked by the user (through UI).
 * @author Or Shwartz, Itay Evron
 */
public class CommandManager implements Observer {
	
	public enum CmdCodes {
		LOG_WRITE_MESSAGE,
		LOG_GET_MESSAGES,
		GUI_TAG_NEW_FILE,
		LSTNR_ACTIVATE,
		LSTNR_DEACTIVATE,
		LSTNR_LISTEN_TO,
		LSTNR_STOP_LISTENING_TO
	}
	
	private TagRepository tagRep = null;
	private Log log = null;
	private MainAppGUI gui = null;
	private Listener listener = null;
	private HashMap<CmdCodes, Command> commandMappings = null;
	
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
		
		initCmdMappings();
	}

	/**
	 * 
	 */
	private void initCmdMappings() {
		
		commandMappings = new HashMap<CmdCodes, Command>();
		
		// Add all the commands to the hashmap
		commandMappings.put(CmdCodes.LOG_WRITE_MESSAGE,
							new WriteMessageCommand());
		commandMappings.put(CmdCodes.GUI_TAG_NEW_FILE,
							new RequestTagCommand());
		commandMappings.put(CmdCodes.LSTNR_ACTIVATE,
							new ActivateListenerCommand());
		commandMappings.put(CmdCodes.LSTNR_ACTIVATE,
							new DeactivateListenerCommand());
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

		initCmdMappings();
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
	public Command getCommand(CmdCodes code) {
		
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
