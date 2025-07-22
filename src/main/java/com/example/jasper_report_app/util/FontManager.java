package com.example.jasper_report_app.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
import java.awt.Font;
import java.awt.FontFormatException;

@Component
public class FontManager {
    
    private static boolean fontsRegistered = false;
    
    public synchronized void registerFonts() {
        if (fontsRegistered) {
            return;
        }
        
        try {
            // Đăng ký font với hệ thống
            registerSystemFont();
            
            // Kiểm tra font có sẵn trong JasperReports
            checkJasperReportsFont();
            
            fontsRegistered = true;
            System.out.println("✅ Font đã được đăng ký thành công!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi đăng ký font: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void registerSystemFont() throws IOException, FontFormatException {
        ClassPathResource fontResource = new ClassPathResource("fonts/ARIALUNI.TTF");
        
        if (!fontResource.exists()) {
            throw new IOException("Font file không tồn tại: fonts/ARIALUNI.TTF");
        }
        
        // Đăng ký với Java AWT
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontResource.getInputStream());
        java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        
        System.out.println("📝 Font family name: " + customFont.getFamily());
        System.out.println("📝 Font name: " + customFont.getName());
    }
    
    private void checkJasperReportsFont() {
        try {
            // Kiểm tra font có sẵn trong JasperReports
            System.out.println("🔍 Kiểm tra font trong JasperReports...");
            
            // List available font families
            System.out.println("📋 Font families có sẵn trong JasperReports:");
            // Note: JasperReports sẽ tự động load từ extension
            
        } catch (Exception e) {
            System.err.println("⚠️ Cảnh báo khi kiểm tra JasperReports font: " + e.getMessage());
        }
    }
    
    public boolean isFontAvailable(String fontName) {
        java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        
        for (String name : fontNames) {
            if (name.equalsIgnoreCase(fontName)) {
                return true;
            }
        }
        return false;
    }
    
    public void listAvailableFonts() {
        java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        
        System.out.println("📋 Font families có sẵn:");
        for (String name : fontNames) {
            if (name.toLowerCase().contains("arial") || 
                name.toLowerCase().contains("unicode") ||
                name.toLowerCase().contains("noto")) {
                System.out.println("  ✓ " + name);
            }
        }
    }
}