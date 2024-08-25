package org.lxp.springboot.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private static final String PERSONAL = "Super Li";
    @Value("${spring.mail.username}")
    private String username;
    @Inject
    private final JavaMailSender javaMailSender;

    public boolean send(String toAddress, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom(username, PERSONAL);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);
            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return false;
        }
    }

    public boolean send(String toAddress, String subject, String text, String pathToAttachment) {
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        return send(toAddress, subject, text, file);
    }

    private boolean send(String toAddress, String subject, String text, FileSystemResource file) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username, PERSONAL);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(message);
            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return false;
        }
    }
}
