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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.mp3.Id3v2Tag;
import entagged.tageditor.listeners.ControlPanelButtonListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.tools.stringtransform.TransformSet;
import entagged.tageditor.tools.stringtransform.TransformationList;
import entagged.tageditor.util.MultipleFieldsMergingTable;
import entagged.tageditor.util.swing.SimpleFocusPolicy;

/**
 * Creates the Bottom panel of the main windows contains all operations realtive
 * to the current showing album/selected file <br>
 * $Id: TagInfoPanel.java,v 1.1 2007/03/23 14:17:11 nicov1 Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK)
 * @version v0.03
 */
public class TagInfoPanel extends AbstractControlPanel {

	/**
	 * This class is for managing events on the commenF field. <br>
	 * Main use is to catch the Tab-Key and cycle around to the next focusable.
	 * component. <br>
	 * Second use is to lay out the info panel again. Got some unpredictable
	 * behavior. It does new layouting if the mouse cursor moves.
	 */
	private final class CommentFieldListener extends KeyAdapter {
		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getSource() == commentF) {
				if (e.getKeyChar() == KeyEvent.VK_TAB) {
					e.consume();
				}
			}
		}

		/**
		 * Catch the Tab-Key and perform a focus change.
		 * 
		 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent e) {
			if (e.getSource() == commentF) {
				if (e.getKeyChar() == KeyEvent.VK_TAB) {
					commentF.transferFocus();
					e.consume();
				}
			}
		}
	}

	private class DeleteButtonListener extends ControlPanelButtonListener {
		public DeleteButtonListener(TagEditorFrame tagEditorFrame,
				MultipleFieldsMergingTable audioFiles) {
			super(tagEditorFrame, audioFiles);
		}

		protected void audioFileAction(AudioFile af)
				throws CannotWriteException {
			AudioFileIO.delete(af);
		}

		protected void finalizeAction() {
			// Save he selected indices
			int[] indices = transformationList.getSelectedIndices();
			PreferencesManager.putIntArray("taginfopanel.transformations",
					indices);
		}

		protected void prepareAction() {
			// Nothing ot prepare
		}
	}

	private class SaveButtonListener extends ControlPanelButtonListener {
		String title, artist, album, year, comment, track, genre;

		private TransformSet transformSet;

		public SaveButtonListener(TagEditorFrame tagEditorFrame,
				MultipleFieldsMergingTable audioFiles) {
			super(tagEditorFrame, audioFiles);
		}

		protected void audioFileAction(AudioFile af)
				throws CannotWriteException {
			Tag tag = af.getTag();

			System.out.println("Tagging file: " + af.getName());

			String VARIES_PATTERN = "\\Q" + VARIES + "\\E";

			if (!title.equals(VARIES)) {
				tag.setTitle(title.replaceAll(VARIES_PATTERN, tag
						.getFirstTitle()));
			}

			if (!artist.equals(VARIES)) {
				tag.setArtist(artist.replaceAll(VARIES_PATTERN, tag
						.getFirstArtist()));
			}

			if (!album.equals(VARIES)) {
				tag.setAlbum(album.replaceAll(VARIES_PATTERN, tag
						.getFirstAlbum()));
			}

			if (!year.equals(VARIES)) {
				tag
						.setYear(year.replaceAll(VARIES_PATTERN, tag
								.getFirstYear()));
			}

			if (!comment.equals(VARIES)) {
				tag.setComment(comment.replaceAll(VARIES_PATTERN, tag
						.getFirstComment()));
			}

			if (!track.equals(VARIES)) {
				tag.setTrack(track.replaceAll(VARIES_PATTERN, tag
						.getFirstTrack()));
			}

			if (!genre.equals(VARIES)) {
				tag.setGenre(genre.replaceAll(VARIES_PATTERN, tag
						.getFirstGenre()));
			}

			this.transformSet.transformFirstCommons(tag);

			if (tag instanceof Id3v2Tag)
				tag
						.setEncoding(PreferencesManager
								.get("entagged.tag.encoding"));

			AudioFileIO.write(af);
		}

		protected void finalizeAction() {
			// Save he selected indices
			int[] indices = transformationList.getSelectedIndices();
			PreferencesManager.putIntArray("taginfopanel.transformations",
					indices);
			artistF.requestFocus();
		}

		protected void prepareAction() {
			title = TagInfoPanel.this.titleF.getText().trim();
			artist = TagInfoPanel.this.artistF.getText().trim();
			album = TagInfoPanel.this.albumF.getText().trim();
			year = TagInfoPanel.this.yearF.getText().trim();
			comment = TagInfoPanel.this.commentF.getText().trim();
			track = TagInfoPanel.this.trackF.getText().trim();
			genre = TagInfoPanel.this.genreF.getTextField().getText().trim();
			this.transformSet = transformationList.getTransformSet();
		}
	}

	/**
	 * This class will be registered on {@link javax.swing.text.JTextComponent}
	 * objects. If such an objects gets the focus its complete content will be
	 * selected. <br>
	 */
	private final class TextFieldFocusListener extends FocusAdapter {
		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent)
		 */
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof JTextComponent) {
				((JTextComponent) e.getSource()).selectAll();
			}
		}
	}

	protected JTextField artistF, albumF, titleF, trackF, yearF;

	protected JTextArea commentF;

	protected AutoCompleteComboBox genreF;

	protected TransformationList transformationList;

	public TagInfoPanel(TagEditorFrame tagEditorFrame,
			MultipleFieldsMergingTable audioFiles) {
		super(tagEditorFrame, audioFiles);
	}

	protected void createPanel() {
		ActionListener saveButtonListener = new SaveButtonListener(
				tagEditorFrame, audioFiles);

		TextFieldFocusListener tffl = new TextFieldFocusListener();

		artistF = new JTextField();
		artistF.addActionListener(saveButtonListener);
		artistF.addFocusListener(tffl);
		albumF = new JTextField();
		albumF.addActionListener(saveButtonListener);
		albumF.addFocusListener(tffl);
		titleF = new JTextField();
		titleF.addActionListener(saveButtonListener);
		titleF.addFocusListener(tffl);
		trackF = new JTextField();
		trackF.addActionListener(saveButtonListener);
		trackF.addFocusListener(tffl);

		String[] sortedGenres = new String[Tag.DEFAULT_GENRES.length];
		System.arraycopy(Tag.DEFAULT_GENRES, 0, sortedGenres, 0,
				sortedGenres.length);
		Arrays.sort(sortedGenres);

		genreF = new AutoCompleteComboBox(sortedGenres);
		// genreF.addActionListener( saveButtonListener );
		((JTextComponent) genreF.getEditor().getEditorComponent())
				.addFocusListener(tffl);

		yearF = new JTextField();
		yearF.addActionListener(saveButtonListener);
		yearF.addFocusListener(tffl);
		commentF = new JTextArea();
		CommentFieldListener cfl = new CommentFieldListener();
		commentF.addKeyListener(cfl);
		commentF.addFocusListener(tffl);
		JScrollPane commentSP = new JScrollPane(commentF);
		// To prevent ugly resizing while typing.
		commentSP.setPreferredSize(new Dimension(
				commentSP.getMaximumSize().width, 225));
		// commentF.addActionListener( saveButtonListener );

		JButton save = new JButton(LangageManager
				.getProperty("common.dialog.save"));
		save.addActionListener(saveButtonListener);
		JButton delete = new JButton(LangageManager
				.getProperty("common.dialog.delete"));
		delete.addActionListener(new DeleteButtonListener(tagEditorFrame,
				audioFiles));

		JLabel artistL = new JLabel(LangageManager
				.getProperty("common.tag.artist"));
		JLabel albumL = new JLabel(LangageManager
				.getProperty("common.tag.album"));
		JLabel titleL = new JLabel(LangageManager
				.getProperty("common.tag.title"));
		JLabel trackL = new JLabel(LangageManager
				.getProperty("common.tag.track"));
		JLabel genreL = new JLabel(LangageManager
				.getProperty("common.tag.genre"));
		JLabel yearL = new JLabel(LangageManager.getProperty("common.tag.year"));
		JLabel commentL = new JLabel(LangageManager
				.getProperty("common.tag.comment"));

		int[] indices = PreferencesManager
				.getIntArray("taginfopanel.transformations");
		this.transformationList = new TransformationList(indices);
		// this.transformationList.setVisibleRowCount(3);
		JScrollPane transformationListS = new JScrollPane(transformationList);

		SimpleFocusPolicy policy = new SimpleFocusPolicy(this);
		policy
				.setComponents(new Component[] { artistF, albumF, titleF,
						trackF, yearF, commentF,
						genreF.getEditor().getEditorComponent(), save });

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 1, 2, 1);
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;

		this.setLayout(gbl);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(artistL, gbc);
		this.add(artistL);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(albumL, gbc);
		this.add(albumL);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbl.setConstraints(titleL, gbc);
		this.add(titleL);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(trackL, gbc);
		this.add(trackL);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbl.setConstraints(commentL, gbc);
		this.add(commentL);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbl.setConstraints(genreL, gbc);
		this.add(genreL);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 4;
		gbl.setConstraints(save, gbc);
		this.add(save);

		gbc.gridx = 4;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbl.setConstraints(delete, gbc);
		this.add(delete);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbl.setConstraints(artistF, gbc);
		this.add(artistF);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbl.setConstraints(albumF, gbc);
		this.add(albumF);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbl.setConstraints(titleF, gbc);
		this.add(titleF);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbl.setConstraints(trackF, gbc);
		this.add(trackF);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(commentSP, gbc);
		this.add(commentSP);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		gbl.setConstraints(genreF, gbc);
		this.add(genreF);

		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbl.setConstraints(yearL, gbc);
		this.add(yearL);

		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbl.setConstraints(yearF, gbc);
		this.add(yearF);

		/*
		 * gbc.weightx = 1; gbc.gridx = 4; gbc.gridy = 0; gbl.setConstraints(
		 * caseTransformationL, gbc ); this.add( caseTransformationL );
		 */

		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridheight = 6;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(transformationListS, gbc);
		this.add(transformationListS);
	}

	public void update() {
		if(audioFiles.getAudioFiles().size() > 0 && ((File)audioFiles.getAudioFiles().get(0)).getAbsoluteFile().isFile()) {
			artistF.setText(audioFiles.getArtist());
			albumF.setText(audioFiles.getAlbum());
			titleF.setText(audioFiles.getTitle());
			trackF.setText(audioFiles.getTrackNumber());
			genreF.getTextField().setText(audioFiles.getGenre());
			yearF.setText(audioFiles.getYear());
			commentF.setText(audioFiles.getComment());
			setEdition(true);
		}
		else {
			artistF.setText("");
			albumF.setText("");
			titleF.setText("");
			trackF.setText("");
			genreF.getTextField().setText("");
			yearF.setText("");
			commentF.setText("");
			setEdition(false);
		}
	}
	
	private void setEdition(boolean choix) {
		artistF.setEnabled(choix);
		albumF.setEnabled(choix);
		titleF.setEnabled(choix);
		trackF.setEnabled(choix);
		genreF.setEnabled(choix);
		yearF.setEnabled(choix);
		commentF.setEnabled(choix);
	}
}
