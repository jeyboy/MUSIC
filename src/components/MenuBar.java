package components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.MediaInfo;
import tabber.Tab;
import tabber.TabOptions;
import tabber.Tabber;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 5651526930411426260L;
	private JTextField title;
	private JCheckBox delete_with_file;
	private JCheckBox interactive;
	private JCheckBox delete_empty_folders;
	private JCheckBox remote_source;
	JFileChooser fileChooser = new JFileChooser(".");
	private Color default_item_color;
	static JMenuItem play_item;
	
	
	void prepareGUI(String tit, boolean del, boolean inter, boolean del_ef, boolean remote) {
		title = new JTextField(tit);
		delete_with_file = new JCheckBox("Delete file with item", del);
		interactive = new JCheckBox("Interactive tab", inter);
		delete_empty_folders = new JCheckBox("Delete empty folders", del_ef);
		remote_source = new JCheckBox("Source of data is remote", remote);		
	}
	void prepareGUI() { prepareGUI("", false, false, false, false); }
	
	public MenuBar(final Tabber tab) {
		setBackground(Color.black);
		ActionBind [] actions = {
				new ActionBind("add_tab", new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						prepareGUI();
					    Object complexMsg[] = { "Create tab with title", title, new JCheckBox[] {delete_with_file, delete_empty_folders, interactive, remote_source} };		
						int option = JOptionPane.showOptionDialog(  
								MenuBar.this,  
								complexMsg,  
								"Creating drop elem", JOptionPane.OK_CANCEL_OPTION,  
								JOptionPane.PLAIN_MESSAGE, null, null,  
								null 
				        );
						if( option == JOptionPane.OK_OPTION ) Common.tabber.AddTab(title.getText(), new TabOptions(delete_with_file.isSelected(), interactive.isSelected(), delete_empty_folders.isSelected(), remote_source.isSelected()));							
				    }
				}),
				new ActionBind("settings", new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Tab tab = Common.tabber.GetCurrentTab();
						if (tab == null) return; 
						prepareGUI(tab.GetTitle(), tab.options.delete_files, tab.options.interactive, tab.options.delete_empty_folders, tab.options.remote_source);
					    Object complexMsg[] = { "Modify tab title", title, delete_with_file, delete_empty_folders, interactive, remote_source };		
						int option = JOptionPane.showOptionDialog(  
								MenuBar.this,  
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
				new ActionBind("start_play", new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Common.rand_play_flag = false;
						if (!Common.mp3.isPlayed())
							Common.raw_flag = !Common.raw_flag;
						else {
							Common.raw_flag = false;
							Common.mp3.stop();
						}
						
						if (Common.raw_flag)
							Common.tabber.MoveSelectAndInit(true);
				    }
				}),
				new ActionBind("mixer", new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Common.tabber.GetCurrentTab().Shuffle();
				    }
				}),					
				new ActionBind("include_base", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					    if (fileChooser.showDialog(MenuBar.this, "Choose") == JFileChooser.APPROVE_OPTION) {
					    	if (fileChooser.getSelectedFile().isDirectory())
					    		ParseLibrary(fileChooser.getSelectedFile());	
					    	else ParseBase(fileChooser.getSelectedFile().getAbsolutePath());
					    }
					}
				})
			};
		
		for(int loop1 = 0; loop1 < actions.length; loop1++) {
			ImageIcon n = GetIcon(service.Settings.imagepath + "menubar/" + actions[loop1].name + ".png");
			final JMenuItem item = new JMenuItem(n);
			if (actions[loop1].name == "start_play") {
				play_item = item;
				item.setPressedIcon(GetIcon(service.Settings.imagepath + "menubar/stop_play.png"));
				item.setDisabledIcon(n);
			}
			item.setBackground(Color.black);
			item.addActionListener(actions[loop1].action);
			item.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					default_item_color = item.getBackground();
					item.setBackground(Color.WHITE);
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					item.setBackground(default_item_color);
				}
				@Override
				public void mousePressed(MouseEvent arg0) {}
				@Override
				public void mouseReleased(MouseEvent arg0) {}
			});
			this.add(item);
		}
		this.setVisible(true);
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
	void ParseLibrary(File path) {
		int proceed_count = 0;
		Collection<File> files = IOOperations.ScanDirectoriesF(new File [] {path});
		for(File f : files) {
			try {
				BufferedReader reader = IOOperations.GetReader(f.getAbsolutePath());
		  		String strLine;
		  		
		  		try {
					while ((strLine = reader.readLine()) != null) {
						if (strLine.length() == 0) continue;
						
						Common.library.Set(strLine.substring(1), strLine.charAt(0) == '1');
						proceed_count++;
					}
					reader.close();
				}
		  		catch (IOException e) { Errorist.printLog(e); }
			} 
			catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
		}
		
		Errorist.printMessage("Library Parse", "Proceed " + proceed_count + " items");
		MainWnd.SetTitle("Proceed " + proceed_count + " items");
	}
	
	public static void SetPlay() { play_item.setIcon(play_item.getPressedIcon()); }
	public static void SetStop() { play_item.setIcon(play_item.getDisabledIcon()); }
	
	ImageIcon GetIcon(String path) {
		ImageIcon n = null;
		try {
			n = new ImageIcon(
					ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(path))
					);
		} 
		catch (IOException e) {Errorist.printLog(e);}
		return n;
	}
	
	
	class ActionBind {
		public ActionListener action = null;
		public String name = null;
		
		public ActionBind(String name, ActionListener action) {
			this.action = action;
			this.name = name;
		}
	}	
}
