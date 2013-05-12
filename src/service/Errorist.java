package service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Errorist {
	static PrintWriter err_writer = null;
	static boolean print_local = true;
	
	static void checkWriter() {
		if (err_writer == null) {
			try { err_writer = IOOperations.getWriter(Constants.logpath, true, false);	} 
			catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
		}
	}
	
	public static void printMessage(String object, String message) {
		checkWriter();

		if (err_writer != null) {
			err_writer.println("***" + object + "*** " + message);
			err_writer.flush();
		}
	}
	
	public static void printLog(Exception e) {
		printLog(e.getStackTrace()[0].getClassName() + "." + e.getStackTrace()[0].getMethodName(), e.getMessage());
	}	
	
	public static void printLog(String className, String funcName, String error) {
		printLog(className + "." + funcName, error);
	}
	
	public static void printLog(String objName, String error) {
		checkWriter();

		if (err_writer != null) {
			ProceedTree(new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]").format(new Date()), objName + "   **   " + error);
			err_writer.flush();
		}
	}
	
	static void PrintDate(String date) {
		err_writer.println(date);
		if (print_local)
			System.err.println(date);
	}
	
	static void PrintTreeItem(String data) {
		err_writer.println(data);
		if (print_local)
			System.err.println(data);
	}	
	
	static void ProceedTree(String date, String message) {
		PrintDate(date);
		
		StackTraceElement [] elems = Thread.currentThread().getStackTrace();
		String curr_class = elems[1].getClassName(), indent = "";
		
		for(int loop1 = 2; loop1 < elems.length; loop1++) {
			StackTraceElement curr = elems[loop1];
			if (curr.getClassName() != curr_class)
				PrintTreeItem((indent += "  ") + curr);
		}
		
		PrintTreeItem((indent += "  ") + message);
	}
	
	public static void close() {
		if (err_writer != null) err_writer.close();
	}
}
