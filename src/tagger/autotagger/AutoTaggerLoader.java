package tagger.autotagger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

/**
 * This class is meant to enable simple dynamic loading of automatic
 * tagging algorithms.
 * @author Or Shwartz
 */
public class AutoTaggerLoader {

	@SuppressWarnings("unchecked")
	public static AutoTagger getAutoTagger(File autoTaggerFile) throws ClassNotFoundException {
		
		Class<AutoTagger> classAutoTagger = null;
		AutoTagger autoTagger = null;
		URL url = null;
		File dirOfAlgorithm = null;

		// Get directory of autoTaggerClass
		dirOfAlgorithm = autoTaggerFile.getParentFile();

		try {
			// Convert algorithm directory to URL representation (file:/...)
			url = dirOfAlgorithm.toURI().toURL();

			// Create a new class loader for algorithm's directory
			ClassLoader classLoader =
				new URLClassLoader(new URL[]{url});

			// If received a class file
			if (autoTaggerFile.toString().endsWith(".class")) {
				
				// Parse the class file
				JavaClass parsedClass =
					new ClassParser(autoTaggerFile.getCanonicalPath()).parse();
				
				// Get class name
				String className =
					parsedClass.getClassName();

				// Load the class file
				classAutoTagger =
					(Class<AutoTagger>)classLoader.loadClass(className);

				// Create a new instance of the automatic tagger class
				autoTagger = classAutoTagger.newInstance();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			// Class file might not be located according to its package
			// name, so attempt reading its class file
			ClassDumpLoader classDumpLoader = new ClassDumpLoader();
			try {
				classAutoTagger =
					(Class<AutoTagger>)classDumpLoader.findClass(autoTaggerFile.getAbsolutePath());
				
				autoTagger = classAutoTagger.newInstance();
			} catch (ClassNotFoundException e1) {
				
				throw e1;
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ClassFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return autoTagger;
	}
}
