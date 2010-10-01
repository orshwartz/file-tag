package tagger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.derby.tools.ij;

/**
 * @author Itay Evron
 * 
 */
public class DataAccessLevel {

	private static Connection conn = null;

	private static final String dbURL = "jdbc:derby:myDB;create=true;user=me;password=mine";
	private static final String jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	private StringBuilder string = new StringBuilder();
	
	private DatabaseMetaData dbmd;
	private ResultSet results;
	private PreparedStatement stmt;
	private Iterator<String> it;
	
	private boolean connCheck;

	

	private static final String tagsTableCreationScript = "TAGS.sql";
	private static final String filesTableCreationScript = "FILES.sql";
	private static final String attachmentsTableCreationScript = "ATTACHMENTS.sql";

	/**
	 * Constructor - Creates the DB if needed.
	 */
	public DataAccessLevel() {

		connect();
		loadTables();
		disconnect();
		
		connCheck = false;
		System.out.println("cons");
	}

	/**
	 * Connects to the DB. 
	 */
	public void connect() {

		try {
			System.out.println("Connecting to database ...");
			Class.forName(jdbcDriver).newInstance();

			// Get a connection
			conn = DriverManager.getConnection(dbURL);
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	
	
	public void makeConnection(){
		
		new Thread( new Runnable(){

			@Override
			public void run() {
				connect();
				connCheck = true;
				
				
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				disconnect();
				connCheck = false;
				
			}
			
		}).start();
			
		while(connCheck == false){}
	}

	/**
	 * Creates the tables if needed.
	 */
	public void loadTables() {

		try { // load three tables to database

			dbmd = conn.getMetaData();
			results = dbmd.getTables(null, "ME", "TAGS", null);

			// add TAGS table, if needed
			if (!results.next()) {
				
					try {
						runScript(tagsTableCreationScript, "UTF-8", "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			}

			results = dbmd.getTables(null, "ME", "FILES", null);

			// add FILES table, if needed
			if (!results.next()) {
				
					try {
						runScript(filesTableCreationScript, "UTF-8", "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			}

			results = dbmd.getTables(null, "ME", "ATTACHMENTS", null);

			// add ATTACHMENTS table, if needed
			if (!results.next()) {
				
				try {
					runScript(attachmentsTableCreationScript, "UTF-8", "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	} // public void loadTables()

	/**
	 * @param script Filename to run
	 * @param scriptEncoding Encoding of script file. 
	 * @param outputEncoding Encoding for output.
	 * @return Results of script execution.
	 * @throws FileNotFoundException If <CODE>script</CODE> is not found.
	 * @throws UnsupportedEncodingException If either <CODE>scriptEncoding</CODE> or
	 * <CODE>outputEncoding</CODE> is not supported.
	 * @throws SQLException If there's a problem committing to the changes. 
	 * 
	 */
	private ByteArrayOutputStream runScript(String script, String scriptEncoding, String outputEncoding) throws UnsupportedEncodingException,
																												FileNotFoundException,
																												SQLException {
		
		ByteArrayOutputStream result = null;
		
		ij.runScript(conn,
					 new BufferedInputStream(new FileInputStream(script)),
					 "UTF-8",
					 result = new ByteArrayOutputStream(),
					 "UTF-8");
		
		return result;
	}

	
	/**
	 * This method adds a given tag to the repository
	 * @param tags - A collection of tags (Strings) to add into the repository
	 * @throws TagAlreadyExistsException if one or more of the <CDOE>tags</CODE>
	 *  in the collection is already inside the repository
	 */
	public void addTags(Collection<String> tags) throws TagAlreadyExistsException {

		connect();

		it = tags.iterator();

		while (it.hasNext()) {
				try {

					// build the string for the command
					string.setLength(0);
					string.append("INSERT INTO tags(tag) VALUES('");
					string.append(it.next());
					string.append("')");

					stmt = conn.prepareStatement(string.toString());
					stmt.executeUpdate();
					stmt.close();
				
				} catch (SQLException e) {
					//ignore
				}
		}
		
		disconnect();

			
	} // 	public void addTags(Collection<String> tags)

	
	/**
	 * This method removes a given tag from the repository
	 * @param tag - a single tag(String) to be removed from the repository
	 * @throws TagNotFoundException if the requested tag is not in the
	 * repository
	 */
	public void removeTag(String tag) throws TagNotFoundException {

		connect();
		
			try {

					// build the string for the command
					string.setLength(0);
					string.append("DELETE FROM tags WHERE tag = '");
					string.append(tag);
					string.append("'");

					stmt = conn.prepareStatement(string.toString());
					stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {	
					e.printStackTrace();
			}
		
		disconnect();
		
			
	} // 	public void removeTag(String tag)

	
	/**
	 * This method renames a given tag
	 * @param oldName - the old name of the tag(String)
	 * @param newName - the new name of the tag(String)
	 */
	public void renameTag(String oldName, String newName) {

		connect();
		
		try {

			// build the string for the command
			string.setLength(0);
			string.append("UPDATE tags SET tag = '");
			string.append(newName);
			string.append("' WHERE tag = '");
			string.append(oldName);
			string.append("'");

			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		disconnect();
			
	} // 	public void renameTag(String oldName, String newName)

	
	/**
	 * This method attaches a given collection of <CODE>tags</CODE>
	 * into a given<CODE>file</CODE> 
	 * @param file - the file that the <CODE>tags</CODE> would be attached to
	 * @param tags - the collection of tags who will be attached to the <CODE>file</CODE>
	 */
	public void tagFile(String file, Collection<String> tags) {

		
		it = tags.iterator();

		try {
			if(!fileExists(file)){
				connect();
				// build the string for the command
				string.setLength(0);
				string.append("INSERT INTO files(filename,last_modified_epoch) ");
				string.append("VALUES('");
				string.append(file);
				string.append("',"); // FIXME : this is weird.
				
				long epoch = System.currentTimeMillis()/1000;
				string.append(epoch);
				string.append(")");

				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
			}
			else {
			connect();
				
				string.setLength(0);
				string.append("UPDATE files SET last_modified_epoch = ");
				long epoch = System.currentTimeMillis()/1000;
				string.append(epoch);
				string.append("WHERE filename = '");
				string.append(file);
				string.append("'");
				
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				stmt.close();
				
			}
			
			while (it.hasNext()) {
					String tag = it.next();

					string.setLength(0);
					string.append("INSERT INTO tags(tag) VALUES('");
					string.append(tag);
					string.append("')");
				
					try {
				
						stmt = conn.prepareStatement(string.toString());
						stmt.executeUpdate();
					} catch (SQLException e) {
					e.printStackTrace();
					}
					
					makeAttachments(file, tag); // attach the tag into the file
			} // while (it.hasNext()) {
			
			stmt.close();
		} catch (SQLException e) {
			//ignore
		}
		
		disconnect();
			
	}//		public void tagFile(String file, Collection<String> tags)

	
	/**
	 * This method removes a given tag from a given file
	 * @param file - the file that the <CODE>tag</CODE> would be removed from
	 * @param tag - the tag that will be the removed
	 */
	public void untagFile(String file, String tag) {
		
		connect();
		
		try {
			
			// get tag id
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");

				stmt = conn.prepareStatement(string.toString());
				results = stmt.executeQuery();
				results.next();
			
			int tagId = results.getInt(1); // get the tag

			// get file id
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM files WHERE filename = '");
			string.append(file);
			string.append("'");

				stmt = conn.prepareStatement(string.toString());
				results = stmt.executeQuery();
				results.next();
				
			int fileId = results.getInt(1); // get the file

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

			// delete any tag that left not tagged to some file
			// build the string for the command
			string.setLength(0);
			string.append("DELETE FROM tags ");
			string.append("WHERE tag_id NOT IN (");
			string.append("SELECT tag_id FROM attachments)");
				
					
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				stmt.close();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		disconnect();
		
		
	} //	public void untagFile(String file, String tag)
	
	
	/**
	 * This method removes all of the tags that are attached to a given file
	 * @param file - The given file
	 */
	public void unTagFileAll(String file){
		
		connect();
		
		// delete all the tags of this file
		// build the string for the command
		string.setLength(0);
		string.append("DELETE FROM attachments WHERE file_id IN ( ");
		string.append("SELECT file_id FROM files WHERE filename ='");
		string.append(file); 
		string.append("')");
		
			try {
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// delete any tag that left not tagged to some file
		// build the string for the command
		string.setLength(0);
		string.append("DELETE FROM tags ");
		string.append("WHERE tag_id NOT IN (");
		string.append("SELECT tag_id FROM attachments)");
		
			try {
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		disconnect();
		
		// TODO : add tags with the algorithms(?);
		
	}//		public void unTagFileAll(String file)
	
	
	public Collection<String> getTagsOfFile(String file){
		
		connect();
		
		Collection<String> tags = new LinkedList<String>();
		ResultSet results2;
		int fileId = 0;
		
		string.setLength(0);
		string.append("SELECT file_id FROM files WHERE filename = '");
		string.append(file);
		string.append("'");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			results.next();
			fileId = results.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		string.setLength(0);
		string.append("SELECT tag_id FROM attachments WHERE file_id = ");
		string.append(fileId);
		
		
		try {
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			
			
				while(results.next()){
					
					string.setLength(0);
					string.append("SELECT tag FROM tags WHERE tag_id = ");
					string.append(results.getInt(1));
					
					stmt = conn.prepareStatement(string.toString());
					results2 = stmt.executeQuery();
					results2.next();
					tags.add( results2.getString(1) );

				}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		disconnect();
		return tags;
	}
	
	
	/**
	 * This method removes a given file from the repository
	 * @param file - The file that will be removed
	 */
	public void removeFile(String file){
		
		
		connect();
		
		
		// remove the file
		// build the string for the command
		string.setLength(0);
		string.append("DELETE FROM files WHERE ");
		string.append("filename = '");
		string.append(file);
		string.append("'");
		
			try {
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// delete any tag that left not tagged to some file
		// build the string for the command
		string.setLength(0);
		string.append("DELETE FROM tags ");
		string.append("WHERE tag_id NOT IN (");
		string.append("SELECT tag_id FROM attachments)");
		
			try {
				stmt = conn.prepareStatement(string.toString());
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		disconnect();
		
	}// 	public void removeFile(String file)
	
	

	/**
	 * This method make the attachments between a given <CODE>file</CODE> 
	 * and a given <CODE>tag</CODE>
	 * @param file - the given file
	 * @param tag - the given tag(String)
	 */
	public void makeAttachments(String file, String tag) {

		try {

			// get tag id
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");

				stmt = conn.prepareStatement(string.toString());
				results = stmt.executeQuery();
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

			
			// insert into attachments
			// build the string for the command
			string.setLength(0);
			string.append("INSERT INTO attachments VALUES(");
			string.append(tagId);
			string.append(",");
			string.append(fileId);
			string.append(")");

				stmt = conn.prepareStatement("insert into ATTACHMENTS values("
						+ tagId + "," + fileId + ")");
				stmt.executeUpdate();
				stmt.close();
			
		} catch (SQLException e) {
			// ignore
		}

	}// 	public void makeAttachments(String file, String tag)

	
	/**
	 * This method checks if a given tag exists within the repository
	 * @param tag - the given tag(String)
	 * @return true if the tag exists in the TAGS table, false otherwise
	 */
	public boolean tagExists(String tag) {

		connect();
		

		try {
			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM tags WHERE tag = '");
			string.append(tag);
			string.append("'");

				stmt = conn.prepareStatement(string.toString());
				results = stmt.executeQuery();

			if (results.next()) {
				disconnect();
				return true;
			} else {	
				disconnect();
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		
		return false;
	}// 	public boolean tagExists(String tag)
	
	public boolean fileExists(String file) {

		connect();

		try {
			
			// build the string for the command
			string.setLength(0);
			string.append("SELECT * FROM files WHERE filename = '");
			string.append(file);
			string.append("'");

				stmt = conn.prepareStatement(string.toString());
				results = stmt.executeQuery();

			if (results.next()) {
				disconnect();
				return true;
			} else {
				disconnect();
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		
		return false;
	}// 	public boolean fileExists(String file)
	
	
	/**
	 * Connection getter.
	 * 
	 * @return The connection
	 */
	public Connection getConnection() {

		return conn;
	}

	/**
	 * This method returns files tagged with certain tags but not tagged with
	 * others.
	 * 
	 * @param includedTags
	 *            Tags which should be attached to returned files.
	 * @param excludedTags
	 *            Tags which should not attached to returned files.
	 * @return Collection of files attached to <CODE>includedTags</CODE> but not
	 *         attached to <CODE>exlcludedTags</CODE>.
	 */
	public Collection<String> searchByTag(Collection<String> includedTags,
										  Collection<String> excludedTags) {

		connect();

		Collection<String> files = new LinkedList<String>();

		
		int i = 0;

		int sizeInc = includedTags.size();
		int sizeExc = excludedTags.size();
		
		if(sizeInc !=0 || sizeExc !=0){
			// select the files that fulfill the specified request
			// build the string for the command
			string.setLength(0);
			string.append("SELECT filename FROM files WHERE file_id IN ");
			string.append("(SELECT file_id FROM tags NATURAL JOIN attachments ");
			if(sizeInc != 0){
				string.append("WHERE ");

				for (String curTag : includedTags) {
			
						++i;
			
						// include those tags
						// build the string for the command
						string.append("tag = '");
						string.append(curTag);
						string.append("' ");
						if (i < sizeInc)
							string.append("OR ");// this tag OR this tag ..
						
				} // for
			} // if(sizeInc != 0){

		if(sizeExc !=0){
			
		i = 0;

			// except those tags ..
			// build the string for the command
			string.append("EXCEPT ");
			string.append("SELECT file_id FROM tags NATURAL JOIN attachments ");
			string.append("WHERE ");

			for (String curTag : excludedTags) {
			
					++i;
			
					// exclude those tags
					// build the string for the command
					string.append("tag = '");
					string.append(curTag);
					string.append("' ");
					if (i < sizeExc)
						string.append("OR "); // this tag OR this tag ..
					else // if we reached the end of the list
						string.append(")");
			} // for
		}// if(sizeExc !=0){
			else
				string.append(")");// if there no tags to exclude
				
			try {
				
					System.out.println(string); // TODO : println for testing
					stmt = conn.prepareStatement(string.toString());
					results = stmt.executeQuery();
			
					while (results.next()) {
						files.add(results.getString(1)); // add the files we found
					}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}// if(sizeInc !=0 && sizeExc !=0)
		
		
		disconnect();
		return files; // return the requested files
		
	} // 	public Collection<String> searchByTag( ... )
	
	
	
	
	/**
	 * This method returns the frequencies of all the tags in the repository
	 * @return A Collection of all the frequencies of the tags in the repository
	 */
	public Collection<TagFreq> getTagListFreqOrdered(){
		
		connect();
		
		TagFreq tagFreq;
		Collection<TagFreq> tagFreqs = new LinkedList<TagFreq>();
		
		// build the string for the command
		string.setLength(0);
		string.append("SELECT tag,tag_frequency FROM freq_desc_tags NATURAL JOIN tags");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			
			while(results.next()){
				tagFreq = new TagFreq(results.getInt(2), results.getString(1)) ;
				tagFreqs.add(tagFreq);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		disconnect();
		return tagFreqs;
	}
	
	/**
	 * This method returns the frequency of a given tag in the repository
	 * @param tag - the given tag
	 * @return the frequency of the given tag (TagFreq)
	 */
	public TagFreq getTagFreq(String tag){
		
		connect();
		
		TagFreq tagFreq = null;
		
		string.setLength(0);
		string.append("SELECT tag,tag_frequency FROM freq_desc_tags NATURAL JOIN tags ");
		string.append("WHERE tag = '" + tag + "'");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			
			results.next();
			
				tagFreq = new TagFreq(results.getInt(2), results.getString(1)) ;		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tagFreq;
	}
	
	/**
	 * This method deletes all of the tags and files located in the DB
	 */
	public void deleteAll(){
		connect();
		
		
		// delete all tags
		try {
			string.setLength(0);
			string.append("DELETE FROM tags");
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// delete all files
		try {
			string.setLength(0);
			string.append("DELETE FROM files");
			stmt = conn.prepareStatement(string.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
	}
	
	public long getTime(String file){
		
		connect();
		
		long epoch = 0;
		
		string.setLength(0);
		string.append("SELECT last_modified_epoch FROM files ");
		string.append("WHERE filename = '");
		string.append(file);
		string.append("'");
		
		try {
			stmt = conn.prepareStatement(string.toString());
			results = stmt.executeQuery();
			results.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			epoch = results.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(epoch);
		
		disconnect();
		return 0;
	}

	
	/*public void searchAttachments(Collection<Integer> incInts,
								  Collection<Integer> excInts) {

		Iterator<Integer> incIt = incInts.iterator();
		Iterator<Integer> excIt = excInts.iterator();

		string.setLength(0);

		string.append("SELECT file_id FROM attachments WHERE (tag_id = '");
	}*/ //TODO: I'm not sure that we need this method

	
	public void dropTables() {
		
		/* TODO : I made this method only for convenience
		 				It can be removed later */
		connect();

		try {
			
			string.setLength(0);
			string.append("DROP TABLE tags");
			
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
			e.printStackTrace();
		}

		disconnect();
	}

	
	/**
	 * Disconnect from the DB.
	 */
	public void disconnect() {

		try {
			System.out.println("Task done. Disconnecting...");
				if (stmt != null) {
						stmt.close();
				}
				
				if (conn != null) {
					DriverManager.getConnection(dbURL + ";shutdown=true");
					conn.close();
				}
			
		} catch (SQLException sqlExcept) {
			// TODO: Do something?
		}
	}
}
