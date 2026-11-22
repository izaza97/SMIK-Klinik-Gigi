package utils;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
public class ToothIcon implements Icon {
private int size;
private Color bg;
private Color toothColor;
public ToothIcon(int size) {
this(size, Color.WHITE, new Color(13,122,95));
}
public ToothIcon(int size, Color bg, Color toothColor) {
this.size = size;
this.bg = bg;
this.toothColor = toothColor;
}
public int getIconWidth() { return size; }
public int getIconHeight() { return size; }
public void paintIcon(Component c, Graphics g, int x, int y) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(bg);
g2.fillOval(x, y, size, size);
g2.setColor(new Color(0,0,0,25));
g2.setStroke(new BasicStroke(1f));
g2.drawOval(x, y, size-1, size-1);
double s = size / 100.0;
GeneralPath p = new GeneralPath();
p.moveTo(x+32*s, y+28*s);
p.curveTo(x+32*s, y+20*s, x+36*s, y+19*s, x+44*s, y+26*s);
p.curveTo(x+46*s, y+28*s, x+48*s, y+28*s, x+50*s, y+28*s);
p.curveTo(x+52*s, y+28*s, x+54*s, y+28*s, x+56*s, y+26*s);
p.curveTo(x+64*s, y+19*s, x+68*s, y+20*s, x+68*s, y+28*s);
p.curveTo(x+68*s, y+45*s, x+66*s, y+60*s, x+62*s, y+70*s);
p.curveTo(x+61*s, y+75*s, x+59*s, y+77*s, x+57*s, y+72*s);
p.curveTo(x+55*s, y+65*s, x+54*s, y+60*s, x+52*s, y+58*s);
p.curveTo(x+51*s, y+56*s, x+49*s, y+56*s, x+48*s, y+58*s);
p.curveTo(x+46*s, y+60*s, x+45*s, y+65*s, x+43*s, y+72*s);
p.curveTo(x+41*s, y+77*s, x+39*s, y+75*s, x+38*s, y+70*s);
p.curveTo(x+34*s, y+60*s, x+32*s, y+45*s, x+32*s, y+28*s);
p.closePath();
g2.setColor(toothColor);
g2.fill(p);
g2.setColor(new Color(255,255,255,90));
g2.fillOval((int)(x+39*s), (int)(y+30*s), (int)(7*s), (int)(13*s));
g2.dispose();
}
}