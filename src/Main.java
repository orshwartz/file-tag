import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;


import gui.MainAppGUI;
import listener.Listener;
import log.Log;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import tagger.FileNotTaggedException;
import tagger.TagAlreadyExistsException;
import tagger.TagNotFoundException;
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
		
		
		/*Collection<String> col = new LinkedList();
		Collection<String> col2 = new LinkedList();
		Collection<String> col3 = new LinkedList();
		Collection<String> col4 = new LinkedList();
		Collection<String> col5 = new LinkedList();
		
		col.add("gomel513");
		col.add("kzomel513");
		col.add("shmip315");
		col.add("shmop315");
		
		col2.add("zz");
		col2.add("dd");
		col2.add("aa");
		col2.add("bb");
		
		col3.add("gomel513");
		col3.add("kzomel513");
		col3.add("aa");
		col3.add("zz");
		
		col4.add("shmop315");
		col4.add("zz");
		col4.add("dd");
		
		col5.add("dd");
		try {
			tagRep.addTag(col);
			tagRep.addTag(col2);
		} catch (TagAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tagRep.searchByTag(col3, col4);*/

		//tagRep.DropTables();
		
		gui.displayGUI();
	}
}
