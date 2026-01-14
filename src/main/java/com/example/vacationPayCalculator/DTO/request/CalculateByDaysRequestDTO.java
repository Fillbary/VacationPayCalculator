package com.example.vacationPayCalculator.DTO.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CalculateByDaysRequestDTO {

    @NotNull(message = "Средняя зарплата обязательна")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    private BigDecimal averageSalary;

    @NotNull(message = "Количество дней отпуска обязательно")
    @Min(value = 1, message = "Количество дней отпуска должно быть не менее 1")
    @Max(value = 28, message = "Количество дней отпуска не может превышать 28")
    private int numberOfVacationDays;

}
