
package model;

/**
 *
 * @author Bich Phuong
 */
public class MonAn {
    private String maMon;
    private String tenMon;
    private String danhMuc;
    private double giaBan;
    private String hinhAnh;
    private String moTa;
    
    public MonAn() {}

    public MonAn(String maMon, String tenMon, String danhMuc, double giaBan, String hinhAnh, String moTa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.danhMuc = danhMuc;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
    }

    // Getter và Setter 
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
