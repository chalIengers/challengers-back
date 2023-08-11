package org.knulikelion.challengers_backend.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {
    MimeMessage createMessage(String name, String email) throws MessagingException;
    int sendMail(String name,String email);
}
