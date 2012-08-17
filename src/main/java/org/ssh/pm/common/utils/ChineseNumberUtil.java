package org.ssh.pm.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class ChineseNumberUtil {

    // 货币大写形式
    private static String bigLetter[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌",
            "玖" };

    // 货币整数单位
    private static String unit[] = new String[] { "元", "拾", "佰", "仟", "万", "亿" };

    // 货币小数单位
    private static String small[] = { "分", "角" };

    /**
     * 日期转化为中文大写日期
     */
    public static String dataToUpper(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return numToUpper(year) + "年" + monthToUppder(month) + "月" + dayToUppder(day) + "日";
    }

    /**
     * 将数字转化为中文大写数字,不带进制和单位
     */
    public static String numToUpper(int num) {
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + bigLetter[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }

    // 月转化为大写
    public static String monthToUppder(int month) {
        if (month < 10) {
            return "零" + numToUpper(month);
        } else if (month == 10) {
            return "壹拾";
        } else {
            return "壹拾" + numToUpper(month - 10);
        }
    }

    // 日转化为大写
    public static String dayToUppder(int day) {
        if (day < 20) {
            return monthToUppder(day);
        } else {
            char[] str = String.valueOf(day).toCharArray();
            if (str[1] == '0') {
                return numToUpper(Integer.parseInt(str[0] + "")) + "拾";
            } else {
                return numToUpper(Integer.parseInt(str[0] + "")) + "拾"
                        + numToUpper(Integer.parseInt(str[1] + ""));
            }
        }
    }

    /**
     * 将金额字符串转换为中文大写的金额,小数部分自动转换为两位小数
     */
    public static String moneyToUppder(String money) {
        if (StringUtils.isNotBlank(money) && NumberUtils.isNumber(money)) {
            // 去掉字符串开始的"0"字符
            while (StringUtils.startsWith(money, "0")) {
                money = StringUtils.substring(money, 1);
            }

            if (StringUtils.contains(money, ".")) {
                // 以小数点为界分割这个字符串
                int index = money.indexOf(".");

                // 这个数的整数部分
                String intOnly = money.substring(0, index);

                // 这个数的小数点及小数部分
                String dotSmall = StringUtils.substring(money, index);
                dotSmall = roundString(dotSmall);

                // 四舍五入后的小数
                index = dotSmall.indexOf(".");
                dotSmall = StringUtils.substring(dotSmall, index);

                money = intOnly + dotSmall;

            } else {
                money = money + ".00";
            }

            return splitNum(money);
        } else {
            return "";
        }

    }

    /**
     * 对传入的数进行四舍五入操作
     */
    private static String roundString(String s) {
        // 如果传入的是空串则继续返回空串
        if ("".equals(s)) {
            return "";
        }

        // 将这个数转换成 double 类型，并对其进行四舍五入操作
        double d = Double.parseDouble(s);

        // 此操作作用在小数点后两位上
        d = (d * 100 + 0.5) / 100;

        // 将 d 进行格式化
        s = new java.text.DecimalFormat("##0.000").format(d);

        // 以小数点为界分割这个字符串
        int index = s.indexOf(".");

        // 这个数的整数部分
        String intOnly = s.substring(0, index);

        // 这个数的小数部分
        String smallOnly = s.substring(index + 1);

        // 如果小数部分大于两位，只截取小数点后两位
        if (smallOnly.length() > 2) {
            String roundSmall = smallOnly.substring(0, 2);
            // 把整数部分和新截取的小数部分重新拼凑这个字符串
            s = intOnly + "." + roundSmall;
        }

        return s;
    }

    /**
     * 把用户输入的数以小数点为界分割开来，<br>
     * 并调用 numFormat() 方法 进行相应的中文金额大写形式的转换 <br/>
     * 注：传入的这个数应该是经过 roundString() 方法进行了四舍五入操作的
     * 
     * @param s
     *            String
     * @return 转换好的中文金额大写形式的字符串
     */
    private static String splitNum(String s) {
        // 如果传入的是空串则继续返回空串
        if ("".equals(s)) {
            return "";
        }

        // 以小数点为界分割这个字符串
        int index = s.indexOf(".");

        // 截取并转换这个数的整数部分
        String intOnly = s.substring(0, index);
        String part1 = intToBig(intOnly);

        // 截取并转换这个数的小数部分
        String part2 = "";
        String smallOnly = s.substring(index + 1);
        if (smallOnly.equals("00") && part1.length() > 0) {
            part2 = "整";
        } else {
            part2 = dotToBig(smallOnly);
        }

        // 把转换好了的整数部分和小数部分重新拼凑一个新的字符串
        String newStr = part1 + part2;
        while (StringUtils.startsWith(newStr, "零")) {
            newStr = StringUtils.substring(newStr, 1);
        }

        return newStr;
    }

    /**
     * 将金额的整数部分转换为大写
     */
    private static String intToBig(String num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num.length(); i++) {
            int digit = Integer.valueOf(num.substring(i, i + 1)).intValue();
            sb.append(bigLetter[digit]);
        }
        String face = sb.reverse().toString();
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < face.length(); i++) {
            // 加权
            if (i == 0) {
                resultSb.append(unit[0]);
            } else {
                // 万位处理
                if ((i + 4) % 8 == 0) {
                    resultSb.append(unit[4]);
                }
                // 亿位处理
                else if (i % 8 == 0) {
                    resultSb.append(unit[5]);
                } else {
                    resultSb.append(unit[i % 4]);
                }
            }
            // 加面值
            String temp = face.substring(i, i + 1);
            resultSb.append(temp);
        }

        String result = resultSb.reverse().toString();

        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零仟", "零");
        result = result.replaceAll("[零]+", "零");

        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零万", "万零");
        result = result.replaceAll("零亿", "亿零");

        // 处理万到亿之间全部为零的情况
        result = result.replaceAll("亿万", "亿");
        result = result.replaceAll("零万", "零");

        // 处理万亿到亿之间全部为零的情况
        result = result.replaceAll("零亿", "亿");

        // 再次处理连续含有多个零的情况
        result = result.replaceAll("[零]+", "零");

        // 再次处理单位
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零亿", "亿");

        // 处理金额整数部分全部为零的情况
        if (StringUtils.startsWith(result, "元")) {
            result = result.replaceAll("元", "");
        }

        return result;
    }

    /**
     * 将金额的小数部分转换为大写
     */
    private static String dotToBig(String num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num.length(); i++) {
            int digit = Integer.valueOf(num.substring(i, i + 1)).intValue();
            sb.append(bigLetter[digit]);
        }
        String face = sb.reverse().toString();
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < face.length(); i++) {
            // 加权
            if (i == 0) {
                resultSb.append(small[0]);
            } else {
                resultSb.append(small[1]);
            }
            // 加面值
            String temp = face.substring(i, i + 1);
            resultSb.append(temp);
        }

        String result = resultSb.reverse().toString();

        result = result.replaceAll("零分", "");
        result = result.replaceAll("零角", "零");
        return result;
    }
}
