package service;

import java.awt.Desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

public class IOOperations {
	public static PrintWriter GetWriter(String path, boolean autoflush, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
    	File f = new File(path);
		FileOutputStream fis = new FileOutputStream(f, append);
		OutputStreamWriter in = new OutputStreamWriter(fis, "UTF-8");
		return new PrintWriter(in, autoflush);
	}
	
	public static BufferedReader GetReader(String path) throws UnsupportedEncodingException, FileNotFoundException {
		InputStreamReader in = new InputStreamReader(new FileInputStream(path), "UTF-8");
		return new BufferedReader(in);
	}
	
	
	public static boolean copyFile(File fromFile, File toFile) throws IOException {
	    if (!fromFile.exists()) return true;
	    if (!fromFile.isFile()) return true;

	    if (!fromFile.canRead())
			JOptionPane.showOptionDialog(  
					null,  
					"File " + fromFile.getName() + " is not readable",  
					"", JOptionPane.OK_OPTION,  
					JOptionPane.ERROR_MESSAGE, null, null,  
					null 
	        ); 

	    if (toFile.isDirectory())
	      toFile = new File(toFile, fromFile.getName());

	    if (toFile.exists())
	    	if (!toFile.canWrite())
	    	{
				JOptionPane.showOptionDialog(  
						null,  
						"You dont have permission for writing in directory " + toFile.getPath(),  
						"", JOptionPane.OK_OPTION,  
						JOptionPane.ERROR_MESSAGE, null, null,  
						null 
		        );
				return false;
	    	}

	    FileInputStream from = null;
	    FileOutputStream to = null;
	    try {
	      from = new FileInputStream(fromFile);
	      to = new FileOutputStream(toFile);
	      byte[] buffer = new byte[4096];
	      int bytesRead;

	      while ((bytesRead = from.read(buffer)) != -1)
	        to.write(buffer, 0, bytesRead); // write
	    } 
	    finally {
	      if (from != null)
	        try { from.close(); }
	      	catch (IOException e) { Errorist.printLog(e); }
	      if (to != null)
	        try { to.close(); }
	      	catch (IOException e) { Errorist.printLog(e); }
	    }
	    return true;
	}	
    public static void execFile(String program, String path) {
//    	SecurityManager.checkExec(String)
    	try { Runtime.getRuntime().exec(program + " " + path); }
    	catch(SecurityException e) {}
    	catch(IOException e) { Errorist.printLog(e); }
    	finally {/*Show error message*/}
    }
    
    public static boolean deleteFile(String path) 	{ return deleteFile(new File(path)); }    
    public static boolean deleteFile(File f) 		{ return f.delete(); }
	
	// application associated to a file extension
	public static boolean open(File file) {
		try{
		    Desktop dt = Desktop.getDesktop();
		    dt.open(file);
		    return true;
	    }
	    catch (Exception e) { Errorist.printLog(e); }
		return openW(file);
	}
	
	//open shared pathes
	static boolean openW(File file) {
		try{
			Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file);
//			Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", " ./lib/aapt d badging ZingMp3.apk"});
			return true;
	    }
	    catch (Exception e) { Errorist.printLog(e); }
		return false;
	}

	public static void print(File document) throws IOException {
	    Desktop dt = Desktop.getDesktop();
	    dt.print(document);
	}

	// default browser
	public static void browse(URI document) throws IOException {
	    Desktop dt = Desktop.getDesktop();
	    dt.browse(document);
	}

	// default mail client
	//   use the mailto: protocol as the URI
//	    ex : mailto:elvis@heaven.com?SUBJECT=Love me tender&BODY=love me sweet
	public static void mail(URI document) throws IOException {
	    Desktop dt = Desktop.getDesktop();
	    dt.mail(document);
	}	
		
	public static Collection<File> ScanDirectories(File[] IOItems) {
	    final Collection<File> all = new ArrayList<File>();
	    for(File item:IOItems) addFilesRecursively(item, all);
	    return all;
	}
	
	private static void addFilesRecursively(File file, Collection<File> all) {
	    final File[] children = file.listFiles();
	    if (children != null)
	        for (File child : children) addFilesRecursively(child, all);
	    else all.add(file);
	}
	

	public static char extensionSeparator = '.'; 
	public static String extension(String fullPath) {
		int dot = fullPath.toLowerCase().lastIndexOf(extensionSeparator);
		return dot == -1 ? "" : fullPath.substring(dot + 1);
	}
	public static String name_without_extension(String title, String ext) {
		return title.substring(0, title.length() - (ext.length() + 1));
	}
	public static String name_without_extension(String title) {
		return title.substring(0, title.length() - (extension(title).length() + 1));
	}	
	

	public static String filename(String fullPath) { // gets filename without extension
		int dot = fullPath.lastIndexOf(extensionSeparator);
		int sep = fullPath.lastIndexOf(File.separatorChar);
		return fullPath.substring(sep + 1, dot);
	}

	public static String path(String fullPath) {
		int sep = fullPath.lastIndexOf(File.separatorChar);
		return fullPath.substring(0, sep);
	}
	
	public static String path(String fullPath, Character separator) {
		int sep = fullPath.lastIndexOf('/');
		fullPath = fullPath.substring(0, sep);		
		try { fullPath = URLDecoder.decode(fullPath, "UTF-8");}
		catch (UnsupportedEncodingException e1) { e1.printStackTrace();}
		return fullPath;
	}	
}