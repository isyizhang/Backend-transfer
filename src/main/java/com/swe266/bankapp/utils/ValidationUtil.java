package com.swe266.bankapp.utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isValidPassword(String password) {
        if (password == null){
            return false;
        }
        String pattern = "^[_\\-\\.0-9a-z]{1,127}$";
        return password.matches(pattern);
    }

    public static boolean isValidUsername(String username) {
        if (username == null){
            return false;
        }
        String pattern = "^[_\\-\\.0-9a-z]{1,127}$";
        return username.matches(pattern);
    }

    public static boolean isValidAmount(double input) {

        if (input < 0.00 || input > 4294967295.99) {
            return false;
        }

        return true;

    }

}
