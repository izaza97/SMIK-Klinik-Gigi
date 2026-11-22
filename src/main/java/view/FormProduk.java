package view;
import utils.Layar;
import javax.swing.*;
public class FormProduk extends JFrame{
public FormProduk(){
FormTindakanResep f=new FormTindakanResep();
f.setVisible(true);
try{f.pilihTabProduk();}catch(Exception e){}
this.dispose();
}
}