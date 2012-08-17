package miage.ihm;

import javax.swing.JButton;
import javax.swing.JFrame;

import entagged.tageditor.TagEditorFrame;
import entagged.tageditor.resources.LangageManager;

import java.awt.Dimension;
import java.util.ArrayList;

public class JButton_Playlist extends JButton {
	
	private JFrame owner; 

	/**
	 * This method initializes 
	 * @param table
	 */
	public JButton_Playlist(JFrame owner) {
		super();
		this.owner = owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(81, 21));
        this.setText(LangageManager.getProperty("play"));
        this.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent e) {
        		onPlay();
        	}
        });		
	}
	
	private void onPlay() {
		ArrayList<String> playlist = TagEditorFrame.getCheckList();
		if(playlist.size() > 0)
			new JDialog_PlayList(owner,playlist).setVisible(true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
