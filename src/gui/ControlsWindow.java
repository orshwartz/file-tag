package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ControlsWindow {

	private static Shell window;
	private static Group group, group2;
	
	public void makeWindow(){
		// make the window
		window = new Shell(MainAppGUI.display);
		window.setText("Control and Monitor");
		//GridLayout sShellLayout = new GridLayout();
		//sShellLayout.makeColumnsEqualWidth = true;
		//sShellLayout.numColumns = 2;
		window.setLayout(new GridLayout(2,true));
		window.setSize(500, 110);
		group=new Group(window,SWT.SHADOW_IN);
    	group.setLayout(new GridLayout(2,false));
    	group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
    	group2=new Group(window,SWT.SHADOW_IN);
    	group2.setLayout(new GridLayout(2,false));
    	group2.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
    	
    	/* ---------------------- Parts --------------------------------*/
    	
    	final Button logBtn = new Button(group, SWT.CHECK);
    	new Label(group,SWT.NONE).setText("Present Log");
    	final Button lstBtnOn = new Button(group2, SWT.RADIO);
    	new Label(group2,SWT.NONE).setText("Activate listener");
    	final Button lstBtnOff = new Button(group2, SWT.RADIO);
    	new Label(group2,SWT.NONE).setText("Deactivate listener");
		
		
	}
	
	public void open(){
		 window.open();
		  while (!window.isDisposed()) {
		      if (!MainAppGUI.display.readAndDispatch())
		    	  MainAppGUI.display.sleep();
		    }
	 }
}
