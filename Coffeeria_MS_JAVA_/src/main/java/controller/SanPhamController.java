
package controller;

/**
 *
 * @author Bich Phuong
 */

import View.E_inventoryPanel;
import dao.SanPhamDAO;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JOptionPane;
import model.SanPham;
import java.util.Date;

public class SanPhamController {
    private final Component view;
    private final E_inventoryPanel sanPhamPanel;

    public SanPhamController(Component view, E_inventoryPanel sanPhamPanel) {
        this.view = view;
        this.sanPhamPanel = sanPhamPanel;
    }

    public void handleInsert(String maSP, String tenSP, String loaiSP, String tongSLStr, String donViTinh,
                             String giaNhapStr, Date ngaySX, Date hanSD, String trangThai) {
        if (maSP.isEmpty() || tenSP.isEmpty() || giaNhapStr.isEmpty() || donViTinh.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đủ các trường bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (new SanPhamDAO().existsByMaSP(maSP)) {
            JOptionPane.showMessageDialog(view, "Mã sản phẩm đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tongSL = 0;
            if (!tongSLStr.isEmpty()) {
                tongSL = Integer.parseInt(tongSLStr);
            }
            double giaNhap = Double.parseDouble(giaNhapStr);

            SanPham sp = new SanPham(maSP, tenSP, loaiSP, tongSL, donViTinh, giaNhap, ngaySX, hanSD, trangThai);
            boolean success = new SanPhamDAO().insertSanPham(sp);
            if (success) {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thành công!");
                if (sanPhamPanel != null) {
                    sanPhamPanel.loadAll(); // cập nhật bảng dữ liệu nếu cần
                }
                ((Window) view).dispose(); // đóng form
            } else {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Số lượng và giá nhập phải là số hợp lệ.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleUpdate(String maSP, String tenSP, String loaiSP, String tongSLStr, String donViTinh, String trangThai,
                         String giaNhapStr, Date ngaySX, Date hanSD, Runnable afterUpdateCallback) {
        if (tenSP.isEmpty() || giaNhapStr.isEmpty() || donViTinh.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ các trường bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int tongSL = 0;
            if (!tongSLStr.isEmpty()) {
                tongSL = Integer.parseInt(tongSLStr);
            }

            String cleanGiaNhapStr = giaNhapStr.replace(".", "").replace(",", "").trim();
            double giaNhap = Double.parseDouble(cleanGiaNhapStr);

            SanPham sp = new SanPham(maSP, tenSP, loaiSP, tongSL, donViTinh, giaNhap, ngaySX, hanSD, trangThai);
            boolean success = new SanPhamDAO().updateSanPham(sp);
            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thành công!");
                if (afterUpdateCallback != null) afterUpdateCallback.run(); 
                ((Window) view).dispose(); 
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Số lượng và giá nhập phải là số hợp lệ.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void handleDelete(String maSP, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = new SanPhamDAO().deleteByMaSP(maSP);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa sản phẩm thành công!");
                if (afterDeleteCallback != null) {
                    afterDeleteCallback.run(); 
                }
            } else {
                JOptionPane.showMessageDialog(view, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}