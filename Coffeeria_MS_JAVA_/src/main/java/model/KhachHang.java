
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
    private String email;
    private String sdt;
    private double diemtichluy;
    private String loaitv;

    public KhachHang() {}

    public KhachHang(String makh, String hoten, Date ngaysinh, String email, String sdt,
                     double diemtichluy, String loaitv) {
        this.makh = makh;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.email = email;
        this.sdt = sdt;
        this.diemtichluy = diemtichluy;
        this.loaitv = loaitv;
    }
    
    public KhachHang(String hoten, String email, Date ngaysinh, String sdt, double diemtichluy, 
                     String loaitv) {
        this.hoten = hoten;
        this.email = email;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.diemtichluy = diemtichluy;
        this.loaitv = loaitv;
    }


    // Getter + Setter
    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }

    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }

    public Date getNgaysinh() { return ngaysinh; }
    public void setNgaysinh(Date ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public double getDiemtichluy() { return diemtichluy; }
    public void setDiemtichluy(double diemtichluy) { this.diemtichluy = diemtichluy; }

    public String getLoaitv() { return loaitv; }
    public void setLoaitv(String loaitv) { this.loaitv = loaitv; }
}
