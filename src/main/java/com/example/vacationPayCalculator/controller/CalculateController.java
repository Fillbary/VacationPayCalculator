package com.example.vacationPayCalculator.controller;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;
import com.example.vacationPayCalculator.service.VacationPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Контроллер для расчета отпускных выплат.
 * Предоставляет REST API для вычисления суммы отпускных на основе разных входных данных.
 *
 * <p>Контроллер обрабатывает два типа запросов:</p>
 * <ul>
 *   <li>Расчет по количеству дней отпуска</li>
 *   <li>Расчет по конкретным датам начала и окончания отпуска</li>
 * </ul>
 *
 * <p>Все методы возвращают ответ в формате JSON с рассчитанной суммой отпускных.</p>
 *
 * @see VacationPaymentService
 * @see CalculateResponseDTO
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/calculate")
public class CalculateController {
    private VacationPaymentService vacationPaymentService;

    public CalculateController(VacationPaymentService vacationPaymentService) {
        this.vacationPaymentService = vacationPaymentService;
    }

    /**
     * Расчет отпускных выплат на основе количества дней отпуска.
     *
     * <p>Пример запроса: {@code GET /api/v1/calculate/days?averageSalary=50000&numberOfVacationDays=14}</p>
     *
     * @param averageSalary средняя заработная плата за 12 месяцев (в рублях)
     * @param numberOfVacationDays количество дней отпуска
     * @return ResponseEntity с результатом расчета или сообщением об ошибке
     *
     * @throws IllegalArgumentException если параметры недопустимы (отрицательные значения и т.д.)
     *
     * @see CalculateByDaysRequestDTO
     * @see CalculateResponseDTO
     */
    @GetMapping("/days")
    public ResponseEntity<CalculateResponseDTO> calculateVacationPayWithsNumberOfDays(
            @RequestParam BigDecimal averageSalary,
            @RequestParam int numberOfVacationDays) {

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(averageSalary, numberOfVacationDays);
        CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithNumberOfDays(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Расчет отпускных выплат на основе дат начала и окончания отпуска.
     *
     * <p>Пример запроса: {@code GET /api/v1/calculate/dates?averageSalary=50000&startDate=2023-06-01&endDate=2023-06-14}</p>
     *
     * <p>При расчете учитываются праздничные дни, которые исключаются из общего количества дней отпуска.</p>
     *
     * @param averageSalary средняя заработная плата за 12 месяцев (в рублях)
     * @param startDate дата начала отпуска (включительно)
     * @param endDate дата окончания отпуска (включительно)
     * @return ResponseEntity с результатом расчета или сообщением об ошибке
     *
     * @throws IllegalArgumentException если параметры недопустимы (некорректные даты и т.д.)
     *
     * @see CalculateByDatesRequestDTO
     * @see CalculateResponseDTO
     */
    @GetMapping("/dates")
    public ResponseEntity<CalculateResponseDTO> calculateVacationPayWithDates(
            @RequestParam BigDecimal averageSalary,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        CalculateByDatesRequestDTO request = new CalculateByDatesRequestDTO(averageSalary, startDate, endDate);
        CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithDates(request);
        return ResponseEntity.ok(response);

    }
}
