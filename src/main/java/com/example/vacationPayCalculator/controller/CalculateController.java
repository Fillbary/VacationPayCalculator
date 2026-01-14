package com.example.vacationPayCalculator.controller;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;
import com.example.vacationPayCalculator.service.VacationPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/calculate")
public class CalculateController {
    private VacationPaymentService vacationPaymentService;

    public CalculateController(VacationPaymentService vacationPaymentService) {
        this.vacationPaymentService = vacationPaymentService;
    }

    @GetMapping("/days")
    public ResponseEntity<CalculateResponseDTO> calculateVacationPayWithsNumberOfDays(
            @RequestParam BigDecimal averageSalary,
            @RequestParam int numberOfVacationDays) {

        CalculateByDaysRequestDTO request = new CalculateByDaysRequestDTO(averageSalary, numberOfVacationDays);

        try {
            CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithNumberOfDays(request);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Обработка бизнес-ошибок
            CalculateResponseDTO errorResponse = new CalculateResponseDTO(
                    BigDecimal.ZERO,
                    "Ошибка расчета: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/dates")
    public ResponseEntity<CalculateResponseDTO> calculateVacationPayWithDates(
            @RequestParam BigDecimal averageSalary,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        CalculateByDatesRequestDTO request = new CalculateByDatesRequestDTO(averageSalary, startDate, endDate);

        try {
            CalculateResponseDTO response = vacationPaymentService.calculatePaymentWithDates(request);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {

            CalculateResponseDTO errorResponse = new CalculateResponseDTO(
                    BigDecimal.ZERO,
                    "Ошибка расчета: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
