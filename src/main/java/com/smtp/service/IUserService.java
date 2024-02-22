package com.smtp.service;

import com.smtp.domin.User;
import com.smtp.event.RegistrationRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author Sampson Alfred
 */

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);
}