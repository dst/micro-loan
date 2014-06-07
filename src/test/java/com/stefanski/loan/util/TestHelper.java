package com.stefanski.loan.util;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author Dariusz Stefanski
 */
public class TestHelper {

    public static final Comparator<BigDecimal> bigDecimalComparator = new Comparator<BigDecimal>() {
        public int compare(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
            return bigDecimal1.compareTo(bigDecimal2);
        }
    };
}
