package org.ssh.pm.common.utils.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.LuminanceSource;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.Result;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.common.HybridBinarizer;

/**
 * @blog http://sjsky.iteye.com
 * @author Michael
 */
public class ZxingDecoderHandler {

    /**
     * @param imgPath
     * @return String
     */
//    public String decode(String imgPath) {
//        BufferedImage image = null;
//        Result result = null;
//        try {
//            image = ImageIO.read(new File(imgPath));
//            if (image == null) {
//                System.out.println("the decode image may be not exit.");
//            }
//            LuminanceSource source = new BufferedImageLuminanceSource(image);
//            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//            Hashtable<Object, Object> hints = new Hashtable<Object, Object>();
//            hints.put(DecodeHintType.CHARACTER_SET, "GBK");
//
//            result = new MultiFormatReader().decode(bitmap, hints);
//            return result.getText();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        String imgPath = "d:/test/twocode/michael_zxing.png";
//        ZxingDecoderHandler handler = new ZxingDecoderHandler();
//        String decodeContent = handler.decode(imgPath);
//        System.out.println("解码内容如下：");
//        System.out.println(decodeContent);
//        System.out.println("Michael ,you have finished zxing decode.");
//
//    }
}
