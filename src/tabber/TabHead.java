package tabber;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import service.Utils;

public class TabHead {
	JLabel title, counter;
	Tab parent;
	
	JLabel generateCloseButton(final Tab tab) {
		final JLabel tabCloseButton = new JLabel(Utils.GetIcon("menubar/delete_tab2.png"));
	    tabCloseButton.setPreferredSize(new Dimension(18, 18));
//	    tabCloseButton.setVisible(false);
//	    
	    tabCloseButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) 		{ tab.tabber.remove(parent); }
//            public void mouseEntered(MouseEvent e) 	{ tabCloseButton.setVisible(true); }
//            public void mouseExited(MouseEvent e) 	{ tabCloseButton.setVisible(false); }            
        });
	    return tabCloseButton;
	}	
	
	public TabHead(Tab tab, String title) {
		parent = tab;
	    final JLabel tabCloseButton = generateCloseButton(parent);
	    JPanel p = new JPanel();
	    p.setOpaque(false);
	    p.setBackground(null);
	    this.counter = new JLabel();
	    this.title = new JLabel(title);
	    p.add(this.counter);
	    p.add(this.title);
	    p.add(tabCloseButton);
	    
	    parent.tabber.setTabComponentAt(parent.tabber.getTabCount()-1, p);
	    setTitleForeground(Color.white);
	    
//	    p.addMouseListener(new MouseAdapter() {
//	    	public void mouseEntered(MouseEvent e) 	{ tabCloseButton.setVisible(true); }
//		    public void mouseExited(MouseEvent e) 	{ tabCloseButton.setVisible(false); }
//		    //temp fix - when add mouse listeners tab not selected at click
//		    public void mouseClicked(MouseEvent e) 	{ parent.tabber.currTab(parent); }
//		});	    
	}
	
	public String getTitle() 			{ return title.getText(); }
	public void setTitle(String text) 	{ title.setText(text); }
	String prepareCounter(int count)	{
		return  (parent.options.remote_source ? "R" : "") +
				(parent.options.interactive ? "I" : "") +
				(parent.options.delete_files ? "D" : "") +
				"(" + count + ")"; 
	}
	public void setCount(int count) 	{ this.counter.setText(prepareCounter(count));	}
	public void setStatus(final String status){
		if (SwingUtilities.isEventDispatchThread()){
			title.setBorder(status.isEmpty() ? null : new TitledBorder(status));
		} else {
		    SwingUtilities.invokeLater(new Runnable(){
		        public void run(){
		        	title.setBorder(status.isEmpty() ? null : new TitledBorder(status));
		        }
		    });
		}		
	}
	public void setTitleForeground(Color color) {
		counter.setForeground(color);
		title.setForeground(color);
	}
}