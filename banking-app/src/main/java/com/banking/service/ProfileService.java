package com.banking.service;

import com.banking.dto.AccountDetailsResponse;
import com.banking.dto.ChangePasswordRequest;
import com.banking.dto.ProfileUpdateRequest;
import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(
            CurrentUserService currentUserService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountDetailsResponse profile() {
        User user = currentUserService.user();
        Account account = currentUserService.account();
        return new AccountDetailsResponse(user.getFullName(), user.getEmail(), user.getPhone(), account.getAccountNumber(), account.getStatus());
    }

    public AccountDetailsResponse update(ProfileUpdateRequest request) {
        User user = currentUserService.user();
        Account account = currentUserService.account();
        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email is already in use");
                });

        user.setEmail(request.email());
        user.setPhone(request.phone());
        User savedUser = userRepository.save(user);
        return new AccountDetailsResponse(
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                account.getAccountNumber(),
                account.getStatus()
        );
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = currentUserService.user();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
