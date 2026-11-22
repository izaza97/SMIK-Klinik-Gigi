package com.klinikgigi;

import javax.swing.*;
import view.FormLogin;

public class Main {

    public static void main(String[] args) {
        // JDK 25 + Swing JFrame
        try {
            for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(i.getName())) {
                    UIManager.setLookAndFeel(i.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new FormLogin().setVisible(true));
    }
}
