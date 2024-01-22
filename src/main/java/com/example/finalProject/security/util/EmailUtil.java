package com.example.finalProject.security.util;

import com.example.finalProject.exception.UserNotFoundException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class EmailUtil {
    @Value("${url.domain}")
    private String domain;

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

    public void sendOtpEmailLink(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Email");
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");
        htmlContent.append("<div>").append("<a href=\"").append(domain).append("/api/v1/auth/forgotpassword-web?email=").append(email).append("&token=").append(token).append("\" target=\"_blank\">click link to change password</a>").append(" please use this link before 5 minutes to change your new password").append("</div>");
        mimeMessageHelper.setText(htmlContent.toString(), true);
        htmlContent.append("</body></html>");

        javaMailSender.send(mimeMessage);
    }

    public void sendEticket(String email, byte[] pdfBytes ) throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("E-Ticket Fly.id");
        mimeMessageHelper.setText("E-Ticket anda harus dibawa, untuk syarat memasuki pesawat");

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        // Attach the PDF
        DataSource source = new ByteArrayDataSource(pdfBytes, "e-ticket/pdf");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("e-ticket.pdf");
        multipart.addBodyPart(messageBodyPart);

        // Add other parts of the email if needed

        mimeMessage.setContent(multipart);


        javaMailSender.send(mimeMessage);
    }



}
