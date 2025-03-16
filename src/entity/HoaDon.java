/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class HoaDon {
    private Integer idHoaDon;
    private String MaHoaDon;
        private Integer idKhachHang;
        private Integer idNhanVien;
        private String tenKhachHang;
        private String diaChi;
        private String sdt;
        private Date ngayTao;
        private Float tongTien;
        private String hinhThucThanhToan;
        private Integer idVoucher;
        private Integer trangThai;

}
