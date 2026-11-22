package view;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.sql.*;import koneksi.Koneksi;import utils.Tema;
import utils.Layar;import java.awt.*;
public class FormAntrean extends JFrame{
JComboBox<String> cDok=new JComboBox<>();DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Antrean","Nama","Jam Daftar","Jam Panggil","Status","Tunggu"},0){public boolean isCellEditable(int r,int c){return false;}};JTable tb=new JTable(m);JLabel lblInfo=new JLabel("Total: 0");java.util.Map<String,String> map=new java.util.HashMap<>();
public FormAntrean(){
setTitle("Antrean");setSize(1000,560);setLocationRelativeTo(null);
Layar.fullscreen(this);setDefaultCloseOperation(DISPOSE_ON_CLOSE);getContentPane().setBackground(Tema.BG);setLayout(new BorderLayout());
add(Tema.header("Antrean Per Dokter","Tahap 2  Panggil  Waktu tunggu otomatis"),BorderLayout.NORTH);
JPanel main=new JPanel(new BorderLayout(12,12));main.setBackground(Tema.BG);main.setBorder(BorderFactory.createEmptyBorder(14,16,16,16));
JPanel top=Tema.kartu();top.setLayout(new BorderLayout(8,8));
JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));f.setOpaque(false);
f.add(new JLabel("| Dokter:"));cDok.setPreferredSize(new Dimension(280,30));f.add(cDok);
JButton bR=new JButton("Refresh");Tema.secondary(bR);f.add(bR);
JButton bP=new JButton("PANGGIL");Tema.success(bP);bP.setPreferredSize(new Dimension(150,36));f.add(bP);
top.add(f,BorderLayout.CENTER);lblInfo.setForeground(Tema.ABU);top.add(lblInfo,BorderLayout.SOUTH);main.add(top,BorderLayout.NORTH);
JPanel card=Tema.kartu();card.setLayout(new BorderLayout(8,8));Tema.styleTable(tb);card.add(new JScrollPane(tb),BorderLayout.CENTER);main.add(card,BorderLayout.CENTER);
add(main,BorderLayout.CENTER);
bR.addActionListener(e->load());bP.addActionListener(e->panggil());cDok.addActionListener(e->load());
try(Connection k=Koneksi.getConnection();ResultSet r=k.createStatement().executeQuery("SELECT * FROM dokter")){while(r.next()){String s=r.getString("id_dokter")+"- "+r.getString("nama");cDok.addItem(s);map.put(s,r.getString("id_dokter"));}}catch(Exception e){}
load();
}
void load(){try{String key=(String)cDok.getSelectedItem();if(key==null||!map.containsKey(key))return;Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("SELECT k.id_kunjungan,k.no_antrean,p.nama,k.jam_daftar,k.jam_panggil,k.status,TIMESTAMPDIFF(MINUTE,k.jam_daftar,IFNULL(k.jam_panggil,NOW())) FROM kunjungan k JOIN pasien p ON k.no_rm=p.no_rm WHERE k.tanggal=CURDATE() AND k.id_dokter=? ORDER BY k.jam_daftar");p.setString(1,map.get(key));ResultSet r=p.executeQuery();m.setRowCount(0);int tot=0;while(r.next()){m.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getString(6),r.getString(7)+"mnt"});tot++;}k.close();lblInfo.setText("Total antrean hari ini: "+tot+"pasien");}catch(Exception e){}}
void panggil(){int i=tb.getSelectedRow();if(i<0){JOptionPane.showMessageDialog(this,"Pilih pasien!");return;}try(Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("UPDATE kunjungan SET status='Dipanggil',jam_panggil=CURTIME() WHERE id_kunjungan=?")){p.setInt(1,Integer.parseInt(m.getValueAt(i,0).toString()));p.executeUpdate();load();JOptionPane.showMessageDialog(this,"Pasien "+m.getValueAt(i,1)+"dipanggil!");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
}