
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
import model.SanPham;
import utils.DBConnection;

public class SanPhamDAO {

    public boolean insertSanPham(SanPham sp) {
        String sql = "INSERT INTO SANPHAM (MASP, TENSP, LOAISP, TONG_SL, DONVITINH, GIANHAP, NGAYSX, HANSD, TRANGTHAI) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getLoaiSP());
            ps.setInt(4, sp.getTongSL());
            ps.setString(5, sp.getDonViTinh());
            ps.setDouble(6, sp.getGiaNhap());
            if (sp.getNgaySX() != null) {
                ps.setDate(7, new java.sql.Date(sp.getNgaySX().getTime()));
            } else {
                ps.setDate(7, null);
            }
            if (sp.getHanSD() != null) {
                ps.setDate(8, new java.sql.Date(sp.getHanSD().getTime()));
            } else {
                ps.setDate(8, null);
            }
            ps.setString(9, sp.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SanPham> findAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("MASP"),
                    rs.getString("TENSP"),
                    rs.getString("LOAISP"),
                    rs.getInt("TONG_SL"),
                    rs.getString("DONVITINH"),
                    rs.getDouble("GIANHAP"),
                    rs.getDate("NGAYSX"),
                    rs.getDate("HANSD"),
                    rs.getString("TRANGTHAI")
                );
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsByMaSP(String maSP) {
        String sql = "SELECT 1 FROM SANPHAM WHERE MASP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSanPham(SanPham sp) {
        String sql = "UPDATE SANPHAM SET TENSP=?, LOAISP=?, TONG_SL=?, DONVITINH=?, GIANHAP=?, NGAYSX=?, HANSD=?, TRANGTHAI=? WHERE MASP=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getLoaiSP());
            ps.setInt(3, sp.getTongSL());
            ps.setString(4, sp.getDonViTinh());
            ps.setDouble(5, sp.getGiaNhap());
            if (sp.getNgaySX() != null) {
                ps.setDate(6, new java.sql.Date(sp.getNgaySX().getTime()));
            } else {
                ps.setDate(6, null);
            }
            if (sp.getHanSD() != null) {
                ps.setDate(7, new java.sql.Date(sp.getHanSD().getTime()));
            } else {
                ps.setDate(7, null);
            }
            ps.setString(8, sp.getTrangThai());
            ps.setString(9, sp.getMaSP());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByMaSP(String maSP) {
        String sql = "DELETE FROM SANPHAM WHERE MASP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public SanPham findById(String maSP) {
        String sql = "SELECT * FROM SANPHAM WHERE MASP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SanPham(
                    rs.getString("MASP"),
                    rs.getString("TENSP"),
                    rs.getString("LOAISP"),
                    rs.getInt("TONG_SL"),
                    rs.getString("DONVITINH"),
                    rs.getDouble("GIANHAP"),
                    rs.getDate("NGAYSX"),
                    rs.getDate("HANSD"),
                    rs.getString("TRANGTHAI")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}