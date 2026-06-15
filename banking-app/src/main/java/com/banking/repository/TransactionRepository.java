package com.banking.repository;

import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findTop50BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(String senderAccount, String receiverAccount);

    List<Transaction> findTop10BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(String senderAccount, String receiverAccount);

    List<Transaction> findAllByOrderByCreatedAtDesc();

    @Query("""
            select t from BankingTransaction t
            where (t.senderAccount = :accountNumber or t.receiverAccount = :accountNumber)
            and (:startDate is null or t.createdAt >= :startDate)
            and (:endDate is null or t.createdAt <= :endDate)
            and (:type is null or t.type = :type)
            and (:minAmount is null or t.amount >= :minAmount)
            and (:maxAmount is null or t.amount <= :maxAmount)
            order by t.createdAt desc
            """)
    List<Transaction> search(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("type") TransactionType type,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount
    );
}
