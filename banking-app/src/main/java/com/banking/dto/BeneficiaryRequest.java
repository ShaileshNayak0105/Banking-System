package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BeneficiaryRequest(
        @NotBlank String name,
        @NotBlank
        @Pattern(regexp = "\\d{10}", message = "Account number must be 10 digits")
        String accountNumber
) {
}
