package com.example.jasper_report_app.controller;

import com.example.jasper_report_app.service.ReportService;
import com.example.jasper_report_app.util.FontManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.example.jasper_report_app.entity.PdaBarcodeData;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired
    private ReportService reportService;
    
    @Autowired
    private FontManager fontManager;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Report API is working!");
    }

    @GetMapping("/barcode-data")
    public List<PdaBarcodeData> getBarcodeData(
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String customerNo,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Integer limit
    ) {
        return reportService.getFilteredBarcodeData(barcode, customerNo, userId, limit);
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody Map<String, Object> requestBody) {
        try {
            // Đăng ký font trước khi tạo báo cáo
            fontManager.registerFonts();
            
            String reportName = "Test_A4_Landscape";
            Map<String, Object> parameters = new HashMap<>();
            if (requestBody.containsKey("parameters")) {
                parameters = (Map<String, Object>) requestBody.get("parameters");
            }
            parameters.put("REPORT_TITLE", "Báo cáo dữ liệu barcode với Arial Unicode MS");
            parameters.put("GENERATED_DATE", new java.util.Date());
            byte[] reportBytes = reportService.generateReport(reportName, parameters, "pdf");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report_arial_unicode.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/generate-with-arial-unicode")
    public ResponseEntity<byte[]> generateReportWithArialUnicode(@RequestBody Map<String, Object> requestBody) {
        try {
            // Đăng ký font trước
            fontManager.registerFonts();
            
            String reportName = "Test_A4_Landscape_Fixed";
            Map<String, Object> parameters = new HashMap<>();
            if (requestBody.containsKey("parameters")) {
                parameters = (Map<String, Object>) requestBody.get("parameters");
            }
            parameters.put("REPORT_TITLE", "Báo cáo dữ liệu barcode với Arial Unicode MS (Fixed)");
            parameters.put("GENERATED_DATE", new java.util.Date());
            byte[] reportBytes = reportService.generateReport(reportName, parameters, "pdf");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report_arial_unicode_fixed.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/generate-with-default-font")
    public ResponseEntity<byte[]> generateReportWithDefaultFont(@RequestBody Map<String, Object> requestBody) {
        try {
            String reportName = "Test_A4_Landscape_Default";
            Map<String, Object> parameters = new HashMap<>();
            if (requestBody.containsKey("parameters")) {
                parameters = (Map<String, Object>) requestBody.get("parameters");
            }
            parameters.put("REPORT_TITLE", "Báo cáo dữ liệu barcode với font mặc định");
            parameters.put("GENERATED_DATE", new java.util.Date());
            byte[] reportBytes = reportService.generateReport(reportName, parameters, "pdf");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report_default_font.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/font-status")
    public ResponseEntity<Map<String, Object>> getFontStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // Đăng ký font trước khi kiểm tra
        fontManager.registerFonts();
        
        boolean arialUnicodeAvailable = fontManager.isFontAvailable("Arial Unicode MS");
        status.put("arialUnicodeAvailable", arialUnicodeAvailable);
        status.put("message", "Font status checked");
        
        // Liệt kê các font Arial có sẵn
        fontManager.listAvailableFonts();
        
        return ResponseEntity.ok(status);
    }
}