package com.example.jasper_report_app.repository;

import com.example.jasper_report_app.entity.PdaBarcodeData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PdaBarcodeDataRepository extends JpaRepository<PdaBarcodeData, String> {
    List<PdaBarcodeData> findByBarcodeContainingAndCustomerNoContainingAndUserIdContaining(String barcode, String customerNo, String userId);
}
