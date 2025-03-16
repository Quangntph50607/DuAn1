/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import entity.ChucVu;
import response.NhanVienResp;
import java.util.List;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import repository.ChucVuRepo;
import repository.NhanVienRepo;
import entity.NhanVien;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import logic.RandomStringGenerator;

/**
 *
 * @author DUNG
 */
public class MenuNhanVien extends javax.swing.JInternalFrame {

    private DefaultTableModel defaultTableModel;

    private NhanVienRepo repo;

    private ChucVuRepo repo_cv;

    private Validate vali;

    private List<NhanVienResp> listView;

    private LocalDate currentDate = LocalDate.now();

    private Date today = new Date();

    private String maNv;

    /**
     * Creates new form MenuBanHang
     */
    public MenuNhanVien() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

//        listView = repo.getAllDangLam();
        vali = new Validate();
        repo = new NhanVienRepo();
        repo_cv = new ChucVuRepo();

        showCbxVaiTro();
//        showCbxGioiTinh();
//        showLocVaiTro();
        loadVaiTro(repo_cv.getAll());
        loadGioiTinh(repo.getAll());

        fillTableDangLam(repo.getAllDangLam());
        fillTableNghiViec(repo.getAllNghiViec());
        boxSearch();
        txtMa.disable();
    }

    private void showCbxVaiTro() {
        cbxVaiTro.removeAllItems();
        for (ChucVu cv : repo_cv.getAll()) {
            cbxVaiTro.addItem(cv.getMaCv());
        }
    }

//    private void showLocVaiTro() {
//        cbxLocVaiTro.removeAllItems();
//        cbxLocVaiTro.addItem("");
//        for (ChucVu cv : repo_cv.getAll()) {
//            cbxLocVaiTro.addItem(cv.getMaCv());
//        }
//    }
    private void loadVaiTro(List<ChucVu> list) {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbxLocVaiTro.getModel();
        cbxLocVaiTro.addItem("");
        for (ChucVu cv : list) {
            comboBoxModel.addElement(cv);
        }
    }

    private void loadGioiTinh(List<NhanVienResp> list) {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbxGioiTinh.getModel();

        cbxGioiTinh.addItem("");
        Set<String> uniqueGenders = new HashSet<>();

        for (NhanVienResp gt : list) {
            String gender = gt.getGioiTinh() == 1 ? "Nam" : "Nữ";
            uniqueGenders.add(gender);
        }

        for (String gender : uniqueGenders) {
            comboBoxModel.addElement(gender);
        }
    }

//    private void showCbxGioiTinh() {
//        cbxGioiTinh.removeAllItems();
//        cbxGioiTinh.addItem("");
//        Set<String> uniqueGenders = new HashSet<>();
//        
//        for (NhanVienResp nv : repo.getAll()) {
//            String gender = nv.getGioiTinh() ? "Nam" : "Nữ";
//            uniqueGenders.add(gender);
//        }
//        
//        for (String gender : uniqueGenders) {
//            cbxGioiTinh.addItem(gender);
//        }
//    }
    public void fillTableDangLam(List<NhanVienResp> lists) {
        defaultTableModel = (DefaultTableModel) tbDangLam.getModel();
        defaultTableModel.setRowCount(0);

        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        lists.forEach(s -> defaultTableModel.addRow(new Object[]{
            index.getAndIncrement(), s.getMaNhanVien(), s.getHoTen(),
            s.getSdt(), s.getEmail(), s.getDiaChi(),
            s.getNgaySinh(), s.getGioiTinh() == 1 ? "Nam" : "Nữ", s.getNgayBatDau(), s.getMatKhau(),
            s.getMaChucVu().equalsIgnoreCase("NV") ? "Nhân viên" : "Quản lý", s.getTrangThai() == 0 ? "Nghỉ việc" : "Đang làm"
        }));
    }
//    public void fillTableDangLam(List<NhanVienResp> lists) {
//        defaultTableModel.setRowCount(0);
//
//        int index = 1;
//        for (NhanVienResp nv : lists) {
//
//            defaultTableModel.addRow(nv.toRowTable(index++));
//        }
//    }

    public void fillTableNghiViec(List<NhanVienResp> lists) {
        defaultTableModel = (DefaultTableModel) tbNghiViec.getModel();
        defaultTableModel.setRowCount(0);
        int index = 1;
        for (NhanVienResp nv : lists) {

            defaultTableModel.addRow(nv.toRowTable(index++));
        }
    }

    public void fillTableToForm(int index) {
        NhanVienResp nv = repo.getAll().get(index);
        txtMa.setText(nv.getMaNhanVien());
        txtTen.setText(nv.getHoTen());
        txtMatKhau.setText(nv.getMatKhau());

        cbxVaiTro.setSelectedItem(nv.getMaChucVu());

        txtDiaChi.setText(nv.getDiaChi());

        txtSdt.setText(nv.getSdt());
        txtNgaySinh.setDate(new Date(nv.getNgaySinh().getTime()));
        txtEmail.setText(nv.getEmail());
        txtNgayBatDau.setDate(new Date(nv.getNgayBatDau().getTime()));
        if (nv.getGioiTinh() == 1) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }

        if (nv.getTrangThai() == 0) {
            rdoNghiViec.setSelected(true);
        } else {
            rdoDangLamViec.setSelected(true);
        }
    }

    public void fillTableToFormDL(int index) {
        NhanVienResp nv = repo.getAllDangLam().get(index);
        txtMa.setText(nv.getMaNhanVien());
        txtTen.setText(nv.getHoTen());
        txtMatKhau.setText(nv.getMatKhau());

        cbxVaiTro.setSelectedItem(nv.getMaChucVu());

        txtDiaChi.setText(nv.getDiaChi());

        txtSdt.setText(nv.getSdt());
        txtNgaySinh.setDate(new Date(nv.getNgaySinh().getTime()));
        txtEmail.setText(nv.getEmail());
        txtNgayBatDau.setDate(new Date(nv.getNgayBatDau().getTime()));
        if (nv.getGioiTinh() == 1) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
        if (nv.getTrangThai() == 0) {
            rdoNghiViec.setSelected(true);
        } else {
            rdoDangLamViec.setSelected(true);
        }
    }

    public void fillTableToFormNV(int index) {
        NhanVienResp nv = repo.getAllNghiViec().get(index);
        txtMa.setText(nv.getMaNhanVien());
        txtTen.setText(nv.getHoTen());
        txtMatKhau.setText(nv.getMatKhau());

        cbxVaiTro.setSelectedItem(nv.getMaChucVu());

        txtDiaChi.setText(nv.getDiaChi());

        txtSdt.setText(nv.getSdt());
        txtNgaySinh.setDate(new Date(nv.getNgaySinh().getTime()));
        txtEmail.setText(nv.getEmail());
        txtNgayBatDau.setDate(new Date(nv.getNgayBatDau().getTime()));
        if (nv.getGioiTinh() == 1) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
        if (nv.getTrangThai() == 0) {
            rdoNghiViec.setSelected(true);
        } else {
            rdoDangLamViec.setSelected(true);
        }
    }

    private void boxSearch() {
        txtsearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txtsearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txtsearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txtsearch.getText());
            }

            private void search(String text) {
                cbxLocVaiTro.setSelectedItem("");
                cbxGioiTinh.setSelectedItem("");
                try {
                    fillTableDangLam(repo.search(text));
                } catch (Exception e) {
                    fillTableDangLam(repo.getAllDangLam());
                }
            }
        });
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtMatKhau.setText("");
        txtDiaChi.setText("");
        txtNgayBatDau.setDate(today);
        txtNgaySinh.setDate(today);
        txtSdt.setText("");
        txtEmail.setText("");

    }

    private NhanVienResp getFormData() {

        return new NhanVienResp(
                maNv,
                txtTen.getText().trim(),
                txtSdt.getText().trim(),
                txtEmail.getText().trim(),
                txtDiaChi.getText().trim(),
                txtNgaySinh.getDate(),
                //                    new SimpleDateFormat("yyyy-MM-dd").parse(txtNgaySinh.getDateFormatString()),
                rdoNam.isSelected() ? 1 : 0,
                txtNgayBatDau.getDate(),
                //                    new SimpleDateFormat("yyyy-MM-dd").parse(txtNgayBatDau.getText()),
                txtMatKhau.getText().trim(),
                rdoNghiViec.isSelected() ? 0 : 1,
                cbxVaiTro.getSelectedItem().toString()
        );

//        NhanVienResp nv = new NhanVienResp();
//        nv.setMaNhanVien(txtMa.getText());
//        nv.setHoTen(txtTen.getText());
//        nv.setMatKhau(txtMatKhau.getText());
//        
//        nv.setTenChucVu(String.valueOf(cbxVaiTro.getSelectedItem()));
//        
//        nv.setDiaChi(txtDiaChi.getText());
//        nv.setLuong(Double.valueOf(txtLuong.getText()));
//        nv.setSdt(txtSdt.getText());
//        try {
//            nv.setNgaySinh(new SimpleDateFormat("yyyy-MM-dd").parse(txtNgaySinh.getText()));
//        } catch (ParseException ex) {
//            Logger.getLogger(MenuNhanVien.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        nv.setEmail(txtEmail.getText());
//        try {
//            nv.setNgayBatDau(new SimpleDateFormat("yyyy-MM-dd").parse(txtNgayBatDau.getText()));
//        } catch (ParseException ex) {
//            Logger.getLogger(MenuNhanVien.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        nv.setGioiTinh(rdoNam.isSelected()?true:false);
////        rdoNam.setSelected(nv.getGioiTinh().equals("Nam"));
////        rdoNu.setSelected(!nv.getGioiTinh().equals("Nam"));
//        nv.setTrangThai(rdoNghiViec.isSelected() ? 0 : 1);
//
//        return nv;
    }

    private boolean kiemTra01() {

        if (txtTen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            return false;
        } else if (txtMatKhau.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhân viên không được để trống!");
            return false;
        } else if (txtDiaChi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ nhân viên không được để trống!");
            return false;
        } else if (txtSdt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại nhân viên không được để trống!");
            return false;
        } else if (txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email nhân viên không được để trống!");
            return false;
        } else if (!vali.checkSdt(txtSdt.getText())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu từ số 0 và phải đủ 10 số!");
            return false;
        } else if (!vali.checkEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Email phải bắt đầu bằng chữ cái và có @!");
            return false;
        } else if (!vali.checkDate(txtNgaySinh.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không thể là ngày sau hôm nay!");
            return false;
        } else if (!vali.checkDate01(txtNgayBatDau.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể là ngày trong quá khứ!");
            return false;
        } else {
            return true;
        }
    }

    private boolean kiemTra() {

        if (txtTen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            return false;
        } else if (txtMatKhau.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhân viên không được để trống!");
            return false;
        } else if (txtDiaChi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ nhân viên không được để trống!");
            return false;
        } else if (txtSdt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại nhân viên không được để trống!");
            return false;
        } else if (txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email nhân viên không được để trống!");
            return false;
        } else if (repo.checkTrung(txtMa.getText())) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên trùng, vui lòng kiểm tra lại!");
            return false;
        } else if (!vali.checkSdt(txtSdt.getText())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu từ số 0 và phải đủ 10 số!");
            return false;
        } else if (!vali.checkEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Email phải bắt đầu bằng chữ cái và có @!");
            return false;
        } else if (!vali.checkDate(txtNgaySinh.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không thể là ngày sau hôm nay!");
            return false;
        } else if (!vali.checkDate01(txtNgayBatDau.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể là ngày sau hôm nay!");
            return false;
        } //        else if (vali.checkTrungSdt(txtSdt.getText())) {
        //            JOptionPane.showMessageDialog(this, "Số điện thoại này đã tồn tại. Vui lòng kiểm tra lại!");
        //            return false;
        //        }
        else {
            return true;
        }
    }

    private NhanVien convertResponseToEntity(NhanVienResp response) {
        ChucVu cv = repo_cv.getChucVuByMa(response.getMaChucVu());
        return new NhanVien(
                response.getMaNhanVien(),
                response.getHoTen(),
                response.getSdt(),
                response.getEmail(),
                response.getDiaChi(),
                response.getNgaySinh(),
                response.getGioiTinh(),
                response.getNgayBatDau(),
                response.getMatKhau(),
                cv.getId(),
                response.getTrangThai()
        );

    }

    public static String convertEmptyToNull(String input) {
        return input == null || input.trim().isEmpty() ? null : input;
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
        jMenu1 = new javax.swing.JMenu();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxVaiTro = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        rdoDangLamViec = new javax.swing.JRadioButton();
        rdoNghiViec = new javax.swing.JRadioButton();
        btnThem = new com.raven.swing.Button();
        btnSua = new com.raven.swing.Button();
        btnReset = new com.raven.swing.Button();
        jLabel17 = new javax.swing.JLabel();
        btnXoa = new com.raven.swing.Button();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        txtNgayBatDau = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        cbxLocVaiTro = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cbxGioiTinh = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtsearch = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDangLam = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbNghiViec = new javax.swing.JTable();

        jMenu1.setText("jMenu1");

        setPreferredSize(new java.awt.Dimension(1330, 800));
        getContentPane().setLayout(null);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Thiết lập thông tin nhân viên:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 10, 200, 20);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã NV:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Tên NV:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Mật khẩu:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Địa chỉ:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Vai trò:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Điện thoại:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Giới tính:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Ngày sinh:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Email:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Trạng thái:");

        buttonGroup1.add(rdoNam);
        rdoNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNu.setText("Nữ");

        buttonGroup2.add(rdoDangLamViec);
        rdoDangLamViec.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoDangLamViec.setSelected(true);
        rdoDangLamViec.setText("Đang làm việc");

        buttonGroup2.add(rdoNghiViec);
        rdoNghiViec.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNghiViec.setText("Nghỉ việc");

        btnThem.setBackground(new java.awt.Color(255, 204, 51));
        btnThem.setText("Thêm");
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 204, 51));
        btnSua.setText("Sửa");
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(255, 204, 51));
        btnReset.setText("Làm mới");
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Ngày bắt đầu:");

        btnXoa.setBackground(new java.awt.Color(255, 204, 51));
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        txtNgaySinh.setDateFormatString("yyyy-MM-dd");

        txtNgayBatDau.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)))
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtTen, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtMatKhau, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(rdoDangLamViec)
                                        .addGap(38, 38, 38))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(rdoNam)
                                        .addGap(91, 91, 91)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdoNu)
                                    .addComponent(rdoNghiViec)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel17))
                            .addComponent(jLabel11))
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(txtSdt, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNgayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(161, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbxVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(15, 15, 15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(rdoDangLamViec)
                            .addComponent(rdoNghiViec))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(30, 40, 1270, 320);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Lọc theo giới tính");

        cbxLocVaiTro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLocVaiTroActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Lọc theo vai trò");

        cbxGioiTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxGioiTinhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel13)
                .addGap(45, 45, 45)
                .addComponent(cbxGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(44, 44, 44)
                .addComponent(cbxLocVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(cbxLocVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(cbxGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(30, 400, 790, 80);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Lọc");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(30, 380, 23, 20);

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtsearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtsearchMouseClicked(evt);
            }
        });
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(950, 400, 350, 80);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Tìm kiếm:");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(950, 380, 66, 20);

        tbDangLam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Tên NV", "Điện Thoại", "Email", "Địa Chỉ", "Ngày Sinh ", "Giới Tính", "Ngày Bắt Đầu", "Mật Khẩu", "Vai Trò", "Trạng Thái"
            }
        ));
        tbDangLam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDangLamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDangLam);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Đang làm việc", jPanel4);

        tbNghiViec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Tên NV", "Lương", "Điện Thoại", "Email", "Địa Chỉ", "Ngày Sinh ", "Giới Tính", "Ngày Bắt Đầu", "Mật Khẩu", "Vai Trò", "Trạng Thái"
            }
        ));
        tbNghiViec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbNghiViecMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbNghiViec);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Nghỉ việc", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(30, 500, 1270, 260);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDangLamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDangLamMouseClicked
        // TODO add your handling code here:
        int index = tbDangLam.getSelectedRow();
        fillTableToFormDL(index);
    }//GEN-LAST:event_tbDangLamMouseClicked

    private void tbNghiViecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbNghiViecMouseClicked
        // TODO add your handling code here:
        int index = tbNghiViec.getSelectedRow();
        fillTableToFormNV(index);
    }//GEN-LAST:event_tbNghiViecMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có muốn thêm nhân viên này không?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (chon == 0) {
            if (repo.checkTrungSDT(txtSdt.getText())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại trùng! Vui lòng kiểm tra lại");

            } else if (repo.checkTrungEmail(txtEmail.getText())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại trùng! Vui lòng kiểm tra lại");
            } else {
                if (kiemTra()) {

                    maNv = RandomStringGenerator.generateRandomString("NV");
                    repo.add(convertResponseToEntity(getFormData()));
                    fillTableDangLam(repo.getAllDangLam());
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                }
            }

        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        int index = tbDangLam.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để sửa!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có sửa nhân viên này không?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (chon == 0) {
            if (kiemTra01()) {
                NhanVienResp nv = repo.getAllDangLam().get(index);
                repo.update(nv.getId(), convertResponseToEntity(getFormData()));
                fillTableDangLam(repo.getAllDangLam());
                fillTableNghiViec(repo.getAllNghiViec());
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
            }
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:

        int index = tbDangLam.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có xóa nhân viên này không?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (chon == 0) {
            NhanVienResp nv = repo.getAllDangLam().get(index);
            repo.remove(nv.getId());
            fillTableDangLam(repo.getAllDangLam());
            fillTableNghiViec(repo.getAllNghiViec());
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        // TODO add your handling code here:
//        cbxLocVaiTro.setSelectedItem("");
//        cbxGioiTinh.setSelectedItem("");
//        try {
//            fillTableDangLam(repo.search(txtsearch.getText()));
//        } catch (Exception e) {
//            fillTableDangLam(repo.getAllDangLam());
//        }


    }//GEN-LAST:event_txtsearchActionPerformed

    private void cbxLocVaiTroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLocVaiTroActionPerformed
        // TODO add your handling code here:  

//        ChucVu cv = new ChucVu();
//
//        try {
//
//            cv = (ChucVu) cbxLocVaiTro.getSelectedItem();
//
//            List<NhanVienResp> list = repo.locVaiTro(cv.getId());
//            fillTableDangLam(list);
//            System.out.println("GioiTinh selected: " + cv.getId());
////            List<NhanVienResp> list = repo.locVaiTro(cv.getId());
//        } catch (Exception e) {
//            fillTableDangLam(repo.getAllDangLam());
//        }
        NhanVienResp nv = new NhanVienResp();
        ChucVu cv = new ChucVu();
        try {
            String gt = (String) cbxGioiTinh.getSelectedItem();
            cv = (ChucVu) cbxLocVaiTro.getSelectedItem();
            boolean genderSelected = gt != null && !gt.isEmpty();
            boolean roleSelected = cv != null;

            if (genderSelected || roleSelected) {
                Integer genderValue = null;
                if (genderSelected) {
                    genderValue = gt.equals("Nam") ? 1 : 0;
                    nv.setGioiTinh(genderValue);
                }

                Integer roleId = null;
                if (roleSelected) {
                    roleId = cv.getId();

                }

                List<NhanVienResp> list = repo.loc(roleId, genderValue);
                fillTableDangLam(list);
            } else {
                fillTableDangLam(repo.getAllDangLam());
            }
        } catch (Exception e) {
            fillTableDangLam(repo.getAllDangLam());
        }


    }//GEN-LAST:event_cbxLocVaiTroActionPerformed

    private void txtsearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtsearchMouseClicked
        // TODO add your handling code here:
        cbxLocVaiTro.setSelectedItem("");
        cbxGioiTinh.setSelectedItem("");
    }//GEN-LAST:event_txtsearchMouseClicked

    private void cbxGioiTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxGioiTinhActionPerformed
        // TODO add your handling code here:
//        NhanVienResp nv = new NhanVienResp();
//        try {
//            String gt = (String) cbxGioiTinh.getSelectedItem();
//            if (gt != null && !gt.isEmpty()) {
//               
//                int genderValue = gt.equals("Nam") ? 1 : 0;
//                nv.setGioiTinh(genderValue);
//
//                List<NhanVienResp> list = repo.loc01(nv.getGioiTinh());
//                fillTableDangLam(list);
//            } else {              
//                fillTableDangLam(repo.getAllDangLam());
//            }
//        } catch (Exception e) {
//            fillTableDangLam(repo.getAllDangLam());
//        }

        NhanVienResp nv = new NhanVienResp();
        try {
            String gt = (String) cbxGioiTinh.getSelectedItem();
            ChucVu cv = null;
            if (cbxLocVaiTro.getSelectedItem() instanceof ChucVu) {
                cv = (ChucVu) cbxLocVaiTro.getSelectedItem();
            }
            boolean genderSelected = gt != null && !gt.isEmpty();
            boolean roleSelected = cv != null;

            if (genderSelected || roleSelected) {
                Integer genderValue = null;
                if (genderSelected) {
                    genderValue = gt.equals("Nam") ? 1 : 0;
                    nv.setGioiTinh(genderValue);
                }

                Integer roleId = null;
                if (roleSelected) {
                    roleId = cv.getId();
                }

                List<NhanVienResp> list = repo.loc(roleId, genderValue);
                fillTableDangLam(list);
            } else {
                fillTableDangLam(repo.getAllDangLam());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fillTableDangLam(repo.getAllDangLam());
        }


    }//GEN-LAST:event_cbxGioiTinhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.Button btnReset;
    private com.raven.swing.Button btnSua;
    private com.raven.swing.Button btnThem;
    private com.raven.swing.Button btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cbxGioiTinh;
    private javax.swing.JComboBox<String> cbxLocVaiTro;
    private javax.swing.JComboBox<String> cbxVaiTro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdoDangLamViec;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNghiViec;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tbDangLam;
    private javax.swing.JTable tbNghiViec;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMatKhau;
    private com.toedter.calendar.JDateChooser txtNgayBatDau;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtsearch;
    // End of variables declaration//GEN-END:variables
}
