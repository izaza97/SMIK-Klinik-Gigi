package utils;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
public class Tema{
public static final Color PRIMER=new Color(18,48,71);
public static final Color PRIMER_GELAP=new Color(9,92,72);
public static final Color PRIMER_MUDA=new Color(230,244,240);
public static final Color BG=new Color(243,246,249);
public static final Color TEKS=new Color(31,41,55);
public static final Color ABU=new Color(107,114,128);
public static final Color BORDER=new Color(229,231,235);
public static void initGlobal(){UIManager.put("Button.arc",14);UIManager.put("Component.arc",14);UIManager.put("TextComponent.arc",10);UIManager.put("Table.rowHeight",28);}
public static void primary(JButton b){b.setBackground(PRIMER);b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));b.setMargin(new Insets(6,14,6,14));b.setMinimumSize(new Dimension(100,32));}
public static void success(JButton b){b.setBackground(new Color(22,163,74));b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));b.setMargin(new Insets(6,14,6,14));b.setMinimumSize(new Dimension(100,32));}
public static void danger(JButton b){b.setBackground(new Color(220,38,38));b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));b.setMargin(new Insets(6,14,6,14));b.setMinimumSize(new Dimension(100,32));}
public static void secondary(JButton b){b.setBackground(Color.WHITE);b.setForeground(PRIMER);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));b.setMargin(new Insets(6,14,6,14));b.setMinimumSize(new Dimension(100,32));b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PRIMER,1,true),BorderFactory.createEmptyBorder(6,14,6,14)));}
public static JPanel kartu(){JPanel p=new JPanel();p.setBackground(Color.WHITE);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1),BorderFactory.createEmptyBorder(12,14,12,14)));return p;}
public static JPanel header(String j,String s){JPanel p=new JPanel(new BorderLayout());p.setBackground(PRIMER);p.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));JPanel t=new JPanel(new GridLayout(2,1));t.setOpaque(false);JLabel a=new JLabel(j);a.setForeground(Color.WHITE);a.setFont(new Font("Segoe UI",Font.BOLD,17));JLabel b=new JLabel(s);b.setForeground(new Color(214,230,245));b.setFont(new Font("Segoe UI",Font.PLAIN,12));t.add(a);t.add(b);p.add(t,BorderLayout.WEST);try{JLabel logo=new JLabel(KlinikLogo.iconCircle(44));logo.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));p.add(logo,BorderLayout.EAST);}catch(Exception e){}return p;}
public static void styleTable(JTable tb){tb.setRowHeight(28);tb.setFont(new Font("Segoe UI",Font.PLAIN,12));tb.setGridColor(BORDER);tb.setShowGrid(true);tb.setSelectionBackground(PRIMER_MUDA);tb.setSelectionForeground(TEKS);JTableHeader h=tb.getTableHeader();h.setBackground(PRIMER);h.setForeground(Color.WHITE);h.setFont(new Font("Segoe UI",Font.BOLD,12));}
}