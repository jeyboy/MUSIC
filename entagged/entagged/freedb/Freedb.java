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

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//$Id: Freedb.java,v 1.1 2007/03/23 14:16:57 nicov1 Exp $
public class Freedb {
    
    private class SimpleTrack implements FreedbTrack {
	    private float length;	    
	    
	    public SimpleTrack(float sec) {
	        this.length = sec;
	    }
	    
	    public SimpleTrack (int min, int sec) {
	    	this(min,sec,0);
	    }
	    
	    public SimpleTrack(int min, int sec, float frac) {
	        this.length = min*60 + sec+frac;
	    }
	    
	    public int getLength() {
	        return (int)this.length;
	    }
	    
	    public float getPreciseLength() {
	    	return length;
	    }
	}
    

    public static void main(String[] args) throws Exception {

    	Freedb freedb = new Freedb();
        
    	String[] serv = freedb.getAvailableServers();
    	for(int i = 0; i< serv.length; i++)
    	    System.out.println(serv[i]); 
    }
    
    private FreedbSettings settings;
    
    public Freedb() {
        this.settings = new FreedbSettings();
    }
    
    public Freedb(FreedbSettings conn) {
        this.settings = conn;
    }
    
    private String searchFreedb(String search) throws FreedbException {
    	setupConnection();
    	
        String terms = search.replaceAll(" ", "+");
        
		URL url = null;
		try {
			url = new URL( "http://www.freedb.org/freedb_search.php?words="+terms+"&allfields=NO&fields=artist&fields=title&allcats=YES&grouping=none" );
		} catch ( MalformedURLException e ) {
			throw new FreedbException("The URL: "+url+" is invalid, remove any accents from the search terms and try again");
		}
		assert url != null;

		URLConnection connection = null;

		try {
			connection = url.openConnection();
			setupProxy(connection);
		} catch ( IOException e ) {
			//e.printStackTrace();
			throw new FreedbException( "Error while trying to connect to freedb server, "+e.getMessage()+". Check your internet connection settings." );
		}
		assert connection != null;

		String output = null;

		try {
			InputStreamReader isr = new InputStreamReader( connection.getInputStream() );
						
			BufferedReader in = new BufferedReader( isr );
			String inputLine;

			output = "";

			while ( ( inputLine = in.readLine() ) != null )
				output += inputLine + "\n";
			in.close();
		} catch ( IOException e ) {
			//e.printStackTrace();
		    throw new FreedbException( "Error while trying read data from freedb server, "+e.getMessage()+". Check your internet connection settings." );
		}
		assert output != null;
		
		return output;
    }
    
    private String askFreedb(String command) throws FreedbException {
    	//System.out.println("Asking freedb");
    	setupConnection();
		URL url = null;

		try {
			url = new URL( "http://"+this.settings.getServer()+":80/~cddb/cddb.cgi" );
		} catch ( MalformedURLException e ) {
			throw new FreedbException("The URL: "+url+" is invalid, correct the server setting");
		}
		assert url != null;

		URLConnection connection = null;

		try {
			connection = url.openConnection();
			setupProxy(connection);
			connection.setDoOutput( true );

			PrintWriter out = new PrintWriter(connection.getOutputStream() );
            // System.out.println( "cmd=" + servercommand + "&hello=" + clientLogin + "+" + clientDomain + "+" + softwareName + "+" + softwareVersion + "&proto=6");
            out.println( "cmd=" + command + "&hello=" + this.settings.getUserLogin() + "+" + this.settings.getUserDomain() + "+" + this.settings.getClientName() + "+" + this.settings.getClientVersion() + "&proto="+ this.settings.getProtocol() );
            out.close();
		} catch ( IOException e ) {
			//e.printStackTrace();
			throw new FreedbException( "Error while trying to connect to freedb server, "+e.getMessage()+". Check your internet connection settings." );
		}
		assert connection != null;

		String output = null;

		try {
			InputStreamReader isr;
			
			try {
				isr = new InputStreamReader( connection.getInputStream(), "UTF-8" );
			} catch( UnsupportedEncodingException ex) {
				isr = new InputStreamReader( connection.getInputStream() );
			}
			
			BufferedReader in = new BufferedReader( isr );
			String inputLine;

			output = "";

			while ( ( inputLine = in.readLine() ) != null )
				output += inputLine + "\n";
			in.close();
		} catch ( IOException e ) {
			//e.printStackTrace();
		    throw new FreedbException( "Error while trying read data from freedb server, "+e.getMessage()+". Check your internet connection settings." );
		}
		assert output != null;

		//Preliminary freedb error check, error codes 4xx and 5xx indicate an error
		if ( output.startsWith( "4" ) || output.startsWith( "5" ) )
			throw new FreedbException( "Freedb server returned an error: \""+output+"\"" );

		return output;
	}
    
    private void setupProxy(URLConnection connection) {
    	/* Set the proxy server host and port */
		if ((this.settings.getInetConn()== FreedbSettings.INETCONN_PROXY_WITH_AUTHENTICATION) ) {
			String proxyUPB64=base64Encode(this.settings.getProxyUser()+":"+this.settings.getProxyPass());
			//System.out.println(proxyUPB64);
			connection.setRequestProperty("Proxy-Authorization", "Basic " + proxyUPB64);	    
		}
    }
    
    private void setupConnection() {
    	if (this.settings.getInetConn()!= FreedbSettings.INETCONN_DIRECT) {
			System.setProperty("http.proxyHost", this.settings.getProxyHost());
			System.setProperty("http.proxyPort", this.settings.getProxyPort());
			//System.out.println("Connecting through proxy:"+ this.settings.getProxyHost()+":"+ this.settings.getProxyPort());
		} else {
			System.getProperties().remove("http.proxyHost");
			System.getProperties().remove("http.proxyPort");
			//System.out.println("Connecting direct");
		}
    }
    
    private String getQueryCommand(FreedbAlbum album) {
        StringBuffer command = new StringBuffer("cddb+query+");

        command.append( album.getDiscId() ).append( "+" );
        
        int tracks = album.getTracksNumber();
        command.append( tracks ).append( "+" );

        int[] trackOffsets = album.getTrackOffsets();

		for ( int j = 0; j < trackOffsets.length - 1; j++ )
		    command.append(trackOffsets[j]).append( "+" );

		command.append((trackOffsets[tracks]-trackOffsets[0]) /75);
		return command.toString();
    }
    
    private String getReadCommand(FreedbQueryResult query) {
		return "cddb+read+" + query.getCategory() + "+" + query.getDiscId();
	}
	
	private String getReadCommand(String genre, String id) {
		return "cddb+read+" + genre + "+" + id;
	}
	
	public String[] getAvailableServers() throws FreedbException {
	    String answer = askFreedb("sites");
	    	    
	    StringTokenizer st = new StringTokenizer(answer, "\n");
	    
	    List l = new LinkedList();
	    while(st.hasMoreTokens()) {
	        String line = st.nextToken();
	        if(!line.startsWith("2") && !line.startsWith(".") ) {
	            String[] sline = line.split(" ", 7);
	            if(sline[1].equals("http"))
	                l.add( sline[0] );
	        }
	    }
	    
	    String[] servers = new String[l.size()];
	    Iterator it = l.iterator();
	    int i = 0;
	    while(it.hasNext()) {
	        servers[i] = (String) it.next();
	        i++;
	    }
	    return servers;
	}
    
    public FreedbQueryResult[] query(FreedbAlbum album) throws FreedbException {
        //Create the command to be sent to freedb
        String command = getQueryCommand(album);
        
        //Send the command, and read the answer
        String queryAnswer = askFreedb(command);
		
        //Parse the result
        StringTokenizer st = new StringTokenizer( queryAnswer, "\n" );

		String[] answers = new String[st.countTokens()];

		for ( int i = 0; i < answers.length; i++ )
			answers[i] = st.nextToken();
		
		if ( queryAnswer.startsWith( "200" ) )  //Exact Match
			return new FreedbQueryResult[]{new FreedbQueryResult( answers[0].substring( 4 ), true )};
		else if ( !queryAnswer.startsWith( "202" ) ) {  //RESULTS
			FreedbQueryResult[] queryResults = new FreedbQueryResult[answers.length - 2];
			
			boolean exact = queryAnswer.startsWith( "210" );
			
			for ( int i = 0; i < queryResults.length; i++ ) {
				queryResults[i] = new FreedbQueryResult( answers[i + 1], exact );
			}
			
			return queryResults;
		}
		else
			throw new FreedbException("Server returned : "+queryAnswer);
    }
    
    public FreedbQueryResult[] query(FreedbTrack[] tracks) throws FreedbException {
        return query(new FreedbAlbum(tracks));
    }
    
    public FreedbQueryResult[] query(float[] times) throws FreedbException {
        SimpleTrack[] tracks = new SimpleTrack[times.length];
        for(int i = 0; i< tracks.length; i++)
            tracks[i] = new SimpleTrack(times[i]);
        return query(tracks);
    }
    
    public FreedbQueryResult[] query(int[] times) throws FreedbException {
    	float[] translated = new float[times.length];
    	for (int i = 0; i < translated.length; i++) {
    		translated[i] = times[i];
    	}
    	return query(translated);
    }
    
    private boolean bigMatch(String line, String[] infos) {
        Matcher matcher = Pattern.compile(".+?php\\?cat=(.+?)&id=(.+?)\">(.+?) \\/ (.+?)<\\/(.*)").matcher(line);
		
		if (matcher.matches()) {
		    infos[0] = matcher.group(1);
			infos[1] = matcher.group(2);
			infos[2] = matcher.group(3);
			infos[2] += " / "+matcher.group(4);
			infos[3] = matcher.group(5);
		}
		
		return matcher.matches();
    }
    
    private boolean smallMatch(String line, String[] infos) {
        Matcher matcher = Pattern.compile(".+?php\\?cat=(.+?)&id=(.+?)\">.*<\\/.*").matcher(line);
		
        if (matcher.matches()) {
		    infos[0] = matcher.group(1);
			infos[1] = matcher.group(2);
		}
		
		return matcher.matches();
	}
    
    public FreedbQueryResult[] query(String search) throws FreedbException {
        //Query the site parse the resulting html, and create appropriate queryresults
        //URL of the search page is like
        //http://www.freedb.org/freedb_search.php? invariant
        //words=bela+fleck   the search terms spaces are escaped (accented letters are not permitted)
        //&allfields=NO      Search in all fields ?
        //&fields=artist     Search in artists
        //&fields=title      Search in albums
        //&allcats=YES       Search in All categories ?
        //&grouping=none     Group the results in a single list or in genres
        //&x=0               ??(not needed)
        //&y=0               ??(not needed)
        //TODO Maybe add options to choose the settings, for the moment default are used
        String answer = searchFreedb(search);
        
        StringTokenizer st = new StringTokenizer(answer, "\n");
        //The resulting html is like:
        //<tr><td><a href="http://www.freedb.org/freedb_search_fmt.php?cat=rock&id=4910fa07">Bela Fleck / 2/16/2000 2/2</a><br><br></tr>
        //When there is one match or
        //<tr><td><a href="http://www.freedb.org/freedb_search_fmt.php?cat=misc&id=f30f6612">Bela Fleck / Daybreak</a><br><a href="http://www.freedb.org/freedb_search_fmt.php?cat=jazz&id=fc0f6712"><font size=-1>2</font></a>&nbsp;<br><br></tr>
        //When there are more albums with the same title
        LinkedList list = new LinkedList();
        String[] infos = new String[4];
        while(st.hasMoreTokens()) {
            String line = st.nextToken();
                        
            if(bigMatch(line, infos)) { //We have a line that has an artist/album
                list.add(new FreedbQueryResult(infos[0]+" "+infos[1]+" "+infos[2]));
                if(smallMatch(infos[3], infos)) //Do we have a number 2 of the same album/artist ?
                    list.add(new FreedbQueryResult(infos[0]+" "+infos[1]+" "+infos[2]));
            }
            else if(smallMatch(line, infos)) //We have a line that is number 3,4 of the previous artist/album
                list.add(new FreedbQueryResult(infos[0]+" "+infos[1]+" "+infos[2]));
        }
        
        FreedbQueryResult[] results = new FreedbQueryResult[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while(it.hasNext()) {
            results[i] = (FreedbQueryResult) it.next();
            i++;
        }
        
        return results;
    }
        
    public FreedbReadResult read(FreedbQueryResult query) throws FreedbException {
        //Create the command to be sent to freedb
        String command = getReadCommand(query);
        
        //Send the command, and read the answer
        String queryAnswer = askFreedb(command);
		
        //Parse the result
        return new FreedbReadResult(queryAnswer, query.isExactMatch());
    }

    public FreedbReadResult read(String genre, String id) throws FreedbException {
        //Create the command to be sent to freedb
        String command = getReadCommand(genre, id);
        
        //Send the command, and read the answer
        String queryAnswer = askFreedb(command);
        
        //Parse the result
        return new FreedbReadResult(queryAnswer, genre);
    }

/**
 * Encode a String in Base64
 * @param whatToEncode
 * @return the encoded string
 */
    
public String base64Encode(String whatToEncode) {
	StringBuffer salida =new StringBuffer();
	//System.out.println("Encoding:"+whatToEncode);
	//System.out.println("length:"+whatToEncode.length());

	for (int i =0;i<whatToEncode.length();i+=3) {
		//System.out.println("Starting at:"+i);
		salida.append(encode3(whatToEncode,i));
	}
    	
	return salida.toString();
    	
    	
}
/**
 * Returns a 4 character string that corresponds to the codification of 3
 * characters of whatToEncode, starting at whereToStart.  Does padding when needed
 * @param whatToEncode
 * @param whereToStart
 * @return the char encoded
 */
private String encode3(String whatToEncode,int whereToStart) {
		String map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		// last input char
		int finish=whereToStart+2;
		// number of 0 input bytes to use for padding
		int pads=finish - whatToEncode.length()+1;
    		
		if (pads > 2) return null;
		//System.out.println("Pads:"+pads);
		char i1=whatToEncode.charAt(whereToStart);
		char i2=0;
		char i3=0;
		if (pads<2) {
			i2=whatToEncode.charAt(whereToStart+1);
			if (pads<1)
				i3=whatToEncode.charAt(whereToStart+2);
		}
    		
    		
		int o1,o2,o3,o4;
		// convert 3*8=24 bits  in 4 numbers of 6 bits each
		// but if padding produces  000000 numbers, 64 is used instead
		o1= i1 >> 2;
		o2=  ((i1 & 3 ) << 4) | (i2  >> 4);
		o3= (pads==2)?
				64:
				( (i2 & 0x0F) << 2 ) | ( i3 >> 6 );
		o4= (pads>0) ?
				64:
				(i3 & 0x3F);
		// return the ascii character that corresponds to each 6 bit number
			return map.substring(o1,o1+1)+map.substring(o2,o2+1) +map.substring(o3,o3+1) +map.substring(o4,o4+1) ;
	}
    
}
