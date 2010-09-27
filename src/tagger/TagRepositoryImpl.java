package tagger;

import gui.MainAppGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import listener.FileEvent;
import tagger.autotagger.AutoTagger;
import tagger.autotagger.AutoTaggerLoader;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz
 */
public class TagRepositoryImpl extends Observable implements TagRepository {

	private DataAccessLevel DAL = null;
    private final static Locale dfltLocal = Locale.getDefault();
    private Map<File, AutoTagger> autoTaggers = new HashMap<File,AutoTagger>();
	private String FILENAME_PERSISTENCE = "tagger_persistence.bin";
	
	/**
	 * <CODE>TagRepositoryImpl</CODE> constructor.
	 */
	public TagRepositoryImpl() {
		
		DAL = new DataAccessLevel();
		DAL.getConnection();
		
		// Restore needed automatic algorithms
		loadTaggerData();
		
		System.out.println(this.getClass().getName() + " up.");
	}

	/**
	 * This method fixes a string to be used as a tag in the repository.
	 * @param string to fix.
	 * @return The string lowercased and without leading and trailing
	 * whitespace.
	 */
	private String fixString(String string) {
		
		// Change to lowercase and remove leading and trailing whitespace
		return string.trim().toLowerCase(dfltLocal);	
	}
	
	/**
	 * This method receives a collection of strings and adds them
	 * to the repository.
	 * @see tagger.TagRepository#addTags(java.util.Collection)
	 */
	@Override
	public void addTags(Collection<String> tags) throws TagAlreadyExistsException {
		
		Collection<String> fixedTags = new ArrayList<String>(tags.size());
		
		// For each tag string
		for (String curTag : tags)
		{
			// Fix current string and add to list
			fixedTags.add(fixString(curTag));
		}
		
		// Add fixed strings to database
		DAL.addTags(fixedTags);
	}

	/**
	 * This method returns all the tags, ordered by frequency - most frequent first.
	 * @see tagger.TagRepository#getTagListFreqOrdered()
	 */
	@Override
	public Collection<TagFreq> getTagListFreqOrdered() {

		return DAL.getTagListFreqOrdered();
	}
	
	/**
	 * This method removes a tag from the repository.
	 * TODO: Say what happens if tag is associated to files or doesn't exist.
	 * @see tagger.TagRepository#removeTag(java.lang.String)
	 */
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
	public Collection<String> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		
		return DAL.searchByTag(includedTags, excludedTags);
	}

	@Override
	public boolean tagExists(String tag) {

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
	public void unTagFileAll(String file){
		DAL.unTagFileAll(file);
	}
	
	public Collection<String> getTagsOfFile(String file){
		
		return DAL.getTagsOfFile(file);
	}
	
	@Override
	public void deleteAll(){
		DAL.deleteAll();
	}


	/**
	 * TODO: Doc this method...
	 * @param fileEvent
	 */
	public void processFileChangeTagging(FileEvent fileEvent) {
		
		Path file = fileEvent.getFile();
		
		switch (fileEvent.getEvent()) {
			case MODIFIED:
				System.out.println("blew it");
				// Remove tags for file (and re-tag it using fall-thru to file creation event)
				DAL.unTagFileAll(file.toString());

			case CREATED:
				System.out.println("you blew it");
				Collection<String> fileTags = new TreeSet<String>(); // XXX: Consider a different data structure if tagging is slow

				// For each tagging algorithm
				Collection<AutoTagger> usedAlgorithms = autoTaggers.values();
				for (AutoTagger curAutoTagger : usedAlgorithms) {
					
					try {
						// Get tags for file
						Collection<String> tagsForFile =
							curAutoTagger.autoTag(new File(file.toString()));

						// Perform safety check on returned result
						if (tagsForFile != null) {
						
							// Add automatically generated tags of file to collection
							fileTags.addAll(tagsForFile); // XXX: Consider using just a path or just a file, if tagging is slow
						}
					} catch (Exception e) {

						// Problem with tagger, continue to next one
						continue; // TODO: Consider attempting to report the problem somehow
					}
				}

				// Tag the file with generated tags
				DAL.tagFile(file.toString(), fileTags);
				
				
				
				break;
			case DELETED:

				// The file was deleted, so delete it
				DAL.removeFile(file.toString());
				
				break;
		}
		
	}
	
	/**
	 * Sets the automatic tagging to use the given automatic taggers.
	 * @param autoTaggers Automatic taggers to use.
	 */
	@Override
	public void setAutoTaggers(Map<File,AutoTagger> autoTaggers) {
		
		// Set automatic taggers to use
		this.autoTaggers = autoTaggers;
		
		// Save list of auto taggers so that they can be loaded again in the future
		saveTaggerData();
	}
	
	/**
	 * Gets the automatic taggers in use.
	 * @return Automatic taggers used. This view of the automatic
	 * taggers can be changed and will be affected in the tagger.
	 */
	@Override
	public Map<File,AutoTagger> getAutoTaggers() {

		// Return the automatic taggers in use
		return autoTaggers;
	}
	
	public void dropTables(){
		DAL.dropTables();
	}	
	
	/* delete after we're done */	
	public void removeFile(String file){
		//TODO : delete after we're done
		DAL.removeFile(file);
	}
	
	/**
	 * Save used algorithm filenames to file.
	 */
	private void saveTaggerData() {

		try {
			FileOutputStream fileOutStream =
				new FileOutputStream(FILENAME_PERSISTENCE );
		
			ObjectOutputStream objOutStream =
				new ObjectOutputStream(fileOutStream);
			
			// Save listened paths data to file
			Set<File> usedAlgoFilesSet = autoTaggers.keySet();
			Collection<File> usedAlgoFilesCol =
				new ArrayList<File>(usedAlgoFilesSet.size());
			for (File curFile : usedAlgoFilesSet) {
				usedAlgoFilesCol.add(curFile);
			}
			objOutStream.writeObject(usedAlgoFilesCol);
			objOutStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Load used algorithm filenames from file.  
	 * @throws FileNotFoundException if persistence file doesn't exist.
	 */
	@SuppressWarnings("unchecked")
	private void loadTaggerData() {

		try {
			File filePersistence =
				new File(FILENAME_PERSISTENCE);
	
			FileInputStream fileInStream;

			fileInStream = new FileInputStream(filePersistence);

			ObjectInputStream objInStream =
				new ObjectInputStream(fileInStream);

			// Read listened paths data from file
			Collection<File> autoTaggerFiles =
				(Collection<File>) objInStream.readObject();
			objInStream.close();
			
			// Load the automatic taggers
			autoTaggers.clear();
			for (File curFile : autoTaggerFiles) {
				
				autoTaggers.put(curFile,
								AutoTaggerLoader.getAutoTagger(curFile));
			}
		} catch (FileNotFoundException e1) {

			// Ignore - file probably does not exist YET
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
