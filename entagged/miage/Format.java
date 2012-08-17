package miage;

/**
 * Modifie les chaines de caractères
 * @author Clément Fourel ; Nicolas Velin
 */
public class Format {
	
	/**
	 * Enleve les crochets dans la chaine
	 * @param str
	 * @return
	 */
	public static String nameWithoutBrackets(String str) {
		str = str.replace("[", "");
		return str.replace("]", "");
	}
	
	/**
	 * Supprime l'indication du tag pour le format WMA (ex. : ALBUM : ...)
	 * @param str, colonne
	 * @return
	 */
	public static String tagWithoutIndication(String str, String colonne) {
		if (colonne.equals("artiste"))
			str = str.replace("ARTIST : ", "");
		else if (colonne.equals("genre"))
			str = str.replace("GENRE : ", "");
		else if (colonne.equals("album"))
			str = str.replace("ALBUM : ", "");
		else if (colonne.equals("annee"))
			str = str.replace("YEAR : ", "");
		else if (colonne.equals("piste"))
			str = str.replace("TRACK : ", "");
		else if (colonne.equals("titre"))
			str = str.replace("TITLE : ", "");
		
		return str;
	}
	
	/**
	 * Dédouble les quotes
	 * @param str
	 * @return
	 */
	public static String nameDoubleQuotes(String str) {
		return str.replaceAll("'", "''");
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String nameWithoutBracketsDoubleQuotes(String str) {
		str = nameWithoutBrackets(str);
		return Format.nameDoubleQuotes(str);
	}
}
