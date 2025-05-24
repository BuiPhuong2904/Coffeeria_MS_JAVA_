
package model;

/**
 *
 * @author Bich Phuong
 */
public class TaiKhoan {
    private String maTK;
    private String email;
    private String matKhau;
    private String loaiTK;
    private String trangThai;

    public TaiKhoan() {}

    public TaiKhoan(String maTK, String email, String matKhau, String loaiTK, String trangThai) {
        this.maTK = maTK;
        this.email = email;
        this.matKhau = matKhau;
        this.loaiTK = loaiTK;
        this.trangThai = trangThai;
    }
    
    public TaiKhoan(String email, String matKhau, String loaiTK, String trangThai) {
        this.email = email;
        this.matKhau = matKhau;
        this.loaiTK = loaiTK;
        this.trangThai = trangThai;
    }


    // Getter + Setter
    public String getMaTK() { return maTK; }
    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getLoaiTK() { return loaiTK; }
    public void setLoaiTK(String loaiTK) { this.loaiTK = loaiTK; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}