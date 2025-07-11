package com.example.jasper_report_app.controller;

import com.example.jasper_report_app.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Report API is working!");
    }

    @GetMapping("/data")
    public ResponseEntity<List<Map<String, Object>>> getSampleData() {
        // Tạo dữ liệu mẫu
        List<Map<String, Object>> data = Arrays.asList(
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
        
        return ResponseEntity.ok(data);
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody Map<String, Object> requestBody) {
        try {
            // Sử dụng tên file template thực tế (không có .jrxml)
            String reportName = "Test_A4_Landscape";
            
            // Lấy parameters từ request body
            Map<String, Object> parameters = new HashMap<>();
            if (requestBody.containsKey("parameters")) {
                parameters = (Map<String, Object>) requestBody.get("parameters");
            }
            
            // Thêm thông tin báo cáo
            parameters.put("REPORT_TITLE", "Báo cáo dữ liệu barcode");
            parameters.put("GENERATED_DATE", new java.util.Date());
            
            // Generate PDF
            byte[] reportBytes = reportService.generateReport(reportName, parameters, "pdf");
            
            // Response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}