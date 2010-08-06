package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;
import commander.CommandManager;
import static commander.CommandManager.CmdCodes.*;



/**
 * 
 * @author Itai Evron
 *
 */
public class ControlsWindow {

	public CommandManager commander;
	private static Shell window;
	private static Group group, group2;
	private boolean listenerOnOff;
	private boolean logOnOff;
	
	private List log;
	
	private static Group logGroup;
	
	public ControlsWindow(CommandManager commander){
		this.commander = commander;	
		listenerOnOff = false;
		logOnOff = false;
	}
	
	public void makeWindow(){
		// make the window
		window = new Shell(MainAppGUI.display);
		window.setText("Control and Monitor");
		//GridLayout sShellLayout = new GridLayout();
		//sShellLayout.makeColumnsEqualWidth = true;
		//sShellLayout.numColumns = 2;
		window.setLayout(new GridLayout(2,true));
		window.setSize(500, 340);
		
		group=new Group(window,SWT.SHADOW_IN);
    	group.setLayout(new GridLayout(2,false));
    	group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
    	
    	group2=new Group(window,SWT.SHADOW_IN);
    	group2.setLayout(new GridLayout(2,false));
    	group2.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
    	
    	/* ---------------------- Parts --------------------------------*/
    	
    	final Button logBtn = new Button(group, SWT.PUSH);
    	GridData logBtnData = new GridData();
    	logBtnData.widthHint = 203;
    	logBtnData.heightHint = 68;
		logBtn.setLayoutData(logBtnData);
    	logBtn.setText("Present Log");
    	logBtn.setImage(SWTResourceManager.getImage("gui/res/b1eye011.PNG"));
    	
    	
    	final Button lstBtn = new Button(group2, SWT.PUSH);
    	lstBtn.setText("Activate Listener");
    	GridData lstBtnData = new GridData();
    	lstBtnData.widthHint = 213;
    	lstBtnData.heightHint = 68;
		lstBtn.setLayoutData(lstBtnData);
    	lstBtn.setImage(SWTResourceManager.getImage("gui/res/Radio.PNG"));
		
    	
    	
    	
    	
    	/* Listen to listener button */
    	
    	lstBtn.addListener(SWT.Selection, new Listener() {
    		public void handleEvent(Event e) {
    			switch (e.type) {
    	          case SWT.Selection:
    	        	  if(listenerOnOff == false){
    	        		  commander.runCommand(LSTNR_ACTIVATE, null);
    	        		  listenerOnOff = true;
    	        		  lstBtn.setText("Deactivate Listener");
    	        		  
    	        	  }
    	        	  else{
    	        		  commander.runCommand(LSTNR_DEACTIVATE, null);
    	        		  listenerOnOff = false;
    	        		  lstBtn.setText("Activate Listener");
    	        	  }
    	   
    			}	
    		   }
    	});
    	
    	
    	/* Listen to log button */
    	
    	logBtn.addListener(SWT.Selection, new Listener() {
    		public void handleEvent(Event e) {
    			switch (e.type) {
    	          case SWT.Selection:
    	        	  if(logOnOff == false){
    	        		  window.setSize(500,640);
    	        		  logOnOff = true;
    	        		  logBtn.setText("Hide Log");

    	        	  }
    	        	  
    	        	  else{
    	        		  window.setSize(500,340);
    	        		  logOnOff = false;
    	        		  logBtn.setText("Present Log");
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
