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
package entagged.tageditor.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import miage.sgbd.SqlProvider;

import entagged.audioformats.AudioFileFilter;
import entagged.tageditor.TagEditorFrame;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.listeners.NavigatorUpdateListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;

/**
 * This class is meant for navigating through the file system. <br>
 * This class will store a history for navigating backward and not just upwards
 * to the parent. <br>
 * On each navigation request, like go backward, the destination will be check
 * if it is still accessible. If not there will be a trial to go up the parents
 * until one is accessible. <br>
 * 
 * @author Christian Laireiter
 */
public class Navigator {

	/**
	 * This class is meant to recognize changes in the current folder and notify
	 * about them.<br>
	 * 
	 * @author Christian Laireiter
	 */
	public final class UpdateWatcher implements Runnable, NavigatorListener {

		/**
		 * If the PreferencesManager has no information about an update
		 * interval, this value is used.<br>
		 */
		public final static int DEFAULT_UPDATE_INTERVAL = 1000;

		/**
		 * If <code>true</code>, the next update will be skpped.<br>
		 * This is used for Directory changes.
		 */
		protected boolean changed = false;

		/**
		 * Stores the absolute file paths instances to their modification time.<br>
		 */
		protected HashMap lastModifiedMap = new HashMap();

		/**
		 * As long as this is <code>true</code>, the Runnable will continue.
		 */
		protected boolean running = true;

		/**
		 * (overridden)
		 * 
		 * @see entagged.tageditor.listeners.NavigatorListener#directoryChanged(java.io.File,
		 *      java.io.File[], int)
		 */
		public void directoryChanged(File newDirectory, File[] contents, int how) {
			changed = true;
			lastModifiedMap.clear();
			for (int i = 0; i < contents.length; i++) {
				lastModifiedMap.put(contents[i].getAbsolutePath(), new Long(
						contents[i].lastModified()));
			}
		}

		/**
		 * Returns the Updateinterval from the settings ({@link PreferencesManager}).<br>
		 * 
		 * @return the Update intervall. If Nothing is set,
		 *         {@link #DEFAULT_UPDATE_INTERVAL}.
		 */
		private int getUpdateInterval() {
			int interval = PreferencesManager
					.getInt("tageditor.navigator.updateinterval");
			if (interval < 0) {
				interval = DEFAULT_UPDATE_INTERVAL;
			}
			return interval;
		}

		/**
		 * (overridden)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			directoryChanged(getCurrentFolder(), getFiles(),
					NavigatorListener.EVENT_RELOAD);
			while (running) {
				try {
					Thread.sleep(getUpdateInterval());
					if (changed) {
						changed = false;
					} else {
						File[] actualListing = loadFiles();
						Hashtable actualTable = new Hashtable();
						for (int i = 0; i < actualListing.length; i++) {
							actualTable.put(actualListing[i].getAbsolutePath(),
									actualListing[i]);
						}
						Hashtable currContentTable = new Hashtable();
						for (int i = 0; i < currentContent.length; i++) {
							currContentTable.put(currentContent[i]
									.getAbsolutePath(), currentContent[i]);
						}
						ArrayList removed = new ArrayList();
						ArrayList modified = new ArrayList();
						ArrayList newFiles = new ArrayList();
						synchronized (currentContent) {
							Iterator currCIt = lastModifiedMap.keySet()
									.iterator();
							while (currCIt.hasNext()) {
								String toCheck = (String) currCIt.next();
								File actual = (File) actualTable
										.remove(toCheck);
								if (actual == null) {
									currContentTable.remove(toCheck);
									removed.add(toCheck);
									currCIt.remove();
								} else {
									Long lastMod = (Long) lastModifiedMap
											.get(toCheck);
									if (lastMod == null) {
										lastModifiedMap.put(toCheck, new Long(
												actual.lastModified()));
									} else {
										if (lastMod.longValue() != actual
												.lastModified()) {
											modified.add(actual);
											lastModifiedMap.put(actual
													.getAbsolutePath(),
													new Long(actual
															.lastModified()));
										}
									}
								}
							}
							newFiles.addAll(actualTable.values());
							currCIt = actualTable.values().iterator();
							while (currCIt.hasNext()) {
								File curr = (File) currCIt.next();
								lastModifiedMap.put(curr.getAbsolutePath(),
										new Long(curr.lastModified()));
							}
							currentContent = (File[]) currContentTable.values()
									.toArray(new File[currContentTable.size()]);
							Arrays.sort(currentContent, FILE_COMPARATOR);
						}
						if (removed.size() + modified.size() + newFiles.size() > 0) {
							synchronized (updateListeners) {
								Iterator it = updateListeners.iterator();
								while (it.hasNext()) {
									((NavigatorUpdateListener) it.next())
											.update(newFiles, modified, removed);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	protected final static Comparator FILE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			File f1 = (File) o1;
			File f2 = (File) o2;
			int cmpResult = 0;
			if (f1.isDirectory() == f2.isDirectory()) {
				cmpResult = f1.getName().compareToIgnoreCase(f2.getName());
			} else {
				cmpResult = f1.isDirectory() ? -1 : 1;
			}
			return cmpResult;
		}
	};

	/**
	 * This is used to reduce the directory content to files with registered
	 * extension. <br>
	 */
	private final static AudioFileFilter fileFilter = new AudioFileFilter();

	/**
	 * This field stores the files of the current folder. <br>
	 */
	protected File[] currentContent;

	/**
	 * This field stores the visited history. <br>
	 */
	private ArrayList history;

	/**
	 * This field holds all registered {@link NavigatorListener}.<br>
	 * 
	 * @see #addNavigatorListener(NavigatorListener)
	 */
	private Vector listeners;

	/**
	 * If this field is set to true, no folder change will be performed. <br>
	 * This functionality was introduced, due to table editing. If backspace is
	 * hit a browse was performed. However in this state browsing should not be
	 * allowed and may be blocked.
	 */
	private boolean locked = false;

	/**
	 * This field stores registered
	 * {@link entagged.tageditor.listeners.NavigatorUpdateListener} objects.
	 */
	protected Vector updateListeners;

	/**
	 * Stores the update Watcher.
	 */
	private UpdateWatcher watcher;

	/**
	 * Creates an instance.
	 * 
	 */
	public Navigator() {
		this.listeners = new Vector();
		this.updateListeners = new Vector();
		this.history = new ArrayList();
		this.currentContent = new File[0];
		restoreSettings();
	}

	/**
	 * This method registers the given listener.
	 * 
	 * @param listener
	 *            Listener which recieves changes.
	 */
	public void addNavigatorListener(NavigatorListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	/**
	 * Adds a {@link NavigatorUpdateListener}.
	 * 
	 * @param listener
	 *            Listener to recieve content updates.
	 */
	public void addNavigatorUpdateListener(NavigatorUpdateListener listener) {
		if (PreferencesManager.getInt("tageditor.navigator.updateinterval") < 0)
			return;
		synchronized (updateListeners) {
			if (!updateListeners.contains(listener)) {
				updateListeners.add(listener);
				if (this.watcher == null) {
					new Thread(this.watcher = new UpdateWatcher(),
							"UpdateWatcher").start();
					addNavigatorListener(watcher);
				}
			}
		}
	}

	/**
	 * This method causes the navigator to browse back to the previous directory
	 * the program visited. <br>
	 * If this previous directory doesn't exist any more It will be tried to
	 * browse upwards the parents to a readable one.
	 * 
	 * @return The list of the files in the previous directory.
	 *         <code>null</code> if the history was empty.
	 */
	public File[] browseBackward() {
		if (history.size() > 1 && !isLocked()) {
			history.remove(history.size() - 1); // remove
			// current
			File destination = (File) history.get(history.size() - 1);
			if (destination.exists() && destination.canRead()) {
				return setDirectory(destination,
						NavigatorListener.EVENT_BACKWARD);
			}
		}
		return null;
	}

	/**
	 * This method will compare the current folder with the given target and
	 * perform following: <br>
	 * <ul>
	 * <li>If folders are equal {@link #reload()}is called.</li>
	 * <li>If destination is the parent folder {@link #browseToParent()}is
	 * called.</li>
	 * <li>Else {@link #setDirectory(File)}is called.</li>
	 * </ul>
	 * <br>
	 * 
	 * @param dest
	 *            Directory which should be opened.
	 */
	public void browseInto(final File dest) {
		if (dest != null && dest.isDirectory() && dest.canRead()) {
			if (dest.equals(getCurrentFolder()))
				reload();
			else if (dest.equals(getCurrentFolder().getParentFile()))
				browseToParent();
			else
				setDirectory(dest);
			
			
			//////////////////
			//Modif MIAGE 08//
			//////////////////
			
			//Put the folder save at null to manage the index color on the tag editor
			SqlProvider.DossierAct=null;
			
			//Update the percentage index of the folder.
			Thread performer = new Thread(new Runnable() {
				public void run() {
					TagEditorFrame.updatePourcentage(LangageManager.getProperty("miage.updateactinprog"),1);
					TagEditorFrame.updatePourcentage(dest.getAbsolutePath(),0);
				}
			}, "Performer");
			performer.start();			
		}
	}

	/**
	 * This method changes the current location to the parent of the currrent
	 * directory.
	 */
	public void browseToParent() {
		File dest = getCurrentFolder().getParentFile();
		if (dest != null && dest.isDirectory() && dest.canRead()) {
			setDirectory(dest, NavigatorListener.EVENT_PARENT);
		}
	}

	/**
	 * This method searches the file roots for a matching start.
	 * 
	 * @return A File instance from where entagged can start.
	 */
	private File findAlternative() {
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i++) {
			File current = roots[i];
			if (current != null && current.exists() && current.canRead()
					&& current.isDirectory()) {
				return current;
			}
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(null);
		if (result == JOptionPane.OK_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * Notifies all registered {@link NavigatorListener}about the directory
	 * changes.
	 * 
	 * @param type
	 *            The type of the change. <br>
	 * @see NavigatorListener#EVENT_BACKWARD
	 * @see NavigatorListener#EVENT_JUMPED
	 */
	public void fireDirectoryChange(int type) {
		synchronized (listeners) {
			Iterator it = listeners.iterator();
			while (it.hasNext()) {
				NavigatorListener current = (NavigatorListener) it.next();
				current.directoryChanged(getCurrentFolder(), getFiles(), type);
			}
		}
	}

	/**
	 * This method returns the current directory where entagged is looking at.
	 * <br>
	 * If somehow nothing is registered whithin the history, the filesystem root
	 * will be returned.
	 * 
	 * @return The directory where entagged is at present.
	 */
	public File getCurrentFolder() {
		File result = null;
		if (!history.isEmpty()) {
			result = (File) history.get(history.size() - 1);
		} else {
			File[] roots = File.listRoots();
			if (roots != null && roots.length > 0) {
				result = roots[0];
			} else {
				throw new IllegalStateException("No Drive is accessible");
			}
			history.add(result);
		}
		return result;
	}

	/**
	 * This method returns the files of the currently selected folder. <br>
	 * Warning no reload is done. These files were recognized by the last
	 * performed {@link #setDirectory(File)}of {@link #setDirectory(File, int)}.
	 * 
	 * @return All interesting files in the current folder.
	 */
	public File[] getFiles() {
		synchronized (this.currentContent) {
			return this.currentContent;
		}
	}

	/**
	 * This method returns the lock state. <br>
	 * 
	 * @return State of the lock. If <code>true</code> no directory change
	 *         will be performed.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * This method loads the files of the current folder, and sotrs them. <br>
	 * 
	 * @return All interesting files and directories.
	 */
	protected File[] loadFiles() {
		synchronized (this.currentContent) {
			this.currentContent = getCurrentFolder().listFiles(fileFilter);
			Arrays.sort(currentContent, FILE_COMPARATOR);
			return currentContent;
		}
	}

	/**
	 * This method will reload the contents of the current directory and notify
	 * all listeners.
	 * 
	 */
	public void reload() {
		this.setDirectory(getCurrentFolder(), NavigatorListener.EVENT_RELOAD);
	}
	

	/**
	 * This method removes the given and registered listener.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	public void removeNavigatorListener(NavigatorListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	/**
	 * This method removes the given listener.
	 * 
	 * @param listener
	 *            listener to remove.
	 */
	public void removeNavigatorUpdateListener(NavigatorUpdateListener listener) {
		synchronized (updateListeners) {
			updateListeners.remove(listener);
			if (updateListeners.isEmpty()) {
				this.watcher.running = false;
				this.watcher = null;
			}
		}
	}

	/**
	 * This method will for example read the last known position of entagged.
	 */
	public void restoreSettings() {
		String workDir = PreferencesManager
				.get("tageditor.tageditorframe.workingdir");
		if (workDir != null) {
			File file = new File(workDir);
			if (file.exists() && file.isDirectory() && file.canRead()) {
				setDirectory(file, NavigatorListener.EVENT_JUMPED);
				return;
			}
		}
		setDirectory(findAlternative(), NavigatorListener.EVENT_JUMPED);
	}

	/**
	 * This method loads the given directory and adds the previous one to the
	 * history. <br>
	 * 
	 * @param directory
	 *            The directory whose content should be displayed.
	 * @return All interesting files within the directory. <code>null</code>
	 *         if the directory couldn't be read.
	 */
	public File[] setDirectory(File directory) {
		assert directory != null && directory.isDirectory() : "Argument must be a directory";
		return setDirectory(directory, NavigatorListener.EVENT_JUMPED);
	}

	/**
	 * This method will set the current directory.
	 * 
	 * @param directory
	 *            new directory.
	 * @param type
	 *            how the change occured.
	 * @return the contents.
	 */
	protected File[] setDirectory(File directory, int type) {
		if (!isLocked()) {
			while (directory != null && !directory.exists())
				directory = directory.getParentFile();
			
			if (directory == null)
				directory = findAlternative();
			
			if (directory != null && directory.canRead()) {
				if (!getCurrentFolder().equals(directory))
					history.add(directory);
				loadFiles();
				fireDirectoryChange(type);
			}
		}
		return getFiles();
	}

	/**
	 * Enables / disables the lock. <br>
	 * If set no directory change will be done even if the appropriate methods
	 * are invoked. <br>
	 * 
	 * @see #isLocked()
	 * 
	 * @param lock
	 *            <code>true</code> to enable the lock.
	 */
	public void setLocked(boolean lock) {
		this.locked = lock;
	}

	/**
	 * Like {@link #restoreSettings()}this method saves them.
	 * 
	 */
	public void storeSettings() {
		PreferencesManager.put("tageditor.tageditorframe.workingdir",
				getCurrentFolder().getAbsolutePath());
	}
}