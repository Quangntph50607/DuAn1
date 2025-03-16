/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.Da;
import entity.XuatXu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class XuatXuRepo {
    public List<XuatXu> getAll(){
        List<XuatXu> list = new ArrayList<>();
        String query = """
                       SELECT ID, ten_nuoc, trang_thai
                       FROM Xuat_xu
                       where trang_thai = 1
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                XuatXu xx = XuatXu.builder()
                        .id(rs.getInt(1))
                        .ten_nuoc(rs.getString(2))
                        .trang_thai(rs.getInt(3))
                        .build();
                list.add(xx);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    
    public XuatXu getXuatXuByTen(String ten){
        String query = """
                    SELECT [ID]
                          ,[ten_nuoc]
                          ,[trang_thai]
                      FROM [dbo].[Xuat_xu]
                      where ten_nuoc = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ten);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                 XuatXu xx = new XuatXu(rs.getInt(1), rs.getString(2), rs.getInt(3));
                return xx;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    public boolean checkTrung(Integer id) {

        String query = """
                       SELECT [ID]   
                         FROM [dbo].[Xuat_xu]
                         where [ID] = ?
                       """;
        List<XuatXu> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Boolean add(XuatXu xx) {
        String sql = """
                       INSERT INTO [dbo].[Xuat_xu]
                                             ([ten_nuoc]
                                             ,[trang_thai])
                                       VALUES
                                             (?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, xx.getTen_nuoc());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    
    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE [dbo].[Xuat_xu]
                           SET [trang_thai] = 0
                         WHERE ID = ?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }
    
    public Boolean update(XuatXu xx, Integer id) {
        String sql = """
                       UPDATE [dbo].[Xuat_xu]
                                     SET [ten_nuoc] = ?
                                        ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, xx.getTen_nuoc());
            ps.setObject(2, xx.getTrang_thai());
            
            ps.setObject(3, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
}
