package service_threads;

//import java.util.Timer;
//import java.util.TimerTask;

public class BaseThread extends Thread {
	int close_flag = 0;
	int sleep_time = 1000;
	
	public boolean closeRequest() { return close_flag == 1; }
	
    public void close() {
    	Thread th = currentThread();
    	if (th.isAlive()) {
    		close_flag = 1;
//    		new Timer(true).schedule(new TimerTask() {
//    		      public void run() {
//    		    	  
////    		        stoppable.requestStop();
//    		      }
//    		}, 500);    		
    	}
    }
}

//Thread example
//PrimeThread p = new PrimeThread(143);
//p.start();		

//Runnable example
//PrimeRun p = new PrimeRun(143);
//new Thread(p).start();	
