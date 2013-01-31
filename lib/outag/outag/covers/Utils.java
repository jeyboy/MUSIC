package outag.covers;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static final Pattern[] stripPatterns = {
			Pattern.compile(" ?\\(dis[ck] \\d+?\\)"),
			Pattern.compile("^(the|a|an|el|le|les|la|los|die|der|das|den) "),
			Pattern.compile(" (the|a|an|el|le|les|la|los|die|der|das|den) "),
			Pattern.compile("('|~|\\!|@|#|\\$|%|\\^|\\*|_|\\[|\\{|\\]|\\}|\\||\\\\|;|:|`|\"|<|,|>|\\.|\\?|/|&)")
			};
	
	public static String normalize(String name) {
		name = name.toLowerCase();

		for (int i = 0; i < stripPatterns.length; i++) {
			Matcher ma = stripPatterns[i].matcher(name);
			name = ma.replaceAll("");
		}

		return name;
	}
	
	public static String getExtension(URL f) {
		String name = f.toExternalForm().toLowerCase();
		int i = name.lastIndexOf( "." );
		if(i == -1)
			return "";
		
		return name.substring( i + 1 );
	}
}