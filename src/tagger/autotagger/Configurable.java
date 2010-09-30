package tagger.autotagger;

import java.io.Serializable;

/**
 * A class implementing this should enable the configuration of an algorithm by
 * displaying a GUI. 
 * @author Or Shwartz
 */
public interface Configurable extends Serializable {

	/**
	 * Displays the configuration GUI.
	 * @throws Exception In case a problem occurred.
	 */
	void showConfigurationGUI() throws Exception;
}
