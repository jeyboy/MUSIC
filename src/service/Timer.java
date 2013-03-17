package service;

public class Timer {
	static long st;
	
	public static void start() { st = System.currentTimeMillis(); }
	
	public static long stop() { return System.currentTimeMillis() - st;}
}