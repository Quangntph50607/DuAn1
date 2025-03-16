/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.Da;
import entity.MaSanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class MaSanPhamRepo {
    public List<MaSanPham> getAll(){
        List<MaSanPham> list = new ArrayList<>();
        String query = """
                       SELECT ID, ma_san_pham, ten_san_pham, ngay_tao, trang_thai
                       FROM San_pham
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                MaSanPham msp = MaSanPham.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham(rs.getString(2))
                        .ten_san_pham(rs.getString(3))
                        .ngay_tao(rs.getDate(4))
                        .trang_thai(rs.getInt(5))
                        .build();
                list.add(msp);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    
    public MaSanPham getSanPhamByMa(String ma) {
        String query = """
                       SELECT [ID]
                             ,[ma_san_pham]
                             ,[ten_san_pham]
                             ,[ngay_tao]
                             ,[trang_thai]
                         FROM [dbo].[San_pham]
                       where [ma_san_pham] =?
                       """;
       
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaSanPham msp = new MaSanPham(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getInt(5));
                return msp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
