package utils;
import java.sql.*;import koneksi.Koneksi;import java.time.LocalDate;
public class AutoNumber{
    public static String noRM() throws Exception{
        try(Connection c=Koneksi.getConnection()){
            ResultSet r=c.createStatement().executeQuery("SELECT MAX(CAST(RIGHT(no_rm,6) AS UNSIGNED)) FROM pasien");
            int n=0; if(r.next()&&r.getString(1)!=null) n=r.getInt(1);
            return String.format("RM-%06d", n+1);
        }
    }
    public static String noAntrean(String idDokter) throws Exception{
        try(Connection c=Koneksi.getConnection(); PreparedStatement p=c.prepareStatement("SELECT COUNT(*)+1 FROM kunjungan WHERE tanggal=CURDATE() AND id_dokter=?")){
            p.setString(1,idDokter); ResultSet r=p.executeQuery(); r.next();
            return "A-"+String.format("%02d", r.getInt(1));
        }
    }
    public static String noKuitansi() throws Exception{
        try(Connection c=Koneksi.getConnection()){
            String tgl=LocalDate.now().toString().replace("-","");
            ResultSet r=c.createStatement().executeQuery("SELECT COUNT(*)+1 FROM pembayaran WHERE DATE(tgl_bayar)=CURDATE()");
            r.next(); return "KWT-"+tgl+"-"+String.format("%03d", r.getInt(1));
        }
    }
}
