package com.example.jasper_report_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "pda_barcode_data")
public class PdaBarcodeData {
    @Id
    @Column(length = 500)
    private String barcode;

    @Column(name = "customer_no", length = 500)
    private String customerNo;

    @Column(name = "pallet_no", length = 500)
    private String palletNo;

    @Column(length = 10)
    private String quantity;

    @Column(length = 10)
    private String length;

    @Column(name = "scan_date", length = 20)
    private String scanDate;

    @Column(name = "modi_date", length = 20)
    private String modiDate;

    @Column(name = "user_id", length = 5)
    private String userId;

    @Column(name = "is_used")
    private Boolean isUsed;

    // Getters and Setters
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getCustomerNo() { return customerNo; }
    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }

    public String getPalletNo() { return palletNo; }
    public void setPalletNo(String palletNo) { this.palletNo = palletNo; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

    public String getScanDate() { return scanDate; }
    public void setScanDate(String scanDate) { this.scanDate = scanDate; }

    public String getModiDate() { return modiDate; }
    public void setModiDate(String modiDate) { this.modiDate = modiDate; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }
}