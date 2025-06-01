package controller;

import View.E_customerPanel;
import dao.KhachHangDAO;
import java.awt.Component;
import java.awt.Window;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import model.KhachHang;

/**
 *
 * @author Bich Phuong
 */
public class KhachHangController {
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final Component view;
    private final E_customerPanel khachHangPanel;

    public KhachHangController(Component view, E_customerPanel khachHangPanel) {
        this.view = view;
        this.khachHangPanel = khachHangPanel;
    }

    public void insertKhachHang(String hoten, String ngaysinhStr, String sdt, 
                                String diemtichluyStr, String loaitv, String matk) {
        if (hoten.isEmpty() || ngaysinhStr.isEmpty() || sdt.isEmpty() || matk.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date ngaysinh = new SimpleDateFormat("dd/MM/yyyy").parse(ngaysinhStr);
            double diemtichluy = diemtichluyStr.isEmpty() ? 0 : Double.parseDouble(diemtichluyStr);

            KhachHang kh = new KhachHang();
            kh.setHoten(hoten);
            kh.setNgaysinh(ngaysinh);
            kh.setSdt(sdt);
            kh.setDiemtichluy(diemtichluy);
            kh.setLoaitv(loaitv);
            kh.setMatk(matk);

            boolean ok = khachHangDAO.insertKhachHang(kh);
            if (ok) {
                JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công.");
                if (khachHangPanel != null) {
                    khachHangPanel.loadAll();
                }
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm khách hàng thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Ngày sinh không hợp lệ, định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Điểm tích lũy phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleUpdate(String makh, String hoten, String ngaysinhStr, String sdt,
                             String diemtichluyStr, String loaitv, String matk, Runnable afterUpdateCallback) {
        if (makh.isEmpty() || hoten.isEmpty() || matk.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Mã, Họ tên và Mã tài khoản.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date ngaysinh = new SimpleDateFormat("dd/MM/yyyy").parse(ngaysinhStr);
            double diemtichluy = diemtichluyStr.isEmpty() ? 0 : Double.parseDouble(diemtichluyStr);

            KhachHang kh = new KhachHang();
            kh.setMakh(makh);
            kh.setHoten(hoten);
            kh.setNgaysinh(ngaysinh);
            kh.setSdt(sdt);
            kh.setDiemtichluy(diemtichluy);
            kh.setLoaitv(loaitv);
            kh.setMatk(matk);

            boolean success = khachHangDAO.updateKhachHang(kh);
            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thành công!");
                if (afterUpdateCallback != null) afterUpdateCallback.run();
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Ngày sinh không hợp lệ, định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Điểm tích lũy phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleDelete(String makh, Runnable callback) {
        int confirm = JOptionPane.showConfirmDialog(view,
            "Bạn có chắc chắn muốn xóa khách hàng này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = khachHangDAO.deleteById(makh);
            if (deleted) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                if (callback != null) callback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}