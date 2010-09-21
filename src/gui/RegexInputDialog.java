package gui;
import java.nio.file.FileSystems;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;


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
public class RegexInputDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Label lblEnterRegex;
	private Text txtRegex;
	private Button btnCancel;
	private Button btnOK;
	private Label lblRegexValidityStatus;
	
	private Color okStatusColor = SWTResourceManager.getColor(0, 0, 0);
	private Color badStatusColor = SWTResourceManager.getColor(255, 0, 0);
	private String okStatusMsg = new String("Regular expression OK.");
	private String badStatusMsg = new String("Invalid expression (see tooltip).");
	private String okStatusTooltipFormat = new String("");
	private String badStatusTooltipFormat = new String("%s");
	
	private String returnedRegex = null;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			RegexInputDialog inst = new RegexInputDialog(shell, SWT.NULL);
			String newRegex = inst.open();
			
			if (newRegex != null) {
				System.out.println(newRegex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegexInputDialog(Shell parent, int style) {
		super(parent, style);
	}

	public String open() {
		try {
			dialogShell = new Shell(getParent(), SWT.DIALOG_TRIM |
												 SWT.APPLICATION_MODAL);

			{
				//Register as a resource user - SWTResourceManager will
				//handle the obtaining and disposing of resources
				SWTResourceManager.registerResourceUser(dialogShell);
			}
			
			
			GridLayout dialogShellLayout = new GridLayout();
			dialogShellLayout.makeColumnsEqualWidth = true;
			dialogShellLayout.numColumns = 2;
			dialogShell.setLayout(dialogShellLayout);			
			dialogShell.setSize(302, 163);
			dialogShell.setText("Regular Expression Input");
			{
				lblEnterRegex = new Label(dialogShell, SWT.NONE);
				GridData lblEnterRegexLData = new GridData();
				lblEnterRegexLData.horizontalSpan = 2;
				lblEnterRegex.setLayoutData(lblEnterRegexLData);
				lblEnterRegex.setText("Enter file filter regular expression:");
			}
			{
				GridData txtRegexLData = new GridData();
				txtRegexLData.widthHint = 191;
				txtRegexLData.heightHint = 14;
				txtRegexLData.horizontalSpan = 2;
				txtRegex = new Text(dialogShell, SWT.BORDER);
				txtRegex.setLayoutData(txtRegexLData);
				txtRegex.addListener(SWT.Modify, new Listener() {

					@Override
					public void handleEvent(Event arg0) {

						boolean isRegexValid = true;
						String statusMsg = null;
						Color color = null;
						String tooltipMsg = null;
						String exceptionMsg = new String("");
						
						try {
							FileSystems.getDefault().getPathMatcher("regex:"+txtRegex.getText());
						}
						catch (PatternSyntaxException e) {
							
							// Use bad error status
							isRegexValid = false;
							exceptionMsg = e.getDescription() + " near index " + e.getIndex();
						}
						
						// If regular expression valid
						if (isRegexValid) {
							
							statusMsg = okStatusMsg;
							color = okStatusColor;
							tooltipMsg = String.format(okStatusTooltipFormat,
													   exceptionMsg);
							
						// Else, regular expression is invalid
						} else {
							
							statusMsg = badStatusMsg;
							color = badStatusColor;
							tooltipMsg = String.format(badStatusTooltipFormat,
													   exceptionMsg);							
						}
						
						lblRegexValidityStatus.setText(statusMsg);
						lblRegexValidityStatus.setForeground(color);
						lblRegexValidityStatus.setToolTipText(tooltipMsg);
						btnOK.setEnabled(isRegexValid);
					}
				});
				txtRegex.addListener(SWT.KeyUp, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						// If "enter" key hit and expression can be submitted
						if ((arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR) &&
							btnOK.isEnabled()) {
							
							// Fake a click on the "OK" button
							btnOK.getListeners(SWT.Selection)[0].handleEvent(arg0);
						}
					}
				});
			}
			{
				GridData lblRegexValidityStatusLData = new GridData();
				lblRegexValidityStatusLData.horizontalSpan = 2;
				lblRegexValidityStatusLData.widthHint = 148;
				lblRegexValidityStatusLData.heightHint = 14;
				lblRegexValidityStatus = new Label(dialogShell, SWT.NONE);
				lblRegexValidityStatus.setLayoutData(lblRegexValidityStatusLData);
				lblRegexValidityStatus.setText(okStatusMsg);
				lblRegexValidityStatus.setForeground(okStatusColor);
				lblRegexValidityStatus.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));
			}
			{
				btnOK = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				GridData btnOKLData = new GridData();
				btnOKLData.horizontalAlignment = GridData.END;
				btnOK.setLayoutData(btnOKLData);
				btnOK.setText("OK");
				btnOK.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						// Save the new requested regular expression
						returnedRegex = txtRegex.getText();
						
						// Close the dialog
						dialogShell.close();
					}
				});
			}
			{
				btnCancel = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				GridData btnCancelLData = new GridData();
				btnCancel.setLayoutData(btnCancelLData);
				btnCancel.setText("Cancel");
				btnCancel.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						
						// Close the dialog
						dialogShell.close();
					}
				});
			}
//			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Return the requested regular expression
		return returnedRegex;
	}
	
}
