package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import koneksi.Koneksi;
import utils.Tema;
import utils.Layar;
import utils.Masking;
public class FormLihatRekamMedis extends JFrame{
JTextField tCari=new JTextField(20);
DefaultTableModel mPasien=new DefaultTableModel(new String[]{"No RM","Nama","Lahir","L/P","NIK","Alergi"},0){public boolean isCellEditable(int r,int c){return false;}};
JTable tbPasien=new JTable(mPasien);
DefaultTableModel mRiw=new DefaultTableModel(new String[]{"Tanggal","Dokter","Tensi","S","Diagnosa"},0){public boolean isCellEditable(int r,int c){return false;}};
JTable tbRiw=new JTable(mRiw);
JTextArea txtDetail=new JTextArea(14,30);
JLabel lblPasienInfo=new JLabel("-");
JLabel lblCountPasien=new JLabel("0 pasien");
JLabel lblCountRiw=new JLabel("0 riwayat");
PanelOdontogram odo=new PanelOdontogram();
java.util.List<Integer> listIdRekam=new java.util.ArrayList<Integer>();
String selectedNoRM=null;
public FormLihatRekamMedis(){
setTitle("Lihat Rekam Medis Pasien");setSize(1350,800);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);getContentPane().setBackground(Tema.BG);setLayout(new BorderLayout());
Layar.fullscreen(this);
add(Tema.header("Lihat Rekam Medis Pasien","Read-only | Riwayat lengkap SOAP + Tensi + Odontogram | Khusus Dokter"),BorderLayout.NORTH);
JPanel main=new JPanel(new BorderLayout(10,10));main.setBackground(Tema.BG);main.setBorder(BorderFactory.createEmptyBorder(10,12,12,12));
JPanel top=Tema.kartu();top.setLayout(new BorderLayout(8,8));
JPanel search=new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));search.setOpaque(false);
search.add(new JLabel("Cari No RM / Nama / NIK:"));tCari.setPreferredSize(new Dimension(300,30));search.add(tCari);
JButton bCari=new JButton("Cari");Tema.primary(bCari);search.add(bCari);
JButton bRefPas=new JButton("Refresh");Tema.secondary(bRefPas);search.add(bRefPas);
lblCountPasien.setForeground(Tema.ABU);lblCountPasien.setFont(new Font("Segoe UI",Font.ITALIC,11));search.add(lblCountPasien);
top.add(search,BorderLayout.NORTH);
Tema.styleTable(tbPasien);tbPasien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
JScrollPane spPas=new JScrollPane(tbPasien);spPas.setPreferredSize(new Dimension(800,150));top.add(spPas,BorderLayout.CENTER);
JLabel hintTop=new JLabel("Pilih pasien di atas untuk tampilkan riwayat di bawah. Klik riwayat untuk detail + odontogram.");hintTop.setForeground(Tema.ABU);hintTop.setFont(new Font("Segoe UI",Font.ITALIC,11));top.add(hintTop,BorderLayout.SOUTH);
main.add(top,BorderLayout.NORTH);
JPanel left=Tema.kartu();left.setLayout(new BorderLayout(6,6));
JPanel leftNorth=new JPanel(new BorderLayout());leftNorth.setOpaque(false);
JLabel lRiw=new JLabel("RIWAYAT KUNJUNGAN");lRiw.setFont(new Font("Segoe UI",Font.BOLD,13));lRiw.setForeground(Tema.PRIMER_GELAP);leftNorth.add(lRiw,BorderLayout.NORTH);
lblPasienInfo.setFont(new Font("Segoe UI",Font.PLAIN,11));leftNorth.add(lblPasienInfo,BorderLayout.CENTER);
lblCountRiw.setForeground(Tema.ABU);lblCountRiw.setFont(new Font("Segoe UI",Font.ITALIC,11));leftNorth.add(lblCountRiw,BorderLayout.SOUTH);
left.add(leftNorth,BorderLayout.NORTH);
Tema.styleTable(tbRiw);tbRiw.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
tbRiw.getColumnModel().getColumn(0).setPreferredWidth(75);
tbRiw.getColumnModel().getColumn(1).setPreferredWidth(110);
tbRiw.getColumnModel().getColumn(2).setPreferredWidth(60);
tbRiw.getColumnModel().getColumn(3).setPreferredWidth(150);
tbRiw.getColumnModel().getColumn(4).setPreferredWidth(150);
left.add(new JScrollPane(tbRiw),BorderLayout.CENTER);
JPanel right=new JPanel(new BorderLayout(8,8));right.setOpaque(false);right.setBackground(Tema.BG);
JPanel detailCard=Tema.kartu();detailCard.setLayout(new BorderLayout(6,6));
detailCard.add(new JLabel("DETAIL REKAM MEDIS (read-only):"),BorderLayout.NORTH);
txtDetail.setEditable(false);txtDetail.setLineWrap(true);txtDetail.setWrapStyleWord(true);txtDetail.setFont(new Font("Segoe UI",Font.PLAIN,12));txtDetail.setBackground(new Color(249,250,251));
JScrollPane spDetail=new JScrollPane(txtDetail);spDetail.setPreferredSize(new Dimension(500,280));detailCard.add(spDetail,BorderLayout.CENTER);
JPanel btnDetail=new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));btnDetail.setOpaque(false);
JButton bCetak=new JButton("Cetak Detail");Tema.secondary(bCetak);
JButton bClear=new JButton("Bersihkan");Tema.secondary(bClear);
btnDetail.add(bCetak);btnDetail.add(bClear);
JLabel hintDetail=new JLabel("Odontogram di bawah read-only.");hintDetail.setForeground(Tema.ABU);hintDetail.setFont(new Font("Segoe UI",Font.ITALIC,11));btnDetail.add(hintDetail);
detailCard.add(btnDetail,BorderLayout.SOUTH);
right.add(detailCard,BorderLayout.NORTH);
right.add(odo,BorderLayout.CENTER);
JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,left,right);
split.setDividerLocation(480);split.setOneTouchExpandable(true);split.setResizeWeight(0.35);
left.setMinimumSize(new Dimension(380,0));right.setMinimumSize(new Dimension(600,0));
main.add(split,BorderLayout.CENTER);
add(main,BorderLayout.CENTER);
bCari.addActionListener(e->loadPasien(tCari.getText().trim()));tCari.addActionListener(e->loadPasien(tCari.getText().trim()));bRefPas.addActionListener(e->{tCari.setText("");loadPasien("");});
tbPasien.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())onPasienPilih();});
tbRiw.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())tampilDetail();});
bCetak.addActionListener(e->cetakDetail());bClear.addActionListener(e->{txtDetail.setText("");odo.reset();});
makeOdoReadOnly();
loadPasien("");
}
void onPasienPilih(){int i=tbPasien.getSelectedRow();if(i<0)return;String noRm=mPasien.getValueAt(i,0).toString();selectedNoRM=noRm;String nama=mPasien.getValueAt(i,1).toString();lblPasienInfo.setText("<html><b>"+noRm+" - "+nama+"</b></html>");loadRiwayat(noRm);}
void loadPasien(String f){try(Connection k=Koneksi.getConnection()){if(k==null)return;PreparedStatement p=k.prepareStatement("SELECT no_rm,nama,tgl_lahir,jk,nik,alergi FROM pasien WHERE no_rm LIKE ? OR nama LIKE ? OR nik LIKE ? ORDER BY nama LIMIT 200");for(int i=1;i<=3;i++)p.setString(i,"%"+f+"%");ResultSet r=p.executeQuery();mPasien.setRowCount(0);int tot=0;while(r.next()){String nik=r.getString(5);String nikMask="";try{nikMask=Masking.nik(nik);}catch(Exception ex){nikMask="****";}mPasien.addRow(new Object[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4),nikMask,r.getString(6)==null?"":r.getString(6)});tot++;}lblCountPasien.setText(tot+" pasien");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void loadRiwayat(String noRm){try(Connection k=Koneksi.getConnection()){if(k==null)return;mRiw.setRowCount(0);listIdRekam.clear();txtDetail.setText("");odo.reset();int tot=0;try{PreparedStatement p=k.prepareStatement("SELECT r.id_rekam,k.tanggal,dd.nama,k.tensi,r.subjektif,r.kode_diagnosis,dg.nama_diagnosis FROM rekam_medis r JOIN kunjungan k ON r.id_kunjungan=k.id_kunjungan LEFT JOIN dokter dd ON k.id_dokter=dd.id_dokter LEFT JOIN diagnosis dg ON r.kode_diagnosis=dg.kode_diagnosis WHERE k.no_rm=? ORDER BY k.tanggal DESC,k.id_kunjungan DESC");p.setString(1,noRm);ResultSet r=p.executeQuery();while(r.next()){listIdRekam.add(Integer.valueOf(r.getInt(1)));String s=r.getString(5)==null?"":r.getString(5).replaceAll("\\n"," ");if(s.length()>40)s=s.substring(0,40)+"...";String diag=(r.getString(6)==null?"":r.getString(6)+" - ")+(r.getString(7)==null?"":r.getString(7));if(diag.length()>40)diag=diag.substring(0,40)+"...";mRiw.addRow(new Object[]{r.getString(2),r.getString(3)==null?"-":r.getString(3),r.getString(4)==null?"":r.getString(4),s,diag});tot++;}}catch(Exception ex){PreparedStatement p2=k.prepareStatement("SELECT r.id_rekam,k.tanggal,dd.nama,r.subjektif,r.kode_diagnosis,dg.nama_diagnosis FROM rekam_medis r JOIN kunjungan k ON r.id_kunjungan=k.id_kunjungan LEFT JOIN dokter dd ON k.id_dokter=dd.id_dokter LEFT JOIN diagnosis dg ON r.kode_diagnosis=dg.kode_diagnosis WHERE k.no_rm=? ORDER BY k.tanggal DESC,k.id_kunjungan DESC");p2.setString(1,noRm);ResultSet r2=p2.executeQuery();while(r2.next()){listIdRekam.add(Integer.valueOf(r2.getInt(1)));String s=r2.getString(4)==null?"":r2.getString(4).replaceAll("\\n"," ");if(s.length()>40)s=s.substring(0,40)+"...";String diag=(r2.getString(5)==null?"":r2.getString(5)+" - ")+(r2.getString(6)==null?"":r2.getString(6));mRiw.addRow(new Object[]{r2.getString(2),r2.getString(3)==null?"-":r2.getString(3),"",s,diag});tot++;}}lblCountRiw.setText(tot+" riwayat untuk "+noRm);if(tot>0)tbRiw.setRowSelectionInterval(0,0);else txtDetail.setText("Belum ada rekam medis untuk pasien ini.");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void tampilDetail(){int row=tbRiw.getSelectedRow();if(row<0||row>=listIdRekam.size())return;int idR=listIdRekam.get(row).intValue();try(Connection k=Koneksi.getConnection()){if(k==null)return;PreparedStatement p=k.prepareStatement("SELECT k.no_rm,pa.nama,k.tanggal,dd.nama,k.tensi,r.subjektif,r.objektif,r.assessment,r.plan,r.kode_diagnosis,dg.nama_diagnosis FROM rekam_medis r JOIN kunjungan k ON r.id_kunjungan=k.id_kunjungan JOIN pasien pa ON k.no_rm=pa.no_rm LEFT JOIN dokter dd ON k.id_dokter=dd.id_dokter LEFT JOIN diagnosis dg ON r.kode_diagnosis=dg.kode_diagnosis WHERE r.id_rekam=?");p.setInt(1,idR);ResultSet r=p.executeQuery();if(r.next()){StringBuilder sb=new StringBuilder();sb.append("No RM    : ").append(r.getString(1)).append(" - ").append(r.getString(2)).append("\n");sb.append("Tanggal  : ").append(r.getString(3)).append("\n");sb.append("Dokter   : ").append(r.getString(4)==null?"-":r.getString(4)).append("\n");String tensi="";try{tensi=r.getString(5);}catch(Exception ex){tensi="";}if(tensi==null||tensi.isEmpty())tensi="-";sb.append("Tensi    : ").append(tensi).append("\n");sb.append("Diagnosa : ").append(r.getString(10)==null?"":r.getString(10)).append(" - ").append(r.getString(11)==null?"":r.getString(11)).append("\n");sb.append("----------------------------------------\n");sb.append("S : ").append(r.getString(6)==null?"":r.getString(6)).append("\n\n");sb.append("O : ").append(r.getString(7)==null?"":r.getString(7)).append("\n\n");sb.append("A : ").append(r.getString(8)==null?"":r.getString(8)).append("\n\n");sb.append("P : ").append(r.getString(9)==null?"":r.getString(9)).append("\n");sb.append("----------------------------------------\n");String isiResep="";
try{
PreparedStatement pr=k.prepareStatement("SELECT isi_resep FROM resep WHERE id_kunjungan=(SELECT id_kunjungan FROM rekam_medis WHERE id_rekam=?)");
pr.setInt(1,idR);
ResultSet rr=pr.executeQuery();
if(rr.next()&&rr.getString(1)!=null) isiResep=rr.getString(1);
rr.close();pr.close();
}catch(Exception exRes){}
sb.append("Resep : ").append(isiResep.isEmpty()?"- (tidak ada)":("\n"+isiResep)).append("\n\n");
sb.append("--------------------------------\n");sb.append("Odontogram:\n");PreparedStatement po=k.prepareStatement("SELECT no_gigi,kondisi FROM odontogram WHERE id_rekam=? ORDER BY no_gigi");po.setInt(1,idR);ResultSet ro=po.executeQuery();Map<String,String> map=new HashMap<String,String>();int c=0;while(ro.next()){String ng=ro.getString(1);String kd=ro.getString(2);sb.append(" ").append(ng).append(" : ").append(kd).append("\n");map.put(ng,kd);c++;}if(c==0)sb.append(" (semua normal)\n");txtDetail.setText(sb.toString());txtDetail.setCaretPosition(0);odo.setData(map);}}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}}
void makeOdoReadOnly(){try{odo.cKondisi.setEnabled(false);}catch(Exception e){}disableToothClicks(odo);}
void disableToothClicks(Container parent){for(Component c:parent.getComponents()){String cn=c.getClass().getName();if(cn.contains("ToothCanvas")){for(java.awt.event.MouseListener ml:c.getMouseListeners())c.removeMouseListener(ml);c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));}if(c instanceof JButton){String tx=((JButton)c).getText();if("Reset".equals(tx))c.setEnabled(false);}if(c instanceof Container)disableToothClicks((Container)c);}}
void cetakDetail(){if(txtDetail.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Pilih riwayat dulu!");return;}try{boolean ok=txtDetail.print(new java.text.MessageFormat("Rekam Medis - {0}"),new java.text.MessageFormat("Hal {0}"),true,null,null,true);if(!ok)JOptionPane.showMessageDialog(this,"Cetak dibatalkan.");}catch(Exception e){JOptionPane.showMessageDialog(this,"Gagal cetak: "+e.getMessage());}}
}