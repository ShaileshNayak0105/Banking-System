package com.banking.controller;

import com.banking.dto.AccountBalanceResponse;
import com.banking.dto.AccountDetailsResponse;
import com.banking.dto.ApiResponse;
import com.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<AccountBalanceResponse>> balance() {
        return ResponseEntity.ok(ApiResponse.ok("Balance fetched", accountService.balance()));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> details() {
        return ResponseEntity.ok(ApiResponse.ok("Account details fetched", accountService.details()));
    }
}
