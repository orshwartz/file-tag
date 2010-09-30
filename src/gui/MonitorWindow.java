package gui;

import static commander.CommandManager.CmdCodes.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

import listener.ListenedDirectory;
import log.EventType;
import org.eclipse.jface.dialogs.ProgressIndicator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import commander.CommandManager;
import commander.commands.TSCommand;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MonitorWindow {
	
	public CommandManager commander;
	private boolean lstn;
	
	private Table table1;
	private Label label1;
	private Button clearLogBtn;
	private ProgressIndicator progressIndicator1;
	private Button rebootSourcesBtn;
	private Button button2;
	private Button button1;
	private static Shell window;
	
	public MonitorWindow(CommandManager commander) {

		this.commander = commander;
	}
	
	public void makeWindow(){
		window = new Shell(MainAppGUI.display, SWT.CLOSE |
				   SWT.TITLE |
				   SWT.MAX |
				   SWT.MIN |
				   SWT.RESIZE);
		window.setText("Control and Monitor");
		window.setSize(481, 383);
		{
			table1 = new Table(window, SWT.V_SCROLL | SWT.BORDER);
			table1.setBounds(12, 30, 326, 275);
			table1.setLinesVisible(true);
		    table1.setHeaderVisible(true);
		    
		    String titles[] = { "Date", "Time", "Message" };
		    
		    for (int i = 0; i < 3; i++) {
		        TableColumn column = new TableColumn(table1, SWT.NONE);
		        column.setText(titles[i]);
		      }

				updateLog();
			for(int i = 0; i<3; i++){
				table1.getColumn(i).pack();
			}
		}
		{
			button1 = new Button(window, SWT.PUSH | SWT.CENTER);
			
			button1.setText("Deactivate Listener");
			button1.setBounds(344, 30, 117, 52);
			
			lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
			if(lstn == false)
				button1.setText("Activate Listener");
			else
				button1.setText("Deactivate Listener");
			
			button1.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					if(lstn == false){
						commander.getCommand(LSTNR_ACTIVATE).execute(null);
						button1.setText("Deactivate Listener");
						lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
					}
					else{
						commander.getCommand(LSTNR_DEACTIVATE).execute(null);
						button1.setText("Activate Listener");
						lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
					}
				}});
			
			
			
		}
		{
			label1 = new Label(window, SWT.NONE);
			label1.setText("System Log");
			label1.setBounds(12, 12, 115, 30);
		}
		{
			button2 = new Button(window, SWT.PUSH | SWT.CENTER);
			button2.setText("Reboot Tagger");
			button2.setBounds(344, 88, 117, 43);
			
			button2.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					MessageBox mBox = new MessageBox(window, 
							SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					
					mBox.setMessage("This action will delete all of the tags and " +
							"the file-to-tags attachments in the system");
					
					int choice = mBox.open();
					
						switch(choice){
							case SWT.OK :
								
								
								TSCommand RbtCmd = commander.getCommand(
										TAGGER_REBOOT);
								RbtCmd.execute(null);
								
								//inform Log
								TSCommand writeMsgCmd = commander.getCommand(
										LOG_WRITE_MESSAGE);
								Object[] params = {EventType.Tagger_Reboot};
								writeMsgCmd.execute(params);
								
								System.out.println("ok");
								break;
							case SWT.CANCEL :
								break;
						}
				}
				
				
			});
			
			
		}
		{
			rebootSourcesBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			rebootSourcesBtn.setText("Reboot Listened Sources");
			rebootSourcesBtn.setBounds(344, 137, 117, 58);
			
			rebootSourcesBtn.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					//open a new thread
					new Thread(new Runnable(){

						@Override
						public void run() {
							commander.getCommand(LSTNR_REBOOT).execute(null);
							
						}
						
					}).start();					
				}
				
			});
			
			
		}
		{
			progressIndicator1 = new ProgressIndicator(window);
			progressIndicator1.setBounds(12, 313, 326, 30);
		}
		{
			clearLogBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			clearLogBtn.setText("Clear Log");
			clearLogBtn.setBounds(344, 201, 117, 41);
			
			clearLogBtn.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					MessageBox mBox = new MessageBox(window, 
							SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					
					mBox.setMessage("This action will clean all the messages from the log");
					
					int choice = mBox.open();
					switch(choice){
						case SWT.OK :
							commander.getCommand(LOG_CLEAR).execute(null);
							break;
						case SWT.CANCEL :
							break;
					}
				}
				
			});
		}

	}
		
	
	
	public void updateLog(){
		TSCommand getMsgCmd = commander.getCommand(LOG_GET_MESSAGES);
		Collection<String> msgs = (Collection<String>) getMsgCmd.execute(null);
		
		for (String str : msgs){
			TableItem item = new TableItem(table1,SWT.None);
			
			String[] parts = str.split(" ", 3);
			
			item.setText(0,parts[0]);
			item.setText(1,parts[1]);
			item.setText(2,parts[2]);
		}
		
	}
	
	
	
	public void open() {

		makeWindow();
		window.open();
		while (!window.isDisposed()) {
			if (!MainAppGUI.display.readAndDispatch())
				MainAppGUI.display.sleep();
		}
	}
}
