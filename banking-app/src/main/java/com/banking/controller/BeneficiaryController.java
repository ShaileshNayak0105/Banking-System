package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.BeneficiaryRequest;
import com.banking.entity.Beneficiary;
import com.banking.service.BeneficiaryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beneficiary")
@Validated
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Beneficiary>> add(@Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary added", beneficiaryService.add(request)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Beneficiary>>> list() {
        return ResponseEntity.ok(ApiResponse.ok("Beneficiaries fetched", beneficiaryService.list()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        beneficiaryService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary deleted"));
    }
}
