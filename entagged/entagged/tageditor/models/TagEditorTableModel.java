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
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.tageditor.TagEditorFrame;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.listeners.NavigatorUpdateListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.ResourcesRepository;
import entagged.tageditor.tools.gui.MultiIcon;

public class TagEditorTableModel extends AbstractTableModel implements NavigatorListener, NavigatorUpdateListener {

	public static class CurrentFolder extends File {
		public CurrentFolder(File f) {
			super(f.getAbsolutePath());
		}

		public String getName() {
			return " (current .)";
		}

		public String toString() {
			return getName();
		}
	}

	public static class ParentFolder extends File {
		public ParentFolder(File f) {
			super(f.getAbsolutePath());
		}

		public String getName() {
			return " (parent ..)";
		}

		public String toString() {
			return getName();
		}
	}

	/** Columns displayed */
	private String[] colTitles = { " ",
		LangageManager.getProperty("tablemodel.filename"),
		LangageManager.getProperty("common.tag.artist"),
		LangageManager.getProperty("common.tag.album"),
		LangageManager.getProperty("common.tag.track"),
		LangageManager.getProperty("common.tag.title"),
		LangageManager.getProperty("common.tag.length"),
		LangageManager.getProperty("common.tag.genre"),
		LangageManager.getProperty("tablemodel.playlist")
	};

	/**
	 * due to automatic update we need the filenames in an extra storage,
	 * because the file instances automatically update the filename attribute
	 * upon renaming.
	 */
	private Vector fileNames;

	private Vector files;

	private Hashtable icons;

	/**
	 * This field stores the entagged instance.
	 */
	private TagEditorFrame parent;

	public TagEditorTableModel(TagEditorFrame ctrl) {
		this.parent = ctrl;
		this.files = new Vector(0);
		this.icons = new Hashtable();
		ImageIcon errorOverlay = ResourcesRepository.getImageIcon("error-icon-overlay.png");
		addFileTypeIcons("mp3", "mp3", errorOverlay);
		addFileTypeIcons("ogg", "ogg", errorOverlay);
		addFileTypeIcons("wav", "wav", errorOverlay);
		addFileTypeIcons("flac", "flac", errorOverlay);
		addFileTypeIcons("mpc", "mpc", errorOverlay);
		addFileTypeIcons("mp+", "mpc", errorOverlay);
		addFileTypeIcons("ape", "ape", errorOverlay);
		addFileTypeIcons("wma", "wma", errorOverlay);
		icons.put("directory", ResourcesRepository.getImageIcon("directory-icon.png"));
		icons.put("parent", ResourcesRepository.getImageIcon("directory-parent-icon.png"));
		icons.put("current", ResourcesRepository.getImageIcon("directory-current-icon.png"));

		parent.getNavigator().addNavigatorListener(this);
		parent.getNavigator().addNavigatorUpdateListener(this);
	}

	/**
	 * This method does a very simple job.<br>
	 * Load icon "&lt;fileType&gt;-icon.png" and put it into {@link #icons} as
	 * "&lt;fileType&gt;", then create the errornous paret with key
	 * "&lt;fileType&gt;-error".<br>
	 * 
	 * @param iconKey
	 *            The prefix for the keys (Hashtable).
	 * @param fileType
	 *            The file type which will be handled.
	 */
	public void addFileTypeIcons(String iconKey, String fileType, ImageIcon errorIcon) {
		ImageIcon defaultIcon = ResourcesRepository.getImageIcon(fileType + "-icon.png");
		MultiIcon multi = new MultiIcon();
		multi.addIcon(defaultIcon, MultiIcon.CENTER);
		multi.addIcon(errorIcon, MultiIcon.CENTER);
		icons.put(iconKey, defaultIcon);
		icons.put(iconKey + "-error", multi);
	}
	
	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.listeners.NavigatorListener#directoryChanged(java.io.File,
	 *      java.io.File[], int)
	 */
	public void directoryChanged(File newDirectory, File[] contents, int how) {
		synchronized (files) {
			this.files = new Vector(contents.length + 2);
			this.fileNames = new Vector(contents.length + 2);
			
			files.add(new CurrentFolder(newDirectory));
			fileNames.add(new CurrentFolder(newDirectory).getAbsolutePath());
			if (newDirectory.getParentFile() != null) {
				files.add(new ParentFolder(newDirectory.getParentFile()));
				fileNames.add(new ParentFolder(newDirectory.getParentFile()).getAbsolutePath());
			}
			for (int i = 0; i < contents.length; i++) {
				files.add(contents[i]);
				fileNames.add(contents[i].getAbsolutePath());
			}
		}
		fireTableDataChanged();
	}

	// returns null if not an audiofile !
	public AudioFile getAudioFileAt(int row) {
		File f = getFileAt(row);
		if (f.isDirectory() || !(f instanceof AudioFile))
			return null;
		else
			return (AudioFile) f;
	}

	public Class getColumnClass(int column) {
		if (column == 0)
			return ImageIcon.class;
		else if(column == getColumnCount() - 1)
			return Boolean.class;
		else
			return String.class;
	}

	public int getColumnCount() {
		return colTitles.length;
	}

	public String getColumnName(int column) {
		return colTitles[column];
	}

	private String getExtension(File f) {
		String name = f.getName().toLowerCase();
		int i = name.lastIndexOf(".");
		if (i == -1)
			return "";

		return name.substring(i + 1);
	}

	public File getFileAt(int row) {
		synchronized (files) {
			File f = (File)files.elementAt(row);
			if (!f.isDirectory() && !(f instanceof AudioFile)) {
				try {
					AudioFile audioFile = AudioFileIO.read(f);
					files.set(row, audioFile);
					f = audioFile;
				}
				catch (Exception e) {} // Do Nothing
			}
			return f;
		}
	}

	public Vector getFiles() {
		return files;
	}

	private String getLength(int length) {
		if (length != -1) {
			int minutes = length / 60;
			int sec = (length - minutes * 60);
			DecimalFormat fmt = new DecimalFormat("00");

			return fmt.format(minutes) + ":" + fmt.format(sec);
		}
		else
			return "timeformat error";
	}

	public int getRowCount() {
		if (files == null)
			return 0;
		return files.size();
	}

	public Object getValueAt(int row, int column) {
		File f = getFileAt(row);

		if (column == 0) {
			if (f instanceof ParentFolder)
				return this.icons.get("parent");
			else if (f instanceof CurrentFolder)
				return this.icons.get("current");
			else if (f.isDirectory())
				return this.icons.get("directory");
			else if (f instanceof AudioFile)
				return this.icons.get(getExtension(f));
			else
				return this.icons.get(getExtension(f) + "-error");
		}
		else if (column == 1)
			return f.getName();
		else if(column == getColumnCount() - 1) {
			if(TagEditorFrame.getCheckList().contains(f.getAbsolutePath()))
				return true;
			else
				return false;
		}
		else if (f.isDirectory() || !(f instanceof AudioFile))
			return "";
		else {
			AudioFile af = (AudioFile) f;
			Tag tag = af.getTag();

			switch (column) {
			case 2:
				return tag.getFirstArtist();
			case 3:
				return tag.getFirstAlbum();
			case 4:
				String ret = tag.getFirstTrack();
				try {
					if (Integer.parseInt(ret) == 0)
						return "";
				}
				catch (Exception e) {} /* return normally */
				return ret;
			case 5:
				return tag.getFirstTitle();
			case 6:
				return getLength(af.getLength());
			case 7:
				return tag.getFirstGenre();
			default:
				return "";
			}
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.listeners.NavigatorUpdateListener#update(java.util.Collection,
	 *      java.util.Collection, java.util.Collection)
	 */
	public void update(Collection newFiles, Collection modifiedFiles, Collection removedFiles) {
		Vector oldFiles = new Vector(files);
		Vector oldNames = new Vector(fileNames);
		directoryChanged(parent.getNavigator().getCurrentFolder(), parent.getNavigator().currentContent, NavigatorListener.EVENT_RELOAD);
		for (int i = 0; i < oldNames.size(); i++) {
			String curr = (String) oldNames.get(i);
			int index = fileNames.indexOf(curr);
			if (index >= 0 && !modifiedFiles.contains(oldFiles.get(i)) && oldFiles.get(i) instanceof AudioFile) {
				files.remove(index);
				files.add(index, oldFiles.get(i));
			}
		}
	}
	
	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.listeners.NavigatorUpdateListener#update(java.util.Collection,
	 *      java.util.Collection, java.util.Collection)
	 */
	@Override public boolean isCellEditable(int rowIndex, int columnIndex) {
		File f = getFileAt(rowIndex);
		if(columnIndex == getColumnCount() - 1 && f.isFile())
			return true;
		else
			return false;
	}
}