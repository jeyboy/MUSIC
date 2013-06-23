package tabber;

public class TabOptions {
	public boolean delete_files = false;
	public boolean interactive = false;
	public boolean play_next = false;
	public boolean remote_source = false;
	
	public TabOptions(boolean _delete_files, boolean _interactive, boolean _play_next, boolean _remote_source) {
		delete_files = _delete_files;
		interactive = _interactive;
		play_next = _play_next;
		remote_source = _remote_source;
	}
	public TabOptions(char params) { deserialize(params); }
	public TabOptions() {}
	
	public String toString() {
		return (remote_source ? "R" : "") +
			(interactive ? "I" : "") +
			(delete_files ? "D" : "") +
			(play_next ? "P" : "");		
	} 
	
	public String serialize() {
		byte ret = (byte)128;
		ret |= bc(delete_files) << 0;
		ret |= bc(interactive) << 1;
		ret |= bc(play_next) << 2;
		ret |= bc(remote_source) << 3;

		return ((char)ret) + "";
	}
	
	void deserialize(char params) {
		byte temp = (byte)params;
		delete_files = cb(temp & 1);
		interactive = cb(temp >> 1 & 1);
		play_next = cb(temp >> 2 & 1);
		remote_source = cb(temp >> 3 & 1);		
	}
	
	int bc(boolean v) {	return v ? 1 : 0; }
	boolean cb(int v) {	return v == 1; }
}