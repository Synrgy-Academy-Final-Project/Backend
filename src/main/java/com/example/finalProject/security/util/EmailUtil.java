package com.example.finalProject.security.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");
        htmlContent.append("<h2>").append(otp).append(" is your email verification code. Please use thise code before 1 minutes or you must regenerate the code again").append("</h2>");
        mimeMessageHelper.setText(htmlContent.toString(), true);
        htmlContent.append("</body></html>");

        javaMailSender.send(mimeMessage);
    }


//    public void sendOtpEmailForgot(String email, String otp) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        mimeMessageHelper.setTo(email);
//        mimeMessageHelper.setSubject("Verify OTP For Forget Password");
//        StringBuilder htmlContent = new StringBuilder();
//        htmlContent.append("<html><body>");
//        htmlContent.append("<h2>").append(otp).append(" is your email verification code. Please use thise code before 1 minutes or you must regenerate the code again").append("</h2>");
//        mimeMessageHelper.setText(htmlContent.toString(), true);
//        htmlContent.append("</body></html>");
//
//        javaMailSender.send(mimeMessage);
//    }
}
