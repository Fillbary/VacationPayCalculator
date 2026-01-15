package com.example.vacationPayCalculator.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) для ответа с результатом расчета отпускных выплат.
 * <p>
 * Содержит рассчитанную сумму отпускных и дополнительное сообщение с информацией
 * о расчете. Используется для возврата результатов из контроллера клиенту.
 * </p>
 *
 * <p>Пример ответа:</p>
 * <pre>
 * {
 *   "vacationPayAmount": 23890.41,
 *   "message": "Рассчитанная сумма отпускных за 14 дней"
 * }
 * </pre>
 *
 * @see com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO
 * @see com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO
 */
@AllArgsConstructor
@Data
public class CalculateResponseDTO {

    private BigDecimal vacationPayAmount;

    private String message;
}
