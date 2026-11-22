package view;

import utils.Layar;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import utils.CetakLaporan;
import model.Session;

public class FormLaporan extends JFrame {

    JTextField tDari = new JTextField(10), tSampai = new JTextField(10);
    JComboBox<String> cDok = new JComboBox<>(new String[] { "SEMUA" });
    JRadioButton rKun = new JRadioButton("Kunjungan", true), rPen = new JRadioButton(
        "Pendapatan"
    ), rTin = new JRadioButton("Tindakan"), rDia = new JRadioButton("Diagnosis"), rKin =
        new JRadioButton("Kinerja Dokter");

    public FormLaporan() {
        setTitle("Laporan");
        setSize(820, 560);
        setLocationRelativeTo(null);
        Layar.fullscreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(utils.Tema.BG);
        setLayout(new BorderLayout());
        add(
            utils.Tema.header("Laporan", "Tahap 7  Filter periode/dokter  Jasper"),
            BorderLayout.NORTH
        );
        JPanel main = new JPanel(new GridLayout(1, 2, 12, 12));
        main.setBackground(utils.Tema.BG);
        main.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));
        JPanel f = utils.Tema.kartu();
        f.setLayout(new GridLayout(0, 1, 8, 8));
        f.add(new JLabel("Filter Laporan:"));
        f.add(new JLabel("Dari (yyyy-mm-dd):"));
        f.add(tDari);
        f.add(new JLabel("Sampai:"));
        f.add(tSampai);
        f.add(new JLabel("Dokter:"));
        f.add(cDok);
        JLabel inf = new JLabel(
            "<html><i>Bulan ini: awal bulan s/d hari ini.<br/>Kasir: hanya Pendapatan.<br/>Dokter: tanpa Pendapatan.</i></html>"
        );
        inf.setForeground(utils.Tema.ABU);
        f.add(inf);
        main.add(f);
        JPanel p = utils.Tema.kartu();
        p.setLayout(new BorderLayout(8, 8));
        p.add(new JLabel("Pilih Laporan:"), BorderLayout.NORTH);
        JPanel rp = new JPanel(new GridLayout(0, 1, 6, 6));
        rp.setOpaque(false);
        ButtonGroup g = new ButtonGroup();
        for (JRadioButton r : new JRadioButton[] { rKun, rPen, rTin, rDia, rKin }) {
            r.setBackground(java.awt.Color.WHITE);
            r.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g.add(r);
            rp.add(r);
        }
        p.add(rp, BorderLayout.CENTER);
        JButton b = new JButton("TAMPIL / CETAK");
        utils.Tema.primary(b);
        b.setPreferredSize(new Dimension(200, 44));
        p.add(b, BorderLayout.SOUTH);
        main.add(p);
        add(main, BorderLayout.CENTER);
        tDari.setText(java.time.LocalDate.now().withDayOfMonth(1).toString());
        tSampai.setText(java.time.LocalDate.now().toString());
        String per = Session.peran == null ? "" : Session.peran;
        if (per.equals("Kasir")) {
            rKun.setEnabled(false);
            rTin.setEnabled(false);
            rDia.setEnabled(false);
            rKin.setEnabled(false);
        }
        if (per.equals("Dokter")) {
            rPen.setEnabled(false);
        }
        if (per.equals("Resepsionis")) {
            rPen.setEnabled(false);
            rKin.setEnabled(false);
        }
        b.addActionListener(e -> cetak());
        try (
            java.sql.Connection k = koneksi.Koneksi.getConnection();
            java.sql.ResultSet r = k.createStatement().executeQuery("SELECT id_dokter FROM dokter")
        ) {
            cDok.removeAllItems();
            cDok.addItem("SEMUA");
            while (r.next()) cDok.addItem(r.getString(1));
        } catch (Exception e) {}
    }

    void cetak() {
        try {
            Map m = new HashMap();
            m.put("dari", java.sql.Date.valueOf(tDari.getText()));
            m.put("sampai", java.sql.Date.valueOf(tSampai.getText()));
            m.put("id_dokter", cDok.getSelectedItem().toString());
            if (rKun.isSelected()) CetakLaporan.cetak("LapKunjungan.jasper", m);
            else if (rPen.isSelected()) CetakLaporan.cetak("LapPendapatan.jasper", m);
            else if (rTin.isSelected()) CetakLaporan.cetak("LapTindakan.jasper", m);
            else if (rDia.isSelected()) CetakLaporan.cetak("LapDiagnosis.jasper", m);
            else CetakLaporan.cetak("LapKinerjaDokter.jasper", m);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
