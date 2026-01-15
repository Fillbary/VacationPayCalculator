package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.config.HolidayConfiguration;
import com.example.vacationPayCalculator.exception.VacationCalculationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Модульные тесты для класса {@link CalendarService}.
 * <p>
 * Тестирует основные функции сервиса работы с календарными данными:
 * <ul>
 *   <li>Расчет количества рабочих дней между датами</li>
 *   <li>Учет праздничных дней при расчете</li>
 *   <li>Валидация входных параметров</li>
 *   <li>Обработка граничных случаев</li>
 * </ul>
 * </p>
 *
 * <p>Используется Mockito для мокирования зависимостей и JUnit 5 для тестирования.</p>
 *
 * @see CalendarService
 * @see HolidayConfiguration
 * @see VacationCalculationException
 */
@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {
    @Mock
    HolidayConfiguration holidayConfig;

    CalendarService calendarService;

    private static final LocalDate TEST_DATE_1 = LocalDate.of(2026, 1, 10);
    private static final LocalDate TEST_DATE_2 = LocalDate.of(2026, 1, 15);
    private static final LocalDate HOLIDAY_DATE = LocalDate.of(2026, 1, 11);

    @BeforeEach
    void setUp() {
        calendarService = new CalendarService(holidayConfig);
    }

    @Test
    public void shouldCalculateDaysBetweenDates() {
        //When
        int result = calendarService.getDaysBetweenDates(TEST_DATE_1, TEST_DATE_2);
        //Then
        assertEquals(6, result, "Должно быть 6 дней без праздников");
    }

    @Test
    public void shouldCalculateDaysBetweenDates_WithHolidays() {
        //Given
        List<LocalDate> holidays = List.of(
                HOLIDAY_DATE
        );
        when(holidayConfig.getHolidays()).thenReturn(holidays);

        //When
        int result = calendarService.getDaysBetweenDates(TEST_DATE_1, TEST_DATE_2);
        //Then
        assertEquals(5, result, "Должно быть 5 дней так как один день праздничный");
    }

    @Test
    void shouldThrowException_WhenDatesInvalid() {
        // Given
        LocalDate nullDate = null;

        // When & Then
        VacationCalculationException exception1 = assertThrows(
                VacationCalculationException.class,
                () -> calendarService.getDaysBetweenDates(nullDate, TEST_DATE_2)
        );
        VacationCalculationException exception2 = assertThrows(
                VacationCalculationException.class,
                () -> calendarService.getDaysBetweenDates(TEST_DATE_1, nullDate)
        );

        assertEquals("Даты не могут быть null", exception1.getMessage());
        assertEquals("Даты не могут быть null", exception2.getMessage());
    }


    @Test
    void shouldThrowException_WhenStartDateAfterEndDate(){
        //When & Then
        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> calendarService.getDaysBetweenDates(TEST_DATE_2, TEST_DATE_1));

        assertEquals("Дата начала не может быть позже даты окончания отпуска", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenAllDatesAreHolidays() {
        //Given
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 4);

        List<LocalDate> holidays = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4)
        );
        when(holidayConfig.getHolidays()).thenReturn(holidays);

        VacationCalculationException exception = assertThrows(
                VacationCalculationException.class,
                () -> calendarService.getDaysBetweenDates(startDate, endDate)
        );

        assertEquals(
                "В указанном периоде нет оплачиваемых дней. Все дни являются праздничными.",
                exception.getMessage());
    }


}
