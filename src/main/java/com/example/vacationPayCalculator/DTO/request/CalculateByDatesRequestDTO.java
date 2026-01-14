package com.example.vacationPayCalculator.DTO.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CalculateByDatesRequestDTO {
    @NotNull(message = "Средняя зарплата обязательна")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    private BigDecimal averageSalary;

    @NotNull(message = "Дата начала отпуска обязательна к заполнению")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания отпуска обязательна к заполнению")
    private LocalDate endDate;
}
