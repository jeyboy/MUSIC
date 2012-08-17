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
package entagged.cli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import entagged.listing.xml.XmlCreator;

/**
 * This class provides a command line application which creates a xml report
 * containing all the available tag information stored within files contained in
 * a given directory. <br>
 * 
 * 
 * @author Christian Laireiter
 */
public class XmlListingGenerator extends ListingGenerator {
	/**
	 * This method prints the banner of the listing generator.
	 *  
	 */
	protected static void banner() {
		System.out
				.println("-------------Entagged Audiofiles XML Listing Generator----------");
	}

//	/**
//	 * This method copies the dtd file for the generated xml listing to the path
//	 * where outfile is located. <br>
//	 * 
//	 * @param outfile
//	 *                  output file.
//	 * @throws IOException
//	 */
//	private static void copyDtd(File outfile) throws IOException {
//		File dir = outfile.getParentFile();
//		File dtdFile = new File(dir, "entagged_listing.dtd");
//		byte[] buffer = new byte[8192];
//		int read = -1;
//		InputStream input = XmlListingGenerator.class.getClassLoader()
//				.getResourceAsStream(
//						"entagged/listing/xml/resource/entagged_listing.dtd");
//		FileOutputStream fos = new FileOutputStream(dtdFile);
//		while ((read = input.read(buffer)) != -1) {
//			fos.write(buffer, 0, read);
//		}
//		fos.flush();
//		fos.close();
//	}

	/**
	 * Application entry point. <br>
	 * 
	 * @param args
	 *                  1: "-v" (optional) for verbose mode. <br>
	 *                  2: The directory of which to create a listing. <br>
	 *                  3: the filepath of the xml file. <br>
	 */
	public static void main(String[] args) {
		banner();

		if (args.length == 0) {
			usage();
			return;
		}

		if (args.length != 2 && args.length != 3) {
			usage();
			System.out.println("There are no two arguments !");
			return;
		}

		boolean verbose = false;
		if (args[0].equals("-v"))
			verbose = true;

		File dir = new File(args[verbose ? 1 : 0]);
		if (!dir.exists() || !dir.isDirectory()) {
			usage();
			System.out.println("The root folder " + dir
					+ " does not exist, or is not a folder !");
			return;
		}

		File outfile = new File(args[verbose ? 2 : 1]);

		XmlListingGenerator lg = new XmlListingGenerator();
		lg.setVerbose(verbose);
		System.out.println("Starting listing generation...");

		String xmlContent = lg.generateListing(dir).toString();

		try {
			PrintWriter fw = new PrintWriter(new FileWriter(outfile));
			fw.println(xmlContent);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot Save to output file " + outfile
					+ " , make sure it can be created and written to !");
			return;
		}

		if (verbose) {
			System.out.println("Copying dtd file");
		}
//		try {
//			copyDtd(outfile);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}

		System.out.println("Exiting Successfully...");
	}

	/**
	 * This method prints the usage information for the listing generator to the
	 * stdout. <br>
	 *  
	 */
	protected static void usage() {
		System.out
				.println("Usage: $ java entagged.cli.XmlListingGenerator [-v] ROOTDIR OUTFILE");
		System.out
				.println("      Where -v : means that you want verbosity, if not, do not write it");
		System.out
				.println("      Where ROOTDIR : the root directory for wich you want to list contents");
		System.out
				.println("      Where OUTFILE : the output file for the xml listing");
	}

	/**
	 * Creates an instance.
	 *  
	 */
	public XmlListingGenerator() {
		listingCreator = new XmlCreator();
	}
}