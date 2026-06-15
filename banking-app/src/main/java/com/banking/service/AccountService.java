package com.banking.service;

import com.banking.dto.AccountBalanceResponse;
import com.banking.dto.AccountDetailsResponse;
import com.banking.entity.Account;
import com.banking.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final CurrentUserService currentUserService;

    public AccountService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public AccountBalanceResponse balance() {
        Account account = currentUserService.account();
        return new AccountBalanceResponse(account.getAccountNumber(), account.getBalance());
    }

    public AccountDetailsResponse details() {
        User user = currentUserService.user();
        Account account = currentUserService.account();
        return new AccountDetailsResponse(
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                account.getAccountNumber(),
                account.getStatus()
        );
    }
}
