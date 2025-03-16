/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import Validate.*;
import entity.NhanVien;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author DUNG
 */
public class Validate {

    public boolean checkSdt(String sdt) {
        String regex = "^0\\d{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sdt);
        if (!m.find()) {
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email) {
        String regex = "^[a-zA-Z][a-zA-z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+){1,3}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (!m.find()) {
            return false;
        }
        return true;
    }
    
   

   
    public boolean checkDate(Date ngay) {
        try {
            
            LocalDate dateOfBirth = ngay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            if (dateOfBirth.isAfter(today)) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }

        return true;

    }
    
    public boolean checkDate01(Date ngay) {
        try {
           
            LocalDate dateOfBirth = ngay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            
            if (dateOfBirth.isBefore(today)) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }

        return true;

    }
}
