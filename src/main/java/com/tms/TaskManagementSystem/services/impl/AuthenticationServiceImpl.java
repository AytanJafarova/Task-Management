package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Auth.SignInRequest;
import com.tms.TaskManagementSystem.request.Auth.SignUpRequest;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.response.Auth.AuthenticationResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.AuthenticationService;
import com.tms.TaskManagementSystem.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    boolean checkingUserCredentials(String username, String password, boolean ignoreUsername) {
        if (username.isBlank() || password.isBlank() || password.length() < 8) {
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_USERNAME_PASSWORD);
        }
        workerRepository.findByUsername(username.toLowerCase())
                .ifPresent(worker -> {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_USERNAME_EXISTS);
                });

        return true;
    }


    public AuthenticationResponse signUp(SignUpRequest request)
    {
        Organization organizationWorker = organizationRepository.findByIdAndStatus(request.getOrganization_id(), OrganizationStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));
        if(checkingUserCredentials(request.getUsername(),request.getPassword(),false))
        {
            Worker worker = workerRepository.save(Worker.builder()
                    .name(request.getName())
                    .surname(request.getSurname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername().toLowerCase())
                    .status(WorkerStatus.ACTIVE)
                    .role(Role.USER)
                    .organization(organizationWorker).build());

            String token = jwtService.generateToken(worker);
            return new AuthenticationResponse(token);
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }

    }

    public AuthenticationResponse signIn(SignInRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Worker selectedUser = workerRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_USERNAME));
        String token = jwtService.generateToken(selectedUser);
        return new AuthenticationResponse(token);
    }
}
