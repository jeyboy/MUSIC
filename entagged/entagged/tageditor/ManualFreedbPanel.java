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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.util.MultipleFieldsMergingTable;
import entagged.tageditor.util.swing.SimpleFocusPolicy;

public class ManualFreedbPanel extends JPanel {

	private class FreedbQueryResultWrapper {
		private FreedbQueryResult r;

		public FreedbQueryResultWrapper(FreedbQueryResult r) {
			this.r = r;
		}

		public String toString() {
			return "(" + r.getCategory() + ") " + r.getArtist() + "-"
					+ r.getAlbum();
		}
	}

	private class QueryButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// SHOW THE WAIT DIALOG
			// WaitDialog wd = new
			// WaitDialog(ManualFreedbPanel.this.tagEditorFrame);
			// ------
			final ProgressDialog progressDialog = new ProgressDialog(
					tagEditorFrame, LangageManager
							.getProperty("freedb.progress.title"),
					LangageManager.getProperty("freedb.progress.query"));
			progressDialog.setModal(true);
			new Thread(new Runnable() {
				public void run() {
					try {
						freedbQueryResults = freedb.query(search.getText());
						if (!progressDialog.isAborted()) {
							ManualFreedbPanel.this.searchDone();
						}
					} catch (FreedbException ex) {
						// ex.printStackTrace();
						System.out.println(LangageManager
								.getProperty("freedb.noresult"));
						JOptionPane.showMessageDialog(
								ManualFreedbPanel.this.tagEditorFrame, ex
										.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					// Hide it from here.
					progressDialog.setVisible(false);
				}
			}, "FreeDB search").start();
			progressDialog.setVisible(true);
			progressDialog.dispose();

			// CLOSES THE WAIT DIALOG
			// wd.setVisible(false);
		}

	}

	private class QueryResultListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			// FreedbQueryResult query = (FreedbQueryResult) e.getItem();
			if (freedbQueryResults != null && freedbQueryResults.length > 0) {
				int selection = results.getSelectedIndex();
				FreedbQueryResult query = freedbQueryResults[0];
				/*
				 * It may occur that nothing has been selected.
				 */
				if (selection >= 0 && selection < freedbQueryResults.length) {
					query = freedbQueryResults[results.getSelectedIndex()];
				}
				try {
					freedbResult = freedb.read(query);
					showResult();
				} catch (FreedbException ex) {
					JOptionPane.showMessageDialog(
							ManualFreedbPanel.this.tagEditorFrame, ex
									.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private class SaveButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent ev) {
			FreedbReadResult fdbrr = ManualFreedbPanel.this.freedbResult;

			if (ManualFreedbPanel.this.audioFiles.getAudioFiles().size() == 0)
				return;

			if (ManualFreedbPanel.this.audioFiles.getAudioFiles().size() <= fdbrr
					.getTracksNumber()) {
				if (!handleNonMatchingFileNumber())
					return;
			} else {
				JOptionPane
						.showMessageDialog(
								ManualFreedbPanel.this.tagEditorFrame,
								"The number of tracks selected is greater than the number of tracks in the freedb result !",
								"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			tagEditorFrame.getEditorSettings().prepareAudioProcessing();

			boolean warning = false;
			String warningMessage = LangageManager
					.getProperty("freedbpanel.errors" + "\n");

			Iterator it = new ArrayList(ManualFreedbPanel.this.audioFiles
					.getAudioFiles()).iterator();
			int i = 0;
			while (it.hasNext()) {
				AudioFile f = (AudioFile) it.next();

				Tag tag = f.getTag();
				tag.setTitle(fdbrr.getTrackTitle(i).trim());
				tag.setAlbum(ManualFreedbPanel.this.album.getText());
				tag.setGenre(ManualFreedbPanel.this.genre.getTextField()
						.getText());
				tag.setArtist(ManualFreedbPanel.this.artist.getText());
				tag.setYear(ManualFreedbPanel.this.year.getText());
				tag.setComment(ManualFreedbPanel.this.comment.getText());
				tag.setTrack(new DecimalFormat("00").format(fdbrr
						.getTrackNumber(i)));

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

			ManualFreedbPanel.this.tagEditorFrame.refreshCurrentTableView();

			if (warning)
				new WarningDialog(ManualFreedbPanel.this.tagEditorFrame,
						warningMessage);
		}

		private boolean handleNonMatchingFileNumber() {
			// Create dialog asking to resolve conflicts
			FreedbChooserPanel panel = new FreedbChooserPanel(
					ManualFreedbPanel.this.tagEditorFrame, new ArrayList(
							ManualFreedbPanel.this.audioFiles.getAudioFiles()),
					ManualFreedbPanel.this.freedbResult);
			return !panel.isAborted();
		}
	}

	protected JTextField artist, album, year, comment;

	protected MultipleFieldsMergingTable audioFiles;

	protected Freedb freedb;

	protected FreedbQueryResult[] freedbQueryResults = null;

	protected FreedbReadResult freedbResult = null;

	protected AutoCompleteComboBox genre;

	protected JComboBox results;

	private JButton save;

	protected JTextField search;

	protected TagEditorFrame tagEditorFrame;

	protected JTextArea tracks;

	public ManualFreedbPanel(TagEditorFrame tagEditorFrame,
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

		ActionListener queryButtonListener = new QueryButtonListener();
		search = new JTextField(LangageManager.getProperty("freedbpanel.enter"));
		search.addActionListener(queryButtonListener);

		results = new JComboBox();
		results.addItemListener(new QueryResultListener());
		results.setEnabled(false);

		JButton query = new JButton(LangageManager
				.getProperty("freedbpanel.search"));
		query.addActionListener(queryButtonListener);

		save = new JButton(LangageManager.getProperty("common.dialog.save"));
		save.addActionListener(saveButtonListener);
		save.setEnabled(false);

		SimpleFocusPolicy policy = new SimpleFocusPolicy(this);
		policy.setComponents(new Component[] { search, artist, album, year,
				genre, comment, save });

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
		gbc.gridwidth = 2;
		gbl.setConstraints(search, gbc);
		this.add(search);

		/*
		 * gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbl.setConstraints(
		 * freedbIDL, gbc ); this.add( freedbIDL );
		 * 
		 * gbc.gridx = 0; gbc.gridy = 2; gbl.setConstraints( freedbGenreL, gbc );
		 * this.add( freedbGenreL );
		 */
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbl.setConstraints(query, gbc);
		this.add(query);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbl.setConstraints(results, gbc);
		this.add(results);

		/*
		 * gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbl.setConstraints(
		 * resultL, gbc ); this.add( resultL );
		 */

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 4;
		gbl.setConstraints(save, gbc);
		this.add(save);

		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(artistL, gbc);
		this.add(artistL);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbl.setConstraints(albumL, gbc);
		this.add(albumL);

		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(yearL, gbc);
		this.add(yearL);

		gbc.gridx = 2;
		gbc.gridy = 3;
		gbl.setConstraints(genreL, gbc);
		this.add(genreL);

		gbc.gridx = 2;
		gbc.gridy = 4;
		gbl.setConstraints(commentL, gbc);
		this.add(commentL);

		// gbc.anchor = GridBagConstraints.WEST;
		/*
		 * gbc.weightx = 1; gbc.gridx = 1; gbc.gridy = 1; gbl.setConstraints(
		 * freedbID, gbc ); this.add( freedbID );
		 * 
		 * gbc.gridx = 1; gbc.gridy = 2; gbl.setConstraints( freedbGenre, gbc );
		 * this.add( freedbGenre );
		 */
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbl.setConstraints(artist, gbc);
		this.add(artist);

		gbc.gridx = 3;
		gbc.gridy = 1;
		gbl.setConstraints(album, gbc);
		this.add(album);

		gbc.gridx = 3;
		gbc.gridy = 2;
		gbl.setConstraints(year, gbc);
		this.add(year);

		gbc.gridx = 3;
		gbc.gridy = 3;
		gbl.setConstraints(genre, gbc);
		this.add(genre);

		gbc.gridx = 3;
		gbc.gridy = 4;
		gbl.setConstraints(comment, gbc);
		this.add(comment);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridheight = 6;
		gbl.setConstraints(tracksPane, gbc);
		this.add(tracksPane);
	}

	protected void searchDone() {
		results.removeAllItems();
		results.setEnabled(true);

		for (int i = 0; i < freedbQueryResults.length; i++)
			results
					.addItem(new FreedbQueryResultWrapper(freedbQueryResults[i]));
	}

	protected void showResult() {
		if (freedbResult != null) {
			artist.setText(freedbResult.getArtist().trim());
			artist.setEnabled(true);
			album.setText(freedbResult.getAlbum().trim());
			album.setEnabled(true);
			year.setText(freedbResult.getYear().trim());
			year.setEnabled(true);
			genre.getTextField().setText(freedbResult.getGenre().trim());
			genre.setEnabled(true);
			comment.setText(freedbResult.getAlbumComment().trim());
			comment.setEnabled(true);

			tracks.setText("");
			int nb = freedbResult.getTracksNumber();
			for (int i = 0; i < nb; i++)
				tracks.append(freedbResult.getTrackTitle(i) + "\n");
		}
		save.setEnabled(true);
	}

	public void update() {
		/*
		 * artist.setText(""); artist.setEnabled(false); album.setText("");
		 * album.setEnabled(false); year.setText(""); year.setEnabled(false);
		 * genre.getTextField().setText(""); genre.setEnabled(false);
		 * comment.setText(""); comment.setEnabled(false);
		 * 
		 * tracks.setText("");
		 * 
		 * results.removeAllItems(); results.setEnabled(false);
		 * 
		 * save.setEnabled(false);
		 */
	}
}
