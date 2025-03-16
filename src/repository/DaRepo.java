/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.ChatLieu;
import entity.Da;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class DaRepo {
    public List<Da> getAll(){
        List<Da> list = new ArrayList<>();
        String query = """
                       SELECT ID, ma_da, ten_da, trang_thai
                       FROM Da
                       where trang_thai = 1
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Da da = Da.builder()
                        .id(rs.getInt(1))
                        .ma_da(rs.getString(2))
                        .ten_da(rs.getString(3))
                        .trang_thai(rs.getInt(4))
                        .build();
                list.add(da);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    
    public Da getDaByMa(String ten){
        String query = """
                    SELECT [ID]
                          ,[ma_da]
                          ,[ten_da]
                          ,[trang_thai]
                      FROM [dbo].[Da]
                      where [ten_da] = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ten);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                 Da da = new Da(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                return da;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_da]   
                         FROM [dbo].[Da]
                         where [ma_da] = ?
                       """;
        List<Da> list = new ArrayList<>();
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
    
    public Boolean add(Da da) {
        String sql = """
                       INSERT INTO [dbo].[Da]
                                             ([ma_da]
                                             ,[ten_da]
                                             ,[trang_thai])
                                       VALUES
                                             (?,?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, da.getMa_da());
            ps.setObject(2, da.getTen_da());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    
    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE [dbo].[Da]
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
    
    public Boolean update(Da da, Integer id) {
        String sql = """
                       UPDATE [dbo].[Da]
                                     SET [ten_da] = ?
                                        ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, da.getTen_da());
            ps.setObject(2, da.getTrang_thai());
            
            ps.setObject(3, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
}
