package torrent_window;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TorrentModel extends AbstractTableModel {
	private static final long serialVersionUID = 4461978199254506038L;
	
	private String[] columnNames = {
			"Name",
            "Progress",
            "Down speed",
            ""
            };
    private Vector<TorrentRow> data = new Vector<TorrentRow>();

    public int getColumnCount() { return columnNames.length; }
    public int getRowCount() { return data.size(); }

    public String getColumnName(int col) { return columnNames[col]; }
    public Object getValueAt(int row, int col) {
    	switch(col) {
    		case 0: return data.elementAt(row).Name();
    		case 1: return data.elementAt(row).Progress();
    		case 2: return data.elementAt(row).DownSpeed();
    		default: return ""; 
    	}
    }
    public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }

    public boolean isCellEditable(int row, int col) { return false; }
    public void setValueAt(Object value, int row, int col) {
//        data[row] = value;
//        fireTableCellUpdated(row, col);
    }
    
    public void AddTorrent(TorrentRow newRow) {
    	data.add(newRow);
    	fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
    public int FindRow(TorrentRow row) { return data.indexOf(row); }
    public void UpdateRow(TorrentRow row) {
    	int index = FindRow(row);
    	fireTableRowsUpdated(index, index);
    }
}