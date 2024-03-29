package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides the labels for the file tree
 */
class FileTreeLabelProvider implements ILabelProvider {
	
	// The listeners
	private List<ILabelProviderListener> listeners;

	// Images for tree nodes
	private Image file;

	private Image folder;

	/**
	 * Constructs a FileTreeLabelProvider
	 */
	public FileTreeLabelProvider() {
		
		// Create the list to hold the listeners
		listeners = new ArrayList<ILabelProviderListener>();

		// Create the images
		try {
			file = new Image(null, new FileInputStream("res/file.gif"));
			folder = new Image(null, new FileInputStream("res/directory.gif"));
			
		} catch (FileNotFoundException e) {
			// Swallow it; we'll do without images
		}
	}

	/**
	 * Sets the preserve case attribute
	 * 
	 * @param preserveCase
	 *            the preserve case attribute
	 */
	public void setPreserveCase() {

		// Since this attribute affects how the labels are computed,
		// notify all the listeners of the change.
		LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			ILabelProviderListener ilpl = (ILabelProviderListener) listeners
					.get(i);
			ilpl.labelProviderChanged(event);
		}
	}

	/**
	 * Gets the image to display for a node in the tree
	 * 
	 * @param arg0
	 *            the node
	 * @return Image
	 */
	@Override
	public Image getImage(Object arg0) {
		// If the node represents a directory, return the directory image.
		// Otherwise, return the file image.
		return ((File) arg0).isDirectory() ? folder : file;
	}

	/**
	 * Gets the text to display for a node in the tree
	 * 
	 * @param arg0
	 *            the node
	 * @return String
	 */
	@Override
	public String getText(Object arg0) {
		// Get the name of the file
		String text = ((File) arg0).getName();

		// If name is blank, get the path
		if (text.length() == 0) {
			text = ((File) arg0).getPath();
		}

		// Check the case settings before returning the text
		return text;
	}

	/**
	 * Adds a listener to this label provider
	 * 
	 * @param arg0
	 *            the listener
	 */
	@Override
	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);
	}

	/**
	 * Called when this LabelProvider is being disposed
	 */
	@Override
	public void dispose() {
		// Dispose the images
		if (folder != null)
			folder.dispose();
		if (file != null)
			file.dispose();
	}

	/**
	 * Returns whether changes to the specified property on the specified
	 * element would affect the label for the element
	 * 
	 * @param arg0
	 *            the element
	 * @param arg1
	 *            the property
	 * @return boolean
	 */
	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	/**
	 * Removes the listener
	 * 
	 * @param arg0
	 *            the listener to remove
	 */
	@Override
	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);
	}
}