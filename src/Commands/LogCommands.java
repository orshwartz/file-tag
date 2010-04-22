package Commands;

import log.Log;



public class LogCommands implements Commands{

	private Log theLog;
	
	public LogCommands(Log log){//Ctor	
		theLog =log;}
	
	private class writeMessage implements Command{


		public void execute() {
		  //theLog.writeMessage(..);
		}
	}
	
	private class clearLog implements Command{


		public void execute() {
		  //theLog.clearLog()(..);
		}
	}
}
