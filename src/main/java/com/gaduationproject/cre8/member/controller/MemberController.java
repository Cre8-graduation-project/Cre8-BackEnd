package com.gaduationproject.cre8.member.controller;

import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public ResponseEntity<Void> saveMember(@Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto){
        memberSignUpService.saveMember(memberSignUpRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
