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
	
	JLabel GenerateCloseButton(final Tab tab) {
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
	
	void Init(Tab tab, String title) {
		parent = tab;
	    final JLabel tabCloseButton = GenerateCloseButton(parent);
	    JPanel p = new JPanel();
	    p.setOpaque(false);
	    p.setBackground(null);
	    this.counter = new JLabel();
	    this.title = new JLabel(title);
	    p.add(this.counter);
	    p.add(this.title);
	    p.add(tabCloseButton);
	    
	    parent.tabber.setTabComponentAt(parent.tabber.getTabCount()-1, p);
	    SetTitleForeground(Color.white);
	    
//	    p.addMouseListener(new MouseAdapter() {
//	    	public void mouseEntered(MouseEvent e) 	{ tabCloseButton.setVisible(true); }
//		    public void mouseExited(MouseEvent e) 	{ tabCloseButton.setVisible(false); }
//		    //temp fix - when add mouse listeners tab not selected at click
//		    public void mouseClicked(MouseEvent e) 	{ parent.tabber.SetCurrentTab(parent); }
//		});	    
	}
	
	public TabHead(Tab parent, String title) {	Init(parent, title);	}
	
	public String GetTitle() 			{ return title.getText(); }
	public void SetTitle(String text) 	{ title.setText(text); }
	String PrepareCounter(int count)	{
		return  (parent.options.remote_source ? "R" : "") +
				(parent.options.interactive ? "I" : "") +
				(parent.options.delete_files ? "D" : "") +
				"(" + count + ")"; 
	}
	public void SetCount(int count) 	{ this.counter.setText(PrepareCounter(count));	}
	public void SetStatus(final String status){
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
	public void SetTitleForeground(Color color) {
		counter.setForeground(color);
		title.setForeground(color);
	}
}
