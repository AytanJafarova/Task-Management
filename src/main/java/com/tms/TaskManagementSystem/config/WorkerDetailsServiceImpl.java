package com.tms.TaskManagementSystem.config;

import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerDetailsServiceImpl implements UserDetailsService {
    private final WorkerRepository workerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return workerRepository.findByUsername(username).orElseThrow(()-> new DataNotFoundException("USer not found"));
    }
}
