package com.smtp.event;

import com.smtp.domin.User;
import com.smtp.service.Iml.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author Sampson Alfred
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;

    private final JavaMailSenderImpl mailSender;
    private User user;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        user = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(user, verificationToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //5. Send the email.
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service by GEETA";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("gogawalesunny58@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}