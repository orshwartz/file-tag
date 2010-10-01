package gui;

import static commander.CommandManager.CmdCodes.*;

import java.util.Collection;

import log.EventType;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import commander.CommandManager;
import commander.CommandManager.CmdCodes;
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
public class MonitorWindow extends Dialog {
	
	public CommandManager commander;
	private boolean lstn;
	private boolean reboot;
	
	private Table tblLogMsgs;
	private Label lblSysLog;
	public Button onOffBtn;
	private Button clearLogBtn;
	private ProgressIndicator progressIndicator1;
	private Button rebootSourcesBtn;
	private Button btnRebootTagger;
	private Button btnLstnrActiveToggle;
	private static Shell window;
	
	private Thread thread;
	
	public MonitorWindow(Shell parent, CommandManager commander) {

		super (parent, SWT.NONE);
		
		this.commander = commander;
	}
	
	public void makeWindow(){
		window = new Shell(MainAppGUI.display, SWT.CLOSE |
				   SWT.TITLE |
				   SWT.MAX |
				   SWT.MIN |
				   SWT.RESIZE);
		GridLayout windowLayout = new GridLayout();
		windowLayout.numColumns = 2;
		window.setLayout(windowLayout);
		window.setText("Control and Monitor");
		{
			lblSysLog = new Label(window, SWT.NONE);
			lblSysLog.setText("System Log");
			GridData label1LData = new GridData();
			label1LData.horizontalSpan = 2;
			lblSysLog.setLayoutData(label1LData);
			lblSysLog.setBounds(12, 12, 115, 30);
		}
		{
			tblLogMsgs = new Table(window, SWT.V_SCROLL | SWT.BORDER);
			tblLogMsgs.setLinesVisible(true);
			GridData table1LData = new GridData();
			table1LData.horizontalAlignment = GridData.FILL;
			table1LData.grabExcessHorizontalSpace = true;
			table1LData.grabExcessVerticalSpace = true;
			table1LData.verticalSpan = 4;
			table1LData.verticalAlignment = GridData.FILL;
			tblLogMsgs.setLayoutData(table1LData);
			tblLogMsgs.setHeaderVisible(true);
			
			String titles[] = { "Date", "Time", "Message" };
			
			for (int i = 0; i < 3; i++) {
				TableColumn column = new TableColumn(tblLogMsgs, SWT.NONE);
				column.setText(titles[i]);
			}
			
			updateLog();
			for(int i = 0; i<3; i++){
				tblLogMsgs.getColumn(i).pack();
			}
		}
		{
			btnRebootTagger = new Button(window, SWT.PUSH | SWT.CENTER);
			btnRebootTagger.setText("Clear all tags");
			GridData button2LData = new GridData();
			button2LData.horizontalAlignment = GridData.FILL;
			btnRebootTagger.setLayoutData(button2LData);
			btnRebootTagger.setBounds(344, 88, 117, 43);
			
			btnRebootTagger.addListener(SWT.Selection, new Listener(){

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
										CmdCodes.TAGGER_CLEAR_ALL_TAGS);
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
			rebootSourcesBtn.setText("Re-tag all files");
			GridData rebootSourcesBtnLData = new GridData();
			rebootSourcesBtnLData.horizontalAlignment = GridData.FILL;
			rebootSourcesBtn.setLayoutData(rebootSourcesBtnLData);
			rebootSourcesBtn.setBounds(344, 137, 117, 58);
			
			rebootSourcesBtn.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					//open a new thread
					thread = new Thread(new Runnable(){

						@Override
						public void run() {
							
							//TODO : should it be here ?
							//commander.getCommand(LSTNR_RETAG_ALL_FILES).execute(null);
							reboot = true;
							Object[] params = {thread};
							commander.getCommand(CmdCodes.LSTNR_RETAG_ALL_FILES).execute(params);
							reboot = false;
						}
						
					});		
					thread.start();
				}
				
			});
			
			
		}
		{
			onOffBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			onOffBtn.setText("Stop/Start Reboot");
			GridData onOffBtnLData = new GridData();
			onOffBtnLData.horizontalAlignment = GridData.FILL;
			onOffBtn.setLayoutData(onOffBtnLData);
			onOffBtn.setBounds(344, 195, 117, 30);
			
			onOffBtn.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event arg0) {
					
					if(reboot == false){
						MessageBox mBox = new MessageBox(window, 
								SWT.ICON_INFORMATION);
						
						mBox.setMessage("Reboot process is not active at the moment");
						mBox.open();
					}
					else{
						synchronized(thread){
						commander.getCommand
								(TAGGER_SET_REBOOT_MODE).execute(null);	
						if((Boolean) commander.getCommand
								(TAGGER_GET_REBOOT_MODE).execute(null)){								
		
								thread.notify();
							
						}
						
						
						}
					}
					
				}
				
			});
			
		}
		{
			btnLstnrActiveToggle = new Button(window, SWT.PUSH | SWT.CENTER);
			
			btnLstnrActiveToggle.setText("Deactivate Listener");

			lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
			if(lstn == false)
				btnLstnrActiveToggle.setText("Activate Listener");
			else {
				GridData button1LData = new GridData();
				button1LData.horizontalAlignment = GridData.FILL;
				button1LData.verticalAlignment = GridData.BEGINNING;
				btnLstnrActiveToggle.setLayoutData(button1LData);
					btnLstnrActiveToggle.setText("Deactivate Listener");
			}
			
			btnLstnrActiveToggle.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					
					if(lstn == false){
						commander.getCommand(LSTNR_ACTIVATE).execute(null);
						btnLstnrActiveToggle.setText("Deactivate Listener");
						lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
					}
					else{
						commander.getCommand(LSTNR_DEACTIVATE).execute(null);
						btnLstnrActiveToggle.setText("Activate Listener");
						lstn = (Boolean) commander.getCommand(LSTNR_ASK_ACTIVE).execute(null);
					}
				}});
			
			
			
		}
		{
			clearLogBtn = new Button(window, SWT.PUSH | SWT.CENTER);
			clearLogBtn.setText("Clear Log");
			GridData clearLogBtnLData = new GridData();
			clearLogBtnLData.horizontalAlignment = GridData.FILL;
			clearLogBtn.setLayoutData(clearLogBtnLData);
			clearLogBtn.setBounds(344, 264, 117, 41);
			
			clearLogBtn.addListener(SWT.Selection, new Listener(){
				
				@Override
				public void handleEvent(Event arg0) {
					
					MessageBox mBox = new MessageBox(window, 
							SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					
					mBox.setMessage("This action will clean all the messages from the log");
					
					if (mBox.open() == SWT.OK)
					{
						commander.getCommand(LOG_CLEAR).execute(null);
						
						// Reflect only this change in the GUI (others don't reflect)
						tblLogMsgs.clearAll();
					}
				}
				
			});
		}
		{
			progressIndicator1 = new ProgressIndicator(window);
			GridData progressIndicator1LData = new GridData();
			progressIndicator1LData.heightHint = 30;
			progressIndicator1LData.horizontalAlignment = GridData.FILL;
			progressIndicator1.setLayoutData(progressIndicator1LData);
		}
		
	}
		
	
	
	@SuppressWarnings("unchecked")
	public void updateLog(){
		TSCommand getMsgCmd = commander.getCommand(LOG_GET_MESSAGES);
		Collection<String> msgs = (Collection<String>) getMsgCmd.execute(null);
		
		for (String str : msgs){
			TableItem item = new TableItem(tblLogMsgs,SWT.None);
			
			String[] parts = str.split(" ", 3);
			
			item.setText(0,parts[0]);
			item.setText(1,parts[1]);
			item.setText(2,parts[2]);
		}
		
	}
	
	
	
	public void open() {

		makeWindow();
		window.open();
		window.pack();
		while (!window.isDisposed()) {
			if (!MainAppGUI.display.readAndDispatch())
				MainAppGUI.display.sleep();
		}
	}
}
