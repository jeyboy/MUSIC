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
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

/**
 * With this class one can easily define the focus traverse order or a container
 * (JPanel). <br>
 * 
 * @author Christian Laireiter
 */
public class SimpleFocusPolicy extends FocusTraversalPolicy {

    /**
     * This field holds the {@link JComponent}objects which are handled by this
     * instance.
     */
    private List components;

    /**
     * The root of the focus cycle.
     */
    private Container rootContainer;

    /**
     * Creates the policy instance and assigns it to the given container.
     * 
     * @param root
     *                  The container which is root of curren cycle.
     */
    public SimpleFocusPolicy(Container root) {
        this.rootContainer = root;
        this.rootContainer.setFocusCycleRoot(true);
        this.rootContainer.setFocusTraversalPolicy(this);
    }

    /**
     * (overridden)
     * 
     * @see java.awt.FocusTraversalPolicy#getComponentAfter(java.awt.Container,
     *           java.awt.Component)
     */
    public Component getComponentAfter(Container focusCycleRoot,
            Component aComponent) {
        int index = components.indexOf(aComponent);
        if (index >= 0 && (focusCycleRoot == this.rootContainer)) {
            index = (index + 1) % components.size();
            return (Component) components.get(index);
        }
        return null;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container,
     *           java.awt.Component)
     */
    public Component getComponentBefore(Container focusCycleRoot,
            Component aComponent) {
        int index = components.indexOf(aComponent);
        if (index-- >= 0 && (focusCycleRoot == this.rootContainer)) {
            if (index < 0) {
                index = 0;
            }
            return (Component) components.get(index);
        }
        return null;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
     */
    public Component getDefaultComponent(Container focusCycleRoot) {
        if (focusCycleRoot == this.rootContainer)
            return (Component) components.get(0);
        return null;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
     */
    public Component getFirstComponent(Container focusCycleRoot) {
        if (focusCycleRoot == this.rootContainer)
            return getDefaultComponent(focusCycleRoot);
        return null;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
     */
    public Component getLastComponent(Container focusCycleRoot) {
        if (focusCycleRoot == this.rootContainer)
            return (Component) components.get(components.size() - 1);
        return null;
    }

    /**
     * This method sets the components which will be handled by this instance.
     * <br>
     * 
     * @param comps
     *                  The components in the traverse order.
     */
    public void setComponents(Component[] comps) {
        assert comps != null && comps.length > 0;
        this.components = Arrays.asList(comps);
    }

}