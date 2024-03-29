package web;

import static commander.CommandManager.CmdCodes.TAGGER_GET_FILES_BY_TAGS;
import static commander.CommandManager.CmdCodes.TAGGER_GET_TAGS_BY_FREQ;

import java.io.IOException;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import commander.CommandManager;

public class WebServer {

	// Data Members
	private CommandManager commander;
	private Server server;
	private Connector connector;
	private Context root;

	/**
	 * 
	 */
	public WebServer(CommandManager commander) {
		
		this.commander = commander;
	}

	public void start() throws Exception {
		
		server = new Server();
        connector = new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        
        root = new Context(server, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(new TagSystemMainServlet()), "/TigTagToe");   
        root.addServlet(new ServletHolder(new SearchFilesServlet()), "/TigTagToe/SearchFiles");   
        root.addServlet(new ServletHolder(new SearchResultsServlet()), "/TigTagToe/SearchResults");      
		root.setAttribute("TAGGER_GET_TAGS_BY_FREQ", commander.getCommand(TAGGER_GET_TAGS_BY_FREQ));
		root.setAttribute("TAGGER_GET_FILES_BY_TAGS", commander.getCommand(TAGGER_GET_FILES_BY_TAGS));
     
        ResourceHandler images = new ResourceHandler();
        images.setResourceBase("WebContent/WEB-INF/images");
        
        HandlerList hl = new HandlerList();
        hl.setHandlers(new Handler[]{images, root});       
        server.setHandler(hl);
        
        server.setGracefulShutdown(1000);
        server.setStopAtShutdown(true);
        
        server.start();
	}
	
	public void close() {
		
		root.setShutdown(true);
		try {
			connector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
