package miage.ihm;

import javax.swing.JPanel;

import java.util.ArrayList;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import entagged.tageditor.resources.LangageManager;

import miage.Atome;
import miage.ListeFichiers;
import miage.sgbd.DataProvider;

import java.awt.Insets;
import java.awt.Dimension;
import java.io.File;

public class JDialog_Doublons extends JDialog {

	private static final long serialVersionUID = 1L;

	private ArrayList<ArrayList<Atome>> listeDoublons;

	private JPanel jContentPane = null;
	private JTextArea jTextArea_Explication = null;
	private JLabel jLabel_Nom = null;
	private JComboBox jComboBox_Choix = null;
	private JButton jButton_OK = null;
	private JButton jButton_Cancel = null;

	private JButton jButton_Ignorer = null;

	private JButton jButton_Play = null;

	private JLabel jLabel_Dossier = null;

	private JLabel jLabel_DossierTitre = null;

	/**
	 * @param owner
	 */
	public JDialog_Doublons(JFrame owner, ArrayList<ArrayList<Atome>> listeDoublons) {
		super(owner,true);
		this.listeDoublons = listeDoublons;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 273);
		this.setLocationRelativeTo(getParent());
		// this.setTitle("Traitement des duplicatas");
		this.setTitle(LangageManager.getProperty("miage.duplicatesSearch"));
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
			gridBagConstraints.gridwidth = 4;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane.add(getJTextArea_Explication(), gridBagConstraints);
			
			if(listeDoublons.size() > 0) {
				// jLabel_Nom = new JLabel("Fichier musical retenu :");
				// jLabel_Nom.setText("Fichier musical retenu :");
				jLabel_Nom = new JLabel(LangageManager.getProperty("miage.duplicatesChooseFile"));
				jLabel_Nom.setText(LangageManager.getProperty("miage.duplicatesChooseFile"));
				
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 0;
				gridBagConstraints1.fill = GridBagConstraints.BOTH;
				gridBagConstraints1.insets = new Insets(5, 10, 10, 10);
				gridBagConstraints1.gridy = 2;
				
				GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
				gridBagConstraints21.gridx = 0;
				gridBagConstraints21.insets = new Insets(10, 10, 5, 10);
				gridBagConstraints21.fill = GridBagConstraints.BOTH;
				gridBagConstraints21.gridy = 1;
				jLabel_DossierTitre = new JLabel("Folder");
				jLabel_DossierTitre.setText("Dossier du fichier :");
				
				GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
				gridBagConstraints13.gridx = 1;
				gridBagConstraints13.gridwidth = 2;
				gridBagConstraints13.insets = new Insets(10, 10, 5, 10);
				gridBagConstraints13.fill = GridBagConstraints.BOTH;
				gridBagConstraints13.gridy = 1;
				jLabel_Dossier = new JLabel();
				
				
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.fill = GridBagConstraints.BOTH;
				gridBagConstraints2.gridy = 2;
				gridBagConstraints2.insets = new Insets(5, 10, 10, 10);
				gridBagConstraints2.gridwidth = 2;
				gridBagConstraints2.weightx = 0.0;
				gridBagConstraints2.gridx = 1;
				jContentPane.add(jLabel_Nom, gridBagConstraints1);
				jContentPane.add(jLabel_DossierTitre, gridBagConstraints21);
				jContentPane.add(jLabel_Dossier, gridBagConstraints13);
				
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0;
				gridBagConstraints3.fill = GridBagConstraints.BOTH;
				gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints3.weightx = 0.0;
				gridBagConstraints3.gridy = 4;
				jContentPane.add(getJComboBox_Choix(), gridBagConstraints2);
				jContentPane.add(getJButton_OK(), gridBagConstraints3);
				
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.gridx = 1;
				gridBagConstraints11.insets = new Insets(10, 10, 10, 10);
				gridBagConstraints11.fill = GridBagConstraints.BOTH;
				gridBagConstraints11.weightx = 1.0;
				gridBagConstraints11.gridy = 4;
				jContentPane.add(getJButton_Ignorer(), gridBagConstraints11);
				
				GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
				gridBagConstraints12.gridx = 3;
				gridBagConstraints12.insets = new Insets(5, 10, 10, 10);
				gridBagConstraints12.fill = GridBagConstraints.BOTH;
				gridBagConstraints12.gridy = 2;
				
				jContentPane.add(getJButton_Play(), gridBagConstraints12);
			}
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 4;
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
				// jTextArea_Explication.setText("Plusieurs fichiers musicaux semblent identiques.\nIl pourrait s'agir de doublons.\n\nVous pouvez choisir le fichier que vous préférez conserver.\nSi vous ne souhaitez pas apporter de modification, cliquez sur Fermer");
				jTextArea_Explication.setText(LangageManager.getProperty("miage.duplicatesExplication"));
			else
				// jTextArea_Explication.setText("Aucun doublon n'a été trouvé");
				jTextArea_Explication.setText(LangageManager.getProperty("miage.duplicatesNoneFound"));
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
			jComboBox_Choix.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					onItemStateChanged();
				}
			});
			ArrayList<Atome> doublons = listeDoublons.get(0);
			for(int i = 0 ; i < doublons.size() ; i++)
				jComboBox_Choix.addItem(doublons.get(i));
		}
		return jComboBox_Choix;
	}

	private void onItemStateChanged() {
		Atome a = (Atome)getJComboBox_Choix().getSelectedItem();
		if(a != null)
			jLabel_Dossier.setText(a.getDossierFichier());
	}

	/**
	 * This method initializes jButton_OK
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton_OK() {
		if(jButton_OK == null) {
			jButton_OK = new JButton("OK");
			jButton_OK.setText(LangageManager.getProperty("common.dialog.ok"));
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
			jButton_Cancel = new JButton("Close");
			jButton_Cancel.setText(LangageManager.getProperty("close"));
			jButton_Cancel.setPreferredSize(new Dimension(30, 26));
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
			jButton_Ignorer = new JButton("Ignore");
			jButton_Ignorer.setPreferredSize(new Dimension(30, 26));
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
	 * This method initializes jButton_Play	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_Play() {
		if(jButton_Play == null) {
			jButton_Play = new JButton("Play");
			jButton_Play.setText(LangageManager.getProperty("play"));
			jButton_Play.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPlay();
				}
			});
		}
		return jButton_Play;
	}

	/**
	 * Valide le choix
	 * Passe au suivant
	 */
	private void onOK() {
		if(listeDoublons.size() > 0) {
			Atome choix = (Atome)jComboBox_Choix.getSelectedItem();
			ArrayList<Atome> doublons = listeDoublons.get(0);
			Atome doublon;
			for(int i = 0 ; i < doublons.size() ; i++) {
				doublon = doublons.get(i);
				if(!doublon.equals(choix))
					DataProvider.supprimerFichier(doublon.getIdFichier());
			}

			listeDoublons.remove(0);
			if(listeDoublons.size() > 0)
				new JDialog_Doublons((JFrame)getParent(),listeDoublons);
		}

		this.dispose();
	}
	
	private void onIgnorer() {
		if(listeDoublons.size() > 0) {
			listeDoublons.remove(0);
			if(listeDoublons.size() > 0)
				new JDialog_Doublons((JFrame)getParent(),listeDoublons);
		}
		this.dispose();
	}

	/**
	 * Ferme la fenêtre
	 */
	private void onCancel() {
		this.dispose();
	}
	
	private void onPlay() {
		Atome choix = (Atome)jComboBox_Choix.getSelectedItem();
		String url = choix.getNomDossier() + File.separator + choix.getNomFichier();
		ListeFichiers.play((JFrame)getParent(), url);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
