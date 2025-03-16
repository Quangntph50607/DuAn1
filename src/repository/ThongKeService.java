/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import entity.ThongKe;
import config.DBConnect;

/**
 *
 * @author adim
 */
public class ThongKeService {
    private Connection conn;
    public ThongKeService(){
        conn = DBConnect.getConnection();
    }
    public ArrayList<ThongKe> getData() throws SQLException{
        ArrayList<ThongKe> list = new ArrayList<>();
        String query = """
                      SELECT    dbo.Hoa_don.ngay_tao, 
                                dbo.Hoa_don.hinh_thuc_thanh_toan, 
                                dbo.Hoa_don_chi_tiet.thanh_tien, 
                                dbo.Hoa_don_chi_tiet.so_luong, 
                                dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, 
                                dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet, 
                                dbo.San_pham_chi_tiet.don_gia
                       FROM      dbo.Hoa_don 
                       INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don 
                       INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
                       WHERE     dbo.Hoa_don_chi_tiet.trang_thai = 2
                          """;
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        
        while(rs.next()){
            ThongKe tk = new ThongKe();
            
                tk.setNgay(rs.getDate("ngay_tao"));
                tk.setMaSP(rs.getString("ma_san_pham_chi_tiet"));
                tk.setTenSP(rs.getString("ten_san_pham_chi_tiet"));
                tk.setSoLuong(rs.getInt("so_luong"));
                tk.setGia(rs.getBigDecimal(7));
                tk.setHinhThuc(rs.getString("hinh_thuc_thanh_toan"));
                tk.setTongTien(rs.getBigDecimal("thanh_tien"));
                list.add(tk);
        }
         return list;
    }
   
    public ArrayList<ThongKe> search(String date) throws SQLException {
    ArrayList<ThongKe> list = new ArrayList<>();
    String query = """
                   SELECT 
                             dbo.Hoa_don.ngay_tao, 
                             dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, 
                             dbo.Hoa_don_chi_tiet.so_luong, 
                              dbo.Hoa_don.hinh_thuc_thanh_toan, 
                              dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet, 
                              dbo.San_pham_chi_tiet.don_gia,
                              dbo.Hoa_don_chi_tiet.thanh_tien
                              FROM      dbo.Hoa_don 
                                      INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don 
                                      INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
                                      WHERE     CONVERT(date, dbo.Hoa_don.ngay_tao) = ? AND dbo.Hoa_don_chi_tiet.trang_thai = 2
                                      """;
     try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, date); 
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ThongKe tk = new ThongKe();
            tk.setNgay(rs.getDate("ngay_tao"));
            tk.setMaSP(rs.getString("ma_san_pham_chi_tiet"));
            tk.setTenSP(rs.getString("ten_san_pham_chi_tiet"));
            tk.setSoLuong(rs.getInt("so_luong"));
            tk.setGia(rs.getBigDecimal(6));
            tk.setHinhThuc(rs.getString("hinh_thuc_thanh_toan"));
            tk.setTongTien(rs.getBigDecimal("thanh_tien"));
            list.add(tk);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return list;
}

    public ArrayList<ThongKe> searchByDate(String date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public ArrayList<ThongKe> searchDanhMuc(String tenSP) throws SQLException {
    ArrayList<ThongKe> list = new ArrayList<>();
    String query = """
                   SELECT 
                             dbo.Hoa_don.ngay_tao, 
                             dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, 
                             dbo.Hoa_don_chi_tiet.so_luong, 
                             dbo.Hoa_don.hinh_thuc_thanh_toan, 
                             dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet, 
                             dbo.San_pham_chi_tiet.don_gia,
                             dbo.Hoa_don_chi_tiet.thanh_tien
                   FROM dbo.Hoa_don 
                   INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don 
                   INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
                   WHERE dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet LIKE ?
                   """;
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, "%" + tenSP + "%"); 
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ThongKe tk = new ThongKe();
            tk.setNgay(rs.getDate("ngay_tao"));
            tk.setMaSP(rs.getString("ma_san_pham_chi_tiet"));
            tk.setTenSP(rs.getString("ten_san_pham_chi_tiet"));
            tk.setSoLuong(rs.getInt("so_luong"));
            tk.setGia(rs.getBigDecimal(6));
            tk.setHinhThuc(rs.getString("hinh_thuc_thanh_toan"));
            tk.setTongTien(rs.getBigDecimal("thanh_tien"));
            list.add(tk);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return list;
}
  public ArrayList<ThongKe> getTop(int topN) throws SQLException {
    ArrayList<ThongKe> list = new ArrayList<>();
 
    String query =  """
                   SELECT TOP (?)
                          dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet,
                          dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet,
                          SUM(dbo.Hoa_don_chi_tiet.so_luong) AS total_quantity,
                          SUM(dbo.Hoa_don_chi_tiet.thanh_tien) AS total_revenue
                   FROM dbo.Hoa_don
                   INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don
                   INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
                   WHERE dbo.Hoa_don_chi_tiet.trang_thai = 2
                   GROUP BY dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet
                   ORDER BY total_quantity DESC
                   """;
    
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, topN); 
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ThongKe tk = new ThongKe();
                tk.setTenSP(rs.getString("ten_san_pham_chi_tiet"));
                tk.setMaSP(rs.getString("ma_san_pham_chi_tiet"));
                tk.setSoLuong(rs.getInt("total_quantity")); 
                tk.setTongTien(rs.getBigDecimal("total_revenue"));
                list.add(tk);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    
    return list;
}
    
  public ArrayList<ThongKe> searchYear(String year) throws SQLException {
    ArrayList<ThongKe> list = new ArrayList<>();
    String query = """
                   SELECT 
                           dbo.Hoa_don.ngay_tao, 
                           dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, 
                           dbo.Hoa_don_chi_tiet.so_luong, 
                           dbo.Hoa_don.hinh_thuc_thanh_toan, 
                           dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet, 
                           dbo.San_pham_chi_tiet.don_gia,
                           dbo.Hoa_don_chi_tiet.thanh_tien
                   FROM dbo.Hoa_don
                   INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don
                   INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
                   WHERE YEAR(dbo.Hoa_don.ngay_tao) = ?
                   """;
    
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, year); 
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ThongKe tk = new ThongKe();
                tk.setNgay(rs.getDate("ngay_tao"));
                tk.setMaSP(rs.getString("ma_san_pham_chi_tiet"));
                tk.setTenSP(rs.getString("ten_san_pham_chi_tiet"));
                tk.setSoLuong(rs.getInt("so_luong"));
                tk.setGia(rs.getBigDecimal(6));
                tk.setHinhThuc(rs.getString("hinh_thuc_thanh_toan"));
                tk.setTongTien(rs.getBigDecimal("thanh_tien"));
                list.add(tk);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return list;
}
  
  public ArrayList<ThongKe> searchMonthYear(String month, String year) throws SQLException {
    ArrayList<ThongKe> result = new ArrayList<>();
  
    String sql = """
        SELECT 
            dbo.Hoa_don.ngay_tao, 
            dbo.San_pham_chi_tiet.ten_san_pham_chi_tiet, 
            dbo.Hoa_don_chi_tiet.so_luong, 
            dbo.Hoa_don.hinh_thuc_thanh_toan, 
            dbo.San_pham_chi_tiet.ma_san_pham_chi_tiet, 
            dbo.San_pham_chi_tiet.don_gia,
            dbo.Hoa_don_chi_tiet.thanh_tien
        FROM dbo.Hoa_don
        INNER JOIN dbo.Hoa_don_chi_tiet ON dbo.Hoa_don.ID = dbo.Hoa_don_chi_tiet.id_hoa_don
        INNER JOIN dbo.San_pham_chi_tiet ON dbo.Hoa_don_chi_tiet.id_san_pham_chi_tiet = dbo.San_pham_chi_tiet.ID
        WHERE MONTH(dbo.Hoa_don.ngay_tao) = ? AND YEAR(dbo.Hoa_don.ngay_tao) = ?
    """;

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, Integer.parseInt(month));
        pst.setInt(2, Integer.parseInt(year));
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            ThongKe tk = new ThongKe();
            tk.setNgay(rs.getDate("ngay_tao"));
            tk.setMaSP(rs.getString("ma_san_pham_chi_tiet")); 
            tk.setTenSP(rs.getString("ten_san_pham_chi_tiet")); 
            tk.setSoLuong(rs.getInt("so_luong")); 
            tk.setGia(rs.getBigDecimal(6));
            tk.setHinhThuc(rs.getString("hinh_thuc_thanh_toan")); 
            tk.setTongTien(rs.getBigDecimal("thanh_tien"));
            result.add(tk);
        }
    }
    return result;
}

    
}
