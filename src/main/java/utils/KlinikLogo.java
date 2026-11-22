package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class KlinikLogo {

    private static BufferedImage cache = null;
    private static boolean tried = false;

    public static BufferedImage loadRaw() {
        if (tried) return cache;
        tried = true;
        String[] paths = { "/logo/logo.png", "/logo.png" };
        for (String p : paths) {
            try (InputStream in = KlinikLogo.class.getResourceAsStream(p)) {
                if (in != null) {
                    cache = ImageIO.read(in);
                    if (cache != null) {
                        System.out.println("Logo dimuat: " + p);
                        break;
                    }
                }
            } catch (Exception e) {}
        }
        if (cache == null) System.out.println("Logo tidak ketemu /logo/logo.png, pakai fallback");
        return cache;
    }

    public static Icon icon(int size) {
        BufferedImage raw = loadRaw();
        if (raw == null) return new ToothIcon(size, Color.WHITE, new Color(18, 48, 71));
        try {
            Image sc = raw.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(sc);
        } catch (Exception e) {
            return new ToothIcon(size, Color.WHITE, new Color(18, 48, 71));
        }
    }

    public static Icon iconCircle(int size) {
        BufferedImage raw = loadRaw();
        if (raw == null) return new ToothIcon(size, Color.WHITE, new Color(18, 48, 71));
        try {
            int s = Math.min(raw.getWidth(), raw.getHeight());
            int x = (raw.getWidth() - s) / 2;
            int y = (raw.getHeight() - s) / 2;
            BufferedImage sq = raw.getSubimage(x, y, s, s);
            BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = out.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            g2.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(sq.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
            g2.setClip(null);
            g2.setColor(new Color(255, 255, 255, 230));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(1, 1, size - 2, size - 2);
            g2.dispose();
            return new ImageIcon(out);
        } catch (Exception e) {
            return new ToothIcon(size, Color.WHITE, new Color(18, 48, 71));
        }
    }

    public static Image frameImage() {
        BufferedImage raw = loadRaw();
        if (raw != null) {
            try {
                return raw.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void setFrameIcon(JFrame f) {
        try {
            Image img = frameImage();
            if (img != null) f.setIconImage(img);
        } catch (Exception e) {}
    }
}
