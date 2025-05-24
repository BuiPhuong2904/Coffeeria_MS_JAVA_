
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
    private String gioitinh;
    private String sdt;
    private String diachi;
    private Date ngayvl;
    private String chucvu;
    private double luong;
    private String maql;
    private String matk;

    public NhanVien() {}

    public NhanVien(String manv, String hoten, Date ngaysinh, String gioitinh, String sdt,
                    String diachi, Date ngayvl, String chucvu, double luong, String maql, String matk) {
        this.manv = manv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ngayvl = ngayvl;
        this.chucvu = chucvu;
        this.luong = luong;
        this.maql = maql;
        this.matk = matk;
    }
    
        public NhanVien(String hoten, Date ngaysinh, String gioitinh, String sdt,
                    String diachi, Date ngayvl, String chucvu, double luong, String maql, String matk) {
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.diachi = diachi;
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

    public String getGioitinh() { return gioitinh; }
    public void setGioitinh(String gioitinh) { this.gioitinh = gioitinh;}

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiachi() { return diachi; }
    public void setDiachi(String diachi) { this.diachi = diachi; }

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