
package controller;

/**
 *
 * @author Bich Phuong
 */
import View.E_home_IE;
import dao.PhieuKhoDAO;
import model.PhieuKho;
import model.CT_PhieuKho;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PhieuKhoController {
    private final PhieuKhoDAO phieuKhoDAO = new PhieuKhoDAO();
    private final Component view;
    private final E_home_IE phieuKhoView;

    public PhieuKhoController(Component view, E_home_IE phieuKhoView) {
        this.view = view;
        this.phieuKhoView = phieuKhoView;
    }

    // Thêm phiếu kho mới cùng chi tiết
    public void insertPhieuKho(PhieuKho phieuKho, List<CT_PhieuKho> chiTietList) {
        if (phieuKho.getMaPhieu().isEmpty() || phieuKho.getNgayGiaoDich() == null ||
                phieuKho.getLoaiPhieu().isEmpty() || phieuKho.getMaNV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin phiếu kho.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = phieuKhoDAO.insertPhieuKho(phieuKho, chiTietList);
        if (success) {
            JOptionPane.showMessageDialog(view, "Thêm phiếu kho thành công!");
            phieuKhoView.loadAll();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm phiếu kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật phiếu kho và danh sách chi tiết
    public void updatePhieuKho(PhieuKho phieuKho, List<CT_PhieuKho> chiTietList) {
        if (phieuKho.getMaPhieu().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập mã phiếu cần cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = phieuKhoDAO.updatePhieuKhoWithChiTiet(phieuKho, chiTietList);

        if (success) {
            JOptionPane.showMessageDialog(view, "Cập nhật phiếu kho thành công!");
            phieuKhoView.loadAll();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xoá phiếu kho và chi tiết
    public void deletePhieuKho(String maPhieu, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa phiếu kho này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = phieuKhoDAO.deletePhieuKho(maPhieu);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa phiếu kho thành công!");
                if (afterDeleteCallback != null) afterDeleteCallback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa phiếu kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}