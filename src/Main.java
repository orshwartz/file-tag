import gui.MainAppGUI;
import listener.Listener;
import log.Log;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import tagger.TagRepository;
import web.WebServer;

import commander.CommandManager;

/**
 * Main composition of subsystems for file tagging application.
 * @author Or Shwartz
 */
public class Main {
	
	public static String SUBSYS_DEPENDS_FILENAME = "subsys_depends.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BeanFactory beanFactory =
			new XmlBeanFactory(new ClassPathResource(SUBSYS_DEPENDS_FILENAME));
		
		Log log = null;
		MainAppGUI gui = null;
		TagRepository tagRep = null;
		Listener listener = null;
		CommandManager commander = null;
		WebServer webServer = null;

		// Create subsystems as singletons using Spring Framework for
		// dependency injection
		log = (Log)beanFactory.getBean("log");
		tagRep = (TagRepository)beanFactory.getBean("tagger");
		listener = (Listener)beanFactory.getBean("listener");
		commander = (CommandManager)beanFactory.getBean("commander");
		gui = (MainAppGUI)beanFactory.getBean("gui");
		webServer = (WebServer)beanFactory.getBean("web");
		
		// Set GUI for commander
		commander.setGui(gui);

		// Register commander as observer for file change issues (it is the
		// "controller" of the application and will know what to do)
		listener.addObserver(commander);
		
		// Activate the listener TODO: Consider doing this based on a saved setting
		listener.activate();
				
		try {
			// Start the web server
			webServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gui.displayGUI();
		
		// Close the listener and the webserver
		listener.close();
		webServer.close();
	}
}
