package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Auth.SignInRequest;
import com.tms.TaskManagementSystem.request.Auth.SignUpRequest;
import com.tms.TaskManagementSystem.response.Auth.AuthenticationResponse;
import com.tms.TaskManagementSystem.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    @Operation(summary = "Creating worker(user) account")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpRequest request)
    {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @PostMapping("/signUpAdmin")
    @Operation(summary = "Creating admin account")
    public ResponseEntity<AuthenticationResponse> signUpAdmin(@RequestBody SignUpRequest request)
    {
        return ResponseEntity.ok(authenticationService.adminCreate(request));
    }

    @PostMapping("/signIn")
    @Operation(summary = "Log in")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInRequest request)
    {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
