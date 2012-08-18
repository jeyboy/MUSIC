package service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Errorist {
	static PrintWriter err_writer = null;
	
//	this.getClass().getName()+"."+ new Exception().getStackTrace()[0].getMethodName();
	public static void printLog(Exception e) {
		printLog(e.getStackTrace()[0].getClassName() + "." + e.getStackTrace()[0].getMethodName(), e.getMessage());
	}	
	
	public static void printLog(String className, String funcName, String error) {
		printLog(className + "." + funcName, error);
	}
	
	public static void printLog(String objName, String error) {
		if (err_writer == null) {
			try { err_writer = IOOperations.GetWriter(Settings.logpath, true);	} 
			catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
		}
		
		if (err_writer != null)
			err_writer.println(new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]").format(new Date()) + " " + objName + "   **   " + error);
		System.err.println(new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]").format(new Date()) + " " + objName + "   **   " + error);
	}
	
	public static void close() {
		if (err_writer != null) err_writer.close();
	}
}
