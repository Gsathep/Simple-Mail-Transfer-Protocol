package com.smtp.security;

import com.smtp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Sampson Alfred
 */
@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}