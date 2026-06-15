package com.banking.service;

import com.banking.dto.BeneficiaryRequest;
import com.banking.entity.Beneficiary;
import com.banking.entity.User;
import com.banking.exception.AccountNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.BeneficiaryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {

    private final CurrentUserService currentUserService;
    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(
            CurrentUserService currentUserService,
            AccountRepository accountRepository,
            BeneficiaryRepository beneficiaryRepository
    ) {
        this.currentUserService = currentUserService;
        this.accountRepository = accountRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Beneficiary add(BeneficiaryRequest request) {
        if (!accountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new AccountNotFoundException("Beneficiary account not found");
        }
        return beneficiaryRepository.save(Beneficiary.builder()
                .user(currentUserService.user())
                .name(request.name())
                .accountNumber(request.accountNumber())
                .build());
    }

    public List<Beneficiary> list() {
        return beneficiaryRepository.findByUserOrderByCreatedAtDesc(currentUserService.user());
    }

    public void delete(UUID id) {
        User user = currentUserService.user();
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary not found"));
        if (!beneficiary.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can delete only your own beneficiaries");
        }
        beneficiaryRepository.delete(beneficiary);
    }
}
