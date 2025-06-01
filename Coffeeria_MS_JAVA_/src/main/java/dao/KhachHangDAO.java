package dao;

import model.KhachHang;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (HOTEN, NGAYSINH, SDT, DIEMTICHLUY, LOAITV, MATK) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoten());
            ps.setDate(2, new java.sql.Date(kh.getNgaysinh().getTime()));
            ps.setString(3, kh.getSdt());
            ps.setDouble(4, kh.getDiemtichluy());
            ps.setString(5, kh.getLoaitv());
            ps.setString(6, kh.getMatk());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
                    rs.getString("SDT"),
                    rs.getDouble("DIEMTICHLUY"),
                    rs.getString("LOAITV"),
                    rs.getString("MATK")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KHACHHANG SET HOTEN = ?, NGAYSINH = ?, SDT = ?, DIEMTICHLUY = ?, LOAITV = ?, MATK = ? WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoten());
            ps.setDate(2, new java.sql.Date(kh.getNgaysinh().getTime()));
            ps.setString(3, kh.getSdt());
            ps.setDouble(4, kh.getDiemtichluy());
            ps.setString(5, kh.getLoaitv());
            ps.setString(6, kh.getMatk());
            ps.setString(7, kh.getMakh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
                    rs.getString("SDT"),
                    rs.getDouble("DIEMTICHLUY"),
                    rs.getString("LOAITV"),
                    rs.getString("MATK")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}