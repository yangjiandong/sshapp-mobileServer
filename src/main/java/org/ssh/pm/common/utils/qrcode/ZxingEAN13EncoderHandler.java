package org.ssh.pm.common.utils.qrcode;

import java.io.File;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;

/**
 * @blog http://sjsky.iteye.com
 * @author Michael
 */
public class ZxingEAN13EncoderHandler {

    /**
     * 编码
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
//    public void encode(String contents, int width, int height, String imgPath) {
//        int codeWidth = 3 + // start guard
//                (7 * 6) + // left bars
//                5 + // middle guard
//                (7 * 6) + // right bars
//                3; // end guard
//        codeWidth = Math.max(codeWidth, width);
//        try {
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
//                    BarcodeFormat.EAN_13, codeWidth, height, null);
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
//        String imgPath = "d:/test/twocode/zxing_EAN13.png";
//        // 益达无糖口香糖的条形码
//        String contents = "6923450657713";
//
//        int width = 105, height = 50;
//        ZxingEAN13EncoderHandler handler = new ZxingEAN13EncoderHandler();
//        handler.encode(contents, width, height, imgPath);
//
//        System.out.println("Michael ,you have finished zxing EAN13 encode.");
//    }
}

