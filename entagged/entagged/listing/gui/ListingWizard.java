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
package entagged.listing.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import entagged.cli.XslTransformer;
import entagged.listing.ListingProcessor;
import entagged.listing.gui.tasks.ReportConfig;
import entagged.listing.gui.tasks.ReportTask;
import entagged.listing.gui.wizard.WizardControl;
import entagged.listing.gui.wizard.WizardDialog;
import entagged.tageditor.resources.LangageManager;

/**
 * This class represents the control (application) for a wizard allowing you to
 * create xml reports of a selected directory and transform them using
 * {@link entagged.cli.XslTransformer}.
 * 
 * @author Christian Laireiter
 */
public class ListingWizard implements WizardControl, Runnable {
    /**
     * Stores the dialog instance.
     */
    private WizardDialog dialog = null;

    /**
     * This is the task which will be performed.
     */
    private ReportTask reportTask;

    /**
     * Creates an instance.
     * 
     */
    public ListingWizard() {
        initialize();
    }

    /**
     * Sets the position of the given dialog in a way, that it will be displayed
     * in the middle of the screen.
     * 
     * @param toRelocate
     *                   to relocate.
     */
    private void centerDialog(JDialog toRelocate) {
        Dimension size = toRelocate.getSize();
        Rectangle maximumWindowBounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        toRelocate.setLocation((maximumWindowBounds.width - size.width) / 2
                + maximumWindowBounds.x,
                (maximumWindowBounds.height - size.height) / 2
                        + maximumWindowBounds.y);
    }

    /**
     * Creates the wizard dialog.
     */
    private void initialize() {
        this.dialog = new WizardDialog();
        this.dialog.setTitle(LangageManager.getProperty("listgen.dialogtitle"));
        this.dialog.setTask(this.reportTask = new ReportTask(this));
        this.dialog.setSize(500, 300);
        centerDialog(dialog);
        this.dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * This method starts the wizard by displaying the dialog. <br>
     * There is also started a thread to not blocking the event queue if a
     * transformation will performed.
     * 
     * @return Thread which contains the wizard.
     * 
     * @throws Exception
     *                    if exceptions occur.
     */
    public Thread launch() throws Exception {
        Thread result = new Thread(this, "Wizard");
        result.start();
        return result;
    }

    /**
     * (overridden)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        dialog.setVisible(true);
        if (!dialog.isCancelled()) {
            ReportConfig config = reportTask.getConfiguration();
            File source = new File(config.getSourceDirectory());
            File target = new File(config.getReportFile());
            ListingProcessor proc = new ListingProcessor(new File[] { source },
                    config.isRecursive(), target, config.getTransformType());
            proc.setVerbose(true);
            ListingProgressDialog lpd = new ListingProgressDialog(dialog);
            if ((config.getTransformType() & ListingProcessor.REPORT_OTHER) > 0) {
                lpd.setModal(false);
            }
            centerDialog(lpd);
            proc.setListingProgressListener(lpd);
            try {
                Thread thread = proc.start();
                lpd.setVisible(true);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (config.getTransformType() == ListingProcessor.REPORT_OTHER
                    && !lpd.abort()) {
                /*
                 * Now we need to transform.
                 */
                try {
                    lpd
                            .appendMessage("\n"
                                    + LangageManager
                                            .getProperty("listgen.transformation.start"));
                    FileOutputStream fos = new FileOutputStream(target);
                    ByteArrayInputStream bais = new ByteArrayInputStream(proc
                            .getLister().getContent().getBytes());
                    XslTransformer.process(bais, fos, config
                            .getTransformTarget());
                    fos.flush();
                    fos.close();
                    lpd.appendMessage("\n"
                            + LangageManager
                                    .getProperty("listgen.transformation.end"));
                } catch (Exception e) {
                    e.printStackTrace();
                    lpd
                            .errorOccured("\n"
                                    + LangageManager
                                            .getProperty("listgen.transformation.error")
                                    + ":" + e.getMessage());
                }
                // Because modality was not set with transformation we need to
                // until the user closes the dialog.
                while (lpd.isVisible()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        // Should not happen
                    }
                }
            }
            lpd.dispose();
            config.saveAsDefault();
        }
        dialog.dispose();
    }

    /**
     * This method overrides the source location on which the wizard should
     * work. <br>
     * Override since the configuration is restored from previous usage.
     * 
     * @param source
     *                   The new source Directory. If not a directory, this method will
     *                   do nothing.
     */
    public void setSource(File source) {
        if (source != null && source.exists() && source.isDirectory()) {
            reportTask.setSourceDirectory(source.getAbsolutePath());
        }
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardControl#updateView()
     */
    public void updateView() {
        this.dialog.updateView();
    }
}