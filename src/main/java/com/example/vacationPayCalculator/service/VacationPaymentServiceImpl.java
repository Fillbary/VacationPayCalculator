package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class VacationPaymentServiceImpl implements VacationPaymentService{
    private final CalendarService calendarService;

    private static final BigDecimal STANDARD_COEFFICIENT = new BigDecimal("29.3");
    private static final int MAX_VACATION_DAYS = 28;
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public VacationPaymentServiceImpl(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Override
    public CalculateResponseDTO calculatePaymentWithNumberOfDays(CalculateByDaysRequestDTO request) {

        int numberOfVacationDays = request.getNumberOfVacationDays();
        BigDecimal averageSalary = request.getAverageSalary();

        validateSalaryAndDays(averageSalary, numberOfVacationDays);
        return calculatePayment(averageSalary, numberOfVacationDays);
    }

    @Override
    public CalculateResponseDTO calculatePaymentWithDates(CalculateByDatesRequestDTO request) {
        BigDecimal averageSalary = request.getAverageSalary();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        int numberOfVacationDays = calendarService.getDaysBetweenDates(startDate, endDate);

        validateSalaryAndDays(averageSalary, numberOfVacationDays);
        return calculatePayment(averageSalary, numberOfVacationDays);
    }

    private CalculateResponseDTO calculatePayment(BigDecimal averageSalary, int numberOfVacationDays) {
        BigDecimal payment = averageSalary
                .divide(STANDARD_COEFFICIENT, 10, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(numberOfVacationDays))
                .setScale(DECIMAL_PLACES, ROUNDING_MODE);

        return new CalculateResponseDTO(payment, "Расчет выполнен успешно");
    }

    private void validateSalaryAndDays(BigDecimal averageSalary, int numberOfVacationDays) {
        if (averageSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше нуля");
        }

        if (numberOfVacationDays > MAX_VACATION_DAYS) {
            throw new IllegalArgumentException("Отпуск не может быть больше 28 дней");
        }

        if (numberOfVacationDays < 1) {
            throw new IllegalArgumentException("Количество дней отпуска должно быть не менее 1");
        }
    }
}
