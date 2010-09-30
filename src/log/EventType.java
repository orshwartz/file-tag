package log;

/**
 * This enumeration denotes possible events for watched files.
 * @author Or Shwartz, Itay Evron
 */
public enum EventType {

	Created,
	Deleted,
	Modified,
	Lstnr_Act,
	Lstnr_Deact,
	Tagger_Reboot,
	Error_Plugin_Creation,
	Moved,
	Renamed,
	System_Up;
	
	public String toString(){
		
		switch(this){
		 	case System_Up :
		 		return "System Up";
		
		 	case Lstnr_Act :
		 		return "Listener Activated";
			
		 	case Lstnr_Deact :
		 		return "Listener Deactivated";
		 	
		 	case Error_Plugin_Creation :
				return "Error occurred during creation of plugins directory.";
			
		 	case Tagger_Reboot :
		 		return "Tagger Reboot";
		}
		return null;
		
	}
}
