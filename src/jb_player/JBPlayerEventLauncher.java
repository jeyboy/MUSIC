package jb_player;

import java.util.Collection;

/** This class implements a threaded events launcher. */
public class JBPlayerEventLauncher extends Thread {
    private int code = -1;
    private int position = -1;
    private double value = 0.0;
    private Object description = null;
    private Collection<JBPlayerListener> listeners = null;
    private Object source = null;

    /** Contructor. */
    public JBPlayerEventLauncher(int code, int position, double value, Object description, Collection<JBPlayerListener> listeners, Object source) {
        super();
        this.code = code;
        this.position = position;
        this.value = value;
        this.description = description;
        this.listeners = listeners;
        this.source = source;
    }

    public void run() {
        if (listeners != null) {
        	JBPlayerEvent event = new JBPlayerEvent(source, code, position, value, description);
        	for(JBPlayerListener bpl : listeners)
                bpl.stateUpdated(event);        		
        }
    }
}
