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
package entagged.tageditor.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class Utils {

    public static Vector getColumnsInModelOrder(JTable table) {
        Vector result = new Vector();
        Enumeration myEnum = table.getColumnModel().getColumns();
        for (; myEnum.hasMoreElements();)
            result.add(myEnum.nextElement());

        // Sort the columns based on the model index
        Collections.sort(result, new Comparator() {
            public int compare(Object a, Object b) {
                TableColumn c1 = (TableColumn) a;
                TableColumn c2 = (TableColumn) b;
                if (c1.getModelIndex() < c2.getModelIndex())
                    return -1;
                else if (c1.getModelIndex() == c2.getModelIndex())
                    return 0;
                else
                    return 1;
            }
        });
        return result;
    }
}
