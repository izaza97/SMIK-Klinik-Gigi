package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import koneksi.Koneksi;
import utils.*;
import model.Session;

public class FormPembayaran extends JFrame {

    JComboBox<String> cKunj = new JComboBox<>(), cMet = new JComboBox<>(
        new String[] { "Tunai", "QRIS", "Transfer", "EDC" }
    );
    JLabel lTotal = new JLabel("Rp 0"), lRinci = new JLabel("Jasa: 0 + Produk: 0"), lDiskonInfo =
        new JLabel("Diskon 0%");
    DefaultTableModel m = new DefaultTableModel(
        new String[] { "Layanan", "Gigi", "Qty", "Subtotal" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tb = new JTable(m);
    Map<String, Integer> map = new HashMap<>();
    int jasa = 0, produk = 0, subtotal = 0, diskonPersen = 0, diskonRp = 0, grand = 0;
    String diskonKet = "";
    JTextField tCariRiw = new JTextField(18);
    DefaultTableModel mRiw = new DefaultTableModel(
        new String[] { "No Kuitansi", "Tanggal", "Pasien", "Dokter", "Total", "Metode" },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JTable tbRiw = new JTable(mRiw);

    public FormPembayaran() {
        setTitle("Pembayaran");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        Layar.fullscreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Tema.BG);
        setLayout(new BorderLayout());
        add(
            Tema.header("Pembayaran & Kwitansi", "Tahap 5 | Invoice diskon dokter | Cetak ulang"),
            BorderLayout.NORTH
        );
        JTabbedPane tab = new JTabbedPane();
        tab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPanel pBayar = new JPanel(new BorderLayout(12, 12));
        pBayar.setBackground(Tema.BG);
        pBayar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel top = Tema.kartu();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        top.add(new JLabel("Menunggu Bayar:"));
        cKunj.setPreferredSize(new Dimension(260, 32));
        top.add(cKunj);
        top.add(new JLabel("Metode:"));
        cMet.setPreferredSize(new Dimension(130, 32));
        top.add(cMet);
        JButton bH = new JButton("Hitung");
        Tema.primary(bH);
        bH.setPreferredSize(new Dimension(110, 32));
        top.add(bH);
        JButton bR = new JButton("Refresh");
        Tema.secondary(bR);
        bR.setPreferredSize(new Dimension(110, 32));
        top.add(bR);
        pBayar.add(top, BorderLayout.NORTH);
        JPanel card = Tema.kartu();
        card.setLayout(new BorderLayout(8, 8));
        JLabel lbl = new JLabel("Rincian Jasa + Produk:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl, BorderLayout.NORTH);
        Tema.styleTable(tb);
        card.add(new JScrollPane(tb), BorderLayout.CENTER);
        pBayar.add(card, BorderLayout.CENTER);
        JPanel bot = Tema.kartu();
        bot.setLayout(new BorderLayout(10, 10));
        JPanel lt = new JPanel(new GridLayout(3, 1));
        lt.setOpaque(false);
        lTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lTotal.setForeground(Tema.PRIMER_GELAP);
        lRinci.setForeground(Tema.ABU);
        lDiskonInfo.setForeground(new Color(220, 38, 38));
        lDiskonInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lt.add(lTotal);
        lt.add(lRinci);
        lt.add(lDiskonInfo);
        bot.add(lt, BorderLayout.WEST);
        JButton by = new JButton("BAYAR + CETAK");
        Tema.success(by);
        by.setPreferredSize(new Dimension(240, 48));
        bot.add(by, BorderLayout.EAST);
        pBayar.add(bot, BorderLayout.SOUTH);
        JPanel pRiw = new JPanel(new BorderLayout(12, 12));
        pRiw.setBackground(Tema.BG);
        pRiw.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel topR = Tema.kartu();
        topR.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topR.add(new JLabel("Cari No/Nama:"));
        topR.add(tCariRiw);
        JButton bCariR = new JButton("Cari");
        Tema.primary(bCariR);
        topR.add(bCariR);
        JButton bRefR = new JButton("Refresh");
        Tema.secondary(bRefR);
        topR.add(bRefR);
        JLabel hint = new JLabel("Double-click untuk cetak ulang.");
        hint.setForeground(Tema.ABU);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        topR.add(hint);
        pRiw.add(topR, BorderLayout.NORTH);
        JPanel cardR = Tema.kartu();
        cardR.setLayout(new BorderLayout(8, 8));
        Tema.styleTable(tbRiw);
        cardR.add(new JScrollPane(tbRiw), BorderLayout.CENTER);
        pRiw.add(cardR, BorderLayout.CENTER);
        JPanel botR = Tema.kartu();
        botR.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton bUlang = new JButton("CETAK ULANG");
        Tema.primary(bUlang);
        bUlang.setPreferredSize(new Dimension(180, 38));
        botR.add(bUlang);
        pRiw.add(botR, BorderLayout.SOUTH);
        tab.add("Bayar Baru", pBayar);
        tab.add("Riwayat & Cetak Ulang", pRiw);
        add(tab, BorderLayout.CENTER);
        bH.addActionListener(e -> hitung());
        by.addActionListener(e -> bayar());
        cKunj.addActionListener(e -> hitung());
        bR.addActionListener(e -> loadCombo());
        bCariR.addActionListener(e -> loadRiwayat(tCariRiw.getText()));
        tCariRiw.addActionListener(e -> loadRiwayat(tCariRiw.getText()));
        bRefR.addActionListener(e -> loadRiwayat(""));
        bUlang.addActionListener(e -> cetakUlang());
        tbRiw.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) cetakUlang();
                }
            }
        );
        loadCombo();
        loadRiwayat("");
    }

    String fmt(int v) {
        return String.format("%,d", v).replace(",", ".");
    }

    void loadCombo() {
        try (
            Connection k = Koneksi.getConnection();
            ResultSet r = k
                .createStatement()
                .executeQuery(
                    "SELECT k.id_kunjungan,p.nama FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm WHERE k.status='Menunggu bayar'"
                )
        ) {
            if (k == null) return;
            cKunj.removeAllItems();
            map.clear();
            while (r.next()) {
                String s = r.getInt(1) + " - " + r.getString(2);
                cKunj.addItem(s);
                map.put(s, r.getInt(1));
            }
            if (cKunj.getItemCount() == 0) cKunj.addItem("TIDAK ADA");
        } catch (Exception e) {}
    }

    void hitung() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (key == null || !map.containsKey(key)) return;
            int id = map.get(key);
            Connection k = Koneksi.getConnection();
            if (k == null) return;
            m.setRowCount(0);
            jasa = 0;
            produk = 0;
            PreparedStatement ps = k.prepareStatement(
                "SELECT l.nama_layanan,t.no_gigi,IFNULL(t.jumlah,1),t.tarif,t.tarif*IFNULL(t.jumlah,1),l.kategori FROM tindakan_kunjungan t JOIN layanan l ON t.kode_layanan=l.kode_layanan WHERE t.id_kunjungan=? ORDER BY l.kategori,l.nama_layanan"
            );
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                String nama = r.getString(1), gigi = r.getString(2), kat = r.getString(6);
                int qty = r.getInt(3), sub = r.getInt(5);
                m.addRow(new Object[] { nama, gigi, qty, sub });
                if ("Produk".equals(kat) || "Obat".equals(kat)) produk += sub;
                else jasa += sub;
            }
            subtotal = jasa + produk;
            diskonPersen = 0;
            diskonKet = "";
            try {
                PreparedStatement pd = k.prepareStatement(
                    "SELECT diskon_persen,diskon_ket FROM kunjungan WHERE id_kunjungan=?"
                );
                pd.setInt(1, id);
                ResultSet rd = pd.executeQuery();
                if (rd.next()) {
                    try {
                        diskonPersen = rd.getInt(1);
                    } catch (Exception e2) {
                        diskonPersen = 0;
                    }
                    diskonKet = rd.getString(2);
                    if (diskonKet == null) diskonKet = "";
                }
            } catch (Exception ex) {
                diskonPersen = 0;
                diskonKet = "";
            }
            diskonRp = subtotal * diskonPersen / 100;
            grand = subtotal - diskonRp;
            k.close();
            lTotal.setText("Rp " + fmt(grand));
            lRinci.setText(
                "Jasa: Rp " +
                fmt(jasa) +
                " + Produk: Rp " +
                fmt(produk) +
                " = Subtotal Rp " +
                fmt(subtotal)
            );
            String ketTxt = diskonKet.isEmpty() ? "" : " - " + diskonKet;
            lDiskonInfo.setText(
                "Diskon " + diskonPersen + "% (-Rp " + fmt(diskonRp) + ")" + ketTxt + " dari dokter"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    Map getParams(String noKwit) {
        Map par = new HashMap();
        par.put("no_kuitansi", noKwit);
        par.put("diskon_persen", Integer.valueOf(0));
        par.put("diskon_nominal", Long.valueOf(0));
        try (
            Connection k = Koneksi.getConnection();
            PreparedStatement p = k.prepareStatement(
                "SELECT pb.tgl_bayar,p.nama,p.alamat,d.nama,pb.metode FROM pembayaran pb JOIN kunjungan kj ON pb.id_kunjungan=kj.id_kunjungan JOIN pasien p ON kj.no_rm=p.no_rm JOIN dokter d ON kj.id_dokter=d.id_dokter WHERE pb.no_kuitansi=?"
            )
        ) {
            p.setString(1, noKwit);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                java.sql.Timestamp ts = r.getTimestamp(1);
                String tgl = "-";
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy HH:mm"
                    );
                    tgl = sdf.format(ts);
                } catch (Exception ex) {
                    tgl = ts.toString();
                }
                par.put("tgl_bayar", tgl);
                par.put("nama_pasien", r.getString(2) == null ? "-" : r.getString(2));
                String al = r.getString(3);
                par.put("alamat_pasien", al == null || al.isEmpty() ? "-" : al);
                par.put("nama_dokter", r.getString(4) == null ? "-" : r.getString(4));
                par.put("metode", r.getString(5) == null ? "-" : r.getString(5));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        try (
            Connection k2 = Koneksi.getConnection();
            PreparedStatement p2 = k2.prepareStatement(
                "SELECT diskon_persen,diskon_nominal FROM pembayaran WHERE no_kuitansi=?"
            )
        ) {
            p2.setString(1, noKwit);
            ResultSet r2 = p2.executeQuery();
            if (r2.next()) {
                int dp = 0;
                try {
                    dp = r2.getInt(1);
                } catch (Exception ex) {
                    dp = 0;
                }
                long dn = 0;
                try {
                    dn = r2.getLong(2);
                } catch (Exception ex) {
                    dn = 0;
                }
                par.put("diskon_persen", Integer.valueOf(dp));
                par.put("diskon_nominal", Long.valueOf(dn));
            }
        } catch (Exception e) {}
        return par;
    }

    void bayar() {
        try {
            String key = (String) cKunj.getSelectedItem();
            if (!map.containsKey(key)) {
                JOptionPane.showMessageDialog(this, "Tidak ada tagihan!");
                return;
            }
            int id = map.get(key);
            hitung();
            String kwt = AutoNumber.noKuitansi();
            Connection k = Koneksi.getConnection();
            if (k == null) return;
            try {
                PreparedStatement p = k.prepareStatement(
                    "INSERT INTO pembayaran(no_kuitansi,id_kunjungan,total_jasa,total_obat,subtotal,diskon_persen,diskon_nominal,total,metode,tgl_bayar,id_user) VALUES(?,?,?,?,?,?,?,?,NOW(),?)"
                );
                p.setString(1, kwt);
                p.setInt(2, id);
                p.setInt(3, jasa);
                p.setInt(4, produk);
                p.setInt(5, subtotal);
                p.setInt(6, diskonPersen);
                p.setInt(7, diskonRp);
                p.setInt(8, grand);
                p.setString(9, cMet.getSelectedItem().toString());
                p.setInt(10, Session.id_user);
                p.executeUpdate();
            } catch (Exception ex) {
                PreparedStatement p2 = k.prepareStatement(
                    "INSERT INTO pembayaran(no_kuitansi,id_kunjungan,total_jasa,total_obat,total,metode,tgl_bayar,id_user) VALUES(?,?,?,?,?,?,NOW(),?)"
                );
                p2.setString(1, kwt);
                p2.setInt(2, id);
                p2.setInt(3, jasa);
                p2.setInt(4, produk);
                p2.setInt(5, grand);
                p2.setString(6, cMet.getSelectedItem().toString());
                p2.setInt(7, Session.id_user);
                p2.executeUpdate();
            }
            k
                .createStatement()
                .executeUpdate("UPDATE kunjungan SET status='Selesai' WHERE id_kunjungan=" + id);
            k.close();
            JOptionPane.showMessageDialog(
                this,
                "Lunas " + kwt + " | Rp " + fmt(grand) + " (Diskon " + diskonPersen + "%)"
            );
            CetakLaporan.cetak("Kuitansi.jasper", getParams(kwt));
            loadCombo();
            m.setRowCount(0);
            lTotal.setText("Rp 0");
            loadRiwayat("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void loadRiwayat(String f) {
        try (Connection k = Koneksi.getConnection()) {
            if (k == null) return;
            PreparedStatement p = k.prepareStatement(
                "SELECT pb.no_kuitansi,pb.tgl_bayar,pa.nama,d.nama,pb.total,pb.metode FROM pembayaran pb JOIN kunjungan kj ON pb.id_kunjungan=kj.id_kunjungan JOIN pasien pa ON kj.no_rm=pa.no_rm JOIN dokter d ON kj.id_dokter=d.id_dokter WHERE pb.no_kuitansi LIKE ? OR pa.nama LIKE ? ORDER BY pb.tgl_bayar DESC LIMIT 200"
            );
            p.setString(1, "%" + f + "%");
            p.setString(2, "%" + f + "%");
            ResultSet r = p.executeQuery();
            mRiw.setRowCount(0);
            while (r.next()) {
                String tgl = r.getString(2);
                try {
                    java.text.SimpleDateFormat s1 = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                    );
                    java.text.SimpleDateFormat s2 = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy HH:mm"
                    );
                    tgl = s2.format(s1.parse(r.getString(2)));
                } catch (Exception ex) {}
                mRiw.addRow(
                    new Object[] {
                        r.getString(1),
                        tgl,
                        r.getString(3),
                        r.getString(4),
                        r.getInt(5),
                        r.getString(6)
                    }
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void cetakUlang() {
        int i = tbRiw.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris riwayat dulu!");
            return;
        }
        String no = mRiw.getValueAt(i, 0).toString();
        CetakLaporan.cetak("Kuitansi.jasper", getParams(no));
    }
}
