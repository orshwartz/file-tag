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
import listener.FileEvents;
import log.EventType;
import tagger.autotagger.AutoTagger;
import tagger.autotagger.AutoTaggerLoader;

/**
 * This class represents a tag repository - which is a repository associating
 * files to tags and uses events to keep track of those files.
 * @author Or Shwartz, Itay Evron
 */
public class TagRepositoryImpl implements TagRepository {

	private DataAccessLevel DAL = null;
    private final static Locale dfltLocal = Locale.getDefault();
    private Map<File, AutoTagger> autoTaggers = new HashMap<File,AutoTagger>();
	private String FILENAME_PERSISTENCE = "tagger_persistence.bin";
	private boolean rebootMode;
	
	
	/**
	 * <CODE>TagRepositoryImpl</CODE> constructor.
	 */
	public TagRepositoryImpl() {
		
		DAL = new DataAccessLevel();
		DAL.getConnection();
		
		// Restore needed automatic algorithms
		loadTaggerData();
		rebootMode = true;
		
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
	 * If there is any file attached to this tag, the attachment is being removed as well
	 * @see tagger.TagRepository#removeTag(java.lang.String)
	 */
	@Override
	public void removeTag(String tag) throws TagNotFoundException {

		DAL.removeTag(tag);
	}

	/**
	 * This method renames a tag from the repository
	 * @see tagger.TagRepository#renameTag(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTag(String oldName, String newName)
			throws TagNotFoundException, TagAlreadyExistsException {
		DAL.renameTag(oldName, newName);
	}

	/** 
	 * This method search in the repository for files that are attached to the
	 * <CODE> includedTags </CODE> tags and also not-attached to the <CODE>
	 *  excludedTags </CODE> tags
	 *  @param includedTags : tags to be included in the search
	 *  @param excludedTags : tags to be excluded in the search
	 *  @return A collection(Strings) of those files
	 * @see tagger.TagRepository#searchByTag(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<String> searchByTag(Collection<String> includedTags,
			Collection<String> excludedTags) {
		
 		return DAL.searchByTag(includedTags, excludedTags);
	}

	/** This method checks if a given <CODE> tag </CODE> exists in the repository
	 * @return true if the tag exists, false otherwise
	 * @see tagger.TagRepository#tagExists(java.lang.String)
	 */
	@Override
	public boolean tagExists(String tag) {
		
		return DAL.tagExists(tag);
	}

	/** This method tags a given <CODE> file </CODE> with the given tags
	 * @param file : the file to be tagged
	 * @param tags : a Collection of tags(Strings)
	 * @see tagger.TagRepository#tagFile(java.lang.String, java.util.Collection)
	 */
	@Override
	public void tagFile(String file, Collection<String> tags) throws FileNotFoundException {
		DAL.tagFile(file, tags);
		
	}

	/** This method removes the attachment from a given <CODE>file</CODE>
	 * to a given <CODE>tag</CODE>
	 * @see tagger.TagRepository#untagFile(java.lang.String, java.lang.String)
	 */
	
	@Override
	public void untagFile(String file, String tag) throws FileNotTaggedException {
		DAL.untagFile(file, tag);
		
	}
	
	/** Removes all the tags that are attached to a give <CODE> file </CODE>
	 * @see tagger.TagRepository#unTagFileAll(java.lang.String)
	 */
	@Override
	public void unTagFileAll(String file){
		DAL.unTagFileAll(file);
	}
	
	/** Gets a Collection of all the tags related to a give <CODE>
	 *  file </CODE>
	 *  @return a Collection of tags(Strings)
	 * @see tagger.TagRepository#getTagsOfFile(java.lang.String)
	 */
	@Override
	public Collection<String> getTagsOfFile(String file){
		return DAL.getTagsOfFile(file);
	}
	
	/** Deletes all the tags and files in the repository
	 * @see tagger.TagRepository#deleteAll()
	 */
	@Override
	public void deleteAll(){
		DAL.deleteAll();
	}


	/**
	 * This Method gets a <CODE> fileEvent </CODE> and responds to it according to the
	 * event the given FileEvent contains
	 * 
	 * There are four cases :
	 * file created : the method then tags the given file using the auto-taggers
	 * file modified : the method untag all the tags from the given file then do the same
	 * 			as in CREATED case
	 * file deleted : the method removes the file from the repository
	 * reboot process : the method checks if the file is in the repository and the last
	 *  time when it's modified, then tagging it
	 * 
	 * @param fileEvent : a given event
	 */
	public void processFileChangeTagging(FileEvent fileEvent) {
		
		Collection<String> fileTags = new TreeSet<String>();
		Path file = fileEvent.getFile();
		
		switch (fileEvent.getEvent()) {
			case MODIFIED:
				// Remove tags for file (and re-tag it using fall-thru to file creation event)
				DAL.unTagFileAll(file.toString());

			case CREATED:

				// Tag the file with generated tags
					fileTags = new TreeSet<String>();
					fileTags = CaseCreated(file);
					if(!fileTags.isEmpty()){
							DAL.tagFile(file.toString(), fileTags);
					}				
				break;
			case DELETED:

				// The file was deleted, so delete it
				DAL.removeFile(file.toString());
				
				break;
			
			case REBOOT:
				
				if( DAL.fileExists(file.toString()) ){
					
					long lastModOfFile = new File(file.toString()).lastModified();
					long modInDB = DAL.getTime(file.toString());
					
					if(modInDB < lastModOfFile){
						DAL.unTagFileAll(file.toString());
						
						
						fileTags = new TreeSet<String>();
						fileTags = CaseCreated(file);
						if(!fileTags.isEmpty()){
								DAL.tagFile(file.toString(), fileTags);
						}
					}
						
					
				}
				else{
					fileTags = new TreeSet<String>();
					fileTags = CaseCreated(file);
					if(!fileTags.isEmpty()){
							DAL.tagFile(file.toString(), fileTags);
					}
				}
				
				
				break;
		}
		
	}
	
	/**
	 * This method is used to prevent double-code in the created and reboot
	 * cases in the processFileChangeTagging method.
	 * The method uses the available auto-taggers algorithms to tag the given
	 * <CODE> file </CODE>
	 * @param file : a file to tag
	 * @return a Collection of tags(Strings) that the <CODE> file </CODE> will be
	 * tagged with
	 */
	private Collection<String> CaseCreated(Path file){
		
		Collection<String> fileTags = new TreeSet<String>();
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
				//continue; // TODO: Consider attempting to report the problem somehow
			}
		}
	
		return fileTags;

		
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

	@Override
	public synchronized void setRebootMode() {
		rebootMode = !rebootMode;
	}

	@Override
	public boolean getRebootMode() {

		return rebootMode;
	}

}
