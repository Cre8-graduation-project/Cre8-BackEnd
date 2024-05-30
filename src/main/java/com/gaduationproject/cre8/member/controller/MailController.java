package com.gaduationproject.cre8.member.controller;

import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.member.dto.EmailCheckRequestDto;
import com.gaduationproject.cre8.member.dto.EmailCheckResponseDto;
import com.gaduationproject.cre8.member.dto.EmailRequestDto;
import com.gaduationproject.cre8.member.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailSendService mailService;

    @PostMapping
    public void mailSend(@RequestBody @Valid final EmailRequestDto emailDto) {

         mailService.sendMail(emailDto);
    }

    @GetMapping("/check")
    public ResponseEntity<BaseResponse<EmailCheckResponseDto>> AuthCheck(@RequestBody @Valid final EmailCheckRequestDto emailCheckRequestDto){

        return ResponseEntity.ok(BaseResponse.createSuccess(mailService.checkAuthNum(emailCheckRequestDto)));
    }

}