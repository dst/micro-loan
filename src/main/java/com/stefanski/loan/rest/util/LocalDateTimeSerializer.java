package com.stefanski.loan.rest.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE;

/**
 * @author Dariusz Stefanski
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        String formatted = dateTime.format(ISO_DATE);
        gen.writeString(formatted);
    }
}
