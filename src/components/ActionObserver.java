package components;

public interface ActionObserver {
	public static int HIDDEN = -1;
	public static int STOP = 0;
	public static int PLAY = 1;
	public static int PAUSE = 2;
	
	void notify(int state);
}
