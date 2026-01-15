package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.config.HolidayConfiguration;
import com.example.vacationPayCalculator.exception.VacationCalculationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с календарными данными и расчета рабочих дней.
 * <p>
 * Предоставляет функциональность для определения праздничных дней
 * и расчета количества рабочих дней в заданном периоде с учетом
 * праздников и выходных.
 * </p>
 *
 * <p>Основные функции:</p>
 * <ul>
 *   <li>Проверка, является ли конкретная дата праздничным днем</li>
 *   <li>Подсчет рабочих дней между двумя датами с исключением праздников</li>
 *   <li>Валидация входных данных для календарных операций</li>
 * </ul>
 *
 * @see HolidayConfiguration
 * @see VacationCalculationException
 */
@Service
public class CalendarService {
    private HolidayConfiguration holidayConfig;

    public CalendarService(HolidayConfiguration configuration) {
        this.holidayConfig = configuration;
    }

    private boolean isHoliday(LocalDate date) {
        if (date == null) return false;
        List<LocalDate> holidays = holidayConfig.getHolidays();
        return holidays.contains(date);
    }

    /**
     * Рассчитывает количество рабочих дней между двумя датами (включительно).
     * <p>
     * Метод исключает из подсчета праздничные дни, определенные в конфигурации.
     * Оба конца периода (начальная и конечная даты) включаются в расчет.
     * </p>
     *
     * <p>Пример:</p>
     * <pre>
     * // Если 1 января - праздник
     * int days = getDaysBetweenDates(
     *     LocalDate.of(2023, 1, 1),
     *     LocalDate.of(2023, 1, 5)
     * ); // вернет 4 (без 1 января)
     * </pre>
     *
     * @param startDate дата начала периода (включительно)
     * @param endDate дата окончания периода (включительно)
     * @return количество рабочих дней в периоде
     * @throws VacationCalculationException если:
     *          <ul>
     *            <li>startDate или endDate равны null</li>
     *            <li>startDate позже endDate</li>
     *            <li>в периоде нет рабочих дней (все дни - праздники)</li>
     *          </ul>
     */
    public int getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new VacationCalculationException("Даты не могут быть null");
        }

        if (startDate.isAfter(endDate)) {
            throw new VacationCalculationException("Дата начала не может быть позже даты окончания отпуска");
        }

        int countDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (!isHoliday(currentDate)) {
                countDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        if (countDays == 0) {
            throw new VacationCalculationException(
                    "В указанном периоде нет оплачиваемых дней. Все дни являются праздничными."
            );
        }

        return countDays;
    }
}
