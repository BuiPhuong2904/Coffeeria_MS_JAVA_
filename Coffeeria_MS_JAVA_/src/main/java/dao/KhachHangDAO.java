
package dao;

import model.KhachHang;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author Bich Phuong
 */
public class KhachHangDAO {
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (MAKH, HOTEN, NGAYSINH, EMAIL, SDT) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMakh());
            ps.setString(2, kh.getHoten());
            ps.setDate(3, (Date) kh.getNgaysinh());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSdt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
