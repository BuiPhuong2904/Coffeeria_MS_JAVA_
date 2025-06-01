
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import utils.DBConnection;

/**
 *
 * @author Bich Phuong
 */
public class NhanVienDAO {
    public boolean insertNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NHANVIEN (HOTEN, NGAYSINH, SDT, NGAYVL, CHUCVU, LUONG, MAQL, MATK) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoten());
            
            if (nv.getNgaysinh() != null) {
                ps.setDate(2, new java.sql.Date(nv.getNgaysinh().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setString(3, nv.getSdt());
            
            if (nv.getNgayvl() != null) {
                ps.setDate(4, new java.sql.Date(nv.getNgayvl().getTime()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            ps.setString(5, nv.getChucvu());
            ps.setDouble(6, nv.getLuong());
            ps.setString(7, nv.getMaql());
            ps.setString(8, nv.getMatk());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<NhanVien> findAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setManv(rs.getString("MANV"));
                nv.setHoten(rs.getString("HOTEN"));
                nv.setNgaysinh(rs.getDate("NGAYSINH"));
                nv.setSdt(rs.getString("SDT"));
                nv.setNgayvl(rs.getDate("NGAYVL"));
                nv.setChucvu(rs.getString("CHUCVU"));
                nv.setLuong(rs.getDouble("LUONG"));
                nv.setMaql(rs.getString("MAQL"));
                nv.setMatk(rs.getString("MATK"));

                list.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public NhanVien findById(String manv) {
        String sql = "SELECT * FROM NHANVIEN WHERE MANV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new NhanVien(
                    rs.getString("MANV"),
                    rs.getString("HOTEN"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("SDT"),
                    rs.getDate("NGAYVL"),
                    rs.getString("CHUCVU"),
                    rs.getDouble("LUONG"),
                    rs.getString("MAQL"),
                    rs.getString("MATK")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NHANVIEN SET HOTEN = ?, NGAYSINH = ?, SDT = ?, NGAYVL = ?, CHUCVU = ?, LUONG = ?, MAQL = ?, MATK = ? WHERE MANV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoten());
            ps.setDate(2, new java.sql.Date(nv.getNgaysinh().getTime()));
            ps.setString(3, nv.getSdt());
            ps.setDate(4, new java.sql.Date(nv.getNgayvl().getTime()));
            ps.setString(5, nv.getChucvu());
            ps.setDouble(6, nv.getLuong());
            ps.setString(7, nv.getMaql());
            ps.setString(8, nv.getMatk());
            ps.setString(9, nv.getManv());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String manv) {
        String selectSQL = "SELECT MATK FROM NHANVIEN WHERE MANV = ?";
        String deleteNhanVienSQL = "DELETE FROM NHANVIEN WHERE MANV = ?";
        String deleteTaiKhoanSQL = "DELETE FROM TAIKHOAN WHERE MATK = ?";

        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement deleteNVStmt = null;
        PreparedStatement deleteTKStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // dùng transaction để rollback nếu lỗi

            // Lấy mã tài khoản
            selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setString(1, manv);
            rs = selectStmt.executeQuery();

            String matk = null;
            if (rs.next()) {
                matk = rs.getString("MATK");
            }

            // Xóa nhân viên
            deleteNVStmt = conn.prepareStatement(deleteNhanVienSQL);
            deleteNVStmt.setString(1, manv);
            int rowsAffectedNV = deleteNVStmt.executeUpdate();

            // Xóa tài khoản 
            int rowsAffectedTK = 0;
            if (matk != null && !matk.isEmpty()) {
                deleteTKStmt = conn.prepareStatement(deleteTaiKhoanSQL);
                deleteTKStmt.setString(1, matk);
                rowsAffectedTK = deleteTKStmt.executeUpdate();
            }

            conn.commit(); 
            return rowsAffectedNV > 0;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, selectStmt, rs);
            DBConnection.close(null, deleteNVStmt, null);
            DBConnection.close(null, deleteTKStmt, null);
        }
        return false;
    }

    public String getTenNVByMaNV(String maNV) {
        String tenNV = null;
        String sql = "SELECT HOTEN FROM NHANVIEN WHERE MANV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tenNV = rs.getString("HOTEN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenNV;
    }

}
