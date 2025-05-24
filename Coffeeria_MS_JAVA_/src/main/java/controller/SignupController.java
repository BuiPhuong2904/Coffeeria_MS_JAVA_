package controller;

import dao.TaiKhoanDAO;
import dao.KhachHangDAO;
import model.TaiKhoan;
import View.C_Signup;
import View.C_Signin;
import dao.NhanVienDAO;

import javax.swing.*;
import model.NhanVien;

public class SignupController {
    private final TaiKhoanDAO taiKhoanDAO;
    private final C_Signup view;
    private final NhanVienDAO NhanVienDAO;

    public SignupController(C_Signup view) {
        this.view = view;
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.NhanVienDAO = new NhanVienDAO();
    }
    
    public void handleSignup(String fullName, String sdt, String email, String password) {
        String hoten = fullName;
        if (hoten.isEmpty() || sdt.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please complete all required fields.", "Notification", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (taiKhoanDAO.existsByEmail(email)) {
            JOptionPane.showMessageDialog(view, "Email is exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaiKhoan tk = new TaiKhoan(email, password, "Employee", "Active");
        String matk = taiKhoanDAO.insertTaiKhoan(tk);
        
        if (matk == null) {
            JOptionPane.showMessageDialog(view, "Tạo tài khoản thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        NhanVien nv = new NhanVien(hoten, null, null, sdt, null, null, null, 0, null, matk);
        boolean nv1 = NhanVienDAO.insertNhanVien(nv);

        if (nv1) {
            JOptionPane.showMessageDialog(view, "Sign-up successful. Welcome!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new C_Signin().setVisible(true);
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Sign-up failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
