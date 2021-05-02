package com.eszop.ordersservice.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class DateTimeConfig {

    @Bean
    public String dateFormat() {
        return "yyyy-MM-dd";
    }

    @Bean
    public String dateTimeFormat() {
        return "yyyy-MM-dd-HH-mm-ss";
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat());
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat())));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat())));
        };
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }

}
