/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import entity.ChatLieu;
import entity.Da;
import entity.KichThuoc;
import entity.MaSanPham;
import entity.MauSac;
import entity.SanPham;
import entity.SanPhamChiTiet;
import entity.XuatXu;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import logic.RandomStringGenerator;
import repository.ChatLieuRepo;
import repository.DaRepo;
import repository.KichThuocRepo;
import repository.MaSanPhamRepo;
import repository.MauSacRepo;
import repository.SanPhamChiTietRepo;
import repository.SanPhamRepo;
import repository.XuatXuRepo;
import request.SanPhamChiTietRequest;
import response.SanPhamChiTietResp;

/**
 *
 * @author DUNG
 */
public class MenuSanPham extends javax.swing.JInternalFrame {

    private DefaultTableModel dtm;
    private SanPhamRepo repo;
    private Validate vali;
    private SanPhamChiTietRepo spct_repo;
    private String path = null;
    private DefaultComboBoxModel comboBoxModel;
    private KichThuocRepo kichThuocRepo;
    private MauSacRepo mauSacRepo;
    private ChatLieuRepo chatLieuRepo;
    private DaRepo daRepo;
    private XuatXuRepo xuatXuRepo;
    private MaSanPhamRepo maSanPhamRepo;
    private Date today = new Date();
    private String ma_san_pham_chi_tiet;
    private String maSp;

    /**
     * Creates new form MenuBanHang
     */
    public MenuSanPham() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        spct_repo = new SanPhamChiTietRepo();
        vali = new Validate();
        repo = new SanPhamRepo();
        fillTable(repo.getAll());

        spct_repo = new repository.SanPhamChiTietRepo();
        chatLieuRepo = new repository.ChatLieuRepo();
        daRepo = new repository.DaRepo();
        kichThuocRepo = new repository.KichThuocRepo();
        maSanPhamRepo = new repository.MaSanPhamRepo();
        mauSacRepo = new repository.MauSacRepo();
        xuatXuRepo = new repository.XuatXuRepo();
        showDataTable_SPCT(spct_repo.getAll());

        loadCBB_KT();
        loadCBB_MS();
        loadCBB_CL();
        loadCBB_DA();
        loadCBB_XX();
        loadCBB_MSP();
        loadCBB_TT();
        loadCboMs(mauSacRepo.getAll());
        loadCboCl(chatLieuRepo.getAll());
        loadCboXx(xuatXuRepo.getAll());

        boxSearch();
        boxSearchSpct();
        txt_MaSPCT.disable();
        txtMa.disable();
        txtNgay.disable();
        txtNgay.setDate(today);

    }

//    public void loadCBB_KT() {
//        cbo_KichThuoc.removeAllItems();
//        
//        for (KichThuoc kt : kichThuocRepo.getAll()) {
//            cbo_KichThuoc.addItem(kt.getKich_thuoc().toString());
//        }
//    }
    public void loadCBB_KT() {
        cbo_KichThuoc.removeAllItems();
        List<KichThuoc> kichThuocList = kichThuocRepo.getAll();
        System.out.println("Kích thước hiện có: " + kichThuocList.size());
        for (KichThuoc kt : kichThuocList) {
            cbo_KichThuoc.addItem(kt.getKich_thuoc().toString());
            System.out.println("Đã thêm vào ComboBox: " + kt.getKich_thuoc().toString());
        }
    }

    public void loadCBB_MS() {
        cbo_MauSac1.removeAllItems();
        for (MauSac ms : mauSacRepo.getAll()) {
            cbo_MauSac1.addItem(ms.getTen_mau());
        }
    }

    public void loadCboMs(List<MauSac> list) {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbo_MauSac.getModel();
        cbo_MauSac.addItem("");
        for (MauSac cv : list) {
            System.out.println(cv);
            comboBoxModel.addElement(cv);
            //ghi đè lên đối tượng Mausac bằng tên màu dùng toString
        }

    }

    public void loadCBB_CL() {
        cbo_ChatLieu1.removeAllItems();
        for (ChatLieu cl : chatLieuRepo.getAll()) {
            cbo_ChatLieu1.addItem(cl.getTen_chat_lieu());
        }
    }

    public void loadCboCl(List<ChatLieu> list) {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbo_ChatLieu.getModel();
        cbo_ChatLieu.addItem("");
        for (ChatLieu cv : list) {
            comboBoxModel.addElement(cv);
        }
    }

    public void loadCBB_DA() {
        cbo_Da.removeAllItems();
        for (Da da : daRepo.getAll()) {
            cbo_Da.addItem(da.getTen_da());
        }
    }

    public void loadCBB_XX() {
        cbo_XuatXu1.removeAllItems();
        for (XuatXu xx : xuatXuRepo.getAll()) {
            cbo_XuatXu1.addItem(xx.getTen_nuoc());
        }
    }

    public void loadCboXx(List<XuatXu> list) {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) cbo_XuatXu.getModel();
        defaultComboBoxModel.addElement("");
        for (XuatXu xx : list) {
            defaultComboBoxModel.addElement(xx);
        }
    }

    public void loadCBB_MSP() {
        cbo_MaSanPham.removeAllItems();
        for (MaSanPham msp : maSanPhamRepo.getAll()) {
            cbo_MaSanPham.addItem(msp.getMa_san_pham());
        }
    }

    private void loadCBB_TT() {
        cbo_TrangThai.addItem("Đang kinh doanh");
        cbo_TrangThai.addItem("Ngưng kinh doanh");
    }

    private void showDataTable_SPCT(List<SanPhamChiTietResp> lists) {
        dtm = (DefaultTableModel) tbl_SPCT.getModel();
        dtm.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1);
        lists.forEach(s -> dtm.addRow(new Object[]{
            index.getAndIncrement(), s.getMa_san_pham_chi_tiet(), s.getTen_san_pham_chi_tiet(), s.getKichThuoc(),
            s.getTenMau(), s.getTenChatLieu(), s.getTenDa(), s.getTenNuoc(), s.getMaSP(), s.getSo_luong(), s.getDon_gia(),
            s.getAnh(), s.getTrang_thai() == 1 ? "Đang kinh doanh" : "Ngưng kinh doanh"
        }));
    }

    public void detail(int index) {
        SanPhamChiTietResp spct_resp = spct_repo.getAll().get(index);
        txt_MaSPCT.setText(spct_resp.getMa_san_pham_chi_tiet());
        txt_TenSPCT.setText(spct_resp.getTen_san_pham_chi_tiet());
        cbo_KichThuoc.setSelectedItem(spct_resp.getKichThuoc());
        cbo_MauSac1.setSelectedItem(spct_resp.getTenMau());
        cbo_ChatLieu1.setSelectedItem(spct_resp.getTenChatLieu());
        cbo_Da.setSelectedItem(spct_resp.getTenDa());
        cbo_XuatXu1.setSelectedItem(spct_resp.getTenNuoc());

        cbo_MaSanPham.setSelectedItem(spct_resp.getMaSP());

        txt_SoLuong.setText(spct_resp.getSo_luong().toString());
        txt_DonGia.setText(spct_resp.getDon_gia().toString());
        if (spct_resp.getAnh().equalsIgnoreCase("No Avatar")) {
            lbHinh.setText("No Avatar");
            lbHinh.setIcon(null);
        } else {
            lbHinh.setText("");

            BufferedImage b;
            //thay đổi tên file
            File file = new File("H:\\Projects_java\\DuAn01\\Du_an01c\\giaoDien01\\src\\img\\" + spct_resp.getAnh());

            try {
                b = ImageIO.read(file);
                int width = lbHinh.getWidth();
                int height = lbHinh.getHeight();
                lbHinh.setIcon(new ImageIcon(b.getScaledInstance(width, height, 0)));
            } catch (Exception e) {
            }
//            lbHinh.setText("");
//            ImageIcon imgIcon = new ImageIcon(getClass().getResource("/image/" + spct_resp.getAnh()));
//            Image img = imgIcon.getImage();
//            Image scaledImg = img.getScaledInstance(lbHinh.getWidth(), lbHinh.getHeight(), Image.SCALE_SMOOTH);
//            imgIcon = new ImageIcon(scaledImg);
//            lbHinh.setIcon(imgIcon);
        }
        cbo_TrangThai.setSelectedItem(spct_resp.getTrang_thai() == 1 ? "Đang kinh doanh" : "Ngưng kinh doanh");

    }

    private SanPhamChiTietRequest getFormData() {
        SanPhamChiTietRequest request = new SanPhamChiTietRequest();

        request.setMaSanPhamChiTiet(ma_san_pham_chi_tiet);
        request.setTenSanPhamChiTiet(txt_TenSPCT.getText());

        int index = cbo_KichThuoc.getSelectedIndex();
        request.setIdKichThuoc(kichThuocRepo.getAll().get(index).getId());

        int index1 = cbo_MauSac1.getSelectedIndex();
        request.setIdMauSac(mauSacRepo.getAll().get(index1).getId());

        int index2 = cbo_ChatLieu1.getSelectedIndex();
        request.setIdChatLieu(chatLieuRepo.getAll().get(index2).getId());

        int index3 = cbo_Da.getSelectedIndex();
        request.setIdDa(daRepo.getAll().get(index3).getId());

        int index4 = cbo_XuatXu1.getSelectedIndex();
        request.setIdXuatXu(xuatXuRepo.getAll().get(index4).getId());

        int index5 = cbo_MaSanPham.getSelectedIndex();
        request.setIdSanPham(maSanPhamRepo.getAll().get(index5).getId());

        request.setSoLuong(Integer.valueOf(txt_SoLuong.getText()));
        request.setDonGia(Float.valueOf(txt_DonGia.getText()));
        if (path == null) {
            request.setAnh("No Avatar");
        } else {
            request.setAnh(path);
        }

        request.setTrangThai(cbo_TrangThai.getSelectedItem().equals("Hết sản phẩm") ? 0 : 1);
        return request;

    }

    private void fillTable(List<SanPham> lists) {
        dtm = (DefaultTableModel) tbl_SanPham.getModel();
        dtm.setRowCount(0);

        AtomicInteger index = new AtomicInteger(1);
        lists.forEach(s -> dtm.addRow(new Object[]{
            index.getAndIncrement(), s.getMaSanPham(), s.getTenSanPham(),
            s.getNgayTao(), s.getTrangThai() == 1 ? "Đang kinh doanh" : "Ngưng kinh doanh"
        }));
    }

    private void fillTableToForm(int index) {
        SanPham sp = repo.getAll().get(index);
        txtMa.setText(sp.getMaSanPham());
        txtTen.setText(sp.getTenSanPham());
        txtNgay.setDate(new Date(sp.getNgayTao().getTime()));
        if (sp.getTrangThai() == 1) {
            rdoHd.setSelected(true);
        } else {
            rdoNgungHd.setSelected(true);
        }
    }

//    public SanPham getDataToForm() {
//        return new SanPham(txtMa.getText(),
//                txtTen.getText(),
//                txtNgay.getDate(),
//                rdoHd.isSelected() ? 1 : 0
//        );
//    }
    public SanPham getDataToForm() {
        SanPham sp = new SanPham();
        sp.setMaSanPham(maSp);
        sp.setTenSanPham(txtTen.getText());
        sp.setNgayTao(txtNgay.getDate());
        sp.setTrangThai(rdoHd.isSelected() ? 1 : 0);
        return sp;
    }

    private boolean kiemTra() {
        if (txtTen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            return false;
        } else if (!vali.checkDate01(txtNgay.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày tạo phải là ngày sau hôm nay!");
            return false;
        } else if (repo.checkTrung(txtMa.getText())) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm trùng, vui lòng kiểm tra lại!");
            return false;
        }

        return true;
    }

    private boolean kiemTra01() {
        if (txtTen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            return false;
        } else if (!vali.checkDate01(txtNgay.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày tạo phải là ngày sau hôm nay!");
            return false;
        }

        return true;
    }

    public void clear() {
        txt_MaSPCT.setText("");
        txt_TenSPCT.setText("");
        cbo_KichThuoc.setSelectedItem("");
        cbo_MauSac1.setSelectedItem("");
        cbo_ChatLieu1.setSelectedItem("");
        cbo_Da.setSelectedItem("");
        cbo_XuatXu1.setSelectedItem("");
        cbo_MaSanPham.setSelectedItem("");
        txt_SoLuong.setText("");
        txt_DonGia.setText("");
        lbHinh.setText("");
        cbo_TrangThai.setSelectedItem("");
    }

    private void boxSearch() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            private void search(String text) {
                try {
                    fillTable(repo.search(text));
                } catch (Exception e) {
                    fillTable(repo.getAll());
                }
            }
        });
    }

    private void boxSearchSpct() {
        txt_TimSanPham.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txt_TimSanPham.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txt_TimSanPham.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txt_TimSanPham.getText());
            }

            private void search(String text) {
                try {
                    showDataTable_SPCT(spct_repo.search(text));
                } catch (Exception e) {
                    showDataTable_SPCT(spct_repo.getAll());
                }
            }
        });
    }

    private Boolean kiemTra_SPCT() {
        if (txt_TenSPCT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập vào tên sản phẩm chi tiết");
            txt_TenSPCT.requestFocus();
            return false;
        } else if (txt_SoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập vào số lượng");
            txt_SoLuong.requestFocus();
            return false;
        } else if (txt_DonGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập vào đơn giá");
            txt_DonGia.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_SanPham = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtTen = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        rdoHd = new javax.swing.JRadioButton();
        rdoNgungHd = new javax.swing.JRadioButton();
        txtNgay = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_MaSPCT = new javax.swing.JTextField();
        txt_TenSPCT = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_SoLuong = new javax.swing.JTextField();
        txt_DonGia = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        btn_Them = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        btn_Xoa = new javax.swing.JButton();
        btn_LamMoi = new javax.swing.JButton();
        lbHinh = new javax.swing.JLabel();
        cbo_ChatLieu1 = new javax.swing.JComboBox<>();
        cbo_KichThuoc = new javax.swing.JComboBox<>();
        cbo_MauSac1 = new javax.swing.JComboBox<>();
        cbo_XuatXu1 = new javax.swing.JComboBox<>();
        cbo_Da = new javax.swing.JComboBox<>();
        cbo_MaSanPham = new javax.swing.JComboBox<>();
        cbo_TrangThai = new javax.swing.JComboBox<>();
        btn_KichThuoc = new javax.swing.JButton();
        btn_MauSac = new javax.swing.JButton();
        btn_ChatLieu = new javax.swing.JButton();
        btn_LoaiDa = new javax.swing.JButton();
        btn_XuatXu = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbo_MauSac = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        cbo_ChatLieu = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        cbo_XuatXu = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txt_TimSanPham = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_SPCT = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1330, 800));

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1330, 800));

        jPanel1.setPreferredSize(new java.awt.Dimension(1330, 800));

        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbl_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Ngày tạo", "Trạng thái"
            }
        ));
        tbl_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_SanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_SanPham);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Thông tin sản phẩm", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Mã sản phẩm:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tên sản phẩm:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Ngày tạo:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Trạng thái");

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAdd.setBackground(new java.awt.Color(255, 204, 0));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 204, 0));
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 204, 0));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 204, 0));
        jButton7.setText("Làm mới");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addGap(18, 18, 18)
                .addComponent(btnSua)
                .addGap(18, 18, 18)
                .addComponent(btnXoa)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addGap(15, 15, 15))
        );

        buttonGroup1.add(rdoHd);
        rdoHd.setSelected(true);
        rdoHd.setText("Đang hoạt động");

        buttonGroup1.add(rdoNgungHd);
        rdoNgungHd.setText("Ngưng hoạt động");

        txtNgay.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTen, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(txtMa))
                .addGap(148, 148, 148)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(48, 48, 48)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoHd)
                    .addComponent(rdoNgungHd)
                    .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(txtMa, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(txtNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(rdoHd))
                        .addGap(18, 18, 18)
                        .addComponent(rdoNgungHd))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Lọc sản phẩm", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Tìm sản phẩm:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel7))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 1139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sản Phẩm", jPanel1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Thông tin sản phẩm", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Mã sản phẩm chi tiết:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Tên sản phẩm chi tiết:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Kích thước:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Màu sắc:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Loại đá:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Chất liệu:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Xuất xứ:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Mã sản phẩm:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Số lượng:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Đơn giá:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Trạng thái:");

        txt_SoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SoLuongActionPerformed(evt);
            }
        });

        txt_DonGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_DonGiaActionPerformed(evt);
            }
        });

        jPanel11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btn_Them.setBackground(new java.awt.Color(255, 204, 0));
        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_Them.setText("Thêm");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_Sua.setBackground(new java.awt.Color(255, 204, 0));
        btn_Sua.setText("Sửa");
        btn_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaActionPerformed(evt);
            }
        });

        btn_Xoa.setBackground(new java.awt.Color(255, 204, 0));
        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        btn_LamMoi.setBackground(new java.awt.Color(255, 204, 0));
        btn_LamMoi.setText("Làm mới");
        btn_LamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btn_Them)
                .addGap(71, 71, 71)
                .addComponent(btn_Sua)
                .addGap(52, 52, 52)
                .addComponent(btn_Xoa)
                .addGap(39, 39, 39)
                .addComponent(btn_LamMoi)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Xoa)
                    .addComponent(btn_Sua)
                    .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_LamMoi))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        lbHinh.setText("Chọn ảnh sản phẩm");
        lbHinh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lbHinh.setPreferredSize(new java.awt.Dimension(134, 175));
        lbHinh.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lbHinhAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        lbHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbHinhMouseClicked(evt);
            }
        });

        cbo_ChatLieu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_ChatLieu1MouseClicked(evt);
            }
        });

        cbo_KichThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbo_KichThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_KichThuocMouseClicked(evt);
            }
        });
        cbo_KichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_KichThuocActionPerformed(evt);
            }
        });

        cbo_MauSac1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbo_MauSac1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_MauSac1MouseClicked(evt);
            }
        });
        cbo_MauSac1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_MauSac1ActionPerformed(evt);
            }
        });

        cbo_XuatXu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_XuatXu1MouseClicked(evt);
            }
        });
        cbo_XuatXu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_XuatXu1ActionPerformed(evt);
            }
        });

        cbo_Da.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_DaMouseClicked(evt);
            }
        });

        cbo_MaSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_MaSanPhamMouseClicked(evt);
            }
        });
        cbo_MaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_MaSanPhamActionPerformed(evt);
            }
        });

        btn_KichThuoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_KichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_KichThuocActionPerformed(evt);
            }
        });

        btn_MauSac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_MauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MauSacActionPerformed(evt);
            }
        });

        btn_ChatLieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_ChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChatLieuActionPerformed(evt);
            }
        });

        btn_LoaiDa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_LoaiDa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoaiDaActionPerformed(evt);
            }
        });

        btn_XuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_XuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatXuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_MaSPCT)
                            .addComponent(txt_TenSPCT)
                            .addComponent(cbo_KichThuoc, 0, 168, Short.MAX_VALUE)
                            .addComponent(cbo_MauSac1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbo_ChatLieu1, 0, 162, Short.MAX_VALUE)
                                    .addComponent(cbo_Da, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbo_MaSanPham, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbo_XuatXu1, 0, 163, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btn_XuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbo_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_ChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_LoaiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel17)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(jLabel16)
                                            .addGap(18, 18, 18)
                                            .addComponent(txt_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txt_DonGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)))
                .addComponent(lbHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16))
                                    .addComponent(btn_ChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbo_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_MaSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbo_ChatLieu1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_TenSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel10))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(cbo_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel14))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel11)
                                            .addComponent(cbo_MauSac1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15)))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(btn_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbo_Da, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addComponent(btn_LoaiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(cbo_XuatXu1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(cbo_MaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btn_XuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(11, 11, 11)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Lọc sản phẩm", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jPanel8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Màu sắc:");

        cbo_MauSac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_MauSacMouseClicked(evt);
            }
        });
        cbo_MauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_MauSacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(cbo_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(cbo_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Chất liệu");

        cbo_ChatLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_ChatLieuMouseClicked(evt);
            }
        });
        cbo_ChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_ChatLieuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(96, 96, 96))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(cbo_ChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(cbo_ChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Xuất xứ");

        cbo_XuatXu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_XuatXuMouseClicked(evt);
            }
        });
        cbo_XuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_XuatXuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(cbo_XuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel22)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addComponent(cbo_XuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel13.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Tìm sản phẩm");

        txt_TimSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_TimSanPhamMouseClicked(evt);
            }
        });
        txt_TimSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TimSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(txt_TimSanPham)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(125, 125, 125))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(txt_TimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel16.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbl_SPCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã sản phẩm chi tiết", "Tên sản phẩm chi tiết", "Kích thước", "Màu sắc", "Chất liệu", "Đá", "Xuất xứ", "Mã sản phẩm", "Số lượng", "Đơn giá", "Ảnh", "Trạng thái"
            }
        ));
        tbl_SPCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_SPCTMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_SPCT);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1306, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 725, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Sản Phẩm Chi Tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1306, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_SanPhamMouseClicked
        // TODO add your handling code here:
        int index = tbl_SanPham.getSelectedRow();
        fillTableToForm(index);
    }//GEN-LAST:event_tbl_SanPhamMouseClicked

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:

        int index = tbl_SanPham.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!");
        } else {
            SanPham sp = repo.getAll().get(index);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa sản phẩm này chứ?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                repo.remove(sp.getId());
                fillTable(repo.getAll());
                JOptionPane.showMessageDialog(this, "Xóa thành cônng!");
            }
        }

    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:

        int index = tbl_SanPham.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để sửa!");
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn sửa sản phẩm này chứ?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                if (kiemTra01()) {
                    SanPham sp = repo.getAll().get(index);
                    repo.update(sp.getId(), getDataToForm());

                    fillTable(repo.getAll());
                    JOptionPane.showMessageDialog(this, "Sửa thành cônng!");
                }
            }
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn thêm sản phẩm này chứ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            if (kiemTra()) {
                maSp = RandomStringGenerator.generateRandomString("SP");
                repo.add(getDataToForm());
                fillTable(repo.getAll());
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_SoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SoLuongActionPerformed

    private void txt_DonGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DonGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DonGiaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn thêm sản phẩm này chứ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            if (spct_repo.checkTrung(txt_MaSPCT.getText())) {
                JOptionPane.showMessageDialog(this, "Trùng mã");
            } else {
                if (kiemTra_SPCT()) {
                    ma_san_pham_chi_tiet = RandomStringGenerator.generateRandomString("SPCT");
                    spct_repo.add(getFormData());
                    //        System.out.println(convertResponseToEntity(getFormData()));
                    showDataTable_SPCT(spct_repo.getAll());
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            }

        }
    }//GEN-LAST:event_btn_ThemActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        // TODO add your handling code here:
        int index = tbl_SPCT.getSelectedRow();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn sửa sản phẩm này chứ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn dòng để sửa");
            } else {
                spct_repo.update(getFormData(), spct_repo.getAll().get(index).getId());
                showDataTable_SPCT(spct_repo.getAll());
                JOptionPane.showMessageDialog(this, "Sửa thành cônng!");

            }
        }
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        // TODO add your handling code here:

        int index = tbl_SPCT.getSelectedRow();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa sản phẩm này không?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn dòng để xóa");
            } else {
                spct_repo.delete(spct_repo.getAll().get(index).getId());
                showDataTable_SPCT(spct_repo.getAll());
                JOptionPane.showMessageDialog(this, "Xóa thành cônng!");
            }
        }
    }//GEN-LAST:event_btn_XoaActionPerformed

    private void btn_LamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btn_LamMoiActionPerformed

    private void lbHinhAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lbHinhAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lbHinhAncestorAdded

    private void lbHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbHinhMouseClicked

        JFileChooser fileChooser = new JFileChooser("H:\\Projects_java\\DuAn01\\Du_an01c\\giaoDien01\\src\\img");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getName();
            BufferedImage b;
            try {
                b = ImageIO.read(file);
                //                lbHinh.setIcon(new ImageIcon(b));
                int width = lbHinh.getWidth();
                int height = lbHinh.getHeight();

                lbHinh.setIcon(new ImageIcon(b.getScaledInstance(width, height, 0)));
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_lbHinhMouseClicked

    private void cbo_KichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_KichThuocActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cbo_KichThuocActionPerformed

    private void cbo_MauSac1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_MauSac1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_MauSac1ActionPerformed

    private void cbo_XuatXu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_XuatXu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_XuatXu1ActionPerformed

    private void cbo_MaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_MaSanPhamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_MaSanPhamActionPerformed

    private void btn_KichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_KichThuocActionPerformed
        // TODO add your handling code here:
        ViewKichThuoc kichThuoc = new ViewKichThuoc();
        
        kichThuoc.setVisible(true);
      
    }//GEN-LAST:event_btn_KichThuocActionPerformed

    private void btn_MauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MauSacActionPerformed
        // TODO add your handling code here:
        ViewMauSac mauSac = new ViewMauSac();
        mauSac.setVisible(true);
    }//GEN-LAST:event_btn_MauSacActionPerformed

    private void btn_ChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChatLieuActionPerformed
        // TODO add your handling code here:
        ViewChatLieu chatLieu = new ViewChatLieu();
        chatLieu.setVisible(true);
    }//GEN-LAST:event_btn_ChatLieuActionPerformed

    private void btn_LoaiDaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LoaiDaActionPerformed
        // TODO add your handling code here:
        ViewDa da = new ViewDa();
        da.setVisible(true);
    }//GEN-LAST:event_btn_LoaiDaActionPerformed

    private void btn_XuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatXuActionPerformed
        // TODO add your handling code here:
        ViewXuatXu xx = new ViewXuatXu();
        xx.setVisible(true);
    }//GEN-LAST:event_btn_XuatXuActionPerformed

    private void cbo_MauSacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_MauSacMouseClicked
        // TODO add your handling code here:
        cbo_ChatLieu.setSelectedItem("");
        cbo_XuatXu.setSelectedItem("");
        txt_TimSanPham.setText("");
    }//GEN-LAST:event_cbo_MauSacMouseClicked

    private void cbo_MauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_MauSacActionPerformed
        // TODO add your handling code here:

        MauSac ms = new MauSac();
        try {

            ms = (MauSac) cbo_MauSac.getSelectedItem();
            List<SanPhamChiTietResp> list = spct_repo.locMau(ms.getId());
            showDataTable_SPCT(list);
        } catch (Exception e) {
            showDataTable_SPCT(spct_repo.getAll());
        }
    }//GEN-LAST:event_cbo_MauSacActionPerformed

    private void cbo_ChatLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_ChatLieuMouseClicked
        // TODO add your handling code here:
        cbo_MauSac.setSelectedItem("");
        cbo_XuatXu.setSelectedItem("");
        txt_TimSanPham.setText("");
    }//GEN-LAST:event_cbo_ChatLieuMouseClicked

    private void cbo_ChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_ChatLieuActionPerformed
        // TODO add your handling code here:

        ChatLieu cl = new ChatLieu();
        try {

            cl = (ChatLieu) cbo_ChatLieu.getSelectedItem();
            List<SanPhamChiTietResp> list = spct_repo.locChatLieu(cl.getId());
            showDataTable_SPCT(list);
        } catch (Exception e) {
            showDataTable_SPCT(spct_repo.getAll());
        }
    }//GEN-LAST:event_cbo_ChatLieuActionPerformed

    private void cbo_XuatXuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_XuatXuMouseClicked
        // TODO add your handling code here:
        cbo_MauSac.setSelectedItem("");
        cbo_ChatLieu.setSelectedItem("");
        txt_TimSanPham.setText("");
    }//GEN-LAST:event_cbo_XuatXuMouseClicked

    private void cbo_XuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_XuatXuActionPerformed
        // TODO add your handling code here:

        XuatXu xx = new XuatXu();
        try {

            xx = (XuatXu) cbo_XuatXu.getSelectedItem();
            List<SanPhamChiTietResp> list = spct_repo.locXuatXu(xx.getId());
            showDataTable_SPCT(list);
        } catch (Exception e) {
            showDataTable_SPCT(spct_repo.getAll());
        }
    }//GEN-LAST:event_cbo_XuatXuActionPerformed

    private void txt_TimSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_TimSanPhamMouseClicked
        // TODO add your handling code here:
        cbo_ChatLieu.setSelectedItem("");
        cbo_MauSac.setSelectedItem("");
        cbo_XuatXu.setSelectedItem("");
    }//GEN-LAST:event_txt_TimSanPhamMouseClicked

    private void txt_TimSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TimSanPhamActionPerformed
        // TODO add your handling code here:
        try {
            showDataTable_SPCT(spct_repo.search(txt_TimSanPham.getText()));
        } catch (Exception e) {
            showDataTable_SPCT(spct_repo.getAll());
        }
    }//GEN-LAST:event_txt_TimSanPhamActionPerformed

    private void tbl_SPCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_SPCTMouseClicked
        // TODO add your handling code here:
        int index = tbl_SPCT.getSelectedRow();
        detail(index);

    }//GEN-LAST:event_tbl_SPCTMouseClicked

    private void cbo_KichThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_KichThuocMouseClicked
        // TODO add your handling code here:
        loadCBB_KT();
    }//GEN-LAST:event_cbo_KichThuocMouseClicked

    private void cbo_MauSac1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_MauSac1MouseClicked
        // TODO add your handling code here:
        loadCBB_MS();
    }//GEN-LAST:event_cbo_MauSac1MouseClicked

    private void cbo_ChatLieu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_ChatLieu1MouseClicked
        // TODO add your handling code here:
        loadCBB_CL();
    }//GEN-LAST:event_cbo_ChatLieu1MouseClicked

    private void cbo_DaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_DaMouseClicked
        // TODO add your handling code here:
        loadCBB_DA();
    }//GEN-LAST:event_cbo_DaMouseClicked

    private void cbo_XuatXu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_XuatXu1MouseClicked
        // TODO add your handling code here:
        loadCBB_XX();
    }//GEN-LAST:event_cbo_XuatXu1MouseClicked

    private void cbo_MaSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_MaSanPhamMouseClicked
        // TODO add your handling code here:
        loadCBB_MSP();
    }//GEN-LAST:event_cbo_MaSanPhamMouseClicked

    private void reset() {
        txtMa.setText("");
        txtTen.setText("");
        txtNgay.setDate(today);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btn_ChatLieu;
    private javax.swing.JButton btn_KichThuoc;
    private javax.swing.JButton btn_LamMoi;
    private javax.swing.JButton btn_LoaiDa;
    private javax.swing.JButton btn_MauSac;
    private javax.swing.JButton btn_Sua;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JButton btn_XuatXu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbo_ChatLieu;
    private javax.swing.JComboBox<String> cbo_ChatLieu1;
    private javax.swing.JComboBox<String> cbo_Da;
    private javax.swing.JComboBox<String> cbo_KichThuoc;
    private javax.swing.JComboBox<String> cbo_MaSanPham;
    private javax.swing.JComboBox<String> cbo_MauSac;
    private javax.swing.JComboBox<String> cbo_MauSac1;
    private javax.swing.JComboBox<String> cbo_TrangThai;
    private javax.swing.JComboBox<String> cbo_XuatXu;
    private javax.swing.JComboBox<String> cbo_XuatXu1;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbHinh;
    private javax.swing.JRadioButton rdoHd;
    private javax.swing.JRadioButton rdoNgungHd;
    private javax.swing.JTable tbl_SPCT;
    private javax.swing.JTable tbl_SanPham;
    private javax.swing.JTextField txtMa;
    private com.toedter.calendar.JDateChooser txtNgay;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txt_DonGia;
    private javax.swing.JTextField txt_MaSPCT;
    private javax.swing.JTextField txt_SoLuong;
    private javax.swing.JTextField txt_TenSPCT;
    private javax.swing.JTextField txt_TimSanPham;
    // End of variables declaration//GEN-END:variables
}
