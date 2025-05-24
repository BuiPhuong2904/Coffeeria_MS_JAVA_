
package model;

/**
 *
 * @author Bich Phuong
 */
import java.util.Date;

public class NhanVien {
    private String manv;
    private String hoten;
    private Date ngaysinh;
    private String sdt;
    private Date ngayvl;
    private String chucvu;
    private double luong;
    private String maql;
    private String matk;

    public NhanVien() {}

    public NhanVien(String manv, String hoten, Date ngaysinh, String sdt,
                    Date ngayvl, String chucvu, double luong, String maql, String matk) {
        this.manv = manv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.ngayvl = ngayvl;
        this.chucvu = chucvu;
        this.luong = luong;
        this.maql = maql;
        this.matk = matk;
    }
    
        public NhanVien(String hoten, Date ngaysinh, String sdt,
                        Date ngayvl, String chucvu, double luong, String maql, String matk) {
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.ngayvl = ngayvl;
        this.chucvu = chucvu;
        this.luong = luong;
        this.maql = maql;
        this.matk = matk;
    }
    
    // Getter + Setter
    public String getManv() { return manv; }
    public void setManv(String manv) { this.manv = manv; }

    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten;}

    public Date getNgaysinh() { return ngaysinh; }
    public void setNgaysinh(Date ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public Date getNgayvl() { return ngayvl; }
    public void setNgayvl(Date ngayvl) { this.ngayvl = ngayvl; }

    public String getChucvu() {return chucvu; }
    public void setChucvu(String chucvu) { this.chucvu = chucvu; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getMaql() { return maql; }
    public void setMaql(String maql) { this.maql = maql; }

    public String getMatk() { return matk; }
    public void setMatk(String matk) { this.matk = matk; }
}