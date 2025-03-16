/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author Admin
 */
import entity.HoaDon;
import entity.HoaDonChiTiet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import config.DBConnect;
import entity.SanPhamChiTiet;
import java.util.Date;
import java.util.List;
public class HDRepository {

    Connection conn;

    public HDRepository() {
        try {
            conn = DBConnect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setThongTinKH(int idHoaDon, int idKhachHang, String tenKhachHang, String diaChiKhachHang, String soDt) {
        String sql = "UPDATE Hoa_don SET id_khach_hang = ?, ten_khach_hang = ?, dia_chi_khach_hang = ?, so_dt = ? WHERE ID = ? AND trang_thai = 1";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idKhachHang);
            ps.setString(2, tenKhachHang);
            ps.setString(3, diaChiKhachHang);
            ps.setString(4, soDt);
            ps.setInt(5, idHoaDon);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<HoaDon> findall() {
        ArrayList<HoaDon> listHD = new ArrayList<>();
        String sql = "SELECT * FROM Hoa_don";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String ma = rs.getString("ma_hoa_don");
                int idKH = rs.getInt("id_khach_hang");
                int idNV = rs.getInt("id_nhan_vien");
                String ten = rs.getString("ten_khach_hang");
                String diaChi = rs.getString("dia_chi_khach_hang");
                String sdt = rs.getString("so_dt");
                Date ngay = rs.getDate("ngay_tao");
                float tongTien = rs.getFloat("tong_tien");
                String hinhThucTT = rs.getString("hinh_thuc_thanh_toan");
                int idVoucher = rs.getInt("id_voucher");
                int trangThai = rs.getInt("trang_thai");
                HoaDon hd = new HoaDon(id, ma, idKH, idNV, ten, diaChi, sdt, ngay, tongTien, hinhThucTT, idVoucher, trangThai);
                listHD.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHD;
    }

    public ArrayList<HoaDonChiTiet> findChiTietByidHD(int idHD) {
        ArrayList<HoaDonChiTiet> listHDCT = new ArrayList<>();
        String sql = """
                     select Hoa_don_chi_tiet.ID,ma_hoa_don_chi_tiet,id_hoa_don,id_san_pham_chi_tiet,Hoa_don_chi_tiet.so_luong,spct.don_gia,thanh_tien,Hoa_don_chi_tiet.trang_thai from Hoa_don_chi_tiet 
                     join San_pham_chi_tiet spct on Hoa_don_chi_tiet.id_san_pham_chi_tiet = spct.id
                     where id_hoa_don = ?
                     """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDonChiTiet hdct = new HoaDonChiTiet();
                hdct.setId(rs.getInt(1));
                hdct.setMa(rs.getString(2));
                hdct.setIdHD(rs.getInt(3));
                hdct.setIdSP(rs.getInt(4));
                hdct.setSl(rs.getInt(5));
                hdct.setDonGia(rs.getFloat(6));
                hdct.setThanhTien(rs.getFloat(7));
                hdct.setTrangThai(rs.getInt(8));
                listHDCT.add(hdct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHDCT;
    }

    public ArrayList<HoaDon> search(String keyword) {
        ArrayList<HoaDon> listHD = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Hoa_don WHERE ma_hoa_don LIKE ? OR ten_khach_hang LIKE ? OR so_dt LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String key = "%" + keyword + "%";
            ps.setObject(1, key);
            ps.setObject(2, key);
            ps.setObject(3, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String ma = rs.getString("ma_hoa_don");
                int idKH = rs.getInt("id_khach_hang");
                int idNV = rs.getInt("id_nhan_vien");
                String ten = rs.getString("ten_khach_hang");
                String diaChi = rs.getString("dia_chi_khach_hang");
                String sdt = rs.getString("so_dt");
                Date ngay = rs.getDate("ngay_tao");
                float tongTien = rs.getFloat("tong_tien");
                String hinhThucTT = rs.getString("hinh_thuc_thanh_toan");
                int idVoucher = rs.getInt("id_voucher");
                int trangThai = rs.getInt("trang_thai");
                HoaDon hd = new HoaDon(id, ma, idKH, idNV, ten, diaChi, sdt, ngay, tongTien, hinhThucTT, idVoucher, trangThai);
                listHD.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHD;
    }

    public ArrayList<HoaDon> fillHoaDon(Integer trangThai, String hinhThucTT, Double tongTienMin, Double tongTienMax, Integer thang, Integer nam) {
        ArrayList<HoaDon> listHD = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Hoa_don WHERE 1=1");
        if (trangThai != null) {
            sql.append(" AND trang_thai = ?");
        }
        if (hinhThucTT != null && !hinhThucTT.isEmpty()) {
            sql.append(" AND hinh_thuc_thanh_toan = ?");
        }
        if (tongTienMin != null) {
            sql.append(" AND tong_tien > ?");
        }
        if (tongTienMax != null) {
            sql.append(" AND tong_tien <= ?");
        }
        if (thang != null) {
            sql.append(" AND MONTH(ngay_tao) = ?");
        }
        if (nam != null) {
            sql.append(" AND YEAR(ngay_tao) = ?");
        }
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (trangThai != null) {
                ps.setInt(i++, trangThai);
            }
            if (hinhThucTT != null && !hinhThucTT.isEmpty()) {
                ps.setString(i++, hinhThucTT);
            }
            if (tongTienMin != null) {
                ps.setDouble(i++, tongTienMin);
            }
            if (tongTienMax != null) {
                ps.setDouble(i++, tongTienMax);
            }
            if (thang != null) {
                ps.setInt(i++, thang);
            }
            if (nam != null) {
                ps.setInt(i++, nam);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setIdHoaDon(rs.getInt("ID"));
                    hd.setMaHoaDon(rs.getString("ma_hoa_don"));
                    hd.setIdKhachHang(rs.getInt("id_khach_hang"));
                    hd.setIdNhanVien(rs.getInt("id_nhan_vien"));
                    hd.setTenKhachHang(rs.getString("ten_khach_hang"));
                    hd.setDiaChi(rs.getString("dia_chi_khach_hang"));
                    hd.setSdt(rs.getString("so_dt"));
                    hd.setNgayTao(rs.getDate("ngay_tao"));
                    hd.setTongTien(rs.getFloat("tong_tien"));
                    hd.setHinhThucThanhToan(rs.getString("hinh_thuc_thanh_toan"));
                    hd.setIdVoucher(rs.getInt("id_voucher"));
                    hd.setTrangThai(rs.getInt("trang_thai"));
                    listHD.add(hd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHD;
    }

    public String getTenNV(int idNV) {
        String tenNV = "";
        try {
            String sql = "SELECT ho_ten  FROM Nhan_vien WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tenNV = rs.getString("ho_ten");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tenNV;
    }

    public Float getGiaTriVoucher(int idV, Float tongTien) {
        float giaTriVoucher = 0;
        float dieuKienGiam = 0;
        String hinhThucGiam = "";
        float giamGia = 0;
        float gioiHanGiam = 0;
        try {
            String sql = "SELECT dieu_kien_giam, hinh_thuc_giam, giam_gia, gioi_han_giam_toi_da FROM Voucher WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dieuKienGiam = rs.getFloat("dieu_kien_giam");
                hinhThucGiam = rs.getString("hinh_thuc_giam");
                giamGia = rs.getFloat("giam_gia");
                gioiHanGiam = rs.getFloat("gioi_han_giam_toi_da");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tongTien < dieuKienGiam) {
            giaTriVoucher = 0;
        } else {
            if (hinhThucGiam.equals("Giảm giá theo số tiền")) {
                giaTriVoucher = giamGia;
            } else if (hinhThucGiam.equals("Giảm giá theo %")) {
                giaTriVoucher = (tongTien * giamGia) / 100;
            }
        }
        if (giaTriVoucher > gioiHanGiam) {
            giaTriVoucher = gioiHanGiam;
        }
        return giaTriVoucher;
    }

    public String getTenSPCT(int idHoaDonChiTiet) {
    String tenSPCT = "";
    try {
        String sql = """
            SELECT ten_san_pham_chi_tiet FROM San_pham_chi_tiet spct
            JOIN Hoa_don_chi_tiet hdct ON hdct.id_san_pham_chi_tiet = spct.ID
            WHERE hdct.ID = ?
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idHoaDonChiTiet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            tenSPCT = rs.getString("ten_san_pham_chi_tiet");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return tenSPCT;
}


    public boolean UpdateKhbyMaHd(String maHd, Integer idKh, String diaChiKh, String tenKh, String sdt) {
        int rowsAffected = 0;
        String sql = """
                     update Hoa_don
                     	set id_khach_hang = ?,
                     	dia_chi_khach_hang = ?,
                     	ten_khach_hang = ?,
                     	so_dt = ?
                     	where ma_hoa_don = ?
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, idKh);
            ps.setObject(2, diaChiKh);
            ps.setObject(3, tenKh);
            ps.setObject(4, sdt);
            ps.setObject(5, maHd);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }
}
