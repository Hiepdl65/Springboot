package com.example.jasper_report_app.service;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.List;

@Service
public class ReportService {

    public byte[] generateReport(String reportName, Map<String, Object> parameters, String format) {
        try {
            // Load template
            InputStream reportStream = getClass().getResourceAsStream("/reports/" + reportName + ".jrxml");
            
            if (reportStream == null) {
                throw new RuntimeException("Template file not found: " + reportName + ".jrxml");
            }
            
            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            // Tạo dữ liệu mẫu cho report
            List<Map<String, Object>> data = createSampleData();
            JRDataSource dataSource = new JRBeanCollectionDataSource(data);
            
            // Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
    
    private List<Map<String, Object>> createSampleData() {
        return List.of(
            Map.of(
                "barcode", "123456789",
                "customer_no", "CUST001", 
                "pallet_no", "PAL001",
                "quantity", "100",
                "length", "50cm",
                "scan_date", "2025-07-11",
                "modi_date", "2025-07-11",
                "user_id", "USER001",
                "is_used", true
            ),
            Map.of(
                "barcode", "987654321",
                "customer_no", "CUST002",
                "pallet_no", "PAL002", 
                "quantity", "200",
                "length", "75cm",
                "scan_date", "2025-07-11",
                "modi_date", "2025-07-11",
                "user_id", "USER002",
                "is_used", false
            ),
            Map.of(
                "barcode", "555666777",
                "customer_no", "CUST003",
                "pallet_no", "PAL003",
                "quantity", "150", 
                "length", "60cm",
                "scan_date", "2025-07-11",
                "modi_date", "2025-07-11",
                "user_id", "USER003",
                "is_used", true
            )
        );
    }
}