
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.CT_HoaDon;
import model.HoaDon;
import utils.DBConnection;

/**
 *
 * @author Bich Phuong
 */
public class HoaDonDAO {

    public boolean insertHoaDon(HoaDon hoaDon, List<CT_HoaDon> chiTietList) {
        String insertHoaDon = "INSERT INTO HOADON (TONGTIENTRUOC, TIENGIAMGIA, TONGTIENSAU, HINHTHUCTT, NGAYLAP, GHICHU, MAKH, MANV, MAKM) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertChiTiet = "INSERT INTO CHITIET_HD (MAHD, MAMON, DONGIA, SOLUONG) VALUES (?, ?, ?, ?)";

        String selectMaHD = "SELECT MAHD FROM HOADON WHERE MAKH = ? AND NGAYLAP = (SELECT MAX(NGAYLAP) FROM HOADON WHERE MAKH = ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            if (!existsInTable(conn, "KHACHHANG", "MAKH", hoaDon.getMaKH())) {
                throw new SQLException("Khách hàng không tồn tại: " + hoaDon.getMaKH());
            }
            if (!existsInTable(conn, "NHANVIEN", "MANV", hoaDon.getMaNV())) {
                throw new SQLException("Nhân viên không tồn tại: " + hoaDon.getMaNV());
            }
            if (hoaDon.getMaKM() != null && !hoaDon.getMaKM().trim().isEmpty() &&
                !existsInTable(conn, "KHUYENMAI", "MAKM", hoaDon.getMaKM())) {
                throw new SQLException("Khuyến mãi không tồn tại: " + hoaDon.getMaKM());
            }

            try (PreparedStatement psHD = conn.prepareStatement(insertHoaDon);
                 PreparedStatement psSelect = conn.prepareStatement(selectMaHD);
                 PreparedStatement psCT = conn.prepareStatement(insertChiTiet)) {

                psHD.setDouble(1, hoaDon.getTongTienTruoc());
                psHD.setDouble(2, hoaDon.getTienGiamGia());
                psHD.setDouble(3, hoaDon.getTongTienSau());
                psHD.setString(4, hoaDon.getHinhThucTT());
                psHD.setDate(5, new java.sql.Date(hoaDon.getNgayLap().getTime()));

                if (hoaDon.getGhiChu() == null || hoaDon.getGhiChu().trim().isEmpty()) {
                    psHD.setNull(6, java.sql.Types.VARCHAR);
                } else {
                    psHD.setString(6, hoaDon.getGhiChu());
                }

                psHD.setString(7, hoaDon.getMaKH());
                psHD.setString(8, hoaDon.getMaNV());

                if (hoaDon.getMaKM() == null || hoaDon.getMaKM().trim().isEmpty()) {
                    psHD.setNull(9, java.sql.Types.VARCHAR);
                } else {
                    psHD.setString(9, hoaDon.getMaKM());
                }

                psHD.executeUpdate();

                // Lấy mã hóa đơn mới tạo
                psSelect.setString(1, hoaDon.getMaKH());
                psSelect.setString(2, hoaDon.getMaKH());

                String maHD;
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        maHD = rs.getString("MAHD");
                    } else {
                        throw new SQLException("Không lấy được mã hóa đơn mới tạo.");
                    }
                }

                for (CT_HoaDon ct : chiTietList) {

                    if (!existsInTable(conn, "MONAN", "MAMON", ct.getMaMon())) {
                        throw new SQLException("Món ăn không tồn tại: " + ct.getMaMon());
                    }

                    psCT.setString(1, maHD);
                    psCT.setString(2, ct.getMaMon());
                    psCT.setDouble(3, ct.getDonGia());
                    psCT.setInt(4, ct.getSoLuong());
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

    private boolean existsInTable(Connection conn, String tableName, String columnName, String value) throws SQLException {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Tìm hóa đơn theo mã
    public HoaDon findHoaDonById(String maHD) {
        String sql = "SELECT * FROM HOADON WHERE MAHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HoaDon(
                    rs.getString("MAHD"),
                    rs.getDouble("TONGTIENTRUOC"),
                    rs.getDouble("TIENGIAMGIA"),
                    rs.getDouble("TONGTIENSAU"),
                    rs.getString("HINHTHUCTT"),
                    rs.getDate("NGAYLAP"),
                    rs.getString("GHICHU"),
                    rs.getString("MAKH"),
                    rs.getString("MANV"),
                    rs.getString("MAKM")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả hóa đơn
    public List<HoaDon> findAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HOADON";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new HoaDon(
                    rs.getString("MAHD"),
                    rs.getDouble("TONGTIENTRUOC"),
                    rs.getDouble("TIENGIAMGIA"),
                    rs.getDouble("TONGTIENSAU"),
                    rs.getString("HINHTHUCTT"),
                    rs.getDate("NGAYLAP"),
                    rs.getString("GHICHU"),
                    rs.getString("MAKH"),
                    rs.getString("MANV"),
                    rs.getString("MAKM")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật hóa đơn (không update chi tiết)
    public boolean updateHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HOADON SET TONGTIENTRUOC = ?, TIENGIAMGIA = ?, TONGTIENSAU = ?, HINHTHUCTT = ?, NGAYLAP = ?, GHICHU = ?, MAKH = ?, MANV = ?, MAKM = ? WHERE MAHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, hoaDon.getTongTienTruoc());
            ps.setDouble(2, hoaDon.getTienGiamGia());
            ps.setDouble(3, hoaDon.getTongTienSau());
            ps.setString(4, hoaDon.getHinhThucTT());
            ps.setDate(5, new java.sql.Date(hoaDon.getNgayLap().getTime()));
            ps.setString(6, hoaDon.getGhiChu());
            ps.setString(7, hoaDon.getMaKH());
            ps.setString(8, hoaDon.getMaNV());
            ps.setString(9, hoaDon.getMaKM());
            ps.setString(10, hoaDon.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa hóa đơn và chi tiết hóa đơn (transaction)
    public boolean deleteHoaDon(String maHD) {
        String deleteCT = "DELETE FROM CHITIET_HD WHERE MAHD = ?";
        String deleteHD = "DELETE FROM HOADON WHERE MAHD = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psCT = conn.prepareStatement(deleteCT);
                 PreparedStatement psHD = conn.prepareStatement(deleteHD)) {

                psCT.setString(1, maHD);
                psCT.executeUpdate();

                psHD.setString(1, maHD);
                int rows = psHD.executeUpdate();

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

    // Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
    public List<CT_HoaDon> findChiTietByMaHD(String maHD) {
        List<CT_HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIET_HD WHERE MAHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CT_HoaDon(
                    rs.getString("MAHD"),
                    rs.getString("MAMON"),
                    rs.getDouble("DONGIA"),
                    rs.getInt("SOLUONG")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật chi tiết hóa đơn
    public boolean updateChiTiet(CT_HoaDon ct) {
        String sql = "UPDATE CHITIET_HD SET SOLUONG = ?, DONGIA = ? WHERE MAHD = ? AND MAMON = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getMaHD());
            ps.setString(4, ct.getMaMon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa chi tiết hóa đơn theo mã hóa đơn và mã món
    public boolean deleteChiTiet(String maHD, String maMon) {
        String sql = "DELETE FROM CHITIET_HD WHERE MAHD = ? AND MAMON = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật hóa đơn và chi tiết hóa đơn cùng lúc (update chi tiết bằng cách xóa hết rồi insert lại)
    public boolean updateHoaDonWithChiTiet(HoaDon hoaDon, List<CT_HoaDon> chiTietList) {
        String updateHoaDon = "UPDATE HOADON SET TONGTIENTRUOC = ?, TIENGIAMGIA = ?, TONGTIENSAU = ?, HINHTHUCTT = ?, NGAYLAP = ?, GHICHU = ?, MAKH = ?, MANV = ?, MAKM = ? WHERE MAHD = ?";
        String deleteCT = "DELETE FROM CHITIET_HD WHERE MAHD = ?";
        String insertCT = "INSERT INTO CHITIET_HD (MAHD, MAMON, DONGIA, SOLUONG) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psHD = conn.prepareStatement(updateHoaDon);
                 PreparedStatement psDeleteCT = conn.prepareStatement(deleteCT);
                 PreparedStatement psInsertCT = conn.prepareStatement(insertCT)) {

                // Cập nhật hóa đơn
                psHD.setDouble(1, hoaDon.getTongTienTruoc());
                psHD.setDouble(2, hoaDon.getTienGiamGia());
                psHD.setDouble(3, hoaDon.getTongTienSau());
                psHD.setString(4, hoaDon.getHinhThucTT());
                psHD.setDate(5, new java.sql.Date(hoaDon.getNgayLap().getTime()));
                psHD.setString(6, hoaDon.getGhiChu());
                psHD.setString(7, hoaDon.getMaKH());
                psHD.setString(8, hoaDon.getMaNV());
                psHD.setString(9, hoaDon.getMaKM());
                psHD.setString(10, hoaDon.getMaHD());
                psHD.executeUpdate();

                // Xóa chi tiết cũ
                psDeleteCT.setString(1, hoaDon.getMaHD());
                psDeleteCT.executeUpdate();

                // Thêm chi tiết mới
                for (CT_HoaDon ct : chiTietList) {
                    psInsertCT.setString(1, ct.getMaHD());
                    psInsertCT.setString(2, ct.getMaMon());
                    psInsertCT.setDouble(3, ct.getDonGia());
                    psInsertCT.setInt(4, ct.getSoLuong());
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
    
    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> doanhThuMap = new LinkedHashMap<>();
        String sql = "SELECT TO_CHAR(NGAYLAP, 'MM-YYYY') AS THANG_NAM, " +
                     "SUM(TONGTIENSAU) AS DOANHTHU " +
                     "FROM HOADON " +
                     "GROUP BY TO_CHAR(NGAYLAP, 'MM-YYYY') " +
                     "ORDER BY TO_DATE(TO_CHAR(NGAYLAP, 'MM-YYYY'), 'MM-YYYY')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String thangNam = rs.getString("THANG_NAM");
                double doanhThu = rs.getDouble("DOANHTHU");
                doanhThuMap.put(thangNam, doanhThu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doanhThuMap;
    }

}
