package com.dreampany.framework.data.util;

/**
 * Created by air on 6/26/17.
 */

public class MathUtil {
    private MathUtil(){}

    public static int ipow(int base, int exp) {
        int result = 1;
        while (exp != 0) {
            if ((exp & 1) != 0) {
                result *= base;
            }
            exp >>= 1;
            base *= base;
        }
        return result;
    }
}
