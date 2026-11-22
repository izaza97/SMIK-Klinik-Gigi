package koneksi;

import java.sql.*;
import javax.swing.JOptionPane;

public class Koneksi {

    // XAMPP default: host localhost:3306 user root password kosong
    private static final String URL =
        "jdbc:mysql://localhost:3306/db_klinik_gigi?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Koneksi XAMPP gagal!\nPastikan XAMPP MySQL Running & DB db_klinik_gigi sudah diimport.\n" +
                e.getMessage()
            );
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
