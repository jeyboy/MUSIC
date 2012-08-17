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
package entagged.listing.gui.tasks;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import entagged.cli.XslTransformer;
import entagged.listing.xml.TransformTarget;

/**
 * Interface for configuring the task. <br>
 * 
 * @author Christian Laireiter
 */
public class TransformPanel extends HelpReportPanel implements
        TreeSelectionListener {

    /**
     * Sorting arrays of {@link TransformTarget}:<br>
     * first: language <br>
     * second: type <br>
     * 
     * @author Christian Laireiter
     */
    private final class LangTypeComp implements Comparator {

        /**
         * (overridden)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            TransformTarget t1 = (TransformTarget) o1;
            TransformTarget t2 = (TransformTarget) o2;
            int result = t1.getLanguage().compareTo(t2.getLanguage());
            if (result == 0) {
                result = t1.getType().compareTo(t2.getType());
            }
            return result;
        }

    }

    /**
     * Displaying the tranform targets.
     * 
     * 
     * @author Christian Laireiter
     */
    private final class TreeRenderer extends DefaultTreeCellRenderer {

        /**
         * (overridden)
         * 
         * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         *           java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean focus) {
            Component result = super.getTreeCellRendererComponent(tree, value,
                    sel, expanded, leaf, row, focus);
            if (value != null) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                if (treeNode.getUserObject() != null
                        && treeNode.getUserObject() instanceof TransformTarget) {
                    this.setText(((TransformTarget) treeNode.getUserObject())
                            .getType());
                }
            }
            return result;
        }

    }

    /**
     * This field shows the description of the selected transformation target.
     */
    private JTextPane description;

    /**
     * This tree will recieve a node for each available filetype. Each filetype
     * node contains a node for the available language.
     */
    private JTree targetSelection;

    private TransformTask transformTask;

    /**
     * Creates an instance.
     * 
     * @param task
     *                  The task for which this interface is created.
     */
    public TransformPanel(TransformTask task) {
        super("entagged/listing/gui/tasks/resource/transformpanelhelp");
        this.transformTask = task;
        initialize();
    }

    /**
     * Creates the tree for selection the tranformation and inserts all choices.
     * 
     * @return tree for selection the transformation target.
     */
    private JTree createTree() {
        DefaultTreeModel treeModel = new DefaultTreeModel(
                new DefaultMutableTreeNode("root"));
        JTree result = new JTree(treeModel);
        result.addTreeSelectionListener(this);
        result.setCellRenderer(new TreeRenderer());
        result.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        HashMap lang2list = new HashMap();
        TransformTarget[] available = (TransformTarget[]) XslTransformer
                .getTransformTargets().toArray(new TransformTarget[0]);
        Arrays.sort(available, new LangTypeComp());
        for (int i = 0; i < available.length; i++) {
            String lang = available[i].getLanguage();
            ArrayList list = (ArrayList) lang2list.get(lang);
            if (list == null) {
                list = new ArrayList();
                lang2list.put(lang, list);
            }
            list.add(available[i]);
        }
        String[] languages = (String[]) lang2list.keySet().toArray(
                new String[0]);
        Arrays.sort(languages);
        TreePath tp = null;
        for (int i = 0; i < languages.length; i++) {
            DefaultMutableTreeNode langNode = new DefaultMutableTreeNode(
                    languages[i]);
            ((DefaultMutableTreeNode) result.getModel().getRoot())
                    .add(langNode);
            langNode.setAllowsChildren(true);
            TransformTarget[] targets = (TransformTarget[]) ((ArrayList) lang2list
                    .get(languages[i])).toArray(new TransformTarget[0]);
            Arrays.sort(targets);
            for (int j = 0; j < targets.length; j++) {
                DefaultMutableTreeNode tt = new DefaultMutableTreeNode(
                        targets[j]);
                langNode.add(tt);
                if (targets[j].equals(transformTask.getConfiguration()
                        .getTransformTarget())) {
                    tp = new TreePath(new Object[] {
                            result.getModel().getRoot(), langNode, tt });
                }
            }
        }
        result.setRootVisible(false);
        if (tp != null) {
            result.setSelectionPath(tp);
        } else {
            result.expandPath(new TreePath(result.getModel().getRoot()));
        }
        return result;
    }

    /**
     * Creates all Components.
     */
    private void initialize() {
        getContentPane().setLayout(new GridBagLayout());
        description = new JTextPane();
        description.setEditable(false);
        targetSelection = createTree();
        getContentPane().add(
                new JScrollPane(targetSelection),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(
                new JScrollPane(description),
                new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 75, 100));
    }

    /**
     * (overridden)
     * 
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        TransformTarget selected = null;
        if (e.getPath().getLastPathComponent() instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath()
                    .getLastPathComponent();
            if (node.getUserObject() instanceof TransformTarget) {
                selected = (TransformTarget) node.getUserObject();
                try {
                    description.getDocument().remove(0,
                            description.getDocument().getLength());
                    description.getDocument().insertString(0,
                            selected.getDescription(),
                            description.getInputAttributes());
                    System.out.println(selected.getDescription());
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        }
        transformTask.getConfiguration().setTransformTarget(selected);
        transformTask.dataUpdated();
    }
}