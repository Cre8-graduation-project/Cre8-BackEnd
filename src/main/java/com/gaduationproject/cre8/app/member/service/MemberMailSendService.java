package com.gaduationproject.cre8.app.member.service;

import com.gaduationproject.cre8.app.member.dto.LoginIdRequestDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.common.response.error.exception.InternalServerErrorException;
import com.gaduationproject.cre8.app.member.dto.EmailCheckAuthNumRequestDto;
import com.gaduationproject.cre8.app.member.dto.EmailCheckAuthNumResponseDto;
import com.gaduationproject.cre8.app.member.dto.EmailRequestDto;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.externalApi.mail.MailService;
import com.gaduationproject.cre8.externalApi.redis.service.RedisUtil;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberMailSendService {

    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String mailTitle = "안녕하세요. cre8입니다";
    private static final String mailContentsAuthNumberBefore = "cre8에 오신 것을 환영합니다.<br><br> 인증번호는 ";
    private static final String mailContentsAuthNumberAfter = " 입니다. <br>인증번호를 입력해주세요!";
    private static long CAN_INPUT_TIME_AFTER_SEND_EMAIL=60*5L;

    private static final String mailContentsTMPPasswordBefore = "임시 비밀번호는 <br><br>";

    private static final String mailContentsTMPPasswordAfter = "<br> 감사합니다";





    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public void sendMail(final EmailRequestDto emailRequestDto)  {

        if(memberRepository.existsByEmail(emailRequestDto.getEmail())){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }

        String authNumber = makeRandomNumber();
        mailService.sendMail(emailRequestDto.getEmail(),mailTitle,mailContentsAuthNumberBefore+authNumber+mailContentsAuthNumberAfter);


        redisUtil.saveAtRedis(emailRequestDto.getEmail(), authNumber,CAN_INPUT_TIME_AFTER_SEND_EMAIL,
                TimeUnit.SECONDS);

    }

    @Transactional
    public void sendTMPPassword(final LoginIdRequestDto loginIdRequestDto)  {

        Member member = memberRepository.findMemberByLoginId(loginIdRequestDto.getLoginId())
                .orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_MEMBER));

        checkValidateUser(loginIdRequestDto,member);

        String tmpPassword = generateRandomString(7);

        mailService.sendMail(member.getEmail(),mailTitle,mailContentsTMPPasswordBefore
                                                                 +tmpPassword
                                                                 +mailContentsTMPPasswordAfter);

        member.changePassword(passwordEncoder.encode(tmpPassword));
        member.changeStatusToTMPPassword();

    }

    private void checkValidateUser(final LoginIdRequestDto loginIdRequestDto, final Member member) {

        if(member.getEmail().equals(loginIdRequestDto.getEmail()) &&
           member.getLoginId().equals(loginIdRequestDto.getLoginId()) &&
           member.getName().equals(loginIdRequestDto.getName())){

            return;
        }

        throw new BadRequestException(ErrorCode.CANT_REQUEST_TMP_PW);
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

    private  String generateRandomString(int length) {
        // 생성할 문자열에 포함될 문자들
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // SecureRandom 객체 생성
        SecureRandom random = new SecureRandom();

        // 랜덤 문자열 생성
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public EmailCheckAuthNumResponseDto checkAuthNum(final EmailCheckAuthNumRequestDto emailCheckAuthNumRequestDto){


        if(redisUtil.getValueByKey(emailCheckAuthNumRequestDto.getEmail())==null||
                !redisUtil.getValueByKey(emailCheckAuthNumRequestDto.getEmail()).equals(
                emailCheckAuthNumRequestDto.getAuthNum())){
            throw new BadRequestException(ErrorCode.NOT_VALIDATE_EMAIL_AUTH_NUMBER);
        }

        redisUtil.deleteAtRedis(emailCheckAuthNumRequestDto.getEmail());

        return EmailCheckAuthNumResponseDto.builder().emailChecked(true).build();
    }

}