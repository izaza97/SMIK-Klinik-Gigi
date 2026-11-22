package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import koneksi.Koneksi;
import model.Session;
import utils.Tema;
import utils.KlinikLogo;
import utils.Layar;

public class FormUtama extends JFrame {

    JLabel lblUser = new JLabel(), lblJam = new JLabel();
    JLabel vPasien = new JLabel("0", SwingConstants.CENTER), vAntre = new JLabel(
        "0",
        SwingConstants.CENTER
    ), vTunggu = new JLabel("0", SwingConstants.CENTER), vOmset = new JLabel(
        "Rp 0",
        SwingConstants.CENTER
    );
    DefaultTableModel mAntre = new DefaultTableModel(
        new String[] { "Antrean", "Pasien", "Dokter", "Status" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JButton bPasien, bDaftar, bAntre, bRM, bRiwayat, bTind, bBayar, bJanji, bLap, bMaster;

    public FormUtama() {
        setTitle("SIM Klinik Gigi - Menu Utama");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Layar.fullscreen(this);
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Tema.PRIMER);
        top.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        JPanel tl = new JPanel(new BorderLayout(12, 0));
        tl.setOpaque(false);
        JLabel ikon = new JLabel(KlinikLogo.iconCircle(42));
        tl.add(ikon, BorderLayout.WEST);
        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setOpaque(false);
        JLabel t = new JLabel("SIM Klinik Gigi");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(Color.WHITE);
        lblUser.setText(Session.nama + " | " + Session.peran);
        lblUser.setForeground(new Color(214, 255, 240));
        txt.add(t);
        txt.add(lblUser);
        tl.add(txt, BorderLayout.CENTER);
        top.add(tl, BorderLayout.WEST);
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tr.setOpaque(false);
        lblJam.setForeground(Color.WHITE);
        lblJam.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JButton bOut = new JButton("Logout");
        bOut.setBackground(Color.WHITE);
        bOut.setForeground(Tema.PRIMER_GELAP);
        bOut.setFocusPainted(false);
        tr.add(lblJam);
        tr.add(bOut);
        top.add(tr, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Color.WHITE);
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(229, 231, 235)));
        side.add(Box.createVerticalStrut(12));
        JLabel mn = new JLabel("  MENU UTAMA");
        mn.setForeground(new Color(107, 114, 128));
        mn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        side.add(mn);
        side.add(Box.createVerticalStrut(8));
        bPasien = menuBtn("Data Pasien");
        bDaftar = menuBtn("Pendaftaran");
        bAntre = menuBtn("Antrean");
        bRM = menuBtn("Rekam Medis");
        bRiwayat = menuBtn("Lihat Rekam Medis");
        bTind = menuBtn("Tindakan, Produk & Resep");
        bBayar = menuBtn("Pembayaran");
        bJanji = menuBtn("Janji Temu");
        bLap = menuBtn("Laporan");
        bMaster = menuBtn("Master");
        for (JButton b : new JButton[] {
            bPasien,
            bDaftar,
            bAntre,
            bRM,
            bRiwayat,
            bTind,
            bBayar,
            bJanji,
            bLap,
            bMaster
        }) {
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }
        side.add(Box.createVerticalGlue());
        JLabel ver = new JLabel("  v1.0 | JDK25 | XAMPP");
        ver.setForeground(new Color(156, 163, 175));
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        side.add(ver);
        side.add(Box.createVerticalStrut(10));
        add(side, BorderLayout.WEST);
        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(Tema.BG);
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel wel = Tema.kartu();
        wel.setLayout(new BorderLayout());
        JLabel w1 = new JLabel("Halo, " + Session.nama);
        w1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel w2 = new JLabel(
            "Hari ini " + LocalDate.now() + " | Tindakan+Produk+Resep satu tempat, invoice 2 tabel."
        );
        w2.setForeground(new Color(107, 114, 128));
        wel.add(w1, BorderLayout.NORTH);
        wel.add(w2, BorderLayout.CENTER);
        JButton bRef = new JButton("Refresh");
        Tema.primary(bRef);
        wel.add(bRef, BorderLayout.EAST);
        center.add(wel, BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 12));
        cards.setOpaque(false);
        cards.add(kartuStat("Total Pasien", vPasien, new Color(37, 99, 235)));
        cards.add(kartuStat("Antrean Hari Ini", vAntre, new Color(217, 119, 6)));
        cards.add(kartuStat("Menunggu Bayar", vTunggu, new Color(220, 38, 38)));
        cards.add(kartuStat("Omset Hari Ini", vOmset, new Color(22, 163, 74)));
        JPanel mid = new JPanel(new BorderLayout(12, 12));
        mid.setOpaque(false);
        mid.add(cards, BorderLayout.NORTH);
        JPanel tabP = Tema.kartu();
        tabP.setLayout(new BorderLayout());
        JLabel jt = new JLabel("Antrean Hari Ini (Live)");
        jt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabP.add(jt, BorderLayout.NORTH);
        JTable tb = new JTable(mAntre);
        Tema.styleTable(tb);
        tabP.add(new JScrollPane(tb), BorderLayout.CENTER);
        mid.add(tabP, BorderLayout.CENTER);
        center.add(mid, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        new Timer(1000, e ->
            lblJam.setText(
                LocalDate.now() +
                "  " +
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            )
        ).start();
        bOut.addActionListener(e -> {
            Session.clear();
            new FormLogin().setVisible(true);
            dispose();
        });
        bPasien.addActionListener(e -> new FormPasien().setVisible(true));
        bDaftar.addActionListener(e -> new FormPendaftaran().setVisible(true));
        bAntre.addActionListener(e -> new FormAntrean().setVisible(true));
        bRM.addActionListener(e -> new FormRekamMedis().setVisible(true));
        bRiwayat.addActionListener(e -> new FormLihatRekamMedis().setVisible(true));
        bTind.addActionListener(e -> new FormTindakanResep().setVisible(true));
        bBayar.addActionListener(e -> new FormPembayaran().setVisible(true));
        bJanji.addActionListener(e -> new FormJanjiTemu().setVisible(true));
        bLap.addActionListener(e -> new FormLaporan().setVisible(true));
        bMaster.addActionListener(e -> new FormMaster().setVisible(true));
        bRef.addActionListener(e -> refresh());
        atur();
        refresh();
    }

    JButton menuBtn(String t) {
        JButton b = new JButton(t);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(210, 38));
        b.setBackground(Color.WHITE);
        b.setForeground(Tema.TEKS);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    JPanel kartuStat(String j, JLabel v, Color w) {
        JPanel p = Tema.kartu();
        p.setLayout(new BorderLayout());
        JLabel jj = new JLabel(j);
        jj.setForeground(new Color(107, 114, 128));
        jj.setFont(new Font("Segoe UI", Font.BOLD, 12));
        v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        v.setForeground(w);
        p.add(jj, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    void atur() {
        String p = Session.peran == null ? "" : Session.peran;
        for (JButton b : new JButton[] {
            bPasien,
            bDaftar,
            bAntre,
            bRM,
            bRiwayat,
            bTind,
            bBayar,
            bJanji,
            bLap,
            bMaster
        }) b.setEnabled(false);
        if (p.equals("Resepsionis")) {
            for (JButton b : new JButton[] { bPasien, bDaftar, bAntre, bJanji, bLap }) b.setEnabled(
                true
            );
        } else if (p.equals("Dokter")) {
            for (JButton b : new JButton[] {
                bPasien,
                bAntre,
                bRM,
                bRiwayat,
                bTind,
                bLap
            }) b.setEnabled(true);
        } else if (p.equals("Kasir")) {
            for (JButton b : new JButton[] { bPasien, bTind, bBayar, bLap }) b.setEnabled(true);
        } else if (p.equals("Pemilik")) {
            for (JButton b : new JButton[] {
                bPasien,
                bDaftar,
                bAntre,
                bJanji,
                bBayar,
                bLap,
                bMaster
            }) b.setEnabled(true);
        }
    }

    void refresh() {
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            ResultSet r1 = k.createStatement().executeQuery("SELECT COUNT(*) FROM pasien");
            r1.next();
            vPasien.setText(r1.getString(1));
            ResultSet r2 = k
                .createStatement()
                .executeQuery("SELECT COUNT(*) FROM kunjungan WHERE tanggal=CURDATE()");
            r2.next();
            vAntre.setText(r2.getString(1));
            ResultSet r3 = k
                .createStatement()
                .executeQuery(
                    "SELECT COUNT(*) FROM kunjungan WHERE tanggal=CURDATE() AND status='Menunggu bayar'"
                );
            r3.next();
            vTunggu.setText(r3.getString(1));
            ResultSet r4 = k
                .createStatement()
                .executeQuery(
                    "SELECT IFNULL(SUM(total),0) FROM pembayaran WHERE DATE(tgl_bayar)=CURDATE()"
                );
            r4.next();
            vOmset.setText("Rp " + String.format("%,d", r4.getInt(1)).replace(",", "."));
            ResultSet ra = k
                .createStatement()
                .executeQuery(
                    "SELECT k.no_antrean,p.nama,d.nama,k.status FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm JOIN dokter d ON k.id_dokter=d.id_dokter WHERE k.tanggal=CURDATE() ORDER BY k.jam_daftar LIMIT 20"
                );
            mAntre.setRowCount(0);
            while (ra.next()) mAntre.addRow(
                new Object[] { ra.getString(1), ra.getString(2), ra.getString(3), ra.getString(4) }
            );
            k.close();
        } catch (Exception e) {}
    }
}
