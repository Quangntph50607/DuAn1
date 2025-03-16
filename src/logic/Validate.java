/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import com.raven.swing.TextField;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Ca1
 */
public class Validate {

    boolean chuoiHopLe = true;
    Map<String, String> listWarning = new LinkedHashMap<>();

    public Validate() {
    }

    public void khongDuocTrong(TextField... textFields) {
        for (TextField i : textFields) {
            if (i.getText().trim().isEmpty()) {
                listWarning.put(i.getLabelText(), i.getLabelText() + " không được để trống");
                chuoiHopLe = false;
            }
        }
    }

    ;

    public void khongDuocTrong(String label, String input) {
        if (input.trim().isEmpty()) {
            listWarning.put(label, label + " không được để trống");
            chuoiHopLe = false;
        }
    }

    public void chiDuocChuaSo(TextField... textFields) {
        for (TextField i : textFields) {
            String number = i.getText();
            try {
                Double.parseDouble(number);
            } catch (Exception e) {
                if (!number.isEmpty()) {
                    listWarning.put(i.getLabelText(), i.getLabelText() + " chỉ được chứa số");
                }
                chuoiHopLe = false;
            }
        }
    }

    public void chiDuocChuaSo(String label, String input) {
        try {
            Double.parseDouble(input);
        } catch (Exception e) {
            if (!input.isEmpty()) {
                listWarning.put(label, label + " chỉ được chứa số");
            }
            chuoiHopLe = false;
        }
    }

    public void phaiLonHon0(TextField... textFields) {
        for (TextField i : textFields) {
            String number = i.getText();
            try {
                if (Double.parseDouble(number) <= 0) {
                    listWarning.put(i.getLabelText(), i.getLabelText() + " phải lớn hơn 0");
                    chuoiHopLe = false;
                }
            } catch (Exception e) {
            }
        }
    }

    void phaiLonHon0(String label, String input) {
        try {
            if (Double.parseDouble(input) <= 0) {
                listWarning.put(label, label + " phải lớn hơn 0");
                chuoiHopLe = false;
            }
        } catch (Exception e) {
            // Handle exception if needed
        }

    }

    public void khongChuaSoVaKiTuDacBiet(TextField... textFields) {
        Pattern pattern = Pattern.compile(
                "[ÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬĐÊẾỀỂỄỆÍÌỈĨỊÒÓỌỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÙÚỤỦŨỤƯỨỪỰỲÝỴỶỸáàảãạăắằẳẵặâấầẩẫậđêếềểễệíìỉĩịòóọỏõôốồổỗộơớờởỡợùúụủũụưứừựỳýỵỷỹ]");

        for (TextField i : textFields) {
            if (!i.getText().isEmpty()) {
                String text = i.getText();
                Matcher matcher = pattern.matcher(text);
                if (!matcher.find()) {
                    listWarning.put(i.getLabelText(), i.getLabelText() + " không được chứa kí tự đặc biệt");
                    chuoiHopLe = false;
                }
            }
        }
    }

    public void khongDuocLaSoAm(TextField... textFields) {
        for (TextField i : textFields) {
            if (!i.getText().isEmpty()) {
                String number = i.getText();
                try {
                    if ( Double.parseDouble(number) < 0.0 ) {
                        listWarning.put(i.getLabelText(), i.getLabelText() + " phải lớn hơn hoặc bằng 0");
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public boolean isChuoiHopLe() {
        return chuoiHopLe;
    }

    public void setChuoiHopLe(boolean chuoiHopLe) {
        this.chuoiHopLe = chuoiHopLe;
    }

    public void showWarning(JInternalFrame panel) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : listWarning.entrySet()) {
            stringBuilder.append(entry.getValue() + "\n");
        }
        JOptionPane.showMessageDialog(panel, stringBuilder.toString());
    }

    public void checkDateIsBefore(JDateChooser dateChooser1, JDateChooser dateChooser2) {
        Date date1 = dateChooser1.getDate();
        Date date2 = dateChooser2.getDate();
        if (date1 != null && date2 != null) {
            LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!localDate1.isBefore(localDate2)) {
                listWarning.put("Ngày", "Ngày bắt đầu phải nhỏ hơn ngày kết thúc ");
                chuoiHopLe = false;
            }
        } else {
            listWarning.put("Ngày", "Ngày không được để trống");
            chuoiHopLe = false;
        }
    }

    public void mucGiamTheoSoTienPhaiLonHon1000VND(TextField textField) {
        try {
            double n = Double.parseDouble(textField.getText());
            if (n <= 1000) {
                listWarning.put(textField.getLabelText(), textField.getLabelText() + " phải lớn hơn 1000 VND");
                chuoiHopLe = false;
            }
        } catch (Exception e) {
        }
    }

    public void mucGiamTheoPhanTramPhaiLonHon0VaBeHon100(TextField textField) {
        try {
            double n = Double.parseDouble(textField.getText());
            if (n <= 0 || n >= 100) {
                listWarning.put(textField.getLabelText(), textField.getLabelText() + " phải lớn hơn 0 và bé hơn 100");
                chuoiHopLe = false;
            }
        } catch (Exception e) {
        }
    }

    public void soThuNhatPhaiNhoHonSoThuHai(TextField textField1, TextField textField2) {
        try {
            double n1 = Double.parseDouble(textField1.getText());
            double n2 = Double.parseDouble(textField2.getText());
            if (n1 > n2) {
                listWarning.put(textField1.getLabelText() + textField2.getLabelText(), textField1.getLabelText() + " phải nhỏ hơn  " + textField2.getLabelText());
                chuoiHopLe = false;
            }
        } catch (Exception e) {
        }
    }

    public void checkDateIsAfterOrEqualCurrent(JDateChooser dateChooser) {
        Date date = dateChooser.getDate();
        if (date != null) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (localDate.isBefore(LocalDate.now())) {
                listWarning.put("Ngày", "Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại  ");
                chuoiHopLe = false;
            }
        } else {
            listWarning.put("Ngày", "Ngày không được để trống");
            chuoiHopLe = false;
        }
    }
}
