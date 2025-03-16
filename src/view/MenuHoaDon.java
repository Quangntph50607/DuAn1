/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import repository.HDRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DUNG
 */
public class MenuHoaDon extends javax.swing.JInternalFrame {

    private ArrayList<HoaDon> currentList;
    private HDRepository hdRepo = new HDRepository();

    /**
     * Creates new form MenuHoaDon
     */
    public MenuHoaDon() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        this.loadTable();

    }

    private void fillHD() {
        Integer trangThai = null;
        if (cbbLocTrangThai.getSelectedItem().equals("Đã hủy")) {
            trangThai = 0;
        } else if (cbbLocTrangThai.getSelectedItem().equals("Chờ thanh toán")) {
            trangThai = 1;
        } else if (cbbLocTrangThai.getSelectedItem().equals("Đã thanh toán")) {
            trangThai = 2;
        }
        String hinhThucTT = null;
        String hinhThucTTSelected = cbbHinhThucTT.getSelectedItem().toString();
        if (!hinhThucTTSelected.equals("Tất cả")) {
            hinhThucTT = hinhThucTTSelected;
        }
        Double tongTienMin = null;
        Double tongTienMax = null;
        String tongTien = cbbTongTien.getSelectedItem().toString();
        if (tongTien.equals("0 - 1 triệu")) {
            tongTienMax = 1000000.0;
        } else if (tongTien.equals("1 triệu - 5 triệu")) {
            tongTienMin = 1000000.0;
            tongTienMax = 5000000.0;
        } else if (tongTien.equals("Trên 5 triệu")) {
            tongTienMin = 5000000.0;
        }
        Integer thang = null;
        String thangSelected = cbbThang.getSelectedItem().toString();
        if (!thangSelected.equals("Tất cả")) {
            thang = Integer.parseInt(thangSelected);
        }
        Integer nam = null;
        String namSelected = cbbNam.getSelectedItem().toString();
        if (!namSelected.equals("Tất cả")) {
            nam = Integer.parseInt(namSelected);
        }
        ArrayList<HoaDon> fillHD = hdRepo.fillHoaDon(trangThai, hinhThucTT, tongTienMin, tongTienMax, thang, nam);
        updateTable(fillHD);
    }

    private void updateTable(ArrayList<HoaDon> results) {
        DefaultTableModel dtm = (DefaultTableModel) this.tblHoaDon.getModel();
        dtm.setRowCount(0);
        for (HoaDon hd : results) {
            String trangThai;
            if (hd.getTrangThai() == 0) {
                trangThai = "Đã hủy";
            } else if (hd.getTrangThai() == 1) {
                trangThai = "Chờ thanh toán";
            } else {
                trangThai = "Đã thanh toán";
            }
            String tenNV = hdRepo.getTenNV(hd.getIdNhanVien());
            float giaTriVoucher = hdRepo.getGiaTriVoucher(hd.getIdVoucher(), hd.getTongTien());
            Object[] rowData = {
                hd.getIdHoaDon(),
                hd.getMaHoaDon(),
                tenNV,
                hd.getIdKhachHang(),
                hd.getTenKhachHang(),
                hd.getDiaChi(),
                hd.getSdt(),
                hd.getNgayTao(),
                hd.getTongTien(),
                hd.getHinhThucThanhToan(),
                giaTriVoucher,
                trangThai,};
            dtm.addRow(rowData);
        }
    }

    private void loadTable() {
        DefaultTableModel dtm = (DefaultTableModel) this.tblHoaDon.getModel();
        dtm.setRowCount(0);
        for (HoaDon hd : hdRepo.findall()) {
            String trangThai;
            if (hd.getTrangThai() == 0) {
                trangThai = "Đã hủy";
            } else if (hd.getTrangThai() == 1) {
                trangThai = "Chờ thanh toán";
            } else {
                trangThai = "Đã thanh toán";
            }
            String tenNV = hdRepo.getTenNV(hd.getIdNhanVien());
            float giaTriVoucher = hdRepo.getGiaTriVoucher(hd.getIdVoucher(), hd.getTongTien());
            Object[] rowData = {
                hd.getIdHoaDon(),
                hd.getMaHoaDon(),
                tenNV,
                hd.getIdKhachHang(),
                hd.getTenKhachHang(),
                hd.getDiaChi(),
                hd.getSdt(),
                hd.getNgayTao(),
                hd.getTongTien(),
                hd.getHinhThucThanhToan(),
                giaTriVoucher,
                trangThai,};
            dtm.addRow(rowData);
        }
    }

    private void loadChiTietHoaDon(int idHD) {
        DefaultTableModel dtm = (DefaultTableModel) tblHoaDonChiTiet.getModel();
        dtm.setRowCount(0);

        for (HoaDonChiTiet hdct : hdRepo.findChiTietByidHD(idHD)) {
            String trangThai;
            if (hdct.getTrangThai() == 0) {
                trangThai = "Đã hủy";
            } else if (hdct.getTrangThai() == 1) {
                trangThai = "Chờ thanh toán";
            } else {
                trangThai = "Đã thanh toán";
            }

            String tenSPCT = hdRepo.getTenSPCT(hdct.getId()); // Lấy tên sản phẩm chi tiết cho từng mục hóa đơn chi tiết

            Object[] rowData = {
                hdct.getId(),
                hdct.getMa(),
                hdct.getIdHD(),
                tenSPCT, // Hiển thị tên sản phẩm chi tiết
                hdct.getSl(),
                hdct.getDonGia(),
                hdct.getThanhTien(),
                trangThai,};
            dtm.addRow(rowData);
        }
        this.currentList = hdRepo.findall();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new swing.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbbThang = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbbNam = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        cbbLocTrangThai = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cbbTongTien = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbbHinhThucTT = new javax.swing.JComboBox<>();
        btnInHoaDon = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDonChiTiet = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1330, 800));

        panelRound1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelRound1.setPreferredSize(new java.awt.Dimension(1200, 530));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Tìm kiếm hóa  đơn:");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Mã HD", "Tên NV", "ID Khách hàng", "Tên KH", "Địa chỉ", "Số dt", "Ngày tạo", "Tổng tiền", "Hình thức thanh toán", "Số tiền được giảm", "Trạng thái"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoaDon);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 80));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Tháng:");

        cbbThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        cbbThang.setPreferredSize(new java.awt.Dimension(90, 30));
        cbbThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbThangActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Năm:");

        cbbNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012" }));
        cbbNam.setPreferredSize(new java.awt.Dimension(90, 30));
        cbbNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbNamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(cbbThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 80));

        cbbLocTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đã thanh toán", "Chờ thanh toán", "Đã hủy" }));
        cbbLocTrangThai.setPreferredSize(new java.awt.Dimension(200, 30));
        cbbLocTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLocTrangThaiActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Trạng thái thanh toán :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cbbLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setPreferredSize(new java.awt.Dimension(250, 80));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Tổng tiền:");

        cbbTongTien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "0 - 1 triệu", "1 triệu - 5 triệu", "Trên 5 triệu" }));
        cbbTongTien.setPreferredSize(new java.awt.Dimension(200, 30));
        cbbTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTongTienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setPreferredSize(new java.awt.Dimension(250, 80));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Hình thức thanh toán:");

        cbbHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Tiền mặt", "Chuyển khoản" }));
        cbbHinhThucTT.setPreferredSize(new java.awt.Dimension(200, 30));
        cbbHinhThucTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbHinhThucTTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btnInHoaDon.setText("In hóa đơn");
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnInHoaDon)
                        .addGap(94, 94, 94)
                        .addComponent(jLabel3)
                        .addGap(34, 34, 34)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelRound1Layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(53, 53, 53)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(48, 48, 48)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnInHoaDon)))
                .addGap(18, 18, 18)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("HÓA ĐƠN");

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 269));

        tblHoaDonChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Mã HDCT", "ID Hóa đơn", "Tên SP chi tiết", "Số lượng", "Đơn giá", "Thành tiền", "Trạng thái"
            }
        ));
        jScrollPane1.setViewportView(tblHoaDonChiTiet);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("HÓA ĐƠN CHI TIẾT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_txtSearchActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        int row = this.tblHoaDon.getSelectedRow();
        if (row == -1) {
            return;
        }
        int idHD = (Integer) tblHoaDon.getValueAt(row, 0);
        this.loadChiTietHoaDon(idHD);

    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void cbbLocTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLocTrangThaiActionPerformed
        // TODO add your handling code here:
        this.fillHD();
    }//GEN-LAST:event_cbbLocTrangThaiActionPerformed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        // TODO add your handling code here:
        String keyword = txtSearch.getText().trim();
        ArrayList<HoaDon> results = hdRepo.search(keyword);
        updateTable(results);
    }//GEN-LAST:event_txtSearchKeyTyped

    private void cbbHinhThucTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbHinhThucTTActionPerformed
        // TODO add your handling code here:
        this.fillHD();

    }//GEN-LAST:event_cbbHinhThucTTActionPerformed

    private void cbbTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTongTienActionPerformed
        // TODO add your handling code here:
        this.fillHD();
    }//GEN-LAST:event_cbbTongTienActionPerformed

    private void cbbThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbThangActionPerformed
        // TODO add your handling code here:
        this.fillHD();
    }//GEN-LAST:event_cbbThangActionPerformed

    private void cbbNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbNamActionPerformed
        // TODO add your handling code here:
        this.fillHD();
    }//GEN-LAST:event_cbbNamActionPerformed

    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        // TODO add your handling code here:

        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để in!");
            return;
        }
        int check = JOptionPane.showConfirmDialog(this, "Bạn chắn chắn muốn in hóa đơn ?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        String directoryPath = "";
        JFileChooser j = new JFileChooser("H:\\Projects_java\\hoaDon");
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int x = j.showSaveDialog(this);
        if (x == JFileChooser.APPROVE_OPTION) {
            directoryPath = j.getSelectedFile().getPath();
        } else {
            return; // Người dùng hủy bỏ
        }

        String fileName = JOptionPane.showInputDialog(this, "Nhập tên file (không cần .pdf):");
        if (fileName == null || fileName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên file không hợp lệ!");
            return;
        }

        String filePath = directoryPath + "/" + fileName + ".pdf";

        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));

            doc.open();

            // Load the custom font
            BaseFont baseFont = BaseFont.createFont("src/style/font-times-new-roman/times-new-roman-14.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);
            Font regularFont = new Font(baseFont, 12);

            // Thêm tiêu đề
            Paragraph title = new Paragraph("Hóa Đơn", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            doc.add(new Paragraph(" ")); // Thêm khoảng trống
            Paragraph storeInfo = new Paragraph(
                    "Jewelery Store\nNhóm 6 - SD19304\nGiảng viên: Nguyễn Khánh Huyền\nĐịa chỉ: FPT Polytechnic",
                    regularFont);
            storeInfo.setAlignment(Element.ALIGN_CENTER);
            doc.add(storeInfo);

            doc.add(new Paragraph(" ")); // Thêm khoảng trống
            doc.add(new Paragraph(
                    "----------------------------------------------------------------------------------------------------------------------------------",
                    regularFont));

            HoaDon selectedHoaDon = currentList.get(selectedRow);

            String maHoaDon = tblHoaDon.getValueAt(selectedRow, 1).toString();
            String maNV = tblHoaDon.getValueAt(selectedRow, 2).toString();
            String diaChi;
            String sdt;

            String tenKhachHang = tblHoaDon.getValueAt(selectedRow, 4).toString();
            if (tenKhachHang.equalsIgnoreCase("Khách hàng vãng lai")) {
                tenKhachHang = "Khách hàng vãng lai";
                diaChi = " ";
                sdt = " ";
            } else {
                diaChi = tblHoaDon.getValueAt(selectedRow, 5).toString();
                sdt = tblHoaDon.getValueAt(selectedRow, 6).toString();
            }
            String ngayTao = tblHoaDon.getValueAt(selectedRow, 7).toString();

            // Create a table with customer details
            PdfPTable customerTable = new PdfPTable(4);
            customerTable.setWidthPercentage(100);
            customerTable.setSpacingBefore(10f);
            customerTable.setSpacingAfter(10f);

            float[] customerColumnWidths = {1f, 2f, 1f, 2f};
            customerTable.setWidths(customerColumnWidths);

            PdfPCell cell;

            cell = new PdfPCell(new Paragraph("Mã HĐ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(maHoaDon, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Thu Ngân", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(maNV, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Tên Khách", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(tenKhachHang, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Ngày Tạo", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(ngayTao, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Địa chỉ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(diaChi, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

// Empty cell without border
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("SDT", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(sdt, regularFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

// Empty cell without border
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(PdfPCell.NO_BORDER);
            customerTable.addCell(cell);

            doc.add(customerTable);

            doc.add(new Paragraph(" ")); // Add a blank line for spacing

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 3f, 2f, 2f};
            table.setWidths(columnWidths);

            table.addCell(new PdfPCell(new Paragraph("STT", regularFont)));
            table.addCell(new PdfPCell(new Paragraph("Tên sản phẩm", regularFont)));
            table.addCell(new PdfPCell(new Paragraph("Số lượng", regularFont)));
            table.addCell(new PdfPCell(new Paragraph("Tổng tiền sản phẩm", regularFont)));

            for (int i = 0; i < tblHoaDonChiTiet.getRowCount(); i++) {

                String productName = tblHoaDonChiTiet.getValueAt(i, 3).toString();
                String quantity = tblHoaDonChiTiet.getValueAt(i, 4).toString();
                String totalAmount = tblHoaDonChiTiet.getValueAt(i, 6).toString();

                table.addCell(new PdfPCell(new Paragraph(String.valueOf(i + 1), regularFont)));
                table.addCell(new PdfPCell(new Paragraph(productName, regularFont)));
                table.addCell(new PdfPCell(new Paragraph(quantity, regularFont)));
                table.addCell(new PdfPCell(new Paragraph(totalAmount, regularFont)));
            }

            doc.add(table);

            doc.add(new Paragraph(" ")); // Thêm khoảng trống

            int idVc = selectedHoaDon.getIdVoucher();
            Float thanhTien = Float.valueOf((selectedHoaDon.getTongTien()));
            Float tienDuocGiam = hdRepo.getGiaTriVoucher(idVc, thanhTien);

            String thanhTienGiam = String.valueOf(selectedHoaDon.getTongTien());
            Float tienBanDau = tienDuocGiam + Float.valueOf(thanhTienGiam);

            double tienMat = Double.parseDouble(selectedHoaDon.getTongTien() + "");
            if (tienMat == 0.0) {
                tienMat = 0.0;
            }
            String tienKhachDua = String.valueOf(tienMat);

            String phuongThucThanhToan = selectedHoaDon.getHinhThucThanhToan();

            // Format currency in VND
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            Paragraph paymentInfo = new Paragraph();
            paymentInfo.add(
                    new Paragraph("Thành Tiền: " + currencyFormat.format(tienBanDau), regularFont));
            paymentInfo.add(new Paragraph(
                    "Số tiền được giảm: " + currencyFormat.format(tienDuocGiam), regularFont));
            paymentInfo.add(new Paragraph(
                    "Thành Tiền Sau Khi Giảm: "
                    + currencyFormat.format(Float.parseFloat(thanhTienGiam)),
                    regularFont));
            paymentInfo.add(new Paragraph("Tiền Khách Đưa: " + currencyFormat.format(Double.parseDouble(tienKhachDua)),
                    regularFont));
            paymentInfo.add(new Paragraph("Phương Thức Thanh Toán: " + phuongThucThanhToan, regularFont));
            doc.add(paymentInfo);

            doc.add(new Paragraph(" ")); // Thêm khoảng trống
            doc.add(new Paragraph(
                    "----------------------------------------------------------------------------------------------------------------------------------",
                    regularFont));

            // Thêm QR code
            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(maHoaDon, BarcodeFormat.QR_CODE, 200, 200);

                ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                byte[] pngData = pngOutputStream.toByteArray();
                Image qrCode = Image.getInstance(pngData);
                qrCode.setAlignment(Element.ALIGN_CENTER);
                qrCode.scaleAbsolute(160, 160);
                doc.add(qrCode);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }

            // Thêm lời cảm ơn
            Paragraph thankYou = new Paragraph("THANK YOU COME AGAIN\n"
                    + "★★★★**★★★★★★★★＊★★★★★\n"
                    + "SOFTWARE BY: GROUP 6\n"
                    + "CONTACT: group6@SD19304.com", regularFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            doc.add(thankYou);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tạo file PDF!");
        } finally {
            doc.close();
        }

        JOptionPane.showMessageDialog(this, "In hóa đơn thành công!");
    }//GEN-LAST:event_btnInHoaDonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JComboBox<String> cbbHinhThucTT;
    private javax.swing.JComboBox<String> cbbLocTrangThai;
    private javax.swing.JComboBox<String> cbbNam;
    private javax.swing.JComboBox<String> cbbThang;
    private javax.swing.JComboBox<String> cbbTongTien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private swing.PanelRound panelRound1;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblHoaDonChiTiet;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
