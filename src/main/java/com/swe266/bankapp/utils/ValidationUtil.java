package com.swe266.bankapp.utils;

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
        int fractional = (int) (input * 100) % 100;


        if (input < 0.00 || input > 4294967295.99) {
            return false;
        }
        // Check if fractional amount is exactly two digits
        if (fractional < 0 || fractional > 99) {
            return false;
        }
        return true;
    }

}
