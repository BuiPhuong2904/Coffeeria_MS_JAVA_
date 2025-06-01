package model;

import java.util.Date;

public class KhachHang {
    private String makh;
    private String hoten;
    private Date ngaysinh;
    private String sdt;
    private double diemtichluy;
    private String loaitv;
    private String matk;

    public KhachHang() {}

    public KhachHang(String makh, String hoten, Date ngaysinh, String sdt,
                     double diemtichluy, String loaitv, String matk) {
        this.makh = makh;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.diemtichluy = diemtichluy;
        this.loaitv = loaitv;
        this.matk = matk;
    }

    public KhachHang(String hoten, Date ngaysinh, String sdt,
                     double diemtichluy, String loaitv, String matk) {
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.diemtichluy = diemtichluy;
        this.loaitv = loaitv;
        this.matk = matk;
    }

    // Getter & Setter
    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }

    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }

    public Date getNgaysinh() { return ngaysinh; }
    public void setNgaysinh(Date ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public double getDiemtichluy() { return diemtichluy; }
    public void setDiemtichluy(double diemtichluy) { this.diemtichluy = diemtichluy; }

    public String getLoaitv() { return loaitv; }
    public void setLoaitv(String loaitv) { this.loaitv = loaitv; }

    public String getMatk() { return matk; }
    public void setMatk(String matk) { this.matk = matk; }
}
