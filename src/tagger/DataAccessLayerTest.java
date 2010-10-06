package tagger;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;

public class DataAccessLayerTest extends TestCase {
	
	DataAccessLayer DAL = new DataAccessLayer();
	
	
	
	public final void testAddandRemoveTags(){
		/* A check of addTags and removeTags methods */
		
		boolean check;
		Collection<String> col = new LinkedList<String>();
		col.add("Thor");
		col.add("Odin");
		col.add("Loki");
		
		try {
			DAL.removeTag("Thor");
			DAL.removeTag("Odin");
			DAL.removeTag("Loki");
		} catch (TagNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// add tags to the repository
		try {
			DAL.addTags(col);
		} catch (TagAlreadyExistsException e) {
			// ignore
		}

		
		// check if they all exist
		check = DAL.tagExists("Loki");
		if(check == false)
			fail("Tag dosen't exist where it should");
		
		check = DAL.tagExists("Odin");
		if(check == false)
			fail("Tag dosen't exist where it should");
		
		// the tag "Donar" never been added to the repository
		check = DAL.tagExists("Donar");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
		
		// remove each tag and check if it still exists
		try {
			DAL.removeTag("Thor");
		} catch (TagNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			check = DAL.tagExists("Thor");
			if(check == true)
				fail("Tag exists after removeTag command");
		
		
		try {
			DAL.removeTag("Odin");
		} catch (TagNotFoundException e) {
			fail("Tag dosen't exist where it should");
		}
		
			check = DAL.tagExists("Odin");
			if(check == true)
				fail("Tag exists where it shouldn't");
		
		
		try {
			DAL.removeTag("Loki");
		} catch (TagNotFoundException e) {
			fail("Tag dosen't exist where it should");
		}
		
			check = DAL.tagExists("Loki");
			if(check == true)
				fail("Tag exists where it shouldn't");
		
		
		// the tag "Donar" never been added to the repository
		try {
			DAL.removeTag("Donar");
		} catch (TagNotFoundException e) {
			System.out.println("Donar dosen't exist thus can not be removed.");
		}
		
			check = DAL.tagExists("Donar");
			if(check == true)
				fail("Tag exists where it shouldn't");
	}
	
	
	public final void testTagFile(){
		
		/*A check of the tagFile, unTagfile and unTagFileAll methods */
		
		boolean check;
		Collection<String> col = new LinkedList<String>();
		col.add("Thor");
		
		String file = "file";
		
		
		DAL.tagFile(file, col);
		
		//check if the file got into the repository
		check = DAL.fileExists("file");
		if(check == false)
			fail("File dosen't exist where it should");
		//check if the tag got into the repository
		check = DAL.tagExists("Thor");
		if(check == false)
			fail("Tag dosen't exist where it should");
		
		//untag the tag from the file
		DAL.untagFile("file","Thor");
		
		//"Thor" is no longer tagging any file, thus should be deleted
		// from the tags table
		check = DAL.tagExists("Thor");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
		col.remove("Thor");
		
		//now tagging two tags
		col.add("Odin");
		col.add("Loki");
		
		DAL.tagFile("file", col);
		//check if the tag got into the repository

		check = DAL.tagExists("Odin");
		if(check == false)
			fail("Tag dosen't exist where it should");
		//check if the tag got into the repository

		check = DAL.tagExists("Loki");
		if(check == false)
			fail("Tag dosen't exist where it should");
		
		//untag all of the tags related to this file
		DAL.unTagFileAll("file");
		
		//"Odin" is no longer tagging any file, thus should be deleted
		// from the tags table
		check = DAL.tagExists("Odin");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
		//"Loki" is no longer tagging any file, thus should be deleted
		// from the tags table
		check = DAL.tagExists("Loki");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
	}
	
	public final void testRenameTag(){
		
		/* A check of the renameTag method */
	
		boolean check;
		Collection<String> col = new LinkedList<String>();
		
		col.add("Thor");
		
		try {
			DAL.addTags(col);
		} catch (TagAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Tag "Donar" is not in the repository
		check = DAL.tagExists("Donar");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
		//rename the tag
		DAL.renameTag("Thor", "Donar");
		
		// there is no longer a tag "Thor" in the repository
		check = DAL.tagExists("Thor");
		if(check == true)
			fail("Tag exists where it shouldn't");
		
		// now there is a tag "Donar" in the repository
		check = DAL.tagExists("Donar");
		if(check == false)
			fail("Tag dosen't exist where it should");
		
		try {
			DAL.removeTag("Donar");
		} catch (TagNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public final void testGetTagFreq(){
		
		/* A check of getTagFreq method */
		
		Collection<String> col = new LinkedList<String>();
		TagFreq tagFreq = new TagFreq(0,"Thor");
		int freq;
		
		col.add("Thor");
		
		// two tags will get the same tag
		String file00 = "file00";
		String file01 = "file01";
		
		// tag one of the two files
		DAL.tagFile("file00", col);
		// freq should be 1
		tagFreq = DAL.getTagFreq("Thor");
		freq = tagFreq.getFreq();
		
		if(freq != 1)
			fail("not correct freq");
		
		// tag the 2nd file
		DAL.tagFile("file01", col);
		// freq should be 2
		tagFreq = DAL.getTagFreq("Thor");
		freq = tagFreq.getFreq();
		
		if(freq != 2)
			fail("not correct freq");
		
		// remove the tag from one of them
		DAL.untagFile("file00", "Thor");
		
		// freq should be 1
		tagFreq = DAL.getTagFreq("Thor");
		freq = tagFreq.getFreq();
		
		if(freq != 1)
			fail("not correct freq");
		
		
	}
	
	public final void testSearch(){
		
		/* A check of searchByTag method */
		
		Collection<String> files = new LinkedList<String>();
		
		
		Collection<String> col = new LinkedList<String>();
		Collection<String> col2 = new LinkedList<String>();
		Collection<String> col3 = new LinkedList<String>();
		Collection<String> col4 = new LinkedList<String>();
		
		Collection<String> inc = new LinkedList<String>();
		Collection<String> exc = new LinkedList<String>();
		
		String file000 = "file000";
		String file001 = "file001";
		String file010 = "file010";
		String file011 = "file011";
		
		col.add("a");
		col.add("b");
		col.add("c");
		
		col2.add("b");
		col2.add("c");
		col2.add("d");
		
		col3.add("a");
		col3.add("c");
		col3.add("d");
		
		col4.add("z");
		col4.add("a");
		
		DAL.tagFile("file000", col);
		DAL.tagFile("file001", col2);
		DAL.tagFile("file010", col3);
		DAL.tagFile("file011", col4);
		
		inc.add("a");
		
		// inc : "a" ; 		exc : none ;
		System.out.println("should get file000, file010 and file011");
		files = DAL.searchByTag(inc,exc);
		
		for(String string : files){
			System.out.println(string);
		}
		
		exc.add("b");
		exc.add("z");
		
		// inc : "a" ; 		exc : "b", "z" ;
		System.out.println("should get file010");
		files = DAL.searchByTag(inc,exc);
		
		for(String string : files){
			System.out.println(string);
		}
		
		
		inc.remove("a");
		inc.add("c");
		exc.remove("b");
		exc.remove("z");
		
		// inc : "c" ; 		exc : none ;
		System.out.println("should get file000, file001, file010");
		files = DAL.searchByTag(inc,exc);
		
		for(String string : files){
			System.out.println(string);
		}
		
		inc.add("z");
		exc.add("a");
		
		// inc : "z" , "c" ;		exc : "a" ;
		System.out.println("should get file001");
		files = DAL.searchByTag(inc, exc);
		
		for(String string : files){
			System.out.println(string);
		}
		
		
	}
		
	
}
