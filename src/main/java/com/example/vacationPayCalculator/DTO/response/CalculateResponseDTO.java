package com.example.vacationPayCalculator.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@AllArgsConstructor
@Data
public class CalculateResponseDTO {

    private BigDecimal vacationPayAmount;

    private String message;
}
