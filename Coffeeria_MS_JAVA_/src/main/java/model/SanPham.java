
package model;

/**
 *
 * @author Bich Phuong
 */
import java.util.Date;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String loaiSP;
    private int tongSL;
    private String donViTinh;
    private String trangThai;
    private double giaNhap;
    private Date ngaySX;
    private Date hanSD;

    public SanPham() {}

    public SanPham(String maSP, String tenSP, String loaiSP, int tongSL, String donViTinh,
                    double giaNhap, Date ngaySX, Date hanSD, String trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.tongSL = tongSL;
        this.donViTinh = donViTinh;
        this.trangThai = trangThai;
        this.giaNhap = giaNhap;
        this.ngaySX = ngaySX;
        this.hanSD = hanSD;
    }

    // Getter & Setter
    public String getMaSP() { return maSP;
    }
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }
    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }
    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public int getTongSL() {
        return tongSL;
    }
    public void setTongSL(int tongSL) {
        this.tongSL = tongSL;
    }

    public String getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getGiaNhap() {
        return giaNhap;
    }
    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public Date getNgaySX() {
        return ngaySX;
    }
    public void setNgaySX(Date ngaySX) {
        this.ngaySX = ngaySX;
    }

    public Date getHanSD() {
        return hanSD;
    }
    public void setHanSD(Date hanSD) {
        this.hanSD = hanSD;
    }

}