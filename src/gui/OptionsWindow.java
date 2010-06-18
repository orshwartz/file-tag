package gui;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import commander.CommandManager;
import commander.CommandManager.CmdCodes;


/**
 * @author Itai Evron
 *
 */
public class OptionsWindow {
	
	public CommandManager commander;
	
	private static Shell window;
	private static Group group;
	private DirectoryDialog directoryDialog = null;
	private String selectedDir;
     
     

	
	
	public OptionsWindow(CommandManager commander){
		this.commander = commander;	
	}
	
	
	public void makeWindow(){
		window = new Shell(MainAppGUI.display);
		window.setLayout(new GridLayout(2,true));
		window.setText("Options and Settings");
		window.setSize(300,340);
		group=new Group(window,SWT.SHADOW_IN);
		group.setLayout(new GridLayout(2,false));
    	group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
    	
    	directoryDialog =  new DirectoryDialog(window);
		
	  /* ---------------------- Parts --------------------------------*/
		
    	final Label label = new Label(group, SWT.BORDER | SWT.WRAP);
    	new Label(group, SWT.BORDER | SWT.WRAP);
        label.setBackground(MainAppGUI.display.getSystemColor(SWT.COLOR_WHITE));
        label.setText("Select a dir/file by clicking the buttons below.");
		final Button slctDirBtn = new Button(group, SWT.PUSH);
		GridData slctDirBtnData = new GridData();
		slctDirBtnData.widthHint = 103;
		slctDirBtnData.heightHint = 38;
    	slctDirBtn.setLayoutData(slctDirBtnData);
		slctDirBtn.setText("Select a directory");
		
		
		
		
		slctDirBtn.addListener(SWT.Selection, new Listener() {
    		public void handleEvent(Event e) {
    			switch (e.type) {
    	          case SWT.Selection:
    	        	  directoryDialog.setFilterPath(selectedDir);
    	        	  directoryDialog.setMessage("Please select a directory and click OK");
 
    	        	  String dir = directoryDialog.open();
    	              if(dir != null) {
    	                label.setText("Selected dir: " + dir);
    	                selectedDir = dir;
    	              }

    	   
    			}	
    		   }
    	});
		
	}
	
	public void open(){
		 window.open();
		  while (!window.isDisposed()) {
		      if (!MainAppGUI.display.readAndDispatch())
		    	  MainAppGUI.display.sleep();
		    }
	 }

}
