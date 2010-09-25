package tagger.autotagger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Loads a class from its file, without regard to directory/package
 * correlation. The class file is read and loaded.
 * @author Or Shwartz
 */
public class ClassDumpLoader extends ClassLoader {
	
	/**
	 * <CODE>ClassDumpLoader</CODE> constructor.
	 */
	public ClassDumpLoader() {
	}

	/**
	 * Loads a class from its file, without regard to directory/package correlation.
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String filename) throws ClassNotFoundException {
		
		File classFile = new File(filename);
		
		// If class file doesn't exist
		if (!classFile.exists()) {
			
			// Throw class not found exception
			throw new ClassNotFoundException(filename);
		}
		
		try {
			byte[] array = new byte[1024];
			BufferedInputStream inStream =
				new BufferedInputStream(new FileInputStream(classFile)); 
			ByteArrayOutputStream outStream =
				new ByteArrayOutputStream(array.length);

			// Read the class file
			int length = inStream.read(array);
			while (length > 0) {
				
				// Write read data to output stream
				outStream.write(array, 0, length);
				length = inStream.read(array);
			}
			
			// Create the class from its binary, without regard to its name or package
			return defineClass(null, outStream.toByteArray(), 0, outStream.size());
			
		} catch (IOException exception) {
			
			throw new ClassNotFoundException("Error reading file: " + filename,
											 exception);
		}
	}
	
	
	// TODO: Remove (it's here for test purposes only)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		ClassDumpLoader cdl = new ClassDumpLoader();
		
		try {
			Class<AutoTagger> autoTaggerClass =
				(Class<AutoTagger>) cdl.loadClass("c:/temp/TaggerBySize.class");
			
			AutoTagger autoTagger = autoTaggerClass.newInstance();

			System.out.println(autoTagger.getAuthor());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
