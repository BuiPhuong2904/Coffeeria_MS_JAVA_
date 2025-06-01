
package dao;

/**
 *
 * @author Bich Phuong
 */

import model.ChamCong;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date; 

public class ChamCongDAO {

    public boolean insertChamCong(ChamCong chamCong) {
        String sql = "INSERT INTO CHAMCONG (NGAYLV, SOGIOLAM, MANV) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            java.sql.Date sqlDate = new java.sql.Date(chamCong.getNgayLV().getTime());
            ps.setDate(1, sqlDate);

            ps.setDouble(2, chamCong.getSoGioLam());
            ps.setString(3, chamCong.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ChamCong findById(String maChamCong) {
        String sql = "SELECT * FROM CHAMCONG WHERE MACHAMCONG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChamCong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ChamCong(
                    rs.getString("MACHAMCONG"),
                    rs.getDate("NGAYLV"),
                    rs.getDouble("SOGIOLAM"),
                    rs.getString("MANV")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateChamCong(ChamCong chamCong) {
        String sql = "UPDATE CHAMCONG SET NGAYLV = ?, SOGIOLAM = ?, MANV = ? WHERE MACHAMCONG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            java.util.Date utilDate = chamCong.getNgayLV();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            ps.setDate(1, sqlDate);

            ps.setDouble(2, chamCong.getSoGioLam());
            ps.setString(3, chamCong.getMaNV());
            ps.setString(4, chamCong.getMaChamCong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String maChamCong) {
        String sql = "DELETE FROM CHAMCONG WHERE MACHAMCONG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChamCong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ChamCong> findAll() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT * FROM CHAMCONG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ChamCong chamCong = new ChamCong(
                    rs.getString("MACHAMCONG"),
                    rs.getDate("NGAYLV"),
                    rs.getDouble("SOGIOLAM"),
                    rs.getString("MANV")
                );
                list.add(chamCong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}