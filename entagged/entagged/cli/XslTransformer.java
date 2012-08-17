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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import entagged.listing.xml.TransformTarget;

/**
 * This class provides the functionality to transform an entagged xml report to
 * another rendition. <br>
 * 
 * @author Christian Laireiter
 */
public class XslTransformer {

    /**
     * This field is internally used to store the defined targets at: <br>
     * 
     * <pre>
     * &quot;entagged.listing.xml.resource.xsltargets.properties&quot;
     * </pre>
     * 
     * <br>
     * A copy could be retrieved by {@link #getTransformTargets()}
     */
    private static ArrayList TRANSFORM_TARGETS = null;

    /**
     * This method returns all defined {@link TransformTarget}in the file: <br>
     * 
     * <pre>
     * &quot;entagged.listing.xml.resource.xsltargets.properties&quot;
     * </pre>
     * 
     * <br>
     * 
     * @return A Colleaction with TransformTargets
     */
    public static Collection getTransformTargets() {
        if (XslTransformer.TRANSFORM_TARGETS == null) {
            Properties properties = new Properties();
            XslTransformer.TRANSFORM_TARGETS = new ArrayList();
            try {
                properties
                        .load(XslTransformer.class
                                .getClassLoader()
                                .getResourceAsStream(
                                        "entagged/listing/xml/resource/xsltargets.properties"));

                HashMap type2lang = new HashMap();
                // Now process the transform target definitions
                Enumeration enumeration = properties.keys();
                while (enumeration.hasMoreElements()) {
                    String currentKey = (String) enumeration.nextElement();
                    String value = properties.getProperty(currentKey);
                    String[] keyStruct = currentKey.split("\\.");
                    String type = keyStruct[0];
                    String lang = keyStruct[1];
                    HashMap lang2object = (HashMap) type2lang.get(type);
                    if (lang2object == null) {
                        lang2object = new HashMap();
                        type2lang.put(type, lang2object);
                    }
                    TransformTarget target = (TransformTarget) lang2object
                            .get(lang);
                    if (target == null) {
                        target = new TransformTarget(lang, type);
                        lang2object.put(lang, target);
                    }
                    if (keyStruct.length == 3) {
                        target.setDescription(value);
                    } else {
                        target.setXslFilename(value);
                    }
                }
                // Reordering to Array
                Iterator iterator = type2lang.values().iterator();
                while (iterator.hasNext()) {
                    Iterator objects = ((HashMap) iterator.next()).values()
                            .iterator();
                    while (objects.hasNext()) {
                        XslTransformer.TRANSFORM_TARGETS.add(objects.next());
                    }
                }
                Object[] tmp = XslTransformer.TRANSFORM_TARGETS.toArray();
                Arrays.sort(tmp);
                XslTransformer.TRANSFORM_TARGETS.clear();
                XslTransformer.TRANSFORM_TARGETS.addAll(Arrays.asList(tmp));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList(XslTransformer.TRANSFORM_TARGETS);
    }

    /**
     * Entry point of the conversion application. <br>
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            boolean helpRequested = false;
            boolean listRequested = false;
            ArrayList instructions = new ArrayList();
            /*
             * Parse the arguments
             */
            for (int i = 0; i < args.length && !helpRequested; i++) {
                if (args[i].equals("--help")) {
                    helpRequested = true;
                } else if (args[i].endsWith("--list")) {
                    listRequested = true;
                } else {
                    instructions.add(args[i].trim());
                }
            }
            if (helpRequested) {
                printUsage();
            } else if (listRequested) {
                // List all available transformation targets.
                Iterator iterator = getTransformTargets().iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            } else {
                // Arguments for transformation must be 4
                if (instructions.size() != 4) {
                    printUsage();
                    return;
                }
                String type = (String) instructions.get(0);
                String lang = (String) instructions.get(1);
                String source = (String) instructions.get(2);
                String target = (String) instructions.get(3);

                // Search the TransformTarget
                TransformTarget transformTarget = null;
                Iterator it = getTransformTargets().iterator();
                while (it.hasNext() && transformTarget == null) {
                    TransformTarget candidate = (TransformTarget) it.next();
                    if (candidate.getType().equals(type)
                            && candidate.getLanguage().equals(lang)) {
                        transformTarget = candidate;
                    }
                }

                // If no transformationTarget found, print the error and exit.
                if (transformTarget == null) {
                    System.err.println("Transformation for type \"" + target
                            + "\" and language \"" + lang
                            + "\" is not available. Use the option --list.");
                    return;
                }

                // Check the source and the target.
                File sourceFile = new File(source);
                if (!sourceFile.exists() || !sourceFile.canRead()) {
                    System.err.println("Source file \"" + source
                            + "\" doesn't exist or can't read");
                    return;
                }

                File targetFile = new File(target);
                if (targetFile.exists() && !targetFile.canWrite()) {
                    System.err.println("Can't write to specified target \""
                            + target + "\"");
                    return;
                }

                FileInputStream sourceStream = new FileInputStream(sourceFile);
                FileOutputStream targetStream = new FileOutputStream(targetFile);
                process(sourceStream, targetStream, transformTarget);

                sourceStream.close();
                targetStream.flush();
                targetStream.close();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method prints the usage information of this applicaton.
     */
    private static void printUsage() {
        System.out.println("Entagged XslTransformer help:");
        System.out.println();
        System.out
                .println("java entagged.cli.XslTransformer [--help] [--list] "
                        + "<target> <language> <sourcefile> <targetfile>");
        System.out.println();
        System.out.println("   --help: prints this help");
        System.out.println();
        System.out.println("   --list: prints all available transformations");
        System.out.println();
        System.out.println("   target: the type of the target (see --list)");
        System.out.println();
        System.out
                .println("   language: the language of the target (see --list)");
        System.out.println();
        System.out.println("   sourcefile: the xml listing");
        System.out.println();
        System.out
                .println("   target: full path (including extension) of the destination.");
    }

    /**
     * This method transforms the xml file <code>source</code> with the method
     * of <code>transformTarget</code> and palcing the result in the file
     * <code>targetFile</code>.
     * 
     * @param input
     *                  XML listing like {@link entagged.cli.XmlListingGenerator}.
     *                  <br>
     * @param output
     *                  Where to write thre reulst.
     * @param transformTarget
     *                  Transformation description.
     * @throws TransformerException
     */
    public static void process(InputStream input, OutputStream output,
            TransformTarget transformTarget) throws TransformerException {

        InputStream xsl = XslTransformer.class
                .getResourceAsStream("/entagged/listing/xml/resource/"
                        + transformTarget.getXslFilename());

        TransformerFactory tf = TransformerFactory.newInstance();

        StreamSource source = new StreamSource(xsl);
        Templates templates = tf.newTemplates(source);

        Transformer trf = templates.newTransformer();

        trf.transform(new StreamSource(input), new StreamResult(output));
    }
}