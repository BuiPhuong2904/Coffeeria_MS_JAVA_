
package model;

/**
 *
 * @author Bich Phuong
 */
import java.util.Date;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private String loaiKM;
    private double giaTriGiam;
    private String dieuKien;
    private Date ngayBD;
    private Date ngayKT;
    private String trangThai;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, String tenKM, String loaiKM, double giaTriGiam,
                     String dieuKien, Date ngayBD, Date ngayKT, String trangThai) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.loaiKM = loaiKM;
        this.giaTriGiam = giaTriGiam;
        this.dieuKien = dieuKien;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public String getLoaiKM() { return loaiKM; }
    public void setLoaiKM(String loaiKM) { this.loaiKM = loaiKM; }

    public double getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(double giaTriGiam) { this.giaTriGiam = giaTriGiam; }

    public String getDieuKien() { return dieuKien; }
    public void setDieuKien(String dieuKien) { this.dieuKien = dieuKien; }

    public Date getNgayBD() { return ngayBD; }
    public void setNgayBD(Date ngayBD) { this.ngayBD = ngayBD; }

    public Date getNgayKT() { return ngayKT; }
    public void setNgayKT(Date ngayKT) { this.ngayKT = ngayKT; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}