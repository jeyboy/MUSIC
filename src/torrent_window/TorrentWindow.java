package torrent_window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import service.Common;

import components.TWindow;

public class TorrentWindow {
	JFrame window;
	JTable table;
	TorrentModel model = new TorrentModel();
	
	void initModel() {
		table = new JTable(model);
	}
	
	void initLayout() {
		initModel();
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        
		c.gridheight = 1; c.gridwidth = 1; c.gridx = 0; c.gridy = 0; c.weightx = c.weighty = 1;
		JScrollPane panel =  new JScrollPane(table);			
		gridbag.setConstraints(panel, c);	window.add(panel);
               
//		c.gridheight = 1; c.gridwidth = 1; c.gridy = 1; c.gridx = 0; 
//        gridbag.setConstraints(Common.tabber, c); wnd.add(Common.tabber);
        
        window.getContentPane().setLayout(gridbag);        
	}		
	
	public TorrentWindow() {
		window = TWindow.create("Torrents", 250, 300);
		initLayout();
	}
	
	public void Show() { window.setVisible(true); }
	public void Hide() { window.setVisible(false); }
	
	public void AddTorrent(String torrentPath, String savePath) {
		new TorrentRow(model, torrentPath, savePath);
	}
}

//setValueAt(Object aValue, int rowIndex, int columnIndex)
