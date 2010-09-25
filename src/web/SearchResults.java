package web;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commander.commands.TSCommand;

import tagger.DataAccessLevel;
import tagger.TagFreq;

    public class SearchResults extends HttpServlet {
        
		/**
		 * 
		 */
		private static final long serialVersionUID = -6352607889178449374L;

		@SuppressWarnings("unchecked")
		protected void doGet(HttpServletRequest request, HttpServletResponse response) 	throws ServletException, IOException {
           
			response.setCharacterEncoding("windows-1255");
			response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<html charset='windows-1255'>");
            response.getWriter().println("<head>");
            response.getWriter().println("<title>Tig Tag Toe</title>");
            
            // Style of the page 
            response.getWriter().println("<style type='text/css'>");
            response.getWriter().println(".Button { font-family: sans-serif;" +
            								"background-color: white; color: black; }");
            response.getWriter().println("</style>");
            
            // Java script
            response.getWriter().println("<script type='text/javascript'>");
            response.getWriter().println("</script>");
            
            response.getWriter().println("</head>");
            
            response.getWriter().println("<body>");
            
            response.getWriter().println("<img src='../TigTagToe.jpg' alt='Tig Tag Toe'/>");
            response.getWriter().println("<form action='/' method='get'>");
            response.getWriter().println("<table style='border: none;'>");
            
            response.getWriter().println("<tr><td><h1>Search Results</h1></td></tr>");
            
            // Show the search information
            String selectedTags[] = request.getParameter("hdnSelectedTags").split("&");
            String includedTags = selectedTags[0].split(":").length > 1 ? selectedTags[0].split(":")[1] : "";
            String excludedTags = selectedTags[1].split(":").length > 1 ? selectedTags[1].split(":")[1] : "";
            
            if (!includedTags.equals("")) {
            	response.getWriter().println("<tr><td><h2>For files containing the tags: " + includedTags);
            	
            	if (!excludedTags.equals("")) {
            		response.getWriter().println("<tr><td><h2>And not containing the tags: " + excludedTags);
            	}
            } else {
            	
            	response.getWriter().println("<tr><td><h2>For files not containing the tags: " + excludedTags);
            }
            
            response.getWriter().println("</h1></td></tr>");
            
            // Show Search Results
            Collection<String> includedTagsList = Arrays.asList(includedTags.split(","));
            Collection<String> excludedTagsList = Arrays.asList(excludedTags.split(","));
            Object[] params = new Object[2];
            params[0] = includedTagsList;
            params[1] = excludedTagsList;
            Collection<String> searchResults = (Collection<String>) ((TSCommand) 
            		getServletContext().getAttribute("TAGGER_GET_FILES_BY_TAGS")).
            			execute(params);

            if (searchResults.isEmpty()) {
            	response.getWriter().println("<tr><td align='center'>No files found.</td></tr>");
            }
            else {
	            response.getWriter().println("<tr><td>");
	            response.getWriter().println(searchResults.toArray().length + " Files were found.</td></tr>");
	            response.getWriter().println("<tr><td><br/></td><tr>");
	            int index = 1;
        		
	            for (String fileName : searchResults) {
	            	response.getWriter().println("<tr><td>" + (index++) + ". " + fileName + "</td></tr>");
	            }
	            
	            response.getWriter().println("</td></tr>");
	            response.getWriter().println("<tr><td><br/></td><tr>");
	            response.getWriter().println("<tr><td align='left'><a href='../../TigTagToe'>Return To Main Page</a></td></tr>");
            }
            
            response.getWriter().println("</table>");
            response.getWriter().println("</form>");
            response.getWriter().println("</body>");
            response.getWriter().println("</html>");
            response.flushBuffer();
        }
    }
