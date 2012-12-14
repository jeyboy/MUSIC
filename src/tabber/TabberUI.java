package tabber;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class TabberUI extends BasicTabbedPaneUI {

    private Color selectColor, selectColor2;
    private Color deSelectColor, deSelectColor2;
    private int inclTab = 4;
    private int anchoFocoV = -12;
    private Polygon shape;

    public static ComponentUI createUI(JComponent c) {
        return new TabberUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        selectColor = new Color(0, 191, 255);
        selectColor2 = new Color(228, 228, 228);
        
        deSelectColor = new Color(200, 200, 200);
        deSelectColor2 = new Color(0, 0, 0);
        
        tabAreaInsets.right = -6;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
    	((Tab)tabPane.getComponentAt(tabIndex)).tabhead.SetTitleForeground(isSelected ? Color.black : Color.white);
    	int xw = x + w, yh = y + h;
    	
        Graphics2D g2D = (Graphics2D) g;
        GradientPaint gradientShadow;
        int xp[] = null;
        int yp[] = null;
        switch (tabPlacement) {
            case LEFT:
                xp = new int[]{x, x, xw, xw, x};
                yp = new int[]{y, yh - 3, yh - 3, y, y};
                gradientShadow = new GradientPaint(x, y, selectColor, x, yh, selectColor2);
                break;
            case RIGHT:
                xp = new int[]{x, x, xw - 2, xw - 2, x};
                yp = new int[]{y, yh - 3, yh - 3, y, y};
                gradientShadow = new GradientPaint(x, y, selectColor, x, yh, selectColor2);
                break;
            case BOTTOM:
                xp = new int[]{x, x, x + 3, xw - inclTab - 6, xw - inclTab - 2, xw - inclTab, xw - 3, x};
                yp = new int[]{y, yh - 3, yh, yh, y + h - 1, yh - 3, y, y};
                gradientShadow = new GradientPaint(x, y, selectColor, x, yh, selectColor2);
                break;
            case TOP:
            default:
                xp = new int[]{x, x, x + 3, xw - inclTab - 6, xw - inclTab - 2, xw - inclTab, xw - inclTab, x};
                yp = new int[]{yh, y + 3, y, y, y + 1, y + 3, yh, yh};
                gradientShadow = new GradientPaint(0, 0, selectColor2, 0, y + h / 2, selectColor);
                break;
        }

        shape = new Polygon(xp, yp, xp.length);
        
        if (isSelected) {
            g2D.setColor(selectColor);
            g2D.setPaint(gradientShadow);
        } else {
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                g2D.setColor(deSelectColor);
                GradientPaint gradientShadowTmp = new GradientPaint(0, 0, deSelectColor, 0, y + h / 2, deSelectColor2);
                g2D.setPaint(gradientShadowTmp);
            } else {
                GradientPaint gradientShadowTmp = new GradientPaint(0, 0, deSelectColor, 0, y + h / 2, deSelectColor2);
                g2D.setPaint(gradientShadowTmp);
            }
        }

        g2D.fill(shape);
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics) - 24;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        if (tabPlacement == LEFT || tabPlacement == RIGHT) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        } else {
            return anchoFocoV + super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        }
    }
}
