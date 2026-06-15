package com.banking.dto;

import com.banking.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionSearchRequest(
        LocalDate startDate,
        LocalDate endDate,
        TransactionType type,
        BigDecimal minAmount,
        BigDecimal maxAmount
) {
}
