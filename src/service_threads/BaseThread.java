package service_threads;

import service.Errorist;

public class BaseThread extends Thread {
	int close_flag = 0;
	int sleep_time = 1000;
	
	public boolean closeRequest() { return close_flag == 1; }
	
    public void close() {
    	Thread th = currentThread();
    	if (th.isAlive())
    		close_flag = 1;
    }
    
    void sleepy() { sleepy(sleep_time); }
    void sleepy(int time) {
	    try { wait(time); }
	    catch (InterruptedException e) { Errorist.printLog(e); }
    }
}

//Thread example
//PrimeThread p = new PrimeThread(143);
//p.start();		

//Runnable example
//PrimeRun p = new PrimeRun(143);
//new Thread(p).start();	
