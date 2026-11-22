package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import koneksi.Koneksi;
import utils.Tema;
import utils.Layar;

public class FormRekamMedis extends JFrame {

    JComboBox<String> cKunj = new JComboBox<String>();
    JComboBox<String> cDiag = new JComboBox<String>();
    JTextField tTensi = new JTextField(10);
    JTextArea tS = new JTextArea(4, 20);
    JTextArea tO = new JTextArea(4, 20);
    JTextArea tA = new JTextArea(4, 20);
    JTextArea tP = new JTextArea(4, 20);
    PanelOdontogram odo = new PanelOdontogram();
    DefaultTableModel mHist = new DefaultTableModel(
        new String[] { "Tanggal", "Dokter", "Tensi", "S", "Diagnosa" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tbHist = new JTable(mHist);
    JTextArea txtDetail = new JTextArea(12, 30);
    JLabel lblPasien = new JLabel("-");
    JLabel lblCountHist = new JLabel("0 riwayat");
    Map<String, Integer> mapK = new HashMap<String, Integer>();
    Map<String, String> mapD = new HashMap<String, String>();
    java.util.List<Integer> histIdRekam = new java.util.ArrayList<Integer>();
    JSplitPane splitMain;
    int lastDivider = 450;

    public FormRekamMedis() {
        setTitle("Rekam Medis - Kiri Riwayat, Kanan Baru");
        setSize(1360, 820);
        setLocationRelativeTo(null);
        Layar.fullscreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Tema.BG);
        setLayout(new BorderLayout());
        add(
            Tema.header(
                "Rekam Medis SOAP + Odontogram",
                "Kiri riwayat sebelumnya, kanan pemeriksaan baru | S + Tensi dari pendaftaran"
            ),
            BorderLayout.NORTH
        );
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Tema.BG);
        main.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));
        JPanel top = Tema.kartu();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        top.add(new JLabel("Kunjungan Dipanggil:"));
        cKunj.setPreferredSize(new Dimension(250, 32));
        top.add(cKunj);
        top.add(new JLabel("Diagnosis ICD-10:"));
        cDiag.setPreferredSize(new Dimension(250, 32));
        top.add(cDiag);
        JButton bR = new JButton("Refresh");
        Tema.secondary(bR);
        bR.setPreferredSize(new Dimension(100, 32));
        top.add(bR);
        JButton bToggle = new JButton("Sembunyikan Riwayat");
        Tema.secondary(bToggle);
        bToggle.setPreferredSize(new Dimension(170, 32));
        top.add(bToggle);
        main.add(top, BorderLayout.NORTH);
        JPanel panelKiri = Tema.kartu();
        panelKiri.setLayout(new BorderLayout(8, 8));
        JPanel kiriNorth = new JPanel(new GridLayout(3, 1, 2, 2));
        kiriNorth.setOpaque(false);
        JLabel lTitleKiri = new JLabel("RIWAYAT SEBELUMNYA");
        lTitleKiri.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTitleKiri.setForeground(Tema.PRIMER_GELAP);
        kiriNorth.add(lTitleKiri);
        lblPasien.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        kiriNorth.add(lblPasien);
        JPanel rowInfo = new JPanel(new BorderLayout());
        rowInfo.setOpaque(false);
        lblCountHist.setForeground(Tema.ABU);
        lblCountHist.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        rowInfo.add(lblCountHist, BorderLayout.WEST);
        JLabel hintKiri = new JLabel("Klik baris untuk detail.");
        hintKiri.setForeground(Tema.ABU);
        hintKiri.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        rowInfo.add(hintKiri, BorderLayout.EAST);
        kiriNorth.add(rowInfo);
        panelKiri.add(kiriNorth, BorderLayout.NORTH);
        Tema.styleTable(tbHist);
        tbHist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbHist.getColumnModel().getColumn(0).setPreferredWidth(75);
        tbHist.getColumnModel().getColumn(1).setPreferredWidth(110);
        tbHist.getColumnModel().getColumn(2).setPreferredWidth(55);
        tbHist.getColumnModel().getColumn(3).setPreferredWidth(140);
        tbHist.getColumnModel().getColumn(4).setPreferredWidth(140);
        JScrollPane spHist = new JScrollPane(tbHist);
        spHist.setPreferredSize(new Dimension(420, 220));
        JPanel detailWrap = new JPanel(new BorderLayout(4, 4));
        detailWrap.setOpaque(false);
        detailWrap.add(new JLabel("Detail (read-only):"), BorderLayout.NORTH);
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDetail.setBackground(new Color(249, 250, 251));
        JScrollPane spDetail = new JScrollPane(txtDetail);
        spDetail.setPreferredSize(new Dimension(420, 300));
        detailWrap.add(spDetail, BorderLayout.CENTER);
        JPanel btnHist = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnHist.setOpaque(false);
        JButton bSalinOdo = new JButton("Salin Odonto ke Kanan");
        Tema.primary(bSalinOdo);
        JButton bSalinS = new JButton("Salin S");
        Tema.secondary(bSalinS);
        btnHist.add(bSalinOdo);
        btnHist.add(bSalinS);
        detailWrap.add(btnHist, BorderLayout.SOUTH);
        JSplitPane splitKiri = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spHist, detailWrap);
        splitKiri.setDividerLocation(220);
        splitKiri.setOneTouchExpandable(true);
        splitKiri.setResizeWeight(0.4);
        panelKiri.add(splitKiri, BorderLayout.CENTER);
        JPanel panelKanan = new JPanel(new BorderLayout(10, 10));
        panelKanan.setBackground(Tema.BG);
        JPanel soap = Tema.kartu();
        soap.setLayout(new BorderLayout(8, 8));
        JPanel soapNorth = new JPanel(new BorderLayout(4, 4));
        soapNorth.setOpaque(false);
        JLabel lTitleKanan = new JLabel("PEMERIKSAAN BARU (isi di kanan)");
        lTitleKanan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTitleKanan.setForeground(Tema.PRIMER_GELAP);
        soapNorth.add(lTitleKanan, BorderLayout.NORTH);
        JPanel vit = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        vit.setOpaque(false);
        vit.add(new JLabel("Tensi:"));
        tTensi.setPreferredSize(new Dimension(100, 28));
        vit.add(tTensi);
        JLabel u = new JLabel("mmHg | cth 120/80");
        u.setForeground(Tema.ABU);
        vit.add(u);
        JLabel hintBaru = new JLabel("S otomatis dari pendaftaran.");
        hintBaru.setForeground(Tema.ABU);
        hintBaru.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        vit.add(hintBaru);
        soapNorth.add(vit, BorderLayout.CENTER);
        soap.add(soapNorth, BorderLayout.NORTH);
        JPanel fg = new JPanel(new GridLayout(2, 2, 8, 8));
        fg.setOpaque(false);
        fg.add(soapItem("S - Subjektif:", tS));
        fg.add(soapItem("O - Objektif:", tO));
        fg.add(soapItem("A - Assessment:", tA));
        fg.add(soapItem("P - Plan:", tP));
        soap.add(fg, BorderLayout.CENTER);
        JSplitPane splitKanan = new JSplitPane(JSplitPane.VERTICAL_SPLIT, soap, odo);
        splitKanan.setDividerLocation(340);
        splitKanan.setOneTouchExpandable(true);
        splitKanan.setResizeWeight(0.45);
        panelKanan.add(splitKanan, BorderLayout.CENTER);
        splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelKiri, panelKanan);
        splitMain.setDividerLocation(450);
        splitMain.setOneTouchExpandable(true);
        splitMain.setResizeWeight(0.0);
        panelKiri.setMinimumSize(new Dimension(360, 0));
        panelKanan.setMinimumSize(new Dimension(600, 0));
        main.add(splitMain, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(Tema.BG);
        JButton bSave = new JButton("SIMPAN");
        Tema.secondary(bSave);
        bSave.setPreferredSize(new Dimension(150, 42));
        JButton bLanjut = new JButton("SIMPAN & LANJUT KE TINDAKAN");
        Tema.success(bLanjut);
        bLanjut.setPreferredSize(new Dimension(300, 42));
        bottom.add(bSave);
        bottom.add(bLanjut);
        main.add(bottom, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);
        bSave.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    simpanSaja();
                }
            }
        );
        bLanjut.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    simpanDanLanjut();
                }
            }
        );
        bToggle.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (splitMain.getDividerLocation() > 50) {
                        lastDivider = splitMain.getDividerLocation();
                        splitMain.setDividerLocation(0);
                        bToggle.setText("Tampilkan Riwayat");
                    } else {
                        splitMain.setDividerLocation(lastDivider > 50 ? lastDivider : 450);
                        bToggle.setText("Sembunyikan Riwayat");
                    }
                }
            }
        );
        cKunj.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    loadPasienInfo();
                    loadAwal();
                    riwayat();
                }
            }
        );
        bR.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    loadKunj();
                    loadDiag();
                    loadPasienInfo();
                    loadAwal();
                    riwayat();
                }
            }
        );
        tbHist
            .getSelectionModel()
            .addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) tampilDetail();
                    }
                }
            );
        bSalinOdo.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    salinOdo();
                }
            }
        );
        bSalinS.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    salinS();
                }
            }
        );
        loadKunj();
        loadDiag();
        loadPasienInfo();
        loadAwal();
        riwayat();
    }

    JPanel soapItem(String j, JTextArea t) {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(j);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(l, BorderLayout.NORTH);
        t.setLineWrap(true);
        t.setWrapStyleWord(true);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(200, 90));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    void loadKunj() {
        try (
            Connection k = Koneksi.getConnection();
            ResultSet r = k
                .createStatement()
                .executeQuery(
                    "SELECT k.id_kunjungan,p.nama,k.no_antrean FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm WHERE k.status='Dipanggil' AND k.tanggal=CURDATE() ORDER BY k.jam_panggil"
                )
        ) {
            if (k == null) return;
            cKunj.removeAllItems();
            mapK.clear();
            while (r.next()) {
                String s = r.getInt(1) + " - " + r.getString(3) + " - " + r.getString(2);
                cKunj.addItem(s);
                mapK.put(s, r.getInt(1));
            }
            if (cKunj.getItemCount() == 0) cKunj.addItem("TIDAK ADA - panggil antrean dulu");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void loadDiag() {
        try (
            Connection k = Koneksi.getConnection();
            ResultSet r = k
                .createStatement()
                .executeQuery("SELECT * FROM diagnosis ORDER BY kode_diagnosis")
        ) {
            if (k == null) return;
            cDiag.removeAllItems();
            mapD.clear();
            while (r.next()) {
                String s = r.getString(1) + " - " + r.getString(2);
                cDiag.addItem(s);
                mapD.put(s, r.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void loadPasienInfo() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (key == null || !mapK.containsKey(key)) {
                lblPasien.setText("-");
                return;
            }
            int idK = mapK.get(key).intValue();
            try (Connection k = Koneksi.getConnection()) {
                if (k == null) return;
                PreparedStatement p = k.prepareStatement(
                    "SELECT k.no_rm,p.nama,p.tgl_lahir,p.alergi,k.tanggal,k.no_antrean,d.nama FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm JOIN dokter d ON k.id_dokter=d.id_dokter WHERE k.id_kunjungan=?"
                );
                p.setInt(1, idK);
                ResultSet r = p.executeQuery();
                if (r.next()) {
                    String alergi = r.getString(4) == null || r.getString(4).isEmpty()
                        ? "-"
                        : r.getString(4);
                    lblPasien.setText(
                        "<html><b>" +
                        r.getString(1) +
                        " - " +
                        r.getString(2) +
                        "</b> | Lahir: " +
                        r.getString(3) +
                        " | Alergi: " +
                        alergi +
                        "<br/>Kunjungan: " +
                        r.getString(5) +
                        " " +
                        r.getString(6) +
                        " | " +
                        r.getString(7) +
                        "</html>"
                    );
                }
            }
        } catch (Exception e) {}
    }

    void loadAwal() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (key == null || !mapK.containsKey(key)) return;
            int idK = mapK.get(key).intValue();
            try (Connection k = Koneksi.getConnection()) {
                if (k == null) return;
                try {
                    PreparedStatement p = k.prepareStatement(
                        "SELECT keluhan,tensi FROM kunjungan WHERE id_kunjungan=?"
                    );
                    p.setInt(1, idK);
                    ResultSet r = p.executeQuery();
                    if (r.next()) {
                        tS.setText(r.getString(1) == null ? "" : r.getString(1));
                        tTensi.setText(r.getString(2) == null ? "" : r.getString(2));
                    }
                } catch (Exception ex) {
                    PreparedStatement p2 = k.prepareStatement(
                        "SELECT keluhan FROM kunjungan WHERE id_kunjungan=?"
                    );
                    p2.setInt(1, idK);
                    ResultSet r2 = p2.executeQuery();
                    if (r2.next()) tS.setText(r2.getString(1) == null ? "" : r2.getString(1));
                }
            }
        } catch (Exception e) {}
    }

    void riwayat() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (key == null || !mapK.containsKey(key)) {
                mHist.setRowCount(0);
                histIdRekam.clear();
                lblCountHist.setText("0 riwayat");
                txtDetail.setText("Pilih kunjungan dulu.");
                return;
            }
            int idK = mapK.get(key).intValue();
            Connection k = Koneksi.getConnection();
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT r.id_rekam,k.tanggal,dd.nama,k.tensi,r.subjektif,r.kode_diagnosis,dg.nama_diagnosis FROM rekam_medis r JOIN kunjungan k ON r.id_kunjungan=k.id_kunjungan LEFT JOIN dokter dd ON k.id_dokter=dd.id_dokter LEFT JOIN diagnosis dg ON r.kode_diagnosis=dg.kode_diagnosis WHERE k.no_rm=(SELECT no_rm FROM kunjungan WHERE id_kunjungan=?) ORDER BY k.tanggal DESC,k.id_kunjungan DESC"
            );
            p.setInt(1, idK);
            ResultSet r = p.executeQuery();
            mHist.setRowCount(0);
            histIdRekam.clear();
            int tot = 0;
            while (r.next()) {
                int idR = r.getInt(1);
                histIdRekam.add(Integer.valueOf(idR));
                String s = r.getString(5) == null ? "" : r.getString(5).replaceAll("\\n", " ");
                String sShort = s.length() > 30 ? s.substring(0, 30) + "..." : s;
                String diag =
                    (r.getString(6) == null ? "" : r.getString(6) + " - ") +
                    (r.getString(7) == null ? "" : r.getString(7));
                if (diag.length() > 35) diag = diag.substring(0, 35) + "...";
                mHist.addRow(
                    new Object[] {
                        r.getString(2),
                        r.getString(3) == null ? "-" : r.getString(3),
                        r.getString(4) == null ? "" : r.getString(4),
                        sShort,
                        diag
                    }
                );
                tot++;
            }
            k.close();
            lblCountHist.setText(tot + " riwayat");
            if (tot == 0) {
                txtDetail.setText("Belum ada riwayat. Pasien baru.");
            } else {
                txtDetail.setText("Klik salah satu baris untuk lihat detail lengkap.");
                if (tbHist.getRowCount() > 0) tbHist.setRowSelectionInterval(0, 0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void tampilDetail() {
        int row = tbHist.getSelectedRow();
        if (row < 0 || row >= histIdRekam.size()) return;
        int idR = histIdRekam.get(row).intValue();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT k.tanggal,dd.nama,k.tensi,r.subjektif,r.objektif,r.assessment,r.plan,r.kode_diagnosis,dg.nama_diagnosis FROM rekam_medis r JOIN kunjungan k ON r.id_kunjungan=k.id_kunjungan LEFT JOIN dokter dd ON k.id_dokter=dd.id_dokter LEFT JOIN diagnosis dg ON r.kode_diagnosis=dg.kode_diagnosis WHERE r.id_rekam=?"
            );
            p.setInt(1, idR);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Tanggal : ").append(r.getString(1)).append("\n");
                sb
                    .append("Dokter : ")
                    .append(r.getString(2) == null ? "-" : r.getString(2))
                    .append("\n");
                sb
                    .append("Tensi : ")
                    .append(
                        r.getString(3) == null || r.getString(3).isEmpty() ? "-" : r.getString(3)
                    )
                    .append("\n");
                sb
                    .append("Diagnosa : ")
                    .append(r.getString(8) == null ? "" : r.getString(8))
                    .append(" - ")
                    .append(r.getString(9) == null ? "" : r.getString(9))
                    .append("\n");
                sb.append("--------------------------------\n");
                sb
                    .append("S : ")
                    .append(r.getString(4) == null ? "" : r.getString(4))
                    .append("\n\n");
                sb
                    .append("O : ")
                    .append(r.getString(5) == null ? "" : r.getString(5))
                    .append("\n\n");
                sb
                    .append("A : ")
                    .append(r.getString(6) == null ? "" : r.getString(6))
                    .append("\n\n");
                sb.append("P : ").append(r.getString(7) == null ? "" : r.getString(7)).append("\n");
                sb.append("--------------------------------\n");
                String isiResep = "";
                try {
                    PreparedStatement pr = k.prepareStatement(
                        "SELECT isi_resep FROM resep WHERE id_kunjungan=(SELECT id_kunjungan FROM rekam_medis WHERE id_rekam=?)"
                    );
                    pr.setInt(1, idR);
                    ResultSet rr = pr.executeQuery();
                    if (rr.next() && rr.getString(1) != null) isiResep = rr.getString(1);
                    rr.close();
                    pr.close();
                } catch (Exception exRes) {}
                sb
                    .append("Resep : ")
                    .append(isiResep.isEmpty() ? "- (tidak ada)" : ("\n" + isiResep))
                    .append("\n\n");
                sb.append("--------------------------------\n");
                sb.append("Odontogram:\n");
                PreparedStatement po = k.prepareStatement(
                    "SELECT no_gigi,kondisi FROM odontogram WHERE id_rekam=? ORDER BY no_gigi"
                );
                po.setInt(1, idR);
                ResultSet ro = po.executeQuery();
                int c = 0;
                while (ro.next()) {
                    sb
                        .append(" ")
                        .append(ro.getString(1))
                        .append(" : ")
                        .append(ro.getString(2))
                        .append("\n");
                    c++;
                }
                if (c == 0) sb.append(" (semua normal)\n");
                txtDetail.setText(sb.toString());
                txtDetail.setCaretPosition(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void salinOdo() {
        int row = tbHist.getSelectedRow();
        if (row < 0 || row >= histIdRekam.size()) {
            JOptionPane.showMessageDialog(this, "Pilih riwayat dulu di kiri!");
            return;
        }
        int idR = histIdRekam.get(row).intValue();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT no_gigi,kondisi FROM odontogram WHERE id_rekam=?"
            );
            p.setInt(1, idR);
            ResultSet r = p.executeQuery();
            Map<String, String> map = new HashMap<String, String>();
            while (r.next()) map.put(r.getString(1), r.getString(2));
            k.close();
            if (map.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Riwayat ini normal.");
                return;
            }
            odo.setData(map);
            JOptionPane.showMessageDialog(this, "Odontogram disalin (" + map.size() + " gigi).");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void salinS() {
        int row = tbHist.getSelectedRow();
        if (row < 0 || row >= histIdRekam.size()) {
            JOptionPane.showMessageDialog(this, "Pilih riwayat dulu!");
            return;
        }
        int idR = histIdRekam.get(row).intValue();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT subjektif FROM rekam_medis WHERE id_rekam=?"
            );
            p.setInt(1, idR);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                String prev = r.getString(1) == null ? "" : r.getString(1);
                if (!prev.isEmpty()) {
                    String cur = tS.getText().trim();
                    if (cur.isEmpty()) tS.setText(prev);
                    else tS.setText(cur + "\n[Riwayat] " + prev);
                }
            }
            k.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    int simpanData() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (!mapK.containsKey(key)) {
                JOptionPane.showMessageDialog(this, "Tidak ada kunjungan Dipanggil!");
                return -1;
            }
            String diagSel = (String) cDiag.getSelectedItem();
            if (diagSel == null || !mapD.containsKey(diagSel)) {
                JOptionPane.showMessageDialog(this, "Pilih diagnosis dulu!");
                return -1;
            }
            int idK = mapK.get(key).intValue();
            String kd = mapD.get(diagSel);
            String tensiVal = tTensi.getText().trim();
            if (!tensiVal.isEmpty() && !tensiVal.matches("\\d{2,3}/\\d{2,3}")) {
                if (
                    JOptionPane.showConfirmDialog(
                        this,
                        "Tensi [" + tensiVal + "] tidak seperti 120/80. Tetap simpan?",
                        "Tensi",
                        JOptionPane.YES_NO_OPTION
                    ) !=
                    0
                ) return -1;
            }
            Connection k = Koneksi.getConnection();
            if (k == null) return -1;
            PreparedStatement p = k.prepareStatement(
                "INSERT INTO rekam_medis(id_kunjungan,subjektif,objektif,assessment,plan,kode_diagnosis) VALUES(?,?,?,?,?,?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            );
            p.setInt(1, idK);
            p.setString(2, tS.getText().trim());
            p.setString(3, tO.getText().trim());
            p.setString(4, tA.getText().trim());
            p.setString(5, tP.getText().trim());
            p.setString(6, kd);
            p.executeUpdate();
            ResultSet g = p.getGeneratedKeys();
            g.next();
            int idR = g.getInt(1);
            for (Map.Entry<String, String> e : odo.getData().entrySet()) {
                PreparedStatement o = k.prepareStatement(
                    "INSERT INTO odontogram(id_rekam,no_gigi,kondisi) VALUES(?,?,?)"
                );
                o.setInt(1, idR);
                o.setString(2, e.getKey());
                o.setString(3, e.getValue());
                o.executeUpdate();
            }
            try {
                PreparedStatement up = k.prepareStatement(
                    "UPDATE kunjungan SET tensi=?, status='Diperiksa' WHERE id_kunjungan=?"
                );
                up.setString(1, tensiVal);
                up.setInt(2, idK);
                up.executeUpdate();
            } catch (Exception ex) {
                k
                    .createStatement()
                    .executeUpdate(
                        "UPDATE kunjungan SET status='Diperiksa' WHERE id_kunjungan=" + idK
                    );
            }
            k.close();
            JOptionPane.showMessageDialog(this, "Tersimpan ID " + idR + " -> Diperiksa.");
            return idK;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return -1;
        }
    }

    void afterSimpan() {
        odo.reset();
        tS.setText("");
        tO.setText("");
        tA.setText("");
        tP.setText("");
        tTensi.setText("");
        loadKunj();
        loadPasienInfo();
        loadAwal();
        riwayat();
    }

    void simpanSaja() {
        int id = simpanData();
        if (id != -1) afterSimpan();
    }

    void simpanDanLanjut() {
        int idK = simpanData();
        if (idK == -1) return;
        afterSimpan();
        bukaTindakan(idK);
    }

    void bukaTindakan(int idK) {
        try {
            FormTindakanResep f = new FormTindakanResep(idK);
            f.setVisible(true);
            f.toFront();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
