package tabber;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import service.Errorist;

public class TabHead {
	JLabel title, counter;
	Tab parent;
	
	ImageIcon GetIcon() {
		try {
			return new ImageIcon(
					ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(service.Settings.imagepath + "menubar/delete_tab2.png"))
					);
		} 
		catch (IOException e1) { Errorist.printLog(e1); }
		return null; 
	}
	
	JLabel GenerateCloseButton(final Tab tab) {
		final JLabel tabCloseButton = new JLabel(GetIcon());
	    tabCloseButton.setPreferredSize(new Dimension(18, 18));
	    tabCloseButton.setVisible(false);
	    
	    tabCloseButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { tab.tabber.removeTabAt(tab.tabber.getTabCount()-1); }
            public void mouseEntered(MouseEvent e) 	{ tabCloseButton.setVisible(true); }
            public void mouseExited(MouseEvent e) 	{ tabCloseButton.setVisible(false); }            
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
	    
	    p.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) 	{ tabCloseButton.setVisible(true); }
            public void mouseExited(MouseEvent e) 	{ tabCloseButton.setVisible(false); }            
        });
	    
	    
	    parent.tabber.setTabComponentAt(parent.tabber.getTabCount()-1, p);
	    SetTitleForeground(Color.white);
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
