
package controller;

import View.C_Homepage;
import View.C_Signin;
import View.E_Homepage;
import dao.TaiKhoanDAO;
import javax.swing.JOptionPane;
import model.TaiKhoan;

/**
 *
 * @author Bich Phuong
 */
public class SigninController {
    private final TaiKhoanDAO taiKhoanDAO;
    private final C_Signin view;

    public SigninController(C_Signin view) {
        this.view = view;
        this.taiKhoanDAO = new TaiKhoanDAO();
    }

    public void handleLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập email và mật khẩu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoan tk = taiKhoanDAO.dangNhap(email, password);
        if (tk == null) {
            JOptionPane.showMessageDialog(view, "Sai email hoặc mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(view, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

        // Mở giao diện tương ứng
        switch (tk.getLoaiTK()) {
            case "Employee", "Manager" -> new E_Homepage(tk).setVisible(true);
            case "Customer" -> new C_Homepage(tk).setVisible(true);
            default -> {
                JOptionPane.showMessageDialog(view, "Không xác định được quyền truy cập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        view.dispose();
    }
}