package com.banking.dto;

import com.banking.entity.AccountStatus;

public record AccountDetailsResponse(
        String fullName,
        String email,
        String phone,
        String accountNumber,
        AccountStatus accountStatus
) {
}
