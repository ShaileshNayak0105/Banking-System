package com.banking.repository;

import com.banking.entity.Beneficiary;
import com.banking.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {
    List<Beneficiary> findByUserOrderByCreatedAtDesc(User user);
}
