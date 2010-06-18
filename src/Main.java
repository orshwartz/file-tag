import gui.MainAppGUI;
import listener.Listener;
import log.Log;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import tagger.TagAlreadyExistsException;
import tagger.TagRepositoryEventDriven;

import commander.CommandManager;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BeanFactory beanFactory =
			new XmlBeanFactory(new ClassPathResource("subsys_depends.xml"));
		
		Log log = null;
		MainAppGUI gui = null;
		TagRepositoryEventDriven tagRep = null;
		Listener listener = null;
		CommandManager commander = null;

		// Create subsystems using Spring Framework for dependency injection
		log = (Log)beanFactory.getBean("log");
		tagRep = (TagRepositoryEventDriven)beanFactory.getBean("tagger");
		listener = (Listener)beanFactory.getBean("listener");
		commander = (CommandManager)beanFactory.getBean("commander");
		gui = (MainAppGUI)beanFactory.getBean("gui");
		
		// Set GUI for commander
		commander.setGui(gui);

		// Register tag repository as observer for file changes
		// and commander as observer for unknown tagging issues
		listener.addObserver(tagRep);
		tagRep.addObserver(commander);
		
		// Show the GUI to the user
		
		gui.displayGUI();
	}
}
