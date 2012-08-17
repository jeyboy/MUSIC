package entagged.tageditor.resources;

import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ResourcesRepository {
	
	public static ImageIcon getImageIcon(String name) {
		return new ImageIcon( getImage(name) );
	}
	
	public static Properties getLangage(String name) throws IOException {
		Properties langage = new Properties();
		langage.load( getStream("entagged/tageditor/resources/langage/", name) );
			
		return langage;
	}
	
	public static Properties getPreferences(String name) throws IOException {
		Properties pref = new Properties();
		pref.load( getStream("entagged/tageditor/resources/preferences/", name) );
		
		return pref;
	}
	
	public static URL getAboutFile() {
		return ResourcesRepository.class.getClassLoader().getResource("entagged/tageditor/resources/about/about.html");
	}
	
	private static Image getImage(String name) {
		try {
			return ImageIO.read( getStream("entagged/tageditor/resources/icons/", name) );
		} catch (IOException e) {
			System.out.println("Warning: Icon not loaded:"+e.getMessage());
			return null;
		}
	}
	
	private static InputStream getStream(String path, String name) {
		return ResourcesRepository.class.getClassLoader().getResourceAsStream(path + name);
	}
}