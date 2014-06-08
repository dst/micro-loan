package com.stefanski.loan.util;

import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Comparator;

/**
 * @author Dariusz Stefanski
 */
public class TestHelper {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );


    public static final Comparator<BigDecimal> bigDecimalComparator = new Comparator<BigDecimal>() {
        public int compare(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
            return bigDecimal1.compareTo(bigDecimal2);
        }
    };
}
