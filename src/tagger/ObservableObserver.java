package tagger;

import java.util.Observable;
import java.util.Observer;

/**
 * This is an observable-observer - useful for observers that sometimes
 * can't handle a certain event and need to pass it on to some other class
 * to take care of.
 * 
 * In our case, useful for a tag repository receiving a file change event of
 * type "new file" which it may not be able to handle alone and so needs to turn
 * to the user for directions.
 * @author Or Shwartz
 */
public abstract class ObservableObserver extends Observable implements Observer {

}
