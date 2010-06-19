package tagger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;


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
	private Statement stmt;
	
	public DataAccessLevel(){
		
		dbURL = "jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine";
		Connect();
		LoadTables();
		Disconnect();
		
	}
	
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

				stmt = conn.createStatement();
				stmt.execute(string.toString());
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

				stmt = conn.createStatement();
				stmt.execute(string.toString());
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

				stmt = conn.createStatement();
				stmt.execute(string.toString());
				stmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	} // public void LoadTables(){
	
	
	
	public void addTag(Collection<String> tags) throws TagAlreadyExistsException{
		
		Connect();
		
			 
			 Iterator<String> it = tags.iterator();
			 
			 while(it.hasNext()){
				 try{
				stmt = conn.createStatement();

					stmt.executeUpdate("insert into TAGS(tag) values('" +
							it.next() + "')");
		        stmt.close();
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 	}
				 
		Disconnect();			 
	}
	
	
	public void removeTag(String tag) throws TagNotFoundException{
	
		Connect();
		try {
			stmt = conn.createStatement();

				stmt.executeUpdate("delete from TAGS where tag = '" +
						tag + "'");
	        stmt.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 Disconnect();
	}
	
	
	public void renameTag(String oldName, String newName){

		Connect();
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("update TAGS set tag = '" +
					newName + "' where tag = '" + oldName + "'");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Disconnect();
	}
	
	
	
	public void tagFile(String file, Collection<String> tags){
		
		Connect();
		Iterator<String> it = tags.iterator();
		
		try {
			stmt = conn.createStatement();

				stmt.executeUpdate("insert into FILES(filename,last_modified_epoch) values('" +
						file + "',3432)");
				
				while(it.hasNext()){
				stmt = conn.createStatement();
				String tag = it.next();
				stmt.executeUpdate("insert into TAGS(tag) values('" +
						tag + "')");
				
				makeAttachments(file, tag);
				}
				stmt.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 Disconnect();
	}
	
	
	public void untagFile(){
		
	}
	
	
	
	
	public void makeAttachments(String file, String tag){
		
		try {
			
			// get tag id

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from TAGS where tag = '" + tag + "'");
			results.next();
			int tagId = results.getInt(1);
			System.out.println("tag id is " + tagId);
			
			// get file id
			
			stmt = conn.createStatement();
			results = stmt.executeQuery("select * from FILES where filename = '" + file + "'");
			results.next();
			int fileId = results.getInt(1);
			System.out.println("file name is " + fileId);
			
			// insert into attachments
			
			stmt = conn.createStatement();
			stmt.executeUpdate("insert into ATTACHMENTS values(" +
					tagId + "," + fileId +")");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	
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
