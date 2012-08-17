package org.ssh.tool.jakartacommons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.google.common.base.Splitter;

//commons lang StringUtils
public class StringUtilsTest {

    @Test
    public void testBlank() {
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("\n\n\t"));
        assertTrue(StringUtils.isBlank(null));
        assertTrue(!StringUtils.isBlank("Test"));

        assertEquals(null, StringUtils.trimToNull(null));
        assertEquals(null, StringUtils.trimToNull(""));
        assertEquals(null, StringUtils.trimToNull("\t"));

        assertTrue(!StringUtils.isNotBlank(null));
    }

    //字符串缩写
    @Test
    public void testAbbreviate() {

        assertEquals("test...", StringUtils.abbreviate("testsccome", 7));
        assertEquals("中午吃饭...", StringUtils.abbreviate("中午吃饭返丑么才额是哦", 7));

    }

    @Test
    public void testSplit() {
        String input = "Franticall oblong";
        // "," " ," 有区别
        //String[] array1 = StringUtils.split(input,",", 2);
        String[] array1 = StringUtils.split(input, " ,", 2);
        assertEquals("Franticall", array1[0]);
        assertEquals("oblong", array1[1]);

        System.out.println(ArrayUtils.toString(array1));

        //去除空字符串
        String str3 = "aaa,,bbb";
        String[] dim3 = StringUtils.split(str3, ","); // => ["aaa", "bbb"]

        assertEquals(2, dim3.length);//2
        assertEquals("aaa", dim3[0]);//"aaa"
        assertEquals("bbb", dim3[1]);//"bbb"

        //包含空字符串
        String str4 = "aaa,,bbb";
        String[] dim4 = StringUtils.splitPreserveAllTokens(str4, ","); // => ["aaa", "", "bbb"]

        assertEquals(3, dim4.length);//3
        assertEquals("aaa", dim4[0]);//"aaa"
        assertEquals("", dim4[1]);//""
        assertEquals("bbb", dim4[2]);//"bbb"

        //指定分割的最大次数（超过后不分割）
        String str5 = "aaa,bbb,ccc";
        String[] dim5 = StringUtils.split(str5, ",", 2); // => ["aaa", "bbb,ccc"]

        assertEquals(2, dim5.length);//2
        assertEquals("aaa", dim5[0]);//"aaa"
        assertEquals("bbb,ccc", dim5[1]);//"bbb,ccc"

    }

    @Test
    public void testNestedStrings() {
        String htmlContent = "<html>\n" +

        "  <head>\n" +

        "    <title>Test Page</title>\n" +

        "  </head>\n" +

        "  <body>\n" +

        "    <p>This is a TEST!</p>\n" +

        "  </body>\n" +

        "</html>";

        //Extract the title from this XHTML content

        String title = StringUtils.substringBetween(htmlContent, "<title>",

        "</title>");

        assertEquals("Test Page", title);

        //
        //        StringUtils.substringBetween( )
        //        Captures content between two strings
        //
        //        StringUtils.substringAfter( )
        //        Captures content that occurs after the specified string
        //
        //        StringUtils.substringBefore( )
        //        Captures content that occurs before a specified string
        //
        //        StringUtils.substringBeforeLast( )
        //        Captures content after the last occurrence of a specified string
        //
        //        StringUtils.substringAfterLast( )
        //        Captures content before the last occurrence of a specified string

    }

    //剔除指定字符
    @Test
    public void testStrip() {
        String original = "-------***---SHAZAM!---***-------";
        String stripped = StringUtils.strip(original, "-*");

        assertEquals("SHAZAM!", stripped);

        String utilTest = "     This is a quite long string     ";
        assertEquals("This is a quite long string", StringUtils.strip(utilTest));
    }

    //制定加强头信息
    @Test
    public void emphasizedHeader() {
        String title = "Test";
        int width = 30;
        // Construct heading using StringUtils: repeat( ), center( ), and join( )

        String stars = StringUtils.repeat("*", width);
        String centered = StringUtils.center(title, width, "*");
        String heading = StringUtils.join(new Object[] { stars, centered, stars }, "\n");

        System.out.println(heading);

    }

    //换行
    @Test
    public void testWrapp() {
        String message = "One Two Three Four Five ccccccccccccccccccccccccccccccccccccccccccc";
        // Wrap the text.
        String wrappedString = WordUtils.wrap(message, 20, "\n", false);
        System.out.println("Wrapped Message:\n\n" + wrappedString);
    }

    @Test
    public void testReplaceString() {
        String name1 = "Tim O'Reilly";
        String name2 = "Mr. Mason-Dixon!";

        String punctuation = ".-'";
        String name1Temp = StringUtils.replaceChars(name1, punctuation, "");
        String name2Temp = StringUtils.replaceChars(name2, punctuation, "");

        assertEquals("Tim OReilly", name1Temp);
        assertEquals("Mr MasonDixon!", name2Temp);
    }

    @Test
    public void testReplaceString2() {
        String name1 = "Tim {O'Reilly}";
        String name2 = "Mr. {Mason-Dixon!}";
        String name3 = "select * from t_view where busdate>={d1} and busdate<={d2}";

        String punctuation = "{}";
        String name1Temp = StringUtils.replaceChars(name1, punctuation, "");
        String name2Temp = StringUtils.replaceChars(name2, punctuation, "");

        punctuation = "{d1}";
        String name3Temp = StringUtils.replace(name3, punctuation, "'2010.01.01'");
        punctuation = "{d2}";
        name3Temp = StringUtils.replace(name3Temp, punctuation, "'2010.01.31'");
        //String name3Temp = name3.replaceAll("(left brace)d1(right brace)}", "'2010.01.01'").replaceAll("(left brace)d2(right brace)}", "'2010.01.31'");
        //String name3Temp = name3.replaceAll("{d1}", "'2010.01.01'").replaceAll("{d2}", "'2010.01.31'");

        assertEquals("Tim O'Reilly", name1Temp);
        assertEquals("Mr. Mason-Dixon!", name2Temp);
        assertEquals("select * from t_view where busdate>='2010.01.01' and busdate<='2010.01.31'", name3Temp);
    }

    //Commons Codec
    //The Commons Codec library is a small library, which includes encoders and decoders for common encoding algorithms, such as Hex, Base64; and phonetic encoders, such as Metaphone, DoubleMetaphone, and Soundex. This tiny component was created to provide a definitive implementation of Base64 and Hex, encouraging reuse and reducing the amount of code duplication between various Apache projects.

    @Test
    public void testDefaultString2() {
        assertEquals("Tim O'Reilly", StringUtils.defaultString("Tim O'Reilly"));

        assertEquals("", StringUtils.defaultString(""));
        assertEquals("", StringUtils.defaultString(null));

        assertEquals("NULL", StringUtils.defaultIfEmpty(null, "NULL"));
    }

    @Test
    public void testGuavaString2() {

        String testString = "WB,QY,1,N,04, , ,1,Y,,";
        Iterable<String> s = Splitter.on(",").split(testString);
        //Iterable<String> s = Splitter.on(",").omitEmptyStrings().trimResults().split(testString);
        for (String string : s) {
            System.out.println("[" + string + "]");
        }
    }

    @Test
    public void testEnd() {
        assertTrue(StringUtils.endsWith(null, null));
        assertTrue(!StringUtils.endsWith(null, "def"));
        assertTrue(!StringUtils.endsWith("abcdef", null));
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertTrue(!StringUtils.endsWith("ABCDEF", "def"));
        assertTrue(!StringUtils.endsWith("ABCDEF", "cde"));

        assertTrue(StringUtils.endsWithIgnoreCase(null, null));
        assertTrue(!StringUtils.endsWithIgnoreCase(null, "def"));
        assertTrue(!StringUtils.endsWithIgnoreCase("abcdef", null));
        assertTrue(StringUtils.endsWithIgnoreCase("abcdef", "def"));
        assertTrue(StringUtils.endsWithIgnoreCase("ABCDEF", "def"));
        assertTrue(!StringUtils.endsWithIgnoreCase("ABCDEF", "cde"));

        assertTrue(!StringUtils.endsWithAny(null, null));
        assertTrue(!StringUtils.endsWithAny(null, new String[] { "abc" }));
        assertTrue(!StringUtils.endsWithAny("abcxyz", null));
        assertTrue(StringUtils.endsWithAny("abcxyz", new String[] { "" }));
        assertTrue(StringUtils.endsWithAny("abcxyz", new String[] { "xyz" }));
        assertTrue(StringUtils.endsWithAny("abcxyz", new String[] { null, "xyz", "abc" }));

        //startsWith 类似
        assertTrue(StringUtils.startsWith(null, null));
    }

    //计算子串出现的次数
    //http://codingstandards.iteye.com/blog/1182146
    @Test
    public void testMatch() {
        //assertEquals(0, StringUtils.countMatches(null, *));
        assertEquals(0, StringUtils.countMatches(null, "a"));
        assertEquals(0, StringUtils.countMatches("", "a"));
        assertEquals(0, StringUtils.countMatches("abba", null));
        assertEquals(0, StringUtils.countMatches("abba", ""));
        assertEquals(2, StringUtils.countMatches("abba", "a"));
        assertEquals(1, StringUtils.countMatches("abba", "ab"));
        assertEquals(0, StringUtils.countMatches("abba", "xxx"));
    }

    //判断是否包含另外的字符串
    @Test
    public void testContains() {
        assertTrue(!StringUtils.contains(null, 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));

        assertTrue(!StringUtils.contains(null, "a"));
        assertTrue(StringUtils.contains("abc", "ab"));
    }

    //join
    //    static String join(Collection collection, char separator)
    //    Joins the elements of the provided Collection into a single String containing the provided elements.
    //    static String join(Collection collection, String separator)
    //    Joins the elements of the provided Collection into a single String containing the provided elements.
    //    static String join(Iterator iterator, char separator)
    //    Joins the elements of the provided Iterator into a single String containing the provided elements.
    //    static String join(Iterator iterator, String separator)
    //    Joins the elements of the provided Iterator into a single String containing the provided elements.
    //    static String join(Object[] array)
    //    Joins the elements of the provided array into a single String containing the provided list of elements.
    //    static String join(Object[] array, char separator)
    //    Joins the elements of the provided array into a single String containing the provided list of elements.
    //    static String join(Object[] array, char separator, int startIndex, int endIndex)
    //    Joins the elements of the provided array into a single String containing the provided list of elements.
    //    static String join(Object[] array, String separator)
    //    Joins the elements of the provided array into a single String containing the provided list of elements.
    //    static String join(Object[] array, String separator, int startIndex, int endIndex)
    //    Joins the elements of the provided array into a single String containing the provided list of elements.

    @Test
    public void testStringJoin() {
        assertEquals("a,b,c", StringUtils.join(new String[] { "a", "b", "c" }, ","));
    }

    @Test
    public void testStringAppend() {
        StringBuilder sb = new StringBuilder();

        sb.append("some string").append('c').append("sss");

        assertEquals("some stringcsss", sb.toString());
        sb.setLength(0);
        assertEquals("", sb.toString());
    }

    //chomp 清除回车符(\r)和换行符(\n)

    @Test
    public void testLeftPad() {
        String utilTest = "This is a quite long string";
        //System.out.println(StringUtils.leftPad(utilTest,30));
        //System.out.println(utilTest.length());//27
        assertEquals("   This is a quite long string", StringUtils.leftPad(utilTest, 30));
    }

    @Test
    public void testOverlay() {
        String utilTest = "This is a quite long string";
        //overlay(String str,String overlay,int start,int end)
        System.out.println(StringUtils.overlay(utilTest, "This is  quite long string", 0, utilTest.length()));
    }

    @Test
    public void testRemove() {
        String utilTest = "This is a quite long filename.fileExtention";
        //System.out.println(StringUtils.remove(utilTest,".fileExtention"));
        assertEquals("This is a quite long filename", StringUtils.remove(utilTest, ".fileExtention"));
    }

    @Test
    public void testSplitByCharacterType() {
        String utilTest = "this is a quite long string, ,";
        String[] splitted = StringUtils.splitByCharacterType(utilTest);
        for (int i = 0; i < splitted.length; i++) {
            String string = splitted[i];
            System.out.println(string);
        }
    }

    @Test
    public void testOther() {
        //数组元素拼接
        String[] array = { "aaa", "bbb", "ccc" };
        String result1 = StringUtils.join(array, ",");

        System.out.println(result1);//"aaa,bbb,ccc"

        //集合元素拼接
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        String result2 = StringUtils.join(list, ",");

        System.out.println(result2);//"aaa,bbb,ccc"

        System.out.println(StringEscapeUtils.escapeCsv("测试测试哦"));//"测试测试哦"
        System.out.println(StringEscapeUtils.escapeCsv("测试,测试哦"));//"\"测试,测试哦\""
        System.out.println(StringEscapeUtils.escapeCsv("测试\n测试哦"));//"\"测试\n测试哦\""

        System.out.println(StringEscapeUtils.escapeHtml4("测试测试哦"));//"<p>测试测试哦</p>"
        System.out.println(StringEscapeUtils.escapeJava("\"rensaninng\"，欢迎您！"));//"\"rensaninng\"\uFF0C\u6B22\u8FCE\u60A8\uFF01"

        System.out.println(StringEscapeUtils.escapeEcmaScript("测试'测试哦"));//"\u6D4B\u8BD5\'\u6D4B\u8BD5\u54E6"
        System.out.println(StringEscapeUtils.escapeXml("<tt>\"bread\" & \"butter\"</tt>"));//"<tt>"bread" &amp; "butter"</tt>"

        //随机数
        // 10位英字
        System.out.println(RandomStringUtils.randomAlphabetic(10));

        // 10位英数
        System.out.println(RandomStringUtils.randomAlphanumeric(10));

        // 10位ASCII码
        System.out.println(RandomStringUtils.randomAscii(10));

        // 指定文字10位
        System.out.println(RandomStringUtils.random(10, "abcde"));

        //数组
        // 追加元素到数组尾部
        int[] array1 = { 1, 2 };
        array1 = ArrayUtils.add(array1, 3); // => [1, 2, 3]

        System.out.println(array1.length);//3
        System.out.println(array1[2]);//3

        // 删除指定位置的元素
        int[] array2 = { 1, 2, 3 };
        array2 = ArrayUtils.remove(array2, 2); // => [1, 2]

        System.out.println(array2.length);//2

        // 截取部分元素
        int[] array3 = { 1, 2, 3, 4 };
        array3 = ArrayUtils.subarray(array3, 1, 3); // => [2, 3]

        System.out.println(array3.length);//2

        // 数组拷贝
        String[] array4 = { "aaa", "bbb", "ccc" };
        String[] copied = (String[]) ArrayUtils.clone(array4); // => {"aaa", "bbb", "ccc"}

        System.out.println(copied.length);//3

        // 判断是否包含某元素
        String[] array5 = { "aaa", "bbb", "ccc", "bbb" };
        boolean result3 = ArrayUtils.contains(array5, "bbb"); // => true
        System.out.println(result3);//true

        // 判断某元素在数组中出现的位置（从前往后，没有返回-1）
        int result4 = ArrayUtils.indexOf(array5, "bbb"); // => 1
        System.out.println(result4);//1

        // 判断某元素在数组中出现的位置（从后往前，没有返回-1）
        int result5 = ArrayUtils.lastIndexOf(array5, "bbb"); // => 3
        System.out.println(result5);//3

        // 数组转Map
        Map<Object, Object> map = ArrayUtils.toMap(new String[][] { { "key1", "value1" }, { "key2", "value2" } });
        System.out.println(map.get("key1"));//"value1"
        System.out.println(map.get("key2"));//"value2"

        // 判断数组是否为空
        Object[] array61 = new Object[0];
        Object[] array62 = null;
        Object[] array63 = new Object[] { "aaa" };

        System.out.println(ArrayUtils.isEmpty(array61));//true
        System.out.println(ArrayUtils.isEmpty(array62));//true
        System.out.println(ArrayUtils.isNotEmpty(array63));//true

        // 判断数组长度是否相等
        Object[] array71 = new Object[] { "aa", "bb", "cc" };
        Object[] array72 = new Object[] { "dd", "ee", "ff" };

        System.out.println(ArrayUtils.isSameLength(array71, array72));//true

        // 判断数组元素内容是否相等
        Object[] array81 = new Object[] { "aa", "bb", "cc" };
        Object[] array82 = new Object[] { "aa", "bb", "cc" };

        System.out.println(ArrayUtils.isEquals(array81, array82));

        // Integer[] 转化为 int[]
        Integer[] array9 = new Integer[] { 1, 2 };
        int[] result = ArrayUtils.toPrimitive(array9);

        System.out.println(result.length);//2
        System.out.println(result[0]);//1

        // int[] 转化为 Integer[]
        int[] array10 = new int[] { 1, 2 };
        Integer[] result10 = ArrayUtils.toObject(array10);

        System.out.println(result.length);//2
        System.out.println(result10[0].intValue());//1

        //日期
        try {
            // 生成Date对象
            Date date = DateUtils.parseDate("2010/01/01 11:22:33", new String[] { "yyyy/MM/dd HH:mm:ss" });

            // 10天后
            Date tenDaysAfter = DateUtils.addDays(date, 10); // => 2010/01/11 11:22:33
            System.out.println(DateFormatUtils.format(tenDaysAfter, "yyyy/MM/dd HH:mm:ss"));

            // 前一个月
            Date prevMonth = DateUtils.addMonths(date, -1); // => 2009/12/01 11:22:33
            System.out.println(DateFormatUtils.format(prevMonth, "yyyy/MM/dd HH:mm:ss"));

            // 判断是否是同一天
            Date date1 = DateUtils.parseDate("2010/01/01 11:22:33", new String[] { "yyyy/MM/dd HH:mm:ss" });
            Date date2 = DateUtils.parseDate("2010/01/01 22:33:44", new String[] { "yyyy/MM/dd HH:mm:ss" });
            System.out.println(DateUtils.isSameDay(date1, date2));// true

            // 日期格式化
            System.out.println(DateFormatUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
