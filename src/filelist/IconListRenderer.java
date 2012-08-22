package filelist;

import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class IconListRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -1227188542632542649L;
	public Map<String, Icon> icons = new HashMap<String, Icon>();

	int left_padding = 16;
	int among_space = 1;	
	
	ListItem curr_item;
	boolean selected;
	JLabel label;	
	
	public Component getTreeCellRendererComponent(
			JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		Object temp = ((DefaultMutableTreeNode) value).getUserObject();
		if (temp instanceof ListItem) {
			curr_item = (ListItem)temp;
			this.selected = selected;
		
			Icon icon = icons.get(curr_item.ext);
			if (icon == null) icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(service.Settings.imagepath + "items/default.png"));
			label.setIcon(icon);
		}
	  
	  return label; 
	}
	
	protected void paintComponent( Graphics g ) 
	{
		if (curr_item != null) {
		
			Graphics2D g2d = (Graphics2D)g.create(0, 0, label.getWidth(), label.getHeight());
			Rectangle r = new Rectangle(0, 0, label.getWidth(), label.getHeight());//g.getClipBounds(); //label.getBounds();//
			GradientPaint gp = null;
			
			if (selected) {
				gp = new GradientPaint(
					    0, 0, FileListConst.selected_grad[0],
					    0, r.height, FileListConst.selected_grad[1] );			
				
	//			switch (curr_item.state) {
	//				case LIKED :
	//					gp = new GradientPaint(
	//						    0, 0, FileListConst.droped_grad[0],
	//						    0, r.height, FileListConst.droped_grad[1] );
	//					break;
	//				case LISTENED :
	//					gp = new GradientPaint(
	//						    0, 0, FileListConst.listened_grad[0],
	//						    0, r.height, FileListConst.listened_grad[1] );
	//					break;
	//				case PLAYED:
	//					gp = new GradientPaint(
	//						    0, 0, FileListConst.played_grad[0],
	//						    0, r.height, FileListConst.played_grad[1] );
	//					break;
	//				case NONE:
	//					gp = new GradientPaint(
	//						    0, 0, FileListConst.usual_grad[0],
	//						    0, r.height, FileListConst.usual_grad[1] );
	//					break;					
	//			}
			}
			else {
				switch (curr_item.state) {
					case LIKED :
						gp = new GradientPaint(
							    0, 0, FileListConst.droped_grad[0],
							    0, r.height, FileListConst.droped_grad[1] );
						break;
					case LISTENED :
						gp = new GradientPaint(
							    0, 0, FileListConst.listened_grad[0],
							    0, r.height, FileListConst.listened_grad[1] );
						break;
					case PLAYED:
						gp = new GradientPaint(
							    0, 0, FileListConst.played_grad[0],
							    0, r.height, FileListConst.played_grad[1] );
						break;
					case NONE:
						gp = new GradientPaint(
							    0, 0, FileListConst.usual_grad[0],
							    0, r.height, FileListConst.usual_grad[1] );
						break;										
				}
			}		
			
			if (gp != null) {
				g2d.setPaint(gp);
				g2d.fillRoundRect(r.x + left_padding, r.y + among_space, r.width - left_padding, r.height - among_space * 2, 10, 30);
	//			g2d.setColor(Color.black);
	//			g2d.setStroke(new BasicStroke(1.5F));
	//			g2d.drawRoundRect(r.x + left_padding, r.y + among_space, r.width - left_padding, r.height - among_space * 2, 10, 30);
			}
		}
		
	    setOpaque( false );
	    super.paintComponent(g);
	    setOpaque( true );
	}	
}