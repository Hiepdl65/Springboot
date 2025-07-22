package com.example.jasper_report_app.service;

import com.example.jasper_report_app.entity.PdaBarcodeData;
import com.example.jasper_report_app.repository.PdaBarcodeDataRepository;
import com.example.jasper_report_app.util.FontManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private PdaBarcodeDataRepository repository;
    
    @Autowired
    private FontManager fontManager;

    public List<PdaBarcodeData> getAllBarcodeData() {
        return repository.findAll();
    }

    public byte[] generateReport(String reportName, Map<String, Object> parameters, String format) {
        try {
            // Đăng ký font trước khi tạo báo cáo
            fontManager.registerFonts();
            
            InputStream reportStream = getClass().getResourceAsStream("/reports/" + reportName + ".jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Template file not found: " + reportName + ".jrxml");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            List<PdaBarcodeData> data = repository.findAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
    
    public List<PdaBarcodeData> getFilteredBarcodeData(String barcode, String customerNo, String userId, Integer limit) {
        String b = barcode != null ? barcode : "";
        String c = customerNo != null ? customerNo : "";
        String u = userId != null ? userId : "";
        List<PdaBarcodeData> result = repository.findByBarcodeContainingAndCustomerNoContainingAndUserIdContaining(b, c, u);
        if (limit != null && limit > 0 && limit < result.size()) {
            return result.subList(0, limit);
        }
        return result;
    }
}