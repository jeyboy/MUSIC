package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import service.Common;

class SliderUI extends BasicSliderUI {
    public SliderUI(JSlider slider) {
        super(slider);
        slider.setBackground(Common.color_background);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
//		slider.setExtent(extent)
    }

    @Override
    public void paintTrack(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);     	
    	g2d.setColor(Color.DARK_GRAY);
    	g2d.fillRoundRect(contentRect.x, contentRect.y, contentRect.width, contentRect.height, 15, 15);
    }

    @Override
    public void paintThumb(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);    	
    	
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.fillRoundRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, 15, 15);    	
    }
    
    public void paintFocus(Graphics g) {}    
}
