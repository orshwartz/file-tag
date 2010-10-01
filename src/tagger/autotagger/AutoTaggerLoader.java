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

	/**
	 * This method loads an <CODE>AutoTagger</CODE> from the contents of the class file,
	 * without regard to the name of the file or the package it is in.
	 * It also enables loading a class from a JAR file, given the standard of
	 * class with binary name <I>some.package.of.ThisClass</I> should be in a JAR
	 * named <I>some.package.of.ThisClass.jar</I>.
	 * @param autoTaggerFile File of <TT>AutoTagger</TT> class to load. Naming doesn't matter
	 * as the class is read from binary- for simpler use. <TT>AutoTagger</TT> implementor can use
	 * whichever package he wants.
	 * @return An <CODE>AutoTagger</CODE> object from the given class file.
	 * @throws ClassNotFoundException
	 */
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
			if (autoTaggerFile.getName().toLowerCase().endsWith(".class")) {
				
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
			
			// Else, if received a JAR file
			else if (autoTaggerFile.getName().toLowerCase().endsWith(".jar")) {

				/// FIXME: I couldn't get jars
				/// to work before project submission :-(
				
				// Attempt loading the AutoTagger class from the jar,
				// given the standard for jar name 
				URLClassLoader jarLoader;
				Class<AutoTagger> clazz;
				String jarPath = "jar:file://" + autoTaggerFile.toString() + "!/";
				url = new File(jarPath).toURI().toURL();
				jarLoader = new URLClassLoader(new URL[]{url});

				clazz =
					(Class<AutoTagger>)jarLoader.loadClass(
						autoTaggerFile.getName().substring(
							0,
							autoTaggerFile.getName().lastIndexOf('.')));
				return clazz.newInstance();
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
