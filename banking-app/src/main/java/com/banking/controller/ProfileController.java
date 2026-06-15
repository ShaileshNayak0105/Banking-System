package com.banking.controller;

import com.banking.dto.AccountDetailsResponse;
import com.banking.dto.ApiResponse;
import com.banking.dto.ChangePasswordRequest;
import com.banking.dto.ProfileUpdateRequest;
import com.banking.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@Validated
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> profile() {
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched", profileService.profile()));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> update(@Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", profileService.update(request)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed"));
    }
}
