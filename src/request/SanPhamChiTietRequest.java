/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author DUNG
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SanPhamChiTietRequest {

    private String maSanPhamChiTiet;
    private String tenSanPhamChiTiet;
    private Integer idKichThuoc;
    private Integer idMauSac;
    private Integer idChatLieu;
    private Integer idDa;
    private Integer idSanPham;
    private Integer idXuatXu;
    private Integer soLuong;
    private Float donGia;
    private String anh;
    private Integer trangThai;
}
