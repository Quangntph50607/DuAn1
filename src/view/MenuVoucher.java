/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.util.ArrayList;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import entity.Voucher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.JOptionPane;
import logic.RandomStringGenerator;
import repository.repo_voucher;
import logic.Validate;

/**
 *
 * @author DUNG
 */
public class MenuVoucher extends javax.swing.JInternalFrame {

    TableRowSorter<TableModel> sorterSearch;
    DefaultTableModel model = null;
    ArrayList<Voucher> vouchers = null;
    repo_voucher repo_voucher = new repo_voucher();

    /**
     * Creates new form MenuVoucher
     */
    public MenuVoucher() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        model = (DefaultTableModel) tblVoucher.getModel();
        sorterSearch = new TableRowSorter<>(model);
        tblVoucher.setRowSorter(sorterSearch);

        rdDangDienRa.setSelected(true);
        runThreadCheckNgayKetThuc();
        loadListVoucherToTable();

        cboxHinhThucGiam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cboxHinhThucGiam.getSelectedIndex() == 1) {
                    txtMucGiam.setLabelText("Mức giảm (%)");
                } else {
                    txtMucGiam.setLabelText("Mức giảm (VND)");
                }
            }
        });

        txtSearchVoucher.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txtSearchVoucher.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txtSearchVoucher.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txtSearchVoucher.getText());
            }

            private void search(String text) {
                sorterSearch.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
    }

    public static String tachSo(String chuoi) {
        StringBuilder soChuoi = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(chuoi);

        while (matcher.find()) {
            soChuoi.append(matcher.group());
        }

        return soChuoi.toString();
    }

    private void runThreadCheckNgayKetThuc() {
        Thread checkNgayKetThuc = new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (Voucher i : repo_voucher.getListVoucherFromDb()) {
                        repo_voucher.updateTrangThai(i.getMaVoucher());
                    }
                    loadListVoucherToTable();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        checkNgayKetThuc.start();
    }

    public void loadListVoucherToTable() {
        vouchers = repo_voucher.getListVoucherFromDb();
        model.setRowCount(0);
        for (Voucher i : vouchers) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateStart = simpleDateFormat.format(i.getNgayBatDau());
            String dateEnd = simpleDateFormat.format(i.getNgayKetThuc());
            String trangThai = "null";
            switch (i.getTrangThai()) {
                case 0:
                    trangThai = "Đã kết thúc  ";
                    break;
                case 1:
                    trangThai = "Sap dien ra ";
                    break;
                case 2:
                    trangThai = "Dang dien ra ";
                    break;
            };
            String mucGiam = formatMucGiam(i.getMucGia());
            if (rdDangDienRa.isSelected() && i.getTrangThai() == 2) {
                model.addRow(new Object[]{
                    i.getMaVoucher(), i.getTenVoucher(), i.getSoLuongVoucher(), i.getGioiHanGiamToiThieu(), i.getGioiHanGiamToiDa(), mucGiam, dateStart, dateEnd, i.getHinhThucGiam(), trangThai});

            } else if (rdSapDienRa.isSelected() && i.getTrangThai() == 1) {
                model.addRow(new Object[]{
                    i.getMaVoucher(), i.getTenVoucher(), i.getSoLuongVoucher(), i.getGioiHanGiamToiThieu(), i.getGioiHanGiamToiDa(), mucGiam, dateStart, dateEnd, i.getHinhThucGiam(), trangThai});

            } else if (rdDaKetThuc.isSelected() && i.getTrangThai() == 0) {

                model.addRow(new Object[]{
                    i.getMaVoucher(), i.getTenVoucher(), i.getSoLuongVoucher(), i.getGioiHanGiamToiThieu(), i.getGioiHanGiamToiDa(), mucGiam, dateStart, dateEnd, i.getHinhThucGiam(), trangThai});

            }
        }
    }

    public String formatMucGiam(int mucGiam) {
        if (mucGiam <= 100) {
            return mucGiam + "%";
        } else {
            return mucGiam + "VND";
        }
    }

    public Voucher getVoucherFormMa(String maVoucher) {
        for (Voucher i : vouchers) {
            if (i.getMaVoucher().equals(maVoucher)) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<String> getColumnNameVoucher() {
        TableColumnModel columnModel = tblVoucher.getColumnModel();
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String columnName = columnModel.getColumn(i).getHeaderValue().toString();
            columnNames.add(columnName);
        }
        return columnNames;
    }

    private Voucher getNewVoucherFromForm() {
        Date dateStart = dateLocNgayBatDau.getDate();
        Date dateEnd = dateLocNgayKetThuc.getDate();
        Voucher voucher = Voucher.builder()
                .maVoucher(RandomStringGenerator.generateRandomString("VC"))
                .tenVoucher(txtTenVoucher.getText())
                .hinhThucGiam(cboxHinhThucGiam.getSelectedItem().toString())
                .soLuongVoucher(Integer.parseInt(txtSoLuong.getText()))
                .gioiHanGiamToiThieu(Double.parseDouble(txtGioiHanGiamToiThieu.getText()))
                .gioiHanGiamToiDa(Double.parseDouble(txtGioiHanGiamToiDa.getText()))
                .ngayBatDau(dateStart)
                .ngayKetThuc(dateEnd)
                .mucGia(Integer.parseInt(txtMucGiam.getText()))
                .build();
        return voucher;
    }

    private Validate check() {
        Validate validate = new Validate();
        validate.khongDuocTrong(txtTenVoucher, txtMucGiam, txtGioiHanGiamToiThieu, txtGioiHanGiamToiDa, txtSoLuong);
        validate.chiDuocChuaSo(txtMucGiam, txtGioiHanGiamToiThieu, txtGioiHanGiamToiDa, txtSoLuong);
        validate.phaiLonHon0(txtSoLuong, txtGioiHanGiamToiThieu, txtGioiHanGiamToiDa);
        validate.khongDuocLaSoAm(txtMucGiam);
        if (cboxHinhThucGiam.getSelectedIndex() == 0) {
            validate.mucGiamTheoSoTienPhaiLonHon1000VND(txtMucGiam);
        } else {
            validate.mucGiamTheoPhanTramPhaiLonHon0VaBeHon100(txtMucGiam);
        }
        validate.soThuNhatPhaiNhoHonSoThuHai(txtGioiHanGiamToiDa, txtGioiHanGiamToiThieu);
        validate.soThuNhatPhaiNhoHonSoThuHai(txtMucGiam, txtGioiHanGiamToiDa);
        validate.khongDuocTrong("Thời gian bắt đầu", dateLocNgayBatDau.getDate() + "");
        validate.khongDuocTrong("Thời gian kết thúc", dateLocNgayKetThuc.getDate() + "");
        validate.checkDateIsAfterOrEqualCurrent(dateLocNgayBatDau);
        validate.checkDateIsBefore(dateLocNgayBatDau, dateLocNgayKetThuc);
        return validate;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVoucher = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        rdDangDienRa = new javax.swing.JRadioButton();
        rdDaKetThuc = new javax.swing.JRadioButton();
        rdSapDienRa = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        btnReset = new javax.swing.JButton();
        btnXoaVoucher = new javax.swing.JButton();
        btnSuaVoucher = new javax.swing.JButton();
        btnThemVoucher = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboxHinhThucGiam = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        dateLocNgayBatDau = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dateLocNgayKetThuc = new com.toedter.calendar.JDateChooser();
        txtTenVoucher = new com.raven.swing.TextField();
        txtGioiHanGiamToiThieu = new com.raven.swing.TextField();
        txtGioiHanGiamToiDa = new com.raven.swing.TextField();
        txtMucGiam = new com.raven.swing.TextField();
        txtSoLuong = new com.raven.swing.TextField();
        jPanel4 = new javax.swing.JPanel();
        txtSearchVoucher = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        tblVoucher.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khuyến mãi", "Tên voucher", "Số lượng", "Điều kiện giảm", "Giới hạn giảm tối đa", "Mức Giảm", "Ngày bắt đầu", "Ngày kết thúc", "Hình thức giảm", "Trạng thái"
            }
        ));
        tblVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVoucherMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVoucher);
        if (tblVoucher.getColumnModel().getColumnCount() > 0) {
            tblVoucher.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroup1.add(rdDangDienRa);
        rdDangDienRa.setText("Đang diễn ra");
        rdDangDienRa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDangDienRaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdDaKetThuc);
        rdDaKetThuc.setText("Đã kết thúc");
        rdDaKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDaKetThucActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdSapDienRa);
        rdSapDienRa.setText("Sap dien ra");
        rdSapDienRa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSapDienRaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdDangDienRa)
                .addGap(12, 12, 12)
                .addComponent(rdSapDienRa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdDaKetThuc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdDangDienRa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rdDaKetThuc)
                    .addComponent(rdSapDienRa))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnReset.setBackground(new java.awt.Color(255, 204, 102));
        btnReset.setText("Làm mới");
        btnReset.setBorderPainted(false);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnXoaVoucher.setBackground(new java.awt.Color(255, 204, 102));
        btnXoaVoucher.setText("Xóa");
        btnXoaVoucher.setBorderPainted(false);
        btnXoaVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaVoucherActionPerformed(evt);
            }
        });

        btnSuaVoucher.setBackground(new java.awt.Color(255, 204, 102));
        btnSuaVoucher.setText("Sửa ");
        btnSuaVoucher.setBorderPainted(false);
        btnSuaVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaVoucherActionPerformed(evt);
            }
        });

        btnThemVoucher.setBackground(new java.awt.Color(255, 204, 102));
        btnThemVoucher.setText("Thêm ");
        btnThemVoucher.setBorderPainted(false);
        btnThemVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemVoucherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnThemVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSuaVoucher, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("Hình thức giảm");

        cboxHinhThucGiam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giảm giá theo số tiền", "Giảm giá theo %", "" }));

        jLabel5.setText("Thời gian bắt đầu ");

        dateLocNgayBatDau.setDateFormatString("dd-MM-yyyy");

        jLabel6.setText("Thời gian kết thúc");

        dateLocNgayKetThuc.setDateFormatString("dd-MM-yyyy");

        txtTenVoucher.setLabelText("Tên voucher");

        txtGioiHanGiamToiThieu.setLabelText("Điều kiện giảm");

        txtGioiHanGiamToiDa.setLabelText("Giới hạn giảm tối đa");

        txtMucGiam.setLabelText("Mức giảm");

        txtSoLuong.setLabelText("Số lượng");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboxHinhThucGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtGioiHanGiamToiThieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateLocNgayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                                .addGap(130, 130, 130)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLocNgayKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                            .addComponent(txtGioiHanGiamToiDa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtMucGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenVoucher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGioiHanGiamToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGioiHanGiamToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboxHinhThucGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLocNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLocNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMucGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSearchVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchVoucherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(txtSearchVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(txtSearchVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tìm kiếm:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(70, 70, 70))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemVoucherActionPerformed
        Validate validate = check();
        int i = JOptionPane.showConfirmDialog(this, "Bạn có muốn thêm voucher này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            if (validate.isChuoiHopLe()) {
                Voucher voucher = getNewVoucherFromForm();
                repo_voucher.addVoucherToDB(voucher);

                loadListVoucherToTable();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
            } else {
                validate.showWarning(this);
            }
        }

    }//GEN-LAST:event_btnThemVoucherActionPerformed

    private void btnSuaVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaVoucherActionPerformed
        int index = tblVoucher.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn dòng muốn sửa");
        } else {

            Validate validate = check();
            int i = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa voucher này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                if (validate.isChuoiHopLe()) {
                    Voucher voucher = getNewVoucherFromForm();
                    String maVoucher = tblVoucher.getValueAt(index, 0).toString();
                    voucher.setMaVoucher(maVoucher);
                    repo_voucher.updateVoucherToDb(voucher);
                    loadListVoucherToTable();
                    JOptionPane.showMessageDialog(rootPane, "Sửa thành công");
                } else {
                    validate.showWarning(this);
                }
            }

        }

    }//GEN-LAST:event_btnSuaVoucherActionPerformed

    private void rdDaKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDaKetThucActionPerformed
        loadListVoucherToTable();
    }//GEN-LAST:event_rdDaKetThucActionPerformed

    private void tblVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVoucherMouseClicked
        int index = tblVoucher.getSelectedRow();
        if (index >=0) {
            Voucher voucher = getVoucherFormMa(tblVoucher.getValueAt(index, 0).toString());

        txtTenVoucher.setText(voucher.getTenVoucher());
        cboxHinhThucGiam.setSelectedItem(voucher.getHinhThucGiam());
        txtGioiHanGiamToiDa.setText(voucher.getGioiHanGiamToiDa() + "");
        txtGioiHanGiamToiThieu.setText(voucher.getGioiHanGiamToiThieu() + "");
        dateLocNgayBatDau.setDate(voucher.getNgayBatDau());
        dateLocNgayKetThuc.setDate(voucher.getNgayKetThuc());
        txtMucGiam.setText(tachSo(voucher.getMucGia() + ""));
        txtSoLuong.setText(voucher.getSoLuongVoucher() + "");
        }
        
    }//GEN-LAST:event_tblVoucherMouseClicked

    private void btnXoaVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaVoucherActionPerformed
        int index = tblVoucher.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn dòng muốn xóa");
        } else {
            int i = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa voucher này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                String maVoucher = tblVoucher.getValueAt(index, 0).toString();
                repo_voucher.removeVoucherToDb(maVoucher);
                loadListVoucherToTable();
                JOptionPane.showMessageDialog(rootPane, "Xóa thành công");
            }

        }
    }//GEN-LAST:event_btnXoaVoucherActionPerformed

    private void rdDangDienRaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDangDienRaActionPerformed
        loadListVoucherToTable();
    }//GEN-LAST:event_rdDangDienRaActionPerformed

    private void txtSearchVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchVoucherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchVoucherActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtTenVoucher.setText("");
        txtGioiHanGiamToiDa.setText("");
        txtGioiHanGiamToiThieu.setText("");
        dateLocNgayBatDau.setDate(null);
        dateLocNgayKetThuc.setDate(null);
        txtMucGiam.setText("");
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void rdSapDienRaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSapDienRaActionPerformed
        loadListVoucherToTable();
    }//GEN-LAST:event_rdSapDienRaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSuaVoucher;
    private javax.swing.JButton btnThemVoucher;
    private javax.swing.JButton btnXoaVoucher;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboxHinhThucGiam;
    private com.toedter.calendar.JDateChooser dateLocNgayBatDau;
    private com.toedter.calendar.JDateChooser dateLocNgayKetThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdDaKetThuc;
    private javax.swing.JRadioButton rdDangDienRa;
    private javax.swing.JRadioButton rdSapDienRa;
    private javax.swing.JTable tblVoucher;
    private com.raven.swing.TextField txtGioiHanGiamToiDa;
    private com.raven.swing.TextField txtGioiHanGiamToiThieu;
    private com.raven.swing.TextField txtMucGiam;
    private javax.swing.JTextField txtSearchVoucher;
    private com.raven.swing.TextField txtSoLuong;
    private com.raven.swing.TextField txtTenVoucher;
    // End of variables declaration//GEN-END:variables
}
