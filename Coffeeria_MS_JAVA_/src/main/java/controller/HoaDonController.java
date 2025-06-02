
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

public class HoaDonController {
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final Component view;
    private final E_billPanel hoaDonView;

    public HoaDonController(Component view, E_billPanel hoaDonView) {
        this.view = view;
        this.hoaDonView = hoaDonView;
    }

    public HoaDonController(E_billPanel aThis) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void insertHoaDon(HoaDon hoaDon, List<CT_HoaDon> chiTietList) {
        if (hoaDon.getMaHD().isEmpty() || hoaDon.getNgayLap() == null ||
            hoaDon.getMaKH().isEmpty() || hoaDon.getMaNV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin hóa đơn.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = hoaDonDAO.insertHoaDon(hoaDon, chiTietList);
        if (success) {
            JOptionPane.showMessageDialog(view, "Thêm hóa đơn thành công!");
            hoaDonView.loadAll();
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
}
