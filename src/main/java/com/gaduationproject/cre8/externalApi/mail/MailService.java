package com.gaduationproject.cre8.externalApi.mail;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.InternalServerErrorException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String mailSendFrom = "dionisos198@naver.com";
    private static final String encodingType = "utf-8";

    public void sendMail(final String email, final String title,final String contents){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            setMiMeMessageHelperForMailFormat(message,email,title,contents);
            javaMailSender.send(message);
        }
        catch (Exception e){
            throw new InternalServerErrorException(ErrorCode.MAIL_SERVER_ERROR);
        }
    }

    public void setMiMeMessageHelperForMailFormat(final MimeMessage message,final String email,final String title,final String contents)
            throws MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(message,true,encodingType);
        helper.setFrom(mailSendFrom);
        helper.setTo(email);
        helper.setSubject(title);
        helper.setText(contents,true);

    }



}
