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
public class SanPham {
    private Integer id;
    private String maSanPham;
    private String tenSanPham;
    private Date ngayTao;
    private Integer trangThai;

    public SanPham(String maSanPham, String tenSanPham, Date ngayTao, Integer trangThai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }
    
    
}
