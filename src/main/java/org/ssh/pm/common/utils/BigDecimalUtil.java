package org.ssh.pm.common.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

/**
 * BigDecimal 类型有关的实用例程
 *
 */
public final class BigDecimalUtil {

    static final String CHINESE_DIGIT = "零壹贰叁肆伍陆柒捌玖";
    static final String CHINESE_MONEYSTR = "亿仟佰拾万仟佰拾元角分";

    /**
     * 四舍五入，指定二位小数
     *
     * @param value
     * @return
     */
    public static BigDecimal getScaleValue(BigDecimal value) {
        if (value != null) {
            value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

    public static BigDecimal getScaleValue(int scaleValue, BigDecimal value) {
        if (value != null) {
            value = value.setScale(scaleValue, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

    /**
     * 返回当前金额格式
     * 参见 http://www.javaworld.com/javaworld/jw-06-2001/jw-0601-cents.html
     * @param currency
     * @return
     */
    public static String getFormatCurrency(double currency) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        if (currency < 0) {
            currency = -currency;
            return "-" + nf.format(currency).substring(1);
        } else {
            return nf.format(currency).substring(1);
        }
    }

    /**
     * 转换大写金额
     * 说明：最大金额为亿
     * @param money
     * @return
     */
    public static String getChineseFormatMoney(BigDecimal money) throws Exception {

        String chinaFormatMoney = "";

        //是否是负的
        boolean isNegative = money.compareTo(new BigDecimal(0)) == -1;
        money = money.abs();

        //最小到分
        money = getScaleValue(money);

        if (money.compareTo(new BigDecimal("999999999")) == 1) {
            throw new Exception("超出金额范围。");
        } else if (money.compareTo(new BigDecimal("0.00")) == 0) {
            return "零万零仟零佰零拾零元零角零分";
        }

        String sMoney = money.toString();
        //格式为12,2
        sMoney = StringUtils.leftPad(sMoney, 12, "0");

        String money1 = getChineseDigit(Integer.parseInt(sMoney.substring(0, 1)));
        String money2 = getChineseDigit(Integer.parseInt(sMoney.substring(1, 2)));
        String money3 = getChineseDigit(Integer.parseInt(sMoney.substring(2, 3)));
        String money4 = getChineseDigit(Integer.parseInt(sMoney.substring(3, 4)));
        String money5 = getChineseDigit(Integer.parseInt(sMoney.substring(4, 5)));
        String money6 = getChineseDigit(Integer.parseInt(sMoney.substring(5, 6)));
        String money7 = getChineseDigit(Integer.parseInt(sMoney.substring(6, 7)));
        String money8 = getChineseDigit(Integer.parseInt(sMoney.substring(7, 8)));
        String money9 = getChineseDigit(Integer.parseInt(sMoney.substring(8, 9)));

        String money10 = getChineseDigit(Integer.parseInt(sMoney.substring(10, 11)));
        String moneya = getChineseDigit(Integer.parseInt(sMoney.substring(11)));

        String[] moneys = { money1, money2, money3, money4, money5, money6, money7, money8, money9, money10, moneya };

        String sign = isNegative ? "负" : "";
        boolean begin = false;

        for (int i = 0; i <= moneys.length - 1; i++) {
            String moneyTemp = moneys[i];
            if (moneyTemp.equals("零") && !begin) {
                continue;
            }
            begin = true;

            chinaFormatMoney = chinaFormatMoney + moneyTemp + CHINESE_MONEYSTR.substring(i, i + 1);
        }

        //return sign + delZero(chinaFormatMoney);
        return sign + chinaFormatMoney;
    }

    //将单个数字(0-9)替换成汉字大写数字
    private static String getChineseDigit(int i) {
        i = i < 0 ? -1 * i : i;
        return CHINESE_DIGIT.substring((i % 10), (i % 10) + 1);
    }

    /**
     *  功能: 整理万左方金额串中零的表达方式
     *  测试用例: 未处理前:                处理后:
     *          "壹 佰 零 拾 零 万.." -> "壹 佰 万.."
     *          "壹 佰 壹 拾 零 万.." -> "壹 佰 壹 拾 万.."
     *          "壹 佰 零 拾 壹 万.." -> "壹 佰 零 壹 万.."
     * TODO
     */
    private static String delZero(String money) {
        int i = money.indexOf("万");
        if (i != -1) {
            if ((money.substring(i - 1, i)).equals("零")) {
                money = money.substring(0, i - 2) + money.substring(i);
            }

            //去掉"万"左边第二个汉字位置上的"零"后的金额单位("拾")
            //(当"壹 佰 零 拾 零 万.."时, "零 拾"一起去掉)
            i = money.indexOf("万");
            if ((money.substring(i - 2, i)).equals("零")) {
                money = money.substring(0, i - 3) + money.substring(i);
            }

            //去掉"万"左边第三个汉字位置上的"零"后的金额单位("拾")
            i = money.indexOf("万");
            //          if (i)
            ////            wanpos = AT("万",m.jestr)
            ////            IF m.wanpos > 9 AND SUBS(m.jestr,m.wanpos-9,2) = "零"
            ////               jestr = SUBS(m.jestr,1,m.wanpos-7) + SUBS(m.jestr,m.wanpos-3)
            ////            ENDIF

        }

        return money;
    }

    public static BigDecimal notNull(String theString) throws NumberFormatException {
        BigDecimal num = new BigDecimal(0.00);
        if (theString == null || theString.equals("")) {
            return num;
        }

        num = new BigDecimal(theString);
        return num;
    }

    //转换成会计格式
    public static String getAccountantMoney(BigDecimal money) {
        String disposeMoneyStr = money.toString();
        //小数点处理
        int dotPosition = disposeMoneyStr.indexOf(".");
        String exceptDotMoeny = null;//小数点之前的字符串
        String dotMeony = null;//小数点之后的字符串
        if (dotPosition > 0) {
            exceptDotMoeny = disposeMoneyStr.substring(0, dotPosition);
            dotMeony = disposeMoneyStr.substring(dotPosition);
        } else {
            exceptDotMoeny = disposeMoneyStr;
        }
        //负数处理
        int negativePosition = exceptDotMoeny.indexOf("-");
        if (negativePosition == 0) {
            exceptDotMoeny = exceptDotMoeny.substring(1);
        }
        StringBuffer reverseExceptDotMoney = new StringBuffer(exceptDotMoeny);
        reverseExceptDotMoney.reverse();//字符串倒转
        //      reverse(reverseExceptDotMoeny);
        char[] moneyChar = reverseExceptDotMoney.toString().toCharArray();
        StringBuffer returnMeony = new StringBuffer();//返回值
        for (int i = 0; i < moneyChar.length; i++) {
            if (i != 0 && i % 3 == 0) {
                returnMeony.append(",");//每隔3位加','
            }
            returnMeony.append(moneyChar[i]);
        }
        returnMeony.reverse();//字符串倒转
        //      reverse(returnMeony);
        if (dotPosition > 0) {
            returnMeony.append(dotMeony);
        }
        if (negativePosition == 0) {
            return "-" + returnMeony.toString();
        } else {
            return returnMeony.toString();
        }
    }

    public static String getChineseNumber(String number) {

        String result = "";

        for (int i = 0; i < number.trim().length(); i++) {
            String a = number.trim().substring(i, i + 1);
            switch (Integer.valueOf(a)) {
            case 1:
                result = result + "一";
                break;
            case 2:
                result = result + "二";
                break;
            case 3:
                result = result + "三";
                break;
            case 4:
                result = result + "四";
                break;
            case 5:
                result = result + "五";
                break;
            case 6:
                result = result + "六";
                break;
            case 7:
                result = result + "七";
                break;
            case 8:
                result = result + "八";
                break;
            case 9:
                result = result + "九";
                break;
            case 0:
                result = result + "十";
                break;
            default:
                break;
            }
        }

        return result;

    }

} // EOP