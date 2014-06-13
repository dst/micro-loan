package com.stefanski.loan.util;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Dariusz Stefanski
 */
public class TestHelper {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
}
