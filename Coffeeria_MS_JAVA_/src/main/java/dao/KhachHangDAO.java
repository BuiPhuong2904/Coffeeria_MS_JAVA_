
package dao;

import model.KhachHang;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Bich Phuong
 */

public class KhachHangDAO {

    // Thêm khách hàng (insert)
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (HOTEN, NGAYSINH, EMAIL, SDT, DIEMTICHLUY, LOAITV) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoten());
            ps.setDate(2, new java.sql.Date(kh.getNgaysinh().getTime()));
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getSdt());
            ps.setDouble(5, kh.getDiemtichluy());
            ps.setString(6, kh.getLoaitv());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy khách hàng theo ID
    public KhachHang findById(String makh) {
        String sql = "SELECT * FROM KHACHHANG WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, makh);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                    rs.getString("MAKH"),
                    rs.getString("HOTEN"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("EMAIL"),
                    rs.getString("SDT"),
                    rs.getDouble("DIEMTICHLUY"),
                    rs.getString("LOAITV")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật khách hàng
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KHACHHANG SET HOTEN = ?, NGAYSINH = ?, EMAIL = ?, SDT = ?, DIEMTICHLUY = ?, LOAITV = ? WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoten());
            ps.setDate(2, new java.sql.Date(kh.getNgaysinh().getTime()));
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getSdt());
            ps.setDouble(5, kh.getDiemtichluy());
            ps.setString(6, kh.getLoaitv());
            ps.setString(7, kh.getMakh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa khách hàng theo ID
    public boolean deleteById(String makh) {
        String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, makh);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả khách hàng
    public List<KhachHang> findAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("MAKH"),
                    rs.getString("HOTEN"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("EMAIL"),
                    rs.getString("SDT"),
                    rs.getDouble("DIEMTICHLUY"),
                    rs.getString("LOAITV")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
