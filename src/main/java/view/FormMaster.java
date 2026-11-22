package view;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.sql.*;import koneksi.Koneksi;import utils.Tema;
import utils.Layar;import java.awt.*;
public class FormMaster extends JFrame{
JTabbedPane tab=new JTabbedPane();
public FormMaster(){
setTitle("Data Master");setSize(1120,660);setLocationRelativeTo(null);
Layar.fullscreen(this);setDefaultCloseOperation(DISPOSE_ON_CLOSE);getContentPane().setBackground(Tema.BG);setLayout(new BorderLayout());
add(Tema.header("Data Master","Pemilik/Admin | 121 Layanan Real | Diagnosis | Dokter | Jadwal | Pengguna"),BorderLayout.NORTH);
tab.setFont(new Font("Segoe UI",Font.BOLD,12));
tab.add("Layanan",pLay());tab.add("Diagnosis",pDiag());tab.add("Dokter",pDok());tab.add("Jadwal",pJad());tab.add("Pengguna",pUser());
add(tab,BorderLayout.CENTER);
}
JPanel base(DefaultTableModel m,JTable t){JPanel p=new JPanel(new BorderLayout(8,8));p.setBackground(Color.WHITE);p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));Tema.styleTable(t);p.add(new JScrollPane(t),BorderLayout.CENTER);return p;}
JPanel pLay(){
DefaultTableModel m=new DefaultTableModel(new String[]{"Kode","Nama","Kategori","Tarif","Keterangan"},0){public boolean isCellEditable(int r,int c){return false;}};
JTable t=new JTable(m);
JPanel p=new JPanel(new BorderLayout(8,8));p.setBackground(Color.WHITE);p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
JPanel fTop=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));fTop.setBackground(Color.WHITE);
String[] katList=new String[]{"SEMUA","Konsultasi","Konservasi","PSA Paket","PSA Non Paket","Esthetic","Oral Surgery","Prosthodontic","Periodontal","Orthodontic","Produk"};
JComboBox<String> fKat=new JComboBox<>(katList);
JTextField fCari=new JTextField(18);
JButton bCariF=new JButton("Cari");Tema.primary(bCariF);
JLabel lblCount=new JLabel("0 data");lblCount.setForeground(Tema.ABU);
fTop.add(new JLabel("Kategori"));fTop.add(fKat);fTop.add(new JLabel("Cari Kode/Nama"));fTop.add(fCari);fTop.add(bCariF);fTop.add(lblCount);
p.add(fTop,BorderLayout.NORTH);
Tema.styleTable(t);p.add(new JScrollPane(t),BorderLayout.CENTER);
JPanel fWrap=new JPanel(new GridLayout(2,1,4,4));fWrap.setBackground(Color.WHITE);
JPanel r1=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));r1.setBackground(Color.WHITE);
JTextField a=new JTextField(8),b=new JTextField(24),c=new JTextField(10);
JComboBox<String> cKat=new JComboBox<>(new String[]{"Konsultasi","Konservasi","PSA Paket","PSA Non Paket","Esthetic","Oral Surgery","Prosthodontic","Periodontal","Orthodontic","Produk"});
cKat.setEditable(true);
r1.add(new JLabel("Kode"));r1.add(a);r1.add(new JLabel("Nama"));r1.add(b);r1.add(new JLabel("Kategori"));r1.add(cKat);r1.add(new JLabel("Tarif"));r1.add(c);
JPanel r2=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));r2.setBackground(Color.WHITE);
JTextField d=new JTextField(30);
JButton s=new JButton("Simpan"),h=new JButton("Hapus"),n=new JButton("Baru");
Tema.primary(s);Tema.danger(h);Tema.secondary(n);
r2.add(new JLabel("Keterangan"));r2.add(d);r2.add(s);r2.add(h);r2.add(n);
JLabel hint=new JLabel("Tarif angka saja, cth 1500000.");hint.setForeground(Tema.ABU);hint.setFont(new Font("Segoe UI",Font.ITALIC,11));r2.add(hint);
fWrap.add(r1);fWrap.add(r2);p.add(fWrap,BorderLayout.SOUTH);
Runnable L=()->{
try(Connection k=Koneksi.getConnection()){
if(k==null)return;
String kat=(String)fKat.getSelectedItem();
String key="%"+fCari.getText().trim()+"%";
String sql="SELECT kode_layanan,nama_layanan,kategori,tarif,keterangan FROM layanan WHERE (?='SEMUA' OR kategori=?) AND (kode_layanan LIKE ? OR nama_layanan LIKE ?) ORDER BY kategori,kode_layanan";
try{
PreparedStatement ps=k.prepareStatement(sql);
ps.setString(1,kat);ps.setString(2,kat);ps.setString(3,key);ps.setString(4,key);
ResultSet r=ps.executeQuery();m.setRowCount(0);int tot=0;
while(r.next()){m.addRow(new Object[]{r.getString(1),r.getString(2),r.getString(3),r.getInt(4),r.getString(5)==null?"":r.getString(5)});tot++;}
lblCount.setText(tot+" data");
}catch(Exception ex){
if(ex.getMessage()!=null&&ex.getMessage().contains("kategori")){JOptionPane.showMessageDialog(this,"Kolom kategori belum ada. Import database/patch_layanan_kategori.sql dulu.");}
else throw ex;
}
}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}
};
L.run();
bCariF.addActionListener(e->L.run());fCari.addActionListener(e->L.run());fKat.addActionListener(e->L.run());
s.addActionListener(e->{
if(a.getText().trim().isEmpty()||b.getText().trim().isEmpty()||c.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Kode, Nama, Tarif wajib!");return;}
try(Connection k=Koneksi.getConnection();PreparedStatement x=k.prepareStatement("REPLACE INTO layanan(kode_layanan,nama_layanan,tarif,kategori,keterangan) VALUES(?,?,?,?,?)")){
x.setString(1,a.getText().trim());x.setString(2,b.getText().trim());x.setInt(3,Integer.parseInt(c.getText().trim()));
Object ko=cKat.getSelectedItem();x.setString(4,ko==null?"Umum":ko.toString());x.setString(5,d.getText().trim());
x.executeUpdate();L.run();
}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}
});
h.addActionListener(e->{int i=t.getSelectedRow();if(i<0){JOptionPane.showMessageDialog(this,"Pilih baris dulu!");return;}if(JOptionPane.showConfirmDialog(this,"Hapus "+m.getValueAt(i,0)+"?","Hapus",JOptionPane.YES_NO_OPTION)!=0)return;try(Connection k=Koneksi.getConnection()){k.createStatement().executeUpdate("DELETE FROM layanan WHERE kode_layanan='"+m.getValueAt(i,0)+"'");L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});
n.addActionListener(e->{a.setText("");b.setText("");c.setText("");d.setText("");t.clearSelection();});
t.getSelectionModel().addListSelectionListener(e->{int i=t.getSelectedRow();if(i>=0&&!e.getValueIsAdjusting()){a.setText(m.getValueAt(i,0).toString());b.setText(m.getValueAt(i,1).toString());cKat.setSelectedItem(m.getValueAt(i,2).toString());c.setText(m.getValueAt(i,3).toString());Object ke=m.getValueAt(i,4);d.setText(ke==null?"":ke.toString());}});
return p;
}
JPanel pDiag(){DefaultTableModel m=new DefaultTableModel(new String[]{"Kode ICD","Nama"},0){public boolean isCellEditable(int r,int c){return false;}};JTable t=new JTable(m);JPanel p=base(m,t);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));f.setBackground(Color.WHITE);JTextField a=new JTextField(8),b=new JTextField(26);f.add(new JLabel("Kode"));f.add(a);f.add(new JLabel("Nama"));f.add(b);JButton s=new JButton("Simpan"),h=new JButton("Hapus");Tema.primary(s);Tema.danger(h);f.add(s);f.add(h);p.add(f,BorderLayout.SOUTH);Runnable L=()->{try(Connection k=Koneksi.getConnection();ResultSet r=k.createStatement().executeQuery("SELECT * FROM diagnosis")){if(k==null)return;m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getString(1),r.getString(2)});}catch(Exception e){}};L.run();s.addActionListener(e->{try(Connection k=Koneksi.getConnection();PreparedStatement x=k.prepareStatement("REPLACE INTO diagnosis VALUES(?,?)")){x.setString(1,a.getText());x.setString(2,b.getText());x.executeUpdate();L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});h.addActionListener(e->{int i=t.getSelectedRow();if(i>=0)try(Connection k=Koneksi.getConnection()){k.createStatement().executeUpdate("DELETE FROM diagnosis WHERE kode_diagnosis='"+m.getValueAt(i,0)+"'");L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});return p;}
JPanel pDok(){DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Nama","SIP","HP"},0){public boolean isCellEditable(int r,int c){return false;}};JTable t=new JTable(m);JPanel p=base(m,t);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));f.setBackground(Color.WHITE);JTextField a=new JTextField(6),b=new JTextField(16),c=new JTextField(12),d=new JTextField(12);f.add(new JLabel("ID"));f.add(a);f.add(new JLabel("Nama"));f.add(b);f.add(new JLabel("SIP"));f.add(c);f.add(new JLabel("HP"));f.add(d);JButton s=new JButton("Simpan");Tema.primary(s);f.add(s);p.add(f,BorderLayout.SOUTH);Runnable L=()->{try(Connection k=Koneksi.getConnection();ResultSet r=k.createStatement().executeQuery("SELECT * FROM dokter")){if(k==null)return;m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getString(1),r.getString(2),r.getString(3),r.getString(5)});}catch(Exception e){}};L.run();s.addActionListener(e->{try(Connection k=Koneksi.getConnection();PreparedStatement x=k.prepareStatement("REPLACE INTO dokter(id_dokter,nama,sip,spesialis,no_hp) VALUES(?,?,?,'Umum',?)")){x.setString(1,a.getText());x.setString(2,b.getText());x.setString(3,c.getText());x.setString(4,d.getText());x.executeUpdate();L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});return p;}
JPanel pJad(){DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Dokter","Hari","Mulai","Selesai"},0){public boolean isCellEditable(int r,int c){return false;}};JTable t=new JTable(m);JPanel p=base(m,t);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5));f.setBackground(Color.WHITE);JTextField a=new JTextField(6),c=new JTextField("15:00",6),d=new JTextField("20:00",6);JComboBox<String> b=new JComboBox<>(new String[]{"Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu"});f.add(new JLabel("ID Dokter"));f.add(a);f.add(new JLabel("Hari"));f.add(b);f.add(new JLabel("Mulai"));f.add(c);f.add(new JLabel("Selesai"));f.add(d);JButton s=new JButton("Tambah");Tema.primary(s);f.add(s);p.add(f,BorderLayout.SOUTH);Runnable L=()->{try(Connection k=Koneksi.getConnection();ResultSet r=k.createStatement().executeQuery("SELECT * FROM jadwal_dokter")){if(k==null)return;m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)});}catch(Exception e){}};L.run();s.addActionListener(e->{try(Connection k=Koneksi.getConnection();PreparedStatement x=k.prepareStatement("INSERT INTO jadwal_dokter(id_dokter,hari,jam_mulai,jam_selesai) VALUES(?,?,?,?)")){x.setString(1,a.getText());x.setString(2,b.getSelectedItem().toString());x.setString(3,c.getText());x.setString(4,d.getText());x.executeUpdate();L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});return p;}
JPanel pUser(){DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Nama","Username","Peran","Dokter"},0){public boolean isCellEditable(int r,int c){return false;}};JTable t=new JTable(m);JPanel p=base(m,t);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT,6,5));f.setBackground(Color.WHITE);JTextField a=new JTextField(10),b=new JTextField(8),c=new JTextField(8),d=new JTextField(5);JComboBox<String> e2=new JComboBox<>(new String[]{"Resepsionis","Dokter","Kasir","Pemilik"});f.add(new JLabel("Nama"));f.add(a);f.add(new JLabel("User"));f.add(b);f.add(new JLabel("Pass"));f.add(c);f.add(new JLabel("Peran"));f.add(e2);f.add(new JLabel("ID Dok"));f.add(d);JButton s=new JButton("Simpan");Tema.primary(s);f.add(s);p.add(f,BorderLayout.SOUTH);Runnable L=()->{try(Connection k=Koneksi.getConnection();ResultSet r=k.createStatement().executeQuery("SELECT * FROM pengguna")){if(k==null)return;m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(5),r.getString(6)});}catch(Exception e){}};L.run();s.addActionListener(e->{try(Connection k=Koneksi.getConnection();PreparedStatement x=k.prepareStatement("INSERT INTO pengguna(nama_lengkap,username,password,peran,id_dokter) VALUES(?,?,?,?,?)")){x.setString(1,a.getText());x.setString(2,b.getText());x.setString(3,c.getText());x.setString(4,e2.getSelectedItem().toString());x.setString(5,d.getText().isEmpty()?null:d.getText());x.executeUpdate();L.run();}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});return p;}
}