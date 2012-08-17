package miage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import miage.jtreeindex.PersonnalTreeCellRenderer;
import miage.sgbd.DataProvider;
import miage.sgbd.SqlProvider;

import entagged.audioformats.AudioFile;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.tageditor.optionpanels.FreedbOptionPanel;
import entagged.tageditor.optionpanels.GeneralOptionPanel;
import entagged.tageditor.optionpanels.OptionDialog;
import entagged.tageditor.optionpanels.OptionPanelInterface;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;

/**
 * Classe permettant la recherche de fichiers physique
 * @author Nicolas Velin
 */
public class ListeFichiers {

	// PARTIE OBJET

	private Vector hierarchieDossier;

	public ListeFichiers(File dossier) {
		hierarchieDossier = new Vector();
		while(dossier != null) {
			hierarchieDossier.add(dossier);
			dossier = dossier.getParentFile();
		}
	}

	/**
	 *
	 * @param listeDossiers
	 * @return
	 */
	public boolean isChild(Vector listeDossiers) {
		boolean child = false;

		String folder1, folder2;
		int i = 0, j;
		int imax = hierarchieDossier.size();
		int jmax = listeDossiers.size();
		while(!child && i < imax) {
			folder1 = ((File)hierarchieDossier.get(i)).getAbsolutePath();
			j = 0;
			while(!child && j < jmax) {
				folder2 = ((File)listeDossiers.get(j)).getAbsolutePath();
				if(folder1.compareToIgnoreCase(folder2) == 0)
					child = true;
				j++;
			}
			i++;
		}

		return child;
	}

	/**
	 *
	 * @param dossier
	 * @return
	 */
	public boolean isChild(File dossier) {
		boolean child = false;

		String folder1;
		String folder2 = dossier.getAbsolutePath();
		int i = 0;
		int imax = hierarchieDossier.size();
		while(!child && i < imax) {
			folder1 = ((File)hierarchieDossier.get(i)).getAbsolutePath();
			if(folder1.compareToIgnoreCase(folder2) == 0)
				child = true;
			i++;
		}

		return child;
	}


	// PARTIE STATIQUE

	public final static String MP3 = "MP3";
	public final static String WMA = "WMA";
	public final static String OGG = "OGG";
	public final static String FLAC = "FLAC";
	public final static String MPC = "MPC";
	public final static String APE = "APE";
	private static ArrayList fichiers;

	/**
	 * Recherche de fichier sans prendre en compte la notion de sous dossiers.
	 * @param element
	 * @param type
	 */
	public static void rechercherFichierSansSousDossiers(String element, ArrayList type) {
		File f = new File(element);
		String [] elements = f.list();
		if(elements != null)
			for(int i = 0 ; i < elements.length ; i++){
				String Fichier = element + File.separatorChar + elements[i];
				File fic = new File(Fichier);
				
				if(fic.exists()) {
					if (!fic.isDirectory()){
						StringTokenizer str = new StringTokenizer(Fichier, ".");
						String extAudio = "";
						while(str.hasMoreTokens()){
							extAudio = str.nextToken();
						}
						if(type.contains(extAudio.toUpperCase())) {
							try {
								AudioFile af = entagged.audioformats.AudioFileIO.read(fic);
								fichiers.add(af);
							}
							catch (CannotReadException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
	}
	/**
	 * Recherche des fichiers dans un dossiers et ses sous dossiers ayant un certain type
	 * @param element C'est un fichier ou un dossier
	 * @param type Les différents types audios recherchés
	 */
	public static void rechercheFichierRecursive(String element, ArrayList type) {
		File f = new File(element);
		if(f.exists()) {
			// L'élément est un dossier
			if(f.isDirectory()) {
				String [] elements = f.list();
				if(elements != null)
					for(int i = 0 ; i < elements.length ; i++)
						rechercheFichierRecursive(element + File.separatorChar + elements[i], type);
			}
			// L'élément est un fichier
			else {
				StringTokenizer str = new StringTokenizer(element, ".");
				String extAudio = "";
				while(str.hasMoreTokens())
					extAudio = str.nextToken();
				if(type.contains(extAudio.toUpperCase())) {
					try {
						AudioFile af = entagged.audioformats.AudioFileIO.read(f);
						fichiers.add(af);
					}
					catch (CannotReadException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	/**
	 * Lance la recherche de fichiers récursive
	 * @param racine le dossier de départ
	 * @return la liste des fichiers de type AudioFile
	 */
	public static ArrayList rechercheFichier(String racine) {
		ArrayList type = new ArrayList();
		type.add(MP3);
		type.add(WMA);
		type.add(OGG);
		type.add(FLAC);
		type.add(MPC);
		type.add(APE);
		fichiers = new ArrayList();
		rechercheFichierRecursive(racine,type);
		return fichiers;
	}
	
	/**
	 * Lance la recherche de fichier sans les sous dossiers
	 * @param racine du dossier de recherche
	 * @return la liste des fichiers de type AudioFile
	 */
	public static ArrayList rechercheFichierPasSousDossier(String racine) {
		ArrayList type = new ArrayList();
		type.add(MP3);
		type.add(WMA);
		type.add(OGG);
		type.add(FLAC);
		type.add(MPC);
		type.add(APE);
		fichiers = new ArrayList();
		rechercherFichierSansSousDossiers(racine,type);
		return fichiers;
	}

	/**
	 * Lance la recherche de fichiers dans un Thread et ajoute le contenu dans la table
	 * @param listeDossier les dossiers de départ
	 * @param owner la fenetre principale
	 */
	public static void rechercheFichierThread(final String[] listeDossier) {
		Thread performer = new Thread(new Runnable() {
			public void run() {
				ArrayList liste;
				for(int i = 0 ; i < listeDossier.length ; i++) {
					liste = ListeFichiers.rechercheFichier(listeDossier[i]);
					DataProvider.insererFichiers(liste);
				}
				int NbFichiers = SqlProvider.getNbFichier();
				JOptionPane.showMessageDialog(null, LangageManager.getProperty("miage.indexationisfinished")+"\n" + NbFichiers + LangageManager.getProperty("miage.filesindexed"), LangageManager.getProperty("miage.filesindexation"), JOptionPane.INFORMATION_MESSAGE);
			}
		}, "Performer");
		performer.start();
	}
	
	public static void rechercheFicThreadSansSousDossier(final String dossier){
			ArrayList liste;
			liste = ListeFichiers.rechercheFichierPasSousDossier(dossier);
			DataProvider.insererFichiers(liste);
			if (liste.size()>0)
				JOptionPane.showMessageDialog(null, LangageManager.getProperty("miage.folderindexed"), LangageManager.getProperty("miage.filesindexation"), JOptionPane.INFORMATION_MESSAGE);
			else 
				JOptionPane.showMessageDialog(null, LangageManager.getProperty("miage.folderinindexable"), LangageManager.getProperty("miage.filesindexation"), JOptionPane.ERROR_MESSAGE);
			
	}
	
	/**
	 * Cette fonction permet d'actualiser le contenu d'un seul dossier sans prendre en compte les sous-dossiers.
	 * @param dossier racine du dossier à actualiser.
	 */
	public static void ActualiserDossier(final String dossier) {
		int idDossier = DataProvider.supprimerDossier(dossier);
		if (idDossier != -1) {
			SqlProvider.supprimerFichierDossier(idDossier);
			SqlProvider.DeleteFromDossierId(idDossier);
		}
		rechercheFicThreadSansSousDossier(dossier);	
		PersonnalTreeCellRenderer.AL = DataProvider.getDossier();
	}
	
	public static void SupprimerDossier (final String dossier) {
		int idDossier = DataProvider.supprimerDossier(dossier);
		if (idDossier != -1) {
			SqlProvider.supprimerFichierDossier(idDossier);
			SqlProvider.DeleteFromDossierId(idDossier);
			JOptionPane.showMessageDialog(null, LangageManager.getProperty("miage.folderunindexed"), LangageManager.getProperty("miage.filesindexation"), JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, LangageManager.getProperty("miage.foldernotindexed"), LangageManager.getProperty("miage.filesindexation"), JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * La fonction retourne le taux d'indexation du dossier.
	 * @param dossier
	 * @return
	 */
	public static int StatistiqueDossier (final String dossier, int etape) {
		ArrayList liste;
		liste = ListeFichiers.rechercheFichierPasSousDossier(dossier);
		int pourcfinal = -1;
		if (liste.size()>0)
		{	
			//Je récupère les fichiers audio d'un dossier.
			ArrayList ListeIndex;
			ListeIndex = SqlProvider.FichierDossier(dossier);
			if (ListeIndex.size()>0) {
				int Taille = liste.size();
				int Tailleindex = ListeIndex.size();
				ArrayList<String> ListeIdent = new ArrayList<String>();
				for (int i = 0; i<Taille ; i++){
					for (int j = 0; j<Tailleindex; j++){
						//On teste si le fichier existe dans les deux listes.
						if((((File)liste.get(i)).getName()).equals(ListeIndex.get(j))) {
							//Si les noms de fichiers sont identiques on conserve dans une troisieme liste les identiques.
							//System.out.println((((File)liste.get(i)).getName())+" == "+ListeIndex.get(j).toString());
							ListeIdent.add(ListeIndex.get(j).toString());
						}
					}
				}
				int TailleIdent = ListeIdent.size();
				int TailleDif = 0;
				float pourcentage = 0;
				if (Taille>Tailleindex){
					TailleDif = Taille;
				}
				else {
					TailleDif = Tailleindex;
				}
				if(etape==1) {
					//Si il y en a qui sont indéxés et qui n'existe plus le pourcentage diminue.
					if (Tailleindex>TailleIdent) 
						TailleDif += Tailleindex-TailleIdent;
				}
				else if (etape==2) {
					TailleDif = Taille;
				}
				
				pourcentage = (float)TailleIdent/TailleDif;
				pourcentage *=100;
				pourcfinal = (int) pourcentage;
			}
			else 
				pourcfinal = -2; //correspond à dossier non indéxés.
		}
		return pourcfinal;
	}
	
	public static void play(JFrame owner, String url) {
		String prog = PreferencesManager.get("entagged.mediaplayer");
		boolean error = true;
		while(error) {
			if(prog == null || !new File(prog).exists()) {
				OptionPanelInterface general = new GeneralOptionPanel(owner);
				OptionPanelInterface freedb = new FreedbOptionPanel();
				OptionPanelInterface[] opts = new OptionPanelInterface[] {general, freedb};
				OptionDialog eod = new OptionDialog(owner,opts);
				eod.setVisible(true);
			}
			prog = PreferencesManager.get("entagged.mediaplayer");

			try {
				Runtime.getRuntime().exec("\"" + prog + "\" \"" + url + "\"");
				error = false;
			}
			catch(IOException e1) {
				final int choice = JOptionPane.showConfirmDialog(owner, LangageManager.getProperty("miage.errorreaderchoice"), "Erreur", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == JOptionPane.NO_OPTION) {
					break;
				}
				prog = null;
			}
		}
	}
	
}
