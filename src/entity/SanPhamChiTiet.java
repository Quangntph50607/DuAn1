/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author ASUS
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SanPhamChiTiet {
    private Integer id;
    private Integer id_kich_thuoc;
    private Integer id_mau_sac;
    private Integer id_chat_lieu;
    private Integer id_da;
    private Integer id_san_pham;
    private Integer id_xuat_xu;
    private String ma_san_pham_chi_tiet;
    private String ten_san_pham_chi_tiet;
    private Integer so_luong;
    private Float don_gia;
    private String anh;
    private Integer trang_thai;

    public SanPhamChiTiet(Integer id_kich_thuoc, Integer id_mau_sac, Integer id_chat_lieu, Integer id_da, Integer id_san_pham, Integer id_xuat_xu, String ma_san_pham_chi_tiet, String ten_san_pham_chi_tiet, Integer so_luong, Float don_gia, String anh, Integer trang_thai) {
        this.id_kich_thuoc = id_kich_thuoc;
        this.id_mau_sac = id_mau_sac;
        this.id_chat_lieu = id_chat_lieu;
        this.id_da = id_da;
        this.id_san_pham = id_san_pham;
        this.id_xuat_xu = id_xuat_xu;
        this.ma_san_pham_chi_tiet = ma_san_pham_chi_tiet;
        this.ten_san_pham_chi_tiet = ten_san_pham_chi_tiet;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.anh = anh;
        this.trang_thai = trang_thai;
    }
    
    
}

