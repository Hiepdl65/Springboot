package com.example.jasper_report_app;

// import com.example.jasper_report_app.util.FontManager;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/simple-report")
@CrossOrigin(origins = "*")
public class APIReportController {

    // @Autowired
    // private FontManager fontManager;

    @PostMapping("/print")
    public ResponseEntity<byte[]> printReport(@RequestBody Map<String, Object> requestBody) {
        try {
            // Đăng ký font trước khi tạo báo cáo
            // fontManager.registerFonts();
            
            // Lấy dữ liệu từ request
            List<Map<String, Object>> data = (List<Map<String, Object>>) requestBody.get("data");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Báo cáo dữ liệu đơn giản");
            parameters.put("GENERATED_DATE", new java.util.Date());

            // Tạo datasource cho JasperReports
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            // Load và compile template
            InputStream reportStream = getClass().getResourceAsStream("/reports/Test_A4_Landscape.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "simple_report.pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 