package org.ssh.pm.common.utils.qrcode;

import java.io.File;
import java.util.Hashtable;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @blog http://sjsky.iteye.com
 * @author Michael
 */
public class ZxingEncoderHandler {

    /**
     * 编码
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
//    public void encode(String contents, int width, int height, String imgPath) {
//        Hashtable<Object, Object> hints = new Hashtable<Object, Object>();
//        // 指定纠错等级
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//        // 指定编码格式
//        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
//        try {
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
//                    BarcodeFormat.QR_CODE, width, height, hints);
//
//            MatrixToImageWriter
//                    .writeToFile(bitMatrix, "png", new File(imgPath));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        String imgPath = "d:/test/twocode/michael_zxing.png";
//        String contents = "Hello Michael(大大),welcome to Zxing!"
//                + "\nMichael’s blog [ http://sjsky.iteye.com ]"
//                + "\nEMail [ sjsky007@gmail.com ]" + "\nTwitter [ @suncto ]";
//        int width = 300, height = 300;
//        ZxingEncoderHandler handler = new ZxingEncoderHandler();
//        handler.encode(contents, width, height, imgPath);
//
//        System.out.println("Michael ,you have finished zxing encode.");
//    }
}
