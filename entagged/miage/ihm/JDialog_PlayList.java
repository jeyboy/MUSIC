package miage.ihm;

import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.GridBagLayout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;

import miage.ListeFichiers;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class JDialog_PlayList extends JDialog {

	private static final long serialVersionUID = 1L;

	private JFrame owner;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private MyTableModel jTableModel = null;
	private JButton jButton_Play = null;
	private JButton jButton_Save = null;
	private JButton jButton_Clear = null;

	private ArrayList<String> playlist;

	private JButton jButton_Delete = null;

	private JButton jButton_Close = null;

	/**
	 * @param owner
	 * @param playlist 
	 */
	public JDialog_PlayList(JFrame owner, ArrayList<String> playlist) {
		super(owner);
		this.owner = owner;
		this.playlist = playlist;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(493, 263);
		this.setTitle(LangageManager.getProperty("tablemodel.playlist"));
		this.setLocationRelativeTo(this.getParent());
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 4;
			gridBagConstraints21.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridheight = 2;
			gridBagConstraints3.weighty = 0.0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 0.0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 3;
			gridBagConstraints1.gridheight = 2;
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 0.0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridwidth = 5;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints);
			jContentPane.add(getJButton_Clear(), gridBagConstraints1);
			jContentPane.add(getJButton_Play(), gridBagConstraints2);
			jContentPane.add(getJButton_Save(), gridBagConstraints3);
			jContentPane.add(getJButton_Delete(), gridBagConstraints11);
			jContentPane.add(getJButton_Close(), gridBagConstraints21);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if(jTable == null) {
			jTable = new JTable();
			jTable.setModel(getJTableModel());
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			Vector v;
			int taille = playlist.size();
			for(int i = 0 ; i < taille ; i++) {
				v = new Vector();
				v.add(playlist.get(i));
				getJTableModel().addRow(v);
			}
			
			jTable.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if(e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
						onDelete();
					}
				}
			});
		}
		return jTable;
	}

	/**
	 * This method initializes jTableModel
	 *
	 * @return javax.swing.TableModel
	 */
	private MyTableModel getJTableModel() {
		if(jTableModel == null) {
			jTableModel = new MyTableModel();
			jTableModel.addColumn(LangageManager.getProperty("common.dialog.files"));
		}
		return jTableModel;
	}

	/**
	 * This method initializes jButton_Play	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Play() {
		if(jButton_Play == null) {
			jButton_Play = new JButton("Play");
			jButton_Play.setText(LangageManager.getProperty("playplayList"));
			jButton_Play.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPlay();
				}
			});
		}
		return jButton_Play;
	}

	/**
	 * This method initializes jButton_Save	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Save() {
		if(jButton_Save == null) {
			jButton_Save = new JButton("Save");
			jButton_Save.setText(LangageManager.getProperty("common.dialog.save"));
			jButton_Save.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onSave();
				}
			});
		}
		return jButton_Save;
	}

	/**
	 * This method initializes jButton_Clear	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Clear() {
		if(jButton_Clear == null) {
			jButton_Clear = new JButton("Clear");
			jButton_Clear.setText(LangageManager.getProperty("clear"));
			jButton_Clear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onClear();
				}
			});
		}
		return jButton_Clear;
	}
	
	/**
	 * This method initializes jButton_Delete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Delete() {
		if(jButton_Delete == null) {
			jButton_Delete = new JButton("Delete");
			jButton_Delete.setText(LangageManager.getProperty("delete"));
			jButton_Delete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onDelete();
				}
			});
		}
		return jButton_Delete;
	}

	/**
	 * This method initializes jButton_Close	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Close() {
		if(jButton_Close == null) {
			jButton_Close = new JButton("Close");
			jButton_Close.setText(LangageManager.getProperty("close"));
			jButton_Close.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onClose();
				}
			});
		}
		return jButton_Close;
	}

	private void onPlay() {
		if(playlist.size() > 0) {
			String url = PreferencesManager.USER_PROPERTIES_DIR + File.separator + "tmp.m3u";
			FileWriter fw;
			try {
				fw = new FileWriter(url);
				BufferedWriter bw = new BufferedWriter(fw);
				for(int i = 0 ; i < playlist.size() ; i++) {
					bw.write(playlist.get(i));
					bw.newLine();
				}
				bw.close();
				fw.close();
				
				ListeFichiers.play(owner, url);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void onSave() {
		if(playlist.size() > 0) {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new MyFilter());

			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String url = fc.getSelectedFile().getAbsolutePath();
				if(url.endsWith(".m3u"))
					url = url.substring(0, url.length() - 4);
				url += ".m3u";
				FileWriter fw;
				try {
					fw = new FileWriter(url);
					BufferedWriter bw = new BufferedWriter(fw);
					for(int i = 0 ; i < playlist.size() ; i++) {
						bw.write(playlist.get(i));
						bw.newLine();
					}
					bw.close();
					fw.close();

					ListeFichiers.play(owner, url);
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void onClear() {
		for(int i = jTable.getRowCount() - 1 ; i >= 0 ; i--) {
			getJTableModel().removeRow(i);
			playlist.remove(i);
		}
		owner.repaint();
	}
	
	private void onDelete() {
		int row = jTable.getSelectedRow();
		if(row > -1) {
			jTableModel.removeRow(row);
			playlist.remove(row);
			owner.repaint();
			if(jTable.getRowCount() > row)
				jTable.setRowSelectionInterval(row, row);
			else if(jTable.getRowCount() == row && row > 0)
				jTable.setRowSelectionInterval(row - 1, row - 1);
		}
	}
	
	private void onClose() {
		dispose();
	}
	
	class MyFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if(file.isDirectory())
				return true;
			else {
				String filename = file.getName();
				return filename.endsWith(".m3u");
			}
		}
		public String getDescription() {
			return "*.m3u";
		}
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
