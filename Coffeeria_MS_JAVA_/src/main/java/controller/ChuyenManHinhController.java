
package controller;

//import com.mycompany.java_coffeeria_ms.controller.*;
import View.E_customerPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import View.E_homePanel;
import View.E_inventoryPanel;
import View.E_menuPanel;
import View.E_billPanel;
import View.E_voucherPanel;
import View.M_employeePanel;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import model.TaiKhoan;
/**
 *
 * @author nttma
 */
public class ChuyenManHinhController {
    private JPanel root;
    private String kindSelected = "";
    
    private List<DanhMucBean> listItem = null;
    
    private TaiKhoan taiKhoan;

    public void setTaiKhoan(TaiKhoan tk) {
        this.taiKhoan = tk;
    }

    public ChuyenManHinhController(JPanel jpnRoot) {
        this.root = jpnRoot;
    }
    
    public void setView(JPanel itemPanel, JLabel itemLabel) {
        kindSelected = "Home";
        itemPanel.setBackground(new Color(207, 178, 145));
        itemLabel.setBackground(new Color(207, 178, 145));
        
        root.removeAll();
        root.setLayout(new BorderLayout());
        
        E_homePanel homePanel = new E_homePanel();
        homePanel.setTaiKhoan(taiKhoan); 
        root.add(homePanel);
        
        root.validate();
        root.repaint();
    }
    
    public void setEvent(List<DanhMucBean> listItem) {
        this.listItem = listItem;
        for(DanhMucBean item : listItem) {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));
        }
    }
    
    class LabelEvent implements MouseListener {
        
        private JPanel node;
        private String kind;
        
        private JPanel itemPanel;
        private JLabel itemLabel;

        public LabelEvent(String kind, JPanel itemPanel, JLabel itemLabel) {
            this.kind = kind;
            this.itemPanel = itemPanel;
            this.itemLabel = itemLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            switch(kind) {
                case "Home":
                    node = new E_homePanel();
                    ((E_homePanel)node).setTaiKhoan(taiKhoan);
                    break;
                case "Menu":
                    node = new E_menuPanel();
                    break;
                case "Bill":
                    node = new E_billPanel();
                    break;
                case "Inventory":
                    node = new E_inventoryPanel();
                    break;
                case "Customer":
                    node = new E_customerPanel();
                    break;
                case "Voucher":
                    node = new E_voucherPanel();
                    break;
                case "Employee":
                    node = new M_employeePanel();
                    break;
                default:
                    node = new E_homePanel();
                    ((E_homePanel)node).setTaiKhoan(taiKhoan);
                    break;
                 
            }
            root.removeAll();
            root.setLayout(new BorderLayout());
            root.add(node);
            root.validate();
            root.repaint();
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            itemPanel.setBackground(new Color(207, 178, 145));
            itemLabel.setBackground(new Color(207, 178, 145));
            setChangeBackground(kind);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            itemPanel.setBackground(new Color(207, 178, 145));
            itemLabel.setBackground(new Color(207, 178, 145));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!kindSelected.equalsIgnoreCase(kind)) {
                itemPanel.setBackground(new Color(245, 245, 220));
                itemLabel.setBackground(new Color(245, 245, 220));

            }
        }
        
    }
    
    private void setChangeBackground(String kind) {
        for(DanhMucBean item : listItem) {
            if(item.getKind().equalsIgnoreCase(kind)) {
                item.getJpn().setBackground(new Color (207, 178, 145));
                item.getJlb().setBackground(new Color (207, 178, 145));
            }
            else {
                item.getJpn().setBackground(new Color (245, 245, 220));
                item.getJlb().setBackground(new Color (245, 245, 220));            
            }
        }
    }
}
