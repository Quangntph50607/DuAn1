/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.ChucVu;
import entity.KichThuoc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class KichThuocRepo {
    public List<KichThuoc> getAll(){
        List<KichThuoc> list = new ArrayList<>();
        String query = """
                       SELECT ID, ma_kich_thuoc, kich_thuoc, trang_thai
                       FROM Kich_thuoc
                       where trang_thai = 1
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                KichThuoc kt = KichThuoc.builder()
                        .id(rs.getInt(1))
                        .ma_kich_thuoc(rs.getString(2))
                        .kich_thuoc(rs.getFloat(3))
                        .trang_thai(rs.getInt(4))
                        .build();
                list.add(kt);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    
     public KichThuoc getKichThuocByMa(String ten){
        String query = """
                    SELECT [ID]
                          ,[ma_kich_thuoc]
                          ,[kich_thuoc]
                          ,[trang_thai]
                      FROM [dbo].[Kich_thuoc]
                      where [kich_thuoc] = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ten);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                KichThuoc kt = new KichThuoc(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getInt(4));
                return kt;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
     
    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_kich_thuoc]   
                         FROM [dbo].[Kich_thuoc]
                         where [ma_kich_thuoc] = ?
                       """;
        List<KichThuoc> list = new ArrayList<>();
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
    
    public Boolean add(KichThuoc kt) {
        String sql = """
                       INSERT INTO [dbo].[Kich_thuoc]
                                             ([ma_kich_thuoc]
                                             ,[kich_thuoc]
                                             ,[trang_thai])
                                       VALUES
                                             (?,?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, kt.getMa_kich_thuoc());
            ps.setObject(2, kt.getKich_thuoc());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    
    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE [dbo].[Kich_thuoc]
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
    
    public Boolean update(KichThuoc kt, Integer id) {
        String sql = """
                       UPDATE [dbo].[Kich_thuoc]
                                     SET [kich_thuoc] = ?
                                        ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, kt.getKich_thuoc());
            ps.setObject(2, kt.getTrang_thai());
            
            ps.setObject(3, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
}
