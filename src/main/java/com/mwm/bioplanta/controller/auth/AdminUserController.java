package com.mwm.bioplanta.controller.auth;

import com.mwm.bioplanta.dto.auth.PasswordResponse;
import com.mwm.bioplanta.service.auth.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
