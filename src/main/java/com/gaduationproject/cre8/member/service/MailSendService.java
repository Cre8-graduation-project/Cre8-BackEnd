package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.common.response.error.exception.InternalServerErrorException;
import com.gaduationproject.cre8.member.dto.EmailCheckAuthNumRequestDto;
import com.gaduationproject.cre8.member.dto.EmailCheckAuthNumResponseDto;
import com.gaduationproject.cre8.member.dto.EmailRequestDto;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailSendService {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String,String> redisTemplate;
    private final MemberRepository memberRepository;

    private static final String mailSendFrom = "dionisos198@naver.com";
    private static final String mailTitle = "안녕하세요. cre8입니다";
    private static final String mailContentsAuthNumberBefore = "cre8에 오신 것을 환영합니다.<br><br> 인증번호는 ";
    private static final String mailContentsAuthNumberAfter = " 입니다. <br>인증번호를 입력해주세요!";

    private static final String encodingType = "utf-8";

    private static long CAN_INPUT_TIME_AFTER_SEND_EMAIL=60*5L;




    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public void sendMail(final EmailRequestDto emailRequestDto)  {

        if(memberRepository.existsByEmail(emailRequestDto.getEmail())){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }

        String authNumber = makeRandomNumber();

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            setMiMeMessageHelperForMailFormat(message, emailRequestDto.getEmail(), authNumber);
            javaMailSender.send(message);
        }
        catch (Exception e){
            throw new InternalServerErrorException(ErrorCode.MAIL_SERVER_ERROR);
        }


        redisTemplate.opsForValue().set(emailRequestDto.getEmail(),authNumber,Duration.ofSeconds(CAN_INPUT_TIME_AFTER_SEND_EMAIL));

    }

    //임의의 6자리 양수를 반환합니다.
    private String makeRandomNumber() {

        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        return randomNumber;
    }

    public void setMiMeMessageHelperForMailFormat(final MimeMessage message,final String email,final String authNumber)
            throws MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(message,true,encodingType);
        helper.setFrom(mailSendFrom);
        helper.setTo(email);
        helper.setSubject(mailTitle);
        helper.setText(mailContentsAuthNumberBefore+authNumber+mailContentsAuthNumberAfter,true);
        
    }

    public EmailCheckAuthNumResponseDto checkAuthNum(final EmailCheckAuthNumRequestDto emailCheckAuthNumRequestDto){


        if(redisTemplate.opsForValue().get(emailCheckAuthNumRequestDto.getEmail())==null||
                !redisTemplate.opsForValue().get(emailCheckAuthNumRequestDto.getEmail()).equals(
                emailCheckAuthNumRequestDto.getAuthNum())){
            throw new BadRequestException(ErrorCode.NOT_VALIDATE_EMAIL_AUTH_NUMBER);
        }

        redisTemplate.delete(emailCheckAuthNumRequestDto.getEmail());

        return EmailCheckAuthNumResponseDto.builder().emailChecked(true).build();
    }

}