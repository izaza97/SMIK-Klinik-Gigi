package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import koneksi.Koneksi;
import model.Session;
import utils.Tema;
import utils.KlinikLogo;
import utils.Layar;

public class FormLogin extends JFrame {

    JTextField txtUser = new JTextField(18);
    JPasswordField txtPass = new JPasswordField(18);

    public FormLogin() {
        setTitle("SIM Klinik Gigi - Login");
        setSize(900, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Layar.fullscreen(this);
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(Tema.PRIMER);
        left.setPreferredSize(new Dimension(400, 540));
        GridBagConstraints gl = new GridBagConstraints();
        gl.insets = new Insets(8, 26, 8, 26);
        gl.gridx = 0;
        gl.anchor = GridBagConstraints.WEST;
        JLabel logo = new JLabel(KlinikLogo.iconCircle(110));
        JLabel t1 = new JLabel("SIM Klinik Gigi");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        t1.setForeground(Color.WHITE);
        JLabel t2 = new JLabel("Senyum Sehat, Pelayanan Cepat");
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t2.setForeground(new Color(214, 230, 245));
        JLabel f1 = new JLabel("- Pendaftaran dan Antrean Cepat");
        JLabel f2 = new JLabel("- Rekam Medis SOAP + Odontogram");
        JLabel f3 = new JLabel("- Tindakan, Produk dan Kuitansi Otomatis");
        JLabel f4 = new JLabel("- Laporan Jasper Siap Sidang");
        f1.setForeground(Color.WHITE);
        f2.setForeground(Color.WHITE);
        f3.setForeground(Color.WHITE);
        f4.setForeground(Color.WHITE);
        gl.gridy = 0;
        left.add(logo, gl);
        gl.gridy = 1;
        left.add(t1, gl);
        gl.gridy = 2;
        left.add(t2, gl);
        gl.gridy = 3;
        left.add(new JLabel(" "), gl);
        gl.gridy = 4;
        left.add(f1, gl);
        gl.gridy = 5;
        left.add(f2, gl);
        gl.gridy = 6;
        left.add(f3, gl);
        gl.gridy = 7;
        left.add(f4, gl);
        add(left, BorderLayout.WEST);
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridwidth = 2;
        JLabel w = new JLabel("Selamat Datang", SwingConstants.CENTER);
        w.setFont(new Font("Segoe UI", Font.BOLD, 22));
        w.setForeground(Tema.TEKS);
        JLabel s = new JLabel("Silakan login sesuai peran Anda", SwingConstants.CENTER);
        s.setForeground(new Color(107, 114, 128));
        g.gridy = 0;
        right.add(w, g);
        g.gridy = 1;
        right.add(s, g);
        g.gridwidth = 1;
        g.gridy = 2;
        g.gridx = 0;
        right.add(new JLabel("Username"), g);
        g.gridx = 1;
        right.add(txtUser, g);
        g.gridx = 0;
        g.gridy = 3;
        right.add(new JLabel("Password"), g);
        g.gridx = 1;
        right.add(txtPass, g);
        JButton b = new JButton("LOGIN");
        Tema.primary(b);
        b.setPreferredSize(new Dimension(200, 42));
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        right.add(b, g);
        JLabel info = new JLabel(
            "resepsionis/dokter/kasir:123 | admin:admin123",
            SwingConstants.CENTER
        );
        info.setForeground(new Color(107, 114, 128));
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        g.gridy = 5;
        right.add(info, g);
        add(right, BorderLayout.CENTER);
        b.addActionListener(e -> login());
        txtPass.addActionListener(e -> login());
        getRootPane().setDefaultButton(b);
    }

    void login() {
        try (
            Connection c = Koneksi.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM pengguna WHERE username=? AND password=?"
            )
        ) {
            if (c == null) return;
            ps.setString(1, txtUser.getText().trim());
            ps.setString(2, new String(txtPass.getPassword()));
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                Session.id_user = r.getInt("id_user");
                Session.nama = r.getString("nama_lengkap");
                Session.username = r.getString("username");
                Session.peran = r.getString("peran");
                Session.id_dokter = r.getString("id_dokter");
                new FormUtama().setVisible(true);
                dispose();
            } else JOptionPane.showMessageDialog(
                this,
                "Username/password salah!",
                "Gagal",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
