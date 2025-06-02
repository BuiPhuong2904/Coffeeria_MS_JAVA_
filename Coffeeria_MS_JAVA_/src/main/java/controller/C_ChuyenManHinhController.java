package controller;

import View.C_dealPanel;
import View.C_historyPanel;
import View.C_homePanel;
import View.C_menuPanel;
import View.C_profilePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.TaiKhoan;

/**
 *
 * @author nttma
 */
public class C_ChuyenManHinhController {
    
    private JPanel root;
    private String kindSelected = "";
    
    private List<DanhMucBean> listItem = null;
    
    private TaiKhoan taiKhoan;

    public void setTaiKhoan(TaiKhoan tk) {
        this.taiKhoan = tk;
    }
        
    public C_ChuyenManHinhController(JPanel jpnRoot) {
        this.root = jpnRoot;
    }
    
    public void setView(JPanel itemPanel, JLabel itemLabel) {
        kindSelected = "Home";
        itemPanel.setBackground(new Color(207, 178, 145));
        itemLabel.setBackground(new Color(207, 178, 145));
        
        root.removeAll();
        root.setLayout(new BorderLayout());

        C_homePanel homePanel = new C_homePanel();
        homePanel.setTaiKhoan(taiKhoan);  // truyền tài khoản
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
                    node = new C_homePanel();
                    ((C_homePanel)node).setTaiKhoan(taiKhoan);
                    break;
                case "Menu":
                    node = new C_menuPanel() {};
                    ((C_menuPanel)node).setTaiKhoan(taiKhoan);
                    break;
                case "Deal":
                    node = new C_dealPanel();
                    break;
                case "History":
                    node = new C_historyPanel();
                    ((C_historyPanel)node).setTaiKhoan(taiKhoan);
                    break;
                case "Profile":
                    node = new C_profilePanel();
                    ((C_profilePanel)node).setTaiKhoan(taiKhoan);
                    break;
                default:
                    node = new C_homePanel();
                    ((C_homePanel)node).setTaiKhoan(taiKhoan);
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
