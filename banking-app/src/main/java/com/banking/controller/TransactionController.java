package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.TransactionSearchRequest;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.service.TransactionService;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Transaction>>> history() {
        return ResponseEntity.ok(ApiResponse.ok("Transaction history fetched", transactionService.history()));
    }

    @GetMapping("/mini-statement")
    public ResponseEntity<ApiResponse<List<Transaction>>> miniStatement() {
        return ResponseEntity.ok(ApiResponse.ok("Mini statement fetched", transactionService.miniStatement()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Transaction>>> search(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String minAmount,
            @RequestParam(required = false) String maxAmount
    ) {
        TransactionSearchRequest request = new TransactionSearchRequest(
                parseDate(startDate),
                parseDate(endDate),
                parseType(type),
                parseAmount(minAmount),
                parseAmount(maxAmount)
        );
        return ResponseEntity.ok(ApiResponse.ok("Transactions fetched", transactionService.search(request)));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("statement.pdf").build().toString())
                .body(transactionService.exportPdf());
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("statement.csv").build().toString())
                .body(transactionService.exportCsv().getBytes(StandardCharsets.UTF_8));
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format: " + value);
        }
    }

    private TransactionType parseType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return TransactionType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid transaction type: " + value);
        }
    }

    private BigDecimal parseAmount(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid amount: " + value);
        }
    }
}
