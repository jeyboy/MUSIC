package tabber;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import service.Errorist;

public class TabHead {
	JLabel title, counter;
	Tab parent;
	
//	String prepareTitle(String title) {
//		int add = 15 - title.length();
//		if (add <= 0) return title;
//		
//		for(int loop1=0; loop1<add; loop1++)
//			if(loop1%2 == 0)
//				title = " "+ title;
//			else title += " ";
//		return title;
//	}
	
	ImageIcon GetIcon() {
		try {
			return new ImageIcon(
					ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(service.Settings.imagepath + "menubar/delete_tab2.png"))
					);
		} 
		catch (IOException e1) { Errorist.printLog(e1); }
		return null; 
	}
	
	JButton GenerateCloseButton(final Tab tab) {
	    JButton tabCloseButton = new JButton(GetIcon());
	    tabCloseButton.setPreferredSize(new Dimension(18, 18));
	    tabCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
		          tab.tabber.removeTabAt(tab.tabber.getTabCount()-1);
		      }
		});
	    return tabCloseButton;
	}	
	
	void Init(Tab tab, String title) {
		parent = tab;
	    JButton tabCloseButton = GenerateCloseButton(parent);
	    tabCloseButton.setBorder(null);
	    JPanel p = new JPanel();
//	    p.setMaximumSize(new Dimension(500, 20));
	    p.setOpaque(false);
	    this.counter = new JLabel();
//	    this.title = new JLabel(prepareTitle(title));
	    this.title = new JLabel(title);
	    p.add(this.counter);
	    p.add(this.title);
	    p.add(tabCloseButton);
	    parent.tabber.setTabComponentAt(parent.tabber.getTabCount()-1, p);
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
}
