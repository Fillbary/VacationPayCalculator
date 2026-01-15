package com.example.vacationPayCalculator.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) для стандартизированных сообщений об ошибках.
 * <p>
 * Используется для возврата структурированной информации об ошибках
 * в формате JSON при возникновении исключений в приложении.
 * Соответствует стандартному формату ошибок Spring Boot.
 * </p>
 *
 * <p>Пример ответа об ошибке:</p>
 * <pre>
 * {
 *   "timestamp": "2023-06-15 14:30:45",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Средняя зарплата обязательна",
 *   "path": "/api/v1/calculate/days"
 * }
 * </pre>
 *
 * <p>Этот DTO используется глобальным обработчиком исключений
 * для обеспечения единообразного формата ответов об ошибках.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
