package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Zipper {
	ZipFile zipFile;
	
	public Zipper(String file) throws IOException {
		zipFile = new ZipFile(file);
	}
	
	public BufferedReader readFile(String name) throws UnsupportedEncodingException, IOException {
		ZipEntry z = zipFile.getEntry(name);
		if (z == null) throw new Error("name not find");
		return new BufferedReader(new InputStreamReader(zipFile.getInputStream(z), "UTF-8"));
	}
	
	public BufferedReader readFileToBuffer(String text) throws UnsupportedEncodingException, IOException {	     
		 return new BufferedReader(new StringReader(text));
	}	
	
//	Scanner scan = new Scanner(sb.toString()); // I have named your StringBuilder object sb
//	while (scan.hasNextLine() ){
//	 String oneLine = scan.nextLine();
//	 System.out.println(oneLine.length());
//	}
	
	public BufferedReader writeFile(String name) throws UnsupportedEncodingException, IOException {
		ZipEntry z = zipFile.getEntry(name);
		if (z == null) throw new Error("name not find");
		return new BufferedReader(new InputStreamReader(zipFile.getInputStream(z), "UTF-8"));
	}	
	
	public void close() throws IOException {
		zipFile.close();
	}
}