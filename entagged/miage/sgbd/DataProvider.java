package miage.sgbd;

import java.io.File;
import java.util.ArrayList;

import miage.Atome;
import miage.Format;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;

/**
 * Classe servant de liaison entre le SQLProvider et le reste du programme
 * @author Nicolas Velin ; Clément Fourel
 */
public class DataProvider {

	// BASE DE DONNEES

	public static void cleanTable() {
		SqlProvider.dropTable();
		SqlProvider.createTable();
	}

	// TABLE FICHIER

	/**
	 * Insère une liste de fichiers dans la BD
	 * @param fichiers la liste de fichiers à insérer
	 */
	public static void insererFichiers(ArrayList fichiers) {
		for(int i = 0 ; i < fichiers.size() ; i++)
			insererFichier((AudioFile)fichiers.get(i));
	}

	/**
	 * Remplit toutes les tables avec les tags du fichier
	 * @param f le fichier Audio à insérer
	 */
	public static void insererFichier(AudioFile f) {

		int idDossier = insererDossier(f);
		int idAlbum = insererAlbum(f);
		int idArtiste = insererArtiste(f);
		int idGenre = insererGenre(f);

		// Insertion du fichier
		String nom = Format.nameDoubleQuotes(f.getName());
		String piste = Format.nameWithoutBrackets(f.getTag().getTrack().toString());
		piste = Format.tagWithoutIndication(piste, "piste");
		String titre = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getTitle().toString());
		titre = Format.tagWithoutIndication(titre, "titre");
		String comment = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getComment().toString());
		int bitrate = f.getBitrate();
		int duree = f.getLength();
		int taille = (int)f.length();

		SqlProvider.insertIntoFichier(nom,idDossier,piste,titre,idArtiste,idAlbum,idGenre,comment,bitrate, taille, duree);
	}

	/**
	 * Modifie et ajoute les informations dans les tables après modification du fichier
	 * @param f le fichier audio modifié
	 */
	public static void modifierFichier(AudioFile f) {

		int idDossier = insererDossier(f);
		int idAlbum = insererAlbum(f);
		int idArtiste = insererArtiste(f);
		int idGenre = insererGenre(f);

		int idFichier = f.getID();
		String nom = Format.nameDoubleQuotes(f.getName());
		String piste = Format.nameWithoutBrackets(f.getTag().getTrack().toString());
		String titre = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getTitle().toString());
		String comment = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getComment().toString());
		int bitrate = f.getBitrate();
		int duree = f.getLength();
		int taille = (int)f.length();

		SqlProvider.updateFichier(idFichier,nom,idDossier,piste,titre,idArtiste,idAlbum,idGenre,comment,bitrate, taille, duree);
	}

	/**
	 *
	 * @param id
	 */
	public static void supprimerFichier(int id) {
		ArrayList<Object[]> fichierPhysique = SqlProvider.getFichierPhysique(id);
		if(fichierPhysique.size() == 1) {
			String path = (String)fichierPhysique.get(0)[0];
			String fichier = (String)fichierPhysique.get(0)[1];
			File f = new File(path + File.separator + fichier);
			f.delete();
			SqlProvider.deleteFromFichier(id);
		}
	}

	/**
	 *
	 * @param f
	 * @return
	 */
	public static int getFichierID(AudioFile f) {
		int idDossier = insererDossier(f);
		String Nom = Format.nameDoubleQuotes(f.getName());
		return SqlProvider.getFichierID(Nom, idDossier);
	}

	/**
	 * Permet d'obtenir une liste de fichiers contenant dans un de leurs tags la chaîne passée en paramètre
	 * @param recherche le tag devant être recherché dans les fichiers
	 * @return
	 */
	public static File [] getFichiers(String recherche) {
		ArrayList<Object []> liste = SqlProvider.getFichiers(recherche);
		int taille = liste.size();
		File [] fichiersAudio = new File [taille];
		for(int i = 0 ; i < taille ; i++) {
			try {
				Object [] o = liste.get(i);
				String str = o[0] + File.separator + o[1];
				fichiersAudio[i] = AudioFileIO.read(new File(str));
			}
			catch(CannotReadException e) {
				e.printStackTrace();
			}
		}
		return fichiersAudio;
	}
	
	// TABLE DOSSIER

	/**
	 * Insertion du dossier correspondant
	 * @param f
	 * @return
	 */
	public static int insererDossier(AudioFile f) {
		String path = Format.nameDoubleQuotes(f.getParent());
		int taille = 0;
		int nombre = 0;
		int id = SqlProvider.insertIntoDossier(path,taille,nombre);

		if(id == -1)
			id = SqlProvider.getDossierID(path);

		return id;
	}
	
	public static int supprimerDossier(String path) {
		path = Format.nameDoubleQuotes(path);
		int idDossier = SqlProvider.getDossierID(path);
		return idDossier;
	}
	
	/**
	 * Retourne une arraylist des dossiers indexés
	 */
	public static ArrayList<String> getDossier(){
		ArrayList<String> AL = SqlProvider.getDossiersNoms();
		return AL;
		
	}

	// TABLE ALBUM

	/**
	 * Insertion de l'album correspondant, si tag Album présent
	 * @param f
	 * @return
	 */
	public static int insererAlbum(AudioFile f) {
		String nom = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getAlbum().toString());
		nom = Format.tagWithoutIndication(nom, "album");
		String annee = Format.nameWithoutBrackets(f.getTag().getYear().toString());
		annee = Format.tagWithoutIndication(annee, "annee");
		int id = -1;

		if(!nom.equals("")) {
			id = SqlProvider.insertIntoAlbum(nom, annee);
			if(id == -1)
				id = SqlProvider.getAlbumID(nom);
		}

		return id;
	}


	// TABLE ARTISTE

	/**
	 * Insertion de l'artiste correspondant, si tag Artiste présent
	 * @param f
	 * @return
	 */
	public static int insererArtiste(AudioFile f) {
		String nom = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getArtist().toString());
		nom = Format.tagWithoutIndication(nom, "artiste");
		int id = -1;

		if(!nom.equals("")) {
			id = SqlProvider.insertIntoArtiste(nom);
			if(id == -1)
				id = SqlProvider.getArtisteID(nom);
		}

		return id;
	}

	/**
	 * Change l'id artiste d'un fichier et supprimed'un
	 * @param sourceID l'id artiste source
	 * @param cibleID l'id artiste cible
	 */
	public static void changeIDArtiste(int sourceID, int cibleID) {
		ArrayList<Object[]> fichierPhysique = SqlProvider.getFichiers(sourceID);
		String newArtist = SqlProvider.getArtisteNom(cibleID);
		for(int i = 0 ; i < fichierPhysique.size() ; i++) {
			try {
				String path = (String)fichierPhysique.get(i)[0];
				String fichier = (String)fichierPhysique.get(i)[1];
				File f = new File(path + File.separator + fichier);
				AudioFile music = AudioFileIO.read(f);
				music.getTag().setArtist(newArtist);
				AudioFileIO.write(music);
			}
			catch(CannotReadException e) {
				e.printStackTrace();
			}
			catch(CannotWriteException e) {
				e.printStackTrace();
			}
		}

		SqlProvider.updateFichier(sourceID, cibleID);
		SqlProvider.deleteFromArtiste(sourceID);
	}

	private static ArrayList<Object[]> doubleArtiste = null;

	private static ArrayList<Object[]> getDoubleArtiste() {
		if(doubleArtiste == null)
			doubleArtiste = SqlProvider.getArtistesDoubles();
		return doubleArtiste;
	}

	private static ArrayList<Object[]> doubleAlbum = null;

	private static ArrayList<Object[]> getDoubleAlbum() {
		if(doubleAlbum == null)
			doubleAlbum = SqlProvider.getAlbumsDoubles();
		return doubleAlbum;
	}

	private static ArrayList<Object[]> doubleFichier = null;

	private static ArrayList<Object[]> getDoubleFichier() {
		if(doubleFichier == null) {
			insererTableTempo();
			doubleFichier = SqlProvider.getDuplicatas();
		}
		return doubleFichier;
	}

	public static ArrayList<Object[]> getCachedDouble(String table) {
		ArrayList<Object[]> Doubles = null;

		if(table.equalsIgnoreCase("artiste"))
			Doubles = getDoubleArtiste();
		else if(table.equalsIgnoreCase("album"))
			Doubles = getDoubleAlbum();
		else if(table.equalsIgnoreCase("fichier"))
			Doubles = getDoubleFichier();

		return Doubles;
	}

	public static void resetCache() {
		doubleArtiste = null;
		doubleAlbum = null;
		doubleFichier = null;
		doublesFichier = null;
		doublesArtiste = null;
		doublesAlbum = null;
	}

	private static ArrayList<ArrayList<Atome>> doublesFichier = null;
	private static ArrayList<ArrayList<Atome>> doublesArtiste = null;
	private static ArrayList<ArrayList<Atome>> doublesAlbum = null;

	public static boolean sameID (Atome a1, Atome a2) {
		if(a1.getID() == a2.getID())
			return true;
		else
			return false;
	}
	
	public static boolean tabContientAtome (ArrayList<Atome> tabAt, Atome a) {
		for (int i = 0; i < tabAt.size(); ++i) {
			Atome at = tabAt.get(i);
			if(sameID(at, a))
				return true;
			else continue;
		}
		return false;
	}
	
	
	public static void addAtomeInList (ArrayList<Atome> tabAt, Atome Val) {
		int i = 0;
		for ( ; i < tabAt.size(); ++i) {
			Atome at = tabAt.get(i);
			if (!sameID(at, Val)) continue;
			else break;
			
		}
		if (i == tabAt.size()) tabAt.add(Val);
	}
	
	public static ArrayList<ArrayList<Atome>> getDoublesInit(String table) {
		resetCache();
		return getDoubles(table);
	}
	
	/**
	 * Recherche d'artistes ou d'albums identiques dont l'écriture diffère légèrement
	 * @param table la table cible : artiste ou album
	 * @return
	 */
	private static ArrayList<ArrayList<Atome>> getDoubles(String table) {
		if ((table.equals("artiste") && doublesArtiste == null)
				|| (table.equals("album") && doublesAlbum == null)
				|| (table.equals("fichier") && doublesFichier == null)) {

			ArrayList<ArrayList<Atome>> Liste = new ArrayList<ArrayList<Atome>>();
			ArrayList<Object[]> Doubles = getCachedDouble(table);
			if (Doubles.size() == 0)
				return Liste;
			ArrayList<Object[]> groupesDoubles = Doubles;

			Object[] b = { -1, -1, "", "", "", "", -1, -1, -1, -1, -1, -1, -1,
					-1 };
			Object[] a = { -1, -1, "", "" };

			int k = 0;
			for (int i = 0; i < Doubles.size(); i++) {
				for (int j = i + 1; j < Doubles.size(); j++) {
					if ((Doubles.get(i)[1] == Doubles.get(j)[0] && Doubles
							.get(i)[0] == Doubles.get(j)[1])
							|| (Doubles.get(i)[1] == Doubles.get(j)[0] && Doubles
									.get(i)[0] != Doubles.get(j)[1])) {
						if (table.equals("fichier"))
							groupesDoubles.set(j, b);
						else
							groupesDoubles.set(j, a);
					}
				}
			}
			Doubles = new ArrayList<Object[]>();
			k = 0;
			for (int i = 0; i < groupesDoubles.size(); i++) {
				if (table.equals("fichier")) {
					if (groupesDoubles.get(i) != b)
						Doubles.add(k++, groupesDoubles.get(i));
				} else {
					if (groupesDoubles.get(i) != a)
						Doubles.add(k++, groupesDoubles.get(i));
				}
			}

			// Premier groupe d'atomes

			Atome at1 = null;
			Atome at2 = null;
			for (int i = 0; i < Doubles.size(); i++) {

				// Création d'un nouveau groupe
				ArrayList<Atome> grp = new ArrayList<Atome>();

				if (table.equals("fichier")) {
					int idFic1 = ((Integer) Doubles.get(i)[0]).intValue();
					int idFic2 = ((Integer) Doubles.get(i)[1]).intValue();

					String nomFic1 = (String) Doubles.get(i)[2];
					String nomFic2 = (String) Doubles.get(i)[3];
					String titreFic1 = (String) Doubles.get(i)[4];
					String titreFic2 = (String) Doubles.get(i)[5];

					int dureeFic1 = ((Integer) Doubles.get(i)[6]).intValue();
					int dureeFic2 = ((Integer) Doubles.get(i)[7]).intValue();

					int idArtiste1 = ((Integer) Doubles.get(i)[8]).intValue();

					String nomArtiste1 = SqlProvider.getArtisteNom(idArtiste1);

					int idArtiste2 = ((Integer) Doubles.get(i)[9]).intValue();
					String nomArtiste2 = SqlProvider.getArtisteNom(idArtiste2);

					int idAlbum1 = ((Integer) Doubles.get(i)[10]).intValue();
					String nomAlbum1 = SqlProvider.getAlbumNom(idAlbum1);
					int idAlbum2 = ((Integer) Doubles.get(i)[11]).intValue();
					String nomAlbum2 = SqlProvider.getAlbumNom(idAlbum2);

					int idDossier1 = ((Integer) Doubles.get(i)[12]).intValue();
					String nomDossier1 = SqlProvider.getDossierNom(idDossier1);
					int idDossier2 = ((Integer) Doubles.get(i)[13]).intValue();
					String nomDossier2 = SqlProvider.getDossierNom(idDossier2);
					at1 = new Atome(idFic1, nomFic1, titreFic1, dureeFic1,
							idArtiste1, nomArtiste1, idAlbum1, nomAlbum1,
							idDossier1, nomDossier1);
					at2 = new Atome(idFic2, nomFic2, titreFic2, dureeFic2,
							idArtiste2, nomArtiste2, idAlbum2, nomAlbum2,
							idDossier2, nomDossier2);
				} else {
					at1 = new Atome(((Integer) Doubles.get(i)[0]).intValue(),
							(String) Doubles.get(i)[2], table);
					at2 = new Atome(((Integer) Doubles.get(i)[1]).intValue(),
							(String) Doubles.get(i)[3], table);
				}
				grp.add(at1);
				grp.add(at2);

				for (int j = i; j < Doubles.size(); j++) {

					if (table.equals("fichier")) {
						int idFic1 = ((Integer) Doubles.get(j)[0]).intValue();
						int idFic2 = ((Integer) Doubles.get(j)[1]).intValue();
						String nomFic1 = (String) Doubles.get(j)[2];
						String nomFic2 = (String) Doubles.get(j)[3];
						String titreFic1 = (String) Doubles.get(j)[4];
						String titreFic2 = (String) Doubles.get(j)[5];
						int dureeFic1 = ((Integer) Doubles.get(j)[6])
								.intValue();
						int dureeFic2 = ((Integer) Doubles.get(j)[7])
								.intValue();

						int idArtiste1 = ((Integer) Doubles.get(j)[8])
								.intValue();
						String nomArtiste1 = SqlProvider
								.getArtisteNom(idArtiste1);
						int idArtiste2 = ((Integer) Doubles.get(j)[9])
								.intValue();
						String nomArtiste2 = SqlProvider
								.getArtisteNom(idArtiste2);

						int idAlbum1 = ((Integer) Doubles.get(j)[10])
								.intValue();
						String nomAlbum1 = SqlProvider.getAlbumNom(idAlbum1);
						int idAlbum2 = ((Integer) Doubles.get(j)[11])
								.intValue();
						String nomAlbum2 = SqlProvider.getAlbumNom(idAlbum2);

						int idDossier1 = ((Integer) Doubles.get(j)[12])
								.intValue();
						String nomDossier1 = SqlProvider
								.getDossierNom(idDossier1);
						int idDossier2 = ((Integer) Doubles.get(j)[13])
								.intValue();
						String nomDossier2 = SqlProvider
								.getDossierNom(idDossier2);
						at1 = new Atome(idFic1, nomFic1, titreFic1, dureeFic1,
								idArtiste1, nomArtiste1, idAlbum1, nomAlbum1,
								idDossier1, nomDossier1);
						at2 = new Atome(idFic2, nomFic2, titreFic2, dureeFic2,
								idArtiste2, nomArtiste2, idAlbum2, nomAlbum2,
								idDossier2, nomDossier2);
					}
					else {
						at1 = new Atome(((Integer) Doubles.get(j)[0])
								.intValue(), (String) Doubles.get(j)[2], table);
						at2 = new Atome(((Integer) Doubles.get(j)[1]).intValue(), (String) Doubles.get(j)[3], table);
					}

					if (DataProvider.tabContientAtome(grp, at1)) {
						DataProvider.addAtomeInList(grp, at2);

						if (table.equals("fichier"))
							Doubles.set(j, b);
						else
							Doubles.set(j, a);

					} else if (DataProvider.tabContientAtome(grp, at2)) {
						DataProvider.addAtomeInList(grp, at1);

						if (table.equals("fichier"))
							Doubles.set(j, b);
						else
							Doubles.set(j, a);
					}
				}
				if (grp.size() != 0 && grp.get(0).getID() != -1)
					Liste.add(grp);
			}

			if (table.equalsIgnoreCase("artiste"))
				doublesArtiste = Liste;
			else if (table.equalsIgnoreCase("album"))
				doublesAlbum = Liste;
			else if (table.equalsIgnoreCase("fichier"))
				doublesFichier = Liste;
		}
		if (table.equalsIgnoreCase("artiste"))
			return doublesArtiste;
		else if (table.equalsIgnoreCase("album"))
			return doublesAlbum;
		else if (table.equalsIgnoreCase("fichier"))
			return doublesFichier;
		else
			return new ArrayList<ArrayList<Atome>>();
	}


	// TABLE GENRE

	/**
	 * Insertion du genre correspondant, si tag Genre présent
	 * @param f
	 * @return
	 */
	public static int insererGenre(AudioFile f) {
		String genre = Format.nameWithoutBracketsDoubleQuotes(f.getTag().getGenre().toString());
		genre = Format.tagWithoutIndication(genre, "genre");
		int id = -1;

		if(!genre.equals("")) {
			id = SqlProvider.insertIntoGenre(genre);
			if(id == -1)
				id = SqlProvider.getGenreID(genre);
		}

		return id;
	}

	/**
	 * Indique si deux artistes ou albums sont identiques ou non
	 * @param Id1 id du premier artiste ou album
	 * @param Id2 id du second artiste ou album
	 * @param table table concernée
	 * @return
	 */
	public static boolean AreSame(int Id1, int Id2, String table) {
		ArrayList<ArrayList<Atome>> metaListe = null;
		if (table.compareToIgnoreCase("artiste") == 0) metaListe = getDoubles("artiste");
		else if (table.compareToIgnoreCase("album") == 0) metaListe = getDoubles("album");

		for (int i = 0; i < metaListe.size(); i++) {
			boolean ExisteInd1 = false;
			boolean ExisteInd2 = false;
			ArrayList<Atome> grpe = metaListe.get(i);
			for (int j = 0; j < grpe.size(); j++) {
				if (table.compareToIgnoreCase("artiste") == 0) {
					if (grpe.get(j).getIdArtiste() == Id1)
						ExisteInd1 = true;
					else if (grpe.get(j).getIdArtiste() == Id2)
						ExisteInd2 = true;
				}
				else if (table.compareToIgnoreCase("album") == 0) {
					if (grpe.get(j).getIdAlbum() == Id1)
						ExisteInd1 = true;
					else if (grpe.get(j).getIdAlbum() == Id2)
						ExisteInd2 = true;
				}

			}
			if (ExisteInd1 && ExisteInd2)
				return true;
		}
		return false;
	}

	public static void insererTableTempo () {
		ArrayList<Object[]> tab = SqlProvider.getDuplicatas2();
		SqlProvider.deleteEtoileFromTempo();
		for (int i=0; i < tab.size(); ++i) {
			Object[] a = tab.get(i);

			int idArtiste = -1;
			int idAlbum = -1;
			if (a[8] != null)
				idArtiste = ((Integer)a[8]).intValue();
			if (a[10] != null)
				idAlbum = ((Integer)a[10]).intValue();
			SqlProvider.insertIntoTempo(((Integer)a[0]).intValue(), Format.nameDoubleQuotes((String) a[2]),  Format.nameDoubleQuotes((String) a[4]), idArtiste, idAlbum, ((Integer)a[12]).intValue(),((Integer)a[6]).intValue());
		}
	}
}