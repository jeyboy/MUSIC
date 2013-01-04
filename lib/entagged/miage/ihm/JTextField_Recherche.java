package miage.ihm;

import javax.swing.JTextField;

import miage.sgbd.DataProvider;

import entagged.tageditor.DirectoryChooser;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.resources.LangageManager;

import java.awt.Dimension;
import java.io.File;

/**
 * Boite de recherche des tags
 * @author Nicolas Velin
 * TODO Ajout du multilingue
 */
public class JTextField_Recherche extends JTextField {

	private TagEditorTableModel tableModel;
	private DirectoryChooser dossierCourant;
	private int i = 0;

	/**
	 * This method initializes 
	 * @param tableModel 
	 */
	public JTextField_Recherche(DirectoryChooser dirChooser, TagEditorTableModel tableModel) {
		super();
		this.tableModel = tableModel;
		this.dossierCourant = dirChooser;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setPreferredSize(new Dimension(200, 20));
		this.setToolTipText(LangageManager.getProperty("miage.searchTextFieldexplication"));
		this.setText(LangageManager.getProperty("miage.searchTextField"));
		this.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				if(e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
					onValidate();
				else
					onKey();
			}
		});
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (JTextField_Recherche.this.getText().equalsIgnoreCase(LangageManager.getProperty("miage.searchTextField")))
					JTextField_Recherche.this.setText("");
			}
		});
		this.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {    
				if (JTextField_Recherche.this.getText().equalsIgnoreCase(LangageManager.getProperty("miage.searchTextField")))
					JTextField_Recherche.this.setText("");
			}
			public void focusLost(java.awt.event.FocusEvent e) {
				if(JTextField_Recherche.this.getText().equals(""))
					JTextField_Recherche.this.setText(LangageManager.getProperty("miage.searchTextField"));
			}
		});		
	}
	
	private void onValidate() {
		i = 0; // Reset
		String str = JTextField_Recherche.this.getText();
		if(!str.equals(""))
			tableModel.directoryChanged(new File("c:\\"),DataProvider.getFichiers(str),NavigatorListener.EVENT_JUMPED);
	}
	
	private void onKey() {
		i++;
		Thread performer = new Thread(new Runnable() {
			public void run() {
				int j = i;
				try {
					Thread.sleep(500);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				if(j == i) {
					i = 0; // Reset
					String str = JTextField_Recherche.this.getText();
					//System.out.println(dossierCourant.getPath());
					if(!str.equals(""))
						tableModel.directoryChanged(new File("c:\\"),DataProvider.getFichiers(str),NavigatorListener.EVENT_JUMPED);

				}
			}
		}, "Performer");
		performer.start();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
