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
package entagged.tageditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.mp3.Id3v2Tag;
import entagged.freedb.Freedb;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;
import entagged.freedb.FreedbReadResult;
import entagged.freedb.FreedbSettings;
import entagged.tageditor.exceptions.OperationException;
import entagged.tageditor.listeners.FreedbPanelButtonListener;
import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.util.MultipleFieldsMergingTable;
import entagged.tageditor.util.swing.SimpleFocusPolicy;

public class FreedbPanel extends JPanel {

	private class FreedbReadResultWrapper {
		private boolean best;

		private FreedbReadResult r;

		public FreedbReadResultWrapper(FreedbReadResult r, boolean best) {
			this.r = r;
			this.best = best;
		}

		public String toString() {
			return (best ? "+ " : "") + r.getArtist() + "-" + r.getAlbum();
		}
	}

	private class QueryButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final ProgressDialog progressDialog = new ProgressDialog(
					tagEditorFrame, LangageManager
							.getProperty("freedb.progress.title"),
					LangageManager.getProperty("freedb.progress.prepare"));
			progressDialog.setAbortable(true);
			progressDialog.setModal(true);
			new Thread(new Runnable() {
				public void run() {
					performSearch(progressDialog);
				}
			}, "FreeDB search").start();
			progressDialog.setVisible(true);
			progressDialog.dispose();
		}
	}

	private class ResultItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (FreedbPanel.this.results.getSelectedIndex() != -1) {
				FreedbReadResult fdbrr = FreedbPanel.this.freedbResults[FreedbPanel.this.results
						.getSelectedIndex()];
				System.out.println(fdbrr);

				FreedbPanel.this.artist.setText(fdbrr.getArtist().trim());
				FreedbPanel.this.artist.setEnabled(true);
				FreedbPanel.this.album.setText(fdbrr.getAlbum().trim());
				FreedbPanel.this.album.setEnabled(true);
				FreedbPanel.this.year.setText(fdbrr.getYear().trim());
				FreedbPanel.this.year.setEnabled(true);
				FreedbPanel.this.genre.getTextField().setText(
						fdbrr.getGenre().trim());
				FreedbPanel.this.genre.setEnabled(true);
				FreedbPanel.this.comment
						.setText(fdbrr.getAlbumComment().trim());
				FreedbPanel.this.comment.setEnabled(true);

				FreedbPanel.this.tracks.setText("");
				int nb = fdbrr.getTracksNumber();
				for (int i = 0; i < nb; i++)
					FreedbPanel.this.tracks.append(fdbrr.getTrackTitle(i)
							+ "\n");
			}
		}
	}

	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			FreedbReadResult fdbrr = FreedbPanel.this.freedbResults[FreedbPanel.this.results
					.getSelectedIndex()];
			tagEditorFrame.getEditorSettings().prepareAudioProcessing();
			boolean warning = false;
			String warningMessage = LangageManager
					.getProperty("freedbpanel.errors")
					+ "\n";

			Iterator it = new ArrayList(FreedbPanel.this.audioFiles
					.getAudioFiles()).iterator();
			int i = 0;
			while (it.hasNext()) {
				AudioFile f = (AudioFile) it.next();

				Tag tag = f.getTag();
				tag.setTitle(fdbrr.getTrackTitle(i).trim());
				tag.setAlbum(FreedbPanel.this.album.getText());
				tag.setGenre(FreedbPanel.this.genre.getTextField().getText());
				tag.setArtist(FreedbPanel.this.artist.getText());
				tag.setYear(FreedbPanel.this.year.getText());
				tag.setComment(FreedbPanel.this.comment.getText());
				tag.setTrack(new DecimalFormat("00").format(i + 1));

				if (tag instanceof Id3v2Tag)
					tag.setEncoding(PreferencesManager
							.get("entagged.tag.encoding"));

				try {
					AudioFileIO.write(f);
				} catch (CannotWriteException e) {
					warning = true;
					warningMessage += e.getMessage() + "\n";
				}
				i++;
			}

			FreedbPanel.this.tagEditorFrame.refreshCurrentTableView();

			if (warning)
				new WarningDialog(FreedbPanel.this.tagEditorFrame,
						warningMessage);
		}
	}

	private class SaveRecursiveButtonListener extends FreedbPanelButtonListener {

		public SaveRecursiveButtonListener(TagEditorFrame tagEditorFrame,
				MultipleFieldsMergingTable audioFiles) {
			super(tagEditorFrame, audioFiles);
		}

		protected void audioFileAction(AudioFile af) {
			// Nothing to do
		}

		protected void directoryAction(java.util.List files)
				throws OperationException {
			if (files.size() <= 2)
				throw new OperationException(
						"Not enough files to create a good FreedbID, skipping");

			// Sort the files in this album
			Collections.sort(files, new Comparator() {
				public int compare(Object a, Object b) {
					File fa = (File) a;
					File fb = (File) b;
					return fa.getName().compareToIgnoreCase(fb.getName());
				}
			});

			// Creates the album to be used by freedb
			float[] f = new float[files.size()];
			Iterator it = files.iterator();
			int i = 0;
			while (it.hasNext()) {
				f[i] = ((AudioFile) it.next()).getPreciseLength();
				i++;
			}

			// Connect to freedb
			FreedbReadResult[] fdbresults = null;
			try {
				FreedbQueryResult[] fdbqr = freedb.query(f);

				System.out.println(LangageManager.getProperty(
						"freedb.searchsuccessful").replaceAll("%1",
						new Integer(fdbqr.length).toString()));
				System.out.println("\t"
						+ LangageManager.getProperty("freedb.readingresults"));

				int maxResults = (fdbqr.length > MAX_RESULTS) ? MAX_RESULTS
						: fdbqr.length;
				fdbresults = new FreedbReadResult[maxResults];

				for (int h = 0; h < maxResults; h++) {
					fdbresults[h] = freedb.read(fdbqr[h]);
					System.out
							.println("\t"
									+ LangageManager.getProperty(
											"freedb.resultof").replaceAll("%1",
											new Integer(h + 1).toString())
											.replaceAll(
													"%2",
													new Integer(fdbqr.length)
															.toString())
											.replaceAll(
													"%3",
													new Integer((h + 1) * 100
															/ fdbqr.length)
															.toString()));
				}
			} catch (FreedbException ex) {
				System.out.println(LangageManager
						.getProperty("freedb.noresult"));
				throw new OperationException(ex.getMessage());
			}

			// Write the tag-------------
			// Select the best result, rely on the comparable interface for
			// readresults
			Arrays.sort(fdbresults);
			FreedbReadResult fdbrr = fdbresults[fdbresults.length - 1];

			it = files.iterator();
			i = 0;
			while (it.hasNext()) {
				AudioFile af = (AudioFile) it.next();

				Tag tag = af.getTag();
				tag.setTitle(fdbrr.getTrackTitle(i).trim());
				tag.setAlbum(fdbrr.getAlbum().trim());
				tag.setGenre(fdbrr.getGenre().trim());
				tag.setArtist(fdbrr.getArtist().trim());
				tag.setYear(fdbrr.getYear().trim());
				tag.setComment(fdbrr.getAlbumComment().trim());
				tag.setTrack(new DecimalFormat("00").format(i + 1));

				if (tag instanceof Id3v2Tag)
					tag.setEncoding(PreferencesManager
							.get("entagged.tag.encoding"));

				try {
					AudioFileIO.write(af);
				} catch (CannotWriteException e) {
					throw new OperationException(
							"Album skipped after this file: " + e.getMessage());
				}
				i++;
			}
		}

		protected void finalizeAction() {
			FreedbPanel.this.tagEditorFrame.refreshCurrentTableView();
		}

		protected void prepareAction() {
			FreedbPanel.this.tagEditorFrame.getEditorSettings()
					.prepareAudioProcessing();
		}
	}

	protected JTextField artist, album, year, comment, maxResults;

	protected MultipleFieldsMergingTable audioFiles;

	protected Freedb freedb;

	protected FreedbReadResult[] freedbResults;

	protected AutoCompleteComboBox genre;

	private int MAX_RESULTS = 5;

	protected JComboBox results;

	private JButton save, saveRecursive;

	protected TagEditorFrame tagEditorFrame;

	protected JTextArea tracks;

	public FreedbPanel(TagEditorFrame tagEditorFrame,
			MultipleFieldsMergingTable audioFiles) {
		this.tagEditorFrame = tagEditorFrame;
		this.audioFiles = audioFiles;

		FreedbSettings fdbs = new FreedbSettings();
		fdbs.setServer(PreferencesManager.get("entagged.freedb.server"));
		fdbs.setUserLogin(PreferencesManager.get("entagged.freedb.login"));
		fdbs.setUserDomain(PreferencesManager.get("entagged.freedb.domain"));
		fdbs
				.setProxyHost(PreferencesManager
						.get("entagged.freedb.proxyserver"));
		fdbs.setProxyPort(PreferencesManager.get("entagged.freedb.proxyport"));
		fdbs.setProxyUser(PreferencesManager.get("entagged.freedb.proxyuser"));
		fdbs.setProxyPass(PreferencesManager.get("entagged.freedb.proxypass"));
		fdbs.setInetConn(PreferencesManager.getInt("entagged.freedb.inetconn"));

		this.freedb = new Freedb(fdbs);

		MAX_RESULTS = PreferencesManager
				.getInt("entagged.freedbpanel.maxresults");

		createPanel();
	}

	private void createPanel() {
		JLabel artistL = new JLabel(LangageManager
				.getProperty("common.tag.artist"));
		JLabel albumL = new JLabel(LangageManager
				.getProperty("common.tag.album"));
		JLabel yearL = new JLabel(LangageManager.getProperty("common.tag.year"));
		JLabel genreL = new JLabel(LangageManager
				.getProperty("common.tag.genre"));
		JLabel commentL = new JLabel(LangageManager
				.getProperty("common.tag.comment"));
		JLabel maxResultL = new JLabel(LangageManager
				.getProperty("freedbpanel.maxresults"));

		ActionListener saveButtonListener = new SaveButtonListener();

		artist = new JTextField();
		artist.addActionListener(saveButtonListener);
		artist.setEnabled(false);
		album = new JTextField();
		album.addActionListener(saveButtonListener);
		album.setEnabled(false);
		year = new JTextField();
		year.addActionListener(saveButtonListener);
		year.setEnabled(false);

		String[] sortedGenres = new String[Tag.DEFAULT_GENRES.length];
		System.arraycopy(Tag.DEFAULT_GENRES, 0, sortedGenres, 0,
				sortedGenres.length);
		Arrays.sort(sortedGenres);
		genre = new AutoCompleteComboBox(sortedGenres);
		// genre.addActionListener( saveButtonListener );
		genre.setEnabled(false);
		comment = new JTextField();
		comment.addActionListener(saveButtonListener);
		comment.setEnabled(false);

		tracks = new JTextArea();
		tracks.setEnabled(false);
		JScrollPane tracksPane = new JScrollPane(tracks);

		JButton query = new JButton(LangageManager
				.getProperty("freedbpanel.search"));
		query.addActionListener(new QueryButtonListener());

		results = new JComboBox();
		results.addItemListener(new ResultItemListener());
		results.setEnabled(false);

		save = new JButton(LangageManager.getProperty("common.dialog.save"));
		save.addActionListener(saveButtonListener);
		save.setEnabled(false);

		saveRecursive = new JButton(LangageManager
				.getProperty("common.dialog.save.recursive"));
		saveRecursive.addActionListener(new SaveRecursiveButtonListener(
				tagEditorFrame, audioFiles));

		maxResults = new JTextField(String.valueOf(MAX_RESULTS));
		maxResults.setPreferredSize(new Dimension(100, 25));

		JPanel maxResultPanel = new JPanel(new FlowLayout());
		maxResultPanel.add(maxResultL);
		maxResultPanel.add(maxResults);

		SimpleFocusPolicy policy = new SimpleFocusPolicy(this);
		policy.setComponents(new Component[] { artist, album, year, genre,
				comment, save });

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 5, 2, 5);

		this.setLayout(gbl);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 1;
		gbc.weightx = 0.5;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(query, gbc);
		this.add(query);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(results, gbc);
		this.add(results);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(maxResultPanel, gbc);
		this.add(maxResultPanel);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 0;

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbl.setConstraints(save, gbc);
		this.add(save);

		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbl.setConstraints(saveRecursive, gbc);
		this.add(saveRecursive);

		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(artistL, gbc);
		this.add(artistL);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(albumL, gbc);
		this.add(albumL);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbl.setConstraints(yearL, gbc);
		this.add(yearL);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbl.setConstraints(genreL, gbc);
		this.add(genreL);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbl.setConstraints(commentL, gbc);
		this.add(commentL);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbl.setConstraints(artist, gbc);
		this.add(artist);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbl.setConstraints(album, gbc);
		this.add(album);

		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(year, gbc);
		this.add(year);

		gbc.gridx = 2;
		gbc.gridy = 3;
		gbl.setConstraints(genre, gbc);
		this.add(genre);

		gbc.gridx = 2;
		gbc.gridy = 4;
		gbl.setConstraints(comment, gbc);
		this.add(comment);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridheight = 6;
		gbl.setConstraints(tracksPane, gbc);
		this.add(tracksPane);
	}

	/**
	 * This method implements the query against FreeDB.
	 * 
	 * @param progressDialog
	 *            Dialog which allows abortion and reflection of the Process.
	 */
	protected void performSearch(ProgressDialog progressDialog) {
		Iterator it;
		java.util.List l = new LinkedList();

		try {
			int oldValue = MAX_RESULTS;
			MAX_RESULTS = Integer.parseInt(maxResults.getText());
			if (MAX_RESULTS <= 0) {
				MAX_RESULTS = oldValue;
				throw new IllegalStateException("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(tagEditorFrame, LangageManager
					.getProperty("freedbpanel.error.invalidmaxresult"),
					LangageManager.getProperty("common.dialog.error"),
					JOptionPane.ERROR_MESSAGE);
			progressDialog.dispose();
			return;
		}

		if (audioFiles.getAudioFiles().size() == 1
				&& ((File) audioFiles.getAudioFiles().get(0)) instanceof TagEditorTableModel.CurrentFolder) {

			Vector files = tagEditorFrame.getTagEditorTableModel().getFiles();
			for (int i = 0; i < files.size(); i++) {
				File file = (File) files.elementAt(i);
				if (!file.isDirectory() && file instanceof AudioFile)
					l.add(file);
			}
		} else {
			it = new ArrayList(audioFiles.getAudioFiles()).iterator();
			while (it.hasNext()) {
				File file = (File) it.next();
				if (!file.isDirectory() && file instanceof AudioFile)
					l.add(file);
			}
		}

		if (l.size() == 0) {
			JOptionPane.showMessageDialog(tagEditorFrame, LangageManager
					.getProperty("freedbpanel.error.nofileselected"),
					LangageManager.getProperty("common.dialog.error) "),
					JOptionPane.ERROR_MESSAGE);
			progressDialog.dispose();
			return;
		}

		if (progressDialog.aborted) {
			progressDialog.dispose();
			return;
		}

		float[] f = new float[l.size()];
		it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			f[i] = ((AudioFile) it.next()).getPreciseLength();
			i++;
		}

		progressDialog.setDescription(LangageManager
				.getProperty("freedb.progress.query"));
		try {
			// Now Query the DB about songs with the audio length of the files.
			FreedbQueryResult[] fdbqr = freedb.query(f);

			if (progressDialog.isAborted()) {
				progressDialog.dispose();
				return;
			}

			progressDialog.setAll(LangageManager
					.getProperty("freedb.readingresults"), true, fdbqr.length,
					1);
			System.out.println(LangageManager.getProperty(
					"freedb.searchsuccessful").replaceAll("%1",
					new Integer(fdbqr.length).toString()));
			System.out.println("\t"
					+ LangageManager.getProperty("freedb.readingresults"));

			/*
			 * Read details from results (Another connection?)
			 */
			FreedbPanel.this.freedbResults = new FreedbReadResult[fdbqr.length];

			for (int h = 0; h < fdbqr.length && !progressDialog.isAborted(); h++) {
				progressDialog.setValue(h + 1);
				FreedbPanel.this.freedbResults[h] = freedb.read(fdbqr[h]);
				System.out
						.println("\t"
								+ LangageManager.getProperty("freedb.resultof")
										.replaceAll("%1",
												new Integer(h + 1).toString())
										.replaceAll(
												"%2",
												new Integer(fdbqr.length)
														.toString())
										.replaceAll(
												"%3",
												new Integer((h + 1) * 100
														/ fdbqr.length)
														.toString()));
			}

			if (progressDialog.isAborted()) {
				progressDialog.dispose();
				return;
			}

			Arrays.sort(FreedbPanel.this.freedbResults);

			if (!progressDialog.isAborted()) {
				/*
				 * If the dialog was killed using the cross, the results
				 * shouldn't be displayed. Conflict with subsequent searches.
				 */
				FreedbPanel.this.searchDone();
			}
		} catch (FreedbException ex) {
			// ex.printStackTrace();
			System.out.println(LangageManager.getProperty("freedb.noresult"));
			JOptionPane.showMessageDialog(FreedbPanel.this.tagEditorFrame, ex
					.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		progressDialog.dispose();
	}

	protected void searchDone() {
		results.removeAllItems();
		results.setEnabled(true);

		for (int i = 0; i < freedbResults.length; i++) {
			// System.out.println(freedbResults[i]);
			results.addItem(new FreedbReadResultWrapper(freedbResults[i],
					i == freedbResults.length - 1));
			// results.addItem( "ID:" + freedbResults[i].getDiscId() + "-" +
			// freedbResults[i].getArtist());
		}

		if (freedbResults.length > 0)
			results.setSelectedIndex(freedbResults.length - 1);

		save.setEnabled(true);
		PreferencesManager.putInt("entagged.freedbpanel.maxresults",
				MAX_RESULTS);
	}

	public void update() {
		// Disable what should be and remove existing text
		artist.setText("");
		artist.setEnabled(false);
		album.setText("");
		album.setEnabled(false);
		year.setText("");
		year.setEnabled(false);
		genre.getTextField().setText("");
		genre.setEnabled(false);
		comment.setText("");
		comment.setEnabled(false);
		tracks.setText("");

		results.removeAllItems();
		results.setEnabled(false);
		save.setEnabled(false);
	}
}
