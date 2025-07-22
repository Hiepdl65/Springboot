# Khắc phục vấn đề Font Arial Unicode MS trong Jasper Reports

## Vấn đề
Template Jasper Report đang sử dụng font "Arial Unicode MS" nhưng khi in bị lỗi vì font không được đăng ký với JasperReports.

## Giải pháp đã triển khai

### 1. Tạo FontManager Utility
- File: `src/main/java/com/example/jasper_report_app/util/FontManager.java`
- Chức năng: Đăng ký font Arial Unicode MS với Java GraphicsEnvironment
- Tự động copy font file từ resources ra thư mục temp và đăng ký

### 2. Cập nhật ReportService
- Tự động gọi `fontManager.registerFonts()` trước khi tạo báo cáo
- Đảm bảo font được đăng ký trước khi JasperReports render

### 3. Sửa template gốc để sử dụng Arial Unicode MS
- `Test_A4_Landscape.jrxml`: Template gốc đã được sửa để sử dụng font Arial Unicode MS
- `Test_A4_Landscape_Fixed.jrxml`: Template với font Arial Unicode MS + PDF embedding
- `Test_A4_Landscape_Default.jrxml`: Template với font mặc định (không có lỗi)

### 4. Cập nhật APIReportController
- Thêm FontManager để đăng ký font tự động
- Thêm 3 endpoints khác nhau cho các template khác nhau

### 5. Thêm endpoints mới
- **ReportController** (`/api/reports/`):
  - `/generate`: Tạo báo cáo với template gốc (Arial Unicode MS)
  - `/generate-with-arial-unicode`: Tạo báo cáo với template Fixed (Arial Unicode MS + PDF embedding)
  - `/generate-with-default-font`: Tạo báo cáo với font mặc định
  - `/font-status`: Kiểm tra trạng thái font

- **APIReportController** (`/api/simple-report/`):
  - `/print`: Tạo báo cáo với template gốc (Arial Unicode MS)
  - `/print-with-arial-unicode`: Tạo báo cáo với template Fixed (Arial Unicode MS + PDF embedding)
  - `/print-with-default-font`: Tạo báo cáo với font mặc định

## Cách sử dụng

### 1. Test font status
```bash
curl http://localhost:8080/api/reports/font-status
```

### 2. Test với APIReportController (Simple Report)
```bash
# Test với template gốc (Arial Unicode MS)
curl -X POST http://localhost:8080/api/simple-report/print \
  -H "Content-Type: application/json" \
  -d '{"data": [{"barcode": "123", "customerNo": "CUST01", "palletNo": "PALLET01", "quantity": "10", "length": "100", "scanDate": "2024-06-01", "userId": "U001", "isUsed": false}]}' \
  --output simple_report.pdf

# Test với template Fixed (Arial Unicode MS + PDF embedding)
curl -X POST http://localhost:8080/api/simple-report/print-with-arial-unicode \
  -H "Content-Type: application/json" \
  -d '{"data": [{"barcode": "123", "customerNo": "CUST01", "palletNo": "PALLET01", "quantity": "10", "length": "100", "scanDate": "2024-06-01", "userId": "U001", "isUsed": false}]}' \
  --output simple_report_arial_unicode.pdf

# Test với font mặc định (khuyến nghị nếu có lỗi)
curl -X POST http://localhost:8080/api/simple-report/print-with-default-font \
  -H "Content-Type: application/json" \
  -d '{"data": [{"barcode": "123", "customerNo": "CUST01", "palletNo": "PALLET01", "quantity": "10", "length": "100", "scanDate": "2024-06-01", "userId": "U001", "isUsed": false}]}' \
  --output simple_report_default_font.pdf
```

### 3. Test với ReportController (Database Report)
```bash
# Test với template gốc (Arial Unicode MS)
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"parameters": {"test": "data"}}' \
  --output report_arial_unicode.pdf

# Test với template Fixed (Arial Unicode MS + PDF embedding)
curl -X POST http://localhost:8080/api/reports/generate-with-arial-unicode \
  -H "Content-Type: application/json" \
  -d '{"parameters": {"test": "data"}}' \
  --output report_arial_unicode_fixed.pdf

# Test với font mặc định (khuyến nghị nếu có lỗi)
curl -X POST http://localhost:8080/api/reports/generate-with-default-font \
  -H "Content-Type: application/json" \
  -d '{"parameters": {"test": "data"}}' \
  --output report_default_font.pdf
```

### 4. Test với Web Interface
Truy cập: `http://localhost:8080/test-simple-report.html`

### 5. Sử dụng trong frontend
```javascript
// Test font status
fetch('http://localhost:8080/api/reports/font-status')
  .then(response => response.json())
  .then(data => console.log('Font status:', data));

// Generate report with APIReportController (Simple Report)
fetch('http://localhost:8080/api/simple-report/print', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ 
    data: [
      {
        barcode: "123",
        customerNo: "CUST01",
        palletNo: "PALLET01",
        quantity: "10",
        length: "100",
        scanDate: "2024-06-01",
        userId: "U001",
        isUsed: false
      }
    ]
  })
})
.then(response => response.blob())
.then(blob => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'simple_report.pdf';
  a.click();
});

// Generate report with ReportController (Database Report)
fetch('http://localhost:8080/api/reports/generate', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ parameters: { test: 'data' } })
})
.then(response => response.blob())
.then(blob => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'report_arial_unicode.pdf';
  a.click();
});
```

## Cấu trúc file font
```
src/main/resources/fonts/
└── ARIALUNI.TTF  # Font Arial Unicode MS
```

## Lưu ý quan trọng

1. **Font file phải có trong thư mục resources**: Đảm bảo file `ARIALUNI.TTF` có trong `src/main/resources/fonts/`

2. **Font được đăng ký tự động**: FontManager sẽ tự động đăng ký font khi cần thiết

3. **Template gốc đã được sửa**: `Test_A4_Landscape.jrxml` giờ đã sử dụng font Arial Unicode MS

4. **PDF embedding**: Template `Test_A4_Landscape_Fixed.jrxml` sử dụng `isPdfEmbedded="true"` để đảm bảo font được embed vào PDF

5. **2 Controllers khác nhau**:
   - **APIReportController**: Cho simple report với data từ request
   - **ReportController**: Cho database report với data từ database

## Troubleshooting

### Nếu vẫn gặp lỗi font:
1. **Sử dụng template với font mặc định**: `Test_A4_Landscape_Default.jrxml`
2. Kiểm tra file font có tồn tại không: `src/main/resources/fonts/ARIALUNI.TTF`
3. Kiểm tra log để xem font có được đăng ký thành công không
4. Test endpoint `/api/reports/font-status` để kiểm tra trạng thái font

### Log messages:
- ✅ Font Arial Unicode MS đã được đăng ký thành công!
- 📁 Font file location: [path]
- 📋 Danh sách font có sẵn: [font list]
- ❌ Lỗi đăng ký font: [error message]

## Kết quả mong đợi

### Với template gốc (Test_A4_Landscape.jrxml):
- Font Arial Unicode MS sẽ được hiển thị đúng trong PDF
- Text tiếng Việt và ký tự đặc biệt sẽ được render chính xác
- Font được đăng ký tự động trước khi tạo báo cáo

### Với template Fixed (Test_A4_Landscape_Fixed.jrxml):
- Font Arial Unicode MS sẽ được hiển thị đúng trong PDF
- Text tiếng Việt và ký tự đặc biệt sẽ được render chính xác
- Font được embed vào PDF

### Với template mặc định (Test_A4_Landscape_Default.jrxml):
- Báo cáo sẽ được tạo thành công không có lỗi font
- Text tiếng Việt và ký tự đặc biệt sẽ được render chính xác
- Sử dụng font mặc định của hệ thống

## Khuyến nghị

**Sử dụng endpoint `/api/reports/generate` hoặc `/api/simple-report/print`** để tạo báo cáo với template gốc đã được sửa để sử dụng font Arial Unicode MS. Nếu vẫn gặp lỗi, hãy sử dụng `/api/reports/generate-with-default-font` hoặc `/api/simple-report/print-with-default-font` để đảm bảo báo cáo luôn được tạo thành công. 