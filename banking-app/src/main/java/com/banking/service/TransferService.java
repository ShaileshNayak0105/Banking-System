package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.entity.Account;
import com.banking.entity.AccountStatus;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.entity.User;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    private final CurrentUserService currentUserService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogService auditLogService;
    private final EmailService emailService;

    public TransferService(
            CurrentUserService currentUserService,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            AuditLogService auditLogService,
            EmailService emailService
    ) {
        this.currentUserService = currentUserService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
    }

    @Transactional
    public BigDecimal transfer(TransferRequest request) {
        User senderUser = currentUserService.user();
        Account sender = currentUserService.account();
        Account receiver = accountRepository.findByAccountNumber(request.receiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to your own account");
        }
        if (request.amount() == null || request.amount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Sender account is blocked");
        }
        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Receiver account is blocked");
        }
        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        receiver.setBalance(receiver.getBalance().add(request.amount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        String description = request.description() == null || request.description().isBlank()
                ? "Fund transfer"
                : request.description();

        transactionRepository.save(Transaction.builder()
                .senderAccount(sender.getAccountNumber())
                .receiverAccount(receiver.getAccountNumber())
                .amount(request.amount())
                .type(TransactionType.DEBIT)
                .description(description)
                .build());

        transactionRepository.save(Transaction.builder()
                .senderAccount(sender.getAccountNumber())
                .receiverAccount(receiver.getAccountNumber())
                .amount(request.amount())
                .type(TransactionType.CREDIT)
                .description(description)
                .build());

        auditLogService.log(senderUser, "TRANSFER", "Transferred " + request.amount() + " to " + receiver.getAccountNumber());
        emailService.sendTransferEmail(senderUser.getEmail(), request.amount(), receiver.getAccountNumber());

        return sender.getBalance();
    }
}
