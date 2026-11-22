package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class PanelOdontogram extends JPanel {

    public JComboBox<String> cKondisi = new JComboBox<String>(
        new String[] {
            "Karies",
            "Tambalan Komposit",
            "Tambalan Amalgam",
            "Gigi Hilang",
            "Sisa Akar"
        }
    );
    public Map<String, String> data = new HashMap<String, String>();
    private Map<String, ToothCanvas> canvasMap = new HashMap<String, ToothCanvas>();
    private JLabel lblCount;
    String[] atasDewasaKanan = { "18", "17", "16", "15", "14", "13", "12", "11" };
    String[] atasDewasaKiri = { "21", "22", "23", "24", "25", "26", "27", "28" };
    String[] atasSusuKanan = { "55", "54", "53", "52", "51" };
    String[] atasSusuKiri = { "61", "62", "63", "64", "65" };
    String[] bawahSusuKanan = { "85", "84", "83", "82", "81" };
    String[] bawahSusuKiri = { "71", "72", "73", "74", "75" };
    String[] bawahDewasaKanan = { "48", "47", "46", "45", "44", "43", "42", "41" };
    String[] bawahDewasaKiri = { "31", "32", "33", "34", "35", "36", "37", "38" };

    public PanelOdontogram() {
        setLayout(new BorderLayout(6, 6));
        setBackground(Color.WHITE);
        setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(13, 122, 95), 1),
                "Odontogram FDI - klik gigi, kanan hapus"
            )
        );
        JLabel inf = new JLabel(
            "<html><center>Dewasa 32: atas 18-11 | 21-28, bawah 48-41 | 31-38 &nbsp; Susu 20: 55-51 | 61-65 dan 85-81 | 71-75<br/>Klik gigi untuk set kondisi, klik kanan untuk Normal. Hanya tidak normal disimpan.</center></html>",
            SwingConstants.CENTER
        );
        inf.setForeground(new Color(107, 114, 128));
        inf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(inf, BorderLayout.NORTH);
        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(Color.WHITE);
        rows.add(makeDewasaAtasRow());
        rows.add(makeSusuAtasRow());
        JPanel midP = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
        midP.setBackground(Color.WHITE);
        midP.add(new MidTri("both"));
        rows.add(midP);
        rows.add(makeSusuBawahRow());
        rows.add(makeDewasaBawahRow());
        add(rows, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        ctrl.setBackground(Color.WHITE);
        ctrl.add(new JLabel("Kondisi:"));
        cKondisi.setPreferredSize(new Dimension(180, 28));
        ctrl.add(cKondisi);
        JButton bR = new JButton("Reset");
        bR.setPreferredSize(new Dimension(80, 28));
        ctrl.add(bR);
        bR.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reset();
                }
            }
        );
        lblCount = new JLabel("0 gigi tidak normal");
        lblCount.setForeground(new Color(13, 122, 95));
        lblCount.setFont(new Font("Segoe UI", Font.BOLD, 11));
        ctrl.add(lblCount);
        south.add(ctrl, BorderLayout.NORTH);
        JPanel leg = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        leg.setBackground(new Color(249, 250, 251));
        leg.add(legItem(new Color(17, 24, 39), "Karies"));
        leg.add(legItem(new Color(6, 182, 212), "Komposit"));
        leg.add(legItem(new Color(148, 163, 184), "Amalgam"));
        leg.add(legItem(new Color(239, 68, 68), "Hilang"));
        leg.add(legItem(new Color(250, 204, 21), "Sisa Akar"));
        leg.add(legItem(Color.WHITE, "Normal"));
        south.add(leg, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel makeDewasaAtasRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        p.setBackground(Color.WHITE);
        for (String no : atasDewasaKanan) p.add(makeTooth(no, true));
        p.add(makeMidUpper());
        for (String no : atasDewasaKiri) p.add(makeTooth(no, true));
        return p;
    }

    private JPanel makeSusuAtasRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        p.setBackground(Color.WHITE);
        for (String no : atasSusuKanan) p.add(makeTooth(no, true));
        JPanel gap = new JPanel();
        gap.setOpaque(false);
        gap.setPreferredSize(new Dimension(16, 30));
        p.add(gap);
        for (String no : atasSusuKiri) p.add(makeTooth(no, true));
        return p;
    }

    private JPanel makeSusuBawahRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        p.setBackground(Color.WHITE);
        for (String no : bawahSusuKanan) p.add(makeTooth(no, false));
        JPanel gap = new JPanel();
        gap.setOpaque(false);
        gap.setPreferredSize(new Dimension(16, 30));
        p.add(gap);
        for (String no : bawahSusuKiri) p.add(makeTooth(no, false));
        return p;
    }

    private JPanel makeDewasaBawahRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        p.setBackground(Color.WHITE);
        for (String no : bawahDewasaKanan) p.add(makeTooth(no, false));
        p.add(makeMidLower());
        for (String no : bawahDewasaKiri) p.add(makeTooth(no, false));
        return p;
    }

    private JPanel makeTooth(String no, boolean labelAtas) {
        JPanel w = new JPanel(new BorderLayout(0, 0));
        w.setOpaque(false);
        JLabel lab = new JLabel(no, SwingConstants.CENTER);
        lab.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lab.setForeground(new Color(31, 41, 55));
        ToothCanvas cv = new ToothCanvas();
        canvasMap.put(no, cv);
        cv.setToolTipText("Gigi " + no + " - klik set, kanan hapus");
        cv.addMouseListener(
            new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (javax.swing.SwingUtilities.isRightMouseButton(e)) onToothClear(no);
                    else onToothClick(no);
                }
            }
        );
        if (labelAtas) {
            w.add(lab, BorderLayout.NORTH);
            w.add(cv, BorderLayout.CENTER);
        } else {
            w.add(cv, BorderLayout.CENTER);
            w.add(lab, BorderLayout.SOUTH);
        }
        return w;
    }

    private JPanel makeMidUpper() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setOpaque(false);
        MidTri tri = new MidTri("down");
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setPreferredSize(new Dimension(16, 14));
        top.add(tri, BorderLayout.CENTER);
        p.add(top, BorderLayout.NORTH);
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        empty.setPreferredSize(new Dimension(16, 30));
        p.add(empty, BorderLayout.CENTER);
        return p;
    }

    private JPanel makeMidLower() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setOpaque(false);
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        empty.setPreferredSize(new Dimension(16, 30));
        p.add(empty, BorderLayout.NORTH);
        MidTri tri = new MidTri("up");
        JPanel bot = new JPanel(new BorderLayout());
        bot.setOpaque(false);
        bot.setPreferredSize(new Dimension(16, 14));
        bot.add(tri, BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);
        return p;
    }

    private void onToothClick(String no) {
        String k = (String) cKondisi.getSelectedItem();
        data.put(no, k);
        ToothCanvas c = canvasMap.get(no);
        if (c != null) c.setKondisi(k);
        updateCount();
    }

    private void onToothClear(String no) {
        data.remove(no);
        ToothCanvas c = canvasMap.get(no);
        if (c != null) c.setKondisi(null);
        updateCount();
    }

    public Map<String, String> getData() {
        return data;
    }

    public void reset() {
        data.clear();
        for (ToothCanvas c : canvasMap.values()) c.setKondisi(null);
        updateCount();
    }

    public void setData(Map<String, String> d) {
        reset();
        if (d != null) {
            data.putAll(d);
            for (Map.Entry<String, String> e : d.entrySet()) {
                ToothCanvas c = canvasMap.get(e.getKey());
                if (c != null) c.setKondisi(e.getValue());
            }
        }
        updateCount();
    }

    private void updateCount() {
        if (lblCount != null) lblCount.setText(data.size() + " gigi tidak normal");
    }

    private JPanel legItem(Color c, String t) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel box = new JLabel();
        box.setPreferredSize(new Dimension(14, 14));
        box.setOpaque(true);
        box.setBackground(c);
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        p.add(box);
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        p.add(l);
        return p;
    }

    class ToothCanvas extends JComponent {

        private String kondisi = null;

        ToothCanvas() {
            setPreferredSize(new Dimension(30, 30));
            setMinimumSize(new Dimension(30, 30));
            setMaximumSize(new Dimension(30, 30));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        void setKondisi(String k) {
            kondisi = k;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int pad = 1;
            Color bg = Color.WHITE;
            if (kondisi != null) {
                if (kondisi.equals("Karies")) bg = new Color(17, 24, 39);
                else if (kondisi.equals("Tambalan Komposit")) bg = new Color(6, 182, 212);
                else if (kondisi.equals("Tambalan Amalgam")) bg = new Color(148, 163, 184);
                else if (kondisi.equals("Gigi Hilang")) bg = new Color(239, 68, 68);
                else if (kondisi.equals("Sisa Akar")) bg = new Color(250, 204, 21);
            }
            g2.setColor(bg);
            g2.fillRect(pad, pad, w - 2 * pad, h - 2 * pad);
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRect(pad, pad, w - 2 * pad - 1, h - 2 * pad - 1);
            int iw = w / 3;
            int ih = h / 3;
            int ix = (w - iw) / 2;
            int iy = (h - ih) / 2;
            boolean dark =
                kondisi != null && (kondisi.equals("Karies") || kondisi.equals("Gigi Hilang"));
            g2.setColor(dark ? Color.WHITE : new Color(100, 100, 100));
            g2.drawRect(ix, iy, iw, ih);
            g2.setColor(
                dark || (kondisi != null && kondisi.equals("Tambalan Komposit"))
                    ? Color.WHITE
                    : new Color(150, 150, 150)
            );
            g2.drawLine(pad, pad, ix, iy);
            g2.drawLine(pad + w - 2 * pad, pad, ix + iw, iy);
            g2.drawLine(pad, pad + h - 2 * pad, ix, iy + ih);
            g2.drawLine(pad + w - 2 * pad, pad + h - 2 * pad, ix + iw, iy + ih);
            if ("Gigi Hilang".equals(kondisi)) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawLine(pad + 2, pad + 2, w - pad - 2, h - pad - 2);
                g2.drawLine(w - pad - 2, pad + 2, pad + 2, h - pad - 2);
            }
            if (kondisi != null) {
                g2.setColor(new Color(13, 122, 95));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(pad, pad, w - 2 * pad - 1, h - 2 * pad - 1);
            }
            g2.dispose();
        }
    }

    class MidTri extends JComponent {

        private String arah;

        MidTri(String arah) {
            this.arah = arah;
            setPreferredSize(new Dimension(16, 14));
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(130, 140, 150));
            int w = getWidth();
            int h = getHeight();
            if (arah.equals("down")) {
                int[] xs = { 2, w - 2, w / 2 };
                int[] ys = { 2, 2, h - 2 };
                g2.fillPolygon(xs, ys, 3);
            } else if (arah.equals("up")) {
                int[] xs = { 2, w - 2, w / 2 };
                int[] ys = { h - 2, h - 2, 2 };
                g2.fillPolygon(xs, ys, 3);
            } else {
                int[] xs1 = { 2, w - 2, w / 2 };
                int[] ys1 = { h / 2, h / 2, 2 };
                int[] xs2 = { 2, w - 2, w / 2 };
                int[] ys2 = { h / 2, h / 2, h - 2 };
                g2.fillPolygon(xs1, ys1, 3);
                g2.fillPolygon(xs2, ys2, 3);
            }
            g2.dispose();
        }
    }
}
