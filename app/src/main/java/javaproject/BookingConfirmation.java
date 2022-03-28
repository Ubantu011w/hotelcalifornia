package javaproject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.ResourceUtils;

/**
 * Booking confirmation class.
 */

public class BookingConfirmation implements Runnable {
  

  private BookingData data;

  public BookingConfirmation(BookingData data) {
    this.data = data;
  }

  /**
   * Function to generate PDF file.
   */
  public void generatePdf(BookingData bd) {
    List<BookingData> reportInfo = new ArrayList<>();
    reportInfo.add(bd);
    String projectPath = System.getProperty("user.dir"); // Hämtar ditt directory oavsett dator
    String outfile = projectPath + "/pdf/bookingConfirmation" + bd.getResId() + ".pdf"; 

    try {
      File template = ResourceUtils.getFile(projectPath + "/src/main/resources/javaproject/confirmationTemplate.jrxml");
      //Kompilerar jxml filen
      JasperReport report = JasperCompileManager.compileReport(template.getAbsolutePath());

      Map<String, Object>  parameters = new HashMap<String, Object>();
      parameters.put("Booking", "Confirmation");

      //Gör datan till en JRDataSource.
      JRDataSource data = new JRBeanCollectionDataSource(reportInfo);

      //Skapar pdfen och exporterar den.
      JasperPrint printPdf = JasperFillManager.fillReport(report, parameters, data);
      JasperExportManager.exportReportToPdfFile(printPdf, outfile);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    generatePdf(data);
    
  }
}
