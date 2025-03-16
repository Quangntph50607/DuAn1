/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.SanPham;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author DUNG
 */
public class SanPhamRepo {

    public List<SanPham> getAll() {
        String query = """
                       SELECT [ID]
                             ,[ma_san_pham]
                             ,[ten_san_pham]
                             ,[ngay_tao]
                             ,[trang_thai]
                         FROM [dbo].[San_pham]
                       """;
        List<SanPham> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt(1));
                sp.setMaSanPham(rs.getString(2));
                sp.setTenSanPham(rs.getString(3));
                sp.setNgayTao(rs.getDate(4));
                sp.setTrangThai(rs.getInt(5));
                list.add(sp);

            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean add(SanPham sp) {
        int check = 0;
        String query = """
                       INSERT INTO [dbo].[San_pham]
                                  ([ma_san_pham]
                                  ,[ten_san_pham]
                                  ,[ngay_tao]
                                  ,[trang_thai])
                            VALUES
                                  (?,?,?,?)
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setObject(1, sp.getMaSanPham());
            ps.setObject(2, sp.getTenSanPham());
            ps.setObject(3, sp.getNgayTao());
            ps.setObject(4, sp.getTrangThai());
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean update(Integer id, SanPham sp) {
        int check = 0;
        String query = """
                       UPDATE [dbo].[San_pham]
                          SET [ma_san_pham] = ?
                             ,[ten_san_pham] = ?
                             ,[ngay_tao] = ?
                             ,[trang_thai] = ?
                        WHERE ID = ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setObject(1, sp.getMaSanPham());
            ps.setObject(2, sp.getTenSanPham());
            ps.setObject(3, sp.getNgayTao());
            ps.setObject(4, sp.getTrangThai());
            ps.setObject(5, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean remove(Integer id) {
        int check = 0;
        String query = """
                       UPDATE [dbo].[San_pham]
                          SET [trang_thai] = 0
                        WHERE ID = ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setObject(1, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public ArrayList<SanPham> search(String keyword) {
        String sql = """
                  SELECT [ID]
                                               ,[ma_san_pham]
                                               ,[ten_san_pham]
                                               ,[ngay_tao]
                                               ,[trang_thai]
                                           FROM [dbo].[San_pham] where
                     """;
        if (keyword.length() > 0) { // isempty
            sql += """         
                  	([ma_san_pham] like ?
                        or [ten_san_pham] like ?                      
   			)
                  """;
        } else {
            return null;
        }
        ArrayList<SanPham> lists = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1; // Vi tri cua dau hoi cham dau tien 

            if (keyword.length() > 0) {
                String value = "%" + keyword + "%";
                // search 1 o input nhieu truong
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt(1));
                sp.setMaSanPham(rs.getString(2));
                sp.setTenSanPham(rs.getString(3));
                sp.setNgayTao(rs.getDate(4));
                sp.setTrangThai(rs.getInt(5));
                lists.add(sp);

            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace(System.out); // nem loi khi xay ra 
        }
        return lists;
    }
    
     public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_san_pham]
                           FROM [dbo].[San_pham]
                           where [ma_san_pham] = ?
                       """;
        List<SanPham> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setObject(1, ma);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
