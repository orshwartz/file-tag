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
	Renamed;
}
