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
		
		
		Collection<String> col = new LinkedList();
		Collection<String> col2 = new LinkedList();
		Collection<String> col3 = new LinkedList();
		Collection<String> col4 = new LinkedList();
		Collection<String> col5 = new LinkedList();
		Collection<String> files = new LinkedList();
		
		col.add("gomel");
		col.add("kzomel");
		col.add("shmip");
		col.add("shmop");
		
		col2.add("zz");
		col2.add("dd");
		col2.add("aa");
		col2.add("bb");
		
		col3.add("gomel");
		col3.add("kzomel");
		col3.add("aa");
		col3.add("zz");
		
		col4.add("gomel");
		col4.add("zz");
	
		col5.add("bb");
		col5.add("aa");
		
			try {
				tagRep.tagFile("file1",col);
				tagRep.tagFile("file2",col2);
				tagRep.tagFile("file3",col3);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//files = tagRep.searchByTag(col4, col5);
			tagRep.getTagListFreqOrdered();
		//tagRep.DropTables();
		
		
		for ( String curFile : files){
			System.out.println("outcome = " + curFile);
		}
		
		gui.displayGUI();
		
		//log.clearLog();
		//log.writeMessage("3");
		//log.writeMessage("2");
		//log.writeMessage("1");
		//System.out.println(log.getMessages());;
	}
}
