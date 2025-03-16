/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import Validate.Validate;
import java.awt.HeadlessException;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import entity.KhachHang;
import repository.KhachHangRepo;
import config.DBConnect;
import java.util.Collections;
import logic.RandomStringGenerator;
import repository.BanHangRepo;
import repository.HDRepository;
import request.KhachHangRequest;

public class ChonKhachHang extends javax.swing.JFrame {

    private ArrayList<KhachHang> list;
    private KhachHangRepo repoKH;
    private KhachHang kh = new KhachHang();
    private Validate vali = new Validate();
    private BanHangRepo repo;
    private KhachHang selectedKhachHang;
    private String maKH;

    /**
     * Creates new form ChonKhachHang
     */
    public ChonKhachHang() throws SQLException {
        initComponents();
        setDefaultCloseOperation(ChonKhachHang.DISPOSE_ON_CLOSE);
        repoKH = new KhachHangRepo();
        this.getData();
        this.fillToTable(list);
    }

    public KhachHang getSelectedKhachHang() {
        return selectedKhachHang;
    }

    public void getData() throws SQLException {
        list = repoKH.findAll();
    }
    public void fillToTable(ArrayList<KhachHang> list) {
    DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
    model.setRowCount(0);
    int index = 1;
    Collections.reverse(list);
       for (KhachHang khachHang : list) {
        Object[] row = {
            index++, 
            khachHang.getMaKH(), 
            khachHang.getTenKH(), 
            khachHang.getGioiTinh() ? "Nam" : "Nữ", 
            khachHang.getSdt(),  
             
            khachHang.getEmail(),
            khachHang.getDiaChi(),
            khachHang.getNgaySinh(), 
            khachHang.getTrangThai() == 1 ? "Còn hoạt động" : "Ngưng hoạt động"
        };
        model.addRow(row);
    }
}

//    public void fillToTable() {
//        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
//        model.setRowCount(0);
//        int index = 1;
//        for (KhachHang khachHang : list) {
//            Object[] row = {index++, khachHang.getMaKH(), khachHang.getTenKH(), khachHang.getGioiTinh() ? "Nam" : "Nữ", khachHang.getSdt(), khachHang.getEmail(), khachHang.getDiaChi(), khachHang.getNgaySinh(), khachHang.getTrangThai() == 1 ? "Còn hoạt động" : "Ngưng hoạt động"};
//            model.addRow(row);
//        }
//    }

    private KhachHang getFormData() {
        KhachHang kh = new KhachHang();
        maKH = RandomStringGenerator.generateRandomString("KH");
        kh.setMaKH(maKH);
        kh.setTenKH(txtTenKH.getText());
        kh.setGioiTinh(rdonam.isSelected());
        kh.setSdt(txtSdt.getText());
        kh.setDiaChi(txtDiaChi.getText());
        kh.setEmail(txtEmail.getText());
        if (txtNgaySinh.getDate() != null) {
            kh.setNgaySinh(new java.sql.Date(txtNgaySinh.getDate().getTime()));
        } else {
            kh.setNgaySinh(null);
        }
        kh.setTrangThai(1);
        return kh;
    }

    private void clearForm() {
        txtTenKH.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtNgaySinh.setDate(null);
        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
    }

    private boolean check() {
        if (txtTenKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống !");
            return false;
        } else if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống !");
            return false;
        } else if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống !");
            return false;
        } else if (!vali.checkDate(txtNgaySinh.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải là ngày trước ngày hôm nay !");
            return false;
        } else if (!vali.checkEmail(txtEmail.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng!");
            return false;
        } else if (!vali.checkSdt(txtSdt.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 số và bắt đầu bằng số 0 !");
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        btnChon = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        rdonam = new javax.swing.JRadioButton();
        rdonu = new javax.swing.JRadioButton();
        txtSdt = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setText("Tìm kiếm:");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Mã KH", "Tên", "Giới tính", "Số dt", "Địa chỉ", "Email", "Ngày sinh", "Trạng thái"
            }
        ));
        jScrollPane1.setViewportView(tblKhachHang);

        btnChon.setBackground(new java.awt.Color(255, 255, 0));
        btnChon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChon.setText("Chọn");
        btnChon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 125, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(btnChon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnChon)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh sách khách hàng", jPanel1);

        jLabel3.setText("Tên KH");

        jLabel4.setText("Giới tính");

        jLabel5.setText("Số dt");

        jLabel6.setText("Địa chỉ");

        jLabel7.setText("Email");

        jLabel8.setText("Ngày sinh");

        buttonGroup1.add(rdonam);
        rdonam.setSelected(true);
        rdonam.setText("Nam");

        buttonGroup1.add(rdonu);
        rdonu.setText("Nữ");

        btnThem.setBackground(new java.awt.Color(255, 255, 0));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(255, 255, 0));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(rdonam, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(41, 41, 41)
                                        .addComponent(rdonu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTenKH)
                                    .addComponent(txtSdt)
                                    .addComponent(txtDiaChi)
                                    .addComponent(txtEmail)
                                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(btnThem)
                        .addGap(140, 140, 140)
                        .addComponent(btnLamMoi)))
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rdonam)
                    .addComponent(rdonu))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnLamMoi))
                .addGap(26, 26, 26))
        );

        jTabbedPane1.addTab("Cập nhật khách hàng", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        // TODO add your handling code here:
        String keyword = txtSearch.getText().trim();
        try {
            List<KhachHang> list = repoKH.search(keyword);
            DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
            model.setRowCount(0);

            for (KhachHang kh : list) {
                model.addRow(new Object[]{
                    kh.getId(),
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getGioiTinh() ? "Nam" : "Nữ",
                    kh.getSdt(),
                    kh.getDiaChi(),
                    kh.getEmail(),
                    kh.getNgaySinh(),
                    kh.getTrangThai() == 1 ? "Còn hoạt động" : "Ngưng hoạt động"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi tìm kiếm khách hàng.");
        }
    }//GEN-LAST:event_txtSearchKeyTyped

    private void btnChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonActionPerformed
        int row = tblKhachHang.getSelectedRow();
        if (row >= 0) {
            selectedKhachHang = list.get(row);
            System.out.println(selectedKhachHang.getMaKH() + selectedKhachHang.getTenKH());
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng");
        }
        this.dispose();


    }//GEN-LAST:event_btnChonActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn thêm khách hàng này?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            if (repoKH.checkTrungMa(maKH)) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại");
            } else if (repoKH.checkTrungSDT(txtSdt.getText())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không được trùng nhau");
            } else if(repoKH.checkTrungEmail(txtEmail.getText())){
                JOptionPane.showMessageDialog(this, "Email không được trùng nhau");
            }
            else{
                if (check()) {
                    KhachHang kh = this.getFormData();
                    repoKH.add(kh);

                    // Sau khi thêm khách hàng mới, tải lại dữ liệu và cập nhật bảng
                    try {
//                        ArrayList<KhachHang> list = repoKH.findAll();
//                        Collections.reverse(list);
                        
                        this.getData(); // Tải lại danh sách khách hàng
                        this.fillToTable(list); // Cập nhật bảng với dữ liệu mới
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi tải lại dữ liệu.");
                    }

                    JOptionPane.showMessageDialog(this, "Thêm thành công");

                    this.clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            }
        }

    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        this.clearForm();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChonKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChonKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChonKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChonKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ChonKhachHang().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ChonKhachHang.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChon;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdonam;
    private javax.swing.JRadioButton rdonu;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenKH;
    // End of variables declaration//GEN-END:variables
}
