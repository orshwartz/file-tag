package tagger.autotagger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

final class ClassDumpLoader extends ClassLoader {
	
	private final File classPath;

	/**
	 * Constructor for <CODE>ClassDumpLoader</CODE>.
	 * @param classPath - path to search classes loaded classes.
	 */
	public ClassDumpLoader(String classPath) {

		// Save class path object
		this.classPath = new File(classPath);
	}

	/**
	 * Loads a class from its file TODO: Doc this.
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		
		File classFile = // TODO: Check how to locate the class well (Maybe settle for file path, and then maybe remove the constructor)
			new File(classPath + "/" + name.replace('.', '/') + ".class");
		
		// If class file doesn't exist
		if (!classFile.exists()) {
			
			// Throw class not found exception
			throw new ClassNotFoundException(name);
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
			
			// Didn't work because name was somewhat inconsistent with location... or something
			// like that. Instead... I pass null as the name and it doesn't care! Woohoo!
//			return defineClass(name, outStream.toByteArray(), 0, outStream.size());
			return defineClass(null, outStream.toByteArray(), 0, outStream.size());
			
		} catch (IOException exception) {
			
			throw new ClassNotFoundException(name, exception);
		}
	}
	
	
	// TODO: Remove (it's here for test purposes only)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		ClassDumpLoader cdl = new ClassDumpLoader("c:/TEMP");
		
		try {
			Class<AutoTagger> autoTaggerClass =
				(Class<AutoTagger>) cdl.loadClass("TaggerBySize");
			
			AutoTagger autoTagger = autoTaggerClass.newInstance();
			
////////// It works!!! It doesn't care that it's c:\temp\TaggerBySize.class
////////// even though it's the class is tagger.autotagger.TaggerBySize
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
