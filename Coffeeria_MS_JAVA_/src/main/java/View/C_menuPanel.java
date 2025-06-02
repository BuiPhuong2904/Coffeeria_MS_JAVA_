
package View;

import controller.HoaDonController;
import dao.KhachHangDAO;
import dao.MonAnDAO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import model.CT_HoaDon;
import model.HoaDon;
import model.MonAn;
import model.TaiKhoan;
import model.WrapLayout;

/**
 *
 * @author nttma
 */
public abstract class C_menuPanel extends javax.swing.JPanel implements addItemListener{

    private TaiKhoan taiKhoan;
    
    private KhachHangDAO khachHangDAO;
    private MonAnDAO monAnDAO;
    
    /**
     * Creates new form C_menuPanel
     */
    
    public void setTaiKhoan(TaiKhoan tk) {
        this.taiKhoan = tk;
        loadData();
    }
        
    public C_menuPanel() {
        initComponents();
        
        khachHangDAO = new KhachHangDAO();
        monAnDAO = new MonAnDAO();
        
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Name", "Price", "Quantity", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        orderTable.setModel(model);
        
        menuScrollPane.setViewportView(menuPanel);
        menuPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 10));
        
        dis_textLabel1.setText("0 VND");
        
        SwingUtilities.invokeLater(() -> {
            loadMonAnToMenu();
            addQuantityChangeListener();
        });
    }
    
    public void loadMonAnToMenu() {
        MonAnDAO monAnDAO = new MonAnDAO(); 
        List<MonAn> danhSachMonAn = monAnDAO.findAll();

        menuPanel.removeAll();
        
        WrapLayout layout = new WrapLayout(FlowLayout.CENTER, 10, 10);
        menuPanel.setLayout(layout);

        for (MonAn mon : danhSachMonAn) {
            itemPanel item = new itemPanel();

            item.setMonAn(mon);
                    
            item.setNameLabel(mon.getTenMon());
            item.setPriceLabel(mon.getGiaBan());
            item.setImage(mon.getImageIcon());
            
            item.setAddItemListener(this);

            menuPanel.add(item);
        }
        
        menuPanel.setPreferredSize(menuPanel.getPreferredSize());

        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private void loadData() {
        if (taiKhoan != null) {
            KhachHangDAO khDao = new KhachHangDAO();
            String hoten = khDao.getHoTenKHByMaTK(taiKhoan.getMaTK());
            nameLabel.setText(hoten != null && !hoten.isEmpty() ? hoten : "Khách hàng");
        }
    }
    
    @Override
    public void onAddItem(MonAn monAn, int quantity) {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        removeEmptyRows(model);

        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(monAn.getTenMon())) {
                int oldQuantity = Integer.parseInt(model.getValueAt(i, 2).toString());
                int newQuantity = oldQuantity + quantity;
                model.setValueAt(newQuantity, i, 2);
                model.setValueAt(monAn.getGiaBan() * newQuantity, i, 3);
                found = true;
                break;
            }
        }

        if (!found) {
            double total = monAn.getGiaBan() * quantity;
            model.addRow(new Object[]{
                monAn.getTenMon(), monAn.getGiaBan(), quantity, total
            });
        }

        updateSubTotalPrice();
        updateTotalPrice();
    }

    private void addQuantityChangeListener() {
        orderTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
                int row = e.getFirstRow();

                try {
                    int quantity = Integer.parseInt(model.getValueAt(row, 2).toString());
                    if (quantity == 0) {
                        model.removeRow(row);
                    } else if (quantity > 0) {
                        double price = Double.parseDouble(model.getValueAt(row, 1).toString());
                        model.setValueAt(price * quantity, row, 3);
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng phải >= 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        model.setValueAt(1, row, 2);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

                updateSubTotalPrice();
                updateTotalPrice();
            }
        });
    }

    private void updateSubTotalPrice() {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        double sum = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);
            if (value != null) {
                try {
                    sum += Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi parse tiền: " + value);
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
    
        sub_textLabel1.setText(formatter.format(sum) + " VND");
    }

    private void updateTotalPrice() {
        double subTotal = parseCurrency(sub_textLabel1.getText());
        double discount = parseCurrency(dis_textLabel1.getText());

        double total = subTotal - discount;
        if (total < 0) total = 0;

        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); 
        formatter.setDecimalFormatSymbols(symbols);
        
        total_textLabel1.setText(formatter.format(total) + " VND");
    }

    private double parseCurrency(String text) {
        String cleaned = text.replace("VND", "").replaceAll("\\.", "").trim();
        return Double.parseDouble(cleaned);
    }

    private void removeEmptyRows(DefaultTableModel model) {
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            if ((model.getValueAt(i, 0) == null || model.getValueAt(i, 0).toString().trim().isEmpty()) &&
                (model.getValueAt(i, 2) == null || model.getValueAt(i, 2).toString().trim().isEmpty())) {
                model.removeRow(i);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tempPanel = new javax.swing.JPanel();
        billPanel = new javax.swing.JPanel();
        orderLabel = new javax.swing.JLabel();
        orderScrollPane = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        totalPanel1 = new javax.swing.JPanel();
        subLabel1 = new javax.swing.JLabel();
        sub_textLabel1 = new javax.swing.JLabel();
        disLabel1 = new javax.swing.JLabel();
        dis_textLabel1 = new javax.swing.JLabel();
        totalLabel1 = new javax.swing.JLabel();
        total_textLabel1 = new javax.swing.JLabel();
        enterLabel = new javax.swing.JLabel();
        entercodeTextField = new javax.swing.JTextField();
        confirmButton = new javax.swing.JButton();
        checkcodeButton = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        menuLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        welcomeLabel = new javax.swing.JLabel();
        menuScrollPane = new javax.swing.JScrollPane();
        menuPanel = new javax.swing.JPanel();

        tempPanel.setBackground(new java.awt.Color(252, 252, 246));
        tempPanel.setPreferredSize(new java.awt.Dimension(1000, 750));

        orderLabel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        orderLabel.setText("ORDER");

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Price", "Quantity", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        orderTable.setPreferredSize(new java.awt.Dimension(452, 392));
        orderScrollPane.setViewportView(orderTable);

        totalPanel1.setBackground(new java.awt.Color(255, 255, 255));

        subLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        subLabel1.setText("Subtotal");

        sub_textLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sub_textLabel1.setText("jLabel");
        sub_textLabel1.setPreferredSize(new java.awt.Dimension(44, 20));

        disLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        disLabel1.setText("Discount:");

        dis_textLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dis_textLabel1.setText("jLabel3");

        totalLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalLabel1.setText("TOTAL");

        total_textLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        total_textLabel1.setText("jLabel3");

        javax.swing.GroupLayout totalPanel1Layout = new javax.swing.GroupLayout(totalPanel1);
        totalPanel1.setLayout(totalPanel1Layout);
        totalPanel1Layout.setHorizontalGroup(
            totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(subLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(totalLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(disLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sub_textLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_textLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dis_textLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        totalPanel1Layout.setVerticalGroup(
            totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sub_textLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dis_textLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_textLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        enterLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        enterLabel.setText("Enter discount code:");

        entercodeTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        entercodeTextField.setForeground(new java.awt.Color(102, 102, 102));
        entercodeTextField.setPreferredSize(new java.awt.Dimension(123, 40));
        entercodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                entercodeTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                entercodeTextFieldFocusLost(evt);
            }
        });
        entercodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entercodeTextFieldActionPerformed(evt);
            }
        });

        confirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        confirmButton.setText("Confirm Payment");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        checkcodeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/check35.png"))); // NOI18N
        checkcodeButton.setPreferredSize(new java.awt.Dimension(40, 40));
        checkcodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkcodeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout billPanelLayout = new javax.swing.GroupLayout(billPanel);
        billPanel.setLayout(billPanelLayout);
        billPanelLayout.setHorizontalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addComponent(orderLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addComponent(orderScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(billPanelLayout.createSequentialGroup()
                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(enterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entercodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkcodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(totalPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        billPanelLayout.setVerticalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(enterLabel)
                        .addComponent(entercodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkcodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(confirmButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        topPanel.setBackground(new java.awt.Color(245, 245, 220));

        menuLabel.setFont(new java.awt.Font("Algerian", 1, 36)); // NOI18N
        menuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menuLabel.setText("menu");
        menuLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        nameLabel.setText("name");

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        welcomeLabel.setText("Welcome,");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(welcomeLabel)
                    .addComponent(nameLabel))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        menuScrollPane.setViewportView(menuPanel);

        javax.swing.GroupLayout tempPanelLayout = new javax.swing.GroupLayout(tempPanel);
        tempPanel.setLayout(tempPanelLayout);
        tempPanelLayout.setHorizontalGroup(
            tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempPanelLayout.createSequentialGroup()
                .addComponent(menuScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tempPanelLayout.setVerticalGroup(
            tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempPanelLayout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuScrollPane)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tempPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tempPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void entercodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entercodeTextFieldFocusGained
        // TODO add your handling code here:
        if (entercodeTextField.getText().equals("")) {
            entercodeTextField.setText("");
            entercodeTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_entercodeTextFieldFocusGained

    private void entercodeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entercodeTextFieldFocusLost
        // TODO add your handling code here:
        if (entercodeTextField.getText().equals("")) {
            entercodeTextField.setText("");
            entercodeTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_entercodeTextFieldFocusLost

    private void entercodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entercodeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entercodeTextFieldActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
        if (orderTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn món!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setTongTienTruoc(parseCurrency(sub_textLabel1.getText()));
            hoaDon.setTienGiamGia(parseCurrency(dis_textLabel1.getText()));
            hoaDon.setTongTienSau(parseCurrency(total_textLabel1.getText()));

            hoaDon.setHinhThucTT("Tiền mặt");
            hoaDon.setNgayLap(new java.util.Date());
            hoaDon.setGhiChu(null);
            hoaDon.setMaNV("NV007");

            String maKM = entercodeTextField.getText().trim();
            hoaDon.setMaKM(maKM.isEmpty() ? null : maKM);

            if (taiKhoan != null) {
                KhachHangDAO khDao = new KhachHangDAO();
                String maKH = khDao.getMaKHByMaTK(taiKhoan.getMaTK());
                hoaDon.setMaKH(maKH != null ? maKH : "KH001");
            } else {
                hoaDon.setMaKH("KH001");
            }

            List<CT_HoaDon> chiTietList = new ArrayList<>();
            MonAnDAO monAnDAO = new MonAnDAO();

            for (int i = 0; i < orderTable.getRowCount(); i++) {
                CT_HoaDon ct = new CT_HoaDon();

                String tenMon = orderTable.getValueAt(i, 0).toString();
                String maMon = monAnDAO.getMaMonByTen(tenMon);

                if (maMon == null) {
                    JOptionPane.showMessageDialog(this, "Món ăn không tồn tại: " + tenMon, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ct.setMaMon(maMon);
                ct.setDonGia(Double.valueOf(orderTable.getValueAt(i, 1).toString()));
                ct.setSoLuong(Integer.valueOf(orderTable.getValueAt(i, 2).toString()));
                chiTietList.add(ct);
            }

            // Gửi sang controller xử lý insert
            HoaDonController hoaDonController = new HoaDonController();
            hoaDonController.insertHoaDon(hoaDon, chiTietList);

            JOptionPane.showMessageDialog(this, "Cảm ơn bạn đã đặt món!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            // Xóa đơn hàng
            DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
            model.setRowCount(0);
            updateSubTotalPrice();
            updateTotalPrice();

        } catch (HeadlessException | NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void checkcodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkcodeButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_checkcodeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel billPanel;
    private javax.swing.JButton checkcodeButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel disLabel1;
    private javax.swing.JLabel dis_textLabel1;
    private javax.swing.JLabel enterLabel;
    private javax.swing.JTextField entercodeTextField;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JScrollPane menuScrollPane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel orderLabel;
    private javax.swing.JScrollPane orderScrollPane;
    private javax.swing.JTable orderTable;
    private javax.swing.JLabel subLabel1;
    private javax.swing.JLabel sub_textLabel1;
    private javax.swing.JPanel tempPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel totalLabel1;
    private javax.swing.JPanel totalPanel1;
    private javax.swing.JLabel total_textLabel1;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
