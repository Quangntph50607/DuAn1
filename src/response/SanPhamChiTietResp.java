/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package response;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SanPhamChiTietResp {
    private Integer id;
    private String ma_san_pham_chi_tiet;
    private String ten_san_pham_chi_tiet;
    private String kichThuoc;
    private String tenMau;
    private String tenChatLieu;
    private String tenDa;
    private String tenNuoc;
    private String maSP;
    private Integer so_luong;
    private Float don_gia;
    private String anh;
    private Integer trang_thai;

    public SanPhamChiTietResp(String ma_san_pham_chi_tiet, String ten_san_pham_chi_tiet, String kichThuoc, String tenMau, String tenChatLieu, String tenDa, String tenNuoc, String maSP, Integer so_luong, Float don_gia, String anh, Integer trang_thai) {
        this.ma_san_pham_chi_tiet = ma_san_pham_chi_tiet;
        this.ten_san_pham_chi_tiet = ten_san_pham_chi_tiet;
        this.kichThuoc = kichThuoc;
        this.tenMau = tenMau;
        this.tenChatLieu = tenChatLieu;
        this.tenDa = tenDa;
        this.tenNuoc = tenNuoc;
        this.maSP = maSP;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.anh = anh;
        this.trang_thai = trang_thai;
    }
    
    

    public Object[] toRowTableSPCT(int stt) {
        return new Object[]{
            stt, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, kichThuoc, tenMau, tenChatLieu, tenDa, tenNuoc, maSP, so_luong, don_gia, anh, trang_thai == 0 ? "Hết sản phẩm" : "Còn sản phẩm"
        };
    }

}
