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
package entagged.tageditor.optionpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import entagged.freedb.Freedb;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbSettings;
import entagged.tageditor.resources.*;

import javax.swing.border.*;
import javax.swing.*;

public class FreedbOptionPanel extends JPanel implements OptionPanelInterface {

	private final ImageIcon IMAGE = ResourcesRepository.getImageIcon("freedb-optionpanel-button.png");

	protected JTextField login, domain, proxyServer, proxyPort, proxyUser,proxyPass;
	protected JComboBox server, inetConn;
	private JButton refresh;

	public FreedbOptionPanel() {
		Border border = BorderFactory.createEtchedBorder();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(border, LangageManager
			.getProperty("freedboptionpanel.title"), TitledBorder.LEFT, TitledBorder.TOP);
		this.setBorder(titledBorder);

		//*****CONTAINER PANEL ****************
		JLabel serverL = new JLabel(LangageManager.getProperty("freedboptionpanel.server"));
		JLabel loginL = new JLabel(LangageManager.getProperty("freedboptionpanel.login"));
		JLabel domainL = new JLabel("@");

		JLabel inetConnL = new JLabel(LangageManager.getProperty("freedboptionpanel.inetconn"));
		JLabel proxyServerL = new JLabel(LangageManager.getProperty("freedboptionpanel.proxyserver"));
		JLabel proxyPortL = new JLabel(LangageManager.getProperty("freedboptionpanel.proxyport"));
		JLabel proxyUserL = new JLabel(LangageManager.getProperty("freedboptionpanel.proxyuser"));
		JLabel proxyPassL = new JLabel(LangageManager.getProperty("freedboptionpanel.proxypass"));

		this.server = new JComboBox(new String[] {PreferencesManager.get("entagged.freedb.server")});

		this.refresh = new JButton("Refresh");
		this.refresh.addActionListener(new FreedbRefreshListener());

		this.login = new JTextField(PreferencesManager.get("entagged.freedb.login"));
		this.domain = new JTextField(PreferencesManager.get("entagged.freedb.domain"));
		this.proxyServer = new JTextField(PreferencesManager.get("entagged.freedb.proxyserver"));
		this.proxyPort = new JTextField(PreferencesManager.get("entagged.freedb.proxyport"));
		this.proxyUser = new JTextField(PreferencesManager.get("entagged.freedb.proxyuser"));
		this.proxyPass = new JTextField(PreferencesManager.get("entagged.freedb.proxypass"));

		Vector conns = new Vector();
		conns.add(new inetConnection(1, LangageManager.getProperty("freedboptionpanel.INETCONN1_DIRECT")));
		conns.add(new inetConnection(2, LangageManager.getProperty("freedboptionpanel.INETCONN2_PROXY")));
		conns.add(new inetConnection(3, LangageManager.getProperty("freedboptionpanel.INETCONN3_PROXY_WITH_AUTHENTICATION")));

		this.inetConn = new JComboBox(conns);
		this.inetConn.addItemListener(new InetConnectionListener());
		this.inetConn.setSelectedIndex(-1);
		this.inetConn.setSelectedIndex(PreferencesManager.getInt("entagged.freedb.inetconn") - 1);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 1, 2, 1);
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		this.setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(serverL, gbc);
		this.add(serverL);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(loginL, gbc);
		this.add(loginL);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbl.setConstraints(inetConnL, gbc);
		this.add(inetConnL);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(proxyServerL, gbc);
		this.add(proxyServerL);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbl.setConstraints(proxyUserL, gbc);
		this.add(proxyUserL);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbl.setConstraints(server, gbc);
		this.add(server);

		gbc.weightx = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbl.setConstraints(login, gbc);
		this.add(login);

		gbc.weightx = 0;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbl.setConstraints(domainL, gbc);
		this.add(domainL);

		gbc.weightx = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbl.setConstraints(domain, gbc);
		this.add(domain);

		gbc.weightx = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbl.setConstraints(inetConn, gbc);
		this.add(inetConn);

		gbc.weightx = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbl.setConstraints(proxyServer, gbc);
		this.add(proxyServer);

		gbc.weightx = 0;
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbl.setConstraints(proxyPortL, gbc);
		this.add(proxyPortL);

		gbc.weightx = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbl.setConstraints(proxyPort, gbc);
		this.add(proxyPort);

		gbc.weightx = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbl.setConstraints(proxyUser, gbc);
		this.add(proxyUser);

		gbc.weightx = 0;
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbl.setConstraints(proxyPassL, gbc);
		this.add(proxyPassL);

		gbc.weightx = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbl.setConstraints(proxyPass, gbc);
		this.add(proxyPass);
	}

	public JButton getButton() {
		return new JButton(LangageManager.getProperty("freedboptionpanel.button"), IMAGE);
	}

	public JPanel getPanel() {
		return this;
	}

	public boolean saveOptions() {
		PreferencesManager.put("entagged.freedb.server", ((String)this.server.getSelectedItem()).trim());
		PreferencesManager.put("entagged.freedb.login", this.login.getText().trim());
		PreferencesManager.put("entagged.freedb.domain", this.domain.getText().trim());
		PreferencesManager.putInt("entagged.freedb.inetconn", ((inetConnection)this.inetConn.getSelectedItem()).getCode());
		PreferencesManager.put("entagged.freedb.proxyserver", this.proxyServer.getText().trim());
		PreferencesManager.put("entagged.freedb.proxyport", this.proxyPort.getText().trim());
		PreferencesManager.put("entagged.freedb.proxyuser", this.proxyUser.getText().trim());
		PreferencesManager.put("entagged.freedb.proxypass", this.proxyPass.getText().trim());

		return true;
	}

	public String getOptionName() {
		return "freedb";
	}

	private class FreedbRefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Freedb freedb = new Freedb();

			server.removeAllItems();

			String[] servers;
			try {
				servers = freedb.getAvailableServers();
			}
			catch(FreedbException ex) {
				servers = new String[] {PreferencesManager
						.get("entagged.freedb.server")};
			}

			for(int i = 0 ; i < servers.length ; i++)
				server.addItem(servers[i]);

			server.setSelectedItem(PreferencesManager.get("entagged.freedb.server"));
		}
	}

	/**
	 * Depending on the type of internet connection, some options must be disabled
	 * 
	 * @author anacleto
	 *
	 */
	private class InetConnectionListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			int i = FreedbOptionPanel.this.inetConn.getSelectedIndex() + 1;
			if(i == FreedbSettings.INETCONN_DIRECT) {
				FreedbOptionPanel.this.proxyServer.setEnabled(false);
				FreedbOptionPanel.this.proxyPort.setEnabled(false);
				FreedbOptionPanel.this.proxyUser.setEnabled(false);
				FreedbOptionPanel.this.proxyPass.setEnabled(false);
			}
			else if(i == FreedbSettings.INETCONN_PROXY) {
				FreedbOptionPanel.this.proxyServer.setEnabled(true);
				FreedbOptionPanel.this.proxyPort.setEnabled(true);
				FreedbOptionPanel.this.proxyUser.setEnabled(false);
				FreedbOptionPanel.this.proxyPass.setEnabled(false);
			}
			else if(i == FreedbSettings.INETCONN_PROXY_WITH_AUTHENTICATION) {
				FreedbOptionPanel.this.proxyServer.setEnabled(true);
				FreedbOptionPanel.this.proxyPort.setEnabled(true);
				FreedbOptionPanel.this.proxyUser.setEnabled(true);
				FreedbOptionPanel.this.proxyPass.setEnabled(true);
			}
		}
	}

	/**
	 * Simple class to store types of internet connections to show in the combo 
	 * @author anacleto
	 */
	private class inetConnection {
		private int code;

		private String name;

		public inetConnection(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public String toString() {
			return name;
		}

		public boolean equals(Object o) {
			if(o instanceof inetConnection) {
				if(((inetConnection)o).getCode() == this.code)
					return true;
			}
			return false;
		}

		/**
		 * @return Returns the code.
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @param code The code to set.
		 */
		public void setCode(int code) {
			this.code = code;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}
	}
}