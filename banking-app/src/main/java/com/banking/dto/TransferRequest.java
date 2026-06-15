package com.banking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank
        @Pattern(regexp = "\\d{10}", message = "Receiver account number must be 10 digits")
        String receiverAccountNumber,
        @NotNull @Min(value = 1, message = "Amount must be greater than 0")
        BigDecimal amount,
        String description
) {
}
