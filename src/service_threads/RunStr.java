package service_threads;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import components.MainWnd;

public class RunStr extends BaseThread {
	String buffer, spacer;
	boolean needRotation = false;
	int wait = 0, dot_length;
	FontMetrics fm;
	HashMap<Character, String> simbols = new HashMap<Character, String>();

    public RunStr() {
    	sleep_time = 45;
    	this.setDaemon(true);
    	
    	BufferedImage image=new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    	fm = image.getGraphics().getFontMetrics();
    	dot_length = fm.charWidth('.');
    	
//    	start();
    }

    synchronized public void run() { routing(); }
    
    void addCharToList(char c) {
		String temp = "";
		int lim = (int) Math.ceil(fm.charWidth(c)/(float)dot_length) - 1;
		
		for(int loop2 = 0; loop2 < lim; loop2++)
			temp += ".";
		
		simbols.put(c, temp);
    }
    
    public void delay(int delay_timeout) { wait = delay_timeout; }
    public void stopRoutind() { needRotation = false; }
    public void update() {
    	simbols.clear();
    	String title = MainWnd.getTitle();
    	needRotation = true;
    	buffer = title + " ::.                .:: " + title;
    	spacer = ":.                .:: " + title + " ::.                .:: " + title;
    	
    	for (int loop1= 0; loop1 < title.length(); loop1++)
    		addCharToList(title.charAt(loop1));

    	simbols.put('.', "");
    	simbols.put(':', "");
    	addCharToList(' ');
    }
    
    void routing() {
    	while(!closeRequest()) {
    		if (wait > 0) {
    			sleepy(wait);
    			wait = 0;
    		}
    		else sleepy();
    		
	        if (needRotation) {        	
	        	if (buffer.substring(0, 2).equals(":."))
	        		buffer = spacer;
	        	
	        	buffer = simbols.get(buffer.charAt(0)) + buffer.substring(1);
	        	MainWnd.setTitle(buffer);
	        }
    	}
    }
}
