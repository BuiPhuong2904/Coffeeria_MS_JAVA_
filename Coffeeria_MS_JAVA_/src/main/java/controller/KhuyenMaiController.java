package controller;

import View.E_voucherPanel;
import dao.KhuyenMaiDAO;
import java.awt.Component;
import java.awt.Window;
import java.util.Date;
import javax.swing.JOptionPane;
import model.KhuyenMai;

/**
 *
 * @author Bich Phuong
 */
public class KhuyenMaiController {
    private final Component view;
    private final E_voucherPanel khuyenMaiPanel;

    public KhuyenMaiController(Component view, E_voucherPanel khuyenMaiPanel) {
        this.view = view;
        this.khuyenMaiPanel = khuyenMaiPanel;
    }

    public void handleInsert(String maKM, String tenKM, String loaiKM, String giaTriStr,
                         String dieuKien, Date ngayBD, Date ngayKT, String trangThai) {
        if (maKM.isEmpty() || tenKM.isEmpty() || loaiKM.isEmpty() || giaTriStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ các trường bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (new KhuyenMaiDAO().existsByMaKM(maKM)) {
            JOptionPane.showMessageDialog(view, "Mã khuyến mãi đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double giaTriGiam;
            String loaiKMProcessed = loaiKM;

            if (giaTriStr.trim().endsWith("%")) {
                String percentStr = giaTriStr.trim().replace("%", "");
                giaTriGiam = Double.parseDouble(percentStr);
                loaiKMProcessed = "Giảm theo %";
            } else {
                giaTriGiam = Double.parseDouble(giaTriStr.trim());
                loaiKMProcessed = "Giảm theo cố định";
            }

            KhuyenMai km = new KhuyenMai(maKM, tenKM, loaiKMProcessed, giaTriGiam, dieuKien, ngayBD, ngayKT, trangThai);
            boolean success = new KhuyenMaiDAO().insertKhuyenMai(km);

            if (success) {
                JOptionPane.showMessageDialog(view, "Thêm khuyến mãi thành công!");
                if (khuyenMaiPanel != null) {
                    khuyenMaiPanel.loadAll();
                }
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm khuyến mãi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Giá trị giảm không hợp lệ. Nhập số hoặc số kèm %.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleUpdate(String maKM, String tenKM, String loaiKM, String giaTriStr,
                             String dieuKien, Date ngayBD, Date ngayKT, String trangThai,
                             Runnable afterUpdateCallback) {
        if (tenKM.isEmpty() || loaiKM.isEmpty() || giaTriStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double giaTriGiam;
            String loaiKMProcessed = loaiKM;

            if (giaTriStr.trim().endsWith("%")) {
                String percentStr = giaTriStr.trim().replace("%", "");
                giaTriGiam = Double.parseDouble(percentStr);
                loaiKMProcessed = "Giảm theo %";
            } else {
                giaTriGiam = Double.parseDouble(giaTriStr.trim());
                loaiKMProcessed = "Giảm theo cố định";
            }

            KhuyenMai km = new KhuyenMai(maKM, tenKM, loaiKMProcessed, giaTriGiam, dieuKien, ngayBD, ngayKT, trangThai);
            boolean success = new KhuyenMaiDAO().updateKhuyenMai(km);
            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                if (afterUpdateCallback != null) afterUpdateCallback.run();
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Giá trị giảm không hợp lệ. Nhập số hoặc số kèm %.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleDelete(String maKM, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = new KhuyenMaiDAO().deleteByMaKM(maKM);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                if (afterDeleteCallback != null) afterDeleteCallback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}