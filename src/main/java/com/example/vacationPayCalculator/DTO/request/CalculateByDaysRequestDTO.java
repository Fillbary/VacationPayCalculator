package com.example.vacationPayCalculator.DTO.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) для запроса расчета отпускных по количеству дней.
 * <p>
 * Содержит параметры для расчета отпускных выплат на основе указанного количества
 * дней отпуска. Расчет выполняется без учета конкретных дат и праздников.
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
 *     new BigDecimal("50000.00"),
 *     14
 * );
 * </pre>
 *
 * <p>Примечание: максимальное количество дней отпуска ограничено 28 днями,
 * что соответствует стандартному ежегодному оплачиваемому отпуску.</p>
 *
 * @see CalculateByDatesRequestDTO
 * @see com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO
 */
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
