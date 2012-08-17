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
package entagged.tageditor.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;

public class MultipleFieldsMergingTable {

	public static final String VARIES = "<??>";

	private Hashtable ht;

	private ArrayList orderedSelection = new ArrayList();

	public MultipleFieldsMergingTable() {
		ht = new Hashtable(10);
		ht.put("files", new LinkedList());
	}

	public void add(File f) {
		assert f != null;

		if (f.isDirectory()) {
			setAllVaries();
			addDirectory(f);
		} else if (f instanceof AudioFile) {
			Tag tag = ((AudioFile) f).getTag();

			merge("artist", tag.getFirstArtist());
			merge("album", tag.getFirstAlbum());
			merge("title", tag.getFirstTitle());
			merge("tracknumber", tag.getFirstTrack());
			merge("comment", tag.getFirstComment());
			merge("genre", tag.getFirstGenre());
			merge("year", tag.getFirstYear());

			addFile(f);
		}
	}

	private void addDirectory(File f) {
		addFile(f);
	}

	private void addFile(File f) {
		((List) ht.get("files")).add(f);
		if (!orderedSelection.contains(f))
			orderedSelection.add(f);
	}

	public void clear() {
		List l = (List) ht.get("files");
		l.clear();
		this.ht.clear();
		this.ht.put("files", l);
	}

	private String get(String key) {
		return (String) ht.get(key);
	}

	public String getAlbum() {
		return get("album");
	}

	public String getArtist() {
		return get("artist");
	}

	public List getAudioFiles() {
		return (List) ht.get("files");
	}

	public String getComment() {
		return get("comment");
	}

	public String getGenre() {
		return get("genre");
	}

	/**
	 * This method returns the selected audiofiles in the order of their
	 * selection.
	 * 
	 * @return All selected files.
	 */
	public File[] getSelectionOrderedFiles() {
		return (File[]) orderedSelection.toArray(new File[orderedSelection
				.size()]);
	}

	public String getTitle() {
		return get("title");
	}

	public String getTrackNumber() {
		String ret = get("tracknumber");

		try {
			if (Integer.parseInt(ret) == 0)
				return "";
		} catch (Exception e) {

		}

		return ret;
	}

	public String getYear() {
		return get("year");
	}

	private void merge(String key, String content) {
		String s = (String) ht.get(key);
		if (s == null)
			set(key, content);
		else if (!s.equals(content))
			set(key, VARIES);
	}

	/**
	 * This method will compare the file list in {@link #ht} with the last saved
	 * result in {@link #orderedSelection}, and will update this field.
	 */
	public void processFileDifference() {
		Iterator it = orderedSelection.iterator();
		List list = (List) ht.get("files");
		while (it.hasNext()) {
			File current = (File) it.next();
			if (!list.contains(current))
				it.remove();
		}
		it = list.iterator();
		ArrayList increment = new ArrayList();
		while (it.hasNext()) {
			File current = (File) it.next();
			if (!orderedSelection.contains(current)) {
				increment.add(current);
			}
		}
		orderedSelection.addAll(increment);
	}

	private void set(String key, String content) {
		ht.put(key, content);
	}

	public void setAlbum(String album) {
		set("album", album);
	}

	private void setAllVaries() {
		set("artist", VARIES);
		set("album", VARIES);
		set("title", VARIES);
		set("tracknumber", VARIES);
		set("comment", VARIES);
		set("genre", VARIES);
		set("year", VARIES);
	}

	public void setArtist(String artist) {
		set("artist", artist);
	}

	public void setComment(String comment) {
		set("comment", comment);
	}

	public void setGenre(String genre) {
		set("genre", genre);
	}

	public void setTitle(String title) {
		set("title", title);
	}

	public void setTrackNumber(String trackNumber) {
		set("tracknumber", trackNumber);
	}

	public void setYear(String year) {
		set("year", year);
	}

}
