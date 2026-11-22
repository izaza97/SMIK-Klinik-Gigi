package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import koneksi.Koneksi;
import utils.*;

public class FormJanjiTemu extends JFrame {

    JTextField tRM = new JTextField(10), tTgl = new JTextField(10), tJam = new JTextField(
        "16:00",
        8
    ), tKep = new JTextField("Kontrol", 12);
    JLabel lblNama = new JLabel("-");
    JComboBox<String> cDok = new JComboBox<>();
    DefaultTableModel m = new DefaultTableModel(
        new String[] { "ID", "NoRM", "Pasien", "Dokter", "Tanggal", "Jam", "Keperluan", "Status" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tb = new JTable(m);
    java.util.Map<String, String> map = new java.util.HashMap<>();

    public FormJanjiTemu() {
        setTitle("Janji Temu");
        setSize(1080, 620);
        setLocationRelativeTo(null);
        Layar.fullscreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Tema.BG);
        setLayout(new BorderLayout());
        add(
            Tema.header(
                "Janji Kontrol",
                "Tahap 6 | Validasi jadwal | Anti bentrok | Pilih pasien dari master"
            ),
            BorderLayout.NORTH
        );
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(Tema.BG);
        main.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));
        JPanel form = Tema.kartu();
        form.setLayout(new BorderLayout(8, 8));
        JPanel grid = new JPanel(new GridLayout(2, 6, 8, 8));
        grid.setOpaque(false);
        grid.add(new JLabel("No RM"));
        JPanel pRM = new JPanel(new BorderLayout(4, 0));
        pRM.setOpaque(false);
        tRM.setEditable(false);
        tRM.setBackground(new Color(243, 244, 246));
        tRM.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pRM.add(tRM, BorderLayout.CENTER);
        JButton bCariPasien = new JButton("Cari...");
        Tema.secondary(bCariPasien);
        bCariPasien.setPreferredSize(new Dimension(90, 30));
        pRM.add(bCariPasien, BorderLayout.EAST);
        grid.add(pRM);
        grid.add(new JLabel("Dokter"));
        grid.add(cDok);
        grid.add(new JLabel("Tanggal (yyyy-mm-dd)"));
        grid.add(tTgl);
        grid.add(new JLabel("Nama Pasien"));
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNama.setForeground(Tema.PRIMER_GELAP);
        grid.add(lblNama);
        grid.add(new JLabel("Jam (HH:mm)"));
        grid.add(tJam);
        grid.add(new JLabel("Keperluan"));
        grid.add(tKep);
        form.add(grid, BorderLayout.CENTER);
        JPanel bb = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bb.setOpaque(false);
        JButton bS = new JButton("Simpan Janji");
        Tema.primary(bS);
        JButton bC = new JButton("Konfirmasi -> Antrean");
        Tema.success(bC);
        JButton bH = new JButton("Hapus");
        Tema.danger(bH);
        JButton bB = new JButton("Batalkan");
        Tema.secondary(bB);
        JButton bBaru = new JButton("Baru");
        Tema.secondary(bBaru);
        bb.add(bS);
        bb.add(bC);
        bb.add(bH);
        bb.add(bB);
        bb.add(bBaru);
        JLabel h = new JLabel("Klik baris untuk pilih | Hapus hanya Terjadwal/Batal");
        h.setForeground(Tema.ABU);
        h.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        bb.add(h);
        form.add(bb, BorderLayout.SOUTH);
        main.add(form, BorderLayout.NORTH);
        JPanel card = Tema.kartu();
        card.setLayout(new BorderLayout(8, 8));
        card.add(new JLabel("Daftar Janji (klik untuk pilih):"), BorderLayout.NORTH);
        Tema.styleTable(tb);
        card.add(new JScrollPane(tb), BorderLayout.CENTER);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        bCariPasien.addActionListener(e -> {
            DialogCariPasien d = new DialogCariPasien(this);
            d.setVisible(true);
            if (d.selectedRM != null) {
                tRM.setText(d.selectedRM);
                lblNama.setText(d.selectedNama);
            }
        });
        bS.addActionListener(e -> simpan());
        bC.addActionListener(e -> konfirm());
        bH.addActionListener(e -> hapus());
        bB.addActionListener(e -> batalkan());
        bBaru.addActionListener(e -> baru());
        tb
            .getSelectionModel()
            .addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) isiDariTabel();
            });
        loadDok();
        tTgl.setText(LocalDate.now().plusDays(7).toString());
        load();
    }

    void loadDok() {
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            ResultSet r = k.createStatement().executeQuery("SELECT * FROM dokter");
            cDok.removeAllItems();
            map.clear();
            while (r.next()) {
                String s = r.getString("id_dokter") + " - " + r.getString("nama");
                cDok.addItem(s);
                map.put(s, r.getString("id_dokter"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void load() {
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            ResultSet r = k
                .createStatement()
                .executeQuery(
                    "SELECT j.id_janji,j.no_rm,p.nama,d.nama,j.tanggal,j.jam,j.keperluan,j.status FROM janji_temu j JOIN dokter d ON j.id_dokter=d.id_dokter LEFT JOIN pasien p ON j.no_rm=p.no_rm ORDER BY j.tanggal DESC, j.jam DESC"
                );
            m.setRowCount(0);
            while (r.next()) m.addRow(
                new Object[] {
                    r.getInt(1),
                    r.getString(2),
                    r.getString(3) == null ? "-" : r.getString(3),
                    r.getString(4),
                    r.getString(5),
                    r.getString(6),
                    r.getString(7),
                    r.getString(8)
                }
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void baru() {
        tRM.setText("");
        lblNama.setText("-");
        tKep.setText("Kontrol");
        tb.clearSelection();
    }

    void isiDariTabel() {
        int sel = tb.getSelectedRow();
        if (sel < 0) return;
        tRM.setText(m.getValueAt(sel, 1).toString());
        lblNama.setText(m.getValueAt(sel, 2).toString());
        String namaDok = m.getValueAt(sel, 3).toString();
        for (int i = 0; i < cDok.getItemCount(); i++) {
            String it = cDok.getItemAt(i);
            if (it != null && it.contains(namaDok)) {
                cDok.setSelectedIndex(i);
                break;
            }
        }
        tTgl.setText(m.getValueAt(sel, 4).toString());
        String jam = m.getValueAt(sel, 5).toString();
        if (jam.length() >= 5) jam = jam.substring(0, 5);
        tJam.setText(jam);
        Object kep = m.getValueAt(sel, 6);
        tKep.setText(kep == null ? "" : kep.toString());
    }

    void simpan() {
        try {
            if (tRM.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih pasien dulu dengan tombol Cari...!");
                return;
            }
            String key = (String) cDok.getSelectedItem();
            if (key == null || !map.containsKey(key)) {
                JOptionPane.showMessageDialog(this, "Pilih dokter dulu!");
                return;
            }
            String idD = map.get(key);
            String tgl = tTgl.getText().trim();
            String jam = tJam.getText().trim();
            LocalDate d;
            try {
                d = LocalDate.parse(tgl);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tanggal harus yyyy-mm-dd, contoh 2026-10-31");
                return;
            }
            if (!jam.matches("\\d{2}:\\d{2}(:\\d{2})?")) {
                JOptionPane.showMessageDialog(this, "Jam harus HH:mm, contoh 16:00");
                return;
            }
            String jamDB = jam.length() == 5 ? jam + ":00" : jam;
            String hari = TanggalIndo.hariDari(d);
            Connection k = Koneksi.getConnection();
            if (k == null) return;
            PreparedStatement cekP = k.prepareStatement("SELECT nama FROM pasien WHERE no_rm=?");
            cekP.setString(1, tRM.getText().trim());
            ResultSet rp = cekP.executeQuery();
            if (!rp.next()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No RM tidak terdaftar di master pasien! Klik Cari..."
                );
                k.close();
                return;
            }
            lblNama.setText(rp.getString(1));
            PreparedStatement j = k.prepareStatement(
                "SELECT COUNT(*) FROM jadwal_dokter WHERE id_dokter=? AND hari=? AND ? BETWEEN jam_mulai AND jam_selesai"
            );
            j.setString(1, idD);
            j.setString(2, hari);
            j.setString(3, jamDB);
            ResultSet rj = j.executeQuery();
            rj.next();
            if (rj.getInt(1) == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Tolak: dokter tidak praktik " + hari + " jam " + jam
                );
                k.close();
                return;
            }
            PreparedStatement b = k.prepareStatement(
                "SELECT COUNT(*) FROM janji_temu WHERE id_dokter=? AND tanggal=? AND jam=? AND status='Terjadwal'"
            );
            b.setString(1, idD);
            b.setString(2, tgl);
            b.setString(3, jamDB);
            ResultSet rb = b.executeQuery();
            rb.next();
            if (rb.getInt(1) > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Tolak: jadwal bentrok! Sudah ada booking jam itu."
                );
                k.close();
                return;
            }
            PreparedStatement p = k.prepareStatement(
                "INSERT INTO janji_temu(no_rm,id_dokter,tanggal,jam,keperluan,status) VALUES(?,?,?,?,?,'Terjadwal')"
            );
            p.setString(1, tRM.getText().trim());
            p.setString(2, idD);
            p.setString(3, tgl);
            p.setString(4, jamDB);
            p.setString(5, tKep.getText().trim());
            p.executeUpdate();
            k.close();
            JOptionPane.showMessageDialog(
                this,
                "Janji tersimpan Terjadwal untuk " + lblNama.getText()
            );
            load();
            baru();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void konfirm() {
        try {
            int sel = tb.getSelectedRow();
            String sId = null;
            if (sel >= 0) sId = m.getValueAt(sel, 0).toString();
            else {
                sId = JOptionPane.showInputDialog(this, "ID Janji yang dikonfirmasi:");
                if (sId == null || sId.trim().isEmpty()) return;
            }
            Connection k = Koneksi.getConnection();
            if (k == null) return;
            PreparedStatement g = k.prepareStatement("SELECT * FROM janji_temu WHERE id_janji=?");
            g.setInt(1, Integer.parseInt(sId.trim()));
            ResultSet r = g.executeQuery();
            if (!r.next()) {
                JOptionPane.showMessageDialog(this, "ID tidak ketemu!");
                k.close();
                return;
            }
            String st = r.getString("status");
            if (!"Terjadwal".equals(st)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Hanya Terjadwal yang bisa dikonfirmasi. Status sekarang: " + st
                );
                k.close();
                return;
            }
            String antre = AutoNumber.noAntrean(r.getString("id_dokter"));
            PreparedStatement p = k.prepareStatement(
                "INSERT INTO kunjungan(tanggal,no_rm,id_dokter,keluhan,no_antrean,jam_daftar,status,jenis) VALUES(CURDATE(),?,?,?, ?,CURTIME(),'Menunggu','Lama')"
            );
            p.setString(1, r.getString("no_rm"));
            p.setString(2, r.getString("id_dokter"));
            p.setString(3, "Kontrol: " + r.getString("keperluan"));
            p.setString(4, antre);
            p.executeUpdate();
            k
                .createStatement()
                .executeUpdate(
                    "UPDATE janji_temu SET status='Dikonfirmasi' WHERE id_janji=" + sId.trim()
                );
            k.close();
            JOptionPane.showMessageDialog(this, "Masuk antrean " + antre);
            load();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void hapus() {
        int sel = tb.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris janji dulu di tabel!");
            return;
        }
        String sId = m.getValueAt(sel, 0).toString();
        String st = m.getValueAt(sel, 7).toString();
        if ("Dikonfirmasi".equals(st) || "Selesai".equals(st)) {
            JOptionPane.showMessageDialog(
                this,
                "Tidak boleh hapus yang sudah " + st + ". Gunakan Batalkan jika perlu."
            );
            return;
        }
        if (
            JOptionPane.showConfirmDialog(
                this,
                "Hapus janji ID " + sId + " (" + m.getValueAt(sel, 2) + ")?",
                "Hapus",
                JOptionPane.YES_NO_OPTION
            ) !=
            0
        ) return;
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            k.createStatement().executeUpdate("DELETE FROM janji_temu WHERE id_janji=" + sId);
            JOptionPane.showMessageDialog(this, "Janji ID " + sId + " dihapus.");
            load();
            baru();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void batalkan() {
        int sel = tb.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris dulu!");
            return;
        }
        String sId = m.getValueAt(sel, 0).toString();
        String st = m.getValueAt(sel, 7).toString();
        if ("Batal".equals(st)) {
            JOptionPane.showMessageDialog(this, "Sudah Batal.");
            return;
        }
        if ("Selesai".equals(st)) {
            JOptionPane.showMessageDialog(this, "Tidak bisa batalkan Selesai.");
            return;
        }
        if (
            JOptionPane.showConfirmDialog(
                this,
                "Batalkan janji ID " + sId + "?",
                "Batalkan",
                JOptionPane.YES_NO_OPTION
            ) !=
            0
        ) return;
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            k
                .createStatement()
                .executeUpdate("UPDATE janji_temu SET status='Batal' WHERE id_janji=" + sId);
            load();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

class DialogCariPasien extends JDialog {

    JTextField tCari = new JTextField(20);
    DefaultTableModel mP = new DefaultTableModel(new String[] { "No RM", "Nama", "NIK", "HP" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tb = new JTable(mP);
    public String selectedRM = null;
    public String selectedNama = null;

    public DialogCariPasien(java.awt.Frame owner) {
        super(owner, "Cari Pasien - Pilih No RM", true);
        setSize(640, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.add(new JLabel("Cari Nama / No RM / NIK / HP:"), BorderLayout.WEST);
        top.add(tCari, BorderLayout.CENTER);
        JButton bCari = new JButton("Cari");
        top.add(bCari, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        utils.Tema.styleTable(tb);
        add(new JScrollPane(tb), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bPilih = new JButton("Pilih");
        JButton bBatal = new JButton("Batal");
        utils.Tema.primary(bPilih);
        utils.Tema.secondary(bBatal);
        bot.add(bBatal);
        bot.add(bPilih);
        add(bot, BorderLayout.SOUTH);
        bCari.addActionListener(e -> load(tCari.getText()));
        tCari.addActionListener(e -> load(tCari.getText()));
        bPilih.addActionListener(e -> pilih());
        bBatal.addActionListener(e -> dispose());
        tb.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) pilih();
                }
            }
        );
        load("");
    }

    void load(String f) {
        try (Connection k = koneksi.Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT no_rm,nama,nik,no_hp FROM pasien WHERE no_rm LIKE ? OR nama LIKE ? OR nik LIKE ? OR no_hp LIKE ? ORDER BY nama LIMIT 100"
            );
            for (int i = 1; i <= 4; i++) p.setString(i, "%" + f + "%");
            ResultSet r = p.executeQuery();
            mP.setRowCount(0);
            while (r.next()) mP.addRow(
                new Object[] { r.getString(1), r.getString(2), r.getString(3), r.getString(4) }
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void pilih() {
        int i = tb.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih baris pasien dulu! (double-click juga bisa)"
            );
            return;
        }
        selectedRM = mP.getValueAt(i, 0).toString();
        selectedNama = mP.getValueAt(i, 1).toString();
        dispose();
    }
}
