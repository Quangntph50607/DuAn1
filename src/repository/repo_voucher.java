/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import entity.Voucher;
import java.sql.*;
import config.*;
import java.util.Date;

/**
 *
 * @author Ca1
 */
public class repo_voucher {

    public static ArrayList<Voucher> getListVoucherFromDb() {
        Connection sConn = DBConnect.getConnection();
        String query = "SELECT * FROM Voucher";
        ArrayList<Voucher> vouchers = new ArrayList<>();

        try {
            Statement stm = sConn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                Voucher voucher = new Voucher();

                voucher.setIdMa(rs.getInt("ID"));
                voucher.setMaVoucher(rs.getString("ma_voucher"));
                voucher.setTenVoucher(rs.getString("ten_voucher"));
                voucher.setSoLuongVoucher(rs.getInt("so_luong_voucher"));
                voucher.setGioiHanGiamToiThieu(rs.getDouble("dieu_kien_giam"));
                voucher.setGioiHanGiamToiDa(rs.getDouble("gioi_han_giam_toi_da"));
                voucher.setNgayBatDau(rs.getDate("ngay_bat_dau"));
                voucher.setNgayKetThuc(rs.getDate("ngay_ket_thuc"));
                voucher.setHinhThucGiam(rs.getString("hinh_thuc_giam"));
                voucher.setTrangThai(rs.getInt("trang_thai"));
                voucher.setMucGia(rs.getInt("giam_gia"));

                vouchers.add(voucher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    ;
    
     public Voucher getMotVc(String mavc) {
        String sql = """
        SELECT [ma_voucher]
              ,[ten_voucher]
              ,[so_luong_voucher]
              ,[dieu_kien_giam]
              ,[gioi_han_giam_toi_da]
              ,[giam_gia]
              ,[ngay_bat_dau]
              ,[ngay_ket_thuc]
              ,[hinh_thuc_giam]
              ,[trang_thai]
          FROM [dbo].[Voucher] where [ma_voucher] = ?
           """;
        Voucher vc = new Voucher();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, mavc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                vc.setMaVoucher(rs.getString(1));
                vc.setTenVoucher(rs.getString(2));
                vc.setSoLuongVoucher(rs.getInt(3));
                vc.setGioiHanGiamToiThieu(rs.getInt(4));
                vc.setGioiHanGiamToiDa(rs.getInt(5));
                vc.setMucGia(rs.getInt(6));
                vc.setNgayBatDau(rs.getDate(7));
                vc.setNgayKetThuc(rs.getDate(8));
                vc.setHinhThucGiam(rs.getString(9));
                vc.setTrangThai(rs.getInt(10));
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return vc;
    }

    public Voucher getHinhThucGiam(String mavc) {
        String sql = """
        SELECT [ma_voucher]
              ,[ten_voucher]
              ,[so_luong_voucher]
              ,[dieu_kien_giam]
              ,[gioi_han_giam_toi_da]
              ,[giam_gia]
              ,[ngay_bat_dau]
              ,[ngay_ket_thuc]
              ,[hinh_thuc_giam]
              ,[trang_thai]
          FROM [dbo].[Voucher] where [ma_voucher] = ?
           """;
        Voucher vc = new Voucher();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, mavc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                vc.setMaVoucher(rs.getString(1));
                vc.setTenVoucher(rs.getString(2));
                vc.setSoLuongVoucher(rs.getInt(3));
                vc.setGioiHanGiamToiThieu(rs.getInt(4));
                vc.setGioiHanGiamToiDa(rs.getInt(5));
                vc.setMucGia(rs.getInt(6));
                vc.setNgayBatDau(rs.getDate(7));
                vc.setNgayKetThuc(rs.getDate(8));
                vc.setHinhThucGiam(rs.getString(9));
                vc.setTrangThai(rs.getInt(10));
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return vc;
    }

    public void addVoucherToDB(Voucher voucher) {
        java.util.Date utilToday = new java.util.Date();
        Date today = new Date(utilToday.getTime());

        Date ngayBatDau = voucher.getNgayBatDau();
        Date ngayKetThuc = voucher.getNgayKetThuc();
        int trangThai;

        if (today.before(ngayBatDau)) {
            trangThai = 1; // Sắp diễn ra
        } else if (today.after(ngayKetThuc)) {
            trangThai = 0; // Đã kết thúc
        } else {
            trangThai = 2; // Đang diễn ra
        }

        // Cập nhật trạng thái vào đối tượng voucher
        voucher.setTrangThai(trangThai);

        Connection sConn = DBConnect.getConnection();
        String query = "INSERT INTO dbo.Voucher\n"
                + "(\n"
                + "    ma_voucher,\n"
                + "    ten_voucher,\n"
                + "    so_luong_voucher,\n"
                + "    dieu_kien_giam,\n"
                + "    gioi_han_giam_toi_da,\n"
                + "    ngay_bat_dau,\n"
                + "    ngay_ket_thuc,\n"
                + "    hinh_thuc_giam,\n"
                + "    trang_thai,\n"
                + "    giam_gia\n"
                + ")\n"
                + "VALUES\n"
                + "(   ?, -- ma_voucher - varchar(10)\n"
                + "    ?, -- ten_voucher - nvarchar(50)\n"
                + "    ?, -- so_luong_voucher - int\n"
                + "    ?, -- dieu_kien_giam - money\n"
                + "    ?, -- gioi_han_giam_toi_da - money\n"
                + "    ?, -- ngay_bat_dau - date\n"
                + "    ?, -- ngay_ket_thuc - date\n"
                + "    ?, -- hinh_thuc_giam - nvarchar(50)\n"
                + "    ?,  -- trang_thai - int\n"
                + "    ?  -- giam_gia - int\n"
                + "    )";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, voucher.getMaVoucher());
            stm.setObject(2, voucher.getTenVoucher());
            stm.setObject(3, voucher.getSoLuongVoucher());
            stm.setDouble(4, voucher.getGioiHanGiamToiThieu());
            stm.setDouble(5, voucher.getGioiHanGiamToiDa());
            stm.setObject(6, voucher.getNgayBatDau());
            stm.setObject(7, voucher.getNgayKetThuc());
            stm.setObject(8, voucher.getHinhThucGiam());
            stm.setObject(9, voucher.getTrangThai());
            stm.setObject(10, voucher.getMucGia());
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateVoucherToDb(Voucher voucher) {
        Connection sConn = DBConnect.getConnection();
        String query = "UPDATE dbo.Voucher\n"
                + "SET \n"
                + "    ten_voucher = ?,       -- nvarchar(50)\n"
                + "    so_luong_voucher = ?,  -- int\n"
                + "    dieu_kien_giam = ?, -- money\n"
                + "    gioi_han_giam_toi_da = ?,    -- money\n"
                + "    ngay_bat_dau = ?,      -- date\n"
                + "    ngay_ket_thuc = ?,     -- date\n"
                + "    hinh_thuc_giam = ?,    -- nvarchar(50)\n"
                + "    trang_thai = ?,         -- int\n"
                + "    giam_gia = ?         -- int\n"
                + "WHERE \n"
                + "    ma_voucher = ?         -- varchar(10)";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, voucher.getTenVoucher());
            stm.setObject(2, voucher.getSoLuongVoucher());
            stm.setDouble(3, voucher.getGioiHanGiamToiThieu());
            stm.setDouble(4, voucher.getGioiHanGiamToiDa());
            stm.setObject(5, voucher.getNgayBatDau());
            stm.setObject(6, voucher.getNgayKetThuc());
            stm.setObject(7, voucher.getHinhThucGiam());
            stm.setObject(8, voucher.getTrangThai());
            stm.setObject(9, voucher.getMucGia());
            stm.setObject(10, voucher.getMaVoucher());
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeVoucherToDb(String maVoucher) {
        Connection sConn = DBConnect.getConnection();
        String query = "UPDATE dbo.Voucher SET trang_thai = -1 WHERE ma_voucher = ?";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, maVoucher);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTrangThaiHetHan(String maVoucher) {
        Connection sConn = DBConnect.getConnection();
        String query = "UPDATE dbo.Voucher SET trang_thai = 0 WHERE ma_voucher = ?";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, maVoucher);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTrangThaiDangDienRa(String maVoucher) {
        Connection sConn = DBConnect.getConnection();
        String query = "UPDATE dbo.Voucher SET trang_thai = 1 WHERE ma_voucher = ?";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, maVoucher);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTrqngThaiSapDienRa(String maVoucher) {
        Connection sConn = DBConnect.getConnection();
        String query = "UPDATE dbo.Voucher SET trang_thai = 2 WHERE ma_voucher = ?";
        try {
            PreparedStatement stm = sConn.prepareStatement(query);
            stm.setObject(1, maVoucher);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTrangThai(String maVoucher) {
        Connection sConn = DBConnect.getConnection();
        try {
            CallableStatement csmt = sConn.prepareCall("{call pc_update_trang_thai_ngay(?,?,?)}");
            csmt.setObject(1, maVoucher);
            csmt.registerOutParameter(2, Types.NVARCHAR);
            csmt.registerOutParameter(3, Types.INTEGER);
            csmt.execute();

        } catch (Exception e) {
        }
    }

    public String getTrangThai(String maVoucher) {
        Connection sConn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            int trangThai = -1;
            sConn = DBConnect.getConnection();
            String query = "SELECT trang_thai FROM dbo.Voucher WHERE ma_voucher = ?";
            stm = sConn.prepareStatement(query);
            stm.setString(1, maVoucher);

            rs = stm.executeQuery();

            if (rs.next()) {
                trangThai = rs.getInt("trang_thai");
            }
            switch (trangThai) {
                case 0:
                    return "Đã kết thúc";
                case 1:
                    return "Sắp diễn ra";
                case 2:
                    return "Đang diễn ra";
            }
        }
        catch (Exception e) {
            
        }
        return "Null";
    }
}
