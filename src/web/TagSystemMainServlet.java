package web;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    public class TagSystemMainServlet extends HttpServlet {
       		
		private static final long serialVersionUID = -6217224731637701317L;

		/**
		 * This deals with HTTP GET command. It sends an HTML of the tagging
		 * system site entrance. 
		 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) 	throws ServletException, IOException {
           
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
            response.getWriter().println("<img src='TigTagToe.jpg' alt='Tig Tag Toe'/>");
            
            response.getWriter().println("<form action='Main' method='GET'>");
            response.getWriter().println("<table style='border: none;'>");
            
            response.getWriter().println("<br/><a href='/TigTagToe/SearchFiles' style='font-size:large'>Search Files</a>");
            response.getWriter().println("</table>");
            response.getWriter().println("</form>");
            response.getWriter().println("</body>");
            response.getWriter().println("</html>");
            response.flushBuffer();
        }
    }
