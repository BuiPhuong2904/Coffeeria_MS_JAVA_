
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.NhanVien;
import utils.DBConnection;

/**
 *
 * @author Bich Phuong
 */
public class NhanVienDAO {
    public boolean insertNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NHANVIEN (HOTEN, NGAYSINH, GIOITINH, SDT, DIACHI, NGAYVL, CHUCVU, LUONG, MAQL, MATK) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoten());
            
            if (nv.getNgaysinh() != null) {
                ps.setDate(2, new java.sql.Date(nv.getNgaysinh().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setString(3, nv.getGioitinh());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getDiachi());
            
            if (nv.getNgayvl() != null) {
                ps.setDate(6, new java.sql.Date(nv.getNgayvl().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            ps.setString(7, nv.getChucvu());
            ps.setDouble(8, nv.getLuong());
            ps.setString(9, nv.getMaql());
            ps.setString(10, nv.getMatk());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
