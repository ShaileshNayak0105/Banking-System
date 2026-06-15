package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.exception.AccountNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public CurrentUserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User user() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));
    }

    public Account account() {
        return accountRepository.findByUser(user())
                .orElseThrow(() -> new AccountNotFoundException("Account not found for logged-in user"));
    }
}
