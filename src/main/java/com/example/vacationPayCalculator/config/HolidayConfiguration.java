package com.example.vacationPayCalculator.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "vacation")
public class HolidayConfiguration {
    private List<LocalDate> holidays = new ArrayList<>();
}
