package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.entity.Account;
import com.banking.entity.AccountStatus;
import com.banking.entity.AuditLog;
import com.banking.entity.Transaction;
import com.banking.service.AdminService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> users() {
        return ResponseEntity.ok(ApiResponse.ok("Users fetched", adminService.usersWithAccounts()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> transactions() {
        return ResponseEntity.ok(ApiResponse.ok("Transactions fetched", adminService.transactions()));
    }

    @PutMapping("/account/{accountNumber}/block")
    public ResponseEntity<ApiResponse<Account>> block(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Account blocked", adminService.setAccountStatus(accountNumber, AccountStatus.BLOCKED)));
    }

    @PutMapping("/account/{accountNumber}/unblock")
    public ResponseEntity<ApiResponse<Account>> unblock(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Account unblocked", adminService.setAccountStatus(accountNumber, AccountStatus.ACTIVE)));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> auditLogs() {
        return ResponseEntity.ok(ApiResponse.ok("Audit logs fetched", adminService.auditLogs()));
    }
}
