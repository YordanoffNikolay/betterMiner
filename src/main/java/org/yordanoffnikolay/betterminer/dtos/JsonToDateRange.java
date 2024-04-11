package org.yordanoffnikolay.betterminer.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToDateRange {
    public static DateRange mapJsonToDateRange(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DateRange dateRange = objectMapper.readValue(json, DateRange.class);
        return dateRange;
    }
}
