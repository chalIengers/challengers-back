package org.knulikelion.challengers_backend.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {
    MimeMessage createMessage(String email);
    String sendMail(String email);
    MimeMessage createPwMessage(String email);
    String sendPwMail(String email);
}
