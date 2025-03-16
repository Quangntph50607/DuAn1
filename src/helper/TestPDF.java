/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TestPDF {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test PDF");
        JPanel panel = new JPanel();
        JButton button = new JButton("Xuất PDF");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writePDF writePDF = new writePDF();
                int maphieu = 123; // Thay thế bằng mã phiếu thực tế
                writePDF.writePX(maphieu);
            }
        });

        panel.add(button);
        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
