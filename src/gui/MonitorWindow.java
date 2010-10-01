package gui;

import static commander.CommandManager.CmdCodes.LOG_CLEAR;
import static commander.CommandManager.CmdCodes.LOG_GET_MESSAGES;
import static commander.CommandManager.CmdCodes.LOG_WRITE_MESSAGE;
import static commander.CommandManager.CmdCodes.LSTNR_ACTIVATE;
import static commander.CommandManager.CmdCodes.LSTNR_ASK_ACTIVE;
import static commander.CommandManager.CmdCodes.LSTNR_DEACTIVATE;
import static commander.CommandManager.CmdCodes.LSTNR_REBOOT;
import static commander.CommandManager.CmdCodes.TAGGER_REBOOT;

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
	
	private Table tblLogMessages;
	private Label label1;
	private Button clearLogBtn;
	private ProgressIndicator progressIndicator1;
	private Button rebootSourcesBtn;
	private Button btnClearAllTags;
	private Button btnActDeactListener;
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
			tblLogMessages = new Table(window, SWT.V_SCROLL | SWT.BORDER);
			tblLogMessages.setBounds(12, 30, 326, 275);
			tblLogMessages.setLinesVisible(true);
		    tblLogMessages.setHeaderVisible(true);
		    
		    String titles[] = { "Date", "Time", "Message" };
		    
		    for (int i = 0; i < 3; i++) {
		        TableColumn column = new TableColumn(tblLogMessages, SWT.NONE);
		        column.setText(titles[i]);
		      }

				updateLog();
			for(int i = 0; i<3; i++){
				tblLogMessages.getColumn(i).pack();
			}
		}
		{
			btnActDeactListener = new Button(window, SWT.PUSH | SWT.CENTER);
			
			btnActDeactListener.setText("Deactivate Listener");
			btnActDeactListener.setBounds(344, 30, 117, 52);
			
			lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
			if(lstn == false)
				btnActDeactListener.setText("Activate Listener");
			else
				btnActDeactListener.setText("Deactivate Listener");
			
			btnActDeactListener.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					
					if(lstn == false){
						commander.getCommand(LSTNR_ACTIVATE).execute(null);
						btnActDeactListener.setText("Deactivate Listener");
						lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
					}
					else{
						commander.getCommand(LSTNR_DEACTIVATE).execute(null);
						btnActDeactListener.setText("Activate Listener");
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
			btnClearAllTags = new Button(window, SWT.PUSH | SWT.CENTER);
			btnClearAllTags.setText("Clear all tags");
			btnClearAllTags.setBounds(344, 88, 117, 43);
			
			btnClearAllTags.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					MessageBox mBox = new MessageBox(window, 
													 SWT.ICON_WARNING |
													 SWT.OK |
													 SWT.CANCEL);
					
					mBox.setMessage("This action will delete all file tagging from the system.\n\nAre you sure you want to clear all the tags?");

					// If user approved tag-clear action
					if (mBox.open() == SWT.OK) {
								
						// Clear the tags
						TSCommand rbtCmd = commander.getCommand(TAGGER_REBOOT);
						rbtCmd.execute(null);
						
						//inform Log FIXME: Do this from TAGGER_REBOOT command somehow!
						TSCommand writeMsgCmd = commander.getCommand(
								LOG_WRITE_MESSAGE);
						Object[] params = {EventType.Tagger_Reboot};
						writeMsgCmd.execute(params);
						
System.out.println("OK"); // TODO: Delete this debug print
					}
				}
			});
		}
		{
			rebootSourcesBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			rebootSourcesBtn.setText("Re-tag all files");
			rebootSourcesBtn.setBounds(344, 137, 117, 58);
			
			rebootSourcesBtn.addListener(SWT.Selection, new Listener(){

				@SuppressWarnings("unchecked")
				@Override
				public void handleEvent(Event arg0) {
					
					Collection<ListenedDirectory> gomel = null;
					gomel = (Collection<ListenedDirectory>) 
							commander.getCommand(LSTNR_REBOOT).execute(null);
					
					/*for(ListenedDirectory dir : gomel){
	
							String[] files = dir.getDirectory().list();
							
							for(String file : files){
								System.out.println(file);
							}
						}*/
					
					
					
				}
				
			});
			
			
		}
		{
			progressIndicator1 = new ProgressIndicator(window);
			progressIndicator1.setBounds(12, 313, 326, 30);
		}
		{
			clearLogBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			clearLogBtn.setText("Clear log");
			clearLogBtn.setBounds(344, 201, 117, 41);
			
			clearLogBtn.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					MessageBox mBox = new MessageBox(window, 
													 SWT.ICON_WARNING |
													 SWT.OK |
													 SWT.CANCEL);
					
					mBox.setMessage("This action will clean all the messages from the log");
					
					// If user approves change
					if (mBox.open() == SWT.OK) {
					
						// Clear the messages
						commander.getCommand(LOG_CLEAR).execute(null);
						
						// Reflect this change in the table (it's not updated
						// online so at least this will be shown)
						tblLogMessages.clearAll();
					}
				}
				
			});
		}

	}
		
	
	
	@SuppressWarnings("unchecked")
	public void updateLog(){
		TSCommand getMsgCmd = commander.getCommand(LOG_GET_MESSAGES);
		Collection<String> msgs = (Collection<String>) getMsgCmd.execute(null);
		
		for (String str : msgs){
			TableItem item = new TableItem(tblLogMessages,SWT.None);
			
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
