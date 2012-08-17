package service_threads;

import service.Errorist;

class Timer extends Thread { 
	private int time;
	public Timer()   
	{
		time=0;
		this.setDaemon(true);     // вспомогательный поток
		this.setPriority(Thread.NORM_PRIORITY);
	}
 
	public void run() { 
		while (true) {
			time++;
			try 
			{
 				Thread.sleep(1000);
 			} 
			catch(InterruptedException e) {
				Errorist.printLog(e);
			}
			finally {}
		}
	}
 
	public int getTime() {
		return time;
	}
}