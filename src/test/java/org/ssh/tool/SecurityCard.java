package org.ssh.tool;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Random;

import javax.imageio.ImageIO;

//http://chfh423.iteye.com/blog/1145652
/**
 * 电子口令卡
 * @author cfish@cfish.org
 * @version v1.0
 */
public class SecurityCard {
    private final static char[] xStr = "ABCDEFGHIJK".toCharArray();
    private final static char[] yStr = "123456".toCharArray();
    private final static DecimalFormat nft = new DecimalFormat("00");

    /**
     * 程序随机创建11位序列号
     * @return
     */
    public static String createSerialNumber(){
        StringBuffer str = new StringBuffer();
        Random random = new Random();
        while(str.length()!=11){
            //int i = random.nextInt(91);//字母数值组合
            //if(i>=48 && (i<58||i>64))str.append((char)i);
            str.append(random.nextInt(10));//数值
        }
        return str.toString();
    }

    /**
     * 获取序列号对应的坐标值(可以自定义算法)
     * @param serialNumber
     * @param x (0-10)
     * @param y (0-5)
     * @return
     */
    public static String getCoordinateValue(String serialNumber,int x,int y){
        byte[] bytes = serialNumber.getBytes();
        int pos = 2;//位偏移量
        if(bytes.length!=11||x>=xStr.length||y>=yStr.length) return nft.format(0);
        //计算数值,可随意更改
        int num = Math.abs((((bytes[x]+xStr[x])<<pos) * ((bytes[y]+yStr[y])<<pos)))+(x*y);
        char[] resNum = String.valueOf(Math.tan(num)).toCharArray();
        num =  Math.abs(Integer.valueOf((resNum[resNum.length-2])+""+resNum[resNum.length-1]));
        return nft.format(num);
    }

    /**
     * 获取序列号对应的坐标值
     * @param serialNumber 序列号
     * @param coordinate 坐标([A-K][1-6]),eg:A3 对应坐标(0,2)
     * @return
     */
    public static String getCoordinateValue(String serialNumber,String... coordinate){
        StringBuffer str = new StringBuffer();
        if(coordinate!=null){
            for(String crd : coordinate){
                char[] pos = crd.toCharArray();
                int x=0,y=0;
                for(int i=0;i<xStr.length;i++){if(xStr[i] == pos[0]){x = i;break;}}
                for(int i=0;i<yStr.length;i++){if(yStr[i] == pos[1]){y = i;break;}}
                str.append(getCoordinateValue(serialNumber,x,y));
            }
        }
        return str.toString();
    }

    /**
     * 创建口令卡
     * @param serialNumber,指定序列号,为空或者不足11位系统自动生成
     * @return
     */
    public static BufferedImage createPasswordCard(String serialNumber){
        //不满足规则重新创建
        if(serialNumber==null || serialNumber.length()!=11)
            serialNumber  = createSerialNumber();

        int width = 290;//宽度
        int height = 180;//高度
        BufferedImage buffimg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffimg.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        Font font = new Font("宋体", Font.PLAIN, 12);
        g.setFont(font);
        g.setColor(Color.gray);
        g.drawString("序列号：", 10, 15);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(serialNumber, 55, 15);
        g.drawRoundRect(0,0,width-1,height-1,10,10);//边框

        int l = 37,t = 15,w = 22,h=22;
        for(int i=1;i<=yStr.length+1;i++){
            if(i<=yStr.length){//画入Y坐标
                g.setPaint(new Color(209,139,69));
                g.fillRect(l-w-3, (i+1)*h-7, w+3, h+1);
                g.setPaint(Color.white);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString(String.valueOf(yStr[i-1]), l-t, t+(i+1)*h-5);
            }
            g.setPaint(new Color(93,126,133));
            g.drawLine(l, t+i*h, l+(xStr.length)*w, t+i*h);//表格线
            for(int j=0;j<=xStr.length;j++){
                g.drawLine(l+j*w, t+i*h, l+j*w,t+h);//表格线
                if(i==1 &&j<xStr.length){//画入X坐标
                    g.setPaint(Color.black);
                    g.setFont(new Font("Arial", Font.PLAIN, 14));
                    g.drawString(String.valueOf(xStr[j]), l+j*w+8, t+i*h-3);
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                }
                if(i<=yStr.length && j<xStr.length){
                    if(i%2==0){//颜色交替
                        g.setPaint(new Color(205,234,248));
                        g.fillRect(l+j*w, t+i*h+1, w, h);
                    }
                    g.setPaint(Color.black);
                    //写入值
                    g.drawString(getCoordinateValue(serialNumber,j,i-1), l+j*w+4, t+i*h+h-5);
                }

            }
        }
        font = new Font("宋体", Font.PLAIN, 60);
        g.setFont(font);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        g.setComposite(ac);
        g.rotate(Math.sin(35));//旋转
        g.drawString("逗你玩", -20, 178);//水印
        g.dispose();//释放
        return buffimg;
    }

    /**
     * 创建口令卡并写入指定流中,自动刷新缓冲
     * @param serialNumber,指定序列号,为空或者不足11位系统自动生成
     * @return
     */
    public static void writePCard2Stream(String serialNumber,OutputStream out) throws IOException{
        ImageIO.write(createPasswordCard(serialNumber), "gif", out);
        out.flush();
    }

    public static void main(String[] args) throws Exception {
        //创建一个序列号,也可以自己指定,我喜欢使用自己手机号码,^_^
        String no = createSerialNumber();

        //创建口令卡,写入磁盘
        writePCard2Stream(no, new FileOutputStream("c:/a.gif"));

        //验证坐标
        String[] pos = {"A3","B4","K2"};
        String posValue = getCoordinateValue(no, pos);//坐标值

        String inputValue = "623457";//输入值
        System.out.println("序列号:"+no);
        System.out.println("提取值:"+posValue);//输入值需与提取值匹配才算验证成功
        System.out.println("验证结果:"+posValue.equals(inputValue));
    }
}
