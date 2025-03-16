/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.KichThuoc;
import entity.MauSac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class MauSacRepo {
    public List<MauSac> getAll(){
        List<MauSac> list = new ArrayList<>();
        String query = """
                       SELECT ID, ma_mau, ten_mau, trang_thai
                       FROM Mau_sac
                       where trang_thai = 1
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                MauSac ms = MauSac.builder()
                        .id(rs.getInt(1))
                        .ma_mau(rs.getString(2))
                        .ten_mau(rs.getString(3))
                        .trang_thai(rs.getInt(4))
                        .build();
                list.add(ms);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    
    public MauSac getMauSacByMa(String ten){
        String query = """
                    SELECT [ID]
                          ,[ma_mau]
                          ,[ten_mau]
                          ,[trang_thai]
                      FROM [dbo].[Mau_sac]
                      where [ten_mau] = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ten);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                 MauSac ms = new MauSac(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                return ms;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_mau]   
                         FROM [dbo].[Mau_sac]
                         where [ma_mau] = ?
                       """;
        List<MauSac> list = new ArrayList<>();
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
    
    public Boolean add(MauSac ms) {
        String sql = """
                       INSERT INTO [dbo].[Mau_sac]
                                             ([ma_mau]
                                             ,[ten_mau]
                                             ,[trang_thai])
                                       VALUES
                                             (?,?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, ms.getMa_mau());
            ps.setObject(2, ms.getTen_mau());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    
    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE [dbo].[Mau_sac]
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
    
    public Boolean update(MauSac ms, Integer id) {
        String sql = """
                       UPDATE [dbo].[Mau_sac]
                                     SET [ten_mau] = ?
                                        ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, ms.getTen_mau());
            ps.setObject(2, ms.getTrang_thai());
            
            ps.setObject(3, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
}
