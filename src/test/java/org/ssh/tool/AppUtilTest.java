package org.ssh.tool;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;
import org.ssh.pm.common.utils.AppUtil;

public class AppUtilTest {
    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    @Test
    public void testDifferenceInDays() {
        //个别时区计算天数有缺陷
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

        Calendar startCal = new GregorianCalendar(2007, 3 - 1, 24);
        Calendar endCal = new GregorianCalendar(2007, 3 - 1, 25);
        long days = AppUtil.differenceInDays(endCal, startCal);

        //System.out.println(days);
        assertEquals(1, days);

        days = AppUtil.differenceInDays2(endCal, startCal);
        //System.out.println(days);
        assertEquals(1, days);
        //以上测试还看不出问题

        Date d3 = new Date("03/24/2012 12:00:00");
        Date d4 = new Date("03/25/2012 12:00:00");
        //printOutput("Manual   ", d3, d4, AppUtil.calculateDays(d3, d4));
        //有缺陷
        assertEquals(0, AppUtil.calculateDays(d3, d4));

        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(d3);
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(d4);
        //printOutput("Calendar   ", d3, d4, AppUtil.differenceInDays(cal4, cal3));
        assertEquals(1, AppUtil.differenceInDays(cal4, cal3));

        //printOutput("Calendar2   ", d3, d4, AppUtil.differenceInDays2(cal4, cal3));
        //有缺陷
        assertEquals(0, AppUtil.differenceInDays2(cal4, cal3));
    }

    private static void printOutput(String type, Date d1, Date d2, long result) {
        System.out.println(type + "- Days between: " + sdf.format(d1) + " and " + sdf.format(d2) + " is: " + result);
    }

    @Test
    public void testConCatFld_FldSQL_s(){
        //"Microsoft SQL Server";"Oracle"
        String sql = AppUtil.conCatFld_FldSQL_s("Microsoft SQL Server", "a.Year", "._s", "a.Month", ".01_s");
        System.out.println(sql);
        assertEquals(" isnull(a.Year,'')  + '.'  + isnull(a.Month,'')  + '.01' ", sql);
    }
}
