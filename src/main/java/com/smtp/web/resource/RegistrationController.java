package com.smtp.web.resource;


import com.smtp.domin.User;
import com.smtp.domin.VerificationToken;
import com.smtp.event.RegistrationCompleteEvent;
import com.smtp.event.RegistrationRequest;
import com.smtp.repo.VerificationTokenRepository;
import com.smtp.service.Iml.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sampson Alfred
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping("/save")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!  Please, check your email for to complete your registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }



    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}