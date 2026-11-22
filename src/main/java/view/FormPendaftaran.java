package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import koneksi.Koneksi;
import utils.*;
public class FormPendaftaran extends JFrame{
JTextField tCari=new JTextField(),tRM=new JTextField(),tNama=new JTextField(),tKeluhan=new JTextField(),tTensi=new JTextField();
JComboBox<String> cDok=new JComboBox<>(),cJenis=new JComboBox<>(new String[]{"Baru","Lama"});
DefaultTableModel m=new DefaultTableModel(new String[]{"No RM","Nama","NIK","HP"},0){public boolean isCellEditable(int r,int c){return false;}};
JTable tb=new JTable(m);
java.util.Map<String,String> mapDok=new java.util.HashMap<>();
public FormPendaftaran(){
setTitle("Pendaftaran");setSize(980,640);setLocationRelativeTo(null);
Layar.fullscreen(this);setDefaultCloseOperation(DISPOSE_ON_CLOSE);getContentPane().setBackground(Tema.BG);setLayout(new BorderLayout());
add(Tema.header("Pendaftaran & Antrean","Tahap 1 | Cari pasien lama | Isi Subjektif (S) + Tensi untuk dokter"),BorderLayout.NORTH);
JPanel main=new JPanel(new BorderLayout(12,12));main.setBackground(Tema.BG);main.setBorder(BorderFactory.createEmptyBorder(14,16,16,16));
JPanel cardS=Tema.kartu();cardS.setLayout(new BorderLayout(6,6));
JPanel s=new JPanel(new BorderLayout(8,8));s.setOpaque(false);s.add(new JLabel("Cari Nama / NIK / HP :"),BorderLayout.WEST);s.add(tCari,BorderLayout.CENTER);
JButton bCari=new JButton("Cari");Tema.primary(bCari);s.add(bCari,BorderLayout.EAST);cardS.add(s,BorderLayout.CENTER);
JLabel inf=new JLabel("Klik baris untuk pilih pasien lama -> otomatis jadi Lama.");inf.setForeground(Tema.ABU);inf.setFont(new Font("Segoe UI",Font.ITALIC,11));cardS.add(inf,BorderLayout.SOUTH);
main.add(cardS,BorderLayout.NORTH);
JPanel cardT=Tema.kartu();cardT.setLayout(new BorderLayout(8,8));cardT.add(new JLabel("Hasil Pencarian (klik untuk pilih):"),BorderLayout.NORTH);Tema.styleTable(tb);cardT.add(new JScrollPane(tb),BorderLayout.CENTER);main.add(cardT,BorderLayout.CENTER);
JPanel cardF=Tema.kartu();cardF.setLayout(new BorderLayout(10,10));
JPanel grid=new JPanel(new GridLayout(3,4,8,8));grid.setOpaque(false);
grid.add(new JLabel("No RM"));grid.add(tRM);grid.add(new JLabel("Nama"));grid.add(tNama);
grid.add(new JLabel("Dokter Hari Ini"));grid.add(cDok);grid.add(new JLabel("Jenis"));grid.add(cJenis);
grid.add(new JLabel("Keluhan / Subjektif (S)"));grid.add(tKeluhan);grid.add(new JLabel("Tensi (cth 120/80)"));grid.add(tTensi);
tRM.setEditable(false);tRM.setBackground(new Color(243,244,246));tRM.setFont(new Font("Segoe UI",Font.BOLD,12));
tKeluhan.setToolTipText("Otomatis jadi Subjektif (S) di SOAP dokter");
tTensi.setToolTipText("cth 120/80, : otomatis jadi /, boleh kosong");
cardF.add(grid,BorderLayout.CENTER);
JPanel pb=new JPanel(new BorderLayout(8,0));pb.setOpaque(false);
JLabel hint2=new JLabel("Subjektif + Tensi dari sini terkirim ke halaman dokter.");hint2.setForeground(Tema.ABU);hint2.setFont(new Font("Segoe UI",Font.ITALIC,11));pb.add(hint2,BorderLayout.WEST);
JButton bDaftar=new JButton("DAFTAR -> Dapat No Antrean");Tema.success(bDaftar);bDaftar.setPreferredSize(new Dimension(260,40));
JPanel pr=new JPanel(new FlowLayout(FlowLayout.RIGHT));pr.setOpaque(false);pr.add(bDaftar);pb.add(pr,BorderLayout.EAST);
cardF.add(pb,BorderLayout.SOUTH);
main.add(cardF,BorderLayout.SOUTH);add(main,BorderLayout.CENTER);
tb.getSelectionModel().addListSelectionListener(e->{int i=tb.getSelectedRow();if(i>=0){tRM.setText(m.getValueAt(i,0).toString());tNama.setText(m.getValueAt(i,1).toString());cJenis.setSelectedItem("Lama");}});
bCari.addActionListener(e->cari());tCari.addActionListener(e->cari());bDaftar.addActionListener(e->daftar());
loadDokter();cari();
}
void loadDokter(){try(Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("SELECT d.id_dokter,d.nama FROM dokter d JOIN jadwal_dokter j ON d.id_dokter=j.id_dokter WHERE j.hari=? GROUP BY d.id_dokter")){if(k==null)return;p.setString(1,TanggalIndo.hariIni());ResultSet r=p.executeQuery();cDok.removeAllItems();mapDok.clear();while(r.next()){String s=r.getString(1)+" - "+r.getString(2);cDok.addItem(s);mapDok.put(s,r.getString(1));}if(cDok.getItemCount()==0)cDok.addItem("TIDAK ADA DOKTER PRAKTIK");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void cari(){try(Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("SELECT * FROM pasien WHERE nama LIKE ? OR nik LIKE ? OR no_hp LIKE ? ORDER BY nama LIMIT 100")){if(k==null)return;for(int i=1;i<=3;i++)p.setString(i,"%"+tCari.getText().trim()+"%");ResultSet r=p.executeQuery();m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getString("no_rm"),r.getString("nama"),r.getString("nik"),r.getString("no_hp")});}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
boolean hasColumn(Connection k,String table,String col){try{DatabaseMetaData md=k.getMetaData();ResultSet rs=md.getColumns(null,null,table,col);boolean ada=rs.next();rs.close();return ada;}catch(Exception e){return false;}}
void daftar(){
try{
String key=(String)cDok.getSelectedItem();if(!mapDok.containsKey(key)){JOptionPane.showMessageDialog(this,"Tidak ada dokter praktik hari ini!");return;}
if(tRM.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Pilih pasien dulu di tabel pencarian!");return;}
String kel=tKeluhan.getText().trim();
String tensiRaw=tTensi.getText().trim();
String tensi=tensiRaw.replace(":","/").replace(".","/").replace(" ","");
if(!tensiRaw.isEmpty()&&!tensiRaw.equals(tensi)){tTensi.setText(tensi);}
if(kel.isEmpty()){if(JOptionPane.showConfirmDialog(this,"Subjektif masih kosong, tetap daftar?","Konfirmasi",JOptionPane.YES_NO_OPTION)!=0)return;}
if(!tensi.isEmpty()&&!tensi.matches("\\d{2,3}/\\d{2,3}")){if(JOptionPane.showConfirmDialog(this,"Format tensi biasanya 120/80. Tetap simpan ["+tensi+"]?","Tensi",JOptionPane.YES_NO_OPTION)!=0)return;}
String idDok=mapDok.get(key);String antre=AutoNumber.noAntrean(idDok);String jenis=cJenis.getSelectedItem().toString();
try(Connection k=Koneksi.getConnection()){
if(k==null)return;
boolean adaTensi=hasColumn(k,"kunjungan","tensi");
if(adaTensi){
String sql="INSERT INTO kunjungan (tanggal, no_rm, id_dokter, keluhan, tensi, no_antrean, jam_daftar, status, jenis) VALUES (CURDATE(), ?, ?, ?, ?, ?, CURTIME(), 'Menunggu', ?)";
PreparedStatement p=k.prepareStatement(sql);
p.setString(1,tRM.getText().trim());
p.setString(2,idDok);
p.setString(3,kel);
p.setString(4,tensi);
p.setString(5,antre);
p.setString(6,jenis);
p.executeUpdate();
}else{
String sql2="INSERT INTO kunjungan (tanggal, no_rm, id_dokter, keluhan, no_antrean, jam_daftar, status, jenis) VALUES (CURDATE(), ?, ?, ?, ?, CURTIME(), 'Menunggu', ?)";
PreparedStatement p2=k.prepareStatement(sql2);
p2.setString(1,tRM.getText().trim());
p2.setString(2,idDok);
p2.setString(3,kel);
p2.setString(4,antre);
p2.setString(5,jenis);
p2.executeUpdate();
JOptionPane.showMessageDialog(this,"Tersimpan tanpa tensi. Import patch_subjektif_tensi.sql dulu.");
}
}
JOptionPane.showMessageDialog(this,"Berhasil! No Antrean: "+antre);
tKeluhan.setText("");tTensi.setText("");
}catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(this,e.getMessage());}
}
}