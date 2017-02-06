package kr.pe.lahuman.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.pe.lahuman.passwd.PasswordGenerate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PasswdGenUI {

	public static void main(String[] args) {
		new PasswdGenUI();
	}
	  // Create Display instance
    final Display display = new Display();
    // Create top level Shell (pass display as parent)
    final Shell passGenShell = new Shell(display, SWT.CLOSE);
    
	public PasswdGenUI(){
		
	    // Set title line
	    passGenShell.setText("PASSWORD GENERATE");
	    passGenShell.setLayout(new FillLayout());
	    passGenShell.setSize(280, 330);
	    
		Composite wholeComposite = new Composite(passGenShell, SWT.NONE);
		GridLayout layoutWhole = new GridLayout();
		layoutWhole.numColumns = 1;
		wholeComposite.setLayout(layoutWhole);

		final Group commonGroup = makeGroupUI(wholeComposite, "SET INFO", 2);
		List<String> minLength = new ArrayList<>();
		for(int i=5; i<11; i++){
			minLength.add(i+"");
		}
		List<String> maxLength = new ArrayList<>();
		for(int i=5; i<50; i++){
			maxLength.add(i+"");
		}
		List<String> orderLength = new ArrayList<>();
		for(int i=0; i<50; i++){
			orderLength .add(i+"");
		}
		makeCombo(commonGroup, "MIN LENGTH : ", minLength, 0);
		makeCombo(commonGroup, "MAX LENGTH : ", maxLength, 0);
		makeCombo(commonGroup, "UPPER LENGTH : ", orderLength, 0);
		makeCombo(commonGroup, "NUMBER LENGTH : ", orderLength, 0);
		makeCombo(commonGroup, "SPECIAL LENGTH : ", orderLength, 0);
		
		
		final Group outputGroup = makeGroupUI(wholeComposite, "OUTPUT",
				3);
		makeChoiseFile(outputGroup, "OUTPUT PATH :", "FILE");
		
		makeCombo(outputGroup, "TOTAL PASSWORD : ", orderLength, 1);
		
		Group btnGroup = new Group(wholeComposite, SWT.NONE);
		org.eclipse.swt.layout.FormLayout layout = new org.eclipse.swt.layout.FormLayout();

		layout.marginLeft = layout.marginRight = 5;
		btnGroup.setLayout(layout);
		btnGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));


		Button saveBtn = new Button(btnGroup, SWT.PUSH);
		FormData saveData = new FormData();
		saveData.left = new FormAttachment(30, 0);
		saveBtn.setLayoutData(saveData);
		saveBtn.setText("SAVE PASSWORD");
		saveBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				final Shell dialog = new Shell(passGenShell, SWT.DIALOG_TRIM
						| SWT.APPLICATION_MODAL);
				dialog.setLayout(new FillLayout());
				dialog.setSize(400, 100);
				dialog.setText("Result ");

				Composite wholeComposite = new Composite(dialog, SWT.NONE);
				GridLayout layoutWhole = new GridLayout();
				layoutWhole.numColumns = 1;
				wholeComposite.setLayout(layoutWhole);
				Label infoLab = new Label(wholeComposite, SWT.NONE);
				infoLab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				infoLab.setText("MakeOutPut........");
				final ProgressBar bar = new ProgressBar(wholeComposite,
						SWT.SMOOTH);
				bar.setBounds(10, 10, 200, 20);
				bar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				final Button clsBtn = new Button(wholeComposite, SWT.PUSH);
				clsBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				clsBtn.setText("OK");
				clsBtn.setEnabled(false);
				clsBtn.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						dialog.close();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}
				});
				
				
				//validate
				
				if("".equals(((Text) outputGroup.getChildren()[1]).getText())){
					
					String titleMsg = "CHECKED OUTPUT FILE!";
					showMsgDialog(titleMsg);
					        
					return;
				};
			
				
				
				
				//setValue
				final Map<String, String> pwInfo = new HashMap<String, String>();
				pwInfo.put("min", ((Combo) commonGroup.getChildren()[1]).getText());
				pwInfo.put("max", ((Combo) commonGroup.getChildren()[3]).getText());
				pwInfo.put("upper", ((Combo) commonGroup.getChildren()[5]).getText());
				pwInfo.put("number", ((Combo) commonGroup.getChildren()[7]).getText());
				pwInfo.put("special", ((Combo) commonGroup.getChildren()[9]).getText());
				pwInfo.put("fileName", ((Text) outputGroup.getChildren()[1]).getText());
				pwInfo.put("total", ((Combo) outputGroup.getChildren()[4]).getText());
				
				System.out.println(pwInfo);
				System.out.println(Integer.parseInt(pwInfo.get("total")));
				System.out.println("********************");
				bar.setMaximum(Integer.parseInt(pwInfo.get("total")));
				
				
				try{

					dialog.open();
					//Process
					//thread를 사용해야 현 Thread와 별개의 Thread로 동작한다.
//					Display.getDefault().syncExec(process4Gen(dialog, bar, clsBtn, pwInfo));
					process4Gen(dialog, bar, clsBtn, pwInfo).start();
					
				}catch(Exception e){
					
				}
				
				
			}

			private Thread process4Gen(final Shell dialog,
					final ProgressBar bar, final Button clsBtn,
					final Map<String, String> pwInfo) {
				return new Thread() {
				    public void run() {
						int i[] = new int[1];
						
						BufferedWriter out = null;
						try {
							PasswordGenerate pg = new PasswordGenerate();
							int total = Integer.parseInt(pwInfo.get("total"));
							String fileName = pwInfo.get("fileName");
							out = new BufferedWriter(new FileWriter(fileName));
							for(i[0]=0; i[0]< total ; i[0]++){
								String pw = pg.generate(Integer.parseInt(pwInfo.get("min")), Integer.parseInt(pwInfo.get("max")), Integer.parseInt(pwInfo.get("upper")), Integer.parseInt(pwInfo.get("number")), Integer.parseInt(pwInfo.get("special")));
								out.write(pw);
								out.newLine();
								barUpdate(bar, clsBtn, i);
							}
						}  catch (Exception e) {
							e.printStackTrace();
//								showMessage(e.toString());
							Display.getDefault().syncExec( 
									    new Runnable() { 
									     public void run(){
//										    	 ((Shell)display.getShells()[1]).close();
									    	 if(dialog.isVisible()){
									    		 dialog.close();
									    	 }
									     } 
									    }
									  );
							showMsgDialog(e.getMessage());
						}finally{
							if(out != null){
								try {
									out.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
				    }
				};
			}

			private void showMsgDialog(String titleMsg) {
				final Shell dialogMsg =
				          new Shell(passGenShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				        dialogMsg.setLayout(new GridLayout());
				        dialogMsg.setText("ERROR MASSAGE!");
				        Label msg = new Label(dialogMsg, SWT.NULL);
						msg.setText(titleMsg);
				        msg.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
				        
				        Button okButton = new Button(dialogMsg, SWT.PUSH);
				        okButton.setText("OK");
				        okButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				        okButton.addSelectionListener(new SelectionListener() {
				          public void widgetSelected(SelectionEvent e) {
				            dialogMsg.close();
				          }

				          public void widgetDefaultSelected(SelectionEvent e) {
				          }
				        });

				        dialogMsg.pack();
				        dialogMsg.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
	    // Display shell
	    passGenShell.open();

	    // Wait until top level shell is closed
	    while (!passGenShell.isDisposed()) {
	      // Check for waiting events
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	}
	
	static Button fileBtn = null;
	static Text filepathOutput =null; 
	private void makeChoiseFile(Group commonGroup, String lableText,
			final String btnText) {
		Label label = new Label(commonGroup, SWT.NULL);
		label.setText(lableText);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		filepathOutput = new Text(commonGroup, SWT.SINGLE | SWT.BORDER);
		filepathOutput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filepathOutput.setEnabled(false);
		fileBtn = new Button(commonGroup, SWT.PUSH);
		fileBtn.setText(btnText);
		fileBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fileBtn.addSelectionListener(htmlFile );
	}
	
		SelectionListener htmlFile = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			
				String[] filterNames = new String[] { "TEXT File" };
				String[] filterExtensions = new String[] { "*.txt" };
				
				FileDialog dialog = makeFileDialog(filterNames, filterExtensions, SWT.SAVE);
				try {
					filepathOutput.setText(dialog.open());
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			filepathOutput.setText("");
		}
	};
	
	private FileDialog makeFileDialog(String[] filterNames, String[] filterExtensions, int type) {
		FileDialog dialog = new FileDialog(
				passGenShell, type);
	
		String filterPath = "/";
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterPath = "c:\\";
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.setFileName("");
		return dialog;
	}
	
	private  void makeInput(Group group, String string) {
		makeInput(group, string, "");
	}
	
	private Combo makeCombo(final Group commonGroup, String labelTxt,
			List<String> comboData, int selectIndex) {
		Label label = new Label(commonGroup, SWT.NULL);
		label.setText(labelTxt);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Combo comboDropDown = new Combo(commonGroup, SWT.DROP_DOWN | SWT.BORDER
				| SWT.READ_ONLY);
		for(String data : comboData){
			comboDropDown.add(data);
		}
		comboDropDown.select(selectIndex);
		
		return comboDropDown ;
	}
	
	private  void makeInput(Group group, String string, String setVal) {
		Label label = new Label(group, SWT.NULL);
		label.setText(string);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(setVal);
	}
	private  Group makeGroupUI(Composite wholeComposite,
			String commonGrouptitle, int commonColumns) {
		final Group commonGroup = new Group(wholeComposite, SWT.NONE);
		commonGroup.setText(commonGrouptitle);
		GridLayout layout = new GridLayout();
		layout.numColumns = commonColumns;
		commonGroup.setLayout(layout);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		commonGroup.setLayoutData(data);
		return commonGroup;
	}
	
	private void barUpdate(final ProgressBar bar,
			final Button clsBtn, final int[] i) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				
				if (bar.isDisposed())
					return;
				// bar update
				bar.setSelection(i[0]+1);
				if (bar.getMaximum() <=( i[0]+1)) {
					clsBtn.setEnabled(true);
				}
			}
		});
	}
}
