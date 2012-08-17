package miage.ihm;

import java.awt.Frame;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Insets;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import miage.ListeFichiers;
import miage.sgbd.DataProvider;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;

/**
 * Panneau d'ajout des dossier à scanner
 * @author Nicolas Velin
 */
public class JPanel_AjoutDossier extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField jTextField = null;
	private JButton jButton_Parcourir = null;
	private JButton jButton_Modifier = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private MyTableModel jTableModel = null;
	private JButton jButton_Ok = null;

	private JDialog dialog = null;

	/**
	 * This is the default constructor
	 */
	public JPanel_AjoutDossier(Frame owner) {
		super();
		initialize();
		dialog = new JDialog(owner,true);
		dialog.add(this);
		dialog.setSize(400,400);
		dialog.setTitle("Sélectionner un dossier...");
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(this.getParent());
		dialog.setVisible(true);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridy = 2;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints11.gridx = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints3.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridx = 0;
		this.setSize(536, 188);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getJTextField(), gridBagConstraints1);
		this.add(getJButton_Parcourir(), gridBagConstraints2);
		this.add(getJButton_Modifier(), gridBagConstraints3);
		this.add(getJButton_Ok(), gridBagConstraints);
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if(jTextField == null) {
			jTextField = new JTextField();
			jTextField.setEditable(false);
		}
		return jTextField;
	}

	/**
	 * This method initializes jButton_Parcourir
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_Parcourir() {
		if(jButton_Parcourir == null) {
			jButton_Parcourir = new JButton();
			jButton_Parcourir.setText(LangageManager.getProperty("common.dialog.explore"));
			jButton_Parcourir.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPacourir();
				}
			});
		}
		return jButton_Parcourir;
	}

	/**
	 * This method initializes jButton_Modifier
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_Modifier() {
		if(jButton_Modifier == null) {
			jButton_Modifier = new JButton();
			jButton_Modifier.setText(LangageManager.getProperty("common.dialog.add"));
			jButton_Modifier.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onModifier();
				}
			});
		}
		return jButton_Modifier;
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
			jTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent e) {
					jButton_Modifier.setText(LangageManager.getProperty("common.dialog.remove"));
				}
			});

			String dossiers = PreferencesManager.get("entagged.tag.foldertoscan");
			if(dossiers != null && dossiers != "") {
				StringTokenizer st = new StringTokenizer(dossiers,";");
				Vector v;
				while (st.hasMoreTokens()) {
					v = new Vector();
					v.add(st.nextToken());
					getJTableModel().addRow(v);
				}
			}
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
			jTableModel.addColumn(LangageManager.getProperty("common.dialog.folderAndChild"));
		}
		return jTableModel;
	}

	/**
	 * This method initializes jButton_Ok
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_Ok() {
		if(jButton_Ok == null) {
			jButton_Ok = new JButton();
			jButton_Ok.setText(LangageManager.getProperty("common.dialog.ok"));
			jButton_Ok.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onOk();
				}
			});
		}
		return jButton_Ok;
	}

	/**
	 * Lance la méthode d'indexation des fichiers et enregistre les paramètres
	 */
	private void onOk() {
		DataProvider.cleanTable();

		String dossiers = "";
		String [] listeDossier = new String[getJTable().getRowCount()];
		int i;
		for(i = 0 ; i < getJTable().getRowCount() ; i++) {
			dossiers += getJTable().getValueAt(i,0) + ";";
			listeDossier[i] = getJTable().getValueAt(i,0).toString();
		}

		if(i > 0)
			miage.ListeFichiers.rechercheFichierThread(listeDossier);

		PreferencesManager.put("entagged.tag.foldertoscan", dossiers);
		try {
			PreferencesManager.cleanPreferences();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		dialog.dispose();
	}

	/**
	 * Ajoute ou supprimer un dossier de la liste
	 * Lors d'un ajout, il vérifie si le dossier n'est pas le descendant ou l'ascendant d'un dossier déjà présent
	 */
	private void onModifier() {
		int row = jTable.getSelectedRow();

		if(row == -1) { // ADD
			String dossier = getJTextField().getText();
			if(dossier.length() > 0) {
				ListeFichiers lf = new ListeFichiers(new File(dossier));

				// List of Folders added
				Vector existantFolders = new Vector();
				File folder;
				for(int i = 0 ; i < getJTable().getRowCount() ; i++) {
					folder = new File((String)getJTable().getValueAt(i,0));
					existantFolders.add(folder);
				}

				boolean delete = false;
				if(!lf.isChild(existantFolders)) {
					ListeFichiers lf2;
					for(int i = existantFolders.size() - 1 ; i >= 0 ; i--) {
						lf2 = new ListeFichiers(((File)existantFolders.get(i)));
						if(lf2.isChild(new File(dossier))) {
							delete = true;
							getJTableModel().removeRow(i);
						}
					}
					String [] dos = {dossier};
					getJTableModel().addRow(dos);
				}
				else
					JOptionPane.showMessageDialog(this, "Le dossier que vous avez souhaité ajouter est déjà présent (lui même ou un dossier parent)\nA traduire","Erreur d'ajout",JOptionPane.ERROR_MESSAGE);
				if(delete)
					JOptionPane.showMessageDialog(this, "Les dossiers redondants ont été supprimé\nA traduire","Modification de la liste",JOptionPane.WARNING_MESSAGE);
				getJTextField().setText("");
			}
		}
		else // REMOVE
			getJTableModel().removeRow(row);
	}

	/**
	 * Affiche un sélecteur de dossier
	 */
	private void onPacourir() {
		JFileChooser chooser = new JFileChooser();//création dun nouveau filechosser
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setApproveButtonText(LangageManager.getProperty("common.dialog.select")); //intitulé du bouton
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			jTable.clearSelection();
			jButton_Modifier.setText("+");
			getJTextField().setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

/**
 * Redéfinie le modèle de table par défaut pour empêcher l'édition des cellules
 * @author Nicolas Velin
 */
class MyTableModel extends DefaultTableModel implements TableModel {

	public MyTableModel() {
		super();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}
