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
package entagged.tageditor.util.swing;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;

/**
 * This class is intended to act as a normal JTextField with one enhancement.
 * The field recieves a list of strings for completition. If the beginning of
 * such strings is written, a window will pop up and shows all matching in a
 * selectable list. If an item is selected with the mouse or by pressing enter,
 * the string will be written. <br>
 * If you know eclipse, similiar to its auto completement functionality.
 * 
 * @author Christian Laireiter
 */
public class QuickHelpFieldExtender extends KeyAdapter implements
		DocumentListener {

	/**
	 * This model is used to display the completion values.
	 */
	private final class CompletionModel implements ListModel {
		private Vector listeners = new Vector();

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
		 */
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		/**
		 * Notifies all registered listeners, that the contents have been
		 * changed.
		 * 
		 */
		public void fireChange() {
			Iterator it = listeners.iterator();
			while (it.hasNext()) {
				ListDataListener ldl = (ListDataListener) it.next();
				ldl.contentsChanged(new ListDataEvent(window.getValueList(),
						ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
			}
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int index) {
			List matches = getMatches();
			index = ((Integer) matches.get(index)).intValue();
			return values.get(index)
					+ " - "
					+ (descriptions.get(index) != null ? descriptions
							.get(index) : "");
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.ListModel#getSize()
		 */
		public int getSize() {
			return getMatches().size();
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
		 */
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

	}

	/**
	 * This field stores the descriptions for the correspoinding values in
	 * {@link #values}.
	 */
	protected Vector descriptions = new Vector();

	/**
	 * When test is inserted, the system time in milliseconds is stored here.
	 */
	protected long lastInsert;

	protected CompletionModel model;

	/**
	 * Here the {@link JTextComponent}is stored on which current instance is
	 * working.
	 */
	protected JTextComponent textField;

	/**
	 * This field stores the values for completement.
	 */
	protected Vector values = new Vector();

	/**
	 * This field stores the window which will allow the user to select a
	 * completition.
	 */
	protected QuickHelpSelectionWindow window = null;

	/**
	 * Creates an instance with its on text field.
	 */
	public QuickHelpFieldExtender() {
		this.textField = new JTextField();
		this.textField.setBorder(null);
		initialize();
	}

	/**
	 * This instance creates an instance, which will listen to the given field
	 * in order to use completion functionality.
	 * 
	 * @param listenTo
	 *            text field where the input is done.
	 */
	public QuickHelpFieldExtender(JTextField listenTo) {
		assert listenTo != null : "Argument must not be null";
		this.textField = listenTo;
		initialize();
	}

	/**
	 * This method adds a value for completement.
	 * 
	 * @param value
	 *            Value, which will be typed.
	 * @param description
	 *            Description for the help window. Can be <code>null</code>.
	 */
	public void addCompletionValue(String value, String description) {
		if (value == null || value.length() == 0) {
			throw new IllegalArgumentException(
					"Value must not be null nor empty.");
		}
		if (!values.contains(value)) {
			this.values.add(value);
			this.descriptions.add(description);
		} else
			throw new IllegalStateException("The value: \"" + value
					+ "\" is already inserted.");
	}

	/**
	 * This method will look where the test cursor is displayed and set the
	 * completement windows location next to it.
	 */
	protected void adjustPosition() {
		Point caretPosition = this.getTextField().getCaret()
				.getMagicCaretPosition();
		if (caretPosition != null) {
			caretPosition.y = this.getTextField().getLocationOnScreen().y - 10
					- getSelectionWindow().getSize().height;
			caretPosition.x += this.getTextField().getLocationOnScreen().x
					- (getSelectionWindow().getWidth() / 2);
			/*
			 * Now make some adjustments, if the window would be partly outside
			 * the screen.
			 */
			if (caretPosition.x < 0) {
				caretPosition.x = 0;
			}
			if (caretPosition.y < 0) {
				caretPosition.y = 0;
			}
			/*
			 * The other possibility will be ignored, because I can't imagine
			 * this would happen on normal use. Same with overlaying the textfield.
			 */
			getSelectionWindow().setLocation(caretPosition);
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		this.lastInsert = System.currentTimeMillis();
		triggerCompletement();
	}

	/**
	 * Creates a editor for use with comboboxes.
	 * 
	 * @return a combobox editor.
	 */
	public ComboBoxEditor createComboBoxEditor() {
		return new ComboBoxEditor() {

			public void addActionListener(ActionListener l) {
				// Nope
			}

			public Component getEditorComponent() {
				return textField;
			}

			public Object getItem() {
				return textField.getText();
			}

			public void removeActionListener(ActionListener l) {
				// nope
			}

			public void selectAll() {
				textField.selectAll();
			}

			public void setItem(Object anObject) {
				textField.setText(anObject.toString());
			}
		};
	}

	/**
	 * This method will read the string form the current textcursor back to a
	 * space character.
	 * 
	 * @return word between space character till text cursor.
	 */
	protected String getLastWord() {
		StringBuffer result = new StringBuffer();
		String text = textField.getText();
		if (text.length() == 0) {
			return "";
		}
		int caretPos = this.textField.getCaretPosition();
		int pointer = caretPos - 1;

		while (pointer > 0) {
			if (!isDelimiter(text.charAt(pointer))) {
				pointer--;
			} else {
				pointer++;
				break;
			}
		}
		if (pointer >= 0 && pointer < caretPos)
			result.append(text.substring(pointer, caretPos));

		return result.toString();
	}

	protected List getMatches() {
		ArrayList result = new ArrayList();
		String lastWort = getLastWord();
		for (int i = 0; lastWort.length() > 0 && i < values.size(); i++) {
			if (values.get(i).toString().startsWith(lastWort)) {
				result.add(new Integer(i));
			}
		}
		return result;
	}

	/**
	 * This method returns the window for displaying completions. <br>
	 * 
	 * @return Window for selection completions.
	 */
	public QuickHelpSelectionWindow getSelectionWindow() {
		if (this.window == null) {
			this.window = new QuickHelpSelectionWindow(this,
					this.model = new CompletionModel());
		}
		return this.window;
	}

	/**
	 * This method returns the text field this object is working with.
	 * 
	 * @return Text field.
	 */
	public JTextComponent getTextField() {
		return this.textField;
	}

	/**
	 * Initializes this Field
	 */
	private void initialize() {
		registerTo(textField);
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		this.lastInsert = System.currentTimeMillis();
		triggerCompletement();
	}

	/**
	 * Searches in the registered values for one which starts with the given
	 * string. If so <code>true</code> is returned.
	 * 
	 * @param start
	 *            The string which is searched.
	 * @return <code>true</code> if one value starts with given string.
	 */
	protected boolean isAnyValueStartingWith(String start) {
		Iterator iterator = values.iterator();
		while (iterator.hasNext() && start.length() > 0) {
			String current = (String) iterator.next();
			if (current.startsWith(start) && !current.equals(start))
				return true;
		}
		return false;
	}

	/**
	 * @param c
	 * @return
	 */
	private boolean isDelimiter(char c) {
		return !Character.isLetterOrDigit(c) && c != '<';
	}

	/**
	 * (overridden)
	 * 
	 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.textField
				|| e.getSource() == this.window.getValueList()
				&& !e.isConsumed()) {
			if (getSelectionWindow().isVisible()) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					getSelectionWindow().setVisible(false);
					e.consume();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT
						|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this.model.fireChange();
				}
				final JList list = getSelectionWindow().getValueList();
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (list.getSelectedIndex() > 0) {
						list.setSelectedIndex(list.getSelectedIndex() - 1);
					}
					e.consume();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (list.getSelectedIndex() < list.getModel().getSize() - 1) {
						list.setSelectedIndex(list.getSelectedIndex() + 1);
					}
					e.consume();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							select(list.getSelectedIndex());
						}
					});
					e.consume();
				}
			}
		}
	}

	/**
	 * This method will change the object to now work with the given textfield.
	 * 
	 * @param toRegister
	 *            The textfield
	 */
	public void registerTo(JTextComponent toRegister) {
		if (this.textField != null) {
			this.textField.getDocument().removeDocumentListener(this);
			this.textField.removeKeyListener(this);
		}
		toRegister.getDocument().addDocumentListener(this);
		toRegister.addKeyListener(this);
		this.textField = toRegister;
	}

	/**
	 * This removes the given completion value.
	 * 
	 * @param value
	 *            The value which shouldn't be provided any more.
	 */
	public void removeCompletionValue(String value) {
		assert value != null;
		int index = -1;
		while ((index = values.indexOf(value)) != -1) {
			values.remove(index);
			descriptions.remove(index);
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		this.lastInsert = System.currentTimeMillis();
		triggerCompletement();
	}

	/**
	 * @param index
	 */
	public void select(int index) {
		List matches = getMatches();
		index = ((Integer) matches.get(index)).intValue();
		String word = getLastWord();
		String value = (String) values.get(index);
		value = value.substring(word.length(), value.length());
		try {
			AttributeSet set = getTextField().getDocument().getRootElements()[0]
					.getAttributes();
			getTextField().getDocument().insertString(
					getTextField().getCaretPosition(), value, set);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getSelectionWindow().setVisible(false);
				textField.requestFocus();
			}
		});
	}

	/**
	 * This method will adjust the position of the completion window and set it
	 * visible. <br>
	 * This method has been introduced for subclasses, in order to interrupt the
	 * displaying of the window. <br>
	 */
	protected void showWindow() {
		getSelectionWindow().setVisible(true);
		adjustPosition();
	}

	/**
	 * This method will start a Thread which will display the completion window.
	 */
	private void triggerCompletement() {
		if (SwingUtilities.getRoot(textField) == null) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final long started = lastInsert = System.currentTimeMillis();
				if (!getSelectionWindow().isVisible()) {
					if (isAnyValueStartingWith(getLastWord())) {
						new Timer(true).schedule(new TimerTask() {
							public void run() {
								if (lastInsert == started
										&& !getSelectionWindow().isVisible()) {
									model.fireChange();
									showWindow();
								}
							}
						}, 500);
					}
				} else {
					if (!isAnyValueStartingWith(getLastWord())) {
						window.setVisible(false);
					} else {
						// Position can be updated if all events have been handled 
						// (repainting after typing and so on)
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								adjustPosition();
								model.fireChange();
							}
						});
					}
				}
			}
		});
	}
}