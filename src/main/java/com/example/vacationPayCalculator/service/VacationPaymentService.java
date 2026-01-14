package com.example.vacationPayCalculator.service;

import com.example.vacationPayCalculator.DTO.request.CalculateByDatesRequestDTO;
import com.example.vacationPayCalculator.DTO.request.CalculateByDaysRequestDTO;
import com.example.vacationPayCalculator.DTO.response.CalculateResponseDTO;

public interface VacationPaymentService {
    CalculateResponseDTO calculatePaymentWithNumberOfDays(CalculateByDaysRequestDTO request);

    CalculateResponseDTO calculatePaymentWithDates(CalculateByDatesRequestDTO request);
}
