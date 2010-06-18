/**
 * 
 */
package tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Locale;
import java.util.Observable;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import sun.security.action.GetLongAction;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz
 */
public class TagRepositoryEventDrivenImpl extends TagRepositoryEventDriven {

	/**
	 * 
	 */

    private static Statement stmt = null;
    private DBconnector connector = null;
    private Connection conn;
    
    private int tag_id;
	
	public TagRepositoryEventDrivenImpl() {
		// TODO Auto-generated constructor stub
		
		tag_id = 1;
		
		connector = new DBconnector();
		conn = connector.getConnection(); // get connection
		
		System.out.println(this.getClass().getName() + " up.");
	}

	@Override
	public void addTag(String tag1) throws TagAlreadyExistsException {
		// TODO Auto-generated method stub
		
		// Remove any leading and trailing whitespace
		tag1 = tag1.trim();
		 try {
		stmt = conn.createStatement();

			stmt.executeUpdate("insert into TAGS(tag) values('" +
					tag1 + "')");
        stmt.close();
		 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// TODO: Consider using only lowercase for tag comparison or
		// maybe even enter tags to repository as lowercase (check sites
		// using tags, for a common way of action).
		// Use tag.toLowerCase(Locale.getDefault())
	}

	@Override
	public Collection<String> getTagListFreqOrdered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTag(String tag) throws TagNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException, TagAlreadyExistsException {
		// TODO Auto-generated method stub		
	}

	@Override
	public Collection<File> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tagExists(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tagFile(File file, String tag) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void untagFile(File file, String tag) throws FileNotTaggedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
