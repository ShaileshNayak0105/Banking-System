package com.banking.dto;

import java.math.BigDecimal;

public record AccountBalanceResponse(String accountNumber, BigDecimal balance) {
}
