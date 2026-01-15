package com.example.vacationPayCalculator.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Конфигурационный класс для работы с праздничными днями.
 * Загружает список праздничных дней из конфигурационных свойств приложения.
 *
 * <p>Свойства конфигурации должны быть заданы в формате:
 * {@code vacation.holidays[0]=2023-01-01, vacation.holidays[1]=2023-01-02, ...}</p>
 *
 * <p>Пример использования в application.properties или application.yml:</p>
 * <pre>
 * # В формате properties:
 * vacation.holidays[0]=2023-01-01
 * vacation.holidays[1]=2023-01-02
 * vacation.holidays[2]=2023-01-07
 *
 * # В формате yaml:
 * vacation:
 *   holidays:
 *     - 2023-01-01
 *     - 2023-01-02
 *     - 2023-01-07
 * </pre>
 *
 * @see ConfigurationProperties
 * @see Component
 */
@Data
@Component
@ConfigurationProperties(prefix = "vacation")
public class HolidayConfiguration {
    /**
     * Список праздничных дней.
     * Инициализируется пустым списком по умолчанию.
     *
     * <p>При загрузке конфигурации Spring автоматически преобразует строковые значения
     * в объекты {@link LocalDate}.</p>
     *
     * <p>Если в конфигурации указаны некорректные даты, будет выброшено исключение
     * {@link org.springframework.boot.context.properties.bind.BindException}.</p>
     */
    private List<LocalDate> holidays = new ArrayList<>();
}
