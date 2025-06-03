
package model;

import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Bich Phuong
 */
public class MonAn {
    private String maMon;
    private String tenMon;
    private String danhMuc;
    private double giaBan;
    private String moTa;
    
    public MonAn() {}

    public MonAn(String maMon, String tenMon, String danhMuc, double giaBan, String moTa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.danhMuc = danhMuc;
        this.giaBan = giaBan;
        this.moTa = moTa;
    }

    // Getter và Setter 
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    
    public ImageIcon getImageIcon() {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        for (String ext : extensions) {
            String path = "/drink/" + maMon + ext; 
            URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                return new ImageIcon(imgUrl);
            }
        }
        return null;
    }

}
