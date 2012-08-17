/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
package entagged.listing.gui.tasks;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class is intended to be used for running a given
 * {@link java.lang.Runnable}each time a event occurs. <br>
 * 
 * @author Christian Laireiter
 */
public class DocumentChangeHelper implements DocumentListener {

	/**
	 * The run() method will be appended to the awt event queue using
	 * {@link javax.swing.SwingUtilities#invokeLater(java.lang.Runnable)}
	 */
	public final static int RUN_AWT_QUEUE = 3;

	/**
	 * The run() method will be called within the event thread.
	 */
	public final static int RUN_DIRECTLY = 1;

	/**
	 * The run() method will be called using a new {@link Thread}.
	 */
	public final static int RUN_THREADED = 2;

	/**
	 * This field stores the runnable which will be executed if an event
	 * occured.
	 */
	private final Runnable method;

	/**
	 * Tells in what way {@link #method}should be performed.
	 */
	private final int runType;

	/**
	 * Creates an instance.
	 * 
	 * @param toRun
	 *                  Runnable whose run() - method should be executed on each
	 *                  event.
	 * @param runMethod
	 *                  one of the <code>RUN_</code> constants. If its not a valid
	 *                  value {@link #RUN_DIRECTLY}will be used.
	 */
	public DocumentChangeHelper(Runnable toRun, int runMethod) {
		if (toRun == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		this.method = toRun;
		this.runType = runMethod;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		perform();
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		perform();
	}

	/**
	 * Executes {@link #method}.
	 */
	private void perform() {
		switch (this.runType) {
		case RUN_THREADED:
			new Thread(this.method).start();
			break;
		case RUN_AWT_QUEUE:
			SwingUtilities.invokeLater(this.method);
			break;
		case RUN_DIRECTLY:
		// execute default.
		default:
			this.method.run();
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		perform();
	}

}