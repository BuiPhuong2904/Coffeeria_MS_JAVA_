
package model;

/**
 *
 * @author Bich Phuong
 */
import java.util.Date;

public class HoaDon {
    private String mahd;
    private Double tongTienTruoc;
    private Double tienGiamGia;
    private Double tongTienSau;
    private String hinhThucTT;
    private Date ngayLap;
    private String ghiChu;
    private String makh;  
    private String manv; 
    private String makm;  

    public HoaDon() {}

    public HoaDon(String mahd, Double tongTienTruoc, Double tienGiamGia, Double tongTienSau,
                  String hinhThucTT, Date ngayLap, String ghiChu, String makh, String manv, String makm) {
        this.mahd = mahd;
        this.tongTienTruoc = tongTienTruoc;
        this.tienGiamGia = tienGiamGia;
        this.tongTienSau = tongTienSau;
        this.hinhThucTT = hinhThucTT;
        this.ngayLap = ngayLap;
        this.ghiChu = ghiChu;
        this.makh = makh;
        this.manv = manv;
        this.makm = makm;
    }

    public String getMaHD() {
        return mahd;
    }
    public void setMaHD(String mahd) {
        this.mahd = mahd;
    }

    public Double getTongTienTruoc() {
        return tongTienTruoc;
    }
    public void setTongTienTruoc(Double tongTienTruoc) {
        this.tongTienTruoc = tongTienTruoc;
    }

    public Double getTienGiamGia() {
        return tienGiamGia;
    }
    public void setTienGiamGia(Double tienGiamGia) {
        this.tienGiamGia = tienGiamGia;
    }

    public Double getTongTienSau() {
        return tongTienSau;
    }
    public void setTongTienSau(Double tongTienSau) {
        this.tongTienSau = tongTienSau;
    }

    public String getHinhThucTT() {
        return hinhThucTT;
    }
    public void setHinhThucTT(String hinhThucTT) {
        this.hinhThucTT = hinhThucTT;
    }

    public Date getNgayLap() {
        return ngayLap;
    }
    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getGhiChu() {
        return ghiChu;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaKH() {
        return makh;
    }
    public void setMaKH(String makh) {
        this.makh = makh;
    }

    public String getMaNV() {
        return manv;
    }
    public void setMaNV(String manv) {
        this.manv = manv;
    }

    public String getMaKM() {
        return makm;
    }
    public void setMaKM(String makm) {
        this.makm = makm;
    }
}
