package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;
import com.example.vacationPayCalculator.exception.VacationCalculationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Реализация сервиса расчета отпускных выплат.
 * <p>
 * Выполняет расчет отпускных выплат на основе средней заработной платы
 * и количества дней отпуска. Поддерживает два режима расчета:
 * <ol>
 *   <li>По количеству дней (без учета конкретных дат)</li>
 *   <li>По конкретным датам (с учетом праздников и выходных)</li>
 * </ol>
 * </p>
 *
 * <p>Расчетная формула:</p>
 * <pre>
 * Отпускные = (Средняя зарплата / 29.3) × Количество оплачиваемых дней
 * </pre>
 *
 * <p>где 29.3 - среднемесячное количество календарных дней согласно Трудовому кодексу РФ.</p>
 *
 * @see VacationPaymentService
 * @see CalendarService
 * @see VacationCalculationException
 */
@Service
public class VacationPaymentServiceImpl implements VacationPaymentService{
    private final CalendarService calendarService;

    /**
     * Стандартный коэффициент для расчета среднедневного заработка.
     * Согласно Трудовому кодексу РФ, среднемесячное количество календарных дней равно 29.3.
     */
    private static final BigDecimal STANDARD_COEFFICIENT = new BigDecimal("29.3");
    private static final int MAX_VACATION_DAYS = 28;
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public VacationPaymentServiceImpl(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalculateResponseDTO calculatePaymentWithNumberOfDays(CalculateByDaysRequestDTO request) {

        int numberOfVacationDays = request.getNumberOfVacationDays();
        BigDecimal averageSalary = request.getAverageSalary();

        validateSalaryAndDays(averageSalary, numberOfVacationDays);
        return calculatePayment(averageSalary, numberOfVacationDays);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalculateResponseDTO calculatePaymentWithDates(CalculateByDatesRequestDTO request) {
        BigDecimal averageSalary = request.getAverageSalary();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        int numberOfVacationDays = calendarService.getDaysBetweenDates(startDate, endDate);

        validateSalaryAndDays(averageSalary, numberOfVacationDays);
        return calculatePayment(averageSalary, numberOfVacationDays);
    }


    /**
     * Выполняет расчет суммы отпускных выплат.
     *
     * <p>Формула расчета:</p>
     * <pre>
     * 1. Среднедневной заработок = Средняя зарплата / 29.3
     * 2. Сумма отпускных = Среднедневной заработок × Количество дней отпуска
     * </pre>
     *
     * <p>Расчет выполняется с высокой точностью (10 знаков после запятой)
     * на промежуточных этапах, окончательный результат округляется до 2 знаков.</p>
     *
     * @param averageSalary средняя заработная плата
     * @param numberOfVacationDays количество оплачиваемых дней отпуска
     * @return DTO ответа с рассчитанной суммой и сообщением
     */
    public CalculateResponseDTO calculatePayment(BigDecimal averageSalary, int numberOfVacationDays) {
        BigDecimal payment = averageSalary
                .divide(STANDARD_COEFFICIENT, 10, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(numberOfVacationDays))
                .setScale(DECIMAL_PLACES, ROUNDING_MODE);

        return new CalculateResponseDTO(payment, "Расчет выполнен успешно");
    }

    /**
     * Выполняет валидацию входных параметров для расчета.
     *
     * @param averageSalary средняя заработная плата
     * @param numberOfVacationDays количество дней отпуска
     * @throws VacationCalculationException если параметры некорректны
     */
    private void validateSalaryAndDays(BigDecimal averageSalary, int numberOfVacationDays) {
        if (averageSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new VacationCalculationException("Зарплата должна быть больше нуля");
        }

        if (numberOfVacationDays > MAX_VACATION_DAYS) {
            throw new VacationCalculationException("Отпуск не может быть больше 28 дней");
        }

        if (numberOfVacationDays < 1) {
            throw new VacationCalculationException("Количество дней отпуска должно быть не менее 1");
        }
    }
}
