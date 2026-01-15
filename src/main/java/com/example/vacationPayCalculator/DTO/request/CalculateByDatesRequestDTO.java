package com.example.vacationPayCalculator.DTO.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) для запроса расчета отпускных по конкретным датам.
 * <p>
 * Содержит все необходимые параметры для расчета отпускных выплат на основе
 * указанного периода отпуска с учетом праздничных и выходных дней.
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * CalculateByDatesRequestDTO request = new CalculateByDatesRequestDTO(
 *     new BigDecimal("50000.00"),
 *     LocalDate.of(2023, 6, 1),
 *     LocalDate.of(2023, 6, 14)
 * );
 * </pre>
 *
 * @see CalculateByDaysRequestDTO
 * @see com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO
 */
@Data
@AllArgsConstructor
public class CalculateByDatesRequestDTO {
    @NotNull(message = "Средняя зарплата обязательна")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    private BigDecimal averageSalary;

    @NotNull(message = "Дата начала отпуска обязательна к заполнению")
    @FutureOrPresent(message = "Дата отпуска не может быть в прошлом")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания отпуска обязательна к заполнению")
    @FutureOrPresent(message = "Дата отпуска не может быть в прошлом")
    private LocalDate endDate;
}
