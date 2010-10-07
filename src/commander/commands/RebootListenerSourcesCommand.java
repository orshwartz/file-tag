package commander.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.FileSystems;
import java.util.Collection;

import listener.FileEvent;
import listener.FileEvents;
import listener.ListenedDirectory;

/**
 * @author Itay Evron
 *
 */
public class RebootListenerSourcesCommand extends TSCommand {

	String completePath = null;
	
	@Override
	public Object execute(Object[] params) {

		Thread thisThread = (Thread) params[0];
		
		Collection<ListenedDirectory> dirs;
		dirs = getListener().getCollectionOfListenedPaths();
		
		
		for(final ListenedDirectory dir1 : dirs){
			
				
				String[] files = dir1.getDirectory().list(new FilenameFilter(){

					@Override
					public boolean accept(File dir, String name) {
	
						return getListener().checkRegex( new File(dir.getAbsolutePath() + 
								FileSystems.getDefault().getSeparator() +
								   name).toPath(),	dir1.getRegularExpressions() );

					}	
				});	
				
				
				for(String file : files){
					
					synchronized(thisThread){
						while(!getTagRepository().getRebootMode()){
							try {
								thisThread.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					completePath = dir1.getDirectory().getAbsolutePath() +
					FileSystems.getDefault().getSeparator() + file;
					
					Collection<String> tags = getTagRepository().processFileChangeTagging(new FileEvent(
							new File(completePath).toPath(),FileEvents.REBOOT));
							
					if (tags != null) {
						for (String curTag : tags) {
							getLog().writeMessage(completePath + " tagged " + curTag);
						}
					}
				}
		}
		
		
		getLog().writeMessage("Retagged all files.");
		
		return null;
	}

}
