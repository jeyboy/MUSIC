package tabber;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class TabberUI extends BasicTabbedPaneUI {

    private Color selectColor, selectColor2;
    private Color deSelectColor, deSelectColor2;
    private int inclTab = 4;
    private int anchoFocoV = -12;
    private int anchoFocoH = -12;
    private int anchoCarpetas = 18;
    private Polygon shape;

    public static ComponentUI createUI(JComponent c) {
        return new TabberUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        selectColor = new Color(0, 191, 255);
        selectColor2 = new Color(228, 228, 228);
        
        deSelectColor = new Color(96, 123, 139);
        deSelectColor2 = new Color(128, 128, 128);
        
        tabAreaInsets.right = anchoCarpetas;
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        if (runCount > 1) {
        	
            int lines[] = new int[runCount];
            for (int i = 0; i < runCount; i++) {
                lines[i] = rects[tabRuns[i]].y + (tabPlacement == TOP ? maxTabHeight : 0);
            }
            Arrays.sort(lines);
            if (tabPlacement == TOP) {
                int fila = runCount;
                for (int i = 0; i < lines.length - 1; i++, fila--) {
                    Polygon carp = new Polygon();
                    carp.addPoint(0, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 2, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i] + 3);
                    if (i < lines.length - 2) {
                        carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i + 1]);
                        carp.addPoint(0, lines[i + 1]);
                    } else {
                        carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i] + rects[selectedIndex].height);
                        carp.addPoint(0, lines[i] + rects[selectedIndex].height);
                    }
                    carp.addPoint(0, lines[i]);
                    g.setColor(hazAlfa(fila));
                    g.fillPolygon(carp);
                    g.setColor(darkShadow.darker());
                    g.drawPolygon(carp);
                }
            } else {
                int fila = 0;
                for (int i = 0; i < lines.length - 1; i++, fila++) {
                    Polygon carp = new Polygon();
                    carp.addPoint(0, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 1, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 1, lines[i + 1] - 3);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 3, lines[i + 1]);
                    carp.addPoint(0, lines[i + 1]);
                    carp.addPoint(0, lines[i]);
                    g.setColor(hazAlfa(fila + 2));
                    g.fillPolygon(carp);
                    g.setColor(darkShadow.darker());
                    g.drawPolygon(carp);
                }
            }
        }
        super.paintTabArea(g, tabPlacement, selectedIndex);
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
                GradientPaint gradientShadowTmp = new GradientPaint(0, 0, deSelectColor, 0, y + 15 + h / 2, deSelectColor2);
                g2D.setPaint(gradientShadowTmp);
            }
        }

        g2D.fill(shape);
//        g2D.draw(shape);
//        if (runCount > 1) {
//            g2D.setColor(hazAlfa(getRunForTab(tabPane.getTabCount(), tabIndex) - 1));
//            g2D.fill(shape);
//        }
//        g2D.fill(shape);
    }

//    @Override
//    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
//  		tabPane.setForegroundAt(tabIndex, isSelected ? Color.black : Color.white);
//        super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
//        System.out.println("draw");
//        g.setFont(font);
//        View v = getTextViewForTab(tabIndex);
//        if (v != null) {
//            // html
//            v.paint(g, textRect);
//        } else {
//            // plain text
//            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
//            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
//                g.setColor(tabPane.getForegroundAt(tabIndex));
//                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//            } else { // tab disabled
//                g.setColor(Color.WHITE);
//                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
//                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
//            }
//        }
//    }
    /*protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
    g.setFont(font);
    View v = getTextViewForTab(tabIndex);
    if (v != null) {
    // html
    v.paint(g, textRect);
    } else {
    // plain text
    int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

    if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
    Color fg = tabPane.getForegroundAt(tabIndex);
    if (isSelected && (fg instanceof UIResource)) {
    Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
    if (selectedFG != null) {
    fg = selectedFG;
    }
    }
    g.setColor(fg);
    SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());

    } else { // tab disabled
    //PAY ATTENTION TO HERE
    g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
    SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
    g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
    SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex,
    textRect.x - 1, textRect.y + metrics.getAscent() - 1);
    }
    }
    }*/

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

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        if (tabPane.hasFocus() && isSelected) {
            g.setColor(UIManager.getColor("ScrollBar.thumbShadow"));
            g.drawPolygon(shape);
        }
    }

    protected Color hazAlfa(int fila) {
        int alfa = 0;
        if (fila >= 0) {
            alfa = 50 + (fila > 7 ? 70 : 10 * fila);
        }
        return new Color(0, 0, 0, alfa);
    }
}
