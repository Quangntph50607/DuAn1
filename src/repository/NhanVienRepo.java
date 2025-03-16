/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import config.DBConnect;
import response.NhanVienResp;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import entity.NhanVien;

/**
 *
 * @author DUNG
 */
public class NhanVienRepo {

    public List<NhanVienResp> getAllDangLam() {

        String query = """
                       SELECT dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.ID, dbo.Nhan_vien.so_dt, dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh, 
                                                                                                 dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, dbo.Nhan_vien.trang_thai
                                                                                    FROM   dbo.Chuc_vu INNER JOIN
                                                                                                 dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                       where [trang_thai]=1
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            List<NhanVienResp> list = new ArrayList<>();
            while (rs.next()) {
                NhanVienResp nv = NhanVienResp.builder()
                        .maChucVu(rs.getString(1))
                        .tenChucVu(rs.getString(2))
                        .maNhanVien(rs.getString(3))
                        .hoTen(rs.getString(4))
                        .id(rs.getInt(5))
                        .sdt(rs.getString(6))
                        .email(rs.getString(7))
                        .diaChi(rs.getString(8))
                        .ngaySinh(rs.getDate(9))
                        .gioiTinh(rs.getInt(10))
                        .ngayBatDau(rs.getDate(11))
                        .matKhau(rs.getString(12))
                        .trangThai(rs.getInt(13))
                        //                        .idChucVu(rs.getInt(15))
                        .build();
                list.add(nv);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVienResp> getAllNghiViec() {

        String query = """
                       SELECT dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.ID, dbo.Nhan_vien.so_dt, dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh, 
                                                                                                 dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, dbo.Nhan_vien.trang_thai
                                                                                    FROM   dbo.Chuc_vu INNER JOIN
                                                                                                 dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                       where [trang_thai]=0
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            List<NhanVienResp> list = new ArrayList<>();
            while (rs.next()) {
                NhanVienResp nv = NhanVienResp.builder()
                        .maChucVu(rs.getString(1))
                        .tenChucVu(rs.getString(2))
                        .maNhanVien(rs.getString(3))
                        .hoTen(rs.getString(4))
                        .id(rs.getInt(5))
                        .sdt(rs.getString(6))
                        .email(rs.getString(7))
                        .diaChi(rs.getString(8))
                        .ngaySinh(rs.getDate(9))
                        .gioiTinh(rs.getInt(10))
                        .ngayBatDau(rs.getDate(11))
                        .matKhau(rs.getString(12))
                        .trangThai(rs.getInt(13))
                        //                        .idChucVu(rs.getInt(15))
                        .build();
                list.add(nv);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVienResp> getAll() {

        String query = """
                       SELECT dbo.Nhan_vien.ID, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.so_dt,
                       dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh,
                       dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, 
                                                                                     dbo.Nhan_vien.trang_thai, dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu
                                                                        FROM   dbo.Chuc_vu INNER JOIN
                                                                                     dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            List<NhanVienResp> list = new ArrayList<>();
            while (rs.next()) {
                NhanVienResp response = new NhanVienResp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9),
                        rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13));
                list.add(response);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean add(NhanVien nv) {
        int check = 0;
        String sql = """
                     INSERT INTO [dbo].[Nhan_vien]
                                                       ([ma_nhan_vien]
                                                       ,[ho_ten]                                                      
                                                       ,[so_dt]
                                                       ,[email]
                                                       ,[dia_chi]
                                                       ,[ngay_sinh]
                                                       ,[gioi_tinh]
                                                       ,[ngay_bat_dau]
                                                       ,[mat_khau]
                                                       ,[id_chuc_vu]
                                                       ,[trang_thai])
                                                 VALUES
                                                       (?,?,?,?,?,?,?,?,?,?,?)
                    """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, nv.getMaNhanVien());
            ps.setObject(2, nv.getHoTen());
            ps.setObject(3, nv.getSdt());
            ps.setObject(4, nv.getEmail());
            ps.setObject(5, nv.getDiaChi());
            ps.setObject(6, nv.getNgaySinh());
            ps.setObject(7, nv.getGioiTinh());
            ps.setObject(8, nv.getNgayBatDau());
            ps.setObject(9, nv.getMatKhau());
            ps.setObject(10, nv.getIdChucVu());
            ps.setObject(11, nv.getTrangThai());

            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }

    public boolean update(Integer id, NhanVien nv) {
        int check = 0;
        String query = """
                       UPDATE [dbo].[Nhan_vien]
                          SET 
                              [ho_ten] =?
                             ,[so_dt] = ?
                             ,[email] = ?
                             ,[dia_chi] = ?
                             ,[ngay_sinh] = ?
                             ,[gioi_tinh] = ?
                             ,[ngay_bat_dau] = ?
                             ,[mat_khau] = ?
                             ,[id_chuc_vu] = ?
                             ,[trang_thai] = ?
                        WHERE id = ?
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

           
            ps.setObject(1, nv.getHoTen());

            ps.setObject(2, nv.getSdt());
            ps.setObject(3, nv.getEmail());
            ps.setObject(4, nv.getDiaChi());
            ps.setObject(5, nv.getNgaySinh());
            ps.setObject(6, nv.getGioiTinh());
            ps.setObject(7, nv.getNgayBatDau());
            ps.setObject(8, nv.getMatKhau());
            ps.setObject(9, nv.getIdChucVu());
            ps.setObject(10, nv.getTrangThai());
            ps.setObject(11, id);
            check = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean remove(Integer id) {
        int check = 0;
        String sql = """
                     UPDATE [dbo].[Nhan_vien]
                             SET [trang_thai] = 0
                           WHERE ID = ?
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;

    }

    public ArrayList<NhanVienResp> search(String keyword
    ) {
        String sql = """
                   SELECT dbo.Nhan_vien.ID, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.so_dt,
                                             dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh,
                                             dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, 
                      					   dbo.Nhan_vien.trang_thai, dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu
                      					   FROM   dbo.Chuc_vu INNER JOIN
                      					   dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                      						where dbo.Nhan_vien.trang_thai=1 and
                     """;
        if (keyword.length() > 0) { // isempty
            sql += """
                   
                  	(ma_nhan_vien like ?
                        or ho_ten like ?
                        or so_dt like ?
                        or email like ?
                        or dia_chi like ?
   			)
                  """;
        } else {
            return null;
        }
        ArrayList<NhanVienResp> lists = new ArrayList<>();
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
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(new NhanVienResp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9),
                        rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13)));

            }
        } catch (Exception e) {
            e.printStackTrace(System.out); // nem loi khi xay ra 
        }
        return lists;
    }

    public List<NhanVienResp> loc(Integer idCv, Integer gt) {

        String query = """
 SELECT dbo.Nhan_vien.ID, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.so_dt,
           dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh,
           dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, 
           dbo.Nhan_vien.trang_thai, dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu
            FROM   dbo.Chuc_vu INNER JOIN
            dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                       
            where (dbo.Nhan_vien.gioi_tinh = ? or ? is null)
            and (dbo.Chuc_vu.ID = ? or ? is null) and [trang_thai]=1
                                 
                       """;
        List<NhanVienResp> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setObject(3, idCv);
            ps.setObject(4, idCv);
            ps.setObject(1, gt);
            ps.setObject(2, gt);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NhanVienResp response = new NhanVienResp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9),
                        rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13));
                list.add(response);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVienResp> locVaiTro(Integer idCv) {

        String query = """
                       SELECT dbo.Nhan_vien.ID, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.so_dt,
             dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh,
         dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, 
                     dbo.Nhan_vien.trang_thai, dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu
                                                      FROM   dbo.Chuc_vu INNER JOIN
                     dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
        				 where dbo.Chuc_vu.ID = ? and [trang_thai]=1
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCv);
            ResultSet rs = ps.executeQuery();
            List<NhanVienResp> list = new ArrayList<>();
            while (rs.next()) {
                NhanVienResp response = new NhanVienResp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9),
                        rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13));
                list.add(response);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVienResp> loc01(Integer gioiTinh) {

        String query = """
                       SELECT dbo.Nhan_vien.ID, dbo.Nhan_vien.ma_nhan_vien, dbo.Nhan_vien.ho_ten, dbo.Nhan_vien.so_dt,
                       dbo.Nhan_vien.email, dbo.Nhan_vien.dia_chi, dbo.Nhan_vien.ngay_sinh, dbo.Nhan_vien.gioi_tinh,
                      dbo.Nhan_vien.ngay_bat_dau, dbo.Nhan_vien.mat_khau, 
                      dbo.Nhan_vien.trang_thai, dbo.Chuc_vu.ma_chuc_vu, dbo.Chuc_vu.ten_chuc_vu
                       FROM   dbo.Chuc_vu INNER JOIN
                           dbo.Nhan_vien ON dbo.Chuc_vu.ID = dbo.Nhan_vien.id_chuc_vu
                                                                                                                                                                                                                                                                                
                            where dbo.Nhan_vien.gioi_tinh = ? and [trang_thai]=1
                       """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, gioiTinh);
            ResultSet rs = ps.executeQuery();
            List<NhanVienResp> list = new ArrayList<>();
            while (rs.next()) {
                NhanVienResp response = new NhanVienResp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9),
                        rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13));
                list.add(response);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkTrung(String ma) {

        String query = """
                       SELECT [ma_nhan_vien]   
                         FROM [dbo].[Nhan_vien]
                         where [ma_nhan_vien] = ?
                       """;
        List<NhanVien> list = new ArrayList<>();
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
    
    public boolean checkTrungSDT(String sdt) {
        String sql = "SELECT COUNT(*) FROM Nhan_vien WHERE so_dt = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkTrungEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Nhan_vien WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    

}
