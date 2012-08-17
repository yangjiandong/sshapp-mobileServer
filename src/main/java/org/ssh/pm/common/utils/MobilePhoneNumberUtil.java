package org.ssh.pm.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobilePhoneNumberUtil {
    private static final Pattern phonepattern = Pattern.compile("^(13[0-9]|15[0-9]|18[0-9])\\d{8}$");

    private static boolean checkPhoneNumber(String number) {
        Matcher matcher = phonepattern.matcher(number);
        return matcher.matches();
    }

    public static boolean checkNumber(String number) {
        if (number == null || number.isEmpty()) {
            return true;
        }
        return checkPhoneNumber(number);
    }

    public static boolean canBeSent(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        return checkPhoneNumber(number);
    }
}
