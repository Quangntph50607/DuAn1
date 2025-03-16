/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import entity.KhachHang;
import config.DBConnect;

/**
 *
 * @author adim
 */
public class KhachHangService {
      private Connection conn;
    public KhachHangService(){
        conn = DBConnect.getConnection();
    }
    
    public ArrayList<KhachHang> getData() throws SQLException{
        ArrayList<KhachHang> list = new ArrayList<>();
        String query = "SELECT * FROM Khach_hang";
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
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
        return list;
    }
    public boolean isMaKHExist(String maKH) throws SQLException {
    String query = "SELECT COUNT(*) FROM Khach_hang WHERE ma_khach_hang = ?";
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, maKH);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}
    
    public void insert(KhachHang kh) throws SQLException {
        String query = "INSERT INTO Khach_hang (ma_khach_hang, ten_khach_hang, gioi_tinh, so_dt, dia_chi, email, ngay_sinh, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             ps.setObject(1,kh.getMaKH());
             ps.setObject(2,kh.getTenKH());
             ps.setObject(3,kh.getGioiTinh());
             ps.setObject(4,kh.getSdt());
             ps.setObject(5,kh.getDiaChi());
             ps.setObject(6,kh.getEmail());
             ps.setObject(7,kh.getNgaySinh());
             ps.setObject(8,kh.getTrangThai());
             ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public void update( KhachHang kh) throws SQLException{
        String query = "UPDATE Khach_hang SET ma_khach_hang=?, ten_khach_hang=?, gioi_tinh=?, so_dt=?, dia_chi=?, email=?, ngay_sinh=?, trang_thai=? WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             ps.setObject(1,kh.getMaKH());
             ps.setObject(2,kh.getTenKH());
             ps.setObject(3,kh.getGioiTinh());
             ps.setObject(4,kh.getSdt());
             ps.setObject(5,kh.getDiaChi());
             ps.setObject(6,kh.getEmail());
             ps.setObject(7,kh.getNgaySinh());
             ps.setObject(8,kh.getTrangThai());
             ps.setObject(9, kh.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
  }
     public ArrayList<KhachHang> search(String keyword) throws SQLException {
        ArrayList<KhachHang> list = new ArrayList<>();
        String query = "SELECT * FROM Khach_hang WHERE ma_khach_hang LIKE ? OR ten_khach_hang LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
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