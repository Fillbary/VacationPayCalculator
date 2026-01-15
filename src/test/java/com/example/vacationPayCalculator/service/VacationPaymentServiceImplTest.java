package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;
import com.example.vacationPayCalculator.exception.VacationCalculationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Модульные тесты для класса {@link VacationPaymentServiceImpl}.
 * <p>
 * Тестирует основные функции сервиса расчета отпускных выплат:
 * <ul>
 *   <li>Расчет отпускных по количеству дней</li>
 *   <li>Расчет отпускных по конкретным датам</li>
 *   <li>Валидацию входных параметров</li>
 *   <li>Корректность финансовых расчетов</li>
 * </ul>
 * </p>
 *
 * <p>Используется Mockito для мокирования зависимостей {@link CalendarService}
 * и JUnit 5 для тестирования.</p>
 *
 * @see VacationPaymentServiceImpl
 * @see VacationCalculationException
 */
@ExtendWith(MockitoExtension.class)
public class VacationPaymentServiceImplTest {
    @Mock
    private CalendarService calendarService;

    private VacationPaymentServiceImpl vacationPaymentService;

    @BeforeEach
    void setUp() {
        vacationPaymentService = new VacationPaymentServiceImpl(calendarService);
    }

    @Test
    public void shouldCalculateCorrectAmountForDays() {
        //Given
        BigDecimal averageSalary = new BigDecimal("50000");
        int numberOfVacationDays = 14;

        CalculateByDaysRequestDTO requestDTO = new CalculateByDaysRequestDTO(averageSalary, numberOfVacationDays);

        //When
        CalculateResponseDTO result = vacationPaymentService.calculatePaymentWithNumberOfDays(requestDTO);


        //Then
        // Расчет: 50000 / 29.3 * 14 = 1706.48 * 14 = 23890.78
        BigDecimal expectedAmount = new BigDecimal("23890.78");

        assertEquals(0, expectedAmount.compareTo(result.getVacationPayAmount()),
                "Сумма отпускных должна быть 23890.78");
        assertEquals("Расчет выполнен успешно", result.getMessage());
    }

    @Test
    public void shouldCalculateCorrectAmountForDates() {
        //Given
        BigDecimal averageSalary = new BigDecimal("50000");
        LocalDate startDate = LocalDate.of(2026, 01, 10);
        LocalDate endDate = LocalDate.of(2026, 01, 15);
        int workingDays = 5;

        when(calendarService.getDaysBetweenDates(startDate, endDate)).thenReturn(5);

        CalculateByDatesRequestDTO requestDTO = new CalculateByDatesRequestDTO(averageSalary, startDate, endDate);

        //When
        CalculateResponseDTO result = vacationPaymentService.calculatePaymentWithDates(requestDTO);

        //Then
        // Расчет: 50000 / 29.3 * 5 = 8532.42
        BigDecimal expectedAmount = new BigDecimal("8532.42");

        assertEquals(0, expectedAmount.compareTo(result.getVacationPayAmount()),
                "Сумма отпускных должна быть 8532.42");
        assertEquals("Расчет выполнен успешно", result.getMessage());
    }

    @Test
    void shouldThrowException_WhenSalaryIsZero() {
        // Given
        BigDecimal zeroSalary = BigDecimal.ZERO;
        int numberOfVacationDays = 14;

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
                zeroSalary, numberOfVacationDays
        );

        // When & Then
        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> vacationPaymentService.calculatePaymentWithNumberOfDays(request)
        );

        assertEquals("Зарплата должна быть больше нуля", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenSalaryIsNegative() {
        // Given
        BigDecimal negativeSalary = new BigDecimal("-1000");
        int numberOfVacationDays = 14;

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
                negativeSalary, numberOfVacationDays
        );

        // When & Then
        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> vacationPaymentService.calculatePaymentWithNumberOfDays(request)
        );

        assertEquals("Зарплата должна быть больше нуля", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenVacationDaysExceedMaximum() {
        // Given
        BigDecimal averageSalary = new BigDecimal("50000");
        int tooManyDays = 29; // Максимум 28

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
                averageSalary, tooManyDays
        );

        // When & Then
        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> vacationPaymentService.calculatePaymentWithNumberOfDays(request)
        );

        assertEquals("Отпуск не может быть больше 28 дней", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenVacationDaysLessThanMinimum() {
        // Given
        BigDecimal averageSalary = new BigDecimal("50000");
        int zeroDays = 0; // Минимум 1

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(
                averageSalary, zeroDays
        );

        // When & Then
        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> vacationPaymentService.calculatePaymentWithNumberOfDays(request)
        );

        assertEquals("Количество дней отпуска должно быть не менее 1", exception.getMessage());
    }

}
