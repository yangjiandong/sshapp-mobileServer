package org.ssh.pm.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.enums.QueryConstants;

//import sun.misc.BASE64Encoder;

public class AppUtil {

    private static Log log = LogFactory.getLog(AppUtil.class);

    /**
     * 返回当前日期 yyyy-mm-dd
     *
     * @return String
     */
    public static String getNowDateString(String sign) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + sign + "MM" + sign + "dd");
        String dateString = formatter.format(new Date());
        return dateString.trim();
    }

    /**
     * 返回当前日期时间yyyy年mm月dd日 hh:mm:ss 星期
     * @return
     */
    public static String getNowDateTimeString() {

        //格式为2003年00月00日上午hh:mm:ss,星期一
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy'年'MM'月'dd'日' a hh:mm:ss ','E");
        Date currentTime_1 = new Date();
        String dateString = formatter.format(currentTime_1);

        return dateString.trim();
    }

    /**
     * 返回当前系统采用的日期分隔符
     */
    public static String getDateSplit() {
        return ".";
    }

    /**
     * 返回当前系统采用的日期分隔符组成的当前日期字符串
     */
    public static String getNowDate() {
        return getNowDateString(AppUtil.getDateSplit());
    }

    /**
     * 返回昨天日期
     *
     * @return
     */
    public static String getYesterDay() {
        return addDays(getNowDateString(AppUtil.getDateSplit()), -1);
    }

    /**
     * 在指定的日期上增减天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 为字符型日期增加天数
     */
    public static String addDays(String dateStr, int days) {
        Date date = getStrDateToDate(dateStr);
        date = addDays(date, days);

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + getDateSplit() + "MM" + getDateSplit() + "dd");
        String dateString = formatter.format(date);

        return dateString.trim();
    }

    /**
     * 在指定的日期上增减月数
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 为字符型日期增加月数
     */
    public static String addMonths(String dateStr, int months) {
        Date date = getStrDateToDate(dateStr);
        date = addMonths(date, months);

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + getDateSplit() + "MM" + getDateSplit() + "dd");
        String dateString = formatter.format(date);

        return dateString.trim();
    }

    /**
     * 为字符型日期增加月数，相应减去一天
     */
    public static String addMonths2(String dateStr, int months) {
        Date date = getStrDateToDate(dateStr);
        date = addMonths(date, months);
        date = addDays(date, -1);

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + getDateSplit() + "MM" + getDateSplit() + "dd");
        String dateString = formatter.format(date);

        return dateString.trim();
    }

    /**
     * 在指定的日期上增减年数
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * 为字符型日期增减年数
     */
    public static String addYear(String dateStr, int years) {
        Date date = getStrDateToDate(dateStr);
        date = addYears(date, years);

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + getDateSplit() + "MM" + getDateSplit() + "dd");
        String dateString = formatter.format(date);

        return dateString.trim();
    }

    /**
     * 转换字符日期值到日期符，字符日期值的日期分隔符为jsite-config.xml定义
     */
    public static Date getStrDateToDate(String dateStr) {
        String dateSplit = getDateSplit();
        if (dateSplit.equals("."))
            dateSplit = "\\.";
        StringTokenizer filter = new StringTokenizer(dateStr, dateSplit);
        int year = Integer.parseInt(filter.nextToken());
        int mon = Integer.parseInt(filter.nextToken()) - 1;
        int day = Integer.parseInt(filter.nextToken());

        Calendar cal = new GregorianCalendar(year, mon, day);
        return cal.getTime();
    }

    /**
     * 返回指定日期的日期值
     */
    public static String getDateDay(String date) {
        String day = "";

        String dateSplit = getDateSplit();
        if (dateSplit.equals("."))
            dateSplit = "\\.";

        StringTokenizer filter = new StringTokenizer(date, dateSplit);
        day = filter.nextToken();
        day = filter.nextToken();
        day = filter.nextToken();

        return day;
    }

    /**
     * 返回指定日期的月份值
     */
    public static String getDateMonth(String date) {
        String month = "";

        String dateSplit = getDateSplit();
        if (dateSplit.equals("."))
            dateSplit = "\\.";

        StringTokenizer filter = new StringTokenizer(date, dateSplit);
        month = filter.nextToken();
        month = filter.nextToken();

        return month;
    }

    /**
     * 返回指定日期的年份值
     */
    public static String getDateYear(String date) {
        String year = "";

        String dateSplit = getDateSplit();
        if (dateSplit.equals("."))
            dateSplit = "\\.";

        StringTokenizer filter = new StringTokenizer(date, dateSplit);
        year = filter.nextToken();

        return year;
    }

    /**
     * 从日期型中取得日期字符串
     */
    public static String getDateStringFromDate(Date date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy" + getDateSplit() + "MM" + getDateSplit() + "dd");
        String dateString = formatter.format(date);

        return dateString.trim();
    }

    // 返回两个日期之间天数
    // 有缺陷
    public static long differenceInDays2(Calendar date1, Calendar date2) {
        final long msPerDay = 1000 * 60 * 60 * 24;

        final long date1Milliseconds = date1.getTime().getTime();
        final long date2Milliseconds = date2.getTime().getTime();
        final long result = (date1Milliseconds - date2Milliseconds) / msPerDay;

        return result;
    }

    //解决个别时区少一天问题
    //http://tripoverit.blogspot.com/2007/07/java-calculate-difference-between-two.html
    /** Using Calendar - THE CORRECT (& Faster) WAY**/
    /****Needs testing ...... Anyone?****/
    //assert: startDate must be before endDate
    public static long differenceInDays(Calendar endDate, Calendar startDate) {
        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        long endInstant = endDate.getTimeInMillis();
        int presumedDays = (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY);
        Calendar cursor = (Calendar) startDate.clone();
        cursor.add(Calendar.DAY_OF_YEAR, presumedDays);
        long instant = cursor.getTimeInMillis();
        if (instant == endInstant)
            return presumedDays;
        final int step = instant < endInstant ? 1 : -1;
        do {
            cursor.add(Calendar.DAY_OF_MONTH, step);
            presumedDays += step;
        } while (cursor.getTimeInMillis() != endInstant);
        return presumedDays;
    }

    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 返回两个日期之间的天数（相同天数，返回值为0）
     *
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return 天数
     */
    public static long getDayTotal(String startDate, String endDate) {

        Calendar startCal, endCal;
        int lyear, lmonth, lday;

        lyear = Integer.parseInt(AppUtil.getDateYear(startDate));
        lmonth = Integer.parseInt(AppUtil.getDateMonth(startDate));
        lday = Integer.parseInt(AppUtil.getDateDay(startDate));
        startCal = new GregorianCalendar(lyear, lmonth - 1, lday);

        lyear = Integer.parseInt(AppUtil.getDateYear(endDate));
        lmonth = Integer.parseInt(AppUtil.getDateMonth(endDate));
        lday = Integer.parseInt(AppUtil.getDateDay(endDate));
        endCal = new GregorianCalendar(lyear, lmonth - 1, lday);

        return differenceInDays(endCal, startCal);
    }

    /**
     * 返回当前日期年份的第一天
     *
     * @return
     */
    public static String getThisYearFirstDate() {
        String now = getNowDate();
        String thisYear = getDateYear(now);

        Calendar lcal = new GregorianCalendar(Integer.parseInt(thisYear), 0, 1);
        return getDateStringFromDate(lcal.getTime());
    }

    /**
     * 返回两个年份的差值 year2 - year1
     */
    public static int getYearsBetween(String year1, String year2) throws NumberFormatException {
        return Integer.parseInt(year2) - Integer.parseInt(year1);
    }

    /**
     * 返回两个yyyy.mm值的月份差值 ym2 - ym1
     */
    public static int getYearMonthBetween(String ym1, String ym2) throws NumberFormatException {
        StringTokenizer filter = new StringTokenizer(ym1, "\\.");
        int year1 = Integer.parseInt(filter.nextToken());
        int mon1 = Integer.parseInt(filter.nextToken());
        if (mon1 > 12) {
            // log.error("月格式错");
            throw new NumberFormatException("月格式错");
        }

        StringTokenizer filter2 = new StringTokenizer(ym2, "\\.");
        int year2 = Integer.parseInt(filter2.nextToken());
        int mon2 = Integer.parseInt(filter2.nextToken());
        if (mon2 > 12) {
            // log.error("月格式错");
            throw new NumberFormatException("月格式错");
        }

        int defYear = year2 - year1;
        if (defYear == 0) {
            return mon2 - mon1;
        } else {
            return (defYear - 1) * 12 + (12 - mon1) + mon2;
        }
    }

    /**
     * 从yyyy.mm格式返回一个日期值
     *
     * @param ym1
     * @param type
     * @return
     * @throws NumberFormatException
     */
    public static String getDateFromYYYYMM(String ym1, String type) throws NumberFormatException {
        StringTokenizer filter = new StringTokenizer(ym1, "\\.");
        String year = filter.nextToken();
        String mon = filter.nextToken();
        if (mon.compareTo("12") > 0) {
            // log.error("月格式错");
            throw new NumberFormatException("月格式错");
        }

        if (type.equalsIgnoreCase("first")) {
            return year + "." + mon + "." + "01";
        } else if (type.equalsIgnoreCase("end")) {
            String next = addMonths(year + "." + mon + "." + "01", 1);
            return addDays(next, -1);
        } else {
            return year + "." + mon + "." + "01";
        }

    }

    /**
     * 将浮点数转换成货币格式的字符串(#,##0.00)
     *
     * @param money
     *            需转换的浮点数 (double)
     * @return 货币格式字符串(#,##0.00)
     */
    public static String getMoneyFormat(double money) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(money);
    }

    public static String getMoneyFormatForStr(String money) {
        String s = " ";
        try {
            if (!StringUtils.isBlank(money.trim())) {
                double m = new Double(money);

                if (m != 0) {
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    s = df.format(m);
                }
            }
        } catch (Exception e) {
            log.error("money:[" + money + "]");
            log.error("", e);
        }
        return s;
    }

    /**
     * 创建手动指派的ID值，如生成数据库中的ID主健值。
     *
     * @param maxId
     *            所有连续ID值中最大的ID， 程序将根据maxId生成比该maxId值大1的连续ID值。
     *            如：有GB000001、GB000002、GB000003这三个连续的ID值， 那么最大的ID值就是GB000003，这时程序生成的连续ID值就是GB000004。
     * @param headId
     *            头ID标识。如：规定要生成的ID值是GB000001这种形式的值， 那么GB就是头ID标识。
     * @return 生成比maxId值大1的连续ID值。
     */
    public static String buildAssignedId(String maxId, String headId) {
        // 手动指派的ID值
        String buildId = null;
        // 补零
        StringBuffer fillZero = null;
        // 数字位的位数
        int numberBit = maxId.replaceFirst(headId, "").length();
        // 获得当前数字
        int number = Integer.parseInt(maxId.replaceFirst(headId, ""));
        // 获得下一位数字的位数
        int nextNumBit = String.valueOf(number + 1).length();
        // 产生下一位数字
        number += 1;
        // 创建手动指派的ID值
        if (numberBit - nextNumBit > 0) {
            fillZero = new StringBuffer();
            // 补零
            for (int i = 0; i < numberBit - nextNumBit; i++) {
                fillZero.append(0);
            }
            buildId = headId + fillZero.toString() + number;
        } else {
            buildId = headId + number;
        }

        return buildId;
    }

    /**
     * 批量创建手动指派的ID值，如生成数据库中的ID主健值。
     *
     * @param maxId
     *            所有连续ID值中最大的ID， 程序将根据该maxId生成比该maxId值大1的连续ID值，
     *            如：有GB000001、GB000002、GB000003这三个连续ID值， 那么最大的ID值就是GB000003，这时程序生成的连续ID值就是GB000004。
     * @param headId
     *            头ID标识，如：规定要生成的ID值是GB000001这种形式的值， 那么GB就是头ID标识。
     * @param idNum
     *            要批量生成的连续ID的数量。如要批量生成5个连续的ID，则该数量应该是 5
     * @return 生成比 maxId大1的连续ID值的List列表
     */
    public static List<String> buildAssignedIds(String maxId, String headId, int idNum) {
        // 已创建的ID列表
        List<String> idList = new ArrayList<String>();
        // 手动指派的ID值
        String buildId = null;
        // 补零
        StringBuffer fillZero = null;
        // 数字位的位数
        int numberBit = maxId.replaceFirst(headId, "").length();
        // 获得当前数字
        int number = Integer.parseInt(maxId.replaceFirst(headId, ""));
        // 获得下一位数字的位数
        int nextNumBit = String.valueOf(number + 1).length();
        // 产生下一位数字
        for (int i = 0; i < idNum; i++) {
            number += 1;

            if (numberBit - nextNumBit > 0) {
                if (fillZero == null)
                    fillZero = new StringBuffer();
                // 补零
                for (int j = 0; j < numberBit - nextNumBit; j++) {
                    fillZero.append(0);
                }
                buildId = headId + fillZero.toString() + number;
                // 获得下一位数字的位数
                nextNumBit = String.valueOf(number + 1).length();
                // 重置fillZero
                fillZero.delete(0, fillZero.length());

                idList.add(buildId);
            } else {
                buildId = headId + number;
                // 获得下一位数字的位数
                nextNumBit = String.valueOf(number + 1).length();

                idList.add(buildId);
            }
        }

        return idList;
    }

    /**
     * http://www.jguru.com/forums/view.jsp?EID=489372
     *
     * Calculates the number of days between two calendar days in a manner which is independent of
     * the Calendar type used.
     *
     * @param d1
     *            The first date.
     * @param d2
     *            The second date.
     *
     * @return The number of days between the two dates. Zero is returned if the dates are the same,
     *         one if the dates are adjacent, etc. The order of the dates does not matter, the value
     *         returned is always >= 0. If Calendar types of d1 and d2 are different, the result may
     *         not be accurate.
     */
    public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
        int y2 = d2.get(java.util.Calendar.YEAR);
        if (d1.get(java.util.Calendar.YEAR) != y2) {
            d1 = (java.util.Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
                d1.add(java.util.Calendar.YEAR, 1);
            } while (d1.get(java.util.Calendar.YEAR) != y2);
        }
        return days;
    } // getDaysBetween()

    /**
     * 判断字符串是否为只含有英文字母的字符串
     */
    public static boolean isEnglish(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isEng = pattern.matcher(str);
        if (!isEng.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为数字字符串(整数、小数或负数)
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^\\-?[0-9]*\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 设置缓存
     */
    public static void setCache(String cacheName, String key, Object value) {

        CacheManager manager = (CacheManager) SpringContextHolder.getBean("ehcacheManager");
        if (manager.getCache(cacheName) == null) {
            manager.addCache(cacheName);
        }

        Cache cache = manager.getCache(cacheName);

        cache.put(new Element(key, value));
        // manager.shutdown();

    }

    /**
     * 获取缓存的值
     */
    public static Object getCache(String cacheName, String key) {

        CacheManager manager = (CacheManager) SpringContextHolder.getBean("ehcacheManager");

        Cache cache = manager.getCache(cacheName);
        if (cache == null)
            return null;

        Element element = cache.get(key);
        // manager.shutdown();

        if (element == null)
            return null;
        else
            return element.getObjectValue();

    }

    /**
     * 清除缓存
     */
    public static void removeCache(String cacheName, String key) {
        CacheManager manager = (CacheManager) SpringContextHolder.getBean("ehcacheManager");

        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.remove(key);
        }
    }

    /**
     * 清除指定模块的所有缓存
     */
    public static void removeAllCache(String cacheName) {
        CacheManager manager = (CacheManager) SpringContextHolder.getBean("ehcacheManager");

        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
        }
    }

    // 深度拷贝，解决类（需实现Serializable ）复制问题
    public static Object depthClone(Object srcObj) {
        Object cloneObj = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(srcObj);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(in);
            cloneObj = oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    private static String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

    private static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
            "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };

    /**
     * 将传入的身份证号码进行校验，并返回一个对应的18位身份证
     *
     * @param personIDCode
     *            身份证号码
     * @return String 十八位身份证号码
     * @throws 无效的身份证号
     */
    public static String getFixedPersonIDCode(String personIDCode) throws Exception {
        if (personIDCode == null)
            throw new Exception("输入的身份证号无效，请检查");

        if (personIDCode.length() == 18) {
            if (isIdentity(personIDCode))
                return personIDCode;
            else
                throw new Exception("输入的身份证号无效，请检查");
        } else if (personIDCode.length() == 15)
            return fixPersonIDCodeWithCheck(personIDCode);
        else
            throw new Exception("输入的身份证号无效，请检查");
    }

    /**
     * 修补15位居民身份证号码为18位，并校验15位身份证有效性
     *
     * @param personIDCode
     *            十五位身份证号码
     * @return String 十八位身份证号码
     * @throws 无效的身份证号
     */
    public static String fixPersonIDCodeWithCheck(String personIDCode) throws Exception {
        if (personIDCode == null || personIDCode.trim().length() != 15)
            throw new Exception("输入的身份证号不足15位，请检查");

        if (!isIdentity(personIDCode))
            throw new Exception("输入的身份证号无效，请检查");

        return fixPersonIDCodeWithoutCheck(personIDCode);
    }

    /**
     * 修补15位居民身份证号码为18位，不校验身份证有效性
     *
     * @param personIDCode
     *            十五位身份证号码
     * @return 十八位身份证号码
     * @throws 身份证号参数不是15位
     */
    public static String fixPersonIDCodeWithoutCheck(String personIDCode) throws Exception {
        if (personIDCode == null || personIDCode.trim().length() != 15)
            throw new Exception("输入的身份证号不足15位，请检查");

        String id17 = personIDCode.substring(0, 6) + "19" + personIDCode.substring(6, 15); // 15位身份证补'19'

        char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 11个校验码字符
        int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 }; // 18个加权因子
        int[] idcd = new int[18];
        int sum; // 根据公式 ∑(ai×Wi) 计算
        int remainder; // 第18位校验码
        for (int i = 0; i < 17; i++) {
            idcd[i] = Integer.parseInt(id17.substring(i, i + 1));
        }
        sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + idcd[i] * factor[i];
        }
        remainder = sum % 11;
        String lastCheckBit = String.valueOf(code[remainder]);
        return id17 + lastCheckBit;
    }

    /**
     * 判断是否是有效的18位或15位居民身份证号码
     *
     * @param identity
     *            18位或15位居民身份证号码
     * @return 是否为有效的身份证号码
     */
    public static boolean isIdentity(String identity) {
        if (identity == null)
            return false;
        if (identity.length() == 18 || identity.length() == 15) {
            String id15 = null;
            if (identity.length() == 18)
                id15 = identity.substring(0, 6) + identity.substring(8, 17);
            else
                id15 = identity;
            try {
                Long.parseLong(id15); // 校验是否为数字字符串

                String birthday = "19" + id15.substring(6, 12);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                sdf.parse(birthday); // 校验出生日期
                if (identity.length() == 18 && !fixPersonIDCodeWithoutCheck(id15).equals(identity))
                    return false; // 校验18位身份证
            } catch (Exception e) {
                return false;
            }
            return true;
        } else
            return false;
    }

    /**
     * 从身份证号中获取出生日期，身份证号可以为15位或18位
     *
     * @param identity
     *            身份证号
     * @return 出生日期
     * @throws 身份证号出生日期段有误
     */
    public static Timestamp getBirthdayFromPersonIDCode(String identity) throws Exception {
        String id = getFixedPersonIDCode(identity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Timestamp birthday = new Timestamp(sdf.parse(id.substring(6, 14)).getTime());
            return birthday;
        } catch (ParseException e) {
            throw new Exception("不是有效的身份证号，请检查");
        }
    }

    /**
     * 从身份证号获取性别
     *
     * @param identity
     *            身份证号
     * @return 性别代码
     * @throws Exception
     *             无效的身份证号码
     */
    public static String getGenderFromPersonIDCode(String identity) throws Exception {
        String id = getFixedPersonIDCode(identity);
        char sex = id.charAt(16);
        return sex % 2 == 0 ? "2" : "1";
    }

    /**
     * 将货币转换为大写形式(类内部调用)
     *
     * @param val
     * @return String
     */
    private static String PositiveIntegerToHanStr(String NumStr) {
        // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
        String RMBStr = "";
        boolean lastzero = false;
        boolean hasvalue = false; // 亿、万进位前有数值标记
        int len, n;
        len = NumStr.length();
        if (len > 15)
            return "数值过大!";
        for (int i = len - 1; i >= 0; i--) {
            if (NumStr.charAt(len - i - 1) == ' ')
                continue;
            n = NumStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9)
                return "输入含非数字字符!";

            if (n != 0) {
                if (lastzero)
                    RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
                // 除了亿万前的零不带到后面
                // if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )
                // 如十进位前有零也不发壹音用此行
                if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
                    RMBStr += HanDigiStr[n];
                RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
                hasvalue = true; // 置万进位前有值标记

            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
                    RMBStr += HanDiviStr[i]; // “亿”或“万”
            }
            if (i % 8 == 0)
                hasvalue = false; // 万进位前有值标记逢亿复位
            lastzero = (n == 0) && (i % 4 != 0);
        }

        if (RMBStr.length() == 0)
            return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        return RMBStr;
    }

    /**
     * 将货币转换为大写形式
     *
     * @param val
     *            传入的数据
     * @return String 返回的人民币大写形式字符串
     */
    public static String numToRMBStr(double val) {
        String SignStr = "";
        String TailStr = "";
        long fraction, integer;
        int jiao, fen;

        if (val < 0) {
            val = -val;
            SignStr = "负";
        }
        if (val > 99999999999999.999 || val < -99999999999999.999)
            return "数值位数过大!";
        // 四舍五入到分
        long temp = Math.round(val * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;
        if (jiao == 0 && fen == 0) {
            TailStr = "整";
        } else {
            TailStr = HanDigiStr[jiao];
            if (jiao != 0)
                TailStr += "角";
            // 零元后不写零几分
            if (integer == 0 && jiao == 0)
                TailStr = "";
            if (fen != 0)
                TailStr += HanDigiStr[fen] + "分";
        }
        // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
        // if( !integer ) return SignStr+TailStr;
        return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "元" + TailStr;
    }

    /**
     * 获取指定年份和月份对应的天数
     *
     * @param year
     *            指定的年份
     * @param month
     *            指定的月份
     * @return int 返回天数
     */
    public static int getDaysInMonth(int year, int month) {
        if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
                || (month == 12)) {
            return 31;
        } else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            return 30;
        } else {
            if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    /**
     * 根据所给的起止时间来计算间隔的天数
     *
     * @param startDate
     *            起始时间
     * @param endDate
     *            结束时间
     * @return int 返回间隔天数
     */
    public static int getIntervalDays(java.sql.Date startDate, java.sql.Date endDate) {
        long startdate = startDate.getTime();
        long enddate = endDate.getTime();
        long interval = enddate - startdate;
        int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
        return intervalday;
    }

    /**
     * 根据所给的起止时间来计算间隔的月数
     *
     * @param startDate
     *            起始时间
     * @param endDate
     *            结束时间
     * @return int 返回间隔月数
     */
    public static int getIntervalMonths(java.sql.Date startDate, java.sql.Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startDateM = startCal.MONTH;
        int startDateY = startCal.YEAR;
        int enddatem = endCal.MONTH;
        int enddatey = endCal.YEAR;
        int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
        return interval;
    }

    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj
     *            待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj == "")
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 合并字符串数组
     *
     * @param a
     *            字符串数组0
     * @param b
     *            字符串数组1
     * @return 返回合并后的字符串数组
     */
    public static String[] mergeStringArray(String[] a, String[] b) {
        if (a.length == 0 || isEmpty(a))
            return b;
        if (b.length == 0 || isEmpty(b))
            return a;
        String[] c = new String[a.length + b.length];
        for (int m = 0; m < a.length; m++) {
            c[m] = a[m];
        }
        for (int i = 0; i < b.length; i++) {
            c[a.length + i] = b[i];
        }
        return c;
    }

    /**
     * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
     */
    public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) {
        String agent = request.getHeader("USER-AGENT");
        try {
            if (null != agent && -1 != agent.indexOf("MSIE")) {
                pFileName = URLEncoder.encode(pFileName, "utf-8");
            } else {
                pFileName = new String(pFileName.getBytes("utf-8"), "iso8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return pFileName;
    }

    /**
     * 根据日期获取星期
     *
     * @param strdate
     * @return
     */
    public static String getWeekDayByDate(String strdate) {
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    /**
     * 判断是否是IE浏览器
     *
     * @param userAgent
     * @return
     */
    public static boolean isIE(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        boolean isIe = true;
        int index = userAgent.indexOf("msie");
        if (index == -1) {
            isIe = false;
        }
        return isIe;
    }

    /**
     * 判断是否是Chrome浏览器
     *
     * @param userAgent
     * @return
     */
    public static boolean isChrome(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        boolean isChrome = true;
        int index = userAgent.indexOf("chrome");
        if (index == -1) {
            isChrome = false;
        }
        return isChrome;
    }

    /**
     * 判断是否是Firefox浏览器
     *
     * @param userAgent
     * @return
     */
    public static boolean isFirefox(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        boolean isFirefox = true;
        int index = userAgent.indexOf("firefox");
        if (index == -1) {
            isFirefox = false;
        }
        return isFirefox;
    }

    /**
     * 获取客户端类型
     *
     * @param userAgent
     * @return
     */
    public static String getClientExplorerType(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        String explorer = "非主流浏览器";
        if (isIE(request)) {
            int index = userAgent.indexOf("msie");
            explorer = userAgent.substring(index, index + 8);
        } else if (isChrome(request)) {
            int index = userAgent.indexOf("chrome");
            explorer = userAgent.substring(index, index + 12);
        } else if (isFirefox(request)) {
            int index = userAgent.indexOf("firefox");
            explorer = userAgent.substring(index, index + 11);
        }
        return explorer.toUpperCase();
    }

    /**
     * 基于MD5算法的单向加密
     *
     * @param strSrc
     *            明文
     * @return 返回密文
     */
    public static String encryptBasedMd5(String strSrc) {
        String outString = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] outByte = md5.digest(strSrc.getBytes("UTF-8"));
            outString = outByte.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outString;
    }

    /**
     * DES算法密钥
     */
    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };

    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    public static String encryptBasedDes(String data) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            // import sun.misc.BASE64Encoder;
            // encryptedData = new BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
            encryptedData = new Base64().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    public static String decryptBasedDes(String cryptData) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new Base64().decode(cryptData)));
            //decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
            log.error("解密错误，错误信息：", e);
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

    //
    public static void main(String[] args) {
        String QJstr = "hello";
        String QJstr1 = "ｈｅｈｅ";

        String result = BQchange(QJstr);
        String result1 = QBchange(QJstr1);

        System.out.println(QJstr + "\n" + result);
        System.out.println(QJstr1 + "\n" + result1);
    }

    // 半角 转全角
    public static final String BQchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;

        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] != -1) {
                b[2] = (byte) (b[2] - 32);
                b[3] = -1;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                outStr = outStr + Tstr;
        }
        return outStr;
    }

    // 全角 转半角
    public static final String QBchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;

        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                outStr = outStr + Tstr;
        }
        return outStr;
    }

    /**
     * 返回可以移植的比较 Year+Month SQL 查询条件
     */
    public static String getYMCond(String driver, String year, String month) {
        if (DBdriverUtil.isDB2(driver)) {
            return " CONCAT(Year,Month)<='" + year + month + "' ";
        } else if (DBdriverUtil.isOracle(driver)) {
            return "Year||Month<='" + year + month + "' ";
        } else {
            return " Year+Month<='" + year + month + "' ";
        }
    }

    /**
     * 返回可以移植的比较 StartYear+StartMonth SQL 查询条件
     */
    public static String getYMCondSQL(String driver, String year, String month) {
        if (DBdriverUtil.isDB2(driver)) {
            return "CONCAT(StartYear,StartMonth)<='" + year + month + "' ";
        } else if (DBdriverUtil.isOracle(driver)) {
            return "StartYear || StartMonth<='" + year + month + "' ";
        } else {
            return "StartYear + StartMonth<='" + year + month + "' ";
        }
    }

    /**
     * 返回正确的取子串SQL函数写法
     */
    public static String subStringSQL(String driver, String fldName, int start, int len) {
        if (DBdriverUtil.isDB2(driver)) {
            return " SUBSTR(" + fldName + "," + start + "," + len + ") ";
        } else if (DBdriverUtil.isOracle(driver)) {
            return " SUBSTR(" + fldName + "," + start + "," + len + ") ";
        } else {
            return " SUBSTRING(" + fldName + "," + start + "," + len + ") ";
        }
    }

    //不定长拼接sql，字符串以_s结束
    public static String conCatFld_FldSQL_s(String driver, String... alls) {
        String one;
        StringBuilder sb = new StringBuilder(alls.length * 16);
        for (int i = 0; i < alls.length; i++) {
            one = alls[i];

            //字符串
            if (one.endsWith("_s")) {
                one = one.substring(0, one.length() - 2);

                if (sb.length() > 0) {
                    if (DBdriverUtil.isOracle(driver)) {
                        sb.append(" || '" + one + "' ");
                    } else {
                        sb.append(" + '" + one + "' ");
                    }

                } else {
                    sb.append(" '" + one + "' ");
                }

            } else {

                if (sb.length() > 0) {
                    if (DBdriverUtil.isOracle(driver)) {
                        sb.append(" || nvl(" + one + ",'空值') ");
                    } else {
                        sb.append(" + isnull(" + one + ",'') ");
                    }

                } else {
                    if (DBdriverUtil.isOracle(driver)) {
                        sb.append(" nvl(" + one + ",'空值') ");
                    } else {
                        sb.append(" isnull(" + one + ",'') ");
                    }
                    //sb.append(" " + one + " ");
                }
            }
        }

        return sb.toString();
    }

    /**
     * 计算药品购入成本
     *
     * @param r1
     *            药品费比例定额
     * @param r2
     *            定额外差价率
     * @param r3
     *            定额内差价率
     * @param income
     *            收入金额
     * @param drugIncome
     *            其中药品收入金额
     *
     * 算法说明：
     *
     * 如果 (drugIncome/income)*100 > r1 （超过了定额），则:
     *
     * 承担的成本 = (药品收入 - 总收入 * r1 ) * (1 - r2) + 总收入 * r1 * (1 - r3) [超出比例定额部分]
     * [定额内部分]
     *
     * 否则：
     *
     * 承担的成本 = 药品收入 x (1 - r3)
     *
     * @return 购入成本金额
     */
    public static double getBuyCost(double r1, double r2, double r3, double income, double drugIncome) {
        if ((drugIncome / income) * 100 > r1) {
            return (drugIncome - income * r1 / 100) * (1 - r2 / 100) + income * r1 / 100 * (1 - r3 / 100);
        } else {
            return drugIncome * (1 - r3 / 100);
        }
    }

    /**
     * 转换a|b到 [NOT] IN ('a','b')
     */
    public static String getSQLINClauseOfString(String aSQLOp, String string, String fldName) {
        StringBuffer retStr = new StringBuffer("");
        StringTokenizer filter = new StringTokenizer(string, "|");
        String oneStr;
        boolean first = true;
        while (filter.hasMoreTokens()) {
            if (!first)
                retStr.append(",");
            if (first)
                first = false;
            oneStr = filter.nextToken();
            retStr.append("'" + oneStr + "'");
        }

        return fldName + " " + aSQLOp + " (" + retStr.toString() + ")";
    }

    /**
     * 转换1|2到 [NOT] IN (1,2)
     */
    public static String getSQLINClauseOfInt(String aSQLOp, String string, String fldName) {
        StringBuffer retStr = new StringBuffer("");
        StringTokenizer filter = new StringTokenizer(string, "|");
        String oneStr;
        boolean first = true;
        while (filter.hasMoreTokens()) {
            if (!first)
                retStr.append(",");
            if (first)
                first = false;
            oneStr = filter.nextToken();
            retStr.append(oneStr);
        }

        return fldName + " " + aSQLOp + " (" + retStr.toString() + ")";
    }

    /**
     * 转换a|b到(LIKE 'a%' OR LIKE 'b%')
     */
    public static String getSQLLIKEClause(String string, String fldName) {
        StringBuffer retStr = new StringBuffer("");
        StringTokenizer filter = new StringTokenizer(string, "|");
        String oneStr;
        boolean first = true;
        while (filter.hasMoreTokens()) {
            if (!first)
                retStr.append(" OR ");
            if (first)
                first = false;
            oneStr = filter.nextToken();
            retStr.append(fldName + " LIKE '" + oneStr + "%'");
        }

        return "(" + retStr.toString() + ")";
    }

    /**
     * 转换a|b到(NOT LIKE 'a%' AND NOT LIKE 'b%')
     */
    public static String getSQLNOTLIKEClause(String string, String fldName) {
        StringBuffer retStr = new StringBuffer("");
        StringTokenizer filter = new StringTokenizer(string, "|");
        String oneStr;
        boolean first = true;
        while (filter.hasMoreTokens()) {
            if (!first)
                retStr.append(" AND ");
            if (first)
                first = false;
            oneStr = filter.nextToken();
            retStr.append(fldName + " NOT LIKE '" + oneStr + "%'");
        }

        return "(" + retStr.toString() + ")";
    }

    /**
     * 返回按项目分类编码查询用WHERE条件
     *
     * 注意：catCode 的格式可以是单个分类编码，也可以是用“|”分隔的多个分类编码
     */
    public static String getSQLWhereClauseByCatCode(String catType, String catCode, String aSQLOp) {

        if (catCode.equals(""))
            return "";

        String fldName = "a.CostCatCode";
        if (catType.equals(CoreConstants.INCOME))
            fldName = "a.IncomeCatCode";

        if (aSQLOp.equals(QueryConstants.IN) || aSQLOp.equals(QueryConstants.NOTIN)) {
            return "AND " + AppUtil.getSQLINClauseOfString(aSQLOp, catCode, fldName);
        }

        if (aSQLOp.equals(QueryConstants.LIKE))
            return "AND " + AppUtil.getSQLLIKEClause(catCode, fldName);

        if (aSQLOp.equals(QueryConstants.NOTLIKE))
            return "AND " + AppUtil.getSQLNOTLIKEClause(catCode, fldName);

        return "";

    }

    public static String getColorFromMoreColors(int i) {
        String color = "000000";//hex black

        try {
            String[] a = CoreConstants.MORECOLORS.clone();
            int len = a.length;
            if (i > len - 1) {
                color = Integer.toHexString((int) (Math.random() * 255 * 255 * 255));
            } else if (i < 0) {
                color = Integer.toHexString((int) (Math.random() * 255 * 255 * 255));
            } else {
                color = a[i];
            }
        } catch (Exception e) {
            log.error("getColorFromMoreColors:", e);
        }
        return color;
    }

    public static Map<String, Object> buildJSONDataResponse(List<? extends Object> data, Long total) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("totalCount", total);
        map.put("rows", data);
        return map;
    }
}
