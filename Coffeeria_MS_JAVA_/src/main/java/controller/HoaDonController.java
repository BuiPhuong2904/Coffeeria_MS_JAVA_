
package controller;

/**
 *
 * @author Bich Phuong
 */

import View.E_billPanel;
import dao.HoaDonDAO;
import model.HoaDon;
import model.CT_HoaDon;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dao.MonAnDAO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class HoaDonController {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private Component view;
    private E_billPanel hoaDonView;

    public HoaDonController(Component view, E_billPanel hoaDonView) {
        this.view = view;
        this.hoaDonView = hoaDonView;
    }

    public HoaDonController(E_billPanel hoaDonView) {
        this.hoaDonView = hoaDonView;
    }

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
    }
    
    public void insertHoaDon(HoaDon hoaDon, List<CT_HoaDon> chiTietList) {
        if (hoaDon.getNgayLap() == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin hóa đơn.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = hoaDonDAO.insertHoaDon(hoaDon, chiTietList);
        if (success) {
            JOptionPane.showMessageDialog(view, "Thêm hóa đơn thành công!");
            if (hoaDonView != null) {
                hoaDonView.loadAll();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Thêm hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật hóa đơn và danh sách chi tiết
    public void updateHoaDon(HoaDon hoaDon, List<CT_HoaDon> chiTietList) {
        if (hoaDon.getMaHD().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập mã hóa đơn cần cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = hoaDonDAO.updateHoaDonWithChiTiet(hoaDon, chiTietList);
        if (success) {
            JOptionPane.showMessageDialog(view, "Cập nhật hóa đơn thành công!");
            hoaDonView.loadAll();
            ((Window) view).dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa hóa đơn và chi tiết
    public void deleteHoaDon(String maHoaDon, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa hóa đơn này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = hoaDonDAO.deleteHoaDon(maHoaDon);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa hóa đơn thành công!");
                if (afterDeleteCallback != null) afterDeleteCallback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public Map<String, Double> getDoanhThuTheoThang() {
        return hoaDonDAO.getDoanhThuTheoThang();
    }
    
    public Map<String, Integer> getTiLeDanhMucMonAn() {
        return hoaDonDAO.getTiLeDanhMucMonAn();
    }

    public void xuatHoaDonPDF(HoaDon hoaDon, List<CT_HoaDon> chiTietList) throws IOException {
        String fileName = "HoaDon_" + (hoaDon.getMaHD() != null ? hoaDon.getMaHD() : System.currentTimeMillis()) + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            String fontPath = "C:/Windows/Fonts/arial.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(baseFont, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font textFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.NORMAL);

            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Mã nhân viên: " + hoaDon.getMaNV(), textFont));
            document.add(new Paragraph("Mã khách hàng: " + hoaDon.getMaKH(), textFont));
            document.add(new Paragraph("Ngày lập: " + hoaDon.getNgayLap().toString(), textFont));
            if (hoaDon.getMaKM() != null)
                document.add(new Paragraph("Mã Khuyến mãi: " + hoaDon.getMaKM(), textFont));
            document.add(new Paragraph("Hình thức thanh toán: " + hoaDon.getHinhThucTT(), textFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell(new PdfPCell(new Phrase("Tên món", textFont)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", textFont)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", textFont)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", textFont)));

            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);

            MonAnDAO monAnDAO = new MonAnDAO();
            for (CT_HoaDon ct : chiTietList) {
                String tenMon = monAnDAO.getTenMonByMa(ct.getMaMon());
                String donGiaStr = formatter.format(ct.getDonGia()) + " VND";
                String thanhTienStr = formatter.format(ct.getDonGia() * ct.getSoLuong()) + " VND";

                table.addCell(new PdfPCell(new Phrase(tenMon, textFont)));
                table.addCell(new PdfPCell(new Phrase(donGiaStr, textFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), textFont)));
                table.addCell(new PdfPCell(new Phrase(thanhTienStr, textFont)));
            }

            document.add(table);

            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);

            document.add(new Paragraph("Tổng tiền trước: " + formatter.format(hoaDon.getTongTienTruoc()) + " VND", textFont));
            document.add(new Paragraph("Tiền giảm giá: " + formatter.format(hoaDon.getTienGiamGia()) + " VND", textFont));
            document.add(new Paragraph("Tổng tiền sau: " + formatter.format(hoaDon.getTongTienSau()) + " VND", textFont));

            document.close();

            JOptionPane.showMessageDialog(null, "Hóa đơn đã được lưu thành PDF: " + fileName, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | FileNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất hóa đơn PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                File pdfFile = new File(fileName);
                Desktop.getDesktop().open(pdfFile);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Không thể mở file PDF: " + ex.getMessage());
            }
        }
    }
}
