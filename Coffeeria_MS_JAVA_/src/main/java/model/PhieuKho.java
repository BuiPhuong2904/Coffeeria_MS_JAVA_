
package model;

/**
 *
 * @author Bich Phuong
 */
public class PhieuKho {
    private String maPhieu;
    private java.sql.Date ngayGiaoDich;
    private String loaiPhieu;
    private double tongTien;
    private String maNV;
    private String ghiChu;

    public PhieuKho() {}

    public PhieuKho(String maPhieu, java.sql.Date ngayGiaoDich, String loaiPhieu, double tongTien, String maNV, String ghiChu) {
        this.maPhieu = maPhieu;
        this.ngayGiaoDich = ngayGiaoDich;
        this.loaiPhieu = loaiPhieu;
        this.tongTien = tongTien;
        this.maNV = maNV;
        this.ghiChu = ghiChu;
    }

    public String getMaPhieu() {
        return maPhieu;
    }
    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public java.sql.Date getNgayGiaoDich() {
        return ngayGiaoDich;
    }
    public void setNgayGiaoDich(java.sql.Date ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public String getLoaiPhieu() {
        return loaiPhieu;
    }
    public void setLoaiPhieu(String loaiPhieu) {
        this.loaiPhieu = loaiPhieu;
    }

    public double getTongTien() {
        return tongTien;
    }
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getGhiChu() {
        return ghiChu;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
