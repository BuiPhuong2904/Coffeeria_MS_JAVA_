
package controller;

/**
 *
 * @author Bich Phuong
 */

import View.E_home_U;
import dao.TaiKhoanDAO;
import model.TaiKhoan;

import javax.swing.*;
import java.awt.Component;
import java.util.List;

public class TaiKhoanController {

    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final Component view;
    private final E_home_U taiKhoanView;

    public TaiKhoanController(Component view, E_home_U taiKhoanView) {
        this.view = view;
        this.taiKhoanView = taiKhoanView;
    }

    public void insertTaiKhoan(String email, String matKhau, String loaiTK, String trangThai) {
        if (email.isEmpty() || matKhau.isEmpty() || loaiTK.isEmpty() || trangThai.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (taiKhoanDAO.existsByEmail(email)) {
            JOptionPane.showMessageDialog(view, "Email đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setEmail(email);
        tk.setMatKhau(matKhau);
        tk.setLoaiTK(loaiTK);
        tk.setTrangThai(trangThai);

        String matk = taiKhoanDAO.insertTaiKhoan(tk);
        if (matk != null) {
            JOptionPane.showMessageDialog(view, "Tạo tài khoản thành công.");
            if (taiKhoanView != null) taiKhoanView.loadAll();
        } else {
            JOptionPane.showMessageDialog(view, "Tạo tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateTaiKhoan(String matk, String email, String matKhau, String loaiTK, String trangThai) {
        if (matk.isEmpty() || email.isEmpty() || matKhau.isEmpty() || loaiTK.isEmpty() || trangThai.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoan tk = new TaiKhoan(matk, email, matKhau, loaiTK, trangThai);

        boolean success = taiKhoanDAO.updateTaiKhoan(tk);
        if (success) {
            JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thành công.");
            if (taiKhoanView != null) taiKhoanView.loadAll();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteTaiKhoan(String matk, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = taiKhoanDAO.deleteById(matk);
            if (deleted) {
                JOptionPane.showMessageDialog(view, "Xóa tài khoản thành công.");
                if (taiKhoanView != null) taiKhoanView.loadAll();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public TaiKhoan getTaiKhoanById(String matk) {
        return taiKhoanDAO.findById(matk);
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanDAO.findAll();
    }

    public TaiKhoan dangNhap(String email, String matKhau) {
        return taiKhoanDAO.dangNhap(email, matKhau);
    }
}