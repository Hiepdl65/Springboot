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
            
            // Sử dụng empty datasource vì template không có detail band
            JRDataSource dataSource = new JREmptyDataSource();
            
            // Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
    

}