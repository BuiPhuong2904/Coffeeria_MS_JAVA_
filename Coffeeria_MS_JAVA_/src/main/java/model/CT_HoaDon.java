
package model;

/**
 *
 * @author Bich Phuong
 */
public class CT_HoaDon {
    private String mahd; 
    private String mamon; 
    private Double donGia;
    private Integer soLuong;

    public CT_HoaDon() {
    }

    public CT_HoaDon(String mahd, String mamon, Double donGia, Integer soLuong) {
        this.mahd = mahd;
        this.mamon = mamon;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public String getMaHD() {
        return mahd;
    }
    public void setMaHD(String mahd) {
        this.mahd = mahd;
    }

    public String getMaMon() {
        return mamon;
    }
    public void setMaMon(String mamon) {
        this.mamon = mamon;
    }

    public Double getDonGia() {
        return donGia;
    }
    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public Integer getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }
}
