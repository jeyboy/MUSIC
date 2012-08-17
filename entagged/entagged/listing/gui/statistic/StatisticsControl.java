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
package entagged.listing.gui.statistic;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import entagged.listing.ListingProcessor;
import entagged.listing.gui.ListingProgressDialog;
import entagged.listing.statistics.Statistic;
import entagged.listing.statistics.StatisticsCollector;
import entagged.tageditor.resources.LangageManager;

/**
 * 
 * @author Christian Laireiter (liree)
 */
public class StatisticsControl implements Runnable {

    /**
     * The frame which is the parent of the dialog that will be displayed during
     * processign.
     */
    private JFrame dialogParent;

    /**
     * Stores the files which should be processed.
     */
    private File[] files;

    /**
     * Creates an instance.
     * 
     * @param selection
     *                   The files which should be scanned. (Directories all
     *                   recognized).
     * @param parent
     *                   The frame which is the parent of the dialog that will be
     *                   displayed during processign.
     */
    public StatisticsControl(File[] selection, JFrame parent) {
        this.files = selection;
        this.dialogParent = parent;
    }

    /**
     * (overridden)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        StatisticsCollector collector = new StatisticsCollector();
        final ListingProgressDialog lpd = new ListingProgressDialog();
		// Center dialog.
		lpd.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lpd.setVisible(true);
            }
        });
        ListingProcessor processor = new ListingProcessor(collector, files);
        processor.setListingProgressListener(lpd);

        try {
            processor.start().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lpd.dispose();
        if (!lpd.abort()) {
            JDialog dialog = new JDialog(dialogParent, LangageManager
                    .getProperty("statistic.statistic"));
            JTabbedPane tab = new JTabbedPane();
            SummaryPanel panel = new SummaryPanel();
            panel.getValidFileCountLabel().setText(
                    collector.getStatistic().getValidFileCount() + "");
            panel.getInvalidFileCountLabel().setText(
                    collector.getStatistic().getInvalidFileCount() + "");
            panel.getTotalMusicTimeLabel().setText(
                    collector.getStatistic().getTotalDuration());
            panel.getTotalFileSize().setText(
                    collector.getStatistic().getTotalFileSize());
            tab.addTab(LangageManager
                    .getProperty("statistic.summary"), panel);
            tab.addTab(LangageManager
                    .getProperty("statistic.codecs"), CategoryPanelCreator.createFor(collector
                    .getStatistic(), Statistic.MAP_CODEC, LangageManager
                    .getProperty("statistic.categorydescription.codec")));
            tab.addTab(LangageManager
                    .getProperty("statistic.bitrate"), CategoryPanelCreator
                    .createBitratePanel(collector.getStatistic()));
            tab.addTab(LangageManager
                    .getProperty("statistic.sampling"), CategoryPanelCreator.createFor(collector
                    .getStatistic(), Statistic.MAP_SAMPLING, LangageManager
                    .getProperty("statistic.categorydescription.sampling")));
            dialog.getContentPane().add(tab);
            dialog.pack();
			// Center dialog.
			dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    /**
     * Starts the processing of the selection. <br>
     * 
     * @return The thread where the reading is done.
     */
    public Thread start() {
        Thread result = new Thread(this);
        result.start();
        return result;
    }

}
