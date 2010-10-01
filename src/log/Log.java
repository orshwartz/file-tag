package log;

import java.util.Collection;

import listener.FileEvent;

/**
 * @author Or Shwartz
 *
 */
public interface Log {

	/*
	 * We need getLog and write.
	 * We might need some more options for log creations, maybe
	 * option to set a format, maybe getting log by recent activities,
	 * maybe filter by activity type (creation of file, deletion,
	 * rename, etc...).
	 */
	public void writeMessage(Object obj);
	public Collection<String> getMessages();
	public void clearLog();
}
