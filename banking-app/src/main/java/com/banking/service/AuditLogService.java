package com.banking.service;

import com.banking.entity.AuditLog;
import com.banking.entity.User;
import com.banking.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(User user, String action, String details) {
        auditLogRepository.save(AuditLog.builder()
                .userId(user.getId())
                .action(action)
                .details(details)
                .build());
    }
}
