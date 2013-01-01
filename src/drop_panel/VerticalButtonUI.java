package drop_panel;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

public class VerticalButtonUI extends BasicButtonUI {
	 
    protected int angle;
 
    public VerticalButtonUI(int angle) {
        super();
        this.angle = angle;
    }
 
    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        return new Dimension( dim.height, dim.width );
    }

    private static Insets paintViewInsets;
 
    @Override
    public void paint(Graphics g, JComponent c) {
        JButton button = (JButton)c;
        String text = button.getText();
        Icon icon = (button.isEnabled()) ? button.getIcon() : button.getDisabledIcon();
 
        if ((icon == null) && (text == null)) return;
  
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform tr = g2.getTransform();
 
        if (icon != null)
            icon.paintIcon(c, g, 0, 0);
 
        if (text != null) {
        	FontMetrics fm = g.getFontMetrics();
        	
            if (angle == 90) {
                g2.rotate( Math.PI / 2 );
                g2.translate( 0, - c.getWidth() );
            }
            else if (angle == 270) {
                g2.rotate( - Math.PI / 2 );
                g2.translate( - c.getHeight(), 0 );
            }        	
            
            paintViewInsets = c.getInsets(paintViewInsets);
            Rectangle paintViewR = new Rectangle(
            		c.getHeight()/2 - (int)fm.getStringBounds(text, g).getWidth()/2,
            		c.getWidth()/2 - (int)fm.getStringBounds(text, g).getHeight()/2,
            		c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom),
            		c.getWidth() - (paintViewInsets.left + paintViewInsets.right)
            );            
        	
            paintText(g, c, new Rectangle(paintViewR.x, paintViewR.y, 0, fm.getAscent()), text);
        }
 
        g2.setTransform( tr );
    }
}
