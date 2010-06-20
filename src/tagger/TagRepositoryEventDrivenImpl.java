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
    private DataAccessLevel DAL = null;
    private Connection conn;
    
    private int tag_id;
	
	public TagRepositoryEventDrivenImpl() {
		// TODO Auto-generated constructor stub
		
		tag_id = 1;
		
		DAL = new DataAccessLevel();
		conn = DAL.getConnection(); // get connection
		
		System.out.println(this.getClass().getName() + " up.");
	}

	@Override
	public void addTag(Collection<String> tags) throws TagAlreadyExistsException {
		// TODO Auto-generated method stub
		
		
		DAL.addTag(tags);
		
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
			DAL.removeTag(tag);
	}

	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException, TagAlreadyExistsException {
		DAL.renameTag(oldName, newName);
	}

	@Override
	public Collection<File> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		
		// TODO Auto-generated method stub
		return DAL.searchByTag(includedTags, excludedTags);
	}

	@Override
	public boolean tagExists(String tag) {
		// TODO Auto-generated method stub
		return DAL.tagExists(tag);
	}

	@Override
	public void tagFile(String file, Collection<String> tags) throws FileNotFoundException {
		DAL.tagFile(file, tags);
		
	}

	@Override
	public void untagFile(String file, String tag) throws FileNotTaggedException {
		DAL.untagFile(file, tag);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void DropTables(){
		DAL.DropTables();
	}

}
