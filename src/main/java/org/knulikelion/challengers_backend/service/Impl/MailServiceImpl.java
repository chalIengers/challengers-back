package org.knulikelion.challengers_backend.service.Impl;

import org.springframework.core.io.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "noreply.challengers@knu-likelion.org";
    private static int number;
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public static void createNumber(){
        number = (int)(Math.random() * 999999-100000+1) + 100000; /*Math.random() * 최댓값-최솟값+1) + 최솟값*/
    }
    @Override
    public MimeMessage createMessage(String email){
        createNumber();
        log.info("Number : {}",number);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("[Challengers] 이메일 인증 번호 발송");

            String body = "<html><body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff;'>";
            body += "<img class='logo' src='cid:image'>";
            body += "<h1 style='padding-top: 50px; font-size: 30px;'>이메일 주소 인증</h1>";
            body += "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? Challengers 관리자 입니다.<br />";
            body += "Challengers 서비스 사용을 위해 회원가입시 고객님께서 입력하신 이메일 주소의 인증이 필요합니다.<br />";
            body += "하단의 인증 번호로 이메일 인증을 완료하시면, 정상적으로 Challengers 서비스를 이용하실 수 있습니다.<br />";
            body += "항상 최선의 노력을 다하는 Challengers가 되겠습니다.<br />";
            body += "감사합니다.</p>";
            body += "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>" + number + "</div>";
            body += "</body></html>";
            messageHelper.setText(body, true);
            ClassPathResource image = new ClassPathResource("img/challengers.png");
            messageHelper.addInline("image", image);
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return mimeMessage;
    }
    @Override
    public String sendMail(String email) {
        MimeMessage mimeMessage = createMessage(email);
        log.info("[Mail 전송 시작]");
        javaMailSender.send(mimeMessage);
        log.info("[Mail 전송 완료]");
        return String.valueOf(number);
    }

    @Override
    public MimeMessage createPwMessage(String email) {
        createNumber();
        log.info("Number : {}",number);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("[Challengers] 비밀번호 변경 이메일 인증 번호 발송");

            String body = "<html><body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff;'>";
            body += "<img class='logo' src='cid:image'>";
            body += "<h1 style='padding-top: 50px; font-size: 30px;'>이메일 주소 인증</h1>";
            body += "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? Challengers 관리자 입니다.<br />";
            body += "Challengers 서비스 내 사용자의 비밀번호 변경을 위해 회원가입시 고객님께서 입력하신 이메일 주소의 인증이 필요합니다.<br />";
            body += "하단의 인증 번호로 이메일 인증을 완료하시면, 정상적으로 Challengers 서비스 비밀번호를 변경 하실 수 있습니다.<br />";
            body += "항상 최선의 노력을 다하는 Challengers가 되겠습니다.<br />";
            body += "감사합니다.</p>";
            body += "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>" + number + "</div>";
            body += "</body></html>";
            messageHelper.setText(body, true);
            ClassPathResource image = new ClassPathResource("img/challengers.png");
            messageHelper.addInline("image", image);
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return mimeMessage;
    }

    @Override
    public String sendPwMail(String email) {
        MimeMessage mimeMessage = createPwMessage(email);
        log.info("[Mail 전송 시작]");
        javaMailSender.send(mimeMessage);
        log.info("[Mail 전송 완료]");
        return String.valueOf(number);
    }
}
