/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import entity.KhachHang;
import config.DBConnect;
import java.util.List;
import request.KhachHangRequest;
public class KhachHangRepo {
    private Connection conn;

    public KhachHangRepo() {
        try {
            conn = DBConnect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<KhachHang> findAll() throws SQLException {
       ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM Khach_hang";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt(1));
                kh.setMaKH(rs.getString(2));
                kh.setTenKH(rs.getString(3));
                kh.setGioiTinh(rs.getBoolean(4));
                kh.setSdt(rs.getString(5));
                kh.setEmail(rs.getString(6));
                kh.setDiaChi(rs.getString(7));
                kh.setNgaySinh(rs.getDate(8));
                kh.setTrangThai(rs.getInt(9));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkTrungMa(String maKH) {
        String sql = "SELECT COUNT(*) FROM Khach_hang WHERE ma_khach_hang = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkTrungSDT(String sdt) {
        String sql = "SELECT COUNT(*) FROM Khach_hang WHERE so_dt = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkTrungEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Khach_hang WHERE email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean add(KhachHang kh) {
        int check = 0;
        String sql = "INSERT INTO Khach_hang (ma_khach_hang, ten_khach_hang, gioi_tinh, so_dt, dia_chi, email, ngay_sinh, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, kh.getMaKH());
            ps.setObject(2, kh.getTenKH());
            ps.setObject(3, kh.getGioiTinh());
            ps.setObject(4, kh.getSdt());
            ps.setObject(5, kh.getDiaChi());
            ps.setObject(6, kh.getEmail());
            ps.setObject(7, kh.getNgaySinh());
            ps.setObject(8, kh.getTrangThai());
            ps.executeUpdate();
            check = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public ArrayList<KhachHang> search(String keyword) throws SQLException {
        ArrayList<KhachHang> list = new ArrayList<>();
        String query = "SELECT * FROM Khach_hang WHERE ma_khach_hang LIKE ? OR ten_khach_hang LIKE ? OR so_dt LIKE ? OR dia_chi LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setId(rs.getInt("ID"));
                    kh.setMaKH(rs.getString("ma_khach_hang"));
                    kh.setTenKH(rs.getString("ten_khach_hang"));
                    kh.setGioiTinh(rs.getBoolean("gioi_tinh"));
                    kh.setSdt(rs.getString("so_dt"));
                    kh.setDiaChi(rs.getString("dia_chi"));
                    kh.setEmail(rs.getString("email"));
                    kh.setNgaySinh(rs.getDate("ngay_sinh"));
                    kh.setTrangThai(rs.getInt("trang_thai"));
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }
    
    
    
}
