package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.config.HolidayConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarService {
    private HolidayConfiguration holidayConfig;

    public CalendarService(HolidayConfiguration configuration) {
        this.holidayConfig = configuration;
    }

    private boolean isHoliday(LocalDate date) {
        List<LocalDate> holidays = holidayConfig.getHolidays();
        return date != null && holidays.contains(date);
    }

    public int getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Даты не могут быть null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания отпуска");
        }

        int countDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (!isHoliday(currentDate)) {
                countDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        if (countDays == 0) {
            throw new IllegalArgumentException(
                    "В указанном периоде нет оплачиваемых дней. Все дни являются праздничными."
            );
        }

        return countDays;
    }
}
