package miage.jtreeindex;
 
import entagged.tageditor.ProgressDialog;
import entagged.tageditor.TagEditorFrame;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.ResourcesRepository;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.color.ColorSpace;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
/**
 * @author Waldo2188 & titof & tsou
 *
 */
public class MainWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree myTree;
	public DefaultTreeModel MyDefaultTree;
	private JButton jButtonIndex = null;
	private JButton jButtonDesindex = null;
	private JLabel jLabelGerer = null;
	private TagEditorFrame TagEdit = null;

	private	PersonnalTreeCellRenderer myRenderer = new PersonnalTreeCellRenderer();
	public MainWindow(){
		//initialisation des composants
		initComponent("C:"+File.separatorChar);
	}
	
	public MainWindow(File selectedRoot) {
		String root = selectedRoot.getAbsolutePath();
		initComponent(root);
	}
	
	public MainWindow(String root) {
		initComponent(root);
	}
	
	public MainWindow(File selectedRoot,TagEditorFrame Tag) {
		String root = selectedRoot.getAbsolutePath();
		TagEdit = Tag;
		initComponent(root);
	}
	
	public MainWindow(String root, TagEditorFrame Tag) {
		TagEdit = Tag;
		initComponent(root);
	}

	private void initComponent(String selectedRoot) {
		//System.out.println(selectedRoot);
		//arrete l'application java lon quite la fenètre, mais le thread lancé continu son office
		
		//Instanciation de la Class Jtree
		//On passe en paramètre un Model
		//MyTreeModel est une classe qui hérite de DefaultTreeModel
		
		//File[] _roots=File.listRoots();
		//System.out.println(_roots[0].toString());
		//TagEditorFrame t = new TagEditorFrame();
		MyDefaultTree = new MyTreeModel(new MyFile(selectedRoot));
		JLabel j= new JLabel(LangageManager.getProperty("miage.diskexplorer")+selectedRoot.toString()) ;
		myTree = new JTree(MyDefaultTree);
		
		  
		// Application de l'afficheur à l'arbre.
		myTree.setCellRenderer(myRenderer); 

		myTree.setShowsRootHandles(true);
		
		
		//Ajout de l'arboressenece lorsqu'un répertoire est ouvert
		myTree.addTreeExpansionListener(new myExpensionListener());
		
		//Sélection d'une feuille
		myTree.addTreeSelectionListener(new myTreeSelectionListener());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 1, 1, 1);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(0, 1, 1, 1);
		gbc2.gridx = 1;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		
		
		//Instanciation de La Class JScrollPanne afin de pouvoir bénéficier des ascenceurs.
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setViewportView(myTree);
		scrollPane.add(new JLabel("hhh"));
		
		scrollPane.setMinimumSize(new Dimension(600, 200));
		
		
		jLabelGerer = new JLabel();
		jLabelGerer.setText(LangageManager.getProperty("miage.stockfoldersoptimum"));
		//jLabelGerer.setSize(400, 400);
		jLabelGerer.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel Jp1 = new JPanel();
		Jp1.setLayout(new BorderLayout());
		
		Jp1.add(j, BorderLayout.NORTH);
		
		
		JPanel Jp = new JPanel();
		Jp.setLayout(new BorderLayout());
		Jp.add(getJButtonIndex(), BorderLayout.NORTH);
		Jp.add(getJButtonDesindex(),BorderLayout.SOUTH);
		
		Jp.add(jLabelGerer, BorderLayout.CENTER);
		
		
		//Jp.setMinimumSize(new Dimension(200,200));
		//Jp.setBackground(Color.red);
		setLayout(new GridBagLayout());
		add(scrollPane, gbc);
		add(Jp1);
		add(Jp, gbc2);
		setBorder(new EmptyBorder(3, 5, 5, 3));
				
		setSize(600,400);
		
		setVisible(true);
		
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	

	public static void main(String[] args) {
		//instanciation de la fenêtre
		new MainWindow();
	}	
	
	public JTree getMyTree() {
		return myTree;
	}
	public void setMyTree(JTree myTree) {
		this.myTree = myTree;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonIndex() {
		if (jButtonIndex == null) {
			jButtonIndex = new JButton();
			jButtonIndex.setText(LangageManager.getProperty("miage.index"));
			jButtonIndex.setIcon( ResourcesRepository.getImageIcon("index.gif"));
			jButtonIndex.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final ProgressDialog progressDialog = new ProgressDialog(
							TagEdit, LangageManager.getProperty("miage.foldertoupdate"),
							LangageManager.getProperty("miage.updatingfolder"));
					progressDialog.setAbortable(false);
					progressDialog.hideAbortButton();
					progressDialog.setModal(true);
					new Thread(new Runnable() {
						public void run() {
							TreeUtil.ajoutdossier(myTreeSelectionListener.path);
							progressDialog.dispose();

						}
					}, LangageManager.getProperty("miage.updatingfolder")).start();
					progressDialog.setVisible(true);
					progressDialog.dispose();					
				}
			});			
		}
		return jButtonIndex;
	}
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDesindex() {
		if (jButtonDesindex == null) {
			jButtonDesindex = new JButton();
			jButtonDesindex.setText(LangageManager.getProperty("miage.unindex"));
			jButtonDesindex.setIcon( ResourcesRepository.getImageIcon("noindex.gif"));
			jButtonDesindex.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					final ProgressDialog progressDialog = new ProgressDialog(
							TagEdit, LangageManager.getProperty("miage.foldertoupdate"),
							LangageManager.getProperty("miage.updatingfolder"));
					progressDialog.setAbortable(false);
					progressDialog.hideAbortButton();
					progressDialog.setModal(true);
					new Thread(new Runnable() {
						public void run() {
							TreeUtil.supprimerdossier(myTreeSelectionListener.path);
							progressDialog.dispose();

						}
					}, LangageManager.getProperty("miage.updatingfolder")).start();
					progressDialog.setVisible(true);
					progressDialog.dispose();							
				}
			});			
		}
		return jButtonDesindex;
	}
	
	public DefaultTreeModel getMyDefaultTree() {
		return MyDefaultTree;
	}
}
