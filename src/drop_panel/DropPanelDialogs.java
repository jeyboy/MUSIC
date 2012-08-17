package drop_panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DropPanelDialogs {
	private JFileChooser fileChooser = new JFileChooser(".");
	public DropPanel container;
	
	public DropPanelDialogs(DropPanel parent) { 
		container = parent;
	}
	
	public void addDropItemDialog() {
		JTextField title = new JTextField();
		final JLabel pathLabel = new JLabel("Path not set");
		JButton folderDialogButton = new JButton("Choose folder");
		folderDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    if (fileChooser.showDialog(container, "Choose") == JFileChooser.APPROVE_OPTION) {
			    	pathLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
	    Object complexMsg[] = { "Create pane with title", title, "and drop path", pathLabel, folderDialogButton };		
		int option = JOptionPane.showOptionDialog(  
				container,  
				complexMsg,  
				"Creating drop elem", JOptionPane.OK_CANCEL_OPTION,  
				JOptionPane.PLAIN_MESSAGE, null, null,  
				null 
        );  
		if( option == JOptionPane.OK_OPTION ) {
			container.AddItem(title.getText(), pathLabel.getText());
		}
	}
	
	public void modDropItemDialog(DropPanelItem item) {
		JTextField title = new JTextField(item.getText());
		final JLabel pathLabel = new JLabel(item.folder.getAbsolutePath());
		JButton folderDialogButton = new JButton("Choose folder");
		folderDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    if (fileChooser.showDialog(container, "Choose") == JFileChooser.APPROVE_OPTION) {
			    	pathLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
	    Object complexMsg[] = { "Change title to", title, "and drop path", pathLabel, folderDialogButton };		
		int option = JOptionPane.showOptionDialog(  
				container,  
				complexMsg,  
				"Modify drop elem", JOptionPane.OK_CANCEL_OPTION,  
				JOptionPane.PLAIN_MESSAGE, null, null,  
				null 
        );  
		if( option == JOptionPane.OK_OPTION ) {
			item.setPath(pathLabel.getText());
			item.setText(title.getText()); 
		}
	}	
}
