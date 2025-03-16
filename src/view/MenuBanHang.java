/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import com.github.sarxos.webcam.Webcam;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.MultiFormatOneDReader;

import entity.HoaDon;
import entity.KhachHang;
import entity.SanPhamChiTiet;
import entity.Voucher;
import entity.XuatXu;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import logic.RandomStringGenerator;
import org.apache.poi.ss.formula.ptg.TblPtg;
import raven.toast.Notifications;
import repository.BanHangRepo;
import repository.HDRepository;
import repository.NhanVienRepo;
import repository.SanPhamChiTietRepo;
import repository.XuatXuRepo;
import repository.repo_voucher;

import request.HoaDonRequest;
import response.GioHangResp;
import response.HoaDonResp;
import response.SanPhamChiTietResp;
import response.NhanVienResp;
import view.ViewLogin;

public class MenuBanHang extends javax.swing.JInternalFrame implements Runnable, ThreadFactory {

    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);

    private BanHangRepo repo;
    private HDRepository hd_repo;
    private SanPhamChiTietRepo spct_repo;
    private NhanVienResp nvResp;

    private DefaultTableModel dtm;
    private Date today = new Date();

    private String maHd;
    private String maHdCt;
    private String maSpct;
    private KhachHang selectedKH = new KhachHang();

    private List<GioHangResp> listGH;
    private List<HoaDonResp> listHD;
    private List<SanPhamChiTietResp> listSP;
    private Float tongTienT;
    private repo_voucher repoVc;
    private XuatXuRepo xuatXuRepo;
    private int row;

    public MenuBanHang() {
        initComponents();
        initWebcam();

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        resultField.setVisible(false);

        xuatXuRepo = new repository.XuatXuRepo();
        repoVc = new repo_voucher();
        repo = new BanHangRepo();
        hd_repo = new HDRepository();

        fillTableHd(repo.getAllHd());
        fillTableSp(repo.getAll());
        addDocumentListenerToResultField();
        seacht();
        tienThua();
        loadCboXx(xuatXuRepo.getAll());
        loadCboHinhThucThanhToan();

        // Thêm ListSelectionListener cho bảng hóa đơn
        tblHoaDonCho.getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event
                    ) {
                        if (!event.getValueIsAdjusting()) {
                            int selectedRow = tblHoaDonCho.getSelectedRow();
                            if (selectedRow != -1) {
                                maHd = tblHoaDonCho.getValueAt(selectedRow, 1).toString(); // Giả sử mã hóa đơn ở cột 1
                                onSelectHoaDon();
                            }
                        }
                    }
                }
                );
    }

    public void loadCboXx(List<XuatXu> list) {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) cbxXuatXu.getModel();
        defaultComboBoxModel.addElement("");
        for (XuatXu xx : list) {
            defaultComboBoxModel.addElement(xx);
        }
    }

    public void loadCboHinhThucThanhToan() {
        cboHinhThucThanhToan.addItem("Tiền mặt");
        cboHinhThucThanhToan.addItem("Chuyển khoản");
    }

    private void fillTableHd(List<HoaDonResp> list) {
        dtm = (DefaultTableModel) tblHoaDonCho.getModel();
        dtm.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        list.forEach(s -> dtm.addRow(new Object[]{
            index.getAndIncrement(), s.getMaHoaDon(), s.getNgayTao(), s.getMaNhanVien(), s.getTenKhachHang()
        }));
    }

    private void fillTableGh(List<GioHangResp> list) {
        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        list.forEach(s -> dtm.addRow(new Object[]{
            index.getAndIncrement(), s.getMasp(), s.getTensp(), s.getDonGia(), s.getSoLuong(), s.getTongTien()
        }));
    }

    private void fillTableSp(List<SanPhamChiTietResp> list) {
        dtm = (DefaultTableModel) tblSanPham.getModel();
        dtm.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        list.forEach(s -> dtm.addRow(new Object[]{
            index.getAndIncrement(), s.getMa_san_pham_chi_tiet(), s.getTen_san_pham_chi_tiet(), s.getDon_gia(), s.getTenMau(), s.getKichThuoc(), s.getTenChatLieu(), s.getTenDa(), s.getTenNuoc(), s.getSo_luong()
        }));
    }

//    private HoaDonRequest detail() {
//        HoaDonRequest hd = new HoaDonRequest();
//        hd.setMaHoaDon(maHd);
//        hd.setNgayTao(today);
//        hd.setIdNhanVien(ViewLogin.manv);
//        hd.setTenKhachHang(tenKhachHang.getText());
//        return hd;
//    }
    private HoaDonRequest detail() {
        HoaDonRequest hd = new HoaDonRequest();
        hd.setMaHoaDon(maHd); // Mã hóa đơn phải được tạo trước đó
        hd.setNgayTao(today);
        hd.setIdNhanVien(ViewLogin.manv);
        hd.setTenKhachHang(tenKhachHang.getText());
        return hd;
    }

    private void showVoucher() {
        cbxVoucher.removeAllItems();
        cbxVoucher.addItem("Chọn voucher");

        try {
            double tongTien = Double.parseDouble(lbltongTien.getText()); // Lấy tổng tiền của hóa đơn
            System.out.println("Tổng tiền: " + tongTien); // In tổng tiền ra console

            List<Voucher> vouchers = repoVc.getListVoucherFromDb();
            System.out.println("Số lượng voucher: " + vouchers.size()); // In số lượng voucher ra console

            for (Voucher vc : vouchers) {

                if (vc.getSoLuongVoucher() > 0 && vc.getNgayBatDau().before(today) && vc.getNgayKetThuc().after(today)) {

                    if (tongTien >= vc.getGioiHanGiamToiThieu()) {
                        cbxVoucher.addItem(vc.getMaVoucher());

                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi chuyển đổi tổng tiền: " + e.getMessage());
        }
    }

    public void getFromBanHang(String ma) {
        GioHangResp gh = repo.BanHang(ma);

        if (gh == null) {
            clearFields();
            lbltongTien.setText("0"); // Đặt giá trị 0 cho lbltongTien khi gh là null
            return;
        }

        System.out.println("Giỏ hàng: " + gh);
        maHoaDon.setText(maHd);

        try {
            String thanhToan = String.valueOf(gh.getTongTien());
            String tongTien = String.valueOf(gh.getTongTien());
            if (thanhToan.equalsIgnoreCase("null")) {
                thanhToan = "0.0";
            }
            if (tongTien.equalsIgnoreCase("null")) {
                tongTien = "0.0";
            }

            lblThanhToan.setText(thanhToan);
            lbltongTien.setText(tongTien);
        } catch (IllegalArgumentException e) {
            clearFields();
        }
    }

//    public void getFromBanHang(String ma) {
//        GioHangResp gh = repo.BanHang(ma);
//        int index = tblHoaDonCho.getSelectedRow();
//
//        if (gh == null) {
//            clearFields();
//            
//            return;
//        }
//
//        System.out.println("Giỏ hàng: " + gh);
//        maHoaDon.setText(maHd);
//
//        try {
//            lblThanhToan.setText(String.valueOf(gh.getTongTien()));
//            lbltongTien.setText(String.valueOf(gh.getTongTien()));
//            
//            String tenKh = tblHoaDonCho.getValueAt(index, 4).toString();
//            if(tenKh.equalsIgnoreCase("Khách hàng vãng lai")){
//                lblMaKH.setText(" ");
//                tenKhachHang.setText("Khách hàng vãng lai");
//            }else{
//                lblMaKH.setText(selectedKH.getMaKH());
//                tenKhachHang.setText(selectedKH.getTenKH());
//            }
//            
//        } catch (IllegalArgumentException e) {
//            clearFields();
//            System.out.println("catch");
//        }
//    }
    private void clearFields() {
        lbltongTien.setText("");
        lblThanhToan.setText("");
        txtTienKhachDua.setText("");
        lblTienThua.setText("");
    }

    private void seacht() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleDocumentChange(txtSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleDocumentChange(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleDocumentChange(txtSearch.getText());
            }

            private void handleDocumentChange(String key) {
                try {
                    fillTableSp(repo.search(key));
                } catch (Exception e) {
                    fillTableSp(repo.getAll());
                }

            }
        });
    }

    private void tienThua() {
        txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                calculateChange();
            }

            public void removeUpdate(DocumentEvent e) {
                calculateChange();
            }

            public void insertUpdate(DocumentEvent e) {
                calculateChange();
            }

            public void calculateChange() {
                try {
                    double total = Double.parseDouble(txtTienKhachDua.getText());
                    double given = Double.parseDouble(lblThanhToan.getText());
                    double change = total-given;
                    lblTienThua.setText(String.valueOf(change));
                } catch (NumberFormatException e) {
                    lblTienThua.setText("0");
                }
            }
        });

    }

    private void addDocumentListenerToResultField() {
        resultField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleDocumentChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleDocumentChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleDocumentChange();
            }

            private void handleDocumentChange() {
                String text = resultField.getText().trim();
                if (!text.isEmpty()) {
                    quet(text);
                }
            }

            private void quet(String maSpct) {
                if (maHd == null) {
                    JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn hóa đơn!");
                } else {
                    String soLuong = JOptionPane.showInputDialog("Nhập số lượng muốn mua");
                    if (soLuong == null || soLuong.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(rootPane, "Số lượng mua không hợp lệ!");
                        return;
                    }

                    try {
                        int soLuongInt = Integer.parseInt(soLuong.trim());
                        int so = repo.getSoLuongTon(maSpct);

                        System.out.println("Số lượng tồn: " + so);
                        System.out.println("Số lượng mua: " + soLuongInt);
                        if (so >= soLuongInt) {
                            System.out.println("Số lượng tồn: " + so + "\nSố lượng mua: " + soLuongInt);
                            if (repo.check(maSpct, maHd)) {
                                int idHD = repo.getIdHD(maHd);
                                repo.updateSoLuong(maSpct, soLuongInt, idHD);
                                listGH = repo.listBanHang(maHd);
                                fillTableGh(listGH);
                                listHD = repo.getAllHd();
                                fillTableHd(listHD);
                                listSP = repo.getAll();
                                fillTableSp(listSP);
                            } else {
                                System.out.println("Else");
                                int idSP = repo.getIdSP(maSpct);
                                int idHD = repo.getIdHD(maHd);
                                repo.themSoLuong(idSP, idHD, soLuongInt, maHdCt, maSpct);
                                listGH = repo.listBanHang(maHd);
                                fillTableGh(listGH);
                                listHD = repo.getAllHd();
                                fillTableHd(listHD);
                                listSP = repo.getAll();
                                fillTableSp(listSP);
                                getFromBanHang(maHd);
                            }
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Số lượng hàng không đủ!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(rootPane, "Số lượng không hợp lệ!");
                    }
                }
            }
        });
    }

    // Phương thức để tính tổng tiền
    private void updateTongTien() {
        double tongTien = 0.0;
        // Tính tổng tiền từ giỏ hàng
        for (GioHangResp item : listGH) {
            tongTien += item.getDonGia() * item.getSoLuong();
        }
        lbltongTien.setText(String.format("%.2f", tongTien));
    }

// Phương thức được gọi khi chọn một hóa đơn
    private void onSelectHoaDon() {
        // Giả sử listGH là danh sách giỏ hàng của hóa đơn được chọn
        listGH = repo.listBanHang(maHd); // Ví dụ lấy giỏ hàng theo mã hóa đơn

        // Cập nhật bảng giỏ hàng
        fillTableGh(listGH);

        // Cập nhật tổng tiền và hiển thị voucher
        updateTongTien();
        showVoucher();
    }

    private void clearForm() {
        lblMaKH.setText(" ");
        tenKhachHang.setText("Khách hàng vãng lai");
        maHoaDon.setText("Vui lòng tạo!");
        lbltongTien.setText("0.0");
        cbxVoucher.setSelectedIndex(0);
        lblThanhToan.setText("0.0");
        txtTienKhachDua.setText("0.0");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        panelCam = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        btnXoaSanPham = new com.raven.swing.Button();
        btnXoaTatCa = new com.raven.swing.Button();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        cbxXuatXu = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnHuyHoaDon = new com.raven.swing.Button();
        btnReset = new com.raven.swing.Button();
        btnThanhToan = new com.raven.swing.Button();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnChonKh = new javax.swing.JButton();
        btnThayDoiKh = new javax.swing.JButton();
        tenKhachHang = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnTaoHoaDon = new javax.swing.JButton();
        txtTienKhachDua = new javax.swing.JTextField();
        cboHinhThucThanhToan = new javax.swing.JComboBox<>();
        lbltongTien = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
        lblThanhToan = new javax.swing.JLabel();
        lblTienThua = new javax.swing.JLabel();
        maHoaDon = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cbxVoucher = new javax.swing.JComboBox<>();
        resultField = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1330, 800));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Hóa đơn chờ");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 86, 20);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã hóa đơn", "Ngày tạo", "Nhân viên tạo", "Khách hàng"
            }
        ));
        tblHoaDonCho.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblHoaDonChoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblHoaDonCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonChoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDonCho);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 630, 150);

        panelCam.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout panelCamLayout = new javax.swing.GroupLayout(panelCam);
        panelCam.setLayout(panelCamLayout);
        panelCamLayout.setHorizontalGroup(
            panelCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
        );
        panelCamLayout.setVerticalGroup(
            panelCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
        );

        getContentPane().add(panelCam);
        panelCam.setBounds(660, 30, 210, 130);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Đơn hàng");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(890, 10, 125, 20);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Giỏ hàng");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 190, 60, 20);

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng tiền"
            }
        ));
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblGioHang);

        btnXoaSanPham.setBackground(new java.awt.Color(255, 204, 102));
        btnXoaSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/change.png"))); // NOI18N
        btnXoaSanPham.setText("Sửa số lượng");
        btnXoaSanPham.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSanPhamActionPerformed(evt);
            }
        });

        btnXoaTatCa.setBackground(new java.awt.Color(255, 204, 102));
        btnXoaTatCa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        btnXoaTatCa.setText("Xóa tất cả");
        btnXoaTatCa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTatCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoaSanPham, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaTatCa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnXoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnXoaTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(10, 210, 860, 194);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Danh sách sản phẩm ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 420, 140, 20);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Tìm kiếm sản phẩm:");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm chi tiết", "Tên sản phẩm chi tiết ", "Đơn giá", "Màu sắc", "Kích thước ", "Chất liệu ", "Tên đá", "Xuất sứ", "Số lượng tồn"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblSanPham);

        cbxXuatXu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxXuatXuMouseClicked(evt);
            }
        });
        cbxXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxXuatXuActionPerformed(evt);
            }
        });

        jLabel15.setText("Xuất xứ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(318, 318, 318)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(cbxXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(12, 447, 860, 320);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Quét mã sản phẩm");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(670, 10, 125, 20);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Mã hóa đơn:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Tổng tiền:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Giảm giá:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Thanh toán:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Tiền khách đưa:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Tiền thừa trả khách:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Hình thức thanh toán:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Ghi chú:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        btnHuyHoaDon.setBackground(new java.awt.Color(255, 204, 102));
        btnHuyHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel02.png"))); // NOI18N
        btnHuyHoaDon.setText("Hủy hóa đơn ");
        btnHuyHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHuyHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyHoaDonActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(255, 204, 102));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refrech.png"))); // NOI18N
        btnReset.setText("Làm mới");
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(255, 204, 102));
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/payment-method.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Mã khách hàng:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Tên khách hàng:");

        btnChonKh.setBackground(new java.awt.Color(255, 204, 51));
        btnChonKh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChonKh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-pet-tray-14.png"))); // NOI18N
        btnChonKh.setText("Chọn");
        btnChonKh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKhActionPerformed(evt);
            }
        });

        btnThayDoiKh.setBackground(new java.awt.Color(255, 204, 51));
        btnThayDoiKh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThayDoiKh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-change-14.png"))); // NOI18N
        btnThayDoiKh.setText("Thay đổi");
        btnThayDoiKh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThayDoiKhActionPerformed(evt);
            }
        });

        tenKhachHang.setText("Khách hàng vãng lai");

        lblMaKH.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(tenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThayDoiKh)
                    .addComponent(btnChonKh))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(btnChonKh)
                    .addComponent(lblMaKH))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(btnThayDoiKh)
                    .addComponent(tenKhachHang))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jLabel18.setText("VNĐ");

        jLabel19.setText("VNĐ");

        jLabel20.setText("VNĐ");

        jLabel21.setText("VNĐ");

        jLabel22.setText("VNĐ");

        btnTaoHoaDon.setBackground(new java.awt.Color(255, 204, 51));
        btnTaoHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaoHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Properties.png"))); // NOI18N
        btnTaoHoaDon.setText("Tạo hóa đơn");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        lbltongTien.setText("0");

        lblGiamGia.setText("0");

        lblThanhToan.setText("0");

        lblTienThua.setText("0");

        maHoaDon.setText("Vui lòng tạo!");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Voucher:");

        cbxVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxVoucherMouseClicked(evt);
            }
        });
        cbxVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVoucherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel7)
                                .addGap(32, 32, 32)
                                .addComponent(lbltongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel8))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(160, 160, 160)
                                        .addComponent(jLabel21))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(cbxVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(maHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTaoHoaDon))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(btnHuyHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(174, 367, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(11, 11, 11)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(149, 149, 149)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel22)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(cboHinhThucThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel18))
                            .addComponent(lblGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btnTaoHoaDon)
                    .addComponent(maHoaDon))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel21)
                    .addComponent(lbltongTien))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cbxVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel22)
                    .addComponent(lblGiamGia))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel20)
                    .addComponent(lblThanhToan))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel19)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lblTienThua)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cboHinhThucThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5);
        jPanel5.setBounds(890, 30, 430, 740);

        resultField.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                resultFieldHierarchyChanged(evt);
            }
        });
        resultField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                resultFieldCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                resultFieldInputMethodTextChanged(evt);
            }
        });
        resultField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultFieldActionPerformed(evt);
            }
        });
        resultField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                resultFieldPropertyChange(evt);
            }
        });
        resultField.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                resultFieldVetoableChange(evt);
            }
        });
        getContentPane().add(resultField);
        resultField.setBounds(660, 180, 210, 22);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonActionPerformed
        // TODO add your handling code here:
//        int i = JOptionPane.showConfirmDialog(this, "Bạn có muốn tạo hóa đơn không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//        int rowCount = tblHoaDonCho.getRowCount();
//        if (i == 0) {
//            if (rowCount > 5) {
//                JOptionPane.showMessageDialog(this, "Hóa đơn chờ đã đạt giới hạn!");
//            } else {
//                maHd = RandomStringGenerator.generateRandomString("HD");
////                maHdCt = RandomStringGenerator.generateRandomString("HDCT");
//                System.out.println("tạo mahdct " + maHdCt);
//                repo.add(detail());
//
//                fillTableHd(repo.getAllHd());
//                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
//            }
//        }

        lblMaKH.setText(" ");
        tenKhachHang.setText("Khách hàng vãng lai");
        int i = JOptionPane.showConfirmDialog(this, "Bạn có muốn tạo hóa đơn không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        int rowCount = tblHoaDonCho.getRowCount();

        if (i == JOptionPane.YES_OPTION) {
            if (rowCount > 5) {
                JOptionPane.showMessageDialog(this, "Hóa đơn chờ đã đạt giới hạn!");
            } else {
                maHd = RandomStringGenerator.generateRandomString("HD");

                // Tạo đối tượng HoaDonRequest với thông tin cần thiết
                HoaDonRequest hd = detail();
                hd.setMaHoaDon(maHd);

                // Thực hiện thêm hóa đơn và nhận ID hóa đơn mới
                int idHoaDon = repo.add(hd);

                if (idHoaDon != -1) {
                    // Tạo mã hóa đơn chi tiết
                    maHdCt = RandomStringGenerator.generateRandomString("HDCT");
                    System.out.println("Tạo mã hóa đơn chi tiết: " + maHdCt);

                    // Thêm hóa đơn chi tiết vào cơ sở dữ liệu
                    boolean added = repo.addHoaDonChiTiet(maHdCt, idHoaDon);

                    if (added) {
                        // Cập nhật bảng hóa đơn
                        fillTableHd(repo.getAllHd());
                        lblMaKH.setText(" ");
                        tenKhachHang.setText("Khách hàng vãng lai");
                        JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi thêm hóa đơn chi tiết!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn!");
                }
            }
        }


    }//GEN-LAST:event_btnTaoHoaDonActionPerformed

    private void tblHoaDonChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMouseClicked
        // TODO add your handling code here:
//        int index = tblHoaDonCho.getSelectedRow();
//        maHd = tblHoaDonCho.getValueAt(index, 1).toString();
//        fillTableGh(repo.listBanHang(maHd));
//        getFromBanHang(maHd);       
//        cbxVoucher.setSelectedIndex(0);
//        lblGiamGia.setText("");
////        maHdCt = repo.getMaHdCtByMaHd(maHd);
////        if(maHdCt==null){
////            JOptionPane.showMessageDialog(this, "Không tìm thấy mã hóa đơn chi tiết!");
////        }
//        System.out.println("Mã hóa đơn chi tiết: " + maHdCt);
//        System.out.println("Mã hóa đơn: " + maHd);

        int index = tblHoaDonCho.getSelectedRow();
        maHd = tblHoaDonCho.getValueAt(index, 1).toString();
        maHdCt = repo.getMaHdCtByMaHd(maHd); // Cập nhật maHdCt khi chọn hóa đơn
        fillTableGh(repo.listBanHang(maHd));
        getFromBanHang(maHd);

        if (cbxVoucher.getItemCount() > 0) {
            cbxVoucher.setSelectedIndex(0);
        }
        lblGiamGia.setText("");

        String tenKh = tblHoaDonCho.getValueAt(index, 4).toString();
        if (tenKh.equalsIgnoreCase("Khách hàng vãng lai")) {
            lblMaKH.setText(" ");
            tenKhachHang.setText("Khách hàng vãng lai");
        } else {
            KhachHang kh = repo.getKhByMaHd(maHd);
            if (kh != null) {
                lblMaKH.setText(kh.getMaKH());
                tenKhachHang.setText(kh.getTenKH());
            } else {
                lblMaKH.setText(" ");
                tenKhachHang.setText("Khách hàng vãng lai");
                // Có thể thêm thông báo lỗi hoặc log lỗi ở đây nếu cần
            }
        }

    }//GEN-LAST:event_tblHoaDonChoMouseClicked

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        // TODO add your handling code here:
        if (maHd == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
        } else {
            String soLuong = JOptionPane.showInputDialog(this, "nhập số lượng muốn mua");
            if (soLuong == null || soLuong.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số lượng sản phẩm không hợp lệ!");
                return;
            }

            try {
                int soLuongInt = Integer.parseInt(soLuong.trim());
                int index = tblSanPham.getSelectedRow();
                String maSP = (String) tblSanPham.getValueAt(index, 1);
                int so = (int) tblSanPham.getValueAt(index, 9);

                if (so >= soLuongInt) {
                    if (repo.check(maSP, maHd)) {
                        int idHD = repo.getIdHD(maHd);
                        repo.updateSoLuong(maSP, soLuongInt, idHD);
                        listGH = repo.listBanHang(maHd);
                        fillTableGh(listGH);
                        listHD = repo.getAllHd();
                        fillTableHd(listHD);
                        listSP = repo.getAll();
                        fillTableSp(listSP);
//                getFromBanHang(maHd);
                    } else {
                        int idSP = repo.getIdSP(maSP);
                        int idHD = repo.getIdHD(maHd);
                        repo.themSoLuong(idSP, idHD, soLuongInt, maHdCt, maSP);
                        listGH = repo.listBanHang(maHd);
                        fillTableGh(listGH);
                        listHD = repo.getAllHd();
                        fillTableHd(listHD);
                        listSP = repo.getAll();
                        fillTableSp(listSP);
                        getFromBanHang(maHd);
                    }
                    updateTongTien();
                    showVoucher();
                } else {
                    JOptionPane.showMessageDialog(this, "Số lượng hàng không đủ");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            }

        }

    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void tblGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseClicked
        // TODO add your handling code here:
//        int index = tblGioHang.getSelectedRow();
//        getFromBanHang(index);

    }//GEN-LAST:event_tblGioHangMouseClicked

    private void btnXoaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSanPhamActionPerformed
        // TODO add your handling code here:

//        int index = tblGioHang.getSelectedRow();
//        if (index < 0) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
//            return;
//        }
//        String soLuong = JOptionPane.showInputDialog(this, "Nhập số lượng xóa");
//        if (soLuong == null || soLuong.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm không hợp lệ!");
//            return;
//        }
//        try {
//            int soLuongInt = Integer.parseInt(soLuong.trim());
//            String maSP = (String) tblGioHang.getValueAt(index, 1);
//            int so = (int) tblGioHang.getValueAt(index, 4);
//            if (so >= soLuongInt) {
//                if (repo.check(maSP, maHd)) {
//                    int idHD = repo.getIdHD(maHd);
//                    repo.UpdateSlSanPham(maSP, soLuongInt, idHD);
//                    listGH = repo.listBanHang(maHd);
//                    fillTableGh(listGH);
//                    listHD = repo.getAllHd();
//                    fillTableHd(listHD);
//                    listSP = repo.getAll();
//                    fillTableSp(listSP);
//                    getFromBanHang(maHd);
//
//                    updateTongTien();
//                    showVoucher();
//
//                } else {
//                    JOptionPane.showMessageDialog(this, "Số lượng xóa không hợp lệ");
//                }
//
//            }else{
//                JOptionPane.showMessageDialog(this, "Số lượng trong giỏ hàng không đủ!;");
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
//        }
        int index = tblGioHang.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }
        String soLuong = JOptionPane.showInputDialog(this, "Nhập số lượng xóa");
        if (soLuong == null || soLuong.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm không hợp lệ!");
            return;
        }
        try {
            int soLuongInt = Integer.parseInt(soLuong.trim());
            String maSP = (String) tblGioHang.getValueAt(index, 1);
            int so = (int) tblGioHang.getValueAt(index, 4);
            if (so >= soLuongInt) {
                if (repo.check(maSP, maHd)) {
                    int idHD = repo.getIdHD(maHd);
                    repo.UpdateSlSanPham(maSP, soLuongInt, idHD);
                    listGH = repo.listBanHang(maHd);
                    fillTableGh(listGH);
                    listHD = repo.getAllHd();
                    fillTableHd(listHD);
                    listSP = repo.getAll();
                    fillTableSp(listSP);
                    getFromBanHang(maHd);

                    updateTongTien();
                    showVoucher();
                } else {
                    JOptionPane.showMessageDialog(this, "Số lượng xóa không hợp lệ");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng trong giỏ hàng không đủ!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
        }


    }//GEN-LAST:event_btnXoaSanPhamActionPerformed

    private void btnXoaTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTatCaActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa tất cả sản phẩm trong giỏ hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            int idHD = repo.getIdHD(maHd);
            repo.XoaAllGioHang(idHD);
            listGH = repo.listBanHang(maHd);
            fillTableGh(listGH);
            listHD = repo.getAllHd();
            fillTableHd(listHD);
            listSP = repo.getAll();
            fillTableSp(listSP);
            getFromBanHang(maHd);
            updateTongTien();
            showVoucher();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }

    }//GEN-LAST:event_btnXoaTatCaActionPerformed

    private void resultFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_resultFieldInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_resultFieldInputMethodTextChanged

    private void resultFieldHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_resultFieldHierarchyChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_resultFieldHierarchyChanged

    private void resultFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_resultFieldPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_resultFieldPropertyChange

    private void resultFieldCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_resultFieldCaretPositionChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_resultFieldCaretPositionChanged

    private void resultFieldVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_resultFieldVetoableChange
        // TODO add your handling code here:

    }//GEN-LAST:event_resultFieldVetoableChange

    private void resultFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultFieldActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_resultFieldActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSearchActionPerformed

    private void cbxVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxVoucherMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cbxVoucherMouseClicked

    private void cbxVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVoucherActionPerformed
        // TODO add your handling code here:
//        try {
//            String mavc = cbxVoucher.getSelectedItem().toString();
//
//            Voucher vc = repoVc.getMotVc(mavc);
//            Voucher vcHt = repoVc.getHinhThucGiam(mavc);
//
//            if (vc.getHinhThucGiam().equalsIgnoreCase(vcHt.getHinhThucGiam())) {
//
//                double tt = Double.parseDouble(lbltongTien.getText());
//                double phanTram = vc.getMucGia();
//                double gg = (tt * phanTram) / 100;
//
//                if (gg > vc.getGioiHanGiamToiDa()) {
//                    gg = vc.getGioiHanGiamToiDa();
//                }
//
//                double thanhTien = tt - gg;
//                lblGiamGia.setText(String.valueOf(gg));
//                lblThanhToan.setText(String.valueOf(thanhTien));
//            } else {
//                // Xử lý nếu các hình thức giảm giá không khớp
//            }
//
//        } catch (Exception e) {
//            lblThanhToan.setText(lbltongTien.getText());
//        }

        try {
            String mavc = cbxVoucher.getSelectedItem().toString();

            if (!mavc.equals("Chọn voucher")) {
                Voucher vc = repoVc.getMotVc(mavc);
                Voucher vcHt = repoVc.getHinhThucGiam(mavc);

                if (vc.getHinhThucGiam().equalsIgnoreCase(vcHt.getHinhThucGiam())) {
                    double tt = Double.parseDouble(lbltongTien.getText());
                    double phanTram = vc.getMucGia();
                    double gg = (tt * phanTram) / 100;

                    if (gg > vc.getGioiHanGiamToiDa()) {
                        gg = vc.getGioiHanGiamToiDa();
                    }

                    double thanhTien = tt - gg;
                    lblGiamGia.setText(String.format("%.2f", gg));
                    lblThanhToan.setText(String.format("%.2f", thanhTien));
                } else {
                    // Xử lý nếu các hình thức giảm giá không khớp
                    lblGiamGia.setText("0");
                    lblThanhToan.setText(lbltongTien.getText());
                }
            } else {
                lblGiamGia.setText("0");
                lblThanhToan.setText(lbltongTien.getText());
            }
        } catch (Exception e) {
            lblGiamGia.setText("0");
            lblThanhToan.setText(lbltongTien.getText());
        }

    }//GEN-LAST:event_cbxVoucherActionPerformed

    private void tblHoaDonChoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblHoaDonChoAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHoaDonChoAncestorAdded

    private void cbxXuatXuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxXuatXuMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cbxXuatXuMouseClicked

    private void cbxXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxXuatXuActionPerformed
        // TODO add your handling code here:
        XuatXu xx = new XuatXu();
        try {

            xx = (XuatXu) cbxXuatXu.getSelectedItem();
            List<SanPhamChiTietResp> list = repo.locXuatXu(xx.getId());
            fillTableSp(list);
        } catch (Exception e) {
            fillTableSp(repo.getAll());
        }
    }//GEN-LAST:event_cbxXuatXuActionPerformed

    private void btnHuyHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHoaDonActionPerformed
        // TODO add your handling code here:
        int index = tblHoaDonCho.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để hủy!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maHD = (String) tblHoaDonCho.getValueAt(index, 1);
                int idHD = repo.getIdHD(maHD);
                repo.huyHoaDon(idHD, maHD);
                lblMaKH.setText(" ");
                tenKhachHang.setText("Khách hàng vãng lai");
                JOptionPane.showMessageDialog(this, "Hủy hóa đơn thành công!");
                fillTableHd(repo.getAllHd());
                fillTableGh(repo.listBanHang(maHd));
                listSP = repo.getAll();
                fillTableSp(listSP);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi hủy hóa đơn!");
            }
        }

    }//GEN-LAST:event_btnHuyHoaDonActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        int index = tblHoaDonCho.getSelectedRow();

        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để thanh toán!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thanh toán hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maHD = (String) tblHoaDonCho.getValueAt(index, 1);
                double tongTien = Double.parseDouble(lbltongTien.getText());
                double giamGia = 0;
                try {
                    giamGia = Double.parseDouble(lblGiamGia.getText());
                } catch (Exception e) {
                    giamGia = 0;
                }
                double thanhToan = Double.parseDouble(lblThanhToan.getText());

                String hinhThucThanhToan = (String) cboHinhThucThanhToan.getSelectedItem();
                String maVc = (String) cbxVoucher.getSelectedItem();

                Integer idVc = null;
                if (!maVc.equalsIgnoreCase("Chọn voucher")) {
                    idVc = repo.getIdVocher(maVc);
                }

                double tienKhachDua = 0;
                try {
                    tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Số tiền khách đưa không hợp lệ!");
                    return;
                }

                if (tienKhachDua < thanhToan) {
                    JOptionPane.showMessageDialog(this, "Số tiền khách đưa không đủ để thanh toán!");
                    return;
                }

                // Tính tiền thừa trả lại cho khách
                double tienThuaTraKhach = tienKhachDua - thanhToan;
                lblTienThua.setText(String.format("%.2f", tienThuaTraKhach)); // Hiển thị tiền thừa trên giao diện (nếu cần)

                // Tiến hành thanh toán
                if (idVc == null) {
                    repo.thanhToanHoaDonKoVc(thanhToan, hinhThucThanhToan, maHD);
                    System.out.println("Không chọn vc");
                } else {
                    repo.thanhToanHoaDonCoVc(thanhToan, hinhThucThanhToan, idVc, maHD, maVc);
//                    ,idKh,diaChi,sdt
                }

//                lbltongTien.setText("");
//                lblThanhToan.setText("");
//                txtTienKhachDua.setText("");
//                lblTienThua.setText("");
                clearForm();
                JOptionPane.showMessageDialog(this, "Thanh toán hóa đơn thành công!");
                fillTableHd(repo.getAllHd());
                fillTableGh(repo.listBanHang(maHD));
                listSP = repo.getAll();
                fillTableSp(listSP);

//                lblMaKH.setText("");
//                tenKhachHang.setText("Khách hàng vãng lai");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thanh toán hóa đơn!");
            }
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnChonKhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKhActionPerformed
        // TODO add your handling code here:
        row = tblHoaDonCho.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần thay đổi khách hàng");
        } else {
            try {
                ChonKhachHang chonKH = new ChonKhachHang();
                chonKH.setVisible(true);
                chonKH.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        selectedKH = chonKH.getSelectedKhachHang();

                    }
                }
                );
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gặp lỗi khi mở form chọn khách hàng");
            }

        }

    }//GEN-LAST:event_btnChonKhActionPerformed

    private void btnThayDoiKhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThayDoiKhActionPerformed
        // TODO add your handling code here:
        if (selectedKH != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn thay đổi khách hàng?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                String maHD = (String) tblHoaDonCho.getValueAt(row, 1);
                String maKh = null;
                try {
                    maKh = selectedKH.getMaKH();
                } catch (Exception e) {
                    maKh = null;
                }
                System.out.println(maKh);

                Integer idKh = null;
                String diaChi = null;
                String sdt = null;
                String tenKH = null;

                if (!maKh.trim().isEmpty()) {
                    idKh = repo.getKhByMa(maKh).getId();
                    diaChi = repo.getKhByMa(maKh).getDiaChi();
                    sdt = repo.getKhByMa(maKh).getSdt();
                    tenKH = selectedKH.getTenKH();
                }
                System.out.println(idKh);
                System.out.println(diaChi);
                System.out.println(sdt);
                System.out.println("Tên 1: " + tenKH);
                lblMaKH.setText(selectedKH.getMaKH());
                tenKhachHang.setText(selectedKH.getTenKH());
                System.out.println("Tên 2: " + selectedKH.getTenKH());

                hd_repo.UpdateKhbyMaHd(maHD, idKh, diaChi, tenKH, sdt);
                listHD = repo.getAllHd();
                fillTableHd(listHD);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng trước");
        }
    }//GEN-LAST:event_btnThayDoiKhActionPerformed

    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            if (webcam.isOpen()) {
                webcam.close();
            }
            webcam.setViewSize(new Dimension(640, 480)); // Đặt kích thước mong muốn
            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(new Dimension(640, 480));
            panel.setFPSDisplayed(true);
            panelCam.setLayout(new java.awt.BorderLayout());
            panelCam.add(panel, java.awt.BorderLayout.CENTER);
            executor.execute(this);
        } else {
            System.out.println("No webcam detected");
        }
    }

    public Webcam getWebcam() {
        return webcam;
    }

    public void closeWebcam() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);

            } catch (InterruptedException ex) {
                Logger.getLogger(MenuBanHang.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam != null) {
                if (webcam.isOpen()) {
                    image = webcam.getImage();
                } else {
                    System.out.println("Webcam is not open.");
                }
            } else {
                System.out.println("Webcam is not initialized.");
            }

            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // No QR code in the image
                }
            } else {
                System.out.println("Image is null.");
            }

            if (result != null) {
                resultField.setText(result.getText());
            }

        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonKh;
    private com.raven.swing.Button btnHuyHoaDon;
    private com.raven.swing.Button btnReset;
    private javax.swing.JButton btnTaoHoaDon;
    private com.raven.swing.Button btnThanhToan;
    private javax.swing.JButton btnThayDoiKh;
    private com.raven.swing.Button btnXoaSanPham;
    private com.raven.swing.Button btnXoaTatCa;
    private javax.swing.JComboBox<String> cboHinhThucThanhToan;
    private javax.swing.JComboBox<String> cbxVoucher;
    private javax.swing.JComboBox<String> cbxXuatXu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblThanhToan;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lbltongTien;
    private javax.swing.JLabel maHoaDon;
    private javax.swing.JPanel panelCam;
    private javax.swing.JTextField resultField;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JLabel tenKhachHang;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTienKhachDua;
    // End of variables declaration//GEN-END:variables
}
