package hot_keys;

import service.Common;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import components.MainWnd;

public class HotKeyManager {
	hot_keys.HotKey hotkey;
	
	public HotKeyManager() {
		hotkey = new hot_keys.HotKey();
		initHotKeys();
	}
	
	void initHotKeys() {
		hotkey.AddKey("control DOWN", new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				Common.tabber.MoveSelectAndInit(true);
			}
		});
		
		hotkey.AddKey("control UP", new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				Common.tabber.MoveSelectAndInit(false);
			}
		});
		
		hotkey.AddKey("control DELETE", new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				Common.tabber.DeleteSelectAndInit();
			}
		});
		
		hotkey.AddKey("control HOME", new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				MainWnd.Toggle();
			}
		});		
	}
	
	public void Shutdown() { hotkey.Shutdown(); } 
}
