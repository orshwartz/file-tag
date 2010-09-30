import gui.MainAppGUI;
import listener.Listener;
import log.Log;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import tagger.TagRepository;
import web.WebServer;

import commander.CommandManager;

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
//		listener.addObserver(tagRep); TODO: Remove this line if everything's OK
		listener.addObserver(commander);
		//tagRep.getSignal().addObserver(commander);
		
		
//		Collection<AutoTagger> autoTaggers = new ArrayList<AutoTagger>();
//		try {
//			autoTaggers.add(AutoTaggerLoader.getAutoTagger(new File("c:/temp/TaggerBySize.class")));
//			autoTaggers.add(AutoTaggerLoader.getAutoTagger(new File("c:/temp/TaggerByASCIIContents.class")));
//			autoTaggers.add(AutoTaggerLoader.getAutoTagger(new File("c:/temp/TaggerByPathKeywords.class")));
//			autoTaggers.add(AutoTaggerLoader.getAutoTagger(new File("c:/temp/TaggerByMetadata.class")));
//			
//			((TagRepositoryEventDrivenImpl)tagRep).setAutoTaggers(autoTaggers);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			ArrayList<String> filters = new ArrayList<String>();
//			filters.add(".*\\.txt"); //wildcard: *.txt
//			filters.add(".*cool.*"); //wildcard: *cool* 
//			listener.listenTo(new ListenedDirectory(new File("c:\\temp"), filters));
//		} catch (NotDirectoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
		
		// Activate the listener TODO: Consider doing this based on a saved setting
		listener.activate();
		
//		// How to copy to the clipboard XXX: This is for using from search results (copy file location)
//		StringSelection strSelection = new StringSelection("C:\\bla.txt");

//		//////// For "locate on disk" option on search results ////////
//		for (Entry<Object, Object> curEntry : System.getProperties().entrySet()) {
//			System.out.println("Key: " + curEntry.getKey() + "\t\tValue: " + curEntry.getValue());
//		}
//		
//		if (System.getProperty("sun.desktop").equals("windows")) {
//			try {
//				Runtime.getRuntime().exec("explorer /n, /select,c:\\windows\\notepad.exe");
//			} catch (IOException e) {
//				// TODO: Either remove the option from pop-up menu or let user know
//			}
//		}

//		Class<AutoTagger> c = null;
//		File f=new File("c:\\temp");// some folder's path like "D:\\myClasses\\"
//		URL url = null;
//		try {
//			url = f.toURI().toURL();
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}	// --> "file:/D:/myClasses/..."
//		URL[] urls=new URL[]{url};
//		// create a new class loader for this directory
//		ClassLoader cl=new URLClassLoader(urls);
//		// load the class file "MyAlgo.class"
//		try {
//			c = (Class<AutoTagger>)cl.loadClass("tagger.autotagger.TaggerBySize");
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		AutoTagger a = null;
//		try {
//			a = c.newInstance();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (a instanceof Configurable) {
//			((Configurable)a).showConfigurationGUI();
//		}
		

//		////////////// Works if there is c:\TEMP\tagger\autotagger\TaggerBySize.class ////////////////
//		ClassLoader loader;
//		try {
//			loader = URLClassLoader.newInstance(new URL[] {new File("c:\\temp").toURI().toURL()},
//									   Main.class.getClassLoader());
//			Class<?> clazz = Class.forName("tagger.autotagger.TaggerBySize", true, loader);
//			AutoTagger autoTagger = (AutoTagger)clazz.getConstructor().newInstance();		
//			System.out.println(autoTagger.getAuthor());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		////////////// Works ///////////////////////////////////////////////////////////////////////
//		AutoTagger autoTagger = null;
//		try {
//			autoTagger = AutoTaggerLoader.getAutoTagger(new File("c:/TEMP/TaggerBySize.class"));
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(autoTagger.getDescription());
		
		
//		Object[] params = new Object[1];
//		params[0] = "Message!!!";
//		commander.getCommand(LOG_WRITE_MESSAGE).execute(params);
//		for (String str : (Collection<String>)commander.getCommand(LOG_GET_MESSAGES).execute(null)) {
//			System.out.println(str);
//		}
		
		try {
			// Start the web server
			webServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gui.displayGUI();
		// Close the listener
		listener.close();
		
//		File file = new File("c:\\TEMP\\New Folder\\New Folder\\New Folder\\24.S08E09.CUSTOM.HEBSUB.HDTV.XviD-iDown.avi");
//		System.out.println(file.getParentFile().toString());
//		System.out.println(file.getParent());
//		System.out.println(file.getName());
		
//		AutoTagger autoTagger =
//			AutoTaggerLoader.getAutoTagger(new File("C:\\TEMP\\TaggerByPathKeywords.class"));
//		System.out.println(autoTagger.getDescription());
		
		//log.clearLog();
		//log.writeMessage("3");
		//log.writeMessage("2");
		//log.writeMessage("1");
//		System.out.println(log.getMessages());;
	}
}
