package components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.MediaInfo;
import tabber.Tab;
import tabber.TabOptions;
import tabber.Tabber;

public class MenuBar {
	private JTextField title;
	private JCheckBox delete_with_file;
	private JCheckBox interactive;
	private JCheckBox delete_empty_folders;
	private JCheckBox remote_source;
	JFileChooser fileChooser = new JFileChooser(".");
	
	void prepareGUI(String tit, boolean del, boolean inter, boolean del_ef, boolean remote) {
		title = new JTextField(tit);
		delete_with_file = new JCheckBox("Delete file with item", del);
		interactive = new JCheckBox("Interactive tab", inter);
		delete_empty_folders = new JCheckBox("Delete empty folders", del_ef);
		remote_source = new JCheckBox("Source of data is remote", remote);		
	}
	void prepareGUI() { prepareGUI("", false, false, false, false); }
	
	public MenuBar() {}
	public ToolBar PrepareToolBar(Composite wnd, Tabber tab) {
		ToolBar res = new ToolBar(wnd, SWT.HORIZONTAL | SWT.WRAP);
		res.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		res.setBackground(new Color(Display.getCurrent(), 0, 0, 0));
		ActionBind [] actions = {
				new ActionBind("add_tab", new Listener() {
					public void handleEvent(Event arg0) {
						prepareGUI();
					    Object complexMsg[] = { "Create tab with title", title, new JCheckBox[] {delete_with_file, delete_empty_folders, interactive, remote_source} };
					    
						int option = JOptionPane.showOptionDialog(  
								null,  
								complexMsg,  
								"Creating drop elem", JOptionPane.OK_CANCEL_OPTION,  
								JOptionPane.PLAIN_MESSAGE, null, null,  
								null 
				        );
						if( option == JOptionPane.OK_OPTION ) Common.tabber.AddTab(title.getText(), new TabOptions(delete_with_file.isSelected(), interactive.isSelected(), delete_empty_folders.isSelected(), remote_source.isSelected()));
					}
				}),
				new ActionBind("settings", new Listener() {
					public void handleEvent(Event arg0) {
						Tab tab = Common.tabber.GetCurrentTab();
						if (tab == null) return; 
						prepareGUI(tab.GetTitle(), tab.options.delete_files, tab.options.interactive, tab.options.delete_empty_folders, tab.options.remote_source);
					    Object complexMsg[] = { "Modify tab title", title, delete_with_file, delete_empty_folders, interactive, remote_source };		
						int option = JOptionPane.showOptionDialog(  
								null,  
								complexMsg,  
								"Modify drop elem", JOptionPane.OK_CANCEL_OPTION,  
								JOptionPane.PLAIN_MESSAGE, null, null,  
								null 
				        );
						if( option == JOptionPane.OK_OPTION ) {
							if (title.getText().trim().length() > 0)
								tab.SetTitle(title.getText());
							tab.options = new TabOptions(delete_with_file.isSelected(), interactive.isSelected(), delete_empty_folders.isSelected(), remote_source.isSelected());
							tab.UpdateCounter();
						}						
				    }
				}),
				new ActionBind("start_play", new Listener() {
					public void handleEvent(Event arg0) {
						
				    }
				}),					
				new ActionBind("include_base", new Listener() {
					public void handleEvent(Event arg0) {
					    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					    if (fileChooser.showDialog(null, "Choose") == JFileChooser.APPROVE_OPTION)
					    	ParseBase(fileChooser.getSelectedFile().getAbsolutePath());
					}
				})
			};
		
		for(int loop1 = 0; loop1 < actions.length; loop1++) {
			Image n = new Image(
					Display.getCurrent(),
					Thread.currentThread().getContextClassLoader().getResourceAsStream(service.Settings.imagepath + "menubar/" + actions[loop1].name + ".png")
			);
			ToolItem item = new ToolItem(res, SWT.PUSH);
			item.setImage(n);

//			item.setBackground(Color.black);
			item.addListener(SWT.Selection, actions[loop1].action);
		}
//		res.setVisible(true);
		return res;
	}
	
	void ParseBase(String path) {
		try {
			BufferedReader reader = IOOperations.GetReader(path);
	  		String strLine, ext;
	  		
	  		try {
				while ((strLine = reader.readLine()) != null) {
					if (strLine.length() == 0) continue;
					boolean flag = reader.readLine().charAt(0) == '1';
					
					strLine = strLine.toLowerCase();
					ext = IOOperations.extension(strLine);
					
					if (ext.length() != 0)
						strLine = strLine.substring(0, strLine.length() - (ext.length() + 1));				
					
					String temp = MediaInfo.SitesFilter(strLine);  
					temp = MediaInfo.SpacesFilter(MediaInfo.ForwardNumberPreFilter(temp));
					Common.library.Set(temp, flag);
					temp = MediaInfo.ForwardNumberFilter(temp);
					Common.library.Set(temp, flag);					
				}
				reader.close();
			}
	  		catch (IOException e) { Errorist.printLog(e); }
		} 
		catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
	}
	
	public class ActionBind {
		public Listener action = null;
		public String name = null;
		
		public ActionBind(String name, Listener action) {
			this.action = action;
			this.name = name;
		}
	}	
}
