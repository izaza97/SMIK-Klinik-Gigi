package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Layar {

    public static void fullscreen(JFrame f) {
        if (f == null) return;
        try {
            KlinikLogo.setFrameIcon(f);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
            int minW = Math.min(1000, sc.width - 40);
            int minH = Math.min(650, sc.height - 40);
            if (minW < 800) minW = 800;
            if (minH < 500) minH = 500;
            f.setMinimumSize(new Dimension(minW, minH));
            f
                .getRootPane()
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F11"), "fs-toggle");
            f
                .getRootPane()
                .getActionMap()
                .put(
                    "fs-toggle",
                    new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            if (
                                (f.getExtendedState() & JFrame.MAXIMIZED_BOTH) ==
                                JFrame.MAXIMIZED_BOTH
                            ) f.setExtendedState(JFrame.NORMAL);
                            else f.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        }
                    }
                );
            f
                .getRootPane()
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "fs-normal");
            f
                .getRootPane()
                .getActionMap()
                .put(
                    "fs-normal",
                    new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            f.setExtendedState(JFrame.NORMAL);
                        }
                    }
                );
        } catch (Exception ex) {}
    }
}
