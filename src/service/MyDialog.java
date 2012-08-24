package service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MyDialog extends Dialog {
	public boolean accept = false;
	public Shell shell;
	
	public MyDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	public MyDialog(Shell parent, int style) {
		super(parent, style);
		setText("Input Dialog");
		shell = new Shell(getParent(), getStyle());
	}

	public boolean open(Object [] items) {
		// Create the dialog window
		shell.setText(getText());
		createContents(shell, items);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return accept;
	}

	private void createContents(final Shell shell, Object [] items) {
		shell.setLayout(new GridLayout(2, true));
		GridData data;
		
		for(int loop1 = 0; loop1< items.length; loop1++) {
			if (items[loop1] instanceof String) {
				Label label = new Label(shell, SWT.NONE);
				label.setText((String)items[loop1]);
				data = new GridData();
				data.horizontalSpan = 1;
				data.horizontalAlignment = GridData.FILL;
				label.setLayoutData(data);
				
				Control c = (Control)items[++loop1];
				data = new GridData(GridData.FILL_HORIZONTAL);
				data.horizontalSpan = 1;
				c.setLayoutData(data);				
			} else {
				Control c = (Control)items[loop1];
				data = new GridData(GridData.FILL_HORIZONTAL);
				data.horizontalSpan = 2;
				c.setLayoutData(data);
			}
		}

	    // Create the OK button and add a handler
	    // so that pressing it will set input
	    // to the entered value
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		data = new GridData(GridData.FILL_HORIZONTAL);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				accept = true;
				shell.close();
			}
		});

	    // Create the cancel button and add a handler
	    // so that pressing it will set input to null
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		data = new GridData(GridData.FILL_HORIZONTAL);
		cancel.setLayoutData(data);
	    cancel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		accept = false;
	    		shell.close();
	    	}
	    });

	    // Set the OK button as the default, so
	    // user can type input and press Enter
	    // to dismiss
	    shell.setDefaultButton(ok);
	}
}