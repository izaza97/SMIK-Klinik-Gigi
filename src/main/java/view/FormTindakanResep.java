package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import koneksi.Koneksi;
import utils.*;

public class FormTindakanResep extends JFrame {

    JComboBox<String> cKunj = new JComboBox<>();
    JTabbedPane tabPane = new JTabbedPane();
    JComboBox<String> cKatTind = new JComboBox<>(), cLayTind = new JComboBox<>();
    JTextField tCariTind = new JTextField(12), tGigi = new JTextField("36", 5), tKetTind =
        new JTextField(15);
    JLabel lTarifTind = new JLabel("Rp 0"), lblKetTind = new JLabel("-"), lblCountTind = new JLabel(
        ""
    );
    DefaultTableModel mT = new DefaultTableModel(
        new String[] { "Layanan", "Gigi", "Keterangan", "Tarif" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tbT = new JTable(mT);
    Map<String, String> mK = new HashMap<>(), mL_Tind = new HashMap<>(), ketTind = new HashMap<>();
    Map<String, Integer> hTind = new HashMap<>();
    List<String[]> listTind = new ArrayList<>();
    JComboBox<String> cKatProd = new JComboBox<>(), cLayProd = new JComboBox<>();
    JTextField tCariProd = new JTextField(12), tQty = new JTextField("1", 5);
    JLabel lHargaProd = new JLabel("Rp 0"), lSubProd = new JLabel("Subtotal: Rp 0"), lblKetProd =
        new JLabel("-"), lblCountProd = new JLabel("");
    DefaultTableModel mP = new DefaultTableModel(
        new String[] { "Produk", "Qty", "Harga", "Subtotal" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tbP = new JTable(mP);
    Map<String, String> mL_Prod = new HashMap<>(), ketProd = new HashMap<>();
    Map<String, Integer> hProd = new HashMap<>();
    List<String[]> listProd = new ArrayList<>();
    JTextArea txtResep = new JTextArea(14, 30);
    JLabel lblCountResep = new JLabel("0 karakter");
    JComboBox<String> cDiskon = new JComboBox<>(
        new String[] { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%", "40%", "45%", "50%" }
    );
    JTextField tDiskonKet = new JTextField(18);
    JLabel lTotalJasa = new JLabel("Jasa: Rp 0"), lTotalProduk = new JLabel(
        "Produk: Rp 0"
    ), lDiskonRp = new JLabel("Diskon 0%: -Rp 0"), lGrand = new JLabel("Grand: Rp 0");
    JLabel lblInfoRM = new JLabel("-");

    public FormTindakanResep() {
        setTitle("Tindakan, Produk & Resep");
        setSize(1220, 800);
        setLocationRelativeTo(null);
        Layar.fullscreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Tema.BG);
        setLayout(new BorderLayout());
        add(
            Tema.header(
                "Tindakan, Produk & Resep",
                "Tahap 4 | Satu tempat 3 tab | Diskon dokter 0%-50%"
            ),
            BorderLayout.NORTH
        );
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(Tema.BG);
        main.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));
        JPanel topWrap = new JPanel(new BorderLayout(6, 6));
        topWrap.setOpaque(false);
        topWrap.setBackground(Tema.BG);
        JPanel top = Tema.kartu();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        top.add(new JLabel("Kunjungan Diperiksa:"));
        cKunj.setPreferredSize(new Dimension(320, 32));
        top.add(cKunj);
        JButton bRef = new JButton("Refresh");
        Tema.secondary(bRef);
        bRef.setPreferredSize(new Dimension(110, 32));
        top.add(bRef);
        JLabel hint = new JLabel(
            "Tab 1 Tindakan, Tab 2 Produk, Tab 3 Resep, pilih diskon, SIMPAN SEMUA."
        );
        hint.setForeground(Tema.ABU);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        top.add(hint);
        topWrap.add(top, BorderLayout.CENTER);
        JPanel infoPanel = Tema.kartu();
        infoPanel.setLayout(new BorderLayout());
        lblInfoRM.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfoRM.setForeground(Tema.PRIMER_GELAP);
        infoPanel.add(lblInfoRM, BorderLayout.CENTER);
        topWrap.add(infoPanel, BorderLayout.SOUTH);
        main.add(topWrap, BorderLayout.NORTH);
        JPanel pTind = new JPanel(new BorderLayout(8, 8));
        pTind.setBackground(Color.WHITE);
        pTind.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel fTindTop = new JPanel(new BorderLayout(8, 8));
        fTindTop.setOpaque(false);
        JPanel flT = new JPanel(new GridLayout(7, 2, 6, 6));
        flT.setOpaque(false);
        flT.add(new JLabel("Kategori:"));
        flT.add(cKatTind);
        flT.add(new JLabel("Cari Kode/Nama:"));
        JPanel pcT = new JPanel(new BorderLayout(4, 0));
        pcT.setOpaque(false);
        pcT.add(tCariTind, BorderLayout.CENTER);
        JButton bCariT = new JButton("Cari");
        Tema.secondary(bCariT);
        bCariT.setPreferredSize(new Dimension(70, 28));
        pcT.add(bCariT, BorderLayout.EAST);
        flT.add(pcT);
        flT.add(new JLabel("Layanan:"));
        flT.add(cLayTind);
        flT.add(new JLabel("Ket Master:"));
        lblKetTind.setForeground(Tema.ABU);
        lblKetTind.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        flT.add(lblKetTind);
        flT.add(new JLabel("No Gigi (cth 36):"));
        flT.add(tGigi);
        flT.add(new JLabel("Ket/Diagnosa:"));
        flT.add(tKetTind);
        flT.add(new JLabel("Tarif:"));
        JPanel pTT = new JPanel(new BorderLayout());
        pTT.setOpaque(false);
        lTarifTind.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pTT.add(lTarifTind, BorderLayout.WEST);
        lblCountTind.setForeground(Tema.ABU);
        lblCountTind.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        pTT.add(lblCountTind, BorderLayout.EAST);
        flT.add(pTT);
        fTindTop.add(flT, BorderLayout.CENTER);
        JPanel blT = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        blT.setOpaque(false);
        JButton bAddT = new JButton("+ Tambah Tindakan");
        Tema.primary(bAddT);
        bAddT.setPreferredSize(new Dimension(180, 34));
        JButton bDelT = new JButton("Hapus");
        Tema.secondary(bDelT);
        bDelT.setPreferredSize(new Dimension(100, 34));
        blT.add(bAddT);
        blT.add(bDelT);
        fTindTop.add(blT, BorderLayout.SOUTH);
        pTind.add(fTindTop, BorderLayout.NORTH);
        Tema.styleTable(tbT);
        pTind.add(new JScrollPane(tbT), BorderLayout.CENTER);
        JPanel pProd = new JPanel(new BorderLayout(8, 8));
        pProd.setBackground(Color.WHITE);
        pProd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel fProdTop = new JPanel(new BorderLayout(8, 8));
        fProdTop.setOpaque(false);
        JPanel flP = new JPanel(new GridLayout(6, 2, 6, 6));
        flP.setOpaque(false);
        flP.add(new JLabel("Kategori:"));
        flP.add(cKatProd);
        flP.add(new JLabel("Cari Kode/Nama:"));
        JPanel pcP = new JPanel(new BorderLayout(4, 0));
        pcP.setOpaque(false);
        pcP.add(tCariProd, BorderLayout.CENTER);
        JButton bCariP = new JButton("Cari");
        Tema.secondary(bCariP);
        bCariP.setPreferredSize(new Dimension(70, 28));
        pcP.add(bCariP, BorderLayout.EAST);
        flP.add(pcP);
        flP.add(new JLabel("Produk:"));
        flP.add(cLayProd);
        flP.add(new JLabel("Keterangan:"));
        lblKetProd.setForeground(Tema.ABU);
        lblKetProd.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        flP.add(lblKetProd);
        flP.add(new JLabel("Harga:"));
        JPanel pHP = new JPanel(new BorderLayout());
        pHP.setOpaque(false);
        lHargaProd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pHP.add(lHargaProd, BorderLayout.WEST);
        lblCountProd.setForeground(Tema.ABU);
        lblCountProd.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        pHP.add(lblCountProd, BorderLayout.EAST);
        flP.add(pHP);
        flP.add(new JLabel("Jumlah:"));
        flP.add(tQty);
        fProdTop.add(flP, BorderLayout.CENTER);
        JPanel blP = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        blP.setOpaque(false);
        JButton bAddP = new JButton("+ Tambah Produk");
        Tema.primary(bAddP);
        bAddP.setPreferredSize(new Dimension(180, 34));
        JButton bDelP = new JButton("Hapus");
        Tema.secondary(bDelP);
        bDelP.setPreferredSize(new Dimension(100, 34));
        lSubProd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lSubProd.setForeground(Tema.PRIMER_GELAP);
        blP.add(bAddP);
        blP.add(bDelP);
        blP.add(lSubProd);
        fProdTop.add(blP, BorderLayout.SOUTH);
        pProd.add(fProdTop, BorderLayout.NORTH);
        Tema.styleTable(tbP);
        pProd.add(new JScrollPane(tbP), BorderLayout.CENTER);
        JPanel pRes = new JPanel(new BorderLayout(8, 8));
        pRes.setBackground(Color.WHITE);
        pRes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtResep.setLineWrap(true);
        txtResep.setWrapStyleWord(true);
        txtResep.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pRes.add(new JScrollPane(txtResep), BorderLayout.CENTER);
        JPanel prBot = new JPanel(new BorderLayout(8, 4));
        prBot.setOpaque(false);
        JPanel prBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        prBtn.setOpaque(false);
        JButton bTpl = new JButton("Isi Contoh");
        Tema.secondary(bTpl);
        JButton bClear = new JButton("Bersihkan");
        Tema.secondary(bClear);
        prBtn.add(bTpl);
        prBtn.add(bClear);
        prBot.add(prBtn, BorderLayout.WEST);
        lblCountResep.setForeground(Tema.ABU);
        lblCountResep.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        prBot.add(lblCountResep, BorderLayout.EAST);
        pRes.add(prBot, BorderLayout.SOUTH);
        tabPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabPane.add("1. Tindakan Medis", pTind);
        tabPane.add("2. Produk", pProd);
        tabPane.add("3. Resep", pRes);
        main.add(tabPane, BorderLayout.CENTER);
        JPanel southWrap = new JPanel(new BorderLayout(8, 8));
        southWrap.setOpaque(false);
        southWrap.setBackground(Tema.BG);
        JPanel discBar = Tema.kartu();
        discBar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        discBar.add(new JLabel("Diskon Dokter:"));
        cDiskon.setPreferredSize(new Dimension(90, 30));
        discBar.add(cDiskon);
        discBar.add(new JLabel("Alasan:"));
        tDiskonKet.setPreferredSize(new Dimension(200, 30));
        discBar.add(tDiskonKet);
        JLabel hintD = new JLabel("5% kelipatan sampai 50%, otomatis ke kasir dan invoice.");
        hintD.setForeground(Tema.ABU);
        hintD.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        discBar.add(hintD);
        southWrap.add(discBar, BorderLayout.NORTH);
        JPanel bot = Tema.kartu();
        bot.setLayout(new BorderLayout(10, 10));
        JPanel tot = new JPanel(new GridLayout(4, 1));
        tot.setOpaque(false);
        lTotalJasa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTotalJasa.setForeground(Tema.PRIMER_GELAP);
        lTotalProduk.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTotalProduk.setForeground(Tema.PRIMER_GELAP);
        lDiskonRp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lDiskonRp.setForeground(new Color(220, 38, 38));
        lGrand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lGrand.setForeground(Tema.PRIMER_GELAP);
        tot.add(lTotalJasa);
        tot.add(lTotalProduk);
        tot.add(lDiskonRp);
        tot.add(lGrand);
        bot.add(tot, BorderLayout.WEST);
        JPanel bb = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bb.setOpaque(false);
        JButton bCetak = new JButton("Cetak Resep");
        Tema.secondary(bCetak);
        bCetak.setPreferredSize(new Dimension(140, 38));
        JButton bSave = new JButton("SIMPAN SEMUA");
        Tema.success(bSave);
        bSave.setPreferredSize(new Dimension(220, 42));
        bb.add(bCetak);
        bb.add(bSave);
        bot.add(bb, BorderLayout.EAST);
        southWrap.add(bot, BorderLayout.CENTER);
        main.add(southWrap, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);
        bAddT.addActionListener(e -> tambahTind());
        bDelT.addActionListener(e -> {
            int i = tbT.getSelectedRow();
            if (i >= 0) {
                mT.removeRow(i);
                hitung();
            }
        });
        bAddP.addActionListener(e -> tambahProd());
        bDelP.addActionListener(e -> {
            int i = tbP.getSelectedRow();
            if (i >= 0) {
                mP.removeRow(i);
                hitung();
            }
        });
        bSave.addActionListener(e -> simpanSemua());
        bCetak.addActionListener(e -> cetakResep());
        bRef.addActionListener(e -> {
            loadKunj();
            loadExisting();
        });
        bTpl.addActionListener(e ->
            txtResep.setText(
                "R/ Amoxicillin 500mg No. X\nS 3 dd 1 sesudah makan\n\nR/ Ibuprofen 400mg No. X\nS 3 dd 1 bila perlu nyeri\n\nCatatan: kontrol 7 hari lagi."
            )
        );
        bClear.addActionListener(e -> txtResep.setText(""));
        txtResep
            .getDocument()
            .addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        updateResepCount();
                    }

                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        updateResepCount();
                    }

                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        updateResepCount();
                    }
                }
            );
        bCariT.addActionListener(e -> filterTind());
        tCariTind.addActionListener(e -> filterTind());
        cKatTind.addActionListener(e -> filterTind());
        cLayTind.addActionListener(e -> updateTindInfo());
        bCariP.addActionListener(e -> filterProd());
        tCariProd.addActionListener(e -> filterProd());
        cKatProd.addActionListener(e -> filterProd());
        cLayProd.addActionListener(e -> updateProdInfo());
        tQty
            .getDocument()
            .addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        updateProdInfo();
                    }

                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        updateProdInfo();
                    }

                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        updateProdInfo();
                    }
                }
            );
        cDiskon.addActionListener(e -> hitung());
        cKunj.addActionListener(e -> loadExisting());
        loadKunj();
        loadAllTind();
        loadAllProd();
        updateResepCount();
        hitung();
    }

    public FormTindakanResep(int idKunjungan) {
        this();
        pilihKunjungan(idKunjungan);
        if (tabPane != null) tabPane.setSelectedIndex(0);
    }

    public void pilihTabProduk() {
        try {
            if (tabPane != null) tabPane.setSelectedIndex(1);
        } catch (Exception e) {}
    }

    public void pilihKunjungan(int idKunjungan) {
        if (idKunjungan <= 0) return;
        try {
            loadKunj();
            boolean ketemu = false;
            for (int i = 0; i < cKunj.getItemCount(); i++) {
                String s = cKunj.getItemAt(i);
                if (
                    s != null &&
                    (s.startsWith(idKunjungan + " -") || s.startsWith(idKunjungan + "-"))
                ) {
                    cKunj.setSelectedIndex(i);
                    ketemu = true;
                    break;
                }
            }
            if (!ketemu) {
                try (
                    Connection k = Koneksi.getConnection();
                    PreparedStatement p = k.prepareStatement(
                        "SELECT k.id_kunjungan,p.nama FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm WHERE k.id_kunjungan=?"
                    )
                ) {
                    p.setInt(1, idKunjungan);
                    ResultSet r = p.executeQuery();
                    if (r.next()) {
                        String s = r.getInt(1) + " - " + r.getString(2);
                        cKunj.addItem(s);
                        mK.put(s, String.valueOf(r.getInt(1)));
                        cKunj.setSelectedItem(s);
                    }
                }
            }
            loadExisting();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    String fmt(int v) {
        return String.format("%,d", v).replace(",", ".");
    }

    int getDiskonPersen() {
        try {
            String s = (String) cDiskon.getSelectedItem();
            if (s == null) return 0;
            return Integer.parseInt(s.replace("%", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    void setDiskonPersen(int p) {
        String t = p + "%";
        for (int i = 0; i < cDiskon.getItemCount(); i++) {
            if (t.equals(cDiskon.getItemAt(i))) {
                cDiskon.setSelectedIndex(i);
                return;
            }
        }
        cDiskon.setSelectedIndex(0);
    }

    void updateResepCount() {
        lblCountResep.setText(txtResep.getText().length() + " karakter");
    }

    int getId() {
        String s = (String) cKunj.getSelectedItem();
        if (s == null || !mK.containsKey(s)) return -1;
        return Integer.parseInt(mK.get(s));
    }

    void loadKunj() {
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            cKunj.removeAllItems();
            mK.clear();
            ResultSet r = k
                .createStatement()
                .executeQuery(
                    "SELECT k.id_kunjungan,p.nama FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm WHERE k.status IN ('Diperiksa','Menunggu bayar') ORDER BY k.id_kunjungan DESC"
                );
            while (r.next()) {
                String s = r.getInt(1) + " - " + r.getString(2);
                cKunj.addItem(s);
                mK.put(s, String.valueOf(r.getInt(1)));
            }
            if (cKunj.getItemCount() == 0) cKunj.addItem("TIDAK ADA");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void loadAllTind() {
        listTind.clear();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            ResultSet rs = k
                .createStatement()
                .executeQuery(
                    "SELECT kode_layanan,nama_layanan,tarif,kategori,keterangan FROM layanan WHERE kategori IS NULL OR kategori NOT IN ('Produk','Obat') ORDER BY kategori,kode_layanan"
                );
            while (rs.next()) listTind.add(
                new String[] {
                    rs.getString(1),
                    rs.getString(2),
                    String.valueOf(rs.getInt(3)),
                    rs.getString(4) == null ? "Umum" : rs.getString(4),
                    rs.getString(5) == null ? "" : rs.getString(5)
                }
            );
            cKatTind.removeAllItems();
            cKatTind.addItem("SEMUA");
            Set<String> s = new LinkedHashSet<>();
            for (String[] r : listTind) s.add(r[3]);
            for (String x : s) cKatTind.addItem(x);
            filterTind();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void filterTind() {
        if (listTind == null) return;
        String kat = (String) cKatTind.getSelectedItem();
        if (kat == null) kat = "SEMUA";
        String key = tCariTind.getText().trim().toLowerCase();
        cLayTind.removeAllItems();
        mL_Tind.clear();
        hTind.clear();
        ketTind.clear();
        int tot = 0;
        for (String[] r : listTind) {
            String kode = r[0], nama = r[1], tarifS = r[2], kk = r[3], ket = r[4];
            if (!kat.equals("SEMUA") && !kat.equals(kk)) continue;
            if (
                !key.isEmpty() &&
                !(kode.toLowerCase().contains(key) ||
                    nama.toLowerCase().contains(key) ||
                    ket.toLowerCase().contains(key))
            ) continue;
            String disp = kode + " - " + nama;
            cLayTind.addItem(disp);
            mL_Tind.put(disp, kode);
            try {
                hTind.put(disp, Integer.parseInt(tarifS));
            } catch (Exception ex) {
                hTind.put(disp, 0);
            }
            ketTind.put(disp, ket);
            tot++;
        }
        lblCountTind.setText(tot + " tindakan");
        if (cLayTind.getItemCount() > 0) updateTindInfo();
    }

    void updateTindInfo() {
        String s = (String) cLayTind.getSelectedItem();
        if (s != null && hTind.containsKey(s)) {
            lTarifTind.setText("Rp " + fmt(hTind.get(s)));
            String ket = ketTind.get(s);
            if (ket == null || ket.isEmpty()) ket = "-";
            lblKetTind.setText(ket);
            lblKetTind.setToolTipText(ket);
        }
    }

    void tambahTind() {
        String s = (String) cLayTind.getSelectedItem();
        if (s == null || !hTind.containsKey(s)) {
            JOptionPane.showMessageDialog(this, "Pilih layanan valid!");
            return;
        }
        if (tGigi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi gigi cth 36!");
            return;
        }
        mT.addRow(
            new Object[] { s, tGigi.getText().trim(), tKetTind.getText().trim(), hTind.get(s) }
        );
        hitung();
    }

    void loadAllProd() {
        listProd.clear();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            ResultSet rs = k
                .createStatement()
                .executeQuery(
                    "SELECT kode_layanan,nama_layanan,tarif,kategori,keterangan FROM layanan WHERE kategori IN ('Produk','Obat') ORDER BY kategori,kode_layanan"
                );
            while (rs.next()) listProd.add(
                new String[] {
                    rs.getString(1),
                    rs.getString(2),
                    String.valueOf(rs.getInt(3)),
                    rs.getString(4) == null ? "Produk" : rs.getString(4),
                    rs.getString(5) == null ? "" : rs.getString(5)
                }
            );
            cKatProd.removeAllItems();
            cKatProd.addItem("SEMUA");
            Set<String> s = new LinkedHashSet<>();
            for (String[] r : listProd) s.add(r[3]);
            for (String x : s) cKatProd.addItem(x);
            filterProd();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void filterProd() {
        if (listProd == null) return;
        String kat = (String) cKatProd.getSelectedItem();
        if (kat == null) kat = "SEMUA";
        String key = tCariProd.getText().trim().toLowerCase();
        cLayProd.removeAllItems();
        mL_Prod.clear();
        hProd.clear();
        ketProd.clear();
        int tot = 0;
        for (String[] r : listProd) {
            String kode = r[0], nama = r[1], tarifS = r[2], kk = r[3], ket = r[4];
            if (!kat.equals("SEMUA") && !kat.equals(kk)) continue;
            if (
                !key.isEmpty() &&
                !(kode.toLowerCase().contains(key) ||
                    nama.toLowerCase().contains(key) ||
                    ket.toLowerCase().contains(key))
            ) continue;
            String disp = kode + " - " + nama;
            cLayProd.addItem(disp);
            mL_Prod.put(disp, kode);
            try {
                hProd.put(disp, Integer.parseInt(tarifS));
            } catch (Exception ex) {
                hProd.put(disp, 0);
            }
            ketProd.put(disp, ket);
            tot++;
        }
        lblCountProd.setText(tot + " produk");
        if (cLayProd.getItemCount() > 0) updateProdInfo();
    }

    void updateProdInfo() {
        String s = (String) cLayProd.getSelectedItem();
        if (s == null || !hProd.containsKey(s)) return;
        int hr = hProd.get(s);
        lHargaProd.setText("Rp " + fmt(hr));
        String ket = ketProd.get(s);
        if (ket == null || ket.isEmpty()) ket = "-";
        lblKetProd.setText(ket);
        lblKetProd.setToolTipText(ket);
        int qty = 1;
        try {
            qty = Integer.parseInt(tQty.getText().trim());
        } catch (Exception e) {
            qty = 1;
        }
        if (qty < 1) qty = 1;
        lSubProd.setText("Subtotal: Rp " + fmt(hr * qty));
    }

    void tambahProd() {
        String s = (String) cLayProd.getSelectedItem();
        if (s == null || !hProd.containsKey(s)) {
            JOptionPane.showMessageDialog(this, "Pilih produk valid!");
            return;
        }
        int qty = 1;
        try {
            qty = Integer.parseInt(tQty.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Qty angka!");
            return;
        }
        if (qty < 1) {
            JOptionPane.showMessageDialog(this, "Qty min 1!");
            return;
        }
        int hr = hProd.get(s);
        mP.addRow(new Object[] { s, qty, hr, hr * qty });
        hitung();
    }

    void hitung() {
        int tj = 0;
        for (int i = 0; i < mT.getRowCount(); i++) tj += Integer.parseInt(
            mT.getValueAt(i, 3).toString()
        );
        int tp = 0;
        for (int i = 0; i < mP.getRowCount(); i++) tp += Integer.parseInt(
            mP.getValueAt(i, 3).toString()
        );
        int sub = tj + tp;
        int persen = getDiskonPersen();
        int diskonRp = sub * persen / 100;
        int grand = sub - diskonRp;
        lTotalJasa.setText("Jasa: Rp " + fmt(tj));
        lTotalProduk.setText("Produk: Rp " + fmt(tp));
        lDiskonRp.setText("Diskon " + persen + "%: -Rp " + fmt(diskonRp));
        lGrand.setText("Grand (setelah diskon): Rp " + fmt(grand));
    }

    void loadExisting() {
        int id = getId();
        if (id == -1) {
            mT.setRowCount(0);
            mP.setRowCount(0);
            txtResep.setText("");
            setDiskonPersen(0);
            tDiskonKet.setText("");
            lblInfoRM.setText("-");
            hitung();
            return;
        }
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            mT.setRowCount(0);
            try {
                PreparedStatement pt = k.prepareStatement(
                    "SELECT l.kode_layanan,l.nama_layanan,t.no_gigi,t.keterangan,t.tarif FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=? AND (l.kategori IS NULL OR l.kategori NOT IN ('Produk','Obat'))"
                );
                pt.setInt(1, id);
                ResultSet r = pt.executeQuery();
                while (r.next()) mT.addRow(
                    new Object[] {
                        r.getString(1) + " - " + r.getString(2),
                        r.getString(3),
                        r.getString(4) == null ? "" : r.getString(4),
                        r.getInt(5)
                    }
                );
            } catch (Exception ex) {
                PreparedStatement pt2 = k.prepareStatement(
                    "SELECT l.kode_layanan,l.nama_layanan,t.no_gigi,t.tarif FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=? AND (l.kategori IS NULL OR l.kategori NOT IN ('Produk','Obat'))"
                );
                pt2.setInt(1, id);
                ResultSet r2 = pt2.executeQuery();
                while (r2.next()) mT.addRow(
                    new Object[] {
                        r2.getString(1) + " - " + r2.getString(2),
                        r2.getString(3),
                        "",
                        r2.getInt(4)
                    }
                );
            }
            mP.setRowCount(0);
            try {
                PreparedStatement ps = k.prepareStatement(
                    "SELECT l.kode_layanan,l.nama_layanan,t.jumlah,t.tarif,t.tarif*t.jumlah FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=? AND l.kategori IN ('Produk','Obat')"
                );
                ps.setInt(1, id);
                ResultSet q = ps.executeQuery();
                while (q.next()) mP.addRow(
                    new Object[] {
                        q.getString(1) + " - " + q.getString(2),
                        q.getInt(3),
                        q.getInt(4),
                        q.getInt(5)
                    }
                );
            } catch (Exception ex) {
                PreparedStatement ps2 = k.prepareStatement(
                    "SELECT l.kode_layanan,l.nama_layanan,t.tarif FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=? AND l.kategori IN ('Produk','Obat')"
                );
                ps2.setInt(1, id);
                ResultSet q2 = ps2.executeQuery();
                while (q2.next()) mP.addRow(
                    new Object[] {
                        q2.getString(1) + " - " + q2.getString(2),
                        1,
                        q2.getInt(3),
                        q2.getInt(3)
                    }
                );
            }
            txtResep.setText("");
            try {
                PreparedStatement pr = k.prepareStatement(
                    "SELECT isi_resep FROM resep WHERE id_kunjungan=?"
                );
                pr.setInt(1, id);
                ResultSet qr = pr.executeQuery();
                if (qr.next() && qr.getString(1) != null) txtResep.setText(qr.getString(1));
            } catch (Exception ex) {}
            try {
                PreparedStatement pd = k.prepareStatement(
                    "SELECT diskon_persen,diskon_ket FROM kunjungan WHERE id_kunjungan=?"
                );
                pd.setInt(1, id);
                ResultSet rd = pd.executeQuery();
                if (rd.next()) {
                    int dp = 0;
                    try {
                        dp = rd.getInt(1);
                    } catch (Exception e2) {
                        dp = 0;
                    }
                    String dk = rd.getString(2);
                    if (dk == null) dk = "";
                    setDiskonPersen(dp);
                    tDiskonKet.setText(dk);
                } else {
                    setDiskonPersen(0);
                    tDiskonKet.setText("");
                }
            } catch (Exception ex) {
                setDiskonPersen(0);
                tDiskonKet.setText("");
            }
            try {
                PreparedStatement pi = k.prepareStatement(
                    "SELECT pa.no_rm,pa.nama,r.subjektif,r.kode_diagnosis,d.nama_diagnosis FROM kunjungan kj JOIN pasien pa ON kj.no_rm=pa.no_rm LEFT JOIN rekam_medis r ON r.id_kunjungan=kj.id_kunjungan LEFT JOIN diagnosis d ON r.kode_diagnosis=d.kode_diagnosis WHERE kj.id_kunjungan=?"
                );
                pi.setInt(1, id);
                ResultSet ri = pi.executeQuery();
                if (ri.next()) {
                    String sRm = ri.getString(3) == null
                        ? ""
                        : ri.getString(3).replaceAll("\\n", " ");
                    if (sRm.length() > 80) sRm = sRm.substring(0, 80) + "...";
                    String dg =
                        (ri.getString(4) == null ? "-" : ri.getString(4)) +
                        " - " +
                        (ri.getString(5) == null ? "" : ri.getString(5));
                    lblInfoRM.setText(
                        "Dari RM: " +
                        ri.getString(1) +
                        " - " +
                        ri.getString(2) +
                        " | S: " +
                        sRm +
                        " | Dx: " +
                        dg
                    );
                    lblInfoRM.setToolTipText(lblInfoRM.getText());
                } else {
                    lblInfoRM.setText("-");
                }
            } catch (Exception ex) {
                lblInfoRM.setText("-");
            }
            hitung();
            updateResepCount();
        } catch (Exception e) {}
    }

    void simpanSemua() {
        int id = getId();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Tidak ada kunjungan!");
            return;
        }
        if (mT.getRowCount() == 0 && mP.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tindakan dan Produk kosong! Isi salah satu.");
            return;
        }
        int persen = getDiskonPersen();
        String dket = tDiskonKet.getText().trim();
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            k
                .prepareStatement(
                    "DELETE t FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=" +
                    id +
                    " AND (l.kategori IS NULL OR l.kategori NOT IN ('Produk','Obat'))"
                )
                .executeUpdate();
            for (int i = 0; i < mT.getRowCount(); i++) {
                String disp = mT.getValueAt(i, 0).toString();
                String kode = mL_Tind.get(disp);
                if (kode == null) kode = disp.split(" - ")[0];
                String gigi = mT.getValueAt(i, 1).toString();
                String ket = mT.getValueAt(i, 2).toString();
                int tarif = Integer.parseInt(mT.getValueAt(i, 3).toString());
                try {
                    PreparedStatement p = k.prepareStatement(
                        "INSERT INTO tindakan_kunjungan(id_kunjungan,kode_layanan,no_gigi,tarif,jumlah,keterangan) VALUES(?,?,?,?,1,?)"
                    );
                    p.setInt(1, id);
                    p.setString(2, kode);
                    p.setString(3, gigi);
                    p.setInt(4, tarif);
                    p.setString(5, ket);
                    p.executeUpdate();
                } catch (Exception ex) {
                    PreparedStatement p2 = k.prepareStatement(
                        "INSERT INTO tindakan_kunjungan(id_kunjungan,kode_layanan,no_gigi,tarif) VALUES(?,?,?,?)"
                    );
                    p2.setInt(1, id);
                    p2.setString(2, kode);
                    p2.setString(3, gigi);
                    p2.setInt(4, tarif);
                    p2.executeUpdate();
                }
            }
            k
                .prepareStatement(
                    "DELETE t FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=" +
                    id +
                    " AND l.kategori IN ('Produk','Obat')"
                )
                .executeUpdate();
            for (int i = 0; i < mP.getRowCount(); i++) {
                String disp = mP.getValueAt(i, 0).toString();
                String kode = mL_Prod.get(disp);
                if (kode == null) kode = disp.split(" - ")[0];
                int qty = Integer.parseInt(mP.getValueAt(i, 1).toString());
                int hr = Integer.parseInt(mP.getValueAt(i, 2).toString());
                try {
                    PreparedStatement p = k.prepareStatement(
                        "INSERT INTO tindakan_kunjungan(id_kunjungan,kode_layanan,no_gigi,tarif,jumlah,keterangan) VALUES(?,?,?,?,?,?)"
                    );
                    p.setInt(1, id);
                    p.setString(2, kode);
                    p.setString(3, "-");
                    p.setInt(4, hr);
                    p.setInt(5, qty);
                    p.setString(6, "");
                    p.executeUpdate();
                } catch (Exception ex) {
                    PreparedStatement p2 = k.prepareStatement(
                        "INSERT INTO tindakan_kunjungan(id_kunjungan,kode_layanan,no_gigi,tarif,jumlah) VALUES(?,?,?,?,?)"
                    );
                    p2.setInt(1, id);
                    p2.setString(2, kode);
                    p2.setString(3, "-");
                    p2.setInt(4, hr);
                    p2.setInt(5, qty);
                    p2.executeUpdate();
                }
            }
            try {
                k.createStatement().executeUpdate("DELETE FROM resep WHERE id_kunjungan=" + id);
                String isi = txtResep.getText().trim();
                if (!isi.isEmpty()) {
                    PreparedStatement pr = k.prepareStatement(
                        "INSERT INTO resep(id_kunjungan,isi_resep) VALUES(?,?)"
                    );
                    pr.setInt(1, id);
                    pr.setString(2, isi);
                    pr.executeUpdate();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal resep: " + ex.getMessage());
                return;
            }
            try {
                PreparedStatement up = k.prepareStatement(
                    "UPDATE kunjungan SET diskon_persen=?, diskon_ket=?, status='Menunggu bayar' WHERE id_kunjungan=?"
                );
                up.setInt(1, persen);
                up.setString(2, dket);
                up.setInt(3, id);
                up.executeUpdate();
            } catch (Exception ex) {
                k
                    .createStatement()
                    .executeUpdate(
                        "UPDATE kunjungan SET status='Menunggu bayar' WHERE id_kunjungan=" + id
                    );
            }
            JOptionPane.showMessageDialog(
                this,
                "Tersimpan! " + lGrand.getText() + " (Diskon " + persen + "%)"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void cetakResep() {
        int id = getId();
        if (id == -1) return;
        String isi = txtResep.getText().trim();
        if (isi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Resep kosong!");
            return;
        }
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement cek = k.prepareStatement(
                "SELECT isi_resep FROM resep WHERE id_kunjungan=?"
            );
            cek.setInt(1, id);
            ResultSet r = cek.executeQuery();
            if (!r.next()) {
                PreparedStatement ins = k.prepareStatement(
                    "INSERT INTO resep(id_kunjungan,isi_resep) VALUES(?,?)"
                );
                ins.setInt(1, id);
                ins.setString(2, isi);
                ins.executeUpdate();
            } else {
                String db = r.getString(1) == null ? "" : r.getString(1);
                if (!db.equals(isi)) {
                    PreparedStatement up = k.prepareStatement(
                        "UPDATE resep SET isi_resep=? WHERE id_kunjungan=?"
                    );
                    up.setString(1, isi);
                    up.setInt(2, id);
                    up.executeUpdate();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }
        Map par = new HashMap();
        par.put("id_kunjungan", id);
        CetakLaporan.cetak("Resep.jasper", par);
    }
}
