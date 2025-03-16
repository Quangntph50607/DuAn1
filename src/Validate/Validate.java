/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        String regex = "^\\w+[a-z0-9]*@{1}+gmail.com$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(!m.find()){
            return false;
        }
        return true;
    }
    public boolean checkDate(Date ngay) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    try {
        LocalDate dateOfBirth = ngay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        
        if (dateOfBirth.isAfter(today)) {
            return false;
        } else {
            return true;
        }
    } catch (Exception ex) {
        return false;
    }
}
    

}
