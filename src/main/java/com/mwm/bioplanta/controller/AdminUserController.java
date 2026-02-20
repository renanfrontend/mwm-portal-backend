package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.PasswordResponse;
import com.mwm.bioplanta.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/{userId}/generate-password")
    public ResponseEntity<PasswordResponse> generatePassword(@PathVariable Long userId) {
        String newPassword = userService.generateAndSetNewPassword(userId);
        return ResponseEntity.ok(new PasswordResponse(newPassword));
    }
}
