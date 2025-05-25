
package dao;

/**
 *
 * @author Bich Phuong
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhuyenMai;
import utils.DBConnection;

public class KhuyenMaiDAO {

    public boolean insertKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KHUYENMAI (MAKM, TENKM, LOAIKM, GIATRIGIAM, DIEUKIEN, NGAYBD, NGAYKT, TRANGTHAI) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setString(3, km.getLoaiKM());
            ps.setDouble(4, km.getGiaTriGiam());
            ps.setString(5, km.getDieuKien());
            ps.setDate(6, new java.sql.Date(km.getNgayBD().getTime()));
            ps.setDate(7, new java.sql.Date(km.getNgayKT().getTime()));
            ps.setString(8, km.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<KhuyenMai> findAll() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KHUYENMAI";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                    rs.getString("MAKM"),
                    rs.getString("TENKM"),
                    rs.getString("LOAIKM"),
                    rs.getDouble("GIATRIGIAM"),
                    rs.getString("DIEUKIEN"),
                    rs.getDate("NGAYBD"),
                    rs.getDate("NGAYKT"),
                    rs.getString("TRANGTHAI")
                );
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public KhuyenMai findById(String maKM) {
        String sql = "SELECT * FROM KHUYENMAI WHERE MAKM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                    rs.getString("MAKM"),
                    rs.getString("TENKM"),
                    rs.getString("LOAIKM"),
                    rs.getDouble("GIATRIGIAM"),
                    rs.getString("DIEUKIEN"),
                    rs.getDate("NGAYBD"),
                    rs.getDate("NGAYKT"),
                    rs.getString("TRANGTHAI")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KHUYENMAI SET TENKM=?, LOAIKM=?, GIATRIGIAM=?, DIEUKIEN=?, NGAYBD=?, NGAYKT=?, TRANGTHAI=? WHERE MAKM=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getLoaiKM());
            ps.setDouble(3, km.getGiaTriGiam());
            ps.setString(4, km.getDieuKien());
            ps.setDate(5, new java.sql.Date(km.getNgayBD().getTime()));
            ps.setDate(6, new java.sql.Date(km.getNgayKT().getTime()));
            ps.setString(7, km.getTrangThai());
            ps.setString(8, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByMaKM(String maKM) {
        String sql = "DELETE FROM KHUYENMAI WHERE MAKM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByMaKM(String maKM) {
        String sql = "SELECT 1 FROM KHUYENMAI WHERE MAKM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}