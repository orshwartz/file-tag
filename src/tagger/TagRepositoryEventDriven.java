/**
 * 
 */
package tagger;

import java.util.Observable;
import java.util.Observer;

/**
 * This abstract class represents a tag repository capable of receiving
 * events (of file changes, e.g.) and in case inability to handle an event
 * (such as a new file addition) pass it on to someone else to take care of
 * (GUI for tagging, e.g.).
 * @author Or Shwartz
 */
public abstract class TagRepositoryEventDriven extends Observable
											   implements TagRepository,
											   			  Observer {

}
