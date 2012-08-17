package miage.ihm;

import javax.swing.JPanel;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.resources.LangageManager;

import miage.Atome;
import miage.sgbd.DataProvider;

import java.awt.Insets;
import java.awt.Dimension;

public class JDialog_MauvaisesFrappes extends JDialog {

	private static final long serialVersionUID = 1L;

	private ArrayList<ArrayList<Atome>> listeDoublons;

	private JPanel jContentPane = null;
	private JTextArea jTextArea_Explication = null;
	private JLabel jLabel_Nom = null;
	private JComboBox jComboBox_Choix = null;
	private JButton jButton_OK = null;
	private JButton jButton_Cancel = null;

	private TagEditorTableModel tableModel;

	private JButton jButton_Ignorer = null;

	/**
	 * @param owner
	 * @param tableModel 
	 */
	public JDialog_MauvaisesFrappes(Frame owner, ArrayList<ArrayList<Atome>> listeDoublons, TagEditorTableModel tableModel) {
		super(owner,true);
		this.listeDoublons = listeDoublons;
		this.tableModel = tableModel; 
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(430, 300);
		this.setLocationRelativeTo(getParent());
		// this.setTitle("Traitement des mauvaises frappes");
		this.setTitle(LangageManager.getProperty("miage.titleWrongInput"));
		this.setContentPane(getJContentPane());
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane.add(getJTextArea_Explication(), gridBagConstraints);

			if(listeDoublons.size() > 0) {
				jLabel_Nom = new JLabel();
				// jLabel_Nom.setText("Nom de l'artiste retenu :");
				jLabel_Nom.setText(LangageManager.getProperty("miage.wrongInputNameChosen"));
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 0;
				gridBagConstraints1.fill = GridBagConstraints.BOTH;
				gridBagConstraints1.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints1.gridy = 1;
				jContentPane.add(jLabel_Nom, gridBagConstraints1);
				
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.fill = GridBagConstraints.BOTH;
				gridBagConstraints2.gridy = 1;
				gridBagConstraints2.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints2.gridwidth = 2;
				gridBagConstraints2.gridx = 1;
				jContentPane.add(getJComboBox_Choix(), gridBagConstraints2);
				
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0;
				gridBagConstraints3.fill = GridBagConstraints.BOTH;
				gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints3.weightx = 0.0;
				gridBagConstraints3.gridy = 2;
				jContentPane.add(getJButton_OK(), gridBagConstraints3);
				
				GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
				gridBagConstraints21.gridx = 1;
				gridBagConstraints21.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints21.fill = GridBagConstraints.BOTH;
				gridBagConstraints21.weightx = 1.0;
				gridBagConstraints21.gridy = 2;
				jContentPane.add(getJButton_Ignorer(), gridBagConstraints21);
			}
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 2;
			jContentPane.add(getJButton_Cancel(), gridBagConstraints4);
			
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextArea_Explication
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea_Explication() {
		if(jTextArea_Explication == null) {
			jTextArea_Explication = new JTextArea();
			jTextArea_Explication.setEditable(false);
			jTextArea_Explication.setLineWrap(true);
			jTextArea_Explication.setWrapStyleWord(true);
			if(listeDoublons.size() > 0)
				// jTextArea_Explication.setText("Plusieurs artistes avec des noms très proches ont été détectés. Il pourrait s'agir du même artiste.\n\nVous pouvez choisir le nom de l'artiste que vous préférez conserver. Si vous ne souhaitez pas apporter de modification, cliquez sur Annuler");
				jTextArea_Explication.setText(LangageManager.getProperty("miage.wrongInputExplication"));
			else
				// jTextArea_Explication.setText("Aucune mauvaise frappe n'a été trouvé");
				jTextArea_Explication.setText(LangageManager.getProperty("miage.wrongInputNoneFound"));
		}
		return jTextArea_Explication;
	}

	/**
	 * This method initializes jComboBox_Choix
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox_Choix() {
		if(jComboBox_Choix == null) {
			jComboBox_Choix = new JComboBox();
			ArrayList<Atome> doublons = listeDoublons.get(0);
			for(int i = 0 ; i < doublons.size() ; i++)
				jComboBox_Choix.addItem(doublons.get(i));
		}
		return jComboBox_Choix;
	}

	/**
	 * This method initializes jButton_OK
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_OK() {
		if(jButton_OK == null) {
			jButton_OK = new JButton();
			jButton_OK.setText("OK");
			jButton_OK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onOK();
				}
			});
		}
		return jButton_OK;
	}

	/**
	 * This method initializes jButton_Cancel
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_Cancel() {
		if(jButton_Cancel == null) {
			jButton_Cancel = new JButton();
			// jButton_Cancel.setText("Fermer");
			jButton_Cancel.setText(LangageManager.getProperty("close"));
			jButton_Cancel.setPreferredSize(new Dimension(51, 26));
			jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return jButton_Cancel;
	}
	
	/**
	 * This method initializes jButton_Ignorer	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Ignorer() {
		if(jButton_Ignorer == null) {
			jButton_Ignorer = new JButton();
			jButton_Ignorer.setPreferredSize(new Dimension(51, 26));
			// jButton_Ignorer.setText("Ignorer");
			jButton_Ignorer.setText(LangageManager.getProperty("ignore"));
			jButton_Ignorer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onIgnorer();
				}
			});
		}
		return jButton_Ignorer;
	}

	/**
	 *
	 *
	 */
	private void onOK() {
		if(listeDoublons.size() > 0) {
			Atome choix = (Atome)jComboBox_Choix.getSelectedItem();
			ArrayList<Atome> doublons = listeDoublons.get(0);
			Atome doublon;
			for(int i = 0 ; i < doublons.size() ; i++) {
				doublon = doublons.get(i);
				if(!doublon.equals(choix))
					DataProvider.changeIDArtiste(doublon.getIdArtiste(),choix.getIdArtiste());
			}

			listeDoublons.remove(0);
			if(listeDoublons.size() > 0)
				new JDialog_MauvaisesFrappes((Frame)getParent(),listeDoublons,tableModel);
		}
		//tableModel.directoryChanged(new File("c:\\"),DataProvider.getFichiersMauvaisesFrappes(),NavigatorListener.EVENT_JUMPED);

		this.dispose();
	}
	
	private void onIgnorer() {
		if(listeDoublons.size() > 0) {
			listeDoublons.remove(0);
			if(listeDoublons.size() > 0)
				new JDialog_MauvaisesFrappes((Frame)getParent(),listeDoublons,tableModel);
		}
		this.dispose();
	}

	/**
	 *
	 *
	 */
	private void onCancel() {
		this.dispose();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
