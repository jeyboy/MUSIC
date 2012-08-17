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

import entagged.listing.Lister;
import entagged.listing.ListingProcessor;
import entagged.listing.separated.SeparatedListingCreator;

/**
 * This class represents an application for creating a file which stores the tag
 * values of the supported audio files of a given directory.
 * 
 */
public class ListingGenerator {

    /**
     * Prints a banner to Standard out.
     */
    protected static void banner() {
        System.out
                .println("-------------Entagged Audiofiles Listing Generator----------");
    }

    /**
     * Starts the listing.
     * 
     * @param args
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

        ListingGenerator lg = new ListingGenerator();
        lg.setVerbose(verbose);
        System.out.println("Starting listing generation...");

        StringBuffer sb = lg.generateListing(dir);

        try {
            PrintWriter fw = new PrintWriter(new FileWriter(outfile));
            fw.println(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot Save to output file " + outfile
                    + " , make sure it can be created and written to !");
            return;
        }

        System.out.println("Exiting Successfully...");
    }

    /**
     * This method prints the usage information for the application.
     */
    protected static void usage() {
        System.out
                .println("Usage: $ java entagged.cli.ListingGenerator [-v] ROOTDIR OUTFILE");
        System.out
                .println("      Where -v : means that you want verbosity, if not, do not write it");
        System.out
                .println("      Where ROOTDIR : the root directory for wich you want to list contents");
        System.out
                .println("      Where OUTFILE : the output file for the tab-separated listing");
    }
    /**
     * This field stores the lister. <br>
     * If used {@link #ListingGenerator()}it will be a
     * {@link SeparatedListingCreator}.<br>
     */
    protected Lister listingCreator;

    /**
     * This field stores the Processor which will read the files.
     */
    protected ListingProcessor processor;

    /**
     * This field indicates whether verbose output is done.
     */
    private boolean verbose = false;

    /**
     * Creates an instance
     */
    public ListingGenerator() {
        this.listingCreator = new SeparatedListingCreator();
    }

    /**
     * This method starts the listing creation. <br>
     * 
     * @param dir
     *                   Directory which should be processed.
     * @return A Stringbuffer containing the report.
     */
    public StringBuffer generateListing(File dir) {
        this.processor = new ListingProcessor(listingCreator,
                new File[] { dir });
        this.processor.setVerbose(this.verbose);
        this.processor.run();
        return new StringBuffer(this.listingCreator.getContent());
    }

    /**
     * This method sets the verbosity. <br>
     * 
     * @param b
     *                   <code>true</code> if detailed output should be given.
     */
    public void setVerbose(boolean b) {
        this.verbose = b;
    }
}
