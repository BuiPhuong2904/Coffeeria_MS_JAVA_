
package dao;

/**
 *
 * @author Bich Phuong
 */
import model.PhieuKho;
import model.CT_PhieuKho;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuKhoDAO {

    public boolean insertPhieuKho(PhieuKho phieuKho, List<CT_PhieuKho> chiTietList) {
        String insertPhieu = "INSERT INTO PHIEUKHO (MAPHIEU, NGAYGIAODICH, LOAIPHIEU, TONGTIEN, MANV, GHICHU) VALUES (?, ?, ?, ?, ?, ?)";
        String insertChiTiet = "INSERT INTO CT_PHIEUKHO (MAPHIEU, MASP, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psPhieu = conn.prepareStatement(insertPhieu);
                 PreparedStatement psCT = conn.prepareStatement(insertChiTiet)) {

                psPhieu.setString(1, phieuKho.getMaPhieu());
                psPhieu.setDate(2, new java.sql.Date(phieuKho.getNgayGiaoDich().getTime()));
                psPhieu.setString(3, phieuKho.getLoaiPhieu());
                psPhieu.setDouble(4, phieuKho.getTongTien());
                psPhieu.setString(5, phieuKho.getMaNV());
                psPhieu.setString(6, phieuKho.getGhiChu());
                psPhieu.executeUpdate();

                for (CT_PhieuKho ct : chiTietList) {
                    psCT.setString(1, ct.getMaPhieu());
                    psCT.setString(2, ct.getMaSP());
                    psCT.setInt(3, ct.getSoLuong());
                    psCT.setDouble(4, ct.getDonGia());
                    psCT.addBatch();
                }
                psCT.executeBatch();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public PhieuKho findPhieuKhoById(String maPhieu) {
        String sql = "SELECT * FROM PHIEUKHO WHERE MAPHIEU = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PhieuKho(
                    rs.getString("MAPHIEU"),
                    rs.getDate("NGAYGIAODICH"),
                    rs.getString("LOAIPHIEU"),
                    rs.getDouble("TONGTIEN"),
                    rs.getString("MANV"),
                    rs.getString("GHICHU")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PhieuKho> findAllPhieuKho() {
        List<PhieuKho> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUKHO";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PhieuKho(
                    rs.getString("MAPHIEU"),
                    rs.getDate("NGAYGIAODICH"),
                    rs.getString("LOAIPHIEU"),
                    rs.getDouble("TONGTIEN"),
                    rs.getString("MANV"),
                    rs.getString("GHICHU")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePhieuKho(PhieuKho phieuKho) {
        String sql = "UPDATE PHIEUKHO SET NGAYGIAODICH = ?, LOAIPHIEU = ?, TONGTIEN = ?, MANV = ?, GHICHU = ? WHERE MAPHIEU = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(phieuKho.getNgayGiaoDich().getTime()));
            ps.setString(2, phieuKho.getLoaiPhieu());
            ps.setDouble(3, phieuKho.getTongTien());
            ps.setString(4, phieuKho.getMaNV());
            ps.setString(5, phieuKho.getGhiChu());
            ps.setString(6, phieuKho.getMaPhieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePhieuKho(String maPhieu) {
        String deleteCT = "DELETE FROM CT_PHIEUKHO WHERE MAPHIEU = ?";
        String deletePhieu = "DELETE FROM PHIEUKHO WHERE MAPHIEU = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psCT = conn.prepareStatement(deleteCT);
                 PreparedStatement psPhieu = conn.prepareStatement(deletePhieu)) {

                psCT.setString(1, maPhieu);
                psCT.executeUpdate();

                psPhieu.setString(1, maPhieu);
                int rows = psPhieu.executeUpdate();

                conn.commit();
                return rows > 0;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CT_PhieuKho> findChiTietByMaPhieu(String maPhieu) {
        List<CT_PhieuKho> list = new ArrayList<>();
        String sql = "SELECT * FROM CT_PHIEUKHO WHERE MAPHIEU = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CT_PhieuKho(
                    rs.getString("MAPHIEU"),
                    rs.getString("MASP"),
                    rs.getInt("SOLUONG"),
                    rs.getDouble("DONGIA")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateChiTiet(CT_PhieuKho ct) {
        String sql = "UPDATE CT_PHIEUKHO SET SOLUONG = ?, DONGIA = ? WHERE MAPHIEU = ? AND MASP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getMaPhieu());
            ps.setString(4, ct.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteChiTiet(String maPhieu, String maSP) {
        String sql = "DELETE FROM CT_PHIEUKHO WHERE MAPHIEU = ? AND MASP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updatePhieuKhoWithChiTiet(PhieuKho phieuKho, List<CT_PhieuKho> chiTietList) {
        String updatePhieu = "UPDATE PHIEUKHO SET NGAYGIAODICH = ?, LOAIPHIEU = ?, TONGTIEN = ?, MANV = ?, GHICHU = ? WHERE MAPHIEU = ?";
        String deleteCT = "DELETE FROM CT_PHIEUKHO WHERE MAPHIEU = ?";
        String insertCT = "INSERT INTO CT_PHIEUKHO (MAPHIEU, MASP, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement psPhieu = conn.prepareStatement(updatePhieu);
                PreparedStatement psDeleteCT = conn.prepareStatement(deleteCT);
                PreparedStatement psInsertCT = conn.prepareStatement(insertCT)
            ) {
                // Cập nhật phiếu
                psPhieu.setDate(1, new java.sql.Date(phieuKho.getNgayGiaoDich().getTime()));
                psPhieu.setString(2, phieuKho.getLoaiPhieu());
                psPhieu.setDouble(3, phieuKho.getTongTien());
                psPhieu.setString(4, phieuKho.getMaNV());
                psPhieu.setString(5, phieuKho.getGhiChu());
                psPhieu.setString(6, phieuKho.getMaPhieu());
                psPhieu.executeUpdate();

                // Xoá chi tiết cũ
                psDeleteCT.setString(1, phieuKho.getMaPhieu());
                psDeleteCT.executeUpdate();

                // Thêm chi tiết mới
                for (CT_PhieuKho ct : chiTietList) {
                    psInsertCT.setString(1, ct.getMaPhieu());
                    psInsertCT.setString(2, ct.getMaSP());
                    psInsertCT.setInt(3, ct.getSoLuong());
                    psInsertCT.setDouble(4, ct.getDonGia());
                    psInsertCT.addBatch();
                }
                psInsertCT.executeBatch();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}