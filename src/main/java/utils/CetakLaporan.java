package utils;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRJavacCompiler;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import koneksi.Koneksi;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.InputStream;

public class CetakLaporan {

    public static void cetak(String file, Map param) {
        try {
            System.out.println("=== CETAK: " + file + " ===");
            String namaJasper = file.endsWith(".jasper") ? file : file.replace(".jrxml", ".jasper");
            InputStream f = CetakLaporan.class.getResourceAsStream("/laporan/" + namaJasper);
            JasperReport jr = null;
            if (f != null) {
                jr = (JasperReport) JRLoader.loadObject(f);
            } else {
                String namaJrxml = file.replace(".jasper", ".jrxml");
                InputStream fx = CetakLaporan.class.getResourceAsStream("/laporan/" + namaJrxml);
                if (fx == null) {
                    JOptionPane.showMessageDialog(null, "File tidak ditemukan [" + namaJrxml + "]");
                    return;
                }
                JasperReportsContext ctx = DefaultJasperReportsContext.getInstance();
                ctx.setProperty("net.sf.jasperreports.compiler.source", "17");
                ctx.setProperty("net.sf.jasperreports.compiler.target", "17");
                JasperDesign design = JRXmlLoader.load(fx);
                design.setLanguage("java");
                JRJavacCompiler compiler = new JRJavacCompiler(ctx);
                jr = compiler.compileReport(design);
            }
            JasperPrint jp = JasperFillManager.fillReport(jr, param, Koneksi.getConnection());
            JasperViewer vwr = new JasperViewer(jp, false);
            vwr.setTitle("Laporan - SIM Klinik Gigi");
            vwr.setExtendedState(JFrame.MAXIMIZED_BOTH);
            vwr.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal cetak [" + file + "]:\n" + e.getMessage());
        }
    }
}
