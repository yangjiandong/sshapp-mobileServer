package org.ssh.tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

//将图片转成二进制并生成Base64编码字符串,再将二进制转换成各种图片
public class TestImageBinary {
    //static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    //static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    public static void main(String[] args) {
        System.out.println(getImageBinary());

        base64StringToImage(getImageBinary());
    }

    static String getImageBinary(){
        File f = new File("c://test.png");
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            //return encoder.encodeBuffer(bytes).trim();
            return new Base64().encodeAsString(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void base64StringToImage(String base64String){
        try {
            //byte[] bytes1 = decoder.decodeBuffer(base64String);
            byte[] bytes1 = new Base64().decodeBase64(base64String);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 =ImageIO.read(bais);
            File w2 = new File("c://test.jpg");//可以是jpg,png,gif格式
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}