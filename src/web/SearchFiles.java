package web;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commander.commands.TSCommand;

import tagger.DataAccessLevel;
import tagger.TagFreq;

    public class SearchFiles extends HttpServlet {
        /**
		 * 
		 */
		private static final long serialVersionUID = -4617239181190239147L;

		@SuppressWarnings("unchecked")
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
            
            // The function that occurs when the user clicks "Include" or "Exclude"
            
            // The function that adds an option to a select
            response.getWriter().println("function addOption(list, value){");
            response.getWriter().println("var newOpt = new Option(value, value);");
            response.getWriter().println("list.options[list.length] = newOpt;}");

            // The function that removes an option from a select
            response.getWriter().println("function deleteOption(list, index){");
            response.getWriter().println("var length = list.length;");
            response.getWriter().println("if (length > 0) { list.options[index] = null; }}");
            
            // The function that removes an option from a select
            response.getWriter().println("function moveOptions(listFrom, listTo){");
            response.getWriter().println("var length = listFrom.length;");
            response.getWriter().println("var selectedText = new Array();");
            response.getWriter().println("var selectedValues = new Array();");
            response.getWriter().println("var selectedCount = 0;");	
            response.getWriter().println("var i;");
            
            // Save and delete from the first list
            response.getWriter().println("for(i=length-1; i>=0; i--){");
            response.getWriter().println("if(listFrom.options[i].selected){");
            response.getWriter().println("selectedText[selectedCount] = listFrom.options[i].value;");
            response.getWriter().println("selectedValues[selectedCount] = listFrom.options[i].value;");
            response.getWriter().println("deleteOption(listFrom, i);");
            response.getWriter().println("selectedCount++;}}");
            
            // Add to the second list
            response.getWriter().println("for(i=selectedCount-1; i>=0; i--){");
            response.getWriter().println("addOption(listTo, selectedText[i], selectedValues[i]);}");
            
            // Enable the search button
            response.getWriter().println("document.getElementById('btnSearch').disabled = false;}");
            
            // Function that sets the selected tags to search
            response.getWriter().println("function SetSelectedTags() {");
            response.getWriter().println("var i = 0;");
            response.getWriter().println("var hidden = document.getElementById('hdnSelectedTags');");
            response.getWriter().println("hidden.value = 'Included:';");
            response.getWriter().println("var lstIncludedTags = document.getElementById('lstIncludedTags');");
            response.getWriter().println("for(i = 0; i < lstIncludedTags.length; ++i){");
            response.getWriter().println("hidden.value = hidden.value + lstIncludedTags.options[i].value");
            response.getWriter().println("if (i < lstIncludedTags.length - 1) { hidden.value = hidden.value + ','; }}");
            response.getWriter().println("hidden.value = hidden.value + '&Excluded:';");
            response.getWriter().println("var lstExcludedTags = document.getElementById('lstExcludedTags');");
            response.getWriter().println("for(i = 0; i < lstExcludedTags.length; ++i){");
            response.getWriter().println("hidden.value = hidden.value + lstExcludedTags.options[i].value");
            response.getWriter().println("if (i < lstExcludedTags.length - 1) { hidden.value = hidden.value + ','; }}");
            response.getWriter().println("return true; }");
            		
            response.getWriter().println("</script>");
            
            response.getWriter().println("</head>");
            
            response.getWriter().println("<body>");
            response.getWriter().println("<img src='../TigTagToe.jpg' alt='Tig Tag Toe'/>");
            response.getWriter().println("<form action='SearchResults' method='get' onsubmit='return SetSelectedTags();'>");
            
            response.getWriter().println("<h1>Search Files:</h1>");
            response.getWriter().println("<h3>Select the tags to include and exclude in the search.</h3>");
            
            response.getWriter().println("<table style='border: none;'>");
            
            response.getWriter().println("<tr><td>Included Tags:</td><td colspan='3'/><td>Excluded Tags:</td></tr>");
            
            response.getWriter().println("<tr><td><select id='lstIncludedTags' multiple='multiple'/></td>");
            response.getWriter().println("<td valign='middle'><input type='button' value='<< Include' id='btnInclude' class='Button' " +
            							" onclick='moveOptions(lstAvailableTags, lstIncludedTags)' /></td>");
            
            // Show all the available tags
            response.getWriter().println("<td><select id='lstAvailableTags' multiple='multiple'>");
            Collection<TagFreq> availableTags = (Collection<TagFreq>) ((TSCommand) 
            		getServletContext().getAttribute("TAGGER_GET_TAGS_BY_FREQ")).execute(null);
            
            for (TagFreq tag : availableTags) {
            	response.getWriter().println("<option value='" + tag.getTagName() + "'>" +
            			tag.getTagName() + "(" + tag.getFreq() + ")" +
            			"</option>");
            }
            
            response.getWriter().println("</select></td>");
            
            response.getWriter().println("<td valign='middle'><input type='button' value='Exclude >>' id='btnExclude' class='Button' " +
            							" onclick='moveOptions(lstAvailableTags, lstExcludedTags)'/></td>");
            
            response.getWriter().println("<td><select id='lstExcludedTags' multiple='multiple'/></td></tr>");
            response.getWriter().println("<tr><td colspan='5' align='left'>");
            response.getWriter().println("<br/><input type='submit' value='Search' id='btnSearch' class='Button' disabled='true'/></td></tr>");
            
            response.getWriter().println("</table>");
            
            // The requested tags for search
            response.getWriter().println("<input type='hidden' name='hdnSelectedTags' id='hdnSelectedTags'/>");
            
            response.getWriter().println("</form>");
            response.getWriter().println("</body>");
            response.getWriter().println("</html>");
            response.flushBuffer();
        }
    }
