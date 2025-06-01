package dao;

import model.TaiKhoan;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {

    public TaiKhoan dangNhap(String email, String matKhau) {
        String sql = "SELECT * FROM TAIKHOAN WHERE EMAIL = ? AND MATKHAU = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTaiKhoan(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
        }

        return null;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM TAIKHOAN WHERE EMAIL = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String insertTaiKhoan(TaiKhoan tk) {
        String matk = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO TAIKHOAN (EMAIL, MATKHAU, LOAITK, TRANGTHAI) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, new String[] { "MATK" });
            ps.setString(1, tk.getEmail());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getLoaiTK());
            ps.setString(4, tk.getTrangThai());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    matk = rs.getString(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matk;
    }

    public String getEmailByMatk(String matk) {
        String email = "";
        String sql = "SELECT EMAIL FROM TAIKHOAN WHERE MATK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matk);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("EMAIL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }
    
    private TaiKhoan mapResultSetToTaiKhoan(ResultSet rs) throws SQLException {
        return new TaiKhoan(
            rs.getString("MATK"),
            rs.getString("EMAIL"),
            rs.getString("MATKHAU"),
            rs.getString("LOAITK"),
            rs.getString("TRANGTHAI")
        );
    }
    
    public TaiKhoan findById(String matk) {
        String sql = "SELECT * FROM TAIKHOAN WHERE MATK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matk);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTaiKhoan(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET EMAIL = ?, MATKHAU = ?, LOAITK = ?, TRANGTHAI = ? WHERE MATK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getEmail());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getLoaiTK());
            ps.setString(4, tk.getTrangThai());
            ps.setString(5, tk.getMatk());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String matk) {
        String sql = "DELETE FROM TAIKHOAN WHERE MATK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matk);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TaiKhoan> findAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
