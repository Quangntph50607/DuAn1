/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import entity.SanPhamChiTiet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import request.SanPhamChiTietRequest;
import response.SanPhamChiTietResp;

/**
 *
 * @author ASUS
 */
public class SanPhamChiTietRepo {

    public List<SanPhamChiTietResp> getAll() {
        String sql = """
                      SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, anh, San_pham_chi_tiet.trang_thai
                                                                FROM San_pham_chi_tiet
                                                                JOIN Kich_thuoc ON Kich_thuoc.ID = San_pham_chi_tiet.id_kich_thuoc
                                                                JOIN Mau_sac ON Mau_sac.ID = San_pham_chi_tiet.id_mau_sac
                                                                JOIN Chat_lieu ON Chat_lieu.ID = San_pham_chi_tiet.id_chat_lieu
                                                                JOIN Da ON Da.ID = San_pham_chi_tiet.id_da
                                                                JOIN Xuat_xu ON Xuat_xu.ID = San_pham_chi_tiet.id_xuat_xu
                                                                JOIN San_pham ON San_pham.ID = San_pham_chi_tiet.id_san_pham
                                                                WHERE San_pham_chi_tiet.trang_thai = 1
                     """;
        ArrayList<SanPhamChiTietResp> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPhamChiTietResp spct_rp = SanPhamChiTietResp.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham_chi_tiet(rs.getString(2))
                        .ten_san_pham_chi_tiet(rs.getString(3))
                        .kichThuoc(rs.getString(4))
                        .tenMau(rs.getString(5))
                        .tenChatLieu(rs.getString(6))
                        .tenDa(rs.getString(7))
                        .tenNuoc(rs.getString(8))
                        .maSP(rs.getString(9))
                        .so_luong(rs.getInt(10))
                        .don_gia(rs.getFloat(11))
                        .anh(rs.getString(12))
                        .trang_thai(rs.getInt(13))
                        .build();
                list.add(spct_rp);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public Boolean delete(Integer id) {
        String sql = """
                       UPDATE San_pham_chi_tiet
                          SET 
                             trang_thai = 0
                        WHERE id = ?
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

    public Boolean update(SanPhamChiTietRequest spct, Integer id) {
        String sql = """
                       UPDATE [dbo].[San_pham_chi_tiet]
                               SET [id_kich_thuoc] = ?
                                  ,[id_mau_sac] = ?
                                  ,[id_chat_lieu] = ?
                                  ,[id_da] = ?
                                  ,[id_san_pham] = ?
                                  ,[id_xuat_xu] = ?
                                  ,[ten_san_pham_chi_tiet] = ?
                                  ,[so_luong] = ?
                                  ,[don_gia] = ?
                                  ,[anh] = ?
                                  ,[trang_thai] = ?
                             WHERE ID=?
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, spct.getIdKichThuoc());
            ps.setObject(2, spct.getIdMauSac());
            ps.setObject(3, spct.getIdChatLieu());
            ps.setObject(4, spct.getIdDa());
            ps.setObject(5, spct.getIdSanPham());
            ps.setObject(6, spct.getIdXuatXu());
            ps.setObject(7, spct.getTenSanPhamChiTiet());
            ps.setObject(8, spct.getSoLuong());

            ps.setObject(9, spct.getDonGia());
            ps.setObject(10, spct.getAnh());
            ps.setObject(11, spct.getTrangThai());
            ps.setObject(12, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }

    public Boolean add(SanPhamChiTietRequest spct) {
        String sql = """
                       INSERT INTO San_pham_chi_tiet
                       			(ma_san_pham_chi_tiet
                       			,ten_san_pham_chi_tiet
                       			,id_kich_thuoc
                       			,id_mau_sac
                                        ,id_chat_lieu
                                        ,id_da
                                        ,id_xuat_xu
                                        ,id_san_pham
                                        ,so_luong
                                        ,don_gia
                                        ,anh
                                        ,trang_thai)
                            VALUES
                                  (?,?,?,?,?,?,?,?,?,?,?,1)
                       """;
        int check = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, spct.getMaSanPhamChiTiet());
            ps.setObject(2, spct.getTenSanPhamChiTiet());
            ps.setObject(3, spct.getIdKichThuoc());
            ps.setObject(4, spct.getIdMauSac());
            ps.setObject(5, spct.getIdChatLieu());
            ps.setObject(6, spct.getIdDa());
            ps.setObject(7, spct.getIdXuatXu());
            ps.setObject(8, spct.getIdSanPham());
            ps.setObject(9, spct.getSoLuong());
            ps.setObject(10, spct.getDonGia());
            ps.setObject(11, spct.getAnh());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }

    public ArrayList<SanPhamChiTietResp> search(String keyword) {
        String sql = """
                   SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, anh, San_pham_chi_tiet.trang_thai
                                                                                   FROM San_pham_chi_tiet
                                                                                   JOIN Kich_thuoc ON Kich_thuoc.ID = San_pham_chi_tiet.id_kich_thuoc
                                                                                   JOIN Mau_sac ON Mau_sac.ID = San_pham_chi_tiet.id_mau_sac
                                                                                   JOIN Chat_lieu ON Chat_lieu.ID = San_pham_chi_tiet.id_chat_lieu
                                                                                   JOIN Da ON Da.ID = San_pham_chi_tiet.id_da
                                                                                   JOIN Xuat_xu ON Xuat_xu.ID = San_pham_chi_tiet.id_xuat_xu
                                                                                   JOIN San_pham ON San_pham.ID = San_pham_chi_tiet.id_san_pham
                                                                                   WHERE 
                     """;
        if (keyword.length() > 0) { // isempty
            sql += """                 
   		(ma_san_pham_chi_tiet like ?
                or ten_san_pham_chi_tiet like ?
                or kich_thuoc like ?
                or Mau_sac.ten_mau like ?
                or Chat_lieu.ten_chat_lieu like ?
                or Da.ten_da like ?
                or ten_nuoc like ?
                or ma_san_pham like ?
                )
                  """;
        } else {
            return null;
        }
        ArrayList<SanPhamChiTietResp> lists = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1; // Vi tri cua dau hoi cham dau tien 

            if (keyword.length() > 0) {
                String value = "%" + keyword + "%";
                // search 1 o input nhieu truong
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);
                ps.setObject(index++, value);

            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPhamChiTietResp spct_rp = SanPhamChiTietResp.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham_chi_tiet(rs.getString(2))
                        .ten_san_pham_chi_tiet(rs.getString(3))
                        .kichThuoc(rs.getString(4))
                        .tenMau(rs.getString(5))
                        .tenChatLieu(rs.getString(6))
                        .tenDa(rs.getString(7))
                        .tenNuoc(rs.getString(8))
                        .maSP(rs.getString(9))
                        .so_luong(rs.getInt(10))
                        .don_gia(rs.getFloat(11))
                        .anh(rs.getString(12))
                        .trang_thai(rs.getInt(13))
                        .build();
                lists.add(spct_rp);
            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace(System.out); // nem loi khi xay ra 
        }
        return null;

    }

    public List<SanPhamChiTietResp> locMau(Integer id) {

        String query = """
                      SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, anh, San_pham_chi_tiet.trang_thai
                      FROM San_pham_chi_tiet
                     JOIN Kich_thuoc ON Kich_thuoc.ID = San_pham_chi_tiet.id_kich_thuoc
                     JOIN Mau_sac ON Mau_sac.ID = San_pham_chi_tiet.id_mau_sac
                     JOIN Chat_lieu ON Chat_lieu.ID = San_pham_chi_tiet.id_chat_lieu
                     JOIN Da ON Da.ID = San_pham_chi_tiet.id_da
                     JOIN Xuat_xu ON Xuat_xu.ID = San_pham_chi_tiet.id_xuat_xu
                    JOIN San_pham ON San_pham.ID = San_pham_chi_tiet.id_san_pham
                         WHERE Mau_sac.ID =  ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<SanPhamChiTietResp> list = new ArrayList<>();
            while (rs.next()) {
                SanPhamChiTietResp spct_rp = SanPhamChiTietResp.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham_chi_tiet(rs.getString(2))
                        .ten_san_pham_chi_tiet(rs.getString(3))
                        .kichThuoc(rs.getString(4))
                        .tenMau(rs.getString(5))
                        .tenChatLieu(rs.getString(6))
                        .tenDa(rs.getString(7))
                        .tenNuoc(rs.getString(8))
                        .maSP(rs.getString(9))
                        .so_luong(rs.getInt(10))
                        .don_gia(rs.getFloat(11))
                        .anh(rs.getString(12))
                        .trang_thai(rs.getInt(13))
                        .build();
                list.add(spct_rp);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<SanPhamChiTietResp> locChatLieu(Integer id) {

        String query = """
                      SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, anh, San_pham_chi_tiet.trang_thai
                                            FROM San_pham_chi_tiet
                                           JOIN Kich_thuoc ON Kich_thuoc.ID = San_pham_chi_tiet.id_kich_thuoc
                                           JOIN Mau_sac ON Mau_sac.ID = San_pham_chi_tiet.id_mau_sac
                                           JOIN Chat_lieu ON Chat_lieu.ID = San_pham_chi_tiet.id_chat_lieu
                                           JOIN Da ON Da.ID = San_pham_chi_tiet.id_da
                                           JOIN Xuat_xu ON Xuat_xu.ID = San_pham_chi_tiet.id_xuat_xu
                                          JOIN San_pham ON San_pham.ID = San_pham_chi_tiet.id_san_pham
                                               WHERE Chat_lieu.ID =  ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<SanPhamChiTietResp> list = new ArrayList<>();
            while (rs.next()) {
                SanPhamChiTietResp spct_rp = SanPhamChiTietResp.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham_chi_tiet(rs.getString(2))
                        .ten_san_pham_chi_tiet(rs.getString(3))
                        .kichThuoc(rs.getString(4))
                        .tenMau(rs.getString(5))
                        .tenChatLieu(rs.getString(6))
                        .tenDa(rs.getString(7))
                        .tenNuoc(rs.getString(8))
                        .maSP(rs.getString(9))
                        .so_luong(rs.getInt(10))
                        .don_gia(rs.getFloat(11))
                        .anh(rs.getString(12))
                        .trang_thai(rs.getInt(13))
                        .build();
                list.add(spct_rp);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<SanPhamChiTietResp> locXuatXu(Integer id) {

        String query = """
                      SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, anh, San_pham_chi_tiet.trang_thai
                                            FROM San_pham_chi_tiet
                                           JOIN Kich_thuoc ON Kich_thuoc.ID = San_pham_chi_tiet.id_kich_thuoc
                                           JOIN Mau_sac ON Mau_sac.ID = San_pham_chi_tiet.id_mau_sac
                                           JOIN Chat_lieu ON Chat_lieu.ID = San_pham_chi_tiet.id_chat_lieu
                                           JOIN Da ON Da.ID = San_pham_chi_tiet.id_da
                                           JOIN Xuat_xu ON Xuat_xu.ID = San_pham_chi_tiet.id_xuat_xu
                                          JOIN San_pham ON San_pham.ID = San_pham_chi_tiet.id_san_pham
                                               WHERE Xuat_xu.ID =  ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<SanPhamChiTietResp> list = new ArrayList<>();
            while (rs.next()) {
                SanPhamChiTietResp spct_rp = SanPhamChiTietResp.builder()
                        .id(rs.getInt(1))
                        .ma_san_pham_chi_tiet(rs.getString(2))
                        .ten_san_pham_chi_tiet(rs.getString(3))
                        .kichThuoc(rs.getString(4))
                        .tenMau(rs.getString(5))
                        .tenChatLieu(rs.getString(6))
                        .tenDa(rs.getString(7))
                        .tenNuoc(rs.getString(8))
                        .maSP(rs.getString(9))
                        .so_luong(rs.getInt(10))
                        .don_gia(rs.getFloat(11))
                        .anh(rs.getString(12))
                        .trang_thai(rs.getInt(13))
                        .build();
                list.add(spct_rp);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_san_pham_chi_tiet]   
                         FROM [dbo].[San_pham_chi_tiet]
                         where [ma_san_pham_chi_tiet] = ?
                       """;
        List<SanPhamChiTiet> list = new ArrayList<>();
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
