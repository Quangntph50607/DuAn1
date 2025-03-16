/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.ChucVu;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author DUNG
 */
public class ChucVuRepo {
    public List<ChucVu> getAll(){
        List<ChucVu> list = new ArrayList<>();
        String query = """
                       SELECT [ID]
                             ,[ma_chuc_vu]
                             ,[ten_chuc_vu]
                         FROM [dbo].[Chuc_vu]
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                ChucVu cv = ChucVu.builder()
                        .id(rs.getInt(1))
                        .maCv(rs.getString(2))
                        .tenCv(rs.getString(3))
                        .build();
                list.add(cv);
            }
            return list;
                
        } catch (Exception e) {
                e.printStackTrace();
                }
        return null;
        
    }
    
       public ChucVu getChucVuByMa(String ma1){
        String query = """
                      SELECT [ID]
                                               ,[ma_chuc_vu]
                                               ,[ten_chuc_vu]
                                           FROM [dbo].[Chuc_vu]
                                           WHERE [ma_chuc_vu] = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ma1);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                ChucVu cv = new ChucVu(rs.getInt(1), rs.getString(2), rs.getString(3));
                return cv;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }

}
