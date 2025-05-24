
package model;

/**
 *
 * @author Bich Phuong
 */
import java.util.Date;

public class KhachHang {
    private String makh;
    private String hoten;
    private Date ngaysinh;
    private String gioitinh;
    private String sdt;
    private double diemtichluy;
    private String loaitv;
    private String matk;

    public KhachHang() {}

    public KhachHang(String makh, String hoten, Date ngaysinh, String gioitinh, String sdt,
                     double diemtichluy, String loaitv, String matk) {
        this.makh = makh;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.diemtichluy = 0;
        this.loaitv = loaitv;
        this.matk = matk;
    }
    
    public KhachHang(String hoten, String gioitinh, Date ngaysinh, String sdt, double diemtichluy, 
                     String loaitv, String matk) {
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.diemtichluy = 0;
        this.loaitv = loaitv;
        this.matk = matk;
    }


    // Getter + Setter
    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }

    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }

    public Date getNgaysinh() { return ngaysinh; }
    public void setNgaysinh(Date ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getGioitinh() { return gioitinh; }
    public void setGioitinh(String gioitinh) { this.gioitinh = gioitinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public double getDiemtichluy() { return diemtichluy; }
    public void setDiemtichluy(double diemtichluy) { this.diemtichluy = diemtichluy; }

    public String getLoaitv() { return loaitv; }
    public void setLoaitv(String loaitv) { this.loaitv = loaitv; }

    public String getMatk() { return matk; }
    public void setMatk(String matk) { this.matk = matk; }
}
