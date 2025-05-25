
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MonAn;
import utils.DBConnection;

/**
 *
 * @author Bich Phuong
 */
public class MonAnDAO {

    public boolean insertMonAn(MonAn mon) {
        String sql = "INSERT INTO MONAN (MAMON, TENMON, DANHMUC, GIABAN, HINHANH, MOTA) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMon());
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getDanhMuc());
            ps.setDouble(4, mon.getGiaBan());
            ps.setString(5, mon.getHinhAnh());
            ps.setString(6, mon.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<MonAn> findAll() {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT * FROM MONAN";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MonAn m = new MonAn(
                    rs.getString("MAMON"),
                    rs.getString("TENMON"),
                    rs.getString("DANHMUC"),
                    rs.getDouble("GIABAN"),
                    rs.getString("HINHANH"),
                    rs.getString("MOTA")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm các hàm update, delete, search nếu cần

    public boolean existsByMaMon(String maMon) {
        String sql = "SELECT 1 FROM MONAN WHERE MAMON = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMonAn(MonAn mon) {
        String sql = "UPDATE MONAN SET TENMON=?, DANHMUC=?, GIABAN=?, HINHANH=?, MOTA=? WHERE MAMON=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setString(2, mon.getDanhMuc());
            ps.setDouble(3, mon.getGiaBan());
            ps.setString(4, mon.getHinhAnh());
            ps.setString(5, mon.getMoTa());
            ps.setString(6, mon.getMaMon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteByMaMon(String maMon) {
        String sql = "DELETE FROM MONAN WHERE MAMON = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MonAn findById(String maMon) {
        String sql = "SELECT * FROM MONAN WHERE MAMON = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MonAn(
                    rs.getString("MAMON"),
                    rs.getString("TENMON"),
                    rs.getString("DANHMUC"),
                    rs.getDouble("GIABAN"),
                    rs.getString("HINHANH"),
                    rs.getString("MOTA")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}