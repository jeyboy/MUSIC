package jb_player;

/** This class implements player events. */
public class JBPlayerEvent {
    public static final int OPENED = 1;
    public static final int PLAYING = 2;
    public static final int STOPPED = 3;
    public static final int PAUSED = 4;
    public static final int SEEKING = 5;
    public static final int SEEKED = 6;
    public static final int EOM = 7;
    public static final int PAN = 8;
    public static final int GAIN = 9;
    public static final int VOLUME = 10;
    public static final int UNKNOWN = 11;
    public static final int OPENING = 12;
    public static final int RESUMED = 13;    
    private int code = UNKNOWN;
    private int position = -1;
    private double value = -1.0;
    private Object source = null;
    private Object description = null;

    /** Constructor
     * @param source of the event
     * @param code of the envent
     * @param position optional stream position
     * @param value opitional control value
     * @param desc optional description */
    public JBPlayerEvent(Object source, int code, int position, double value, Object desc) {
        this.value = value;
        this.position = position;
        this.source = source;
        this.code = code;
        this.description = desc;
    }

    /** Return code of the event triggered. */
    public int getCode() { return code; }

    /** Return position in the stream when event occured. */
    public int getPosition() { return position; }

    /** Return value related to event triggered. */
    public double getValue() { return value; }

    /** Return description. */
    public Object getDescription() { return description; }

    public Object getSource() { return source; }

    public String toString() {
    	switch(code) {
    		case OPENED	: return "OPENED:" + position;
    		case OPENING: return "OPENING:" + position + ":" + description;
    		case PLAYING: return "PLAYING:" + position;
    		case STOPPED: return "STOPPED:" + position;
    		case PAUSED	: return "PAUSED:" + position;
    		case RESUMED: return "RESUMED:" + position;
    		case SEEKING: return "SEEKING:" + position;
    		case SEEKED	: return "SEEKED:" + position;
    		case EOM	: return "EOM:" + position;
    		case PAN	: return "PAN:" + value;
    		case GAIN	: return "GAIN:" + value;
            default		: return "UNKNOWN:" + position;    		
    	}
    }
}
