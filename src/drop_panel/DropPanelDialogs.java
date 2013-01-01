package drop_panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import service.Utils;

public class DropPanelDialogs {
	private JFileChooser fileChooser = new JFileChooser(".");
	public DropPanel container;
	
	public DropPanelDialogs(DropPanel parent) { container = parent; }

	public String[] ShowDialog(String dialog_title, String item_title, String item_path) {
		JTextField title = new JTextField(item_title);
		final JLabel pathLabel = new JLabel(item_path);
		JButton folderDialogButton = new JButton("Choose folder");
		folderDialogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    if (fileChooser.showDialog(container, "Choose") == JFileChooser.APPROVE_OPTION)
			    	pathLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
	    Object complexMsg[] = { dialog_title, title, "and drop path", pathLabel, folderDialogButton };		
		if( Utils.showDialog(container, "Modify drop elem", complexMsg) == JOptionPane.OK_OPTION )
			return new String[] {title.getText(), pathLabel.getText()};
		else return new String[0];
	}	
	
	public void addDropItemDialog() {
		String [] res = ShowDialog("Create pane with title", "", "Path not set");
		if (res.length != 0)
			container.AddItem(res[0], res[1]);
	}
	
	public void modDropItemDialog(DropPanelItem item) {
		String [] res = ShowDialog("Change title to", item.getText(), item.folder.getAbsolutePath());
		if (res.length != 0) {
			item.setText(res[0]);
			item.setPath(res[1]);
		}
	}	
}