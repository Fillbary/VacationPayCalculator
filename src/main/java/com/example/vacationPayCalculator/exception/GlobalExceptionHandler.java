package com.example.vacationPayCalculator.exception;

import com.example.vacationPayCalculator.DTO.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для приложения VacationPayCalculator.
 * <p>
 * Этот класс перехватывает исключения, возникающие в контроллерах,
 * и преобразует их в стандартизированные ответы об ошибках.
 * Обеспечивает централизованную обработку исключений для всего приложения.
 * </p>
 *
 * <p>Обрабатывает следующие типы исключений:</p>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} - ошибки валидации входных параметров</li>
 *   <li>{@link VacationCalculationException} - ошибки при расчете отпускных</li>
 * </ul>
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 * @see ErrorResponseDTO
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения валидации входных параметров.
     * <p>
     * Возникает при нарушении ограничений, заданных аннотациями валидации
     * в DTO-классах ({@link jakarta.validation.constraints}).
     * </p>
     *
     * @param ex исключение валидации
     * @param request текущий веб-запрос
     * @return ResponseEntity с деталями ошибки валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "ValidationFailed",
                errorMessage,
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, возникающие при расчете отпускных выплат.
     * <p>
     * Возникает при обнаружении бизнес-логических ошибок в процессе расчета,
     * таких как некорректные даты, недопустимые значения и т.д.
     * </p>
     *
     * @param ex исключение расчета отпускных
     * @param request текущий веб-запрос
     * @return ResponseEntity с деталями ошибки расчета
     */
    @ExceptionHandler(VacationCalculationException.class)
    public ResponseEntity<ErrorResponseDTO> handleVacationCalculationException(
            VacationCalculationException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Vacation Calculation Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
