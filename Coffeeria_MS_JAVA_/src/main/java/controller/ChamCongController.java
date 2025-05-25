
package controller;

/**
 *
 * @author Bich Phuong
 */

import View.M_home_T;
import dao.ChamCongDAO;
import model.ChamCong;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChamCongController {
    private final ChamCongDAO chamCongDAO = new ChamCongDAO();
    private final Component view;
    private final M_home_T  chamCongView;

    public ChamCongController(Component view, M_home_T chamCongView) {
        this.view = view;
        this.chamCongView = chamCongView;
    }

    public void insertChamCong(Date ngayLV, String soGioLamStr, String maNV) {
        if (soGioLamStr.isEmpty() || maNV.isEmpty() || ngayLV == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double soGioLam = Double.parseDouble(soGioLamStr);

            ChamCong cc = new ChamCong();
            cc.setNgayLV(ngayLV); 
            cc.setSoGioLam(soGioLam);
            cc.setMaNV(maNV);

            boolean success = chamCongDAO.insertChamCong(cc);
            if (success) {
                JOptionPane.showMessageDialog(view, "Thêm chấm công thành công.");
                chamCongView.loadAll(); 
            } else {
                JOptionPane.showMessageDialog(view, "Thêm chấm công thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Số giờ làm phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateChamCong(String maChamCong, String ngayLVStr, String soGioLamStr, String maNV) {
        if (maChamCong.isEmpty() || ngayLVStr.isEmpty() || soGioLamStr.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date ngayLV = new SimpleDateFormat("dd/MM/yyyy").parse(ngayLVStr);
            double soGioLam = Double.parseDouble(soGioLamStr);

            ChamCong cc = new ChamCong();
            cc.setMaChamCong(maChamCong);
            cc.setNgayLV(ngayLV);
            cc.setSoGioLam(soGioLam);
            cc.setMaNV(maNV);

            boolean success = chamCongDAO.updateChamCong(cc);
            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật chấm công thành công.");
                chamCongView.loadAll(); // Cập nhật bảng
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật chấm công thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Ngày làm việc không hợp lệ, định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Số giờ làm phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa chấm công
    public void deleteChamCong(String maChamCong, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa bản ghi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = chamCongDAO.deleteById(maChamCong);
            if (deleted) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                if (afterDeleteCallback != null) afterDeleteCallback.run();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}