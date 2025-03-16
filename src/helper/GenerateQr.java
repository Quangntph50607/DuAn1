/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DUNG
 */
public class GenerateQr {
    private static final String outPutQr = "C:\\Users\\quan1\\OneDrive\\Máy tính\\GenerateQr.PNG";
    

    private static void generateQRcode(String text, int width, int height, String filePath) throws WriterException {

       QRCodeWriter qc = new QRCodeWriter();
       BitMatrix bm = qc.encode(text, BarcodeFormat.QR_CODE, width, height);
       Path pobj = FileSystems.getDefault().getPath(filePath);

        try {
            MatrixToImageWriter.writeToPath(bm,"PNG",pobj);
        } catch (IOException ex) {
            Logger.getLogger(GenerateQr.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) throws WriterException{
        generateQRcode("SPCT010", 1250, 1250, outPutQr);
    }

}
