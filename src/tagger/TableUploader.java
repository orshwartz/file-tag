package tagger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TableUploader {

	BufferedReader br;
	String fileName = "Tables.sql";
	
	public TableUploader(){
		
		System.out.println(this.getClass().getName() + " up.");
		
		/*try {
			br = new BufferedReader( new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
