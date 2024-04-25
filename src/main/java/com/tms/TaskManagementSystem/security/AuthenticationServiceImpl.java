package com.tms.TaskManagementSystem.security;

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
import com.tms.TaskManagementSystem.response.Auth.AuthenticationResponse;
import com.tms.TaskManagementSystem.services.impl.WorkerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WorkerServiceImpl workerService;

    public AuthenticationResponse signUp(SignUpRequest request)
    {
        Organization organizationWorker = organizationRepository.findByIdAndStatus(request.getOrganization_id(), OrganizationStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));

        if(workerService.checkingUserCredentials(request.getUsername(),request.getPassword(), request.getEmail(), false, false))
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
        if(!request.getUsername().isBlank() && !request.getPassword().isBlank())
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            Worker selectedUser = workerRepository.findByUsernameAndStatus(request.getUsername(),WorkerStatus.ACTIVE)
                    .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_USERNAME));
            String token = jwtService.generateToken(selectedUser);
            return new AuthenticationResponse(token);
        }
       else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_CREDENTIALS);
        }
    }

    @Override
    public AuthenticationResponse adminCreate(SignUpRequest request) {
        if(workerService.checkingUserCredentials(request.getUsername(),request.getPassword(),request.getEmail(),false, false))
        {
            Worker worker = workerRepository.save(Worker.builder()
                    .name(request.getName())
                    .surname(request.getSurname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername().toLowerCase())
                    .status(WorkerStatus.ACTIVE)
                    .role(Role.ADMIN)
                    .build());

            String token = jwtService.generateToken(worker);
            return new AuthenticationResponse(token);
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }
}