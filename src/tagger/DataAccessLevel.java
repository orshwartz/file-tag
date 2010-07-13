package tagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * @author Itai Evron
 *
 */
public class DataAccessLevel {
	
	private static Connection conn = null;
	
	private String fileName = null;
	private static String dbURL = null;
	private String command;
	private StringBuilder string;
	private boolean connectionMode;
	
	private BufferedReader br;
	private PreparedStatement stmt;	

	private static final String tagsTableCreationScript =
													"TAGS.sql";
	private static final String filesTableCreationScript =
													"FILES.sql";
	private static final String attachmentsTableCreationScript =
													"ATTACHMENTS.sql";
	
	/**
	 * 
	 */
	public DataAccessLevel(){
		
		dbURL = "jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine";
		connect();
		loadTables();
		disconnect();
	}
	
	/**
	 * 
	 */
	public void connect(){
		
		try
        {
			System.out.println("Connecting to database...");
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            
            // Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
	}
	
	/**
	 * 
	 */
	public void loadTables(){

		string = new StringBuilder();
		
		
			/* ------------- */
		
		try { // load three tables to database
			
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet results = dbmd.getTables(null, "ME", "TAGS", null);
			// add TAGS table, if needed
			if(!results.next())
			{
				fileName = tagsTableCreationScript;
				try {
					br = new BufferedReader( new FileReader(fileName));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					while((command = br.readLine() ) != null){
						string.append(command);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//stmt = conn.createStatement();
				PreparedStatement stmt = conn.prepareStatement(string.toString());
				stmt.execute();
				stmt.close();
				
			}
			
			results = dbmd.getTables(null, "ME", "FILES", null);
			
			// add FILES table, if needed
			if(!results.next())
			{
				fileName = filesTableCreationScript;
				string.setLength(0);
				try {
					br = new BufferedReader( new FileReader(fileName));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					while((command = br.readLine() ) != null){
						string.append(command);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//stmt = conn.createStatement();
				PreparedStatement stmt = conn.prepareStatement(string.toString());
				stmt.execute();
				stmt.close();
			}
			
			results = dbmd.getTables(null, "ME", "ATTACHMENTS", null);
			
			// add ATTACHMENTS table, if needed
			if(!results.next())
			{
				fileName = attachmentsTableCreationScript;
				string.setLength(0);
				try {
					br = new BufferedReader( new FileReader(fileName));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					while((command = br.readLine() ) != null){
						string.append(command);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//stmt = conn.createStatement();
				PreparedStatement stmt = conn.prepareStatement(string.toString());
				stmt.execute();
				stmt.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			
			
			string.setLength(0);
			string.append("CREATE VIEW freq_desc_tags (tag_id, tag_frequency) AS ");
			string.append("SELECT tag_id, COUNT(tag_id) FROM attachments ");
			string.append("GROUP BY tag_id ORDER BY COUNT(tag_id) DESC");
			
			stmt = conn.prepareStatement(string.toString());
			stmt.execute();
			
		}catch (SQLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	} // public void LoadTables(){
	
	
	
	/**
	 * @param tags
	 * @throws TagAlreadyExistsException
	 */
	public void addTags(Collection<String> tags) throws TagAlreadyExistsException{
		
		connect();
		
			 
			 Iterator<String> it = tags.iterator();

			 while(it.hasNext()){
				 try{
				
					 // build the string for the command
					 string.setLength(0);
					 string.append("INSERT INTO tags(tag) VALUES('");
					 string.append(it.next());
					 string.append("')");
					 
					 PreparedStatement stmt = conn.prepareStatement(string.toString());

					stmt.executeUpdate();
					stmt.close();
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 	}
				 
		disconnect();			 
	}
	
	
	/**
	 * @param tag
	 * @throws TagNotFoundException
	 */
	public void removeTag(String tag) throws TagNotFoundException{
	
		connect();
		try {
			
			// build the string for the command
			string.setLength(0);
			string.append("DELETE FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");
			
			PreparedStatement stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				stmt.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 disconnect();
	}
	
	
	/**
	 * @param oldName
	 * @param newName
	 */
	public void renameTag(String oldName, String newName){

		connect();
		try {
			
			// build the string for the command
			string.setLength(0);
			string.append("UPDATE tags SET tag = '");
			string.append(newName);
			string.append("' WHERE tag = '");
			string.append(oldName);
			string.append("'");
			
			PreparedStatement stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}
	
	
	
	/**
	 * @param file
	 * @param tags
	 */
	public void tagFile(String file, Collection<String> tags){
		
		connect();
		Iterator<String> it = tags.iterator();
		
		try {
			
			// build the string for the command
			string.setLength(0);
			string.append("INSERT INTO files(filename,last_modified_epoch) ");
			string.append("VALUES('");
			string.append(file);
			string.append("',3432)");
			
			PreparedStatement stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				
				while(it.hasNext()){
				//stmt = conn.createStatement();
				String tag = it.next();
				
				string.setLength(0);
				string.append("INSERT INTO tags(tag) VALUES('");
				string.append(tag);
				string.append("')");
					try{
						stmt = conn.prepareStatement(string.toString());
						stmt.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				makeAttachments(file, tag);
				}
				stmt.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 disconnect();
	}
	
	
	/**
	 * @param file
	 * @param tag
	 */
	public void untagFile(String file, String tag){
		// get tag id
		connect();
		try {
			// get tag id
			
			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");
			
			PreparedStatement stmt = conn.prepareStatement(string.toString());
			ResultSet results = stmt.executeQuery();
			results.next();
			int tagId = results.getInt(1);
			
			// get file id
			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM files WHERE filename = '");
			string.append(file);
			string.append("'");
			
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			results.next();
			int fileId = results.getInt(1);
			
			// remove from attachments
			
			// build the string for the command
			string.setLength(0);
			string.append("DELETE FROM attachments WHERE ");
			string.append("tag_id = ");
			string.append(tagId);
			string.append("AND file_id = ");
			string.append(fileId);
			
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
			
			// delete tags that remained tagged to nothing
			string.setLength(0);
			string.append("DELETE FROM tags ");
			string.append("WHERE tag_id NOT IN (");
			string.append("SELECT tag_id FROM attachments)");
	
			stmt = conn.prepareStatement(string.toString());
			stmt.execute();
			
			
			stmt.close();
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 disconnect();
		
	}
	
	public void unTagFileAll(String file){
		
		connect();
		PreparedStatement stmt;
		string.setLength(0);
		string.append("DELETE FROM attachments WHERE file_id IN ( ");
		string.append("SELECT file_id FROM files WHERE filename ='");
		string.append(file); 
		string.append("')");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		string.setLength(0);
		string.append("DELETE FROM tags ");
		string.append("WHERE tag_id NOT IN (");
		string.append("SELECT tag_id FROM attachments)");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		// TODO : add tags with the algorithms;
		
	}
	
	
	public void removeFile(String file){
		
		
		connect();
		PreparedStatement stmt;
		
		string.setLength(0);
		string.append("DELETE FROM files WHERE ");
		string.append("filename = '");
		string.append(file);
		string.append("'");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		string.setLength(0);
		string.append("DELETE FROM tags ");
		string.append("WHERE tag_id NOT IN (");
		string.append("SELECT tag_id FROM attachments)");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		disconnect();
	}
	
	
	
	
	/**
	 * @param file
	 * @param tag
	 */
	public void makeAttachments(String file, String tag){
		
		try {
			
			// get tag id

			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");
			
			
			stmt = conn.prepareStatement(string.toString());
			ResultSet results = stmt.executeQuery();
			results.next();
			int tagId = results.getInt(1);
			System.out.println("tag id is " + tagId);
			
			// get file id
			
			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM files WHERE filename = '");
			string.append(file);
			string.append("'");
			
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			results.next();
			int fileId = results.getInt(1);
			System.out.println("file name is " + fileId);
			
			// insert into attachments
			
			
			// build the string for the command
			string.setLength(0);
			string.append("INSERT INTO attachments VALUES(");
			string.append(tagId);
			string.append(",");
			string.append(fileId);
			string.append(")");
			
			stmt = conn.prepareStatement("insert into ATTACHMENTS values(" +
					tagId + "," + fileId +")");
			stmt.executeUpdate();
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public boolean tagExists(String tag){
		
		connect();
		
		try {
		string.setLength(0);
		string.append("SELECT * FROM tags WHERE tag = '");
		string.append(tag);
		string.append("'");
	
	
				stmt = conn.prepareStatement(string.toString());
				ResultSet results = stmt.executeQuery();
				
				if(results.next())
					return true;
				else
					return false;
				
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	
	
		disconnect();
		return false;
		
	}
	
	/**
	 * Connection getter.
	 * @return
	 */
	public Connection getConnection(){
	
		return conn;
	}
	
	/**
	 * This method returns files tagged with certain tags but not tagged with others.
	 * @param includedTags Tags which should be attached to returned files.
	 * @param excludedTags Tags which should not attached to returned files.
	 * @return Collection of files attached to <CODE>includedTags</CODE> but not
	 * attached to <CODE>exlcludedTags</CODE>.
	 */
	public Collection<String> searchByTag(Collection<String> includedTags,
										Collection<String> excludedTags){
		
		connect();
		
		Collection<String> files = new LinkedList<String>();
		
		int i=0;
		int size = includedTags.size();
		PreparedStatement stmt;
		string.setLength(0);
		string.append("SELECT filename FROM files WHERE file_id IN ");
		string.append("(SELECT file_id FROM tags NATURAL JOIN attachments ");
		string.append("WHERE ");
		
			for (String curTag : includedTags)
			{
				i++;
				string.append("tag = '");
				string.append(curTag);
				string.append("' ");
				if( i < size )
					string.append("OR ");
			}
		
		i=0;
		size = excludedTags.size();
		
		string.append("EXCEPT ");
		string.append("SELECT file_id FROM tags NATURAL JOIN attachments ");
		string.append("WHERE ");
		

			for (String curTag : excludedTags)
			{
				i++;
				string.append("tag = '");
				string.append(curTag);
				string.append("' ");
				if( i < size )
					string.append("OR ");
				else
					string.append(")");
			}
		
		try {
			System.out.println(string);
			stmt = conn.prepareStatement(string.toString());
			ResultSet results = stmt.executeQuery();
			
			System.out.println("nani?");
			while(results.next())
	        {
	            files.add(results.getString(1));
	        }
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		
		

		
			
		disconnect();
				return files;
		
	}
	
	public Collection<TagFreq> getTagListFreqOrdered(){
		
		connect();
		
		TagFreq tagFreq;
		StringBuilder part = new StringBuilder();
		Collection<TagFreq> tagFreqs = new LinkedList<TagFreq>();
		
		string.setLength(0);
		string.append("SELECT tag,tag_frequency FROM freq_desc_tags NATURAL JOIN tags");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			ResultSet results = stmt.executeQuery();
			
			while(results.next()){
				
				tagFreq = new TagFreq(results.getInt(2), results.getString(1)) ;
				tagFreqs.add(tagFreq);
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return tagFreqs;
	}
	
	
	
	public void dropTables(){
			connect();
			
			try {
				string.setLength(0);
				string.append("DROP TABLE tags");
				PreparedStatement stmt;
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				
				string.setLength(0);
				string.append("DROP TABLE files");
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				
				string.setLength(0);
				string.append("DROP TABLE attachments");
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			disconnect();
	}
	
	
	/**
	 * 
	 */
	public void disconnect(){
		
		try
        {
			System.out.println("Task done. Disconnecting...");
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        }
        catch (SQLException sqlExcept)
        {
        	// TODO: Do something?
        }	
	}
}
