/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import config.DBConnect;
import entity.HoaDon;
import java.util.ArrayList;
import java.util.List;
import logic.RandomStringGenerator;
import request.HoaDonRequest;
import response.GioHangResp;
import response.HoaDonResp;
import response.SanPhamChiTietResp;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import entity.KhachHang;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author DUNG
 */
public class BanHangRepo {

    public List<SanPhamChiTietResp> getAll() {
        String sql = """
            SELECT San_pham_chi_tiet.ID, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kich_thuoc, ten_mau, ten_chat_lieu, ten_da, ten_nuoc, ma_san_pham, San_pham_chi_tiet.so_luong, don_gia, San_pham_chi_tiet.trang_thai
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
                        .trang_thai(rs.getInt(12))
                        .build();
                list.add(spct_rp);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public List<HoaDonResp> getAllHd() {
        String sql = """
            SELECT dbo.Hoa_don.ID, dbo.Hoa_don.ma_hoa_don, dbo.Hoa_don.id_khach_hang, dbo.Hoa_don.id_nhan_vien, dbo.Hoa_don.ten_khach_hang, dbo.Hoa_don.dia_chi_khach_hang, dbo.Hoa_don.so_dt, dbo.Hoa_don.ngay_tao, dbo.Hoa_don.tong_tien, dbo.Hoa_don.hinh_thuc_thanh_toan, 
            dbo.Hoa_don.id_voucher, dbo.Hoa_don.trang_thai, dbo.Nhan_vien.ma_nhan_vien
            FROM   dbo.Hoa_don INNER JOIN
            dbo.Nhan_vien ON dbo.Hoa_don.id_nhan_vien = dbo.Nhan_vien.ID
            where Hoa_don.trang_thai= 1
                     """;
        List<HoaDonResp> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDonResp hd = HoaDonResp.builder()
                        .idHoaDon(rs.getInt(1))
                        .MaHoaDon(rs.getString(2))
                        .idKhachHang(rs.getInt(3))
                        .idNhanVien(rs.getInt(4))
                        .tenKhachHang(rs.getString(5))
                        .diaChi(rs.getString(6))
                        .sdt(rs.getString(7))
                        .ngayTao(rs.getDate(8))
                        .tongTien(rs.getFloat(9))
                        .hinhThucThanhToan(rs.getString(10))
                        .idVoucher(rs.getInt(11))
                        .trangThai(rs.getInt(12))
                        .maNhanVien(rs.getString(13))
                        .build();
                list.add(hd);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

//    public boolean add(HoaDonRequest hd) {
//        int check = 0;
//        String sql = """
//                     INSERT INTO [dbo].[Hoa_don]
//              ([ma_hoa_don]        
//               ,[id_nhan_vien]
//              ,[ten_khach_hang]         
//              ,[ngay_tao]         
//              ,[trang_thai])
//              VALUES
//              (?,?,?,?,1)
//                    """;
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setObject(1, hd.getMaHoaDon());
//            ps.setObject(2, hd.getIdNhanVien());
//            ps.setObject(3, hd.getTenKhachHang());
//            ps.setObject(4, hd.getNgayTao());
//
//            check = ps.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
//        return check > 0;
//    }
    public int add(HoaDonRequest hd) {
        String sql = """
                 INSERT INTO [dbo].[Hoa_don]
          ([ma_hoa_don]
           ,[id_nhan_vien]
           ,[ten_khach_hang]         
           ,[ngay_tao]         
           ,[trang_thai])
           VALUES
           (?,?,?,?,1)
                 """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, hd.getMaHoaDon());
            ps.setObject(2, hd.getIdNhanVien());
            ps.setObject(3, hd.getTenKhachHang());
            ps.setObject(4, hd.getNgayTao());

            int check = ps.executeUpdate();

            if (check > 0) {
                // Lấy id_hoa_don mới tạo
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Trả về ID hóa đơn
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return -1; // Trả về -1 nếu không có ID hóa đơn
    }

    public boolean addHoaDonChiTiet(String maHoaDonChiTiet, int idHoaDon) {
        String sql = """
                 INSERT INTO [dbo].[Hoa_don_chi_tiet]
          ([ma_hoa_don_chi_tiet]
           ,[id_hoa_don])
           VALUES
           (?, ?)
                 """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, maHoaDonChiTiet);
            ps.setInt(2, idHoaDon);

            int check = ps.executeUpdate();
            return check > 0;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }

    public int getIdSP(String maSP) {
        String sql = """
              	 SELECT ID 
                              FROM san_pham_chi_tiet 
                              WHERE ma_san_pham_chi_tiet = ?
               """;
        int id = -1;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getSoLuongTon(String maSpct) {
        String sql = """
              	 select so_luong from San_pham_chi_tiet where ma_san_pham_chi_tiet = ?
               """;
        int id = -1;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSpct);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getIdHD(String maHD) {
        String sql = """
               SELECT ID 
               FROM hoa_don 
               WHERE ma_hoa_don = ?
               """;
        int id = -1;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getIdVocher(String maVc) {
        String sql = """
               select ID from Voucher where ma_voucher = ?
               """;
        int id = -1;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public KhachHang getKhByMa(String maKh) {
        String query = """
                     select ID,so_dt,dia_chi from Khach_hang where ma_khach_hang = ?
                      """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, maKh);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt(1));
                kh.setSdt(rs.getString(2));
                kh.setDiaChi(rs.getString(3));

                return kh;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }

//    public boolean updateSoLuong(String maSP, int soLuong, int idHD) {
//        int rowsAffected = 0;
//
//        String sql1 = """
//                UPDATE hoa_don_chi_tiet
//        SET so_luong = so_luong + ?,
//        thanh_tien = (so_luong + ?) * (SELECT don_gia FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
//        WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet LIKE ?)
//                """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
//            ps.setInt(1, soLuong);
//            ps.setInt(2, soLuong);
//            ps.setString(3, maSP);
//            ps.setString(4, maSP);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String sql2 = """
//                UPDATE [dbo].[san_pham_chi_tiet]
//                   SET [so_luong] = so_luong - ?
//                 WHERE ma_san_pham_chi_tiet = ?
//                """;
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
//            ps.setInt(1, soLuong);
//            ps.setString(2, maSP);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String sql3 = """
//     update hoa_don
//                 set tong_tien = (select sum(thanh_tien) from hoa_don_chi_tiet where id_hoa_don = ?)
//                 where ID = ?
//                """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
//            ps.setInt(1, idHD);
//            ps.setInt(2, idHD);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rowsAffected > 0;
//    }
    public boolean updateSoLuong(String maSP, int soLuong, int idHD) {
        int rowsAffected = 0;

        // Cập nhật số lượng và thành tiền trong hóa đơn chi tiết
        String sql1 = """
            IF EXISTS (
                SELECT 1 
                FROM hoa_don_chi_tiet 
                WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?) 
                AND id_hoa_don = ?
            )
            BEGIN
                UPDATE hoa_don_chi_tiet
                SET so_luong = so_luong + ?, 
                    thanh_tien = (so_luong + ?) * (SELECT don_gia FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
                WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
                AND id_hoa_don = ?
            END
            ELSE
            BEGIN
                INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_pham_chi_tiet, so_luong, thanh_tien)
                VALUES (
                    ?,
                    (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?),
                    ?,
                    ? * (SELECT don_gia FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
                )
            END
            """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            // Update part
            ps.setString(1, maSP);
            ps.setInt(2, idHD);
            ps.setInt(3, soLuong);
            ps.setInt(4, soLuong);
            ps.setString(5, maSP);
            ps.setString(6, maSP);
            ps.setInt(7, idHD);
            // Insert part
            ps.setInt(8, idHD);
            ps.setString(9, maSP);
            ps.setInt(10, soLuong);
            ps.setInt(11, soLuong);
            ps.setString(12, maSP);

            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật số lượng sản phẩm trong kho
        String sql2 = """
            UPDATE san_pham_chi_tiet
            SET so_luong = so_luong - ?
            WHERE ma_san_pham_chi_tiet = ?
            """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, soLuong);
            ps.setString(2, maSP);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật tổng tiền của hóa đơn
        String sql3 = """
            UPDATE hoa_don
            SET tong_tien = (SELECT SUM(thanh_tien) FROM hoa_don_chi_tiet WHERE id_hoa_don = ?)
            WHERE ID = ?
            """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setInt(1, idHD);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

//    public boolean UpdateSlSanPham(String maSP, int soLuong, int idHD) {
//        int rowsAffected = 0;
//
//        String sql1 = """
//        UPDATE hoa_don_chi_tiet
//        SET so_luong = so_luong - ?,
//            thanh_tien = (so_luong - ?) * (SELECT don_gia FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
//        WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet LIKE ?)
//        """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
//            ps.setInt(1, soLuong);
//            ps.setInt(2, soLuong);
//            ps.setString(3, maSP);
//            ps.setString(4, maSP);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String sql2 = """
//        UPDATE [dbo].[san_pham_chi_tiet]
//        SET [so_luong] = so_luong + ?
//        WHERE ma_san_pham_chi_tiet = ?
//        """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
//            ps.setInt(1, soLuong);
//            ps.setString(2, maSP);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String sql3 = """
//        UPDATE hoa_don
//        SET tong_tien = (SELECT SUM(thanh_tien) FROM hoa_don_chi_tiet WHERE id_hoa_don = ?)
//        WHERE ID = ?
//        """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
//            ps.setInt(1, idHD);
//            ps.setInt(2, idHD);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Xóa sản phẩm chi tiết nếu số lượng = 0
//        String sql4 = """
//        DELETE FROM hoa_don_chi_tiet
//        WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
//        AND so_luong = 0
//        """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql4)) {
//            ps.setString(1, maSP);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return rowsAffected > 0;
//    }
    public boolean UpdateSlSanPham(String maSP, int soLuong, int idHD) {
        int rowsAffected = 0;

        String sql1 = """
        UPDATE hoa_don_chi_tiet
        SET so_luong = so_luong - ?,
            thanh_tien = (so_luong - ?) * (SELECT don_gia FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
        WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet LIKE ?)
        AND id_hoa_don = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, soLuong);
            ps.setString(3, maSP);
            ps.setString(4, maSP);
            ps.setInt(5, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql2 = """
        UPDATE san_pham_chi_tiet
        SET so_luong = so_luong + ?
        WHERE ma_san_pham_chi_tiet = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, soLuong);
            ps.setString(2, maSP);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql3 = """
        UPDATE hoa_don
        SET tong_tien = (SELECT SUM(thanh_tien) FROM hoa_don_chi_tiet WHERE id_hoa_don = ?)
        WHERE ID = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setInt(1, idHD);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql4 = """
        DELETE FROM hoa_don_chi_tiet
        WHERE id_san_pham_chi_tiet = (SELECT id FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?)
        AND id_hoa_don = ?
        AND so_luong = 0
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql4)) {
            ps.setString(1, maSP);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public boolean XoaAllGioHang(int idHD) {
        int rowsAffected = 0;

        // Cập nhật lại số lượng sản phẩm trong bảng san_pham_chi_tiet trước khi xóa
        String sql1 = """
    UPDATE san_pham_chi_tiet
    SET so_luong = so_luong + (
        SELECT SUM(so_luong) FROM hoa_don_chi_tiet WHERE id_san_pham_chi_tiet = san_pham_chi_tiet.id
        AND id_hoa_don = ?
    )
    WHERE id IN (
        SELECT id_san_pham_chi_tiet FROM hoa_don_chi_tiet WHERE id_hoa_don = ?
    )
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setInt(1, idHD);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Xóa toàn bộ sản phẩm chi tiết khỏi giỏ hàng theo id hóa đơn
        String sql2 = """
    DELETE FROM hoa_don_chi_tiet
    WHERE id_hoa_don = ?
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật lại tổng tiền trong bảng hoa_don
        String sql3 = """
    UPDATE hoa_don
    SET tong_tien = 0
    WHERE ID = ?
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setInt(1, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public List<GioHangResp> listBanHang(String maHD) {

        String sql = """
         select ma_san_pham_chi_tiet,ten_san_pham_chi_tiet,San_pham_chi_tiet.don_gia,
          Hoa_don_chi_tiet.so_luong,Hoa_don_chi_tiet.thanh_tien
          from San_pham_chi_tiet
          join Hoa_don_chi_tiet on Hoa_don_chi_tiet.id_san_pham_chi_tiet = San_pham_chi_tiet.ID
          join Hoa_don on Hoa_don.ID = Hoa_don_chi_tiet.id_hoa_don
          where Hoa_don_chi_tiet.trang_thai = 1 and Hoa_don.ma_hoa_don = ?
                     """;
        List<GioHangResp> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GioHangResp gh = GioHangResp.builder()
                        .masp(rs.getString(1))
                        .tensp(rs.getString(2))
                        .donGia(rs.getFloat(3))
                        //                        .maHoaDonChiTiet(rs.getString(4))
                        .soLuong(rs.getInt(4))
                        .tongTien(rs.getFloat(5))
                        .build();
                list.add(gh);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public GioHangResp BanHang(String maHD) {
        String sql = """
         select ma_san_pham_chi_tiet,ten_san_pham_chi_tiet,San_pham_chi_tiet.don_gia,
	 Hoa_don_chi_tiet.so_luong,Hoa_don.tong_tien
	  from San_pham_chi_tiet
 	  join Hoa_don_chi_tiet on Hoa_don_chi_tiet.id_san_pham_chi_tiet = San_pham_chi_tiet.ID
	  join Hoa_don on Hoa_don.ID = Hoa_don_chi_tiet.id_hoa_don
	  where Hoa_don_chi_tiet.trang_thai = 1 and Hoa_don.ma_hoa_don = ?
           """;
        GioHangResp gh = new GioHangResp();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gh.setMasp(rs.getString(1));
                gh.setTensp(rs.getString(2));
                gh.setDonGia(rs.getFloat(3));
                gh.setSoLuong(rs.getInt(4));
                gh.setTongTien(rs.getFloat(5));
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return gh;
    }

    public boolean themSoLuong(int idSP, int idHD, int soLuong, String maHDCT, String maSPCT) {
        String sql = """
	INSERT INTO hoa_don_chi_tiet(ma_hoa_don_chi_tiet,id_hoa_don ,id_san_pham_chi_tiet, so_luong, thanh_tien,trang_thai)
        VALUES(?, ?, ?, ?, (SELECT don_gia * ? FROM san_pham_chi_tiet WHERE ma_san_pham_chi_tiet = ?),1)
                """;
        int rowsAffected = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHDCT);
            ps.setInt(2, idHD);
            ps.setInt(3, idSP);
            ps.setInt(4, soLuong);
            ps.setInt(5, soLuong);
            ps.setString(6, maSPCT);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql2 = """
                UPDATE [dbo].[san_pham_chi_tiet]
                                   SET [so_luong] = so_luong - ?
                                 WHERE ma_san_pham_chi_tiet = ?
                """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, soLuong);
            ps.setString(2, maSPCT);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql3 = """
                 update hoa_don
                         set tong_tien = (select sum(thanh_tien) from hoa_don_chi_tiet where id_hoa_don = ?)
                         where ID = ?
                """;
//        String sql3 = """
//    update hoa_don            
//    set tong_tien = (select sum(so_luong * (select don_gia from san_pham_chi_tiet where ma_san_pham_chi_tiet = hoa_don_chi_tiet.ma_san_pham_chi_tiet)) from hoa_don_chi_tiet where id_hoa_don = ?)
//    where ID = ?
//""";

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setInt(1, idHD);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    public boolean check(String maSP, String maHD) {
        String sql = """
                      select id_san_pham_chi_tiet
                      from hoa_don_chi_tiet
                      where id_san_pham_chi_tiet = (select id from san_pham_chi_tiet where ma_san_pham_chi_tiet = ?) 
                      and id_hoa_don = (select id from hoa_don where ma_hoa_don = ?)
                     """;
        int id = 0;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, maSP);
            ps.setObject(2, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

//    public String getMaHdCtByMaHd(String mahhhd) {
//        String sql = """
//            select ma_hoa_don_chi_tiet from Hoa_don
//            	  join Hoa_don_chi_tiet hdct on hdct.id_hoa_don = Hoa_don.ID
//            	  where ma_hoa_don = ?
//                     """;
//        
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setObject(1, mahhhd);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                HoaDonResp spct_rp = HoaDonResp.builder()
//                        .maHoaDonChiTiet(rs.getString(1))
//                        .build();
//             return spct_rp.getMaHoaDonChiTiet();
//            }
//            
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
//        return null;
//    }
    public String getMaHdCtByMaHd(String maHd) {
        String sql = """
    SELECT ma_hoa_don_chi_tiet
    FROM Hoa_don
    JOIN Hoa_don_chi_tiet hdct ON hdct.id_hoa_don = Hoa_don.ID
    WHERE ma_hoa_don = ?
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, maHd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maHoaDonChiTiet = rs.getString("ma_hoa_don_chi_tiet");
                return maHoaDonChiTiet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double calculateTongTien(String maHd) {
        String sql = """
SELECT SUM(hdct.so_luong * sp.don_gia) AS tong_tien
        FROM Hoa_don_chi_tiet hdct
        JOIN San_pham_chi_tiet sp ON hdct.id_san_pham_chi_tiet = sp.id
        JOIN Hoa_don hd ON hdct.id_hoa_don = hd.id
        WHERE hd.ma_hoa_don = ?
    """;

        double tongTien = 0.0;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, maHd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tongTien = rs.getDouble("tong_tien");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return tongTien;
    }

    private static final Logger LOGGER = Logger.getLogger(BanHangRepo.class.getName());

    public void createHoaDon(String maHd, String maHdCt) {
        Connection con = null;
        PreparedStatement psHd = null;
        PreparedStatement psHdCt = null;

        String sqlHoaDon = "INSERT INTO Hoa_don (ma_hoa_don) VALUES (?)";
        String sqlHoaDonChiTiet = "INSERT INTO Hoa_don_chi_tiet (ma_hoa_don_chi_tiet, id_hoa_don) VALUES (?, (SELECT id FROM Hoa_don WHERE ma_hoa_don = ?))";

        try {
            con = DBConnect.getConnection();
            con.setAutoCommit(false);

            psHd = con.prepareStatement(sqlHoaDon);
            psHd.setString(1, maHd);
            psHd.executeUpdate();

            psHdCt = con.prepareStatement(sqlHoaDonChiTiet);
            psHdCt.setString(1, maHdCt);
            psHdCt.setString(2, maHd);
            psHdCt.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL exception occurred", e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", ex);
                }
            }
        } finally {
            // Ensure resources are closed
            try {
                if (psHd != null) {
                    psHd.close();
                }
                if (psHdCt != null) {
                    psHdCt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }

    // Other methods...
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
                                                                                   WHERE San_pham_chi_tiet.trang_thai=1 and
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

    public boolean huyHoaDon(int idHD, String maHD) {
        int rowsAffected = 0;

        String sql1 = """
        UPDATE san_pham_chi_tiet
        SET so_luong = so_luong + (SELECT so_luong FROM hoa_don_chi_tiet WHERE hoa_don_chi_tiet.id_san_pham_chi_tiet = san_pham_chi_tiet.id AND hoa_don_chi_tiet.id_hoa_don = ?)
        WHERE id IN (SELECT id_san_pham_chi_tiet FROM hoa_don_chi_tiet WHERE id_hoa_don = ?)
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setInt(1, idHD);
            ps.setInt(2, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql2 = """
        UPDATE hoa_don_chi_tiet SET trang_thai = 0 WHERE id_hoa_don = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql3 = """
        UPDATE hoa_don SET trang_thai = 0 WHERE id = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setInt(1, idHD);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public boolean thanhToanHoaDonCoVc(double tongTien, String hinhThucThanhToan, int idVoucher, String maHd) {
        int rowsAffected = 0;

        // Cập nhật thông tin thanh toán vào hóa đơn
        String sql1 = """
        UPDATE Hoa_don
        SET tong_tien = ?,  
        hinh_thuc_thanh_toan = ?,
        id_voucher = ?, 
        trang_thai = 2
        WHERE ma_hoa_don = ?
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setObject(1, tongTien);
            ps.setObject(2, hinhThucThanhToan);
            ps.setObject(3, idVoucher);
            ps.setObject(4, maHd);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public boolean thanhToanHoaDonCoVc(double tongTien, String hinhThucThanhToan, Integer idVoucher, String maHd, String maVoucher) {
        int rowsAffected = 0;

        // Cập nhật thông tin thanh toán vào hóa đơn
        String sql1 = """
    UPDATE Hoa_don
    SET tong_tien = ?,  
    hinh_thuc_thanh_toan = ?,
    id_voucher = ?, 
    trang_thai = 2
    WHERE ma_hoa_don = ?
""";

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setDouble(1, tongTien);
            ps.setString(2, hinhThucThanhToan);
            if (idVoucher == null) {
                ps.setNull(3, java.sql.Types.INTEGER); // Đặt giá trị idVoucher thành null nếu không có voucher
            } else {
                ps.setInt(3, idVoucher);
            }
            ps.setString(4, maHd);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Giảm số lượng voucher

        String sql2 = """
            UPDATE voucher
            SET so_luong_voucher = so_luong_voucher - 1
            WHERE ma_voucher = ?
        """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setObject(1, maVoucher);
            rowsAffected += ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql3 = """
        UPDATE hoa_don_chi_tiet
        SET trang_thai = 2
        WHERE id_hoa_don = (SELECT id FROM hoa_don WHERE ma_hoa_don = ?)
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
            ps.setString(1, maHd);
            rowsAffected += ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

//     public boolean thanhToanHoaDonCoVc(double tongTien, String hinhThucThanhToan, Integer idVoucher, String maHd, String maVoucher,Integer idkh, String diaChiKh, String sdt) {
//        int rowsAffected = 0;
//
//        // Cập nhật thông tin thanh toán vào hóa đơn
//        String sql1 = """
//        UPDATE Hoa_don
//        SET 
//    	id_khach_hang =?,
//    	dia_chi_khach_hang=?,
//    	so_dt=?,
//    	tong_tien = ?,  
//        hinh_thuc_thanh_toan = ?,
//        id_voucher = ?, 
//        trang_thai = 2
//        WHERE ma_hoa_don = ?
//""";
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
//            ps.setObject(1, idkh);
//            ps.setObject(2, diaChiKh);
//            ps.setObject(3, sdt);
//            ps.setDouble(4, tongTien);
//            ps.setString(5, hinhThucThanhToan);
//            if (idVoucher == null) {
//                ps.setNull(6, java.sql.Types.INTEGER); // Đặt giá trị idVoucher thành null nếu không có voucher
//            } else {
//                ps.setInt(7, idVoucher);
//            }
//            ps.setString(8, maHd);
//            rowsAffected = ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Giảm số lượng voucher
//
//        String sql2 = """
//            UPDATE voucher
//            SET so_luong_voucher = so_luong_voucher - 1
//            WHERE ma_voucher = ?
//        """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
//            ps.setObject(1, maVoucher);
//            rowsAffected += ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String sql3 = """
//        UPDATE hoa_don_chi_tiet
//        SET trang_thai = 2
//        WHERE id_hoa_don = (SELECT id FROM hoa_don WHERE ma_hoa_don = ?)
//    """;
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql3)) {
//            ps.setString(1, maHd);
//            rowsAffected += ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return rowsAffected > 0;
//    }
    public boolean thanhToanHoaDonKoVc(double tongTien, String hinhThucThanhToan, String maHd) {
        int rowsAffected = 0;

        // Cập nhật thông tin thanh toán vào hóa đơn
        String sql1 = """
        UPDATE Hoa_don
        SET tong_tien = ?,  
        hinh_thuc_thanh_toan = ?,      
        trang_thai = 2
        WHERE ma_hoa_don = ?
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setObject(1, tongTien);
            ps.setObject(2, hinhThucThanhToan);
            ps.setObject(3, maHd);
            rowsAffected = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql2 = """
        UPDATE hoa_don_chi_tiet
        SET trang_thai = 2
        WHERE id_hoa_don = (SELECT id FROM hoa_don WHERE ma_hoa_don = ?)
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setString(1, maHd);
            rowsAffected += ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public void generatePdfInvoice(String maHd, double tongTien, double giamGia, double thanhToan, String hinhThucThanhToan) {

        Document document = new Document();
        try {
            // Đường dẫn file PDF
            String filePath = "C:\\Users\\quan1\\OneDrive\\Máy tính\\giaoDien01\\lib\\TimesNewRoman" + maHd + ".pdf";

            // Tạo PdfWriter để ghi tài liệu PDF
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Tạo và sử dụng phông chữ
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);

            // Thêm nội dung vào tài liệu
            document.add(new Paragraph("HÓA ĐƠN", font));
            document.add(new Paragraph("Mã hóa đơn: " + maHd, font));
            document.add(new Paragraph("Tổng tiền: " + tongTien + " VNĐ", font));
            document.add(new Paragraph("Giảm giá: " + giamGia + " VNĐ", font));
            document.add(new Paragraph("Thanh toán: " + thanhToan + " VNĐ", font));
            document.add(new Paragraph("Hình thức thanh toán: " + hinhThucThanhToan, font));

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public KhachHang getKhByMaHd(String maHd) {
        
        String sql = """
                     select ma_khach_hang,Hoa_don.ten_khach_hang from Khach_hang 
                     join Hoa_don on Hoa_don.id_khach_hang = Khach_hang.id
                     where ma_hoa_don = ?
                     """;      
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, maHd);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString(1));
                kh.setTenKH(rs.getString(2));
               
                return kh;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }

}
