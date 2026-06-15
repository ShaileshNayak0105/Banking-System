package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.AccountStatus;
import com.banking.entity.AuditLog;
import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.exception.AccountNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.AuditLogRepository;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;

    public AdminService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<Map<String, Object>> usersWithAccounts() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Account account = accountRepository.findByUser(user).orElse(null);
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", user.getId());
                    row.put("fullName", user.getFullName());
                    row.put("email", user.getEmail());
                    row.put("phone", user.getPhone());
                    row.put("role", user.getRole());
                    row.put("accountNumber", account == null ? null : account.getAccountNumber());
                    row.put("balance", account == null ? null : account.getBalance());
                    row.put("status", account == null ? null : account.getStatus());
                    return row;
                })
                .toList();
    }

    public List<Transaction> transactions() {
        return transactionRepository.findAllByOrderByCreatedAtDesc();
    }

    public Account setAccountStatus(String accountNumber, AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setStatus(status);
        return accountRepository.save(account);
    }

    public List<AuditLog> auditLogs() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }
}
