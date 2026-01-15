package com.example.vacationPayCalculator.exception;

public class VacationCalculationException extends RuntimeException {
    public VacationCalculationException(String message) {
        super(message);
    }

    public VacationCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
