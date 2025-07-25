package com.example.jasper_report_app.util;

import org.springframework.stereotype.Component;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.*;

@Component
public class FontManager {
    
    private static boolean fontsRegistered = false;
    
    public synchronized void registerFonts() {
        if (fontsRegistered) {
            System.out.println("ℹ️ Fonts đã được đăng ký trước đó");
            return;
        }
        
        try {
            System.out.println("🔧 Kiểm tra fonts hệ thống cho đa ngôn ngữ...");
            
            // Kiểm tra fonts có sẵn cho tiếng Việt và tiếng Hoa
            checkSystemFonts();
            
            fontsRegistered = true;
            System.out.println("✅ Hoàn thành kiểm tra fonts!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi kiểm tra fonts: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void checkSystemFonts() {
        System.out.println("🔍 Kiểm tra fonts hệ thống hỗ trợ đa ngôn ngữ...");
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        Set<String> systemFontSet = new HashSet<>(Arrays.asList(fontFamilies));
        
        // Fonts ưu tiên cho tiếng Việt và tiếng Hoa
        String[] priorityFonts = {
            "Arial Unicode MS",
            "DejaVu Sans",
            "DejaVu Serif", 
            "Liberation Sans",
            "Noto Sans CJK TC",
            "SansSerif"
        };
        
        System.out.println("📋 Fonts ưu tiên cho đa ngôn ngữ:");
        String bestFont = null;
        
        for (String fontName : priorityFonts) {
            boolean available = systemFontSet.contains(fontName);
            System.out.println("   " + (available ? "✅" : "❌") + " " + fontName);
            
            if (available && bestFont == null) {
                // Test font với tiếng Việt và tiếng Hoa
                if (testMultiLanguageSupport(fontName)) {
                    bestFont = fontName;
                }
            }
        }
        
        if (bestFont != null) {
            System.out.println("🎯 Font khuyến nghị: " + bestFont);
        } else {
            System.out.println("⚠️ Sử dụng fallback: SansSerif");
        }
        
        // Test fonts hỗ trợ đầy đủ
        testAvailableFonts();
    }
    
    private boolean testMultiLanguageSupport(String fontName) {
        try {
            Font font = new Font(fontName, Font.PLAIN, 12);
            
            // Test strings
            String vietnamese = "Tiếng Việt: àáạảãăắằặẳẵâấầậẩẫ èéẹẻẽêếềệểễ";
            String chinese = "中文: 你好世界 包子和小雞 简体中文 繁體中文";
            String english = "English: Hello World ABC xyz 123";
            
            boolean supportsVietnamese = font.canDisplayUpTo(vietnamese) == -1;
            boolean supportsChinese = font.canDisplayUpTo(chinese) == -1;
            boolean supportsEnglish = font.canDisplayUpTo(english) == -1;
            
            System.out.println("     🇻🇳 Tiếng Việt: " + (supportsVietnamese ? "✅" : "❌"));
            System.out.println("     🇨🇳 中文: " + (supportsChinese ? "✅" : "❌"));
            System.out.println("     🇺🇸 English: " + (supportsEnglish ? "✅" : "❌"));
            
            return supportsVietnamese && supportsEnglish; // Ít nhất phải hỗ trợ tiếng Việt
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private void testAvailableFonts() {
        System.out.println("\n🧪 Test fonts hỗ trợ đa ngôn ngữ:");
        
        String[] testFonts = {
            "Arial Unicode MS",
            "DejaVu Sans", 
            "DejaVu Serif",
            "Liberation Sans",
            "SansSerif"
        };
        
        String testText = "Hello Xin chào 你好世界 こんにちは 안녕하세요";
        
        for (String fontName : testFonts) {
            if (isFontAvailable(fontName)) {
                try {
                    Font font = new Font(fontName, Font.PLAIN, 12);
                    boolean fullySupported = font.canDisplayUpTo(testText) == -1;
                    System.out.println("   " + (fullySupported ? "✅" : "⚠️") + " " + fontName + 
                                     (fullySupported ? " - HOÀN HẢO" : " - MỘT PHẦN"));
                } catch (Exception e) {
                    System.out.println("   ❌ " + fontName + " - LỖI");
                }
            }
        }
    }
    
    public boolean isFontAvailable(String fontName) {
        if (fontName == null || fontName.trim().isEmpty()) {
            return false;
        }
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontNames = ge.getAvailableFontFamilyNames();
            
            for (String name : fontNames) {
                if (name.equalsIgnoreCase(fontName.trim())) {
                    return true;
                }
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi kiểm tra font '" + fontName + "': " + e.getMessage());
            return false;
        }
    }
    
    public String getBestAvailableFont() {
        // Thứ tự ưu tiên dựa trên fonts có sẵn
        String[] preferredFonts = {
            "Arial Unicode MS",
            "DejaVu Sans",
            "Liberation Sans", 
            "SansSerif"
        };
        
        for (String font : preferredFonts) {
            if (isFontAvailable(font)) {
                System.out.println("🎯 Chọn font tốt nhất: " + font);
                return font;
            }
        }
        
        System.out.println("⚠️ Sử dụng font mặc định: Dialog");
        return "Dialog";
    }
    
    public void listAvailableFonts() {
        System.out.println("=== FONTS HỖ TRỢ ĐA NGÔN NGỮ ===");
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontFamilies = ge.getAvailableFontFamilyNames();
            
            System.out.println("📊 Tổng số fonts: " + fontFamilies.length);
            
            // Fonts Unicode/đa ngôn ngữ
            System.out.println("\n📋 Fonts hỗ trợ Unicode/đa ngôn ngữ:");
            Arrays.stream(fontFamilies)
                  .filter(font -> font.toLowerCase().contains("unicode") ||
                                font.toLowerCase().contains("dejavu") ||
                                font.toLowerCase().contains("liberation") ||
                                font.toLowerCase().contains("noto") ||
                                font.toLowerCase().contains("arial"))
                  .sorted()
                  .forEach(font -> {
                      String testText = "Việt中文";
                      try {
                          Font f = new Font(font, Font.PLAIN, 12);
                          boolean supports = f.canDisplayUpTo(testText) == -1;
                          System.out.println("  " + (supports ? "✅" : "⚠️") + " " + font);
                      } catch (Exception e) {
                          System.out.println("  ❌ " + font);
                      }
                  });
                  
        } catch (Exception e) {
            System.err.println("❌ Lỗi liệt kê fonts: " + e.getMessage());
        }
    }
    
    public List<String> getAvailableFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontNames = ge.getAvailableFontFamilyNames();
            return Arrays.asList(fontNames);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi lấy danh sách fonts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean testVietnameseSupport(String fontName) {
        if (!isFontAvailable(fontName)) {
            return false;
        }
        
        String vietnameseText = "Tiếng Việt: àáạảãăắằặẳẵâấầậẩẫ èéẹẻẽêếềệểễ";
        
        try {
            Font font = new Font(fontName, Font.PLAIN, 12);
            return font.canDisplayUpTo(vietnameseText) == -1;
            
        } catch (Exception e) {
            return false;
        }
    }
}