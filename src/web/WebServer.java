package web;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import commander.CommandManager;
import static commander.CommandManager.CmdCodes.*;

public class WebServer {

	// Data Members
	private CommandManager commander;

	/**
	 * 
	 */
	public WebServer(CommandManager commander) {
		
		this.commander = commander;
	}

	public void start() throws Exception {
		
		Server server = new Server();
        Connector connector=new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        
        Context root = new Context(server, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(new TagSystemMainServlet()), "/TigTagToe");   
        root.addServlet(new ServletHolder(new SearchFiles()), "/TigTagToe/SearchFiles");   
        root.addServlet(new ServletHolder(new SearchResults()), "/TigTagToe/SearchResults");      
		root.setAttribute("TAGGER_GET_TAGS_BY_FREQ", commander.getCommand(TAGGER_GET_TAGS_BY_FREQ));
		root.setAttribute("TAGGER_GET_FILES_BY_TAGS", commander.getCommand(TAGGER_GET_FILES_BY_TAGS));
     
        ResourceHandler images = new ResourceHandler();
        images.setResourceBase("WebContent/WEB-INF/images");
        
        HandlerList hl = new HandlerList();
        hl.setHandlers(new Handler[]{images, root});       
        server.setHandler(hl);
        
        server.start();
//        server.join();
	}
}
