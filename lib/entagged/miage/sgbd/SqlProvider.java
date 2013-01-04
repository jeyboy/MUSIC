package miage.sgbd;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import miage.Format;

/**
 * Classe faisant l'interface entre la connexion et le reste du programme
 * C'est la seul classe pouvant exécuter des requetes
 * @author Nicolas Velin ; Clément Fourel
 */
public class SqlProvider {

	private static ArrayList<String> ALFic = new ArrayList<String>();
	public static String DossierAct = null;
	/**
	 * Exécute une requete de mise à jour
	 * @param requete la requete à exécuter
	 * @return Nombre de valeurs affectées par la mise à jour
	 */
	private static int update(String requete) {
		return Connexion.getInstance().executeUpdate(requete);
	}

	/**
	 * Exécute une requete de sélection
	 * @param requete la requete à exécuter
	 * @return ResultSet représentant le résultat de la requête
	 */
	public static ResultSet select(String requete) {
		return Connexion.getInstance().executeQuery(requete);
	}

	/**
	 * Supprime une table
	 * @param table la table à supprimer
	 * @return
	 */
	private static int drop(String table) {
		String requete = "DROP TABLE " + table;
		return update(requete);
	}
	
	/**
	 * crée un index
	 * @param champ de la table à indéxer
	 * @return
	 */
	private static int createIndex(String table, String champ, String nom) {
		String requete = "create INDEX " + nom + " on " + table + "(" + nom + ")";
		return update(requete);
	}

	/**
	 * Récupère tout les éléments d'une table
	 * @param table la table sur laquelle la requete est exécuté
	 * @return le résultat de la requete
	 */
	public static ResultSet selectEtoile(String table) {
		return select("SELECT * FROM " + table);
	}

	/**
	 * Supprime tous les éléments de la table
	 * @param table la table d'où les éléments seront supprimés
	 * @return
	 */
	private static int deleteEtoile(String table) {
		return update("DELETE FROM " + table);
	}

	protected static int deleteEtoileFromTempo() {
		return update("DELETE FROM TEMPO");
	}


	// BASE DE DONNEES

	/**
	 *
	 */
	public static void createDataBase() {
		//dropTable();
		createTable();
		createAlias("DISTANCE","miage.Chaines.AreSameString");
		createAlias("DISTANCE_A","miage.sgbd.DataProvider.AreSame");
	}

	/**
	 * Batch de création des table
	 */
	protected static void createTable() {
		createDossier();
		createArtiste();
		createAlbum();
		createGenre();
		createFichier();
		createTempo();
	}

	/**
	 * Batch de suppression des table
	 */
	protected static void dropTable() {
		SqlProvider.drop("fichier");
		SqlProvider.drop("tempo");
		SqlProvider.drop("dossier");
		SqlProvider.drop("artiste");
		SqlProvider.drop("album");
		SqlProvider.drop("genre");
	}
	
	/**
	 * Batch de création des index
	 */
	private static void createAllIndex() {
		SqlProvider.createIndex("artiste", "nom", "index_artiste");
		SqlProvider.createIndex("album", "nom", "index_album");
		SqlProvider.createIndex("dossier", "path", "index_dossier");
		SqlProvider.createIndex("genre", "genre", "index_genre");
		SqlProvider.createIndex("fichier", "nom", "index_nom");
		SqlProvider.createIndex("fichier", "titre", "index_titre");
	}

	/**
	 * Batch de suppression des tables
	 */
	protected static void deleteTable() {
		SqlProvider.deleteEtoile("fichier");
		SqlProvider.deleteEtoile("dossier");
		SqlProvider.deleteEtoile("artiste");
		SqlProvider.deleteEtoile("album");
		SqlProvider.deleteEtoile("genre");
		SqlProvider.deleteEtoile("tempo");
	}

	/**
	 *
	 * @param fonction
	 */
	private static void createAlias(String nom, String fonction) {
		update("GRANT ALL ON CLASS \"" + fonction + "\" TO PUBLIC");
		update("CREATE ALIAS " + nom + " FOR \"" + fonction + "\"");
	}


	// TABLE DOSSIER

	/**
	 * Créer la table dossier
	 * @return
	 */
	public static int createDossier() {
		String requete = "CREATE TABLE dossier (id INTEGER NOT NULL IDENTITY PRIMARY KEY, path VARCHAR NOT NULL, taille INTEGER DEFAULT 0 NOT NULL, nombre INTEGER DEFAULT 0 NOT NULL, CONSTRAINT DossierUnique UNIQUE(path))";
		return update(requete);
	}

	/**
	 * Insère une nouvelle ligne dans la table dossier
	 * @param path le chemin du dossier
	 * @param taille la taille de tous les fichiers du dossier
	 * @param nombre le nombre de fichiers dans ce dossier
	 * @return
	 */
	public static int insertIntoDossier(String path, int taille, int nombre) {
		String requete = "INSERT INTO dossier (path,taille,nombre) VALUES ('" + path + "'," + taille + "," + nombre + ")";
		return update(requete);
	}

	/**
	 * Supprime les lignes de la table dossier ayant un chemin particulier
	 * @param path le chemin du dossier à supprimer
	 * @return
	 */
	public static int DeleteFromDossier(String path) {
		String requete = "DELETE FROM dossier WHERE path = '" + path + "'";
		return update(requete);
	}
	
	public static int DeleteFromDossierId (int id) {
		String requete = "DELETE FROM dossier WHERE ID = " + id ;
		return update(requete);
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static int getDossierID(String path) {
		String requete = "SELECT id FROM dossier WHERE path = '" + path + "'";
		ResultSet id = select(requete);
		return Connexion.resultsetUnEntier(id);
	}
	
	
	public static String getDossierNom(int id) {
		String requete = "SELECT path FROM dossier WHERE id = " + id + "";
		ResultSet nom = select(requete);
		return Connexion.resultsetUneChaine(nom);
	}
	
	public static ArrayList<String> getDossiersNoms() {
		ArrayList<String> Arr = new ArrayList<String>();
		String requete = "SELECT path FROM dossier";
		try{
			ResultSet Rs = select(requete);
			while (Rs.next()) {
			    Arr.add(Rs.getString(1));
			}
		}
		catch (Exception exc){
			System.out.println(exc);
		}
		
		return Arr;

	}


	// TABLE ALBUM

	/**
	 * Créer la table album
	 * @return
	 */
	public static int createAlbum() {
		String requete = "CREATE TABLE album (id INTEGER NOT NULL IDENTITY PRIMARY KEY, nom VARCHAR NOT NULL, annee VARCHAR NOT NULL, CONSTRAINT AlbumUnique UNIQUE(nom))";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @param annee
	 * @return
	 */
	public static int insertIntoAlbum(String nom, String annee) {
		String requete = "INSERT INTO album (nom,annee) VALUES ('" + nom + "', '" + annee + "')";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int deleteFromAlbum(String nom) {
		String requete = "DELETE FROM album where nom = '" + nom + "'";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int getAlbumID(String nom) {
		String requete = "SELECT id FROM album WHERE nom = '" + nom + "'";
		ResultSet id = select(requete);
		return Connexion.resultsetUnEntier(id);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public static String getAlbumNom(int id) {
		String requete = "SELECT nom FROM album WHERE id = " + id + "";
		ResultSet nom = select(requete);
		return Connexion.resultsetUneChaine(nom);
	}

	/**
	 * Récupère tous les albums identiques dont l'écriture diffère
	 * @return le/les album(s) correspondant
	 */
	private static ResultSet selectMauvaisesFrappesAlbum() {
		//return select("select distinct(A.id), AL.id, A.nom,  AL.nom from album A , album AL where lower(A.nom) <> lower(AL.nom) and DISTANCE(A.nom,AL.nom) = true and A.id < AL.id");
		return select("select distinct(A.id), AL.id, A.nom,  AL.nom from album A , album AL where lower(A.nom) <> lower(AL.nom) and DISTANCE(A.nom,AL.nom) = true");
	}
	/**
	 *
	 * @return
	 */
	public static ArrayList<Object []> getAlbumsDoubles() {
		ResultSet rst = selectMauvaisesFrappesAlbum();
		return resultsetEnListe(rst);
	}


	// TABLE GENRE

	/**
	 * Créer la table genre
	 * @return
	 */
	public static int createGenre() {
		String requete = "CREATE TABLE genre (id INTEGER NOT NULL IDENTITY PRIMARY KEY, genre VARCHAR NOT NULL, CONSTRAINT GenreUnique UNIQUE(genre))";
		return update(requete);
	}

	/**
	 *
	 * @param genre
	 * @return
	 */
	public static int insertIntoGenre(String genre) {
		String requete = "INSERT INTO genre (genre) VALUES ('" + genre + "')";
		return update(requete);
	}

	/**
	 *
	 * @param genre
	 * @return
	 */
	public static int deleteFromGenre(String genre) {
		String requete = "DELETE FROM genre where genre = '" + genre + "'";
		return update(requete);
	}

	/**
	 *
	 * @param genre
	 * @return
	 */
	public static int getGenreID(String genre) {
		String requete = "SELECT id FROM genre WHERE genre = '" + genre + "'";
		ResultSet id = select(requete);
		return Connexion.resultsetUnEntier(id);
	}


	// TABLE ARTISTE

	/**
	 * Créer la table artiste
	 * @return
	 */
	public static int createArtiste() {
		String requete = "CREATE TABLE artiste (id INTEGER NOT NULL IDENTITY PRIMARY KEY, nom VARCHAR NOT NULL, CONSTRAINT ArtisteUnique UNIQUE(nom))";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int insertIntoArtiste(String nom) {
		String requete = "INSERT INTO artiste (nom) VALUES ('" + nom + "')";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int deleteFromArtiste(String nom) {
		String requete = "DELETE FROM artiste where nom_artiste = '" + nom + "'";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int deleteFromArtiste(int id) {
		String requete = "DELETE FROM artiste where id = " + id;
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @return
	 */
	public static int getArtisteID(String nom) {
		String requete = "SELECT id FROM artiste WHERE nom = '" + nom + "'";
		ResultSet id = select(requete);
		return Connexion.resultsetUnEntier(id);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public static String getArtisteNom(int id) {
		String requete = "SELECT nom FROM artiste WHERE id = " + id + "";
		ResultSet nom = select(requete);
		return Connexion.resultsetUneChaine(nom);
	}

	/**
	 * Récupère tous les artistes identiques dont l'écriture diffère
	 * @return le/les artistes correspondant
	 */
	private static ResultSet selectMauvaisesFrappesArtiste() {
		//return select("select distinct(A.id), AR.id, A.nom,  AR.nom from artiste A , artiste AR where lower(A.nom) <> lower(AR.nom) and (lower(A.nom) not like '%artsite inconnu%' and lower(A.nom) not like '%inconnu%' and lower(A.nom) not like '%unknown%' and lower(A.nom) not like '%new artist%') and DISTANCE(A.nom,AR.nom) = true and A.id < AR.id");
		return select("select distinct(A.id), AR.id, A.nom,  AR.nom from artiste A , artiste AR where lower(A.nom) <> lower(AR.nom) and (lower(A.nom) not like '%artsite inconnu%' and lower(A.nom) not like '%inconnu%' and lower(A.nom) not like '%unknown%' and lower(A.nom) not like '%new artist%') and DISTANCE(A.nom,AR.nom) = true");
	}

	/**
	 *
	 * @return
	 */
	public static ArrayList<Object []> getArtistesDoubles() {
		ResultSet rst = selectMauvaisesFrappesArtiste();
		return resultsetEnListe(rst);
	}

	
	// TABLE FICHIER

	/**
	 * Créer la table fichier
	 * @return
	 */
	public static int createFichier() {
		String requete = "CREATE TABLE fichier (id INTEGER NOT NULL IDENTITY PRIMARY KEY, nom VARCHAR NOT NULL, id_dossier INTEGER NOT NULL," + "num_piste VARCHAR," + "titre VARCHAR," + "id_artiste INTEGER DEFAULT NULL," + "id_album INTEGER DEFAULT NULL," + "id_genre INTEGER DEFAULT NULL," + "comment VARCHAR," + "bitrate INTEGER DEFAULT 0 NOT NULL," + "taille INTEGER DEFAULT 0 NOT NULL," + "duree INTEGER DEFAULT 0 NOT NULL," + "FOREIGN KEY (id_dossier) REFERENCES dossier (id)," + "FOREIGN KEY (id_artiste) REFERENCES artiste (id)," + "FOREIGN KEY (id_album) REFERENCES album (id)," + "FOREIGN KEY (id_genre) REFERENCES genre (id))";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @param id_dossier
	 * @param num_piste
	 * @param titre
	 * @param id_artiste
	 * @param id_album
	 * @param id_genre
	 * @param comment
	 * @param bitrate
	 * @param taille
	 * @param duree
	 * @return
	 */
	public static int insertIntoFichier(String nom, int id_dossier, String num_piste, String titre, int id_artiste, int id_album, int id_genre, String comment, int bitrate, int taille, int duree) {
		String colonnes;
		String valeurs;

		colonnes = "nom, id_dossier, num_piste, titre, ";
		valeurs = "'" + nom + "', " + id_dossier + ", " + "'" + num_piste + "', " + "'" + titre + "', ";

		if(id_artiste > -1) {
			colonnes += "id_artiste, ";
			valeurs += id_artiste + ", ";
		}
		if(id_album > -1) {
			colonnes += "id_album, ";
			valeurs += id_album + ", ";
		}
		if(id_genre > -1) {
			colonnes += "id_genre, ";
			valeurs += id_genre + ", ";
		}

		colonnes += "comment, bitrate, taille, duree";
		valeurs += "'" + comment + "', " + bitrate + ", " + taille + ", " + duree;
		
		String requete = "INSERT INTO fichier (" +  colonnes + ") VALUES (" + valeurs + ")";
		return update(requete);
	}

	/**
	 *
	 * @param id
	 * @param nom
	 * @param id_dossier
	 * @param num_piste
	 * @param titre
	 * @param id_artiste
	 * @param id_album
	 * @param id_genre
	 * @param comment
	 * @param bitrate
	 * @param taille
	 * @param duree
	 * @return
	 */
	public static int updateFichier(int id, String nom, int id_dossier, String num_piste, String titre, int id_artiste, int id_album, int id_genre, String comment, int bitrate, int taille, int duree) {
		String requete = "UPDATE fichier" +
						" SET nom = '" + nom + "'," +
						" id_dossier = " + id_dossier + "," +
						" num_piste = '" + num_piste + "'," +
						" titre = '" + titre + "'," +
						" id_artiste = " + id_artiste + "," +
						" id_album = " + id_album + "," +
						" id_genre = " + id_genre + "," +
						" comment = '" + comment + "'," +
						" bitrate = " + bitrate + "," +
						" taille = " + taille + "," +
						" duree = " + duree +
						" WHERE id = " + id;
		return update(requete);
	}

	public static int updateFichier(int sourceID, int cibleID) {
		String requete = "UPDATE fichier SET id_artiste = " + cibleID + " WHERE id_artiste = " + sourceID;
		return update(requete);
	}

	/**
	 * Suppression d'un ligne Fichier à partir de son id
	 * @param nom
	 * @return
	 */
	public static int deleteFromFichier(int id) {
		String requete = "DELETE FROM fichier where id = " + id;
		return update(requete);
	}

	/**
	 * Suppression d'un ligne Fichier à partir de son nom
	 * @param nom
	 * @return
	 */
	public static int deleteFromFichier(String nom) {
		String requete = "DELETE FROM fichier where nom_fichier = '" + nom + "'";
		return update(requete);
	}
	
	/**
	 *
	 * @param nom
	 * @param idDossier
	 * @return
	 */
	public static int getFichierID(String nom, int idDossier) {
		String requete = "SELECT id FROM fichier WHERE nom = '" + nom + "' AND id_dossier = " + idDossier;
		ResultSet id = select(requete);
		return Connexion.resultsetUnEntier(id);
	}
	
	/**
	 * Modif MIAGE 2008.
	 * @param path
	 * @return
	 */
	public static boolean FichierExist(String path){
		int i = path.lastIndexOf(File.separatorChar);
		String dossier = path.substring(0, i);
		String fichier = path.substring(i+1);
		if (DossierAct==null || !DossierAct.equals(dossier)){
			ALFic.clear();
			DossierAct = dossier;
			int IdDossier = SqlProvider.getDossierID(Format.nameDoubleQuotes(dossier));
			if (IdDossier !=-1){
				String requete = "SELECT nom FROM fichier WHERE id_dossier = " + IdDossier;
				try{
					ResultSet Rs = select(requete);
					while (Rs.next()) {
					    ALFic.add(Rs.getString(1));
					}
				}
				catch (Exception exc){
					System.out.println(exc);
				}
			}
		}
		if (ALFic.contains(fichier)) return true;

		return false;
	}
	
	public static ArrayList<String> FichierDossier(String dossier) {
		ArrayList<String> AL = new ArrayList<String>();
		int IdDossier = SqlProvider.getDossierID(Format.nameDoubleQuotes(dossier));
		if (IdDossier !=-1){
			String requete = "SELECT nom FROM fichier WHERE id_dossier = " + IdDossier;
			try{
				ResultSet Rs = select(requete);
				while (Rs.next()) {
				    AL.add(Rs.getString(1));
				}
			}
			catch (Exception exc){
				System.out.println(exc);
			}
		}
		return AL;
	}

	/**
	 * Supprime tous les fichiers correspondants à un dossier
	 * @param IdDossier
	 */
	public static int supprimerFichierDossier (int IdDossier) {
		String requete = "DELETE FROM fichier where id_dossier = " + IdDossier;
		return update(requete);
	}
	
	public static ArrayList<Object[]> getFichierPhysique(int id) {
		String requete = "SELECT d.path, f.nom FROM fichier f JOIN dossier d ON f.id_dossier = d.id WHERE f.id = " + id;
		ResultSet rst = select(requete);
		return resultsetEnListe(rst);
	}

	public static int getNbFichier() {
		String requete = "SELECT count(*)id FROM fichier";
		ResultSet nb = select(requete);
		return Connexion.resultsetUnEntier(nb);
	}

	/**
	 *
	 * @return
	 */
	public static ArrayList<Object []> getFichiers() {
		String requete = "SELECT d.path, f.nom FROM fichier f, dossier d WHERE f.id_dossier = d.id";
		ResultSet rst = select(requete);
		return resultsetEnListe(rst);
	}

	/**
	 *
	 * @return
	 */
	public static ArrayList<Object []> getFichiers(int id_artiste) {
		String requete = "SELECT d.path, f.nom FROM fichier f JOIN dossier d ON f.id_dossier = d.id WHERE f.id_artiste = " + id_artiste;
		ResultSet rst = select(requete);
		return resultsetEnListe(rst);
	}

	/**
	 * Récupère tous les fichiers en référence à un mot clé
	 * @param mot clé
	 * @return le/les fichiers correspondant
	 */
	private static ResultSet selectFicParMotCle(String motCle) {
		if(motCle.equals("*"))
			return select("SELECT d.path, f.nom FROM fichier F, dossier D where F.id_dossier = D.id");
		else {
			motCle = motCle.toLowerCase();
			motCle = Format.nameDoubleQuotes(motCle);
			return select("SELECT d.path, f.nom FROM fichier F, artiste AR, album AL, genre G, dossier D where F.id_dossier = D.id and F.id_artiste = AR.id and F.id_album = AL.id and F.id_genre = G.id and (DISTANCE('" + motCle + "', Titre) = true or DISTANCE('" + motCle + "', AL.nom) = true or DISTANCE('" + motCle + "', AR.nom) = true or DISTANCE('" + motCle + "', G.genre) = true or lower(Titre) like '%" + motCle + "%' or lower(F.nom) like '%" + motCle + "%' or lower(AL.nom) like '%" + motCle + "%' or lower(AR.nom) like '%" + motCle + "%')");
		}
	}

	/**
	 * Récupère les fichiers duplicatas
	 * Duplicatas : fichiers ayant même titre(ou équivalent), même artiste(ou équivalent), même album(ou équivalent), même durée
	 * @return le/les fichiers correspondant
	 */
	private static ResultSet selectFicDuplicatas() {
		return select("select distinct(F1.id), F2.id, F1.nom, F2.nom, F1.titre, F2.titre, F1.duree, F2.duree, F1.id_artiste, F2.id_artiste, F1.id_album, F2.id_album, F1.id_dossier, F2.id_dossier from tempo F1, tempo F2 where (F1.id_artiste = F2.id_artiste) and (F1.id_album = F2.id_album) and F1.duree = F2.duree and F1.id <> F2.id");
	}
	/**
	 * Récupère les fichiers duplicatas
	 * Duplicatas : fichiers ayant même titre(ou équivalent), même artiste(ou équivalent), même album(ou équivalent), même durée
	 * @return le/les fichiers correspondant
	 */
	private static ResultSet selectFicDuplicatas2() {
		return select("select distinct(F1.id), F2.id, F1.nom, F2.nom, F1.titre, F2.titre, F1.duree, F2.duree, F1.id_artiste, F2.id_artiste, F1.id_album, F2.id_album, F1.id_dossier, F2.id_dossier  from fichier F1, fichier F2 where ((F1.titre = F2.titre and F1.titre <> '' and F1.titre not like 'track%' and F1.titre not like 'Track%') or DISTANCE(F1.titre,F2.titre)= true) and F1.duree = F2.duree and F1.id <> F2.id");
	}

	public static ArrayList<Object []> getDuplicatas() {
		ResultSet rst = selectFicDuplicatas();
		return resultsetEnListe(rst);
	}
	public static ArrayList<Object []> getDuplicatas2() {
		ResultSet rst = selectFicDuplicatas2();
		return resultsetEnListe(rst);
	}
	/**
	 *
	 * @return
	 */
	public static ArrayList<Object []> getFichiers(String recherche) {
		ResultSet rst = selectFicParMotCle(recherche);
		return resultsetEnListe(rst);
	}

	/**
	 *
	 * @return
	 */
	public static String getPathFichiers(int id) {
		ArrayList<Object []> tab = getFichierPhysique(id);
		String path = (String) tab.get(0)[0] + File.separator + (String)tab.get(0)[1];
		System.out.println(path);
		return path;
	}

	/**
	 * Retourne une liste contenant chaque ligne du resultset
	 * @param resultset le resultat d'une requete
	 * @return l'ArrayList contenant les n-uplets
	 */
	public static ArrayList<Object []> resultsetEnListe(ResultSet rset) {
		ArrayList<Object []> liste = new ArrayList<Object []>();
		try {
			ResultSetMetaData rsmd = rset.getMetaData();
	        int numColumns = rsmd.getColumnCount();
			int i = 0;
			while(rset.next()) {
				Object [] o = new Object[numColumns];
				for (int j = 1 ; j <= numColumns ; j++)
					o[j-1] = rset.getObject(j);
				liste.add(o);
				i++;
			}
			rset.close();
		}
		catch(SQLException e) {
			if(Connexion.DEBUG)
				e.printStackTrace();
		}
		return liste;
	}
	

	// TABLE TEMPO

	/**
	 * Créer la table fichier
	 * @return
	 */
	public static int createTempo() {
		String requete = "CREATE TABLE tempo (id INTEGER NOT NULL, nom VARCHAR NOT NULL, "  + "titre VARCHAR," + "id_artiste INTEGER DEFAULT NULL," + "id_album INTEGER DEFAULT NULL,"  + "id_dossier INTEGER DEFAULT NULL," + "duree INTEGER DEFAULT 0 NOT NULL, "  + "FOREIGN KEY (id_artiste) REFERENCES artiste (id)," + "FOREIGN KEY (id_dossier) REFERENCES dossier (id)," + "FOREIGN KEY (id_album) REFERENCES album (id)" +")";
		return update(requete);
	}

	/**
	 *
	 * @param nom
	 * @param id_dossier
	 * @param num_piste
	 * @param titre
	 * @param id_artiste
	 * @param id_album
	 * @param id_genre
	 * @param comment
	 * @param bitrate
	 * @param taille
	 * @param duree
	 * @return
	 */
	public static int insertIntoTempo(int id, String nom, String titre, int id_artiste, int id_album, int id_dossier, int duree) {
		String colonnes;
		String valeurs;

		colonnes = "id, nom, id_dossier, titre, ";
		valeurs = id + ", '" + nom + "', " + id_dossier + ", " + "'" + titre + "', ";


		if(id_artiste > -1) {
			colonnes += "id_artiste, ";
			valeurs += id_artiste + ", ";
		}
		if(id_album > -1) {
			colonnes += "id_album, ";
			valeurs += id_album + ", ";
		}

		colonnes += "duree";
		valeurs += duree;

		String requete = "INSERT INTO tempo (" +  colonnes + ") VALUES (" + valeurs + ")";

		return update(requete);

	}
}