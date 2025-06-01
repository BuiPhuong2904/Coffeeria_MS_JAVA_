
package controller;

import View.E_menuPanel;
import dao.MonAnDAO;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JOptionPane;
import model.MonAn;

/**
 *
 * @author Bich Phuong
 */
public class MenuController {
    private final Component view;
    private final E_menuPanel menuPanel; 

    public MenuController(Component view, E_menuPanel menuPanel) {
        this.view = view;
        this.menuPanel = menuPanel;
    }

    public void handleInsert(String maMon, String tenMon, String danhMuc, String giaBanStr, String moTa) {
        if (maMon.isEmpty() || tenMon.isEmpty() || giaBanStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đủ các thông tin bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (new MonAnDAO().existsByMaMon(maMon)) {
            JOptionPane.showMessageDialog(view, "Mã món đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double giaBan = Double.parseDouble(giaBanStr);
            MonAn mon = new MonAn(maMon, tenMon, danhMuc, giaBan, moTa);
            boolean success = new MonAnDAO().insertMonAn(mon);
            if (success) {
                JOptionPane.showMessageDialog(view, "Thêm món thành công!");
                if (menuPanel != null) {
                    menuPanel.loadAll();
                }
                ((Window) view).dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm món thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Giá bán phải là số.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleUpdate(String maMon, String tenMon, String danhMuc, String giaStr, String moTa, Runnable afterUpdateCallback) {
        if (tenMon.isEmpty() || giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ tên và giá.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double giaBan = Double.parseDouble(giaStr);
            MonAn updated = new MonAn(maMon, tenMon, danhMuc, giaBan, moTa);
            boolean success = new MonAnDAO().updateMonAn(updated);
            if (success) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                if (afterUpdateCallback != null) afterUpdateCallback.run();
                ((Window) view).dispose(); 
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Giá bán phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleDelete(String maMon, Runnable afterDeleteCallback) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = new MonAnDAO().deleteByMaMon(maMon);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa món thành công!");
                if (afterDeleteCallback != null) {
                    afterDeleteCallback.run();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Xóa món thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}