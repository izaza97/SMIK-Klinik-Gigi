package view;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;import java.sql.*;import koneksi.Koneksi;import utils.*;
public class FormPasien extends JFrame{
JTextField tRM=new JTextField(),tNIK=new JTextField(),tNama=new JTextField(),tLahir=new JTextField("1997-01-01"),tHP=new JTextField(),tAlergi=new JTextField(),tAlamat=new JTextField(),tCari=new JTextField();
JComboBox<String> cJK=new JComboBox<>(new String[]{"L - Laki-laki","P - Perempuan"});
DefaultTableModel m=new DefaultTableModel(new String[]{"No RM","NIK (mask)","Nama","Lahir","L/P","HP (mask)","Alergi","Alamat"},0){public boolean isCellEditable(int r,int c){return false;}};JTable tb=new JTable(m);
public FormPasien(){
setTitle("Data Pasien");setSize(1100,640);setLocationRelativeTo(null);
Layar.fullscreen(this);setDefaultCloseOperation(DISPOSE_ON_CLOSE);getContentPane().setBackground(Tema.BG);setLayout(new BorderLayout());
add(Tema.header("Data Pasien","No RM otomatis | Cegah NIK ganda | Alamat untuk kwitansi"),BorderLayout.NORTH);
JPanel main=new JPanel(new BorderLayout(12,12));main.setBackground(Tema.BG);main.setBorder(BorderFactory.createEmptyBorder(14,16,16,16));
JPanel cardForm=Tema.kartu();cardForm.setLayout(new BorderLayout(10,10));cardForm.add(new JLabel("Form Pasien"),BorderLayout.NORTH);
JPanel grid=new JPanel(new GridLayout(4,4,8,8));grid.setOpaque(false);
grid.add(new JLabel("No RM"));grid.add(tRM);grid.add(new JLabel("NIK"));grid.add(tNIK);
grid.add(new JLabel("Nama"));grid.add(tNama);grid.add(new JLabel("Tgl Lahir yyyy-mm-dd"));grid.add(tLahir);
grid.add(new JLabel("JK"));grid.add(cJK);grid.add(new JLabel("No HP"));grid.add(tHP);
grid.add(new JLabel("Alergi"));grid.add(tAlergi);grid.add(new JLabel("Alamat"));grid.add(tAlamat);
tRM.setEditable(false);tRM.setBackground(new Color(243,244,246));tRM.setFont(new Font("Segoe UI",Font.BOLD,12));
cardForm.add(grid,BorderLayout.CENTER);
JPanel btnBar=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));btnBar.setOpaque(false);
JButton bBaru=new JButton("Baru"),bSimpan=new JButton("Simpan"),bHapus=new JButton("Hapus");
Tema.secondary(bBaru);Tema.primary(bSimpan);Tema.danger(bHapus);btnBar.add(bBaru);btnBar.add(bSimpan);btnBar.add(bHapus);
cardForm.add(btnBar,BorderLayout.SOUTH);main.add(cardForm,BorderLayout.NORTH);
JPanel cardTab=Tema.kartu();cardTab.setLayout(new BorderLayout(8,8));
JPanel search=new JPanel(new BorderLayout(8,8));search.setOpaque(false);
search.add(new JLabel("Cari Nama / NIK / HP :"),BorderLayout.WEST);search.add(tCari,BorderLayout.CENTER);
JButton bCari=new JButton("Cari");Tema.primary(bCari);search.add(bCari,BorderLayout.EAST);
cardTab.add(search,BorderLayout.NORTH);Tema.styleTable(tb);cardTab.add(new JScrollPane(tb),BorderLayout.CENTER);main.add(cardTab,BorderLayout.CENTER);
add(main,BorderLayout.CENTER);
bBaru.addActionListener(e->baru());bSimpan.addActionListener(e->simpan());bHapus.addActionListener(e->hapus());bCari.addActionListener(e->load(tCari.getText()));tCari.addActionListener(e->load(tCari.getText()));
baru();load("");
}
void baru(){try{tRM.setText(AutoNumber.noRM());tNIK.setText("");tNama.setText("");tLahir.setText("1997-01-01");tHP.setText("");tAlergi.setText("");tAlamat.setText("");}catch(Exception e){}}
void load(String c){try(Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("SELECT * FROM pasien WHERE nama LIKE ? OR nik LIKE ? OR no_hp LIKE ? ORDER BY no_rm DESC")){if(k==null)return;for(int i=1;i<=3;i++)p.setString(i,"%"+c+"%");ResultSet r=p.executeQuery();m.setRowCount(0);while(r.next())m.addRow(new Object[]{r.getString("no_rm"),Masking.nik(r.getString("nik")),r.getString("nama"),r.getString("tgl_lahir"),r.getString("jk"),Masking.hp(r.getString("no_hp")),r.getString("alergi"),r.getString("alamat")==null?"":r.getString("alamat")});}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void simpan(){if(tNIK.getText().trim().isEmpty()||tNama.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"NIK dan Nama wajib!");return;}try(Connection k=Koneksi.getConnection()){PreparedStatement cek=k.prepareStatement("SELECT COUNT(*) FROM pasien WHERE nik=?");cek.setString(1,tNIK.getText().trim());ResultSet cr=cek.executeQuery();cr.next();if(cr.getInt(1)>0){JOptionPane.showMessageDialog(this,"NIK sudah ada!");return;}String jk=((String)cJK.getSelectedItem()).substring(0,1);PreparedStatement p=k.prepareStatement("INSERT INTO pasien(no_rm,nik,nama,tgl_lahir,jk,no_hp,alamat,alergi,tgl_daftar) VALUES(?,?,?,?,?,?,?,?,CURDATE())");p.setString(1,tRM.getText());p.setString(2,tNIK.getText().trim());p.setString(3,tNama.getText().trim());p.setString(4,tLahir.getText().trim());p.setString(5,jk);p.setString(6,tHP.getText().trim());p.setString(7,tAlamat.getText().trim());p.setString(8,tAlergi.getText().trim());p.executeUpdate();JOptionPane.showMessageDialog(this,"Tersimpan "+tRM.getText());load("");baru();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void hapus(){if(tRM.getText().isEmpty())return;if(JOptionPane.showConfirmDialog(this,"Hapus "+tRM.getText()+"?","Hapus",JOptionPane.YES_NO_OPTION)!=0)return;try(Connection k=Koneksi.getConnection();PreparedStatement p=k.prepareStatement("DELETE FROM pasien WHERE no_rm=?")){p.setString(1,tRM.getText());p.executeUpdate();load("");baru();}catch(Exception e){JOptionPane.showMessageDialog(this,"Gagal: "+e.getMessage());}}
}