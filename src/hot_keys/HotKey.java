package hot_keys;

import javax.swing.KeyStroke;

import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.MediaKey;
import com.tulskiy.keymaster.common.Provider;

public class HotKey {
	public Provider provider = Provider.getCurrentProvider(true);
	
	public void AddKey(String keys, HotKeyListener listener) {
		provider.register(KeyStroke.getKeyStroke(keys), listener);
//		"control shift PLUS"
	}
	public void AddKey(KeyStroke keys, HotKeyListener listener) {
		provider.register(keys, listener);
	}	
	public void AddMediaKey(MediaKey key, HotKeyListener listener) {
		provider.register(key, listener);
	}	
	
	public void Shutdown() {
		provider.reset();
		provider.stop();		
	}
}
