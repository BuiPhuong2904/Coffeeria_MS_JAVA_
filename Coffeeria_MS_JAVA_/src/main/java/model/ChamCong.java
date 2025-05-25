
package model;

import java.util.Date;

/**
 *
 * @author Bich Phuong
 */
public class ChamCong {
    private String maChamCong;
    private Date ngayLV;
    private double soGioLam;
    private String maNV;

    public ChamCong() {
    }

    public ChamCong(String maChamCong, Date ngayLV, double soGioLam, String maNV) {
        this.maChamCong = maChamCong;
        this.ngayLV = ngayLV;
        this.soGioLam = soGioLam;
        this.maNV = maNV;
    }

    public String getMaChamCong() {
        return maChamCong;
    }
    public void setMaChamCong(String maChamCong) {
        this.maChamCong = maChamCong;
    }

    public Date getNgayLV() {
        return ngayLV;
    }
    public void setNgayLV(Date ngayLV) {
        this.ngayLV = ngayLV;
    }

    public double getSoGioLam() {
        return soGioLam;
    }
    public void setSoGioLam(double soGioLam) {
        this.soGioLam = soGioLam;
    }

    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    } 
}
