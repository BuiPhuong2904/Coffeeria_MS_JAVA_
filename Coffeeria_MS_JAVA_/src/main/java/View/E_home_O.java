
package View;

import dao.KhachHangDAO;
import dao.MonAnDAO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;
import model.MonAn;
import model.WrapLayout;

public abstract class E_home_O extends javax.swing.JFrame implements addItemListener{
    
    public E_home_O() {
        initComponents();
        
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Name", "Price", "Quantity", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        
        orderTable.setModel(model);
        
        dis_textLabel.setText("0 VND");
        
        returnButton.setContentAreaFilled(false);
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        
        loadMonAnToMenu();
        
        addQuantityChangeListener();
        
//        orderFrame.refreshMenu(updatedDrinkList);
    }
    
    public void loadMonAnToMenu() {
        MonAnDAO monAnDAO = new MonAnDAO();
        List<MonAn> danhSachMonAn = monAnDAO.findAll(); 

        menuPanel.removeAll();
        menuPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 10));


        for (MonAn mon : danhSachMonAn) {
            itemPanel item = new itemPanel();
            
            item.setMonAn(mon);
            item.setAddItemListener(this);

            item.setNameLabel(mon.getTenMon());
            item.setPriceLabel(mon.getGiaBan());
            item.setImage(mon.getImageIcon());

            menuPanel.add(item);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    @Override
    public void onAddItem(MonAn monAn, int quantity) {
        System.out.println("Đã thêm món: " + monAn.getTenMon() + ", số lượng: " + quantity);

        DefaultTableModel model = (DefaultTableModel) orderTable.getModel(); 
        removeEmptyRows(model);

        boolean found = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object tenMonObj = model.getValueAt(i, 0); 

            if (tenMonObj != null && tenMonObj.toString().equals(monAn.getTenMon())) {
                Object oldQtyObj = model.getValueAt(i, 2);
                int oldQuantity = oldQtyObj != null ? Integer.parseInt(oldQtyObj.toString()) : 0;

                int newQuantity = oldQuantity + quantity;
                model.setValueAt(newQuantity, i, 2);

                double newTotal = monAn.getGiaBan() * newQuantity;
                model.setValueAt(newTotal, i, 3);

                found = true;
                break;
            }
        }

        if (!found) {
            double total = monAn.getGiaBan() * quantity;
            model.addRow(new Object[]{
                monAn.getTenMon(),
                monAn.getGiaBan(),
                quantity,
                total
            });
        }

        updateSubTotalPrice();
        
        updateTotalPrice();

    }
    
    private void addQuantityChangeListener() {
        orderTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 2) { 
                    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();

                    try {
                        int quantity = Integer.parseInt(model.getValueAt(row, 2).toString());
                        if (quantity == 0) {
                            // Xóa dòng nếu số lượng = 0
                            model.removeRow(row);
                        } else if (quantity > 0) {
                            double price = Double.parseDouble(model.getValueAt(row, 1).toString());
                            double total = price * quantity;
                            model.setValueAt(total, row, 3);
                        } else {
                            // Số lượng âm không hợp lệ, báo lỗi và đặt lại 1
                            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn hoặc bằng 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            model.setValueAt(1, row, 2);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                    updateSubTotalPrice();
                    updateTotalPrice();
                }
            }
        });
    }

    private void updateSubTotalPrice() {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        double sum = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3); 
            if (value != null) {
                if (value instanceof Number) {
                    sum += ((Number) value).doubleValue();
                } else {
                    try {
                        sum += Double.parseDouble(value.toString());
                    } catch (NumberFormatException e) {

                        System.err.println("Lỗi chuyển dữ liệu sang số: " + value);
                    }
                }
            }
        }

//        sub_textLabel.setText(String.format("%.0f VNĐ", sum));
        DecimalFormat formatter = new DecimalFormat("#,###");
        sub_textLabel.setText(formatter.format(sum) + " VND");

    }
    
    private void updateTotalPrice() {
        double subTotal = 0;
        double discount = 0;

        try {
            String subText = sub_textLabel.getText().replace(" VND", "").replace(",", "").replace(".", "").trim();
            subTotal = Double.parseDouble(subText);

        } catch (NumberFormatException e) {
            System.err.println("Lỗi đọc tổng tiền trước: " + e.getMessage());
        }

        try {
            String disText = dis_textLabel.getText().replace(" VND", "").replace(",", "").replace(".", "").trim();
            if (disText.isEmpty()) {
                discount = 0;
            } else {
                discount = Double.parseDouble(disText);
            }
        } catch (NumberFormatException e) {
            discount = 0;
            System.err.println("Lỗi đọc giảm giá: " + e.getMessage());
        }

        double total = subTotal - discount;

        if (total < 0) total = 0;

        DecimalFormat formatter = new DecimalFormat("#,###");
        total_textLabel.setText(formatter.format(total) + " VND");
    }

    private void removeEmptyRows(DefaultTableModel model) {
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Object tenMonObj = model.getValueAt(i, 0);
            Object quantityObj = model.getValueAt(i, 2);

            boolean isEmptyRow = (tenMonObj == null || tenMonObj.toString().trim().isEmpty())
                              && (quantityObj == null || quantityObj.toString().trim().isEmpty());

            if (isEmptyRow) {
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
        topPanel = new javax.swing.JPanel();
        returnButton = new javax.swing.JButton();
        orderPanel = new javax.swing.JPanel();
        orderLabel = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        nameTextField = new javax.swing.JTextField();
        checkButton = new javax.swing.JButton();
        orderScrollPane = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        conPanel = new javax.swing.JPanel();
        confirmButton = new javax.swing.JButton();
        totalPanel = new javax.swing.JPanel();
        subLabel = new javax.swing.JLabel();
        sub_textLabel = new javax.swing.JLabel();
        disLabel = new javax.swing.JLabel();
        dis_textLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        total_textLabel = new javax.swing.JLabel();
        orderLabel1 = new javax.swing.JLabel();
        menuScrollPane = new javax.swing.JScrollPane();
        menuPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tempPanel.setBackground(new java.awt.Color(252, 252, 246));
        tempPanel.setPreferredSize(new java.awt.Dimension(1100, 750));

        topPanel.setBackground(new java.awt.Color(153, 255, 204));

        returnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/return20.png"))); // NOI18N
        returnButton.setFocusPainted(false);
        returnButton.setPreferredSize(new java.awt.Dimension(30, 30));
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        orderPanel.setPreferredSize(new java.awt.Dimension(473, 696));

        orderLabel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        orderLabel.setText("ORDER");

        phoneTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        phoneTextField.setForeground(new java.awt.Color(102, 102, 102));
        phoneTextField.setText("PHONE NUMBER");
        phoneTextField.setPreferredSize(new java.awt.Dimension(123, 40));
        phoneTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                phoneTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                phoneTextFieldFocusLost(evt);
            }
        });
        phoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneTextFieldActionPerformed(evt);
            }
        });

        nameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameTextField.setForeground(new java.awt.Color(102, 102, 102));
        nameTextField.setText("CUSTOMER NAME");
        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusLost(evt);
            }
        });
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        checkButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/check35.png"))); // NOI18N
        checkButton.setPreferredSize(new java.awt.Dimension(40, 40));
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkButtonActionPerformed(evt);
            }
        });

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

        conPanel.setPreferredSize(new java.awt.Dimension(473, 44));

        confirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        confirmButton.setText("Confirm Payment");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout conPanelLayout = new javax.swing.GroupLayout(conPanel);
        conPanel.setLayout(conPanelLayout);
        conPanelLayout.setHorizontalGroup(
            conPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, conPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        conPanelLayout.setVerticalGroup(
            conPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(confirmButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        totalPanel.setBackground(new java.awt.Color(255, 255, 255));

        subLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        subLabel.setText("Subtotal");

        sub_textLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sub_textLabel.setText("jLabel");
        sub_textLabel.setPreferredSize(new java.awt.Dimension(44, 20));

        disLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        disLabel.setText("Discount:");

        dis_textLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dis_textLabel.setText("jLabel3");

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalLabel.setText("TOTAL");

        total_textLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        total_textLabel.setText("jLabel3");

        javax.swing.GroupLayout totalPanelLayout = new javax.swing.GroupLayout(totalPanel);
        totalPanel.setLayout(totalPanelLayout);
        totalPanelLayout.setHorizontalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(subLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(totalLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(disLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dis_textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(sub_textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        totalPanelLayout.setVerticalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sub_textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dis_textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_textLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(conPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orderLabel)
                            .addGroup(orderPanelLayout.createSequentialGroup()
                                .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(conPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        orderLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        orderLabel1.setText("MENU");

        menuScrollPane.setViewportView(menuPanel);

        javax.swing.GroupLayout tempPanelLayout = new javax.swing.GroupLayout(tempPanel);
        tempPanel.setLayout(tempPanelLayout);
        tempPanelLayout.setHorizontalGroup(
            tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tempPanelLayout.createSequentialGroup()
                .addGroup(tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tempPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(orderLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE))
                    .addComponent(menuScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tempPanelLayout.setVerticalGroup(
            tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempPanelLayout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tempPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tempPanelLayout.createSequentialGroup()
                        .addComponent(orderLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(menuScrollPane))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void phoneTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneTextFieldFocusGained
        // TODO add your handling code here:
        if (phoneTextField.getText().equals("PHONE NUMBER")) {
            phoneTextField.setText("");
            phoneTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_phoneTextFieldFocusGained

    private void phoneTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneTextFieldFocusLost
        // TODO add your handling code here:
        if (phoneTextField.getText().equals("")) {
            phoneTextField.setText("PHONE NUMBER");
            phoneTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_phoneTextFieldFocusLost

    private void phoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneTextFieldActionPerformed

    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusGained
        // TODO add your handling code here:
        if (nameTextField.getText().equals("CUSTOMER NAME")) {
            nameTextField.setText("");
            nameTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_nameTextFieldFocusGained

    private void nameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusLost
        // TODO add your handling code here:
        if (nameTextField.getText().equals("")) {
            nameTextField.setText("CUSTOMER NAME");
            nameTextField.setForeground(new Color(102, 102, 102));
        }
    }//GEN-LAST:event_nameTextFieldFocusLost

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        // TODO add your handling code here:
        E_homePanel homePanel = new E_homePanel();
        homePanel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_returnButtonActionPerformed

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkButtonActionPerformed
        // TODO add your handling code here:
        String phone = phoneTextField.getText().trim();
        if (phone.isEmpty()) {
            nameTextField.setText("Khách lẻ");
            return;
        }

        KhachHangDAO khDao = new KhachHangDAO();
        KhachHang kh = khDao.findByPhone(phone);

        if (kh != null) {
            nameTextField.setText(kh.getHoten());
        } else {
            nameTextField.setText("Khách lẻ");
        }
    }//GEN-LAST:event_checkButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkButton;
    private javax.swing.JPanel conPanel;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel disLabel;
    private javax.swing.JLabel dis_textLabel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JScrollPane menuScrollPane;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel orderLabel;
    private javax.swing.JLabel orderLabel1;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JScrollPane orderScrollPane;
    private javax.swing.JTable orderTable;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JButton returnButton;
    private javax.swing.JLabel subLabel;
    private javax.swing.JLabel sub_textLabel;
    private javax.swing.JPanel tempPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JLabel total_textLabel;
    // End of variables declaration//GEN-END:variables
}
