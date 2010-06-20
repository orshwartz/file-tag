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
	
	
	/**
	 * 
	 */
	public DataAccessLevel(){
		
		dbURL = "jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine";
		Connect();
		LoadTables();
		Disconnect();
		
	}
	
	/**
	 * 
	 */
	public void Connect(){
		
		
		try
        {
			System.out.println("Connecting to data base .. " );
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
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
	public void LoadTables(){

		string = new StringBuilder();
		
		
			/* ------------- */
		
		try { // load three tables to database
			
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet results = dbmd.getTables(null, "ME", "TAGS", null);
			
			// add TAGS table, if needed
			if(!results.next())
			{
				fileName = "TAGS.sql";
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
				fileName = "FILES.sql";
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
				fileName = "ATTACHMENTS.sql";
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
			
	} // public void LoadTables(){
	
	
	
	/**
	 * @param tags
	 * @throws TagAlreadyExistsException
	 */
	public void addTag(Collection<String> tags) throws TagAlreadyExistsException{
		
		Connect();
		
			 
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
				 
		Disconnect();			 
	}
	
	
	/**
	 * @param tag
	 * @throws TagNotFoundException
	 */
	public void removeTag(String tag) throws TagNotFoundException{
	
		Connect();
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
			 Disconnect();
	}
	
	
	/**
	 * @param oldName
	 * @param newName
	 */
	public void renameTag(String oldName, String newName){

		Connect();
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
		Disconnect();
	}
	
	
	
	/**
	 * @param file
	 * @param tags
	 */
	public void tagFile(String file, Collection<String> tags){
		
		Connect();
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
				
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				makeAttachments(file, tag);
				}
				stmt.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 Disconnect();
	}
	
	
	/**
	 * @param file
	 * @param tag
	 */
	public void untagFile(String file, String tag){
		// get tag id
		Connect();
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
			
			stmt.close();
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Disconnect();
		
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
		
		Connect();
		
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

	
	
		Disconnect();
		return false;
		
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	
	
	
	
	public Collection<File> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags){
		
		Connect();
		int i = 0;
		int incSize = 0;
		int excSize = 0;
		String part;
		Integer integer;
		
		Collection<Integer> incInts = new LinkedList();
		Collection<Integer> excInts = new LinkedList();
		PreparedStatement stmt;
		
		
		// get included tag_ids
		incSize = includedTags.size();
		Iterator<String> it = includedTags.iterator();
		string.setLength(0);
		string.append("SELECT tag_id FROM tags WHERE tag = '");
		
		while(it.hasNext()){
			
				part = it.next();
				string.append(part);
			
				if(it.hasNext())
					string.append("' OR tag = '");
				else
					string.append("'");
		}
	
			try {
				stmt = conn.prepareStatement(string.toString());
				ResultSet results = stmt.executeQuery();
				
						for(i=0;i<incSize;i++){
							results.next();
							incInts.add(results.getInt(1));
					
						}
					
				} catch (SQLException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		// get excluded tag_ids
		excSize = excludedTags.size();
		it = excludedTags.iterator();
		string.setLength(0);
		string.append("SELECT tag_id FROM tags WHERE tag = '");	
		
		while(it.hasNext()){
			
				part = it.next();
				string.append(part);
			
				if(it.hasNext())
					string.append("' OR tag = '");
				else
					string.append("'");
		}	
		
			try {
				stmt = conn.prepareStatement(string.toString());
				ResultSet results = stmt.executeQuery();
			
					for(i=0;i<excSize;i++){
						results.next();
						excInts.add(results.getInt(1));
				
					}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		searchAttachments(incInts,excInts);
		Disconnect();
				return null;
		
	}
	
	
	public void searchAttachments(Collection<Integer> incInts,
			Collection<Integer> excInts){
		
		Iterator<Integer> incIt = incInts.iterator();
		Iterator<Integer> excIt = excInts.iterator();
		
		string.setLength(0);
		
		string.append("SELECT file_id FROM attachments WHERE (tag_id = '");
		
		
		
	}
	
	
	
	public void DropTables(){
			Connect();
			
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
			
			
			
			Disconnect();
		
		
	}
	
	
	/**
	 * 
	 */
	public void Disconnect(){
		
		try
        {
			System.out.println("Task done. Disconnecting ..");
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
            
        }
		
		
		
	}

}