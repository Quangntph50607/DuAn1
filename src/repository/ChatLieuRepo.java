/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.ChatLieu;
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
public class ChatLieuRepo {
    public List<ChatLieu> getAll(){
        List<ChatLieu> list = new ArrayList<>();
        String query = """
                       SELECT ID, ma_chat_lieu, ten_chat_lieu, trang_thai
                       FROM Chat_lieu
                       where trang_thai = 1
                       """;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ChatLieu cl = ChatLieu.builder()
                        .id(rs.getInt(1))
                        .ma_chat_lieu(rs.getString(2))
                        .ten_chat_lieu(rs.getString(3))
                        .trang_thai(rs.getInt(4))
                        .build();
                list.add(cl);
            }
            return list;       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;       
    }
    public ChatLieu getChatLieuByMa(String ten){
        String query = """
                    SELECT [ID]
                          ,[ma_chat_lieu]
                          ,[ten_chat_lieu]
                          ,[trang_thai]
                      FROM [dbo].[Chat_lieu]
                      where [ten_chat_lieu] = ?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, ten);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                 ChatLieu cl = new ChatLieu(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                return cl;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_chat_lieu]   
                         FROM [dbo].[Chat_lieu]
                         where [ma_chat_lieu] = ?
                       """;
        List<ChatLieu> list = new ArrayList<>();
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
    
    public Boolean add(ChatLieu cl) {
        String sql = """
                       INSERT INTO [dbo].[Chat_lieu]
                                             ([ma_chat_lieu]
                                             ,[ten_chat_lieu]
                                             ,[trang_thai])
                                       VALUES
                                             (?,?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, cl.getMa_chat_lieu());
            ps.setObject(2, cl.getTen_chat_lieu());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    
    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE [dbo].[Chat_lieu]
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
    
    public Boolean update(ChatLieu cl, Integer id) {
        String sql = """
                       UPDATE [dbo].[Chat_lieu]
                                     SET [ten_chat_lieu] = ?
                                        ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, cl.getTen_chat_lieu());
            ps.setObject(2, cl.getTrang_thai());
            
            ps.setObject(3, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
}
