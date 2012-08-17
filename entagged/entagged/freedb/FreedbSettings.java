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
package entagged.freedb;


/**
 *  Creates a connection socket to the freedb server cgi script and eventually query it $Id: FreedbSettings.java,v 1.1 2007/03/23 14:16:58 nicov1 Exp $
 *
 * @author     Rapha?l Slinckx (KiKiDonK)
 * @version    v0.03
 */
public class FreedbSettings {
	
	public final static int INETCONN_DIRECT=1;
	public final static int INETCONN_PROXY=2;
	public final static int INETCONN_PROXY_WITH_AUTHENTICATION=3;

	private String server = "freedb.freedb.org";
	private String userLogin = format(System.getProperty("user.name"));
	private String userDomain = format(System.getProperty("os.name"));
	private String clientName = "Entagged";
	private String clientVersion = "v0.10";
	
	private String proxyHost =null;
	private String proxyPort =null;
	private String proxyUser =null;
	private String proxyPass =null;
	private int inetConn=INETCONN_DIRECT;
	
	
	public void setServer(String server) {
	    this.server = format(server);
	}
	
	public String getServer() {
	    return this.server;
	}
	
	public void setUserLogin(String userLogin) {
	    this.userLogin = format(userLogin);
	}
	
	public String getUserLogin() {
	    return this.userLogin;
	}
	
	public void setUserDomain(String userDomain) {
	    this.userDomain = format(userDomain);
	}
	
	public String getUserDomain() {
	    return this.userDomain;
	}
	
	public void setClientName(String clientName) {
	    this.clientName = format(clientName);
	}

	
	public String getClientName() {
	    return this.clientName;
	}
	
	public void setClientVersion(String clientVersion) {
	    this.clientVersion = format(clientVersion);
	}
	
	public String getClientVersion() {
	    return this.clientVersion;
	}
	
	public String getProtocol() {
	    return "6";
	}
	
	private String format(String s) {
	    if(s.length() == 0)
	        return "default";
	    int i = s.indexOf(" ");
	    return (i == -1) ? s : s.substring(0, i);
	}
	
	public int getInetConn() {
		return inetConn;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getProxyPass() {
		return proxyPass;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setInetConn(int i) {
		inetConn = i;
	}

	public void setProxyHost(String string) {
		proxyHost = string;
	}

	public void setProxyPass(String string) {
		proxyPass = string;
	}

	public void setProxyPort(String string) {
		proxyPort = string;
	}

	public void setProxyUser(String string) {
		proxyUser = string;
	}

}

