package log;

/**
 * This enumeration denotes possible events for watched files.
 * @author Or Shwartz, Itay Evron
 */
public enum EventType {

	Created,
	Deleted,
	Modified,
	Moved,
	Renamed;
}
