package miage.sgbd;

import java.io.File;
import java.sql.*;

import entagged.tageditor.resources.PreferencesManager;

/**
 * La classe gérant les fonctions élémentaires pour la gestion d'une BD
 * @author Nicolas Velin ; Clément Fourel
 */
public class Connexion {
	
	public static final boolean DEBUG = false; // Permet d'afficher les exceptions lors des updates

	private Connection connexion;

	/** L'instance statique */
	private static Connexion instance;

	/**
	 * Constructeur redéfini comme étant privé pour interdire
	 * son appel et forcer à passer par la méthode getInstance()
	 */
	private Connexion() {
		initConnexion();
	}

	/**
	 * Récupère l'instance unique de la class Singleton.
	 * Remarque : le constructeur est rendu inaccessible
	 * @return la connexion créée
	 */
	public static Connexion getInstance() {
		if(instance == null) // Premier appel
			instance = new Connexion();
		return instance;
	}

	/**
	 * Initialise la connexion
	 * Par exemple : C:\Documents and Settings\NSV\.entagged\database
	 */
	private void initConnexion() {
		try {
			String dbName = "database";
			String login = "sa";
			String pwd = "";
			String url = PreferencesManager.USER_PROPERTIES_DIR + "" + File.separatorChar + "" +dbName;
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			connexion = DriverManager.getConnection("jdbc:hsqldb:file:" + url + "; shutdown=true", login,  pwd);
		}
		catch (Exception e) {
			if(DEBUG)
				e.printStackTrace();
		}
	}
	
	/**
	 * Envoi une requete du type SELECT
	 * @param requete une chaine de caractère représentant la requete SQL
	 * @return ResulSet représentant le résultat de la requête
	 */
	protected ResultSet executeQuery(String requete) {
		Statement statement;
		ResultSet resultat = null;
		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery(requete);
			statement.close();
		}
		catch (SQLException e) {
			if(DEBUG)
				e.printStackTrace();
		}
		return resultat;
	}
	
	/**
	 * Retourne l'entier contenu dans un resulset 
	 * @param resultset le resultat d'une requete ne contenant qu'un seul élément
	 * @return l'élément unique
	 */
	protected static int resultsetUnEntier(ResultSet rset) {
		int id = -1;
		try {
			if(rset.next())
				id = rset.getInt(1);
			rset.close();
		}
		catch(SQLException e) {
			if(DEBUG)
				e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Retourne la chaine contenu dans un resulset 
	 * @param resultset le resultat d'une requete ne contenant qu'un seul élément
	 * @return l'élément unique
	 */
	protected static String resultsetUneChaine(ResultSet rset) {
		String ch = "";
		try {
			if(rset.next())
				ch = rset.getString(1);
			rset.close();
		}
		catch(SQLException e) {
			if(DEBUG)
				e.printStackTrace();
		}
		return ch;
	}
	
	/**
	 * Récupère le dernier ID créé
	 * @return
	 */
	private int getID() {
		ResultSet resultat = executeQuery("CALL IDENTITY()");
		return resultsetUnEntier(resultat);
	}

	/**
	 * Envoi une requete du type CREATE, UPDATE, DELETE, INSERT
	 * @param requete une chaine de caractère représentant la requete SQL
	 * @return Nombre de valeurs affectées par la mise à jour
	 * 
	 */
	protected int executeUpdate(String requete) {
		Statement statement;
		int nbVal = -1;
		
		try {
			statement = connexion.createStatement();
			nbVal = statement.executeUpdate(requete);
			if(requete.startsWith("INSERT"))
				nbVal = getID();
			statement.close();
		}
		catch (SQLException e) {
			if(DEBUG)
				e.printStackTrace();
		}
		return nbVal;
	}

	/**
	 * Ferme proprement la connexion en arretant la BD
	 * Elle termine l'objet Connection et le supprime
	 */
	public void shutdown() {
		Statement statement;
		try {
			statement = connexion.createStatement();
			statement.executeQuery("SHUTDOWN");
			statement.close();
			connexion.close();
		}
		catch (SQLException e) {
			if(DEBUG)
				e.printStackTrace();
		}
	}
}