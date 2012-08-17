package miage;

/**
 * @author Clément Fourel
 */
public class Chaines {

	/*
	 * Distance de levenshtein
	 */
	public static int LD(String s, String t) {
		int n = s.length();
		int m = t.length();

		if(n == 0)
			return m;
		else if(m == 0)
			return n;
		else {
			int[][] d = new int[n + 1][m + 1];

			for(int i = 0 ; i <= n ; d[i][0] = i++);
			for(int j = 1 ; j <= m ; d[0][j] = j++);

			for(int i = 1 ; i <= n ; i++) {
				char sc = s.charAt(i - 1);
				for(int j = 1 ; j <= m ; j++) {
					int v = d[i - 1][j - 1];
					if(t.charAt(j - 1) != sc)
						v++;
					d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), v);
				}
			}
			return d[n][m];
		}
	}

	public static String SupprDiacr(String Chaine) {
		int CstNbDiacr = 11;
		char TabDiacr[] = {'à', 'â', 'é', 'è', 'ê', 'ë', 'î', 'ï', 'ô', 'û', 'ç'};
		char TabBase[] = {'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'o', 'u', 'c'};
		char TabChaine[] = Chaine.toCharArray();

		for(int i = 0 ; i < Chaine.length() ; ++i)
			for(int j = 0 ; j < CstNbDiacr ; ++j)
				if(TabChaine[i] == TabDiacr[j]) {
					TabChaine[i] = TabBase[j];
					break;
				}
		Chaine = String.copyValueOf(TabChaine);
		return Chaine;
	}

	public static String CalculCle(String Chaine) {
		String Ch = "";
		Chaine = SupprDiacr(Chaine.toLowerCase());
		
		char TabChaine[] = Chaine.toCharArray();

		// Suppression des espaces, points et doubles caractères consécutifs
		for(int i = 0 ; i < Chaine.length() ; ++i) {
			if(TabChaine[i] == ' ' || TabChaine[i] == '.' || (i < (Chaine.length() - 1) && TabChaine[i] == TabChaine[i + 1]))
				continue;

			Ch += TabChaine[i];
		}
		return Ch;
	}

	public static boolean AreSameString(String s, String t) {
		if (s.length() == 0 || s.length() == 0 ) return false;
		if (s.startsWith("track") || s.startsWith("Track") || t.startsWith("track") || t.startsWith("Track")) return false;
		s = CalculCle(s);
		t = CalculCle(t);
		double NbLV = LD(s, t);

		int TailleS = s.length();
		int TailleT = t.length();

		int Max;

		if(TailleS > TailleT)
			Max = TailleS;
		else
			Max = TailleT;

		return (NbLV / Max < 0.25);
	}
}