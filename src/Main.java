import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import commander.CommandManager;

import gui.MainAppGUI;
import tagger.TagRepositoryEventDriven;
import tagger.TagRepositoryEventDrivenImpl;
import listener.Listener;
import listener.ListenerImpl;
import log.Log;

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

	/* TBD create LogImpl through spring */
		log = (Log)beanFactory.getBean("log");
		//tagRep = new TagRepositoryEventDrivenImpl();
		tagRep = (TagRepositoryEventDriven)beanFactory.getBean("tagger");
		//listener = new ListenerImpl();
		listener = (Listener)beanFactory.getBean("listener");
		//commander = new CommandManager(tagRep, listener, log);
		commander = (CommandManager)beanFactory.getBean("commander");
		//gui = new MainAppGUI(commander);
		gui = (MainAppGUI)beanFactory.getBean("gui");

		tagRep.addObserver(commander);
		listener.addObserver(tagRep);
		
		gui.displayGUI();
	}
}
