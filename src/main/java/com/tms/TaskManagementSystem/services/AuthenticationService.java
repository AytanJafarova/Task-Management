package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Auth.SignInRequest;
import com.tms.TaskManagementSystem.request.Auth.SignUpRequest;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.response.Auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);
    AuthenticationResponse signIn(SignInRequest request);
}
