
package controller;

import View.M_employeePanel;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import java.awt.Component;
import java.awt.Window;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import model.NhanVien;
import model.TaiKhoan;

/**
 *
 * @author Bich Phuong
 */
public class NhanVienController {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final Component view;
    private final M_employeePanel employeePanel; 

    public NhanVienController(Component view, M_employeePanel employeePanel) {
        this.view = view;
        this.employeePanel = employeePanel;
    }

    public void insertNhanVienWithAccount(NhanVien nv, String email, String password) {
        if (taiKhoanDAO.existsByEmail(email)) {
            JOptionPane.showMessageDialog(view, "Email đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaiKhoan tk = new TaiKhoan(email, password, "Employee", "Active");
        String matk = taiKhoanDAO.insertTaiKhoan(tk);

        if (matk == null) {
            JOptionPane.showMessageDialog(view, "Tạo tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nv.setMatk(matk);
        boolean ok = nhanVienDAO.insertNhanVien(nv);
        if (ok) {
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công.");
            if (employeePanel != null) {
                employeePanel.loadAll(); // gọi lại loadAll() để cập nhật bảng
            }
            ((Window) view).dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleUpdate(String manv, String hoten, String ngaysinhStr, String sdt, 
                             String ngayvlStr, String chucvu, String luongStr, 
                             String maql, String matk, Runnable afterUpdateCallback) {
        if (hoten.isEmpty() || manv.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Mã và Họ tên.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date ngaysinh = new SimpleDateFormat("dd/MM/yyyy").parse(ngaysinhStr);
            Date ngayvl = new SimpleDateFormat("dd/MM/yyyy").parse(ngayvlStr);

            // Chuyển đổi lương
            luongStr = luongStr.replace(".", "").replace(",", ".");
            double luong = Double.parseDouble(luongStr);

            NhanVien nv = new NhanVien(manv, hoten, ngaysinh, sdt, ngayvl, chucvu, luong, maql, matk);
            boolean success = new NhanVienDAO().updateNhanVien(nv);

            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật nhân viên thành công!");
                if (afterUpdateCallback != null) afterUpdateCallback.run();
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Ngày tháng không hợp lệ, định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Lương phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleDelete(String manv, Runnable callback) {
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Bạn có chắc chắn muốn xóa nhân viên này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = new NhanVienDAO().deleteById(manv);
            if (deleted) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                callback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}