package org.knulikelion.challengers_backend.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {
    MimeMessage createMessage(String email) throws MessagingException;
    String sendMail(String email);
}
