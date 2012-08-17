/*
 * Created on 9 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package miage.jtreeindex;

import java.io.File;

/**
 * @author G909248
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyFile extends File {

	/**
	 * @param arg0
	 */
	public MyFile(String arg0) {
		super(arg0);
	}

	public String toString() {
		return getName();
	}
}
