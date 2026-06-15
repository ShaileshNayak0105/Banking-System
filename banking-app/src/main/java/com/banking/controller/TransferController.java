package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.TransferRequest;
import com.banking.service.TransferService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@Validated
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<BigDecimal>> send(@Valid @RequestBody TransferRequest request) {
        BigDecimal newBalance = transferService.transfer(request);
        return ResponseEntity.ok(ApiResponse.ok("Transfer successful", newBalance));
    }
}
