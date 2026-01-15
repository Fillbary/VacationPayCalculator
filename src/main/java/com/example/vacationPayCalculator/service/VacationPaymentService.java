package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;

import java.math.BigDecimal;


public interface VacationPaymentService {
    /**
     * Рассчитывает сумму отпускных выплат на основе количества дней отпуска.
     * <p>
     * Используется для расчета отпускных, когда известна только продолжительность
     * отпуска в днях, без привязки к конкретным датам. Все указанные дни считаются
     * оплачиваемыми.
     * </p>
     *
     * <p>Пример использования:</p>
     * <pre>
     * CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
     *     new BigDecimal("50000.00"),
     *     14
     * );
     * CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithNumberOfDays(request);
     * // Результат: сумма отпускных за 14 дней
     * </pre>
     *
     * @param request DTO запроса, содержащий среднюю зарплату и количество дней отпуска
     * @return DTO ответа с рассчитанной суммой отпускных и информационным сообщением
     * @throws com.example.vacationPayCalculator.exception.VacationCalculationException
     *          если параметры запроса некорректны
     */
    CalculateResponseDTO calculatePaymentWithNumberOfDays(CalculateByDaysRequestDTO request);

    /**
     * Рассчитывает сумму отпускных выплат на основе конкретных дат отпуска.
     * <p>
     * Используется для точного расчета отпускных с учетом праздничных и выходных дней.
     * Метод определяет количество рабочих дней в указанном периоде и рассчитывает
     * выплаты только за эти дни.
     * </p>
     *
     * <p>Пример использования:</p>
     * <pre>
     * CalculateByDatesRequestDTO request = new CalculateByDatesRequestDTO(
     *     new BigDecimal("50000.00"),
     *     LocalDate.of(2023, 6, 1),
     *     LocalDate.of(2023, 6, 14)
     * );
     * CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithDates(request);
     * // Результат: сумма отпускных за рабочие дни в указанном периоде
     * </pre>
     *
     * @param request DTO запроса, содержащий среднюю зарплату и даты отпуска
     * @return DTO ответа с рассчитанной суммой отпускных и информационным сообщением
     * @throws com.example.vacationPayCalculator.exception.VacationCalculationException
     *          если параметры запроса некорректны или в периоде нет рабочих дней
     */
    CalculateResponseDTO calculatePaymentWithDates(CalculateByDatesRequestDTO request);
}
