
package model;

/**
 *
 * @author Bich Phuong
 */
public class CT_PhieuKho {
    private String maPhieu;
    private String maSP;
    private int soLuong;
    private double donGia;

    public CT_PhieuKho() {}

    public CT_PhieuKho(String maPhieu, String maSP, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPhieu() {
        return maPhieu;
    }
    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaSP() {
        return maSP;
    }
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }
    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}